package se.mah.elis.impl.services.storage;

import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.exceptions.TypeMismatchException;
import se.mah.elis.impl.services.storage.exceptions.YouAreBreakingTheInternetException;
import se.mah.elis.impl.services.storage.exceptions.YouAreDoingItWrongException;
import se.mah.elis.services.storage.exceptions.StorageException;

/**
 * TableBuilder is a helper class used by {@link StorageImpl} to build table
 * models for on-the-fly table creation.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
 */
public class TableBuilder {
	
	/**
	 * Constructs a MySQL create statement based on a set of properties.
	 * 
	 * @param tableName The name of the table that we wish to create.
	 * @param p The parameters that describe the table columns.
	 * @return A MySQL create statement.
	 * @throws StorageException if the model couldn't be built.
	 * @since 2.0
	 */
	public static String buildModel(String tableName, Properties p)
			throws StorageException {
		if (tableName == null || tableName.length() < 1 ||
			p == null || p.isEmpty()) {
			throw new YouAreBreakingTheInternetException();
		}
		
		StringBuffer query = new StringBuffer();
		
		try {
			query.append("CREATE TABLE `" +
					StorageUtils.mysqlifyName(tableName) + "` (");
			query.append(generateDataModel(p));
			query.append(");");
			
			return query.toString();
		} catch (TypeMismatchException e) {
			throw new YouAreBreakingTheInternetException();
		}
	}
	
	/**
	 * Constructs a table model from a set of properties.
	 * 
	 * @param p The properties that describe the table layout.
	 * @return The table model, as a String.
	 * @since 2.0
	 */
	private static String generateDataModel(Properties p) {
		StringBuffer columns = new StringBuffer();
		Enumeration<Object> keys = p.keys();
		String key = null;
		int i = 0;
		
		while (keys.hasMoreElements()) {
			key = (String) keys.nextElement();
			
			
			if (columns.length() > 0) {
				columns.append(", ");
			}
			
			columns.append("`" +
					StorageUtils.mysqlifyName(key) + "` " +
					dataType(p.get(key)));
			if (i++ == 0) {
				columns.append(" PRIMARY KEY");
			}
		}
		
		return columns.toString();
	}
	
	/**
	 * Takes an object and determines what SQL data type is best suited for
	 * storing said object.
	 * 
	 * @param value This is the object that, whose SQL data type we're trying
	 * 		to find out.
	 * @return The SQL data type.
	 * @since 2.0
	 */
	private static String dataType(Object value) {
		String dataType = null;
		
		if (value instanceof Boolean) {
			dataType = "BOOLEAN";
		} else if (value instanceof Integer) {
			dataType = "INTEGER";
		} else if (value instanceof Long) {
			dataType = "BIGINT";
		} else if (value instanceof Float) {
			dataType = "FLOAT";
		} else if (value instanceof Double) {
			dataType = "DOUBLE";
		} else if (value instanceof UUID) {
			dataType = "BINARY(16)";
		} else if (value instanceof Character) {
			dataType = "CHAR(1)";
		} else if (value instanceof String) {
			try {
				int size = Integer.parseInt((String) value);
				if (size > 0) {
					dataType = "VARCHAR(" + size + ")";
				} else {
					throw new YouAreDoingItWrongException();
				}
			} catch (NumberFormatException e) {
				dataType = "TEXT";
			}
		} else if (value instanceof DateTime) {
			dataType = "TIMESTAMP";
		} else {
			dataType = "BLOB";
		}
		
		return dataType;
	}
}
