package se.mah.elis.impl.services.storage;

import java.nio.ByteBuffer;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.AbstractUser;

import org.apache.commons.codec.binary.Hex;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;

/**
 * This class contains a bunch of helper methods to be used by StorageImpl.
 * The methods could easily have been in StorageImpl itself, but have been
 * moved out in the name of separation of concerns and readability.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
 */
@Component(name = "Elis StorageUtils")
public class StorageUtils {
	
	// The MySQL connection. Ideally, this should be shared with the
	// StorageImpl object.
	private Connection connection;
	
	@Reference
	private LogService log;
	
	/**
	 * Creates an instance of StorageUtils.
	 * 
	 * @since 2.0
	 */
	public StorageUtils() {
	}
	
	/**
	 * Creates an instance of StorageUtils.
	 * 
	 * @param connection The MySQL connection to use.
	 * @since 2.0
	 */
	public StorageUtils(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Sets the connection.
	 * 
	 * @param connection The MySQL connection to use.
	 * @since 2.0
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Creates a table that doesn't already exist in the database.
	 * 
	 * @param tableName The name of the table to be created.
	 * @param p The properties describing the table to be created.
	 * @since 2.0
	 */
	public void createTableIfNotExisting(String tableName, Properties p) {
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
			stmt.execute(TableBuilder.buildModel(tableName, p));
			log(LogService.LOG_INFO, "Created new table " + tableName);
		} catch (SQLException | StorageException e) {
			// Skip this like we just don't care.
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
	}

	/**
	 * Generates a list of column names to be included in the SQL queries.
	 * 
	 * @param p The properties to generate the key list from.
	 * @return A string with a list of column names.
	 * @since 2.0
	 */
	public static String generateKeyList(Properties p) {
		Iterator<Entry<Object, Object>> entries = p.entrySet().iterator();
		Entry<Object, Object> entry = null;
		StringBuffer keys = new StringBuffer();
		
		while (entries.hasNext()) {
			entry = entries.next();
			if (!(entry.getKey() instanceof String)) {
				throw new IllegalArgumentException();
			}
			
			if (keys.length() > 0) {
				keys.append(", ");
			}
			
			keys.append("`" + mysqlifyName((String) entry.getKey()) + "`");
		}
		
		return keys.toString();
	}

	/**
	 * Generates a list of question marks to be used in prepared statements.  
	 * 
	 * @param p The properties to generate the question mark list from.
	 * @return A string with a list of question marks.
	 * @since 2.0
	 */
	public static String generateQMarks(Properties p) {
		Iterator<Entry<Object, Object>> entries = p.entrySet().iterator();
		Entry<Object, Object> entry = null;
		StringBuffer keys = new StringBuffer();
		
		while (entries.hasNext()) {
			entry = entries.next();
			if (!(entry.getValue() instanceof Collection)) {
				if (keys.length() > 0) {
					keys.append(", ");
				}
				if (entry.getValue() instanceof UUID) {
					keys.append("x?");
				} else {
					keys.append("?");
				}
			}
		}
		
		return keys.toString();
	}

	/**
	 * Make a class name possible to use as a table name. In practice, this
	 * means that we're replacing all periods with hyphens.
	 * 
	 * @param name The class name to convert to a table name.
	 * @return A decent table name.
	 * @since 2.0
	 */
	public static String mysqlifyName(String name) {
		return name.replace('.', '-').replaceAll(" ", "_");
	}

	/**
	 * Make a class name possible to use as a table name. In practice, this
	 * means that we're replacing all periods with hyphens.
	 * 
	 * @param name The table name to convert to a class name.
	 * @return A canonical class name.
	 * @since 2.0
	 */
	public static String demysqlifyName(String name) {
		return name.replace('-', '.').replace("_", " ");
	}

	/**
	 * Analyzes a value and adds it as a parameter to a prepared statement.
	 * 
	 * @param stmt The prepared statement to add the parameter to.
	 * @param value The value to be analyzed and added to the statement.
	 * @param index The position of the parameter in the statement.
	 * @param useLike Set to true to search for string likeness.
	 * @return Returns the value of the next index. If the parameter wasn't
	 * 		set, then the current index is returned.
	 * @throws SQLException When the parameter couldn't be set.
	 * @since 2.0
	 */
	public int addParameter(PreparedStatement stmt, Object value, int index,
			boolean useLike) throws SQLException {
		if (value == null) {
			stmt.setNull(index, Types.NULL);
		} else if (value instanceof Integer) {
			stmt.setInt(index, ((Integer) value).intValue());
		} else if (value instanceof Long) {
			stmt.setLong(index, ((Long) value).longValue());
		} else if (value instanceof Float) {
			stmt.setFloat(index, ((Float) value).floatValue());
		} else if (value instanceof Double) {
			stmt.setDouble(index, ((Double) value).doubleValue());
		} else if (value instanceof Boolean) {
			stmt.setBoolean(index, ((Boolean) value).booleanValue());
		} else if (value instanceof UUID) {
			stmt.setString(index, stripDashesFromUUID((UUID) value));
		} else if (value instanceof String) {
			if (useLike) {
				stmt.setString(index, '%' + (String) value + '%');
			} else {
				stmt.setString(index, (String) value);
			}
		} else if (value instanceof Byte) {
			stmt.setByte(index, (Byte) value);
		} else if (value instanceof DateTime) {
			stmt.setTimestamp(index, new Timestamp(((DateTime) value).getMillis()));
		} else if (value instanceof Collection) {
			// Don't do anything, this is handled elsewhere
			return index;
		} else {
			// TODO: Implement this, mofo!
			Blob blob = connection.createBlob();
			return index;
		}
		
		return index + 1;
	}

	/**
	 * Generates a list of properties in the form of
	 * <i>`key1` = ?, `key2` = ?</i> to be included in e.g. an update query.
	 * 
	 * @param properties The properties to list.
	 * @return A string as described above.
	 * @since 2.0
	 */
	public static String pairUp(Properties properties) {
		return pairUp(properties, ", ", false);
	}

	/**
	 *  Generates a list of properties in the form of
	 * <i>`key1` = ? AND `key2` = ?</i> to be included in e.g. a select query.
	 * 
	 * @param properties The properties to list.
	 * @param useLike Set to true to search for string likeness.
	 * @return A string as described above.
	 * @since 2.0
	 */
	public static String pairUpForSelect(Properties properties, boolean useLike) {
		return pairUp(properties, " AND ", useLike);
	}
	
	/**
	 *  Generates a list of properties in the form of
	 * <i>`key1` = ?<connector>`key2` = ?</i> to be included in a select query.
	 * 
	 * @param properties The properties to list.
	 * @param connector The string to connect the pairs.
	 * @param useLike Set to true to search for string likeness.
	 * @return A string as described above.
	 * @since 2.0
	 */
	private static String pairUp(Properties properties, String connector,
			boolean useLike) {
		StringBuffer pairs = new StringBuffer();
		boolean added = false;
		
		for (Entry<?, ?> e : properties.entrySet()) {
			if (added) {
				pairs.append(connector);
			}
			if (e.getValue() instanceof String && useLike) {
				pairs.append("`" + mysqlifyName((String) e.getKey()) + "` LIKE ?");
				added = true;
			} else if (e.getValue() instanceof UUID) {
				pairs.append("`" + mysqlifyName((String) e.getKey()) + "` = x?");
				added = true;
			} else if (e.getValue() instanceof Collection) {
				// Don't do anything, this is handled elsewhere
				added = false;
			} else {
				pairs.append("`" + mysqlifyName((String) e.getKey()) + "` = ?");
				added = true;
			}
		}
		
		return pairs.toString();
	}

	/**
	 * Converts a UUID object to a byte array for storing in MySQL.
	 * 
	 * @param uuid The UUID to convert.
	 * @return A byte array.
	 * @since 2.0
	 */
	public static byte[] uuidToBytes(UUID uuid) {
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		
		return bb.array();
	}

	/**
	 * Creates a set of Properties from an SQL result set. If the result set
	 * consists of more than one row, only the first row will be evaluated.
	 * 
	 * @param rs The result set
	 * @return A newly created Properties object.
	 * @since 2.0
	 */
	public Properties resultSetRowToProperties(ResultSet rs) {
		ResultSetMetaData meta = null;
		Properties props = new Properties();
		Object payload = null;
		
		try {
			meta = rs.getMetaData();
			rs.next();
			
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				payload = null;
				switch (meta.getColumnType(i)) {
					case Types.VARBINARY:
					case Types.BINARY:
						byte[] bytes = rs.getBytes(i);
						if (bytes.length == 16) {
							payload = bytesToUUID(bytes);
						} else {
							payload = bytes;
						}
						break;
					case Types.NULL:
						// Don't add this to the properties, it'll not end well
						break;
					case Types.TINYINT:
						payload = rs.getBoolean(i);
					case Types.INTEGER:
						payload = rs.getInt(i);
						break;
					case Types.FLOAT:
					case Types.REAL:
						payload = rs.getFloat(i);
						break;
					case Types.DOUBLE:
						payload = rs.getDouble(i);
						break;
					case Types.CHAR:
					case Types.VARCHAR:
						payload = rs.getString(i);
						break;
					case Types.TIMESTAMP:
						payload = new DateTime(rs.getTimestamp(i).getTime());
						break;
					default:
						// Don't do anything at all here.
				}
				if (payload != null) {
					addValueToProps(props, payload.getClass().getName(),
							meta.getColumnName(i), payload);
				}
			}
		} catch (SQLException e) {
			// Well, this didn't fare very well. Let's just return a null reference.
			log(LogService.LOG_WARNING, "Failed to convert result set to properties. " +
					"Current properties object is " + props, e);
			props = null;
		}
		
		return props;
	}
	
