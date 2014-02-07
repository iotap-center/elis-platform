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
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.impl.services.storage.exceptions.YouAreDoingItWrongException;
import se.mah.elis.services.storage.exceptions.StorageException;

/**
 * This class contains a bunch of helper methods to be used by StorageImpl.
 * The methods could easily have been in StorageImpl itself, but have been
 * moved out in the name of separation of concerns and readability.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
 */
public class StorageUtils {
	
	// The MySQL connection. Ideally, this should be shared with the
	// StorageImpl object.
	private Connection connection;
	
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
	 * Creates a table that doesn't already exist in the database.
	 * 
	 * @param tableName The name of the table to be created.
	 * @param p The properties describing the table to be created.
	 * @since 2.0
	 */
	public void createTableIfNotExisting(String tableName, Properties p) {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute(TableBuilder.buildModel(tableName, p));
		} catch (SQLException | StorageException e) {
			// Skip this like we just don't care.
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
			if (keys.length() > 0) {
				keys.append(", ");
			}
			keys.append("?");
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
	 * @throws SQLException When the parameter couldn't be set.
	 * @since 2.0
	 */
	public void addParameter(PreparedStatement stmt, Object value, int index)
			throws SQLException {
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
			stmt.setBytes(index, uuidToBytes((UUID) value));
		} else if (value instanceof String) {
			stmt.setString(index, (String) value);
		} else if (value instanceof Byte) {
			stmt.setByte(index, (Byte) value);
		} else if (value instanceof DateTime) {
			stmt.setTimestamp(index, new Timestamp(((DateTime) value).getMillis()));
		} else {
			// TODO: Implement this, mofo!
			Blob blob = connection.createBlob();
		}
	}

	/**
	 * Generates a list of properties in the form of
	 * <i>`key1` = ?, `key2` = ?</i> to be included in e.g. an update
	 * query.
	 * 
	 * @param properties The properties to list.
	 * @return A string as described above.
	 * @since 2.0
	 */
	public static String pairUp(Properties properties) {
		StringBuffer pairs = new StringBuffer();
		
		for (Entry e : properties.entrySet()) {
			if (pairs.length() > 0) {
				pairs.append(", ");
			}
			pairs.append("`" + mysqlifyName((String) e.getKey()) + "` = ?");
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
		int colCount = 0;
		
		try {
			meta = rs.getMetaData();
			colCount = meta.getColumnCount();
			
			for (int i = 0; i < colCount; i++) {
				addValueToProps(props, meta.getColumnClassName(i),
						meta.getColumnName(i), rs.getObject(i));
			}
		} catch (SQLException e) {
			// Well, this didn't fare very well. Let's just return a null reference.
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
		Properties props = null;
		String tableName = null;
		
		String query = "SELECT table_name FROM object_lookup_table " +
				"WHERE id = '" + uuidToBytes(uuid) + "';";
		try {
			
			// Let's take command of the commit ship ourselves.
			// Forward, mateys!
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(query);
			props = resultSetRowToProperties(rs);
			rs.close();
			stmt.close();
			
			tableName = (String) props.get("table_name");
		} catch (SQLException e) {
			// Skip this like we just don't care.
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
		
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		
		try {
			// Let's take command of the commit ship ourselves.
			// Forward, mateys!
			connection.setAutoCommit(false);
			PreparedStatement stmt = connection.prepareStatement(query);
			
			// Populate the query
			stmt.setString(1, uuidToBytes(uuid).toString());
			stmt.setString(2, table);
			
			// Run the statement and end the transaction
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// Skip this like we just don't care
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
		} else if (clazz.equals("java.lang.Boolean")) {
			props.put(colName, (Boolean) value);
		} else {
			props.put(colName, value);
		}
		
		// TODO We should _really_ handle UUIDs as well. However, this seems to
		// require some exploratory development and will be postponed for the
		// time being.
	}
}
