package se.mah.elis.impl.services.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.impl.services.storage.exceptions.YouAreDoingItWrongException;
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
	
	// The MySQL query translator
	private QueryTranslator translator;

	// The user factory
	@Reference
	private UserFactory factory;
	
	// The database connection
	private Connection connection;
	
	// Some storage utilities
	private StorageUtils utils;
	
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
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// TODO Replace with non-static stuff later on
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost/elis?"
						+	"user=elis&password=notallthatsecret");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		// TODO Replace the MySQL query translator with more generic stuff.
		translator = new MySQLQueryTranslator();
		utils = new StorageUtils(connection);
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
		this.factory = factory;
		// TODO Replace the MySQL query translator with more generic stuff.
		translator = new MySQLQueryTranslator();
		utils = new StorageUtils(connection);
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
		
		if (data != null && !StorageUtils.isEmpty(data.getProperties())) {
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
			String query = "INSERT INTO `" + utils.mysqlifyName(tableName) +
					"` VALUES (" + utils.generateQMarks(data.getProperties()) + ");";
			
			// This will be used by the parameter loop below
			int i = 1;
			
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				PreparedStatement stmt = connection.prepareStatement(query);
				
				// Add the parameters to the query. We don't know what we'll
				// run into. Better let someone else, i.e. addParameter() take
				// care of that for us.
				for (Entry<Object, Object> prop : props.entrySet()) {
					utils.addParameter(stmt, prop.getValue(), i++, false);
				}
				
				// Run the statement and end the transaction
				stmt.executeUpdate();
				stmt.close();
				
				utils.pairUUIDWithTable(uuid, tableName);
			} catch (SQLException e) {
				// Try to create a non-existing table, but only once.
				if (e.getErrorCode() == 1146 && !finalRun) {
					utils.createTableIfNotExisting(
							utils.mysqlifyName(tableName),
							data.getPropertiesTemplate());
					insert(data, true);
				} else {
					// Well, that didn't work too well. Give up and die.
					throw new StorageException(STORAGE_ERROR);
				}
			}
		} else {
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
		Properties props = null;
		PreparedStatement stmt = null;
		java.sql.ResultSet keys = null;
		
		if (user != null) {
			if (user.getServiceName() == null ||
					user.getServiceName().isEmpty()) {
				throw new StorageException(USER_NOT_VALID);
			}
			
			// Platform users are handled differently from any other user type.
			if (user instanceof PlatformUser) {
				PlatformUser pu = (PlatformUser) user;
				PlatformUserIdentifier pid =
						(PlatformUserIdentifier) pu.getIdentifier();
				props = user.getProperties();
				
				// First of all, let's make a sanity check of the user.
				if (pid == null || pid.isEmpty()) {
					throw new StorageException(OBJECT_NOT_VALID);
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
					+	"(?, ?, PASSWORD(?), ?, ?, ?, ?)";
				
				try {
					// Let's take command of the commit ship ourselves.
					// Forward, mateys!
					connection.setAutoCommit(false);
					stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					
					// Has this user been stored before?
					if (pid.getId() > 0) {
						stmt.setInt(1, pid.getId());
					} else {
						stmt.setNull(1, Types.NULL);
					}
					stmt.setString(2, pid.getUsername());
					stmt.setString(3, pid.getPassword());
					stmt.setString(4, pu.getFirstName());
					stmt.setString(5, pu.getLastName());
					stmt.setString(6, pu.getEmail());
					stmt.setTimestamp(7, new Timestamp(pu.created().getMillis()));
					stmt.executeUpdate();
					keys = stmt.getGeneratedKeys();
					keys.next();
					pid.setId(keys.getInt(1));
					stmt.close();
				} catch (SQLException e) {
					// This shouldn't happen. The table should be in place
					throw new StorageException(STORAGE_ERROR);
				}
			} else {
				// Just a generic User object.
				
				// Let's sanity check it first
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
				
				props = user.getProperties();
				
				// This will be used by the parameter loop below
				int i = 1;
				
				// Get the user properties
				Properties userProps = user.getProperties();
				
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
					
					utils.pairUUIDWithTable(uuid, tableName);
				} catch (SQLException e) {
					// Try to create a non-existing table, but only once.
					if (e.getErrorCode() == 1146 && !finalRun) {
						utils.createTableIfNotExisting(tableName,
								user.getPropertiesTemplate());
						insert(user, true);
					} else {
						// Well, that didn't work too well. Give up
						// and die.
						throw new StorageException(STORAGE_ERROR);
					}
				} finally {
					try {
						stmt.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
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
		
		boolean updated = false;
		
		if (data != null) {
			if (data.getDataId() == null) {
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
				PreparedStatement stmt = connection.prepareStatement(query);
				
				// Add the parameters to the query. We don't know what we'll
				// run into. Better let someone else, i.e. addParameter() take
				// care of that for us.
				for (Entry<Object, Object> prop : props.entrySet()) {
					utils.addParameter(stmt, prop.getValue(), i++, false);
				}
				
				// Run the statement and end the transaction
				updated = stmt.executeUpdate() > 0;
				stmt.close();
			} catch (SQLException e) {
				// Try to create a non-existing table, but only once.
				if (e.getErrorCode() == 1146 && !finalRun) {
					utils.createTableIfNotExisting(
							StorageUtils.mysqlifyName(tableName),
							data.getPropertiesTemplate());
					update(data, true);
				} else {
					// Well, that didn't work too well. Give up and die.
					throw new StorageException(STORAGE_ERROR);
				}
			}
			
			if (!updated) {
				throw new StorageException(OBJECT_NOT_FOUND);
			}
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
		
		String query = null;
		String tableName = null;
		Properties props = user.getProperties();
		
		if (user != null && !StorageUtils.isEmpty(props)) {
			if (user.getIdentifier() == null ||
				StorageUtils.isEmpty(user.getIdentifier().getProperties())) {
				throw new StorageException(USER_NOT_FOUND);
			}
			
			// Platform users are handled differently from any other user type.
			if (user instanceof PlatformUser) {
				PlatformUser pu = (PlatformUser) user;
				PlatformUserIdentifier pid =
						(PlatformUserIdentifier) pu.getIdentifier();
				
				if (pid.getId() < 1 ||
					pid.getUsername() == null || pid.getUsername().isEmpty()) {
					throw new StorageException(USER_NOT_VALID);
				}
				
				// Generate the table name and bestow MySQL magic onto it
				tableName = utils.mysqlifyName("se.mah.elis.services.users.PlatformUser");
				
				/*
				 * PlatformUser objects are stored as
				 * |int id|String username|String password|String first_name|
				 * 		String last_name|String e-mail|
				 */
				if (pid.getPassword().isEmpty()) {
					query = "UPDATE `" + tableName + "` SET " +
							"username = ?, " +
							"first_name = ?, last_name = ?, `email` = ? " + 
							"WHERE id = " + pid.getId() + ";";
				} else {
					// Only update the password if it is being changed
					query = "UPDATE `" + tableName + "` SET " +
							"username = ?, first_name = ?, " +
							"last_name = ?, `email` = ?, " +
							"`password` = PASSWORD(?) WHERE id = " +
							pid.getId() + ";";
				}
				
				try {
					// Let's take command of the commit ship ourselves.
					// Forward, mateys!
					connection.setAutoCommit(false);
					PreparedStatement stmt = connection.prepareStatement(query);
					
					stmt.setString(1, pid.getUsername());
					stmt.setString(2, pu.getFirstName());
					stmt.setString(3, pu.getLastName());
					stmt.setString(4, pu.getEmail());
					if (pid.getPassword().length() > 0) {
						// Only update the password if it is being changed
						stmt.setString(5, pid.getPassword());
					}
					stmt.executeUpdate();
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
						throw new StorageException(STORAGE_ERROR);
					}
				}	
			} else {
				// Just a generic AbstractUser object.
				// First, let's see if we will be able to find it
				if (((User) user).getUserId() == null) {
					throw new StorageException(USER_NOT_FOUND);
				}
				
				// This will be used by the parameter loop below
				int i = 1;
				
				// Generate the table name
				tableName = user.getClass().getCanonicalName();
				
				// Create a query to be run as a prepared statement and do
				// some magic MySQL stuff to it.
				query = "UPDATE `" + utils.mysqlifyName(tableName) +
						"` SET " + utils.pairUp(user.getProperties()) +
						" WHERE uuid = x'" +
						StorageUtils.stripDashesFromUUID(((User) user).getUserId()) + "';";

				try {
					// Let's take command of the commit ship ourselves.
					// Forward, mateys!
					connection.setAutoCommit(false);
					PreparedStatement stmt = connection.prepareStatement(query);
				
					// Add the parameters to the query. We don't know what
					// we'll run into. Better let someone else, i.e.
					// addParameter() take care of that for us.
					for (Entry<Object, Object> prop : user.getProperties().entrySet()) {
						utils.addParameter(stmt, prop.getValue(), i++, false);
					}
					
					// Run the statement and end the transaction
					stmt.executeUpdate();
					stmt.close();
				} catch (SQLException e) {
					// Try to create a non-existing table, but only once.
					if (e.getErrorCode() == 1146 && !finalRun) {
						// Flatten the user properties
						Properties template = new Properties();
						template.putAll(user.getIdentifier()
								.getPropertiesTemplate());
						template.putAll(user.getPropertiesTemplate());
						template.remove("identifier");
						utils.createTableIfNotExisting(tableName,
								user.getPropertiesTemplate());
						update(user, true);
					} else {
						// Well, that didn't work too well. Give up and die.
						throw new StorageException(STORAGE_ERROR);
					}
				}
			}
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
			// TODO: It might be possible to create a version of this method that
			//		 also takes a PreparedStatement as a parameter, thereby
			//		 minimizing execution time and DB load.
			
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
				PreparedStatement stmt = connection.prepareStatement(query);
				
				// Run the statement and end the transaction
				if (stmt.executeUpdate() > 0) {
					utils.freeUUID(uuid);
				}
				stmt.close();
			} catch (SQLException e) {
				throw new StorageException(STORAGE_ERROR);
			}
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
		
		if (user != null) {
			String query = null;
			UUID uuid = null;
			String tableName = null;
			
			if (user instanceof PlatformUser) {
				// Create a query to be run and do some MySQL stuff to it.
				tableName = "se-mah-elis-services-users-PlatformUser";
				PlatformUserIdentifier pid =
						(PlatformUserIdentifier) ((PlatformUser) user).getIdentifier();
				query = "DELETE FROM `" + tableName + "` WHERE id = " +
						pid.getId()  + " AND username = '" +
						pid.getUsername() + "';";
			} else {
				// Generate the table name
				tableName = user.getClass().getCanonicalName();
				// Create a query to be run and do some MySQL stuff to it.
				uuid = (UUID) user.getProperties().get("uuid");
				query = "DELETE FROM `" + StorageUtils.mysqlifyName(tableName) +
						"` WHERE uuid = x'" +
						StorageUtils.stripDashesFromUUID(uuid) + "';";
			}
			
			try {
				// Run the statement and end the transaction
				PreparedStatement stmt = connection.prepareStatement(query);
				if (stmt.executeUpdate() > 0 && user instanceof User) {
					utils.freeUUID(uuid);
				}
				stmt.close();
			} catch (SQLException e) {
				throw new StorageException(STORAGE_ERROR);
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
			try {
				DeleteQuery dq = new DeleteQuery(query);
				
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				Statement stmt = connection.createStatement();
				stmt.execute(dq.compile());
				stmt.close();
			} catch (SQLException e) {
				throw new StorageException(STORAGE_ERROR);
			} catch (ClassCastException ce) {
				throw new StorageException(DELETE_QUERY);
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
		ElisDataObject edo = null;
		String tableName = utils.lookupUUIDTable(id);
		String className = null;
		String query = null;
		Properties props = null;
		
		if (tableName != null) {
			className = utils.demysqlifyName(tableName);
			try {
				edo = (ElisDataObject) Class.forName(className).newInstance();
				
				query = "SELECT * FROM `" + tableName +
						"` WHERE dataid = x'" +
						StorageUtils.stripDashesFromUUID(id) + "';";
				
				Statement stmt = connection.createStatement();
				java.sql.ResultSet rs = stmt.executeQuery(query);
				props = utils.resultSetRowToProperties(rs);
				rs.close();
				stmt.close();
				
				// Populate the object, then we're pretty much done.
				edo.populate(props);
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				throw new StorageException(INSTANCE_OBJECT_ERROR);
			} catch (SQLException e) {
				throw new StorageException(OBJECT_NOT_FOUND);
			}
		}
		
		return edo;
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
	public User readUser(UUID id) throws StorageException {
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
	private User readUser(UUID id, boolean finalRun) throws StorageException {
		User user = null;
		String tableName = utils.lookupUUIDTable(id);
		String className = null;
		
		if (tableName != null) {
			className = utils.demysqlifyName(tableName);
			String query = null;
			Properties props = null;
			
			query = "SELECT * FROM `" + tableName + "` WHERE uuid = x'" +
					StorageUtils.stripDashesFromUUID(id) + "';";
			
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				Statement stmt = connection.createStatement();
				java.sql.ResultSet rs = stmt.executeQuery(query);
				props = utils.resultSetRowToProperties(rs);
				rs.close();
				stmt.close();
				
				user = factory.build(className.substring(className.lastIndexOf('.')+1),
						(String) props.get("service_name"), props);
			} catch (SQLException e) {
				throw new StorageException(USER_NOT_FOUND);
			} catch (UserInitalizationException e) {
				throw new StorageException(INSTANCE_USER_ERROR);
			} catch (NullPointerException e) {
				// This is not a User object.
				throw new StorageException(INSTANCE_USER_ERROR);
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
		Class clazz = id.identifies();
		String className = clazz.getName();
		String tableName = utils.mysqlifyName(className);
		String query = null;

		if (clazz == se.mah.elis.services.users.PlatformUser.class) {
			// This is a platform user.
			String userType = "se.mah.elis.services.users.PlatformUser";
			String serviceName = userType;
			int puid = ((PlatformUserIdentifier) id).getId();
			String username = ((PlatformUserIdentifier) id).getUsername();
			String password = ((PlatformUserIdentifier) id).getPassword();
			Properties props = null;
			
			query = "SELECT * FROM `" + tableName + "` WHERE ";
			if (puid > 0) {
				query += "id = " + puid + " ";
			}
			if (!username.isEmpty()) {
				if (puid > 0) {
					query += "AND ";
				}
				query += "username = '" + username + "';";
			}
			
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				Statement stmt = connection.createStatement();
				java.sql.ResultSet rs = stmt.executeQuery(query);
				props = utils.resultSetRowToProperties(rs);
				rs.close();
				stmt.close();
				
				// Create a PlatformUser object
				user = factory.build(props);
			} catch (SQLException | NullPointerException e) {
				throw new StorageException(USER_NOT_FOUND);
			} catch (UserInitalizationException e) {
				throw new StorageException(INSTANCE_USER_ERROR);
			}
		} else {
			// This is a generic user.
			String userType = id.identifies().getSimpleName();
			Properties props = null;
			
			// We'll run this as a prepared statement, since we don't know what
			// we'll run into.
			query = "SELECT * FROM `" + tableName + "` WHERE " +
					utils.pairUpForSelect(id.getProperties(), false) +
					" ORDER BY uuid ASC;";
			
			try {
				PreparedStatement stmt = connection.prepareStatement(query);
				
				int i = 1;
				for (Entry<Object, Object> entry : id.getProperties().entrySet()) {
					utils.addParameter(stmt, entry.getValue(), i++, false);
				}
				
				java.sql.ResultSet rs = stmt.executeQuery();
				
				props = utils.resultSetRowToProperties(rs);
				rs.close();
				stmt.close();
				user = factory.build(userType,
						(String) props.get("service_name"), props);
			} catch (SQLException | NullPointerException e) {
				throw new StorageException(USER_NOT_FOUND);
			} catch (UserInitalizationException e) {
				throw new StorageException(INSTANCE_USER_ERROR);
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
			// Generate the table name
			String tableName = user.getClass().getCanonicalName();
			
			String query = null;
			String id = null;
			
			if (user instanceof User) {
				id = StorageUtils.stripDashesFromUUID(((User) user).getUserId());
				query = "SELECT * FROM `" + utils.mysqlifyName(tableName) +
						"` WHERE uuid = x'" + id + "';";
			} else {
				id = "" + ((PlatformUserIdentifier) ((PlatformUser) user)
						.getIdentifier()).getId();
				query = "SELECT * FROM `" + utils.mysqlifyName(tableName) +
						"` WHERE id = " + id + ";";
			}

			// Build query from identifier
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				Statement stmt = connection.createStatement();
				Properties props =
						utils.resultSetRowToProperties(stmt.executeQuery(query));
				user.populate(props);
				stmt.close();
			} catch (SQLException | NullPointerException e) {
				// No user type
				throw new StorageException(STORAGE_ERROR);
			}
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
			
		} catch (SQLException | InstantiationException | IllegalAccessException e) {
			// Well, that didn't work too well. Just return an empty array,
			// i.e. do nothing here.
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return users.toArray(new AbstractUser[0]);
	}

	@Override
	public PlatformUser[] readPlatformUsers(Properties criteria) {
		ArrayList<PlatformUser> users = new ArrayList<PlatformUser>();
		PreparedStatement stmt = null;
		String query = null;
		int i = 1;
		
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
				users.add((PlatformUser) factory.build((Properties) props.get(i)));
			}
			
		} catch (SQLException | UserInitalizationException e) {
			// Well, that didn't work too well. Just return an empty array,
			// i.e. do nothing here.
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		Class clazz = query.getDataType();
		
		if (query != null) {
			query.setTranslator(new MySQLQueryTranslator());
			
			try {
				// Let's take command of the commit ship ourselves.
				// Forward, mateys!
				connection.setAutoCommit(false);
				Statement stmt = connection.createStatement();
				
				// Fetch the results and convert them to readable objects.
				java.sql.ResultSet rs = stmt.executeQuery(query.compile());
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
						throw new StorageException(INSTANCE_OBJECT_ERROR);
					}
					objs.add(props);
				}
				
				// Close the database stuff gracefully
				rs.close();
				stmt.close();
				
				// Aaand we're done. Finish this up, then move on.
				result = new ResultSetImpl(clazz, objs.toArray());
			} catch (SQLException | IllegalAccessException e) {
				throw new StorageException(STORAGE_ERROR);
			} catch (InstantiationException e) {
				throw new StorageException(INSTANCE_OBJECT_ERROR);
			}
		}
				
		return result;
	}
}
