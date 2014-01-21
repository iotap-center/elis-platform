package se.mah.elis.impl.services.storage;

import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.UUID;

import org.joda.time.DateTime;

public class TableBuilder {
	
	public static String buildModel(String tableName, Properties p) {
		StringBuffer query = new StringBuffer();

		query.append("CREATE TABLE `" + tableName + "` (");
		query.append(generateDataModel(p));
		query.append(") TYPE=innodb;");
		
		return query.toString();
	}
	
	private static String generateDataModel(Properties p) {
		Iterator<Entry<Object, Object>> entries = p.entrySet().iterator();
		Entry<Object, Object> entry = entries.next();
		StringBuffer columns = new StringBuffer();
		
		while (entry != null) {
			if (columns.length() > 0) {
				columns.append(", ");
			}
			
			columns.append(entry.getKey() + " " + dataType(entry.getValue()));
			
			entry = entries.next();
		}
		
		return columns.toString();
	}
	
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
			int size = Integer.parseInt((String) value);
			if (size > 0) {
				dataType = "VARCHAR(" + size + ")";
			} else {
				dataType = "TEXT";
			}
		} else if (value instanceof DateTime) {
			dataType = "DATETIME";
		} else {
			dataType = "BLOB";
		}
		
		return dataType;
	}
}
