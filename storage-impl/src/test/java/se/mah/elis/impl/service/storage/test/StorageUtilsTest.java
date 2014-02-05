package se.mah.elis.impl.service.storage.test;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.impl.service.storage.test.mock.MockConnection;
import se.mah.elis.impl.service.storage.test.mock.MockResultSet;
import se.mah.elis.impl.service.storage.test.mock.MockResultSetMetaData;
import se.mah.elis.impl.services.storage.StorageUtils;

public class StorageUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStorageUtils() {
		StorageUtils utils = new StorageUtils(new MockConnection());
		
		assertNotNull(utils);
	}

	@Test
	public void testCreateTableIfNotExisting() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		String tableName = java.lang.String.class.getName();
		Properties props = new OrderedProperties();
		ArrayList<Object> objectStore = connection.getObjectStore();
		String expected, actual;
		
		props.put("col 1", new Integer(1));
		props.put("col 2", "16");
		props.put("col 3", false);
		
		utils.createTableIfNotExisting(tableName, props);
		
		expected = "CREATE TABLE `java-lang-String` (`col_1` INTEGER PRIMARY KEY, `col_2` VARCHAR(16), `col_3` BOOLEAN);";
		actual = (String) objectStore.get(0);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testCreateTableIfNotExistingBadTableName() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		String tableName = "";
		Properties props = new OrderedProperties();
		ArrayList<Object> objectStore = connection.getObjectStore();
		
		props.put("col 1", new Integer(1));
		props.put("col 2", "16");
		props.put("col 3", false);
		
		utils.createTableIfNotExisting(tableName, props);
		
		assertNull(objectStore.get(0));
	}

	@Test
	public void testCreateTableIfNotExistingNoTableName() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		String tableName = null;
		Properties props = new OrderedProperties();
		ArrayList<Object> objectStore = connection.getObjectStore();
		
		props.put("col 1", new Integer(1));
		props.put("col 2", "16");
		props.put("col 3", false);
		
		utils.createTableIfNotExisting(tableName, props);
		
		assertNull(objectStore.get(0));
	}

	@Test
	public void testCreateTableIfNotExistingBadProperties() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		String tableName = "";
		Properties props = new OrderedProperties();
		ArrayList<Object> objectStore = connection.getObjectStore();
		
		props.put("col 1", new Integer(1));
		props.put(false, "16");
		props.put("col 3", false);
		
		utils.createTableIfNotExisting(tableName, props);
		
		assertNull(objectStore.get(0));
	}

	@Test
	public void testCreateTableIfNotExistingBadProperties2() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		String tableName = "";
		Properties props = new OrderedProperties();
		ArrayList<Object> objectStore = connection.getObjectStore();
		
		props.put("col 1", new Integer(1));
		props.put("col 2", "Batman!");
		props.put("col 3", false);
		
		utils.createTableIfNotExisting(tableName, props);
		
		assertNull(objectStore.get(0));
	}

	@Test
	public void testCreateTableIfNotExistingNoProperties() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		String tableName = "";
		Properties props = new OrderedProperties();
		ArrayList<Object> objectStore = connection.getObjectStore();
		
		utils.createTableIfNotExisting(tableName, props);
		
		assertNull(objectStore.get(0));
	}

	@Test
	public void testGenerateKeyList() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		props.put("col 1", 42);
		props.put("col 2", "Batman!");
		props.put("col 3", false);
		props.put("col 4", 1.3);
		
		expected = "`col_1`, `col_2`, `col_3`, `col_4`";
		actual = StorageUtils.generateKeyList(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGenerateKeyListOnlyOneElement() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		props.put("col 1", 42);
		
		expected = "`col_1`";
		actual = StorageUtils.generateKeyList(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGenerateKeyListEmptyList() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		expected = "";
		actual = StorageUtils.generateKeyList(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGenerateKeyListIncludingNonString() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		props.put("col 1", 42);
		props.put(true, "Batman!");
		props.put("col 3", false);
		props.put("col 4", 1.3);
		
		try {
			StorageUtils.generateKeyList(props);
			fail("This shouldn't happen.");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testGenerateQMarks() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		props.put("col 1", 42);
		props.put("col 2", "Batman!");
		props.put("col 3", false);
		props.put("col 4", 1.3);
		
		expected = "?, ?, ?, ?";
		actual = StorageUtils.generateKeyList(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGenerateQMarksOnlyOne() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		props.put("col 1", 42);
		
		expected = "?";
		actual = StorageUtils.generateKeyList(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGenerateQMarksNone() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		expected = "";
		actual = StorageUtils.generateKeyList(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testMysqlifyName() {
		String name = "Batman and Robin.";
		String expected = "Batman_and_Robin-";
		String actual = StorageUtils.mysqlifyName(name);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testMysqlifyNameNameAlreadyOK() {
		String name = "Batman_and_Robin-";
		String expected = "Batman_and_Robin-";
		String actual = StorageUtils.mysqlifyName(name);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testMysqlifyNameEmptyName() {
		String name = "";
		String expected = "";
		String actual = StorageUtils.mysqlifyName(name);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testMysqlifyNameHyphens() {
		String name = "Batman.and.Robin";
		String expected = "Batman-and-Robin";
		String actual = StorageUtils.mysqlifyName(name);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testMysqlifyNameUnderscores() {
		String name = "Batman and Robin";
		String expected = "Batman_and_Robin";
		String actual = StorageUtils.mysqlifyName(name);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testDemysqlifyName() {
		String name = "Batman_and_Robin-";
		String expected = "Batman and Robin.";
		String actual = StorageUtils.demysqlifyName(name);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testDemysqlifyNameEmptyName() {
		String name = "";
		String expected = "";
		String actual = StorageUtils.demysqlifyName(name);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testDemysqlifyNameNameAlreadyOK() {
		String name = "Batman and Robin.";
		String expected = "Batman and Robin.";
		String actual = StorageUtils.demysqlifyName(name);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testDemysqlifyNameUnderscores() {
		String name = "Batman_and_Robin";
		String expected = "Batman and Robin";
		String actual = StorageUtils.demysqlifyName(name);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testDemysqlifyNameHyphens() {
		String name = "Batman-and-Robin-";
		String expected = "Batman.and.Robin.";
		String actual = StorageUtils.demysqlifyName(name);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testAddParameter() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);

			utils.addParameter(stmt, 1, 0);
			utils.addParameter(stmt, "horses", 1);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Int: 1", (String) objectStore.get(0));
		assertEquals("String: horses", (String) objectStore.get(1));
	}

	@Test
	public void testAddParameterBadIndex() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);

			utils.addParameter(stmt, "horses", -1);
			fail("This shouldn't happen");
		} catch (SQLException e) {
			// This should happen
		}
	}

	@Test
	public void testAddParameterNull() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);

			utils.addParameter(stmt, null, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Null: null", (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterBoolean() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);

			utils.addParameter(stmt, true, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Boolean: true", (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterByte() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);

			utils.addParameter(stmt, (byte) 12, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Byte: 12", (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterInteger() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);


			utils.addParameter(stmt, 42, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Int: 42", (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterLong() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);


			utils.addParameter(stmt, (long) 42, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Long: 42", (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterFloat() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);


			utils.addParameter(stmt, 4.2, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Float: 4.2", (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterDouble() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);


			utils.addParameter(stmt, (double) 4.2, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Double: 42", (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterString() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);


			utils.addParameter(stmt, "horses", 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("String: horses", (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterDateTime() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		DateTime dt = DateTime.now();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);


			utils.addParameter(stmt, dt, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Timestamp: " + dt.getMillis(), (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterUUID() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);

			utils.addParameter(stmt, 42, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Bytes[]: HOWTODOTHIS?", (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testPairUp() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		props.put("col 1", 42);
		props.put("col 2", "Batman!");
		props.put("col 3", false);
		props.put("col 4", 1.3);
		
		expected = "`col_1` = ?, `col_2` = ?', `col_3` = ?, `col_4` = ?";
		actual = StorageUtils.pairUp(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testPairUpEmpty() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		expected = "";
		actual = StorageUtils.pairUp(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testPairUpOnlyOne() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		props.put("col 1", 42);
		
		expected = "`col_1` = 42";
		actual = StorageUtils.pairUp(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testUuidToBytes() {
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		ByteBuffer expected = ByteBuffer.wrap(new byte[16]);
		byte[] actual = StorageUtils.uuidToBytes(uuid);
		
		expected.putLong(uuid.getMostSignificantBits());
		expected.putLong(uuid.getLeastSignificantBits());
		
		assertEquals(expected.array(), actual);
	}

	@Test
	public void testResultSetRowToProperties() {
		MockResultSetMetaData meta = new MockResultSetMetaData();
		ArrayList<Object> data = new ArrayList<>();
		MockResultSet rs = new MockResultSet(meta, data);
		StorageUtils utils = new StorageUtils(new MockConnection());
		Properties expected = new Properties();
		Properties actual;

		meta.add("Col1", java.lang.String.class.getName());
		meta.add("Col2", java.lang.Integer.class.getName());
		meta.add("Col3", java.lang.Float.class.getName());
		
		data.add("Batman");
		data.add(42);
		data.add(1.2);
		
		expected.put("Col1", "Batman");
		
		actual = utils.resultSetRowToProperties(rs);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testResultSetRowToPropertiesEmptyRow() {
		MockResultSetMetaData meta = new MockResultSetMetaData();
		ArrayList<Object> data = new ArrayList<>();
		MockResultSet rs = new MockResultSet(meta, data);
		StorageUtils utils = new StorageUtils(new MockConnection());
		Properties expected = new Properties();
		Properties actual;
		
		actual = utils.resultSetRowToProperties(rs);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testResultSetRowToPropertiesClassAndPropertiesMismatch() {
		MockResultSetMetaData meta = new MockResultSetMetaData();
		ArrayList<Object> data = new ArrayList<>();
		MockResultSet rs = new MockResultSet(meta, data);
		StorageUtils utils = new StorageUtils(new MockConnection());
		Properties actual;

		meta.add("Col1", java.lang.Integer.class.getName());
		meta.add("Col2", java.lang.Integer.class.getName());
		meta.add("Col3", java.lang.Float.class.getName());
		
		data.add("Batman");
		data.add(42);
		data.add(1.2);
		
		actual = utils.resultSetRowToProperties(rs);
		
		assertNull(actual);
	}

	@Test
	public void testResultSetToProperties() {
		MockResultSetMetaData meta = new MockResultSetMetaData();
		ArrayList<Object> data = new ArrayList<>();
		MockResultSet rs = new MockResultSet(meta, data);
		StorageUtils utils = new StorageUtils(new MockConnection());
		Properties expected = new Properties();
		Properties actual;

		meta.add("Col1", java.lang.String.class.getName());
		meta.add("Col2", java.lang.Integer.class.getName());
		meta.add("Col3", java.lang.Float.class.getName());
		
		data.add("Batman");
		data.add(42);
		data.add(1.2);
		
		expected.put("Col1", "Batman");
		expected.put("Col2", 42);
		expected.put("Col3", 1.3);
		
		actual = utils.resultSetRowToProperties(rs);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testResultSetToPropertiesOnlyOneRow() {
		MockResultSetMetaData meta = new MockResultSetMetaData();
		ArrayList<Object> data = new ArrayList<>();
		MockResultSet rs = new MockResultSet(meta, data);
		StorageUtils utils = new StorageUtils(new MockConnection());
		Properties expected = new Properties();
		Properties actual;

		meta.add("Col1", java.lang.String.class.getName());
		
		data.add("Batman");
		
		expected.put("Col1", "Batman");
		
		actual = utils.resultSetRowToProperties(rs);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testResultSetToPropertiesEmptySet() {
		MockResultSetMetaData meta = new MockResultSetMetaData();
		ArrayList<Object> data = new ArrayList<>();
		MockResultSet rs = new MockResultSet(meta, data);
		StorageUtils utils = new StorageUtils(new MockConnection());
		Properties expected = new Properties();
		Properties actual;
		
		actual = utils.resultSetRowToProperties(rs);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testResultSetToPropertiesClassAndPropertiesMismatch() {
		MockResultSetMetaData meta = new MockResultSetMetaData();
		ArrayList<Object> data = new ArrayList<>();
		MockResultSet rs = new MockResultSet(meta, data);
		StorageUtils utils = new StorageUtils(new MockConnection());
		Properties actual;

		meta.add("Col1", java.lang.String.class.getName());
		meta.add("Col2", java.lang.String.class.getName());
		meta.add("Col3", java.lang.Float.class.getName());
		
		data.add("Batman");
		data.add(42);
		data.add(1.2);
		
		actual = utils.resultSetRowToProperties(rs);
		
		assertNull(actual);
	}

	@Test
	public void testLookupUUIDTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testLookupUUIDTableNonExistentUUID() {
		fail("Not yet implemented");
	}

	@Test
	public void testLookupUUIDTableMalformedUUID() {
		fail("Not yet implemented");
	}

	@Test
	public void testLookupUUIDTableNoUUID() {
		fail("Not yet implemented");
	}

	@Test
	public void testPairUUIDWithTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testPairUUIDWithTableBadTableName() {
		fail("Not yet implemented");
	}

	@Test
	public void testPairUUIDWithTableNoTableName() {
		fail("Not yet implemented");
	}

	@Test
	public void testPairUUIDWithTableBadUUID() {
		fail("Not yet implemented");
	}

	@Test
	public void testPairUUIDWithTableNoUUID() {
		fail("Not yet implemented");
	}

}
