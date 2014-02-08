package se.mah.elis.impl.service.storage.test;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
import se.mah.elis.services.storage.exceptions.StorageException;

public class StorageUtilsTest {
	
	private Connection connection = null;
	private static int INITIAL_ROW_COUNT = 6;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			connection = null;
		}
	}
	
	private void setUpDatabase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection =  DriverManager
			          .getConnection("jdbc:mysql://localhost/elis_test?"
			                  + "user=elis_test&password=elis_test");
			Statement statement = connection.createStatement();
			statement.execute("TRUNCATE TABLE object_lookup_table;");
			// c3677d61-2378-4183-b478-ec915fd32e60
			statement.execute("INSERT INTO object_lookup_table VALUES (UNHEX('c3677d6123784183b478ec915fd32e60'), 'table1')");
			// c3677d61-2378-4183-b478-ec915fd32e42
			statement.execute("INSERT INTO object_lookup_table VALUES (UNHEX('c3677d6123784183b478ec915fd32e42'), 'table2')");
			// c3677d61-2378-4183-b478-ec915fd32e13
			statement.execute("INSERT INTO object_lookup_table VALUES (UNHEX('c3677d6123784183b478ec915fd32e13'), 'table3')");
			// c3677d61-2378-4183-b478-ec915fd32e11
			statement.execute("INSERT INTO object_lookup_table VALUES (UNHEX('c3677d6123784183b478ec915fd32e11'), 'table1')");
			// c3677d61-2378-4183-b478-ec915fd32e17
			statement.execute("INSERT INTO object_lookup_table VALUES (UNHEX('c3677d6123784183b478ec915fd32e17'), 'table1')");
			// c3677d61-2378-4183-b478-ec915fd32e05
			statement.execute("INSERT INTO object_lookup_table VALUES (UNHEX('c3677d6123784183b478ec915fd32e05'), 'table2')");
			statement.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int countBindingsInDB() {
		Statement statement;
		int bindings = -1;
		
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT count(*) FROM object_lookup_table");
			rs.next();
			bindings = rs.getInt(1);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bindings;
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
		
		assertEquals(0, objectStore.size());
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
		
		assertEquals(0, objectStore.size());
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
		
		assertEquals(0, objectStore.size());
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
		
		assertEquals(0, objectStore.size());
	}

	@Test
	public void testCreateTableIfNotExistingNoProperties() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		String tableName = "";
		Properties props = new OrderedProperties();
		ArrayList<Object> objectStore = connection.getObjectStore();
		
		utils.createTableIfNotExisting(tableName, props);
		
		assertEquals(0, objectStore.size());
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
		actual = StorageUtils.generateQMarks(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGenerateQMarksOnlyOne() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		props.put("col 1", 42);
		
		expected = "?";
		actual = StorageUtils.generateQMarks(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGenerateQMarksNone() {
		Properties props = new OrderedProperties();
		String expected, actual;

		expected = "";
		actual = StorageUtils.generateQMarks(props);

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
		} catch (SQLException |IndexOutOfBoundsException e) {
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
		
		assertEquals("Null: 0", (String) objectStore.get(0));
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

			utils.addParameter(stmt, (float) 4.2, 0);
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
		
		assertEquals("Double: 4.2", (String) objectStore.get(0));
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
		java.sql.Timestamp ts = new java.sql.Timestamp(dt.getMillis());
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(query);

			utils.addParameter(stmt, dt, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Timestamp: " + ts, (String) objectStore.get(0));
	}

	@Test
	public void testAddParameterUUID() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		StringBuffer expected = new StringBuffer();
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		byte[] bytes = null;

		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		bytes = bb.array();
		
		for (int i = 0; i < bytes.length; i++) {
			expected.append(bytes[i]);
		}
		
		try {
			stmt = connection.prepareStatement(query);

			utils.addParameter(stmt, uuid, 0);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Byte[]: " + expected.toString(), (String) objectStore.get(0));
	}

//	@Test
//	public void testAddParameterObject() {
//		// TODO We need to discuss this behaviour
//		fail("Not yet implemented");
//	}

	@Test
	public void testPairUp() {
		Properties props = new OrderedProperties();
		String expected, actual;
		
		props.put("col 1", 42);
		props.put("col 2", "Batman!");
		props.put("col 3", false);
		props.put("col 4", 1.3);
		
		expected = "`col_1` = ?, `col_2` = ?, `col_3` = ?, `col_4` = ?";
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
		
		expected = "`col_1` = ?";
		actual = StorageUtils.pairUp(props);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testUuidToBytes() {
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		byte[] actual = StorageUtils.uuidToBytes(uuid);
		byte[] expected = null;
		
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		expected = bb.array();
		
		for (int i = 0; i < 16; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}

	@Test
	public void testResultSetRowToProperties() {
		MockResultSetMetaData meta = new MockResultSetMetaData();
		ArrayList<Object> data = new ArrayList<>();
		MockResultSet rs = new MockResultSet(meta, data);
		StorageUtils utils = new StorageUtils(new MockConnection());
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e42");
		
		Properties expected = new Properties();
		Properties actual;

		meta.add("Col1", java.lang.String.class.getName());
		meta.add("Col2", java.lang.Integer.class.getName());
		meta.add("Col3", java.lang.Float.class.getName());
		meta.add("Col4", byte[].class.getName());
		
		data.add("Batman");
		data.add(42);
		data.add((float) 1.2);
		data.add(StorageUtils.uuidToBytes(uuid));
		
		expected.put("Col1", "Batman");
		
		actual = utils.resultSetRowToProperties(rs);
		
		assertEquals(4, actual.size());
		assertTrue(actual.containsKey("Col1"));
		assertEquals("Batman", actual.get("Col1"));
		assertTrue(actual.containsKey("Col2"));
		assertEquals(42, actual.get("Col2"));
		assertTrue(actual.containsKey("Col3"));
		assertEquals((float) 1.2, actual.get("Col3"));
		assertTrue(actual.containsKey("Col4"));
		assertEquals(uuid, actual.get("Col4"));
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
		Properties actual = null;

		meta.add("Col1", java.lang.Integer.class.getName());
		meta.add("Col2", java.lang.Integer.class.getName());
		meta.add("Col3", java.lang.Float.class.getName());
		
		data.add("Batman");
		data.add(42);
		data.add((float) 1.2);
		
		try {
			actual = utils.resultSetRowToProperties(rs);
			fail("this shouldn't happen");
		} catch (ClassCastException e) {
		}
		
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
		data.add((float) 1.3);
		
		expected.put("Col1", "Batman");
		expected.put("Col2", 42);
		expected.put("Col3", (float) 1.3);
		
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
		Properties actual = null;

		meta.add("Col1", java.lang.String.class.getName());
		meta.add("Col2", java.lang.String.class.getName());
		meta.add("Col3", java.lang.Float.class.getName());
		
		data.add("Batman");
		data.add(42);
		data.add((float) 1.2);
		
		try {
			actual = utils.resultSetRowToProperties(rs);
			fail("this shouldn't happen");
		} catch (ClassCastException e) {
		}
		
		assertNull(actual);
	}
	
	// The tests below need a database to work against. For this, we use a
	// MySQL database, called elis_test, username and password are both
	// elis_test.
	
	@Test
	public void testLookupUUIDTable() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e42");
		String expected = "table2";
		String actual = utils.lookupUUIDTable(uuid);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testLookupUUIDTableNonExistentUUID() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		String actual = utils.lookupUUIDTable(uuid);
		
		assertNull(actual);
	}

	@Test
	public void testPairUUIDWithTable() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e42");
		String expected = "table2";
		String actual = utils.lookupUUIDTable(uuid);
		
		assertNull(actual);
		
		try {
			utils.pairUUIDWithTable(uuid, expected);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		actual = utils.lookupUUIDTable(uuid);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testPairUUIDWithTableNoTableName() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e42");
		String table = "";
		String actual = utils.lookupUUIDTable(uuid);
		
		assertNull(actual);
		
		try {
			utils.pairUUIDWithTable(uuid, table);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		actual = utils.lookupUUIDTable(uuid);
		
		assertNull(actual);
	}

	@Test
	public void testPairUUIDWithTableNoUUID() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e42");
		String expected = "table2";
		String actual = utils.lookupUUIDTable(uuid);
		
		assertNull(actual);
		
		try {
			utils.pairUUIDWithTable(null, expected);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		actual = utils.lookupUUIDTable(uuid);
		
		assertNull(actual);
	}

	@Test
	public void testPairUUIDWithTableDuplicateUUID() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e42");
		String expected = "table2";
		String actual = utils.lookupUUIDTable(uuid);
		
		assertEquals(expected, actual);
		
		try {
			utils.pairUUIDWithTable(null, expected);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		actual = utils.lookupUUIDTable(uuid);
		
		assertEquals(INITIAL_ROW_COUNT, countBindingsInDB());
	}

	@Test
	public void testFreeUUID() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e42");
		String expected = "table2";
		String actual = utils.lookupUUIDTable(uuid);
		
		assertEquals(expected, actual);
		assertEquals(INITIAL_ROW_COUNT, countBindingsInDB());
		
		utils.freeUUID(uuid);
		actual = utils.lookupUUIDTable(uuid);
		
		assertNull(actual);
		assertEquals(INITIAL_ROW_COUNT - 1, countBindingsInDB());
	}

	@Test
	public void testFreeUUIDNoSuchUUID() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e42");
		String actual = utils.lookupUUIDTable(uuid);
		
		assertNull(actual);
		assertEquals(INITIAL_ROW_COUNT, countBindingsInDB());
		
		utils.freeUUID(uuid);
		actual = utils.lookupUUIDTable(uuid);
		
		assertNull(actual);
		assertEquals(INITIAL_ROW_COUNT, countBindingsInDB());
	}
	
	@Test
	public void testStripDashesFromUUID() {
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e42");
		String expected = "deadbeef23784183b478ec915fd32e42";
		String actual = StorageUtils.stripDashesFromUUID(uuid);
		
		assertEquals(expected, actual);
	}
}
