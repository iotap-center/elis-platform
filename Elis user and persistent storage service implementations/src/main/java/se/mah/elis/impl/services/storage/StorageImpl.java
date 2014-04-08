package se.mah.elis.impl.services.storage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.log.LogService;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.impl.services.storage.query.DeleteQuery;
import se.mah.elis.impl.services.storage.query.MySQLQueryTranslator;
import se.mah.elis.impl.services.storage.result.ResultSetImpl;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.Query;
import se.mah.elis.services.storage.query.QueryTranslator;
import se.mah.elis.services.storage.result.ResultSet;
import se.mah.elis.services.users.AbstractUser;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;

/**
 * The Elis reference implementation of the
 * {@link se.mah.elis.services.storage.Storage Storage} interface in the
 * Persistent Storage API.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@Component(name = "Elis Persistent Storage")
@Service(value=Storage.class)
public class StorageImpl implements Storage {
	
	private static final String SERVICE_PID = "se.mah.elis.services.storage.impl";
	private static final String DB_USER = SERVICE_PID + ".db.user";
	private static final String DB_PASS = SERVICE_PID + ".db.password";
	private static final String DB_NAME = SERVICE_PID + ".db.name";
	private static final String DB_HOST = SERVICE_PID + ".db.host";
	private static final String DB_PORT = SERVICE_PID + ".db.port";
	public static Dictionary<String, ?> properties;
	
	private boolean isInitialised = false;

	
	// The MySQL query translator
	private QueryTranslator translator;

	// The user factory
	@Reference
	private UserFactory userFactory;

	@Reference
	private ConfigurationAdmin configAdmin;
	
	// The database connection
	private Connection connection;
	
	// Some storage utilities
	private StorageUtils utils;
	
	@Reference
	private LogService log;
	
	// Some error messages
	private static String OBJECT_NOT_FOUND = "Couldn't find object in data store";
	private static String OBJECT_NOT_VALID = "Couldn't save this object";
	private static String USER_NOT_FOUND = "Couldn't find user in data store";
	private static String USER_NOT_VALID = "Couldn't save this user";
	private static String INSTANCE_OBJECT_ERROR = "Couldn't instantiate object";
	private static String INSTANCE_USER_ERROR = "Couldn't instantiate user";
	private static String STORAGE_ERROR = "Couldn't access storage";
	private static String DELETE_QUERY = "This storage engine requires a DeleteQuery.";

	/**
	 * Creates an instance of this class. It sets up a connection to a
	 * pre-defined database.
	 * 
	 * @since 1.0
	 */
	public StorageImpl() {
		translator = new MySQLQueryTranslator();
		connection = null;
		utils = null;
	}
	
	/**
	 * Creates an instance of this class with an already created database
	 * connection. This is mainly meant to be used for testing purposes.
	 * 
	 * @param connection A JDBC connection.
	 * @since 2.0
	 */
	public StorageImpl(Connection connection) {
		this.connection = connection;
		// TODO Replace the MySQL query translator with more generic stuff.
		translator = new MySQLQueryTranslator();
		utils = new StorageUtils(connection);
		isInitialised = true;
	}
	
	/**
	 * Creates an instance of this class with an already created database
	 * connection. This is mainly meant to be used for testing purposes.
	 * 
	 * @param connection A JDBC connection.
	 * @since 2.0
	 */
	public StorageImpl(Connection connection, UserFactory factory) {
		this.connection = connection;
		this.userFactory = factory;
		// TODO Replace the MySQL query translator with more generic stuff.
		translator = new MySQLQueryTranslator();
		utils = new StorageUtils(connection);
		isInitialised = true;
	}
	
	private void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost/elis?"
						+	"user=elis&password=notallthatsecret");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			log(LogService.LOG_ERROR, "Missing configuration");
		}
		// TODO Replace the MySQL query translator with more generic stuff.
		utils = new StorageUtils(connection);
		log(LogService.LOG_INFO, "Connecting as user ");
		isInitialised = true;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#insert(ElisDataObject) insert(ElisDataObject)}.
	 * 
	 * @param data The data object to be stored.
	 * @throws StorageException Thrown when the object couldn't be stored.
	 * @since 2.0
	 */
	@Override
	public void insert(ElisDataObject data) throws StorageException {
		insert(data, false);
	}
	
	/**
	 * This method is called by the public insert(ElisDataObject) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param data The data object to be stored.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the object couldn't be stored.
	 * @since 2.0
	 */
	private void insert(ElisDataObject data, boolean finalRun)
			throws StorageException {
		// TODO: It might be possible to create a version of this method that
		//		 also takes a PreparedStatement as a parameter, thereby
		//		 minimizing execution time and DB load.
		
		if (data != null && StorageUtils.validateEDOProperties(data.getProperties(), false)) {
			log(LogService.LOG_INFO, "Inserting data: " + data);
			
			PreparedStatement stmt = null;
			
			// Basically, this is a new object. Let's insert it.
			Properties props = null;
			UUID uuid = null;
			
			// Generate the table name
			String tableName = data.getClass().getCanonicalName();
			
			if (data.getDataId() == null) {
				// Generate the data id
				uuid = UUID.randomUUID();
				data.setDataId(uuid);
			} else {
				uuid = data.getDataId();
			}
			
			props = data.getProperties();
			
			if (StorageUtils.isEmpty(props)) {
				throw new StorageException();
			}
			
			// Create a query to be run as a prepared statement and do some
			// magic MySQL stuff to it.
			String query = "INSERT INTO `" + StorageUtils.mysqlifyName(tableName) +
					"` VALUES (" + StorageUtils.generateQMarks(data.getProperties()) + ");";
			
			// This will be used by the parameter loop below
			int i = 1;
			
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				stmt = connection.prepareStatement(query);
				
				// Add the parameters to the query. We don't know what we'll
				// run into. Better let someone else, i.e. addParameter() take
				// care of that for us.
				for (Entry<Object, Object> prop : props.entrySet()) {
					utils.addParameter(stmt, prop.getValue(), i++, false);
				}
				
				// Run the statement and end the transaction
				stmt.executeUpdate();
				connection.commit();
				
				utils.pairUUIDWithTable(uuid, tableName);
			} catch (SQLException e) {
				// Try to create a non-existing table, but only once.
				if (e.getErrorCode() == 1146 && !finalRun) {
					if (StorageUtils.validateEDOProperties(
							data.getPropertiesTemplate(), false)) {
						utils.createTableIfNotExisting(
								StorageUtils.mysqlifyName(tableName),
								data.getPropertiesTemplate());
						insert(data, true);
					} else {
						// Bad properties. Bail out.
						log(LogService.LOG_WARNING, STORAGE_ERROR + ": Bad template " +
								data.getPropertiesTemplate());
						throw new StorageException(STORAGE_ERROR);
					}
				} else {
					// Well, that didn't work too well. Give up and die.
					log(LogService.LOG_ERROR, STORAGE_ERROR + ": General error", e);
					throw new StorageException(STORAGE_ERROR);
				}
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
		} else {
			log(LogService.LOG_WARNING, "Failed to insert data");
			throw new StorageException();
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#insert(ElisDataObject[]) insert(ElisDataObject[])}.
	 * 
	 * @param data The data objects to be stored.
	 * @throws StorageException Thrown when the objects couldn't be stored.
	 * @since 2.0
	 */
	@Override
	public void insert(ElisDataObject[] data) throws StorageException {
		insert(data, false);
	}
	
	/**
	 * This method is called by the public insert(ElisDataObject[]) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param data The data objects to be stored.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the object couldn't be stored.
	 * @since 2.0
	 */
	private void insert(ElisDataObject[] data, boolean finalRun) throws StorageException {
		if (data != null && data.length > 0) {
			// Just delegate this to the insert(AbstractUser, false) method.
			for (ElisDataObject dataParticle : data) {
				insert(dataParticle, false);
			}
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#insert(AbstractUser) insert(AbstractUser)}.
	 * 
	 * @param user The user object to be stored.
	 * @throws StorageException Thrown when the object couldn't be stored.
	 * @since 2.0
	 */
	@Override
	public void insert(AbstractUser user) throws StorageException {
		insert(user, false);
	}

	/**
	 * This method is called by the public insert(AbstractUser) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param user The user object to be stored.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the object couldn't be stored.
	 * @since 2.0
	 */
	private void insert(AbstractUser user, boolean finalRun)
			throws StorageException {
		// TODO: It might be possible to create a version of this method that
		//		 also takes a PreparedStatement as a parameter, thereby
		//		 minimizing execution time and DB load.
		
		String query = null;
		String tableName = null;
		PreparedStatement stmt = null;
		
		if (user != null) {
			log(LogService.LOG_INFO, "Inserting user: " + user);
			
			if (user.getServiceName() == null ||
					user.getServiceName().isEmpty()) {
				log(LogService.LOG_WARNING, USER_NOT_VALID + ": No service name");
				throw new StorageException(USER_NOT_VALID);
			}
			
			// Platform users are handled differently from any other user type.
			if (user instanceof PlatformUser) {
				PlatformUser pu = (PlatformUser) user;
				PlatformUserIdentifier pid =
						(PlatformUserIdentifier) pu.getIdentifier();
				
				// First of all, let's make a sanity check of the user.
				if (pid == null || pid.isEmpty()) {
					log(LogService.LOG_WARNING, USER_NOT_VALID + ": Empty identifier");
					throw new StorageException(USER_NOT_VALID);
				}
				
				// Then let's convert potential nulls to at least empty strings
				if (pu.getFirstName() == null) {
					pu.setFirstName("");
				}
				if (pu.getLastName() == null) {
					pu.setLastName("");
				}
				if (pu.getEmail() == null) {
					pu.setEmail("");
				}
						
				
				// This is indeed a new object. Insert it.
				
				// Generate the table name and bestow MySQL magic onto it
				tableName = "se-mah-elis-services-users-PlatformUser";
				
				/*
				 * PlatformUser objects are stored as
				 * |int id|String username|String password|String first_name|
				 * 		String last_name|String e-mail|
				 */
				query = "INSERT INTO `" + tableName + "` VALUES "
					+	"(x?, ?, PASSWORD(?), ?, ?, ?, ?)";
				
				try {
					// Let's take command of the commit ship ourselves.
					// Forward, mateys!
					connection.setAutoCommit(false);
					stmt = connection.prepareStatement(query);
					
					// Has this user been stored before?
					if (pu.getUserId() == null) {
						pu.setUserId(UUID.randomUUID());
					}
					stmt.setString(1, StorageUtils.stripDashesFromUUID(pu.getUserId()));
					stmt.setString(2, pid.getUsername());
					stmt.setString(3, pid.getPassword());
					stmt.setString(4, pu.getFirstName());
					stmt.setString(5, pu.getLastName());
					stmt.setString(6, pu.getEmail());
					stmt.setTimestamp(7, new Timestamp(pu.created().getMillis()));
					stmt.executeUpdate();
					connection.commit();
					
					utils.pairUUIDWithTable(pu.getUserId(), tableName);
				} catch (SQLException e) {
					// This shouldn't happen. The table should be in place
					log(LogService.LOG_WARNING, STORAGE_ERROR + ": Table " + tableName + " not found", e);
					throw new StorageException(STORAGE_ERROR);
				} finally {
					try {
						stmt.close();
					} catch (SQLException e) {}
				}
			} else {
				// Just a generic User object.
				
				// Let's sanity check it first
				if (!StorageUtils.validateAbstractUserProperties(user.getProperties(), false)) {
					throw new IllegalArgumentException();
				}
				if (user.getIdentifier() == null ||
						StorageUtils.isEmpty(user.getIdentifier()
								.getProperties())) {
					throw new StorageException();
				}
				
				// This might be a new user. Insert it.
				UUID uuid = null;
				if (((User) user).getUserId() == null) {
					// Generate the UUID
					uuid = UUID.randomUUID();
					((User) user).setUserId(uuid);
				} else {
					uuid = ((User) user).getUserId();
				}
				
				// This will be used by the parameter loop below
				int i = 1;
				
				// Get the user properties
				Properties userProps = user.getProperties();
				
				// Flatten the user properties
				userProps = StorageUtils.flattenPropertiesWithIdentifier(userProps, false);
				
				// Generate the table name
				tableName = user.getClass().getCanonicalName();
				
				// Create a query to be run as a prepared statement and do
				// some magic MySQL stuff to it.
				query = "INSERT INTO `" + StorageUtils.mysqlifyName(tableName) + 
						"` VALUES (" + StorageUtils.generateQMarks(userProps) + ");";

				try {
					// Let's take command of the commit ship ourselves.
					// Forward, mateys!
					connection.setAutoCommit(false);
					stmt = connection.prepareStatement(query);
				
					// Add the parameters to the query. We don't know what
					// we'll run into. Better let someone else, i.e.
					// addParameter() take care of that for us.
					for (Entry<Object, Object> prop : userProps.entrySet()) {
						utils.addParameter(stmt, prop.getValue(), i++, false);
					}
					
					// Run the statement and end the transaction
					stmt.executeUpdate();
					connection.commit();
					
					utils.pairUUIDWithTable(uuid, tableName);
				} catch (SQLException e) {
					// Try to create a non-existing table, but only once.
					Properties propTemplate = user.getPropertiesTemplate();
					propTemplate = StorageUtils.flattenPropertiesWithIdentifier(propTemplate, true);
					
					if (e.getErrorCode() == 1146 && !finalRun) {
						if (StorageUtils.validateAbstractUserProperties(propTemplate, true)) {
							utils.createTableIfNotExisting(tableName,
									propTemplate);
							insert(user, true);
						}
					} else {
						// Well, that didn't work too well. Give up
						// and die.
						log(LogService.LOG_WARNING, STORAGE_ERROR + ": Table " + tableName + " not found");
						throw new StorageException(STORAGE_ERROR);
					}
				} finally {
					try {
						stmt.close();
					} catch (Exception e) {}
				}
			}
		} else {
			log(LogService.LOG_WARNING, "Failed to insert user");
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#insert(AbstractUser[]) insert(AbstractUser[])}.
	 * 
	 * @param users The user objects to be stored.
	 * @throws StorageException Thrown when the objects couldn't be stored.
	 * @since 2.0
	 */
	@Override
	public void insert(AbstractUser[] users) throws StorageException {
		insert(users, false);
	}
	
	/**
	 * This method is called by the public insert(AbstractUser[]) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param users The user objects to be stored.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the object couldn't be stored.
	 * @since 2.0
	 */
	private void insert(AbstractUser[] users, boolean finalRun)
			throws StorageException {
		if (users != null && users.length > 0) {
			// Just delegate this to the insert(AbstractUser, false) method.
			for (AbstractUser user : users) {
				insert(user, false);
			}
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#update(ElisDataObject) update(ElisDataObject)}.
	 * 
	 * @param data The data object to be stored.
	 * @throws StorageException Thrown when the object couldn't be stored.
	 * @since 2.0
	 */
	@Override
	public void update(ElisDataObject data) throws StorageException {
		update(data, false);
	}

	/**
	 * This method is called by the public update(ElisDataObject) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param data The data object to be updated.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the object couldn't be updated.
	 * @since 2.0
	 */
	private void update(ElisDataObject data, boolean finalRun) throws StorageException {
		// TODO: It might be possible to create a version of this method that
		//		 also takes a PreparedStatement as a parameter, thereby
		//		 minimizing execution time and DB load.
		
		PreparedStatement stmt = null;
		boolean updated = false;
		
		if (data != null) {
			log(LogService.LOG_INFO, "Updating data object: " + data);
			
			if (!StorageUtils.validateEDOProperties(data.getProperties(), true)) {
				log(LogService.LOG_WARNING, OBJECT_NOT_FOUND + ": " + data.toString());
				throw new StorageException(OBJECT_NOT_FOUND);
			}
			
			// Generate the table name
			String tableName = data.getClass().getCanonicalName();
			
			// Create a query to be run as a prepared statement and do some
			// magic MySQL stuff to it.
			Properties props = data.getProperties();
			String query = "UPDATE `" + StorageUtils.mysqlifyName(tableName) +
					"` SET " + StorageUtils.pairUp(data.getProperties()) +
					" WHERE dataid = x'" + 
					StorageUtils.stripDashesFromUUID(data.getDataId()) + "';";
			
			// This will be used by the parameter loop below
			int i = 1;
			
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				stmt = connection.prepareStatement(query);
				
				// Add the parameters to the query. We don't know what we'll
				// run into. Better let someone else, i.e. addParameter() take
				// care of that for us.
				for (Entry<Object, Object> prop : props.entrySet()) {
					utils.addParameter(stmt, prop.getValue(), i++, false);
				}
				
				// Run the statement and end the transaction
				updated = stmt.executeUpdate() > 0;
				connection.commit();
			} catch (SQLException e) {
				// Try to create a non-existing table, but only once.
				if (e.getErrorCode() == 1146 && !finalRun) {
					if (StorageUtils.validateEDOProperties(
							data.getPropertiesTemplate(), false)) {
						utils.createTableIfNotExisting(
								StorageUtils.mysqlifyName(tableName),
								data.getPropertiesTemplate());
						update(data, true);
					}
				} else {
					// Well, that didn't work too well. Give up and die.
					log(LogService.LOG_WARNING, STORAGE_ERROR + ": Couldn't find table " + tableName);
					throw new StorageException(STORAGE_ERROR);
				}
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
			
			if (!updated) {
				throw new StorageException(OBJECT_NOT_FOUND);
			}
		} else {
			log(LogService.LOG_WARNING, "Failed to update data object");
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#update(ElisDataObject[]) update(ElisDataObject[])}.
	 * 
	 * @param data The data objects to be updated.
	 * @throws StorageException Thrown when the objects couldn't be updated.
	 * @since 2.0
	 */
	@Override
	public void update(ElisDataObject[] data) throws StorageException {
		update(data, false);
	}

	/**
	 * This method is called by the public update(ElisDataObject[]) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param data The data objects to be stored.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the objects couldn't be stored.
	 * @since 2.0
	 */
	private void update(ElisDataObject[] data, boolean finalRun) throws StorageException {
		if (data != null && data.length > 0) {
			// Just delegate this to the insert(AbstractUser, false) method.
			for (ElisDataObject dataParticle : data) {
				update(dataParticle, false);
			}
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#update(AbstractUser) update(AbstractUser)}.
	 * 
	 * @param user The user object to be updated.
	 * @throws StorageException Thrown when the object couldn't be updated.
	 * @since 2.0
	 */
	@Override
	public void update(AbstractUser user) throws StorageException {
		update(user, false);
	}

	/**
	 * This method is called by the public update(AbstractUser) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param user The user object to be updated.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the object couldn't be updated.
	 * @since 2.0
	 */
	private void update(AbstractUser user, boolean finalRun) throws StorageException {
		// TODO: It might be possible to create a version of this method that
		//		 also takes a PreparedStatement as a parameter, thereby
		//		 minimizing execution time and DB load.
		
		PreparedStatement stmt = null;
		String query = null;
		String tableName = null;
		
		if (user != null) {
			log(LogService.LOG_INFO, "Updating user: " + user);
			
			if (user.getIdentifier() == null ||
				StorageUtils.isEmpty(user.getIdentifier().getProperties()) ||
				user.getUserId() == null) {
				log(LogService.LOG_WARNING, USER_NOT_FOUND + ": No identifier");
				throw new StorageException(USER_NOT_FOUND);
			}
			
			// Platform users are handled differently from any other user type.
			if (user instanceof PlatformUser) {
				PlatformUser pu = (PlatformUser) user;
				PlatformUserIdentifier pid =
						(PlatformUserIdentifier) pu.getIdentifier();
				
				if (pu.getUserId() == null ||
					pid.getUsername() == null || pid.getUsername().isEmpty()) {
					log(LogService.LOG_WARNING, USER_NOT_VALID + ": Missing identifier or username");
					throw new StorageException(USER_NOT_VALID);
				}
				
				// Generate the table name and bestow MySQL magic onto it
				tableName = StorageUtils.mysqlifyName("se.mah.elis.services.users.PlatformUser");
				
				/*
				 * PlatformUser objects are stored as
				 * |int id|String username|String password|String first_name|
				 * 		String last_name|String e-mail|
				 */
				if (pid.getPassword() == null || pid.getPassword().isEmpty()) {
					query = "UPDATE `" + tableName + "` SET " +
							"username = ?, " +
							"first_name = ?, last_name = ?, `email` = ? " + 
							"WHERE uuid = x'" +
							StorageUtils.stripDashesFromUUID(user.getUserId()) +
							"';";
				} else {
					// Only update the password if it is being changed
					query = "UPDATE `" + tableName + "` SET " +
							"username = ?, first_name = ?, " +
							"last_name = ?, `email` = ?, " +
							"`password` = PASSWORD(?) WHERE uuid = x'" +
							StorageUtils.stripDashesFromUUID(user.getUserId()) +
							"';";
				}
				
				try {
					// Let's take command of the commit ship ourselves.
					// Forward, mateys!
					connection.setAutoCommit(false);
					stmt = connection.prepareStatement(query);
					
					stmt.setString(1, pid.getUsername());
					stmt.setString(2, pu.getFirstName());
					stmt.setString(3, pu.getLastName());
					stmt.setString(4, pu.getEmail());
					if (pid.getPassword() != null && pid.getPassword().length() > 0) {
						// Only update the password if it is being changed
						stmt.setString(5, pid.getPassword());
					}
					stmt.executeUpdate();
					connection.commit();
				} catch (SQLException e) {
					// Try to create a non-existing table, but only once.
					if (e.getErrorCode() == 1146 && !finalRun) {
						// Flatten the user properties
						Properties template = new Properties();
						template.putAll(user.getIdentifier()
								.getPropertiesTemplate());
						template.putAll(user.getPropertiesTemplate());
						utils.createTableIfNotExisting(tableName,
								user.getPropertiesTemplate());
						insert(user, true);
					} else {
						// Well, that didn't work too well. Give up and die.
						log(LogService.LOG_WARNING, STORAGE_ERROR + ": Couldn't find table " + tableName, e);
						throw new StorageException(STORAGE_ERROR);
					}
				} finally {
					try {
						stmt.close();
					} catch (SQLException e) {}
				}
			} else {
				// Just a generic AbstractUser object.
				// First, let's see if we will be able to find and store it
				if (!StorageUtils.validateAbstractUserProperties(user.getProperties(), true)) {
					throw new IllegalArgumentException();
				}
				
				// Flatten the user properties
				Properties userProps = user.getProperties();
				userProps = StorageUtils.flattenPropertiesWithIdentifier(userProps, false);
				
				// This will be used by the parameter loop below
				int i = 1;
				
				// Generate the table name
				tableName = user.getClass().getCanonicalName();
				
				// Create a query to be run as a prepared statement and do
				// some magic MySQL stuff to it.
				query = "UPDATE `" + StorageUtils.mysqlifyName(tableName) +
						"` SET " + StorageUtils.pairUp(userProps) +
						" WHERE uuid = x'" +
						StorageUtils.stripDashesFromUUID(((User) user).getUserId()) + "';";

				try {
					// Let's take command of the commit ship ourselves.
					// Forward, mateys!
					connection.setAutoCommit(false);
					stmt = connection.prepareStatement(query);
				
					// Add the parameters to the query. We don't know what
					// we'll run into. Better let someone else, i.e.
					// addParameter() take care of that for us.
					for (Entry<Object, Object> prop : userProps.entrySet()) {
						utils.addParameter(stmt, prop.getValue(), i++, false);
					}
					
					// Run the statement and end the transaction
					stmt.executeUpdate();
				} catch (SQLException e) {
					// Try to create a non-existing table, but only once.
					if (e.getErrorCode() == 1146 && !finalRun) {
						// Flatten the user properties
						Properties template = new Properties();
						template = StorageUtils.flattenPropertiesWithIdentifier(template, true);
						if (StorageUtils.validateAbstractUserProperties(template, true)) {
							utils.createTableIfNotExisting(tableName,
									user.getPropertiesTemplate());
							update(user, true);
						}
					} else {
						// Well, that didn't work too well. Give up and die.
						log(LogService.LOG_WARNING, STORAGE_ERROR + ": Couldn't find table " + tableName, e);
						throw new StorageException(STORAGE_ERROR);
					}
				} finally {
					try {
						stmt.close();
					} catch (SQLException e) {}
				}
			}
		} else {
			log(LogService.LOG_WARNING, "Failed to update user");
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#update(AbstractUser[]) update(AbstractUser[])}.
	 * 
	 * @param users The user objects to be updated.
	 * @throws StorageException Thrown when the objects couldn't be updated.
	 * @since 2.0
	 */
	@Override
	public void update(AbstractUser[] users) throws StorageException {
		update(users, false);
	}

	/**
	 * This method is called by the public update(AbstractUser[]) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param users The user objects to be updated.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the objects couldn't be updated.
	 * @since 2.0
	 */
	private void update(AbstractUser[] users, boolean finalRun) throws StorageException {
		if (users != null && users.length > 0) {
			// Just delegate this to the insert(AbstractUser, false) method.
			for (AbstractUser user : users) {
				if (user != null)
					update(user, false);
			}
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#delete(ElisDataObject) update(ElisDataObject)}.
	 * 
	 * @param data The data object to be deleted.
	 * @throws StorageException Thrown when the object couldn't be deleted.
	 * @since 2.0
	 */
	@Override
	public void delete(ElisDataObject data) throws StorageException {
		delete(data, false);
	}
	
	/**
	 * This method is called by the public delete(ElisDataObject) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param data The data object to be deleted.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the object couldn't be deleted.
	 * @since 2.0
	 */
	private void delete(ElisDataObject data, boolean finalRun) throws StorageException {
		if (data != null) {
			log(LogService.LOG_INFO, "Deleting data: " + data);
			
			// TODO: It might be possible to create a version of this method that
			//		 also takes a PreparedStatement as a parameter, thereby
			//		 minimizing execution time and DB load.
			
			PreparedStatement stmt = null;
			
			// Generate the table name
			String tableName = data.getClass().getCanonicalName();
			
			// Create a query to be run as a prepared statement and do some
			// magic MySQL stuff to it.
			UUID uuid = (UUID) data.getProperties().get("dataid");
			String query = "DELETE FROM `" + StorageUtils.mysqlifyName(tableName) +
					"` WHERE dataid = x'" +
					StorageUtils.stripDashesFromUUID(uuid) + "';";
			
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				stmt = connection.prepareStatement(query);
				
				// Run the statement and end the transaction
				if (stmt.executeUpdate() > 0) {
					utils.freeUUID(uuid);
				}
				connection.commit();
			} catch (SQLException e) {
				log(LogService.LOG_WARNING, STORAGE_ERROR + ": Couldn't delete object " + data, e);
				throw new StorageException(STORAGE_ERROR);
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
		} else {
			log(LogService.LOG_WARNING, "Failed to delete data object");
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#delete(ElisDataObject[]) update(ElisDataObject[])}.
	 * 
	 * @param data The data objects to be deleted.
	 * @throws StorageException Thrown when the objects couldn't be deleted.
	 * @since 2.0
	 */
	@Override
	public void delete(ElisDataObject[] data) throws StorageException {
		delete(data, false);
	}

	/**
	 * This method is called by the public delete(ElisDataObject[]) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param data The data objects to be deleted.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the objects couldn't be deleted.
	 * @since 2.0
	 */
	private void delete(ElisDataObject[] data, boolean finalRun) throws StorageException {
		if (data != null && data.length > 0) {
			// Just delegate this to the insert(AbstractUser, false) method.
			for (ElisDataObject dataParticle : data) {
				delete(dataParticle, false);
			}
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#delete(AbstractUser) update(AbstractUser)}.
	 * 
	 * @param user The user object to be deleted.
	 * @throws StorageException Thrown when the object couldn't be deleted.
	 * @since 2.0
	 */
	@Override
	public void delete(AbstractUser user) throws StorageException {
		delete(user, false);
	}

	/**
	 * This method is called by the public delete(ElisDataObject[]) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param user The user object to be deleted.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the user couldn't be deleted.
	 * @since 2.0
	 */
	private void delete(AbstractUser user, boolean finalRun) throws StorageException {
		// TODO: It might be possible to create a version of this method that
		//		 also takes a PreparedStatement as a parameter, thereby
		//		 minimizing execution time and DB load.
		
		log(LogService.LOG_INFO, "Deleting user: " + user);
		
		if (user != null && user.getUserId() != null) {
			PreparedStatement stmt = null;
			String query = null;
			UUID uuid = user.getUserId();
			String tableName = null;
			
			if (user instanceof PlatformUser) {
				// Create a query to be run and do some MySQL stuff to it.
				tableName = "se-mah-elis-services-users-PlatformUser";
				query = "DELETE FROM `" + tableName + "` WHERE uuid = x'" +
						StorageUtils.stripDashesFromUUID(uuid) + "';";
			} else {
				// Generate the table name
				tableName = user.getClass().getCanonicalName();
				// Create a query to be run and do some MySQL stuff to it.
				query = "DELETE FROM `" + StorageUtils.mysqlifyName(tableName) +
						"` WHERE uuid = x'" +
						StorageUtils.stripDashesFromUUID(uuid) + "';";
			}
			
			try {
				// Run the statement and end the transaction
				stmt = connection.prepareStatement(query);
				if (stmt.executeUpdate() > 0) {
					utils.freeUUID(uuid);
				}
			} catch (SQLException e) {
				log(LogService.LOG_WARNING, STORAGE_ERROR + ": Couldn't delete user " + user, e);
				throw new StorageException(STORAGE_ERROR);
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#delete(AbstractUser[]) update(AbstractUser[])}.
	 * 
	 * @param user The user objects to be deleted.
	 * @throws StorageException Thrown when the objects couldn't be deleted.
	 * @since 2.0
	 */
	@Override
	public void delete(AbstractUser[] users) throws StorageException {
		delete(users, false);
	}

	/**
	 * This method is called by the public delete(AbstractUser[]) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param users The user objects to be deleted.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the users couldn't be deleted.
	 * @since 2.0
	 */
	private void delete(AbstractUser[] users, boolean finalRun) throws StorageException {
		if (users != null && users.length > 0) {
			// Just delegate this to the insert(AbstractUser, false) method.
			for (AbstractUser user : users) {
				delete(user, false);
			}
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#delete(Query) update(Query)}.
	 * 
	 * @param query The query to be run.
	 * @throws StorageException Thrown when query couldn't be run.
	 * @since 2.0
	 */
	@Override
	public void delete(Query query) throws StorageException {
		delete(query, false);
	}

	/**
	 * This method is called by the public delete(Query) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param query The query to be run.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the query couldn't be run.
	 * @since 2.0
	 */
	private void delete(Query query, boolean finalRun) throws StorageException {
		if (query != null) {
			Statement stmt = null;
			
			try {
				DeleteQuery dq = new DeleteQuery(query);
				
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				stmt = connection.createStatement();
				stmt.execute(dq.compile());
				connection.commit();
			} catch (SQLException e) {
				log(LogService.LOG_WARNING, STORAGE_ERROR + ": Couldn't run delete query", e);
				throw new StorageException(STORAGE_ERROR);
			} catch (ClassCastException e) {
				log(LogService.LOG_WARNING, DELETE_QUERY + ": Couldn't run delete query", e);
				throw new StorageException(DELETE_QUERY);
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#readData(UUID) readData(UUID)}.
	 * 
	 * @param UUID The object to be read.
	 * @return The data object we're looking for.
	 * @throws StorageException Thrown when object couldn't be read.
	 * @since 1.0
	 */
	@Override
	public ElisDataObject readData(UUID id) throws StorageException {
		return readData(id, false);
	}

	/**
	 * This method is called by the public readData(UUID) method.
	 * 
	 * @param UUID The object to be read.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @return The data object we're looking for.
	 * @throws StorageException Thrown when the object couldn't be read.
	 * @since 2.0
	 */
	private ElisDataObject readData(UUID id, boolean finalRun)
			throws StorageException {
		Statement stmt = null;
		java.sql.ResultSet rs = null;
		ElisDataObject edo = null;
		String tableName = utils.lookupUUIDTable(id);
		String className = null;
		String query = null;
		Properties props = null;
		
		log(LogService.LOG_INFO, "Reading data: " + id);
		
		if (tableName != null) {
			className = StorageUtils.demysqlifyName(tableName);
			try {
				edo = (ElisDataObject) Class.forName(className).newInstance();
				
				query = "SELECT * FROM `" + tableName +
						"` WHERE dataid = x'" +
						StorageUtils.stripDashesFromUUID(id) + "';";
				
				stmt = connection.createStatement();
				rs = stmt.executeQuery(query);
				props = utils.resultSetRowToProperties(rs);
				
				// Populate the object, then we're pretty much done.
				edo.populate(props);
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				log(LogService.LOG_WARNING, INSTANCE_OBJECT_ERROR + ": " + id, e);
				throw new StorageException(INSTANCE_OBJECT_ERROR);
			} catch (SQLException e) {
				log(LogService.LOG_WARNING, OBJECT_NOT_FOUND + ": " + id, e);
				throw new StorageException(OBJECT_NOT_FOUND);
			} finally {
				try {
					rs.close();
					stmt.close();
				} catch (SQLException e) {}
			}
		}
		
		return edo;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#readData(ElisDataObject) readData(ElisDataObject)}.
	 * 
	 * @param edo The object to be read.
	 * @return The data object we're looking for.
	 * @throws StorageException if the data object wasn't found.
	 * @since 2.0
	 */
	@Override
	public ElisDataObject readData(ElisDataObject edo) throws StorageException {
		if (edo.getDataId() == null) {
			log(LogService.LOG_WARNING, OBJECT_NOT_VALID + ": " + edo);
			throw new StorageException(OBJECT_NOT_VALID);
		}
		
		return readData(edo.getDataId());
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#readUser(UserIdentifier) readUser(UserIdentifier)}.
	 * 
	 * @param id The unique data id.
	 * @return A User object.
	 * @throws StorageException if the user wasn't found.
	 * @since 2.0
	 */
	@Override
	public AbstractUser readUser(UUID id) throws StorageException {
		return readUser(id, false);
	}

	/**
	 * This method is called by the public readUser(UUID) method.
	 * 
	 * @param id The user to be read.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @return The user we're looking for.
	 * @throws StorageException Thrown when the user couldn't be read.
	 * @since 2.0
	 */
	private AbstractUser readUser(UUID id, boolean finalRun) throws StorageException {
		AbstractUser user = null;
		String tableName = utils.lookupUUIDTable(id);
		String className = null;
		Statement stmt = null;
		java.sql.ResultSet rs = null;
		
		log(LogService.LOG_INFO, "Reading user: " + id);
		
		if (tableName != null) {
			className = StorageUtils.demysqlifyName(tableName);
			String query = null;
			Properties props = null;
			
			query = "SELECT * FROM `" + tableName + "` WHERE uuid = x'" +
					StorageUtils.stripDashesFromUUID(id) + "';";
			
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				stmt = connection.createStatement();
				rs = stmt.executeQuery(query);
				props = utils.resultSetRowToProperties(rs);
				connection.commit();
				
				if (props.containsKey("service_name")) {
					// Not a platform user
					user = userFactory.build(className.substring(className.lastIndexOf('.')+1),
							(String) props.get("service_name"), props);
				} else if (className.equals("se.mah.elis.services.users.PlatformUser")) {
					// A platform user
					user = userFactory.build(props);
					((PlatformUserIdentifier) user.getIdentifier()).setPassword(null);
				} else {
					throw new StorageException(INSTANCE_USER_ERROR);
				}
			} catch (SQLException e) {
				log(LogService.LOG_WARNING, USER_NOT_FOUND + ": " + id.toString(), e);
				throw new StorageException(USER_NOT_FOUND);
			} catch (UserInitalizationException e) {
				log(LogService.LOG_WARNING, INSTANCE_USER_ERROR + ": " + id, e);
				throw new StorageException(INSTANCE_USER_ERROR);
			} catch (NullPointerException e) {
				// This is not a User object.
				log(LogService.LOG_WARNING, INSTANCE_USER_ERROR + ": Not a user " + id, e);
				throw new StorageException(INSTANCE_USER_ERROR);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					stmt.close();
				} catch (SQLException e) {}
			}
		}
		
		return user;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#readUser(UserIdentifier) readUser(UserIdentifier)}.
	 * 
	 * @param UserIdentifier The user to be read.
	 * @return The user we're looking for.
	 * @throws StorageException Thrown when user couldn't be read.
	 * @since 1.0
	 */
	@Override
	public AbstractUser readUser(UserIdentifier id) throws StorageException {
		return readUser(id, false);
	}

	/**
	 * This method is called by the public readUser(UserIdentifier) method.
	 * 
	 * @param UserIdentifier The user to be read.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @return The user we're looking for.
	 * @throws StorageException Thrown when the user couldn't be read.
	 * @since 2.0
	 */
	private AbstractUser readUser(UserIdentifier id, boolean finalRun)
			throws StorageException {
		AbstractUser user = null;
		java.sql.ResultSet rs = null;
		PreparedStatement stmt = null;
		Class<?> clazz = id.identifies();
		String className = clazz.getName();
		String tableName = StorageUtils.mysqlifyName(className);
		String query = null;
		
		log(LogService.LOG_INFO, "Reading user: " + id);

		if (clazz == se.mah.elis.services.users.PlatformUser.class) {
			// This is a platform user.
			String username = ((PlatformUserIdentifier) id).getUsername();
			String password = ((PlatformUserIdentifier) id).getPassword();
			Properties props = null;
			
			query = "SELECT * FROM `" + tableName + "` WHERE ";
			if (!username.isEmpty()) {
				query += "username = ? AND password = PASSWORD(?);";
			}
			
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				stmt = connection.prepareStatement(query);
				
				stmt.setString(1, username);
				stmt.setString(2, password);
				
				rs = stmt.executeQuery();
				props = utils.resultSetRowToProperties(rs);
				connection.commit();
				
				// Create a PlatformUser object
				user = userFactory.build(props);
				((PlatformUserIdentifier) user.getIdentifier()).setPassword(null);
			} catch (SQLException | NullPointerException e) {
				log(LogService.LOG_WARNING, USER_NOT_FOUND + ": " + id.toString(), e);
				throw new StorageException(USER_NOT_FOUND);
			} catch (UserInitalizationException e) {
				log(LogService.LOG_WARNING, INSTANCE_USER_ERROR + ": " + id, e);
				throw new StorageException(INSTANCE_USER_ERROR);
			} finally {
				try {
					rs.close();
					stmt.close();
				} catch (SQLException e) {}
			}
		} else {
			stmt = null;
			rs = null;
			
			// This is a generic user.
			String userType = id.identifies().getSimpleName();
			Properties props = null;
			
			// We'll run this as a prepared statement, since we don't know what
			// we'll run into.
			query = "SELECT * FROM `" + tableName + "` WHERE " +
					StorageUtils.pairUpForSelect(id.getProperties(), false) +
					" ORDER BY uuid ASC;";
			
			try {
				stmt = connection.prepareStatement(query);
				
				int i = 1;
				for (Entry<Object, Object> entry : id.getProperties().entrySet()) {
					utils.addParameter(stmt, entry.getValue(), i++, false);
				}
				
				rs = stmt.executeQuery();
				
				props = utils.resultSetRowToProperties(rs);
				user = userFactory.build(userType,
						(String) props.get("service_name"), props);
			} catch (SQLException | NullPointerException e) {
				log(LogService.LOG_WARNING, USER_NOT_FOUND + ": " + id.toString(), e);
				throw new StorageException(USER_NOT_FOUND);
			} catch (UserInitalizationException e) {
				log(LogService.LOG_WARNING, INSTANCE_USER_ERROR + ": " + id, e);
				throw new StorageException(INSTANCE_USER_ERROR);
			} finally {
				try {
					rs.close();
					stmt.close();
				} catch (SQLException e) {}
			}
		}
		
		return user;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#readUser(AbstractUser) readUser(AbstractUser)}.
	 * 
	 * @param UserIdentifier The user to be read.
	 * @throws StorageException Thrown when user couldn't be read.
	 * @since 2.0
	 */
	@Override
	public AbstractUser readUser(AbstractUser user) throws StorageException {
		return readUser(user, false);
	}

	/**
	 * This method is called by the public readUser(UserIdentifier) method.
	 * 
	 * @param UserIdentifier The user to be read.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @throws StorageException Thrown when the user couldn't be read.
	 * @since 2.0
	 */
	private AbstractUser readUser(AbstractUser user, boolean finalRun)
			throws StorageException {
		if (user != null) {
			log(LogService.LOG_INFO, "Reading user: " + user);
			
			// Generate the table name
			String tableName = user.getClass().getCanonicalName();
			
			Statement stmt = null;
			String query = null;
			String id = null;
			
			if (user instanceof User) {
				id = StorageUtils.stripDashesFromUUID(((User) user).getUserId());
				query = "SELECT * FROM `" + StorageUtils.mysqlifyName(tableName) +
						"` WHERE uuid = x'" + id + "';";
			}

			// Build query from identifier
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				stmt = connection.createStatement();
				Properties props =
						utils.resultSetRowToProperties(stmt.executeQuery(query));
				user.populate(props);
				connection.commit();
			} catch (SQLException | NullPointerException e) {
				// No user type
				log(LogService.LOG_WARNING, STORAGE_ERROR + ": " + user, e);
				throw new StorageException(STORAGE_ERROR);
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
		} else {
			log(LogService.LOG_WARNING, "Failed to read user");
		}
		
		return user;
	}

	@Override
	public AbstractUser[] readUsers(Class userType, Properties criteria) {
		ArrayList<AbstractUser> users = new ArrayList<AbstractUser>();
		String tableName = StorageUtils.mysqlifyName(userType.getName());
		PreparedStatement stmt = null;
		String query = null;
		int i = 1;
		
		log(LogService.LOG_INFO, "Reading users: " + userType + ", " + criteria);
		
		// First of all, remove any attempts to filter on "password"
		criteria.remove("password");
		
		// Next, let's build the select query
		query = "SELECT * FROM `" + tableName + "`";
		if (criteria.size() > 0) {
			query += " WHERE " + StorageUtils.pairUpForSelect(criteria, true);
		}
		query += " ORDER BY `created` ASC;";
		
		try {
			// Let's take command of the commit ship ourselves.
			// Forward, mateys!
			connection.setAutoCommit(false);
			stmt = connection.prepareStatement(query);
		
			// Add the parameters to the query. We don't know what
			// we'll run into. Better let someone else, i.e.
			// addParameter() take care of that for us.
			for (Entry<Object, Object> prop : criteria.entrySet()) {
				utils.addParameter(stmt, prop.getValue(), i++, true);
			}
			
			// Run the statement and create the new users;
			java.sql.ResultSet rs = stmt.executeQuery();
			ArrayList<Properties> props = null;
			
			props = utils.resultSetToProperties(rs);
			for (i = 0; i < props.size(); i++) {
				AbstractUser user = (AbstractUser) userType.newInstance();
				user.populate((Properties) props.get(i));
				users.add(user);
			}

			connection.commit();
		} catch (SQLException | InstantiationException | IllegalAccessException e) {
			// Well, that didn't work too well. Just return an empty array,
			// i.e. do nothing here.
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {}
		}
		
		
		return users.toArray(new AbstractUser[0]);
	}

	@Override
	public PlatformUser[] readPlatformUsers(Properties criteria) {
		ArrayList<PlatformUser> users = new ArrayList<PlatformUser>();
		PreparedStatement stmt = null;
		String query = null;
		int i = 1;
		
		log(LogService.LOG_INFO, "Reading platform users: " + criteria);
		
		// First of all, remove any attempts to filter on "password"
		criteria.remove("password");
		
		// Next, let's build the select query
		query = "SELECT * FROM `se-mah-elis-services-users-PlatformUser`";
		if (criteria.size() > 0) {
			query += " WHERE " + StorageUtils.pairUpForSelect(criteria, true);
		}
		query += " ORDER BY `created` ASC;";
		
		try {
			// Let's take command of the commit ship ourselves.
			// Forward, mateys!
			connection.setAutoCommit(false);
			stmt = connection.prepareStatement(query);
		
			// Add the parameters to the query. We don't know what
			// we'll run into. Better let someone else, i.e.
			// addParameter() take care of that for us.
			for (Entry<Object, Object> prop : criteria.entrySet()) {
				utils.addParameter(stmt, prop.getValue(), i++, true);
			}
			
			// Run the statement and create the new users;
			java.sql.ResultSet rs = stmt.executeQuery();
			ArrayList<Properties> props = null;
			
			props = utils.resultSetToProperties(rs);
			for (i = 0; i < props.size(); i++) {
				users.add((PlatformUser) userFactory.build((Properties) props.get(i)));
			}

			connection.commit();
		} catch (SQLException | UserInitalizationException e) {
			// Well, that didn't work too well. Just return an empty array,
			// i.e. do nothing here.
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {}
		}
		
		return (PlatformUser[]) users.toArray(new PlatformUser[0]);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.Storage#select(Query) select(Query)}.
	 * 
	 * @param Query The query to be run.
	 * @throws StorageException Thrown when user couldn't be read.
	 * @since 1.0
	 */
	@Override
	public ResultSet select(Query query) throws StorageException {
		return select(query, false);
	}

	/**
	 * This method is called by the public select(Query) method. It
	 * tries to create the table needed to store the data object if it doesn't
	 * already exist. However, it will only try once. If it doesn't succeed on
	 * its first try, it will stop trying by throwing an exception.
	 * 
	 * @param Query The query to be run.
	 * @param finalRun True if this method has been run before. Any method
	 * 		which isn't in fact this very method should call the method by
	 * 		setting this parameter to false.
	 * @return A ResultSet object containing all found values.
	 * @throws StorageException Thrown when the query couldn't be run.
	 * @since 2.0
	 */
	private ResultSet select(Query query, boolean finalRun) throws StorageException {
		ResultSet result = null;
		Class<?> clazz = query.getDataType();
		
		if (query != null) {
			Statement stmt = null;
			java.sql.ResultSet rs = null;
			query.setTranslator(new MySQLQueryTranslator());
			
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				stmt = connection.createStatement();
				
				// Fetch the results and convert them to readable objects.
				rs = stmt.executeQuery(query.compile());
				ArrayList<Properties> propList = utils.resultSetToProperties(rs);
				ArrayList<Object> objs = new ArrayList<Object>();
				Object instance = null;
				
				// Let's convert the raw data to ElisDataObject or AbstractUser
				// objects. That will be fun.
				for (Properties props : propList) {
					instance = clazz.newInstance();
					
					// Here, we'll try to populate the created objects.
					if (instance instanceof ElisDataObject) {
						((ElisDataObject) instance).populate(props);
					} else if (instance instanceof AbstractUser) {
						((AbstractUser) instance).populate(props);
					} else {
						// Well, this was awkward. We don't know what this is.
						// Rather than trying to guess and potentially destroy
						// things, just give up and die.
						stmt.close();
						log(LogService.LOG_WARNING, INSTANCE_OBJECT_ERROR);
						throw new StorageException(INSTANCE_OBJECT_ERROR);
					}
					objs.add(props);
				}
				
				// Close the database stuff gracefully
				connection.commit();
				
				// Aaand we're done. Finish this up, then move on.
				result = new ResultSetImpl(clazz, objs.toArray());
			} catch (SQLException | IllegalAccessException e) {
				log(LogService.LOG_WARNING, STORAGE_ERROR, e);
				throw new StorageException(STORAGE_ERROR);
			} catch (InstantiationException e) {
				log(LogService.LOG_WARNING, INSTANCE_OBJECT_ERROR, e);
				throw new StorageException(INSTANCE_OBJECT_ERROR);
			} finally {
				try {
					rs.close();
					stmt.close();
				} catch (SQLException e) {}
			}
		}
				
		return result;
	}
	
	private void log(int level, String message, Throwable t) {
		if (log != null) {
			log.log(level, "StorageImpl: " + message, t);
		}
	}
	
	private void log(int level, String message) {
		if (log != null) {
			log.log(level, "StorageImpl: " + message);
		}
	}

	protected void bindUserFactory(UserFactory uf) {
		this.userFactory = uf;
	}

	protected void unbindUserFactory(UserFactory uf) {
		this.userFactory = null;
	}
	
	protected void bindLog(LogService log) {
		this.log = log;
	}
	
	protected void unbindLog(LogService log) {
		this.log = null;
	}

	protected void bindConfigAdmin(ConfigurationAdmin ca) {
		configAdmin = ca;
		setConfig();
		if (!isInitialised)
			init();
	}

	protected void unbindConfigAdmin(ConfigurationAdmin ca) {
		configAdmin = null;
	}

	private void setConfig() {
		try {
			Configuration config = configAdmin.getConfiguration(SERVICE_PID);
			properties = config.getProperties();
			setDefaultConfiguration();
			config.update(properties);
		} catch (IOException e) {
			log(LogService.LOG_ERROR, "Failed to get configuration from Configuration Admin service.");
			setDefaultConfiguration();
		} finally {
			log(LogService.LOG_INFO, "Installed configuration");
		}
	}

	private void setDefaultConfiguration() {
		if (properties == null) {
			properties = getDefaultConfiguration();
			log(LogService.LOG_INFO, "Using default configuration.");
		}
	}

	private static Dictionary<String, ?> getDefaultConfiguration() {
		Dictionary<String, Object> props = new Hashtable<>();
		props.put(DB_USER, "elis_test");
		props.put(DB_PASS, "elis_test");
		props.put(DB_NAME, "elis_test");
		props.put(DB_HOST, "localhost");
		props.put(DB_PORT, "3306");
		return props;
	}
}