	/**
	 * Creates an array list of Properties objects from a (possibly)
	 * multi-rowed SQL result set.
	 * 
	 * @param rs The result set.
	 * @return A newly created array list of Properties objects.
	 * @since 2.0
	 */
	public ArrayList<Properties> resultSetToProperties(ResultSet rs) {
		ArrayList<Properties> propArray = new ArrayList<Properties>();
		
		ResultSetMetaData meta = null;
		Properties props = null;
		int colCount = 0;
		int i = 0;
		
		try {
			meta = rs.getMetaData();
			colCount = meta.getColumnCount();
			
			while (rs.next()) {
				props = new Properties();
				for (i = 1; i <= colCount; i++) {
					addValueToProps(props, meta.getColumnClassName(i),
							meta.getColumnName(i), rs.getObject(i));
				}
				propArray.add(props);
			}
		} catch (SQLException e) {
			// Well, this didn't fare very well. Let's just skip this row.
			log(LogService.LOG_WARNING, "Failed to convert result set to properties.", e);
		}
		
		return propArray;
	}

	/**
	 * Looks up the table associated with a UUID.
	 * 
	 * @param uuid The UUID to look up.
	 * @return The name of the table in which the UUID-identifed entry is to be
	 * 		found.
	 * @since 2.0
	 */
	public String lookupUUIDTable(UUID uuid) {
		String tableName = null;
		Statement stmt = null;
		java.sql.ResultSet rs = null;
		
		String query = "SELECT stored_in FROM object_lookup_table " +
				"WHERE id = x'" + stripDashesFromUUID(uuid) + "';";
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				tableName = rs.getString(1);
			}
		} catch (SQLException e) {
			// Skip this like we just don't care.
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {}
		}
		
		return tableName;
	}

	/**
	 * Pairs a UUID with a table name.
	 * 
	 * @param uuid The UUID.
	 * @param table The table name.
	 * @throws StorageException if the information couldn't be stored, e.g. if
	 * 		the UUID already exists in the table.
	 * @since 2.0
	 */
	public void pairUUIDWithTable(UUID uuid, String table) throws StorageException {
		// TODO: It might be possible to create a version of this method that
		//		 also takes a PreparedStatement as a parameter, thereby
		//		 minimizing execution time and DB load.
		
		PreparedStatement stmt = null;
		String query = "INSERT INTO object_lookup_table VALUES(x?, ?);";
		
		if (uuid == null || table == null || table.length() == 0) {
			throw new StorageException();
		}
		table = mysqlifyName(table);
		
		try {
			stmt = connection.prepareStatement(query);
			
			// Populate the query
			stmt.setString(1, stripDashesFromUUID(uuid));
			stmt.setString(2, table);
			
			// Run the statement and end the transaction
			stmt.executeUpdate();
		} catch (SQLException e) {
			log(LogService.LOG_ERROR, "Couldn't write to database", e);
			throw new StorageException("Couldn't write to database.");
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
	}
	
	/**
	 * Decouples a UUID from a table, i.e. frees it for use in another table.
	 * 
	 * @param uuid The UUID.
	 * @param table The table name.
	 * @since 1.0
	 */
	public void freeUUID(UUID uuid) {
		Statement stmt = null;
		String query = "DELETE FROM object_lookup_table WHERE id = x'" +
				stripDashesFromUUID(uuid) + "';";
		
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// Skip this like we just don't care.
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
	}
	
	/**
	 * Associates a user with a platform user.
	 * 
	 * @param platformUser The owning platform user.
	 * @param user The owned user.
	 * @throws StorageException if the couple couldn't be stored.
	 * @since 2.0
	 */
	public void coupleUsers(UUID platformUser, UUID user)
			throws StorageException {
		PreparedStatement stmt = null;
		String query = "INSERT INTO user_bindings VALUES(x?, x?);";
		
		if (user == null) {
			throw new StorageException();
		}
		
		try {
			connection.setAutoCommit(true);
			stmt = connection.prepareStatement(query);
			
			// Populate the query
			stmt.setString(1, stripDashesFromUUID(platformUser));
			stmt.setString(2, stripDashesFromUUID(user));
			
			// Run the statement and end the transaction
			stmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getErrorCode() != 1062) {
				log(LogService.LOG_INFO, "Users already coupled: " + platformUser + " and " + user);
				throw new StorageException("Users already coupled.");
			}
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
	}
	
	/**
	 * De-associates a user with a platform user.
	 * 
	 * @param platformUser The owning platform user.
	 * @param user The owned user.
	 * @since 2.0
	 */
	public void decoupleUsers(UUID platformUser, UUID user) {
		Statement stmt = null;
		String query = "DELETE FROM user_bindings WHERE platform_user = x'" +
				stripDashesFromUUID(platformUser) + "' AND user = x'" +
				stripDashesFromUUID(user) + "';";
		
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// Skip this like we just don't care.
			log(LogService.LOG_INFO, "Users already decoupled: " + platformUser + " and " + user);
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
	}
	
	/**
	 * Returns a list of the id numbers of all platform users associated with a
	 * specific user.
	 * 
	 * @param user The user to search for.
	 * @return An array containing a list of all associated platform users.
	 * @since 2.0
	 */
	public UUID[] getPlatformUsersAssociatedWithUser(UUID user) {
		ArrayList<UUID> platformUsers = new ArrayList<UUID>();
		Statement stmt = null;
		java.sql.ResultSet rs = null;
		
		String query = "SELECT platform_user FROM user_bindings " +
				"WHERE user = x'" + stripDashesFromUUID(user) + "';";
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				platformUsers.add(bytesToUUID(rs.getBytes(1)));
			}
		} catch (SQLException e) {
			// Skip this like we just don't care.
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {}
		}
		
		return platformUsers.toArray(new UUID[0]);
	}
	
	/**
	 * Returns a list of the id numbers of all users associated with a specific
	 * platform user.
	 * 
	 * @param id The platform user to search for.
	 * @return An array containing a list of all associated users.
	 * @since 2.0
	 */
	public UUID[] getUsersAssociatedWithPlatformUser(UUID id) {
		ArrayList<UUID> platformUsers = new ArrayList<UUID>();
		Statement stmt = null;
		java.sql.ResultSet rs = null;
		
		String query = "SELECT user FROM user_bindings " +
				"WHERE platform_user = x'" + stripDashesFromUUID(id) + "';";
		try {
			// Let's take command of the commit ship ourselves.
			// Forward, mateys!
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				platformUsers.add(bytesToUUID(rs.getBytes(1)));
			}
		} catch (SQLException e) {
			// Skip this like we just don't care.
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {}
		}
		
		return platformUsers.toArray(new UUID[0]);
	}
	
	/**
	 * Returns a list of objects collected by a collecting object.
	 * 
	 * @param owner The collecting object.
	 * @param collection The name of the collection.
	 * @return A list of UUIDs, each representing a collected object.
	 * @since 2.0
	 */
	public UUID[] listCollectedObjects(UUID owner, String collection) {
		ArrayList<UUID> uuids = new ArrayList<UUID>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT `collected_object` FROM collections " +
				"WHERE `collecting_object` = x? AND collection_name = ?;";
		
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, StorageUtils.stripDashesFromUUID(owner));
			stmt.setString(2, collection);
			rs = stmt.executeQuery();
			while (rs.next()) {
				uuids.add(bytesToUUID(rs.getBytes(1)));
			}
		} catch (SQLException e) {
			log(LogService.LOG_WARNING, StorageImpl.STORAGE_ERROR + ": Couldn't fetch from collections", e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
		
		return uuids.toArray(new UUID[0]);
	}
	
	/**
	 * Deletes collections belonging to a User or ElisDataObject.
	 * 
	 * @param owner The UUID belonging to the collecting object.
	 * @since 2.0
	 */
	public void deleteCollections(UUID owner) {
		PreparedStatement stmt = null;
		String query = "DELETE FROM collections WHERE `collecting_object` = x? OR `collected_object` = x?;";
		
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, StorageUtils.stripDashesFromUUID(owner));
			stmt.setString(2, StorageUtils.stripDashesFromUUID(owner));
			stmt.executeUpdate();
		} catch (SQLException e) {
			log(LogService.LOG_WARNING, StorageImpl.STORAGE_ERROR + ": Couldn't remove stuff from collections", e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
	}
	
	/**
	 * Deletes collections belonging to a User or ElisDataObject.
	 * 
	 * @param owner The UUID belonging to the collecting object.
	 * @since 2.0
	 */
	public void deleteFromCollections(UUID owner) {
		PreparedStatement stmt = null;
		String query = "DELETE FROM collections WHERE `collected_object` = x? OR `collected_object` = x?;";
		
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, StorageUtils.stripDashesFromUUID(owner));
			stmt.setString(2, StorageUtils.stripDashesFromUUID(owner));
			stmt.executeUpdate();
		} catch (SQLException e) {
			log(LogService.LOG_WARNING, StorageImpl.STORAGE_ERROR + ": Couldn't remove stuff from collections", e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
	}
	
	/**
	 * Updates a collection, i.e. removes and adds references to objects in
	 * said collection.
	 * 
	 * @param owner The owning object's id number.
	 * @param set The collection that we want to update.
	 * @param name The name of the owning object's field referencing the
	 * 		collection.
	 * @since 2.0
	 */
	public void updateCollection(UUID owner, Collection<UUID> set, String name) {
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		String ownerId = owner.toString().replace("-", "");
		
		// First of all: If the set is empty, then we can simply throw the
		// whole collection away
		if (set.size() == 0) {
			// No objects in collection. Let the mayhem begin!
			String delete = "DELETE FROM collections WHERE " +
					"collecting_object = x? " +
					"AND collection_name = ?;";
			
			try {
				stmt = connection.prepareStatement(delete);
				stmt.setString(1, ownerId);
				stmt.setString(2, name);
				stmt.executeUpdate();
			} catch (SQLException e) {
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
		} else {
			// Not empty. Let the pruning and blossom begin!
			UUID uuid = null;
			
			// First, remove all objects not present in the set
			String trim = "DELETE FROM collections WHERE " +
					"collecting_object = x'" + ownerId + "' " +
					"AND collection_name = '" + name +  "' " +
					"AND collected_object NOT IN (";
			for (Object o : set) {
				if (o instanceof AbstractUser) {
					uuid = ((AbstractUser) o).getUserId();
				} else if (o instanceof ElisDataObject) {
					uuid = ((ElisDataObject) o).getDataId();
				} else if (o instanceof UUID) {
					uuid = (UUID) o;
				} else {
					uuid = null;
				}
				if (uuid != null) {
					trim += "x'" + uuid.toString().replace("-", "") + "', ";
				}
			}
			trim = trim.substring(0, trim.length() - 2);
			trim +=	");";
			
			try {
				stmt = connection.prepareStatement(trim);
				stmt.executeUpdate();
			} catch (SQLException e) {
				try {
					stmt.close();
				} catch (SQLException e1) {}
			}
			
			// Then, add all new objects if they don't exist
			String insert = "INSERT IGNORE INTO collections " +
					"(collecting_object, collected_object, collection_name) " +
					"VALUES ";
			for (Object o : set) {
				if (o instanceof AbstractUser) {
					uuid = ((AbstractUser) o).getUserId();
				} else if (o instanceof ElisDataObject) {
					uuid = ((ElisDataObject) o).getDataId();
				} else if (o instanceof UUID) {
					uuid = (UUID) o;
				} else {
					uuid = null;
				}
				if (uuid != null) {
					insert += "(x'" + ownerId + "', x'" +
							uuid.toString().replace("-", "") +
							"', '" + name + "'), ";
				}
			}
			insert = insert.substring(0, insert.length() - 2);
			insert += ";";
			
			try {
				stmt2 = connection.prepareStatement(insert);
				int r = stmt2.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					stmt2.close();
				} catch (SQLException e2) {}
			}
		}
	}
	
	/**
	 * Removes dashes from a UUID string for inclusion in MySQL queries.
	 * 
	 * @param uuid The UUID to prepare for MySQL usage.
	 * @return A MySQL-friendly representation of the UUID.
	 * @since 2.0
	 */
	public static String stripDashesFromUUID(UUID uuid) {
		return uuid.toString().replace("-", "");
	}

	/**
	 * Converts bytes to a UUID object.
	 * 
	 * @param bytes The bytes to be converted to a UUID.
	 * @return A UUID object.
	 * @since 2.0
	 */
	public static UUID bytesToUUID(byte[] bytes) {
		StringBuilder build = new StringBuilder(Hex.encodeHexString(bytes));

		build.insert(20, '-');
		build.insert(16, '-');
		build.insert(12, '-');
		build.insert(8, '-');
		
		return UUID.fromString(build.toString());
	}
	
	/**
	 * Checks whether a set of properties are empty or not. A set of properties
	 * are considered empty if all of them are deemed empty. A property is
	 * considered empty if:<br>
	 * <ul>
	 *   <li>A String is empty</li>
	 *   <li>A Number is 0</li>
	 * </ul>
	 * Note that property sets can't hold null values.
	 * 
	 * @param props The properties to check.
	 * @return True if the property set is empty, otherwise false.
	 * @since 2.0
	 */
	public static boolean isEmpty(Properties props) {
		Iterator<Entry<Object, Object>> entries = props.entrySet().iterator();
		Entry<Object, Object> entry = null;
		Object value = null;
		boolean isEmpty = true;
		
		while (entries.hasNext() && isEmpty) {
			entry = entries.next();
			value = entry.getValue();
			if (value instanceof String) {
				if (!entry.getKey().equals("service_name")) {
					isEmpty = (((String) value).length() < 1);
				}
			} else if (value instanceof Number) {
				isEmpty = (((Number) value).equals(0));
			} else if (value instanceof DateTime) {
				if (!entry.getKey().equals("created")) {
					isEmpty = ((DateTime) value).isEqual(0);
				}
			} else if (value instanceof UUID) {
				// Do nothing
			} else {
				isEmpty = false;
			}
		}
		
		return isEmpty;
	}
	
//	/**
//	 * Flatten a set of properties prior to saving them to the database.
//	 * 
//	 * @param props The properties to flatten.
//	 * @param isTemplate If set to true, the method will call the
//	 * 		getPropertiesTemplate() method on the identifier.
//	 * @return Returns a flattened Properties object.
//	 * @since 2.0
//	 */
//	public static Properties flattenPropertiesWithIdentifier(Properties props, boolean isTemplate) {
//		OrderedProperties flatProps = new OrderedProperties();
//		
//		for (Entry<Object, Object> prop : props.entrySet()) {
//			if (prop.getValue() instanceof UserIdentifier) {
//				if (isTemplate ) {
//					flatProps.putAll(((UserIdentifier) prop.getValue()).getPropertiesTemplate());
//				} else {
//					flatProps.putAll(((UserIdentifier) prop.getValue()).getProperties());
//				}
//			} else {
//				flatProps.put(prop.getKey(), prop.getValue());
//			}
//		}
//		
//		return flatProps;
//	}
	
	/**
	 * <p>Validates that a set of properties are OK for storing. The criteria
	 * for a valid set of properties are:</p>
	 * 
	 * <ul>
	 *   <li>The first value is a UUID object called "dataid" if this is an
	 *   existing data object or a template</li>
	 *   <li>A UUID object called "ownerid"</li>
	 *   <li>A Joda DateTime object called "created"</li>
	 *   <li>At least one more element</li>
	 * </ul>
	 * 
	 * @param props The property set to validate.
	 * @param checkForDataId If set to true, the "dataid" field is checked.
	 * @return True of the property set is OK, otherwise false.
	 * @since 2.0
	 */
	public static boolean validateEDOProperties(Properties props,
			boolean checkForDataId) {
		boolean result = false;
		
		if (checkForDataId) {
			Iterator<Entry<Object, Object>> entries = props.entrySet().iterator();
			Entry<Object, Object> firstEntry = entries.next();
			
			result = props.size() > 3 &&
					"dataid".equals(firstEntry.getKey()) &&
					firstEntry.getValue() instanceof UUID &&
					props.containsKey("ownerid") &&
					props.get("ownerid") instanceof UUID &&
					props.containsKey("created") &&
					props.get("created") instanceof DateTime;
		} else {
			result = props.size() > 2 &&
					props.containsKey("ownerid") &&
					props.get("ownerid") instanceof UUID &&
					props.containsKey("created") &&
					props.get("created") instanceof DateTime;
		}
		
		return result;
	}
	
	/**
	 * <p>Validates that a set of properties are OK for storing. The criteria
	 * for a valid set of properties are:</p>
	 * 
	 * <ul>
	 *   <li>The first value is a UUID object called "uuid" if this is an
	 *   existing user object or a template</li>
	 *   <li>A Joda DateTime object called "created"</li>
	 *   <li>A String object called "service_name"</li>
	 *   <li>At least one more element</li>
	 * </ul>
	 * 
	 * @param props The property set to validate.
	 * @param checkForUUID If set to true, the "uuid" field is checked.
	 * @return True of the property set is OK, otherwise false.
	 * @since 2.0
	 */
	public static boolean validateAbstractUserProperties(Properties props,
			boolean checkForUUID) {
		boolean result = false;
		
		if (checkForUUID) {
			Iterator<Entry<Object, Object>> entries = props.entrySet().iterator();
			Entry<Object, Object> firstEntry = entries.next();
			
			result = props.size() > 3 &&
					"uuid".equals(firstEntry.getKey()) &&
					firstEntry.getValue() instanceof UUID &&
					props.containsKey("service_name") &&
					props.get("service_name") instanceof String &&
					props.containsKey("created") &&
					props.get("created") instanceof DateTime;
		} else {
			result = props.size() > 2 &&
					props.containsKey("service_name") &&
					props.get("service_name") instanceof String &&
					props.containsKey("created") &&
					props.get("created") instanceof DateTime;
		}
		
		return result;
	}
	
	/**
	 * Adds a value to a Properties object, as something else than a simple
	 * Object, i.e. as an int, a String, or what not.
	 * 
	 * @param props The Properties object to add the value to.
	 * @param clazz The class name of the passed value, as retrieved by
	 * 		{@link java.sql.ResultSetMetaData#getColumnClassName(int)}.
	 * @param colName The name of the column in the database.
	 * @param value The object that we want to add to the Properties object.
	 * @since 2.0
	 */
	private void addValueToProps(Properties props, String clazz,
			String colName, Object value) {
		
		// Match with a Java class name, then call the corresponding getter and
		// add to the Properties object.
		if (clazz.equals("java.lang.String")) {
			props.put(colName, (String) value);
		} else if (clazz.equals("java.lang.Integer")) {
			props.put(colName, (Integer) value);
		} else if (clazz.equals("java.lang.Long")) {
			props.put(colName, (Long) value);
		} else if (clazz.equals("java.lang.Float")) {
			props.put(colName, (Float) value);
		} else if (clazz.equals("java.lang.Double")) {
			props.put(colName, (Double) value);
		} else if (clazz.equals("java.lang.Byte")) {
			props.put(colName, (Byte) value);
		} else if (clazz.equals("java.sql.Date")) {
			props.put(colName, new DateTime((Date) value));
		} else if (clazz.equals("java.sql.Datetime")) {
			props.put(colName, new DateTime((Date) value));
		} else if (clazz.equals("java.sql.Timestamp")) {
			props.put(colName, new DateTime(((Timestamp) value).getTime()));
		} else if (clazz.equals("java.lang.Boolean")) {
			props.put(colName, (Boolean) value);
		} else if (clazz.equals(byte[].class.getName()) &&
				((byte[]) value).length == 16) {
			props.put(colName, bytesToUUID((byte[]) value));
		} else if (clazz.equals(Collection.class.getName())) {
			// Don't do anything, this is done elsewhere
		} else {
			props.put(colName, value);
		}
	}
	
	private void log(int level, String message, Throwable t) {
		if (log != null) {
			log.log(level, "StorageUtils: " + message, t);
		}
	}
	
	private void log(int level, String message) {
		if (log != null) {
			log.log(level, "StorageUtils: " + message);
		}
	}
	
	protected void bindLog(LogService log) {
		this.log = log;
	}
	
	protected void unbindLog(LogService log) {
		this.log = null;
	}
}
