package se.mah.elis.impl.service.storage.test;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
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
			
			// Truncate stuff
			statement.execute("TRUNCATE TABLE object_lookup_table;");
			statement.execute("TRUNCATE TABLE user_bindings;");
			statement.execute("TRUNCATE TABLE collections;");
			
			// Object lookup table
			// c3677d61-2378-4183-b478-ec915fd32e60
			statement.execute("INSERT INTO object_lookup_table VALUES (x'c3677d6123784183b478ec915fd32e60', 'table1')");
			// c3677d61-2378-4183-b478-ec915fd32e42
			statement.execute("INSERT INTO object_lookup_table VALUES (x'c3677d6123784183b478ec915fd32e42', 'table2')");
			// c3677d61-2378-4183-b478-ec915fd32e13
			statement.execute("INSERT INTO object_lookup_table VALUES (x'c3677d6123784183b478ec915fd32e13', 'table3')");
			// c3677d61-2378-4183-b478-ec915fd32e11
			statement.execute("INSERT INTO object_lookup_table VALUES (x'c3677d6123784183b478ec915fd32e11', 'table1')");
			// c3677d61-2378-4183-b478-ec915fd32e17
			statement.execute("INSERT INTO object_lookup_table VALUES (x'c3677d6123784183b478ec915fd32e17', 'table1')");
			// c3677d61-2378-4183-b478-ec915fd32e05
			statement.execute("INSERT INTO object_lookup_table VALUES (x'c3677d6123784183b478ec915fd32e05', 'table2')");
			
			// Collections
			// c3677d61-2378-4183-b478-ec915fd32e60 <>- c3677d61-2378-4183-b478-ec915fd32e42
			statement.execute("INSERT INTO collections VALUES (x'c3677d6123784183b478ec915fd32e60', x'c3677d6123784183b478ec915fd32e42', 'collection1')");
			// c3677d61-2378-4183-b478-ec915fd32e60 <>- c3677d61-2378-4183-b478-ec915fd32e13
			statement.execute("INSERT INTO collections VALUES (x'c3677d6123784183b478ec915fd32e60', x'c3677d6123784183b478ec915fd32e13', 'collection1')");
			// c3677d61-2378-4183-b478-ec915fd32e17 <>- c3677d61-2378-4183-b478-ec915fd32e42
			statement.execute("INSERT INTO collections VALUES (x'c3677d6123784183b478ec915fd32e17', x'c3677d6123784183b478ec915fd32e42', 'collection1')");
			// c3677d61-2378-4183-b478-ec915fd32e17 <>- c3677d61-2378-4183-b478-ec915fd32e13
			statement.execute("INSERT INTO collections VALUES (x'c3677d6123784183b478ec915fd32e17', x'c3677d6123784183b478ec915fd32e13', 'collection2')");
			// c3677d61-2378-4183-b478-ec915fd32e17 <>- c3677d61-2378-4183-b478-ec915fd32e11
			statement.execute("INSERT INTO collections VALUES (x'c3677d6123784183b478ec915fd32e17', x'c3677d6123784183b478ec915fd32e11', 'collection2')");
			
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
	
	private int countUserBindingsInDB() {
		Statement statement;
		int bindings = -1;
		
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT count(*) FROM user_bindings");
			rs.next();
			bindings = rs.getInt(1);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bindings;
	}
	
	private int countObjectsInCollections() {
		Statement statement;
		int count = -1;
		
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT count(*) FROM collections");
			rs.next();
			count = rs.getInt(1);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	private int countObjectsInCollection(UUID collector, String collection) {
		Statement statement;
		int count = -1;
		
		try {
			statement = connection.createStatement();
			String query = "SELECT count(*) FROM collections WHERE " +
					"collecting_object = x'" + collector.toString().replace("-", "") + "' AND " +
					"collection_name = '" + collection + "';";
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
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
		props.put("col 5", UUID.randomUUID());
		
		expected = "?, ?, ?, ?, x?";
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
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, 1, index, false);
			index = utils.addParameter(stmt, "horses", index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Int: 1", (String) objectStore.get(0));
		assertEquals("String: horses", (String) objectStore.get(1));
		assertEquals(2, index);
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

			utils.addParameter(stmt, "horses", -1, false);
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
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, null, index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Null: 0", (String) objectStore.get(0));
		assertEquals(1, index);
	}

	@Test
	public void testAddParameterBoolean() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, true, index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Boolean: true", (String) objectStore.get(0));
		assertEquals(1, index);
	}

	@Test
	public void testAddParameterByte() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, (byte) 12, index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Byte: 12", (String) objectStore.get(0));
		assertEquals(1, index);
	}

	@Test
	public void testAddParameterInteger() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, 42, index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Int: 42", (String) objectStore.get(0));
		assertEquals(1, index);
	}

	@Test
	public void testAddParameterLong() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, (long) 42, index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Long: 42", (String) objectStore.get(0));
		assertEquals(1, index);
	}

	@Test
	public void testAddParameterFloat() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, (float) 4.2, index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Float: 4.2", (String) objectStore.get(0));
		assertEquals(1, index);
	}

	@Test
	public void testAddParameterDouble() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, (double) 4.2, index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Double: 4.2", (String) objectStore.get(0));
		assertEquals(1, index);
	}

	@Test
	public void testAddParameterString() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, "horses", index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("String: horses", (String) objectStore.get(0));
		assertEquals(1, index);
	}

	@Test
	public void testAddParameterStringWithWildcards() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, "horses", index, true);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("String: %horses%", (String) objectStore.get(0));
		assertEquals(1, index);
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
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, dt, index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("Timestamp: " + ts, (String) objectStore.get(0));
		assertEquals(1, index);
	}

	@Test
	public void testAddParameterUUID() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		String expected = StorageUtils.stripDashesFromUUID(uuid);
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		byte[] bytes = null;
		int index = 0;

		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		bytes = bb.array();
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, uuid, index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
		}
		
		assertEquals("String: " + expected, (String) objectStore.get(0));
		assertEquals(1, index);
	}

	@Test
	public void testAddParameterCollection() {
		MockConnection connection = new MockConnection();
		StorageUtils utils = new StorageUtils(connection);
		ArrayList<Object> objectStore = connection.getObjectStore();
		String query = "INSERT INTO object_lookup_table VALUES(?, ?);";
		PreparedStatement stmt = null;
		Collection collection = new ArrayList();
		int index = 0;
		
		try {
			stmt = connection.prepareStatement(query);

			index = utils.addParameter(stmt, collection, index, false);
		} catch (SQLException e) {
			// This should NEVER happen with a mock object.
			fail("This shouldn't happen");
		}
		
		assertEquals(0, index);
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

		meta.add("Col1", Types.VARCHAR, java.lang.String.class.getName());
		meta.add("Col2", Types.INTEGER, java.lang.Integer.class.getName());
		meta.add("Col3", Types.FLOAT, java.lang.Float.class.getName());
		meta.add("Col4", Types.VARBINARY, byte[].class.getName());
		
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

		meta.add("Col1", Types.INTEGER, java.lang.Integer.class.getName());
		meta.add("Col2", Types.INTEGER, java.lang.Integer.class.getName());
		meta.add("Col3", Types.FLOAT, java.lang.Float.class.getName());
		
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

		meta.add("Col1", Types.VARCHAR, java.lang.String.class.getName());
		meta.add("Col2", Types.INTEGER, java.lang.Integer.class.getName());
		meta.add("Col3", Types.FLOAT, java.lang.Float.class.getName());
		
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

		meta.add("Col1", Types.VARCHAR, java.lang.String.class.getName());
		
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

		meta.add("Col1", Types.VARCHAR, java.lang.String.class.getName());
		meta.add("Col2", Types.VARCHAR, java.lang.String.class.getName());
		meta.add("Col3", Types.FLOAT, java.lang.Float.class.getName());
		
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
	
	@Test
	public void testCoupleUsers() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID user = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		try {
			utils.coupleUsers(platformUser, user);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(1, countUserBindingsInDB());
	}
	
	@Test
	public void testCoupleUsersSeveralCouples() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID platformUser2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		UUID user1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID user2 = UUID.fromString("00001111-2222-3333-4444-555566667778");
		
		try {
			utils.coupleUsers(platformUser1, user1);
			utils.coupleUsers(platformUser1, user2);
			utils.coupleUsers(platformUser2, user1);
			utils.coupleUsers(platformUser2, user2);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(4, countUserBindingsInDB());
	}
	
	@Test
	public void testCoupleUsersExistingCouple() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID platformUser2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		UUID user1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID user2 = UUID.fromString("00001111-2222-3333-4444-555566667778");
		
		try {
			utils.coupleUsers(platformUser1, user1);
			utils.coupleUsers(platformUser1, user2);
			utils.coupleUsers(platformUser2, user1);
			utils.coupleUsers(platformUser2, user2);
			utils.coupleUsers(platformUser1, user1); // Existing couple!
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(4, countUserBindingsInDB());
	}
	
	@Test
	public void testDecoupleUsers() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID platformUser2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		UUID user1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID user2 = UUID.fromString("00001111-2222-3333-4444-555566667778");
		
		try {
			utils.coupleUsers(platformUser1, user1);
			utils.coupleUsers(platformUser1, user2);
			utils.coupleUsers(platformUser2, user1);
			utils.coupleUsers(platformUser2, user2);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		utils.decoupleUsers(platformUser1, user2);
		
		assertEquals(3, countUserBindingsInDB());
	}
	
	@Test
	public void testDecoupleUsersNonExistingCouple() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID platformUser2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		UUID user1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID user2 = UUID.fromString("00001111-2222-3333-4444-555566667778");
		
		try {
			utils.coupleUsers(platformUser1, user1);
			utils.coupleUsers(platformUser1, user2);
			utils.coupleUsers(platformUser2, user1);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		utils.decoupleUsers(platformUser2, user2);
		
		assertEquals(3, countUserBindingsInDB());
	}
	
	@Test
	public void testGetUsersAssociatedWithPlatformUserOneCouple() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID platformUser2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		UUID user1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID user2 = UUID.fromString("00001111-2222-3333-4444-555566667778");
		UUID[] expected = new UUID[1];
		UUID[] actual = null;
		
		expected[0] = user1;
		
		try {
			utils.coupleUsers(platformUser1, user1);
			utils.coupleUsers(platformUser1, user2);
			utils.coupleUsers(platformUser2, user1);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		actual = utils.getUsersAssociatedWithPlatformUser(platformUser2);
		
		assertNotNull(actual);
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetUsersAssociatedWithPlatformUserSeveral() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID platformUser2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		UUID user1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID user2 = UUID.fromString("00001111-2222-3333-4444-555566667778");
		UUID[] expected = new UUID[2];
		UUID[] actual = null;
		
		expected[0] = user1;
		expected[1] = user2;
		
		try {
			utils.coupleUsers(platformUser1, user1);
			utils.coupleUsers(platformUser1, user2);
			utils.coupleUsers(platformUser2, user1);
			utils.coupleUsers(platformUser2, user2);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		actual = utils.getUsersAssociatedWithPlatformUser(platformUser1);
		
		assertNotNull(actual);
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetUsersAssociatedWithPlatformUserNoCouples() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID platformUser2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		UUID user1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID user2 = UUID.fromString("00001111-2222-3333-4444-555566667778");
		UUID[] expected = new UUID[0];
		UUID[] actual = null;
		
		try {
			utils.coupleUsers(platformUser1, user1);
			utils.coupleUsers(platformUser1, user2);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		actual = utils.getUsersAssociatedWithPlatformUser(platformUser2);
		
		assertNotNull(actual);
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetPlatformUsersAssociatedWithUserOneCouple() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID platformUser2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		UUID user1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID user2 = UUID.fromString("00001111-2222-3333-4444-555566667778");
		UUID[] expected = new UUID[1];
		UUID[] actual = null;
		
		expected[0] = platformUser1;
		
		try {
			utils.coupleUsers(platformUser1, user1);
			utils.coupleUsers(platformUser1, user2);
			utils.coupleUsers(platformUser2, user1);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		actual = utils.getPlatformUsersAssociatedWithUser(user2);
		
		assertNotNull(actual);
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetPlatformUsersAssociatedWithUserSeveralCouples() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID platformUser2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		UUID user1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID user2 = UUID.fromString("00001111-2222-3333-4444-555566667778");
		UUID[] expected = new UUID[2];
		UUID[] actual = null;

		expected[0] = platformUser1;
		expected[1] = platformUser2;
		
		try {
			utils.coupleUsers(platformUser1, user1);
			utils.coupleUsers(platformUser1, user2);
			utils.coupleUsers(platformUser2, user1);
			utils.coupleUsers(platformUser2, user2);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		actual = utils.getPlatformUsersAssociatedWithUser(user1);
		
		assertNotNull(actual);
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetPlatformUsersAssociatedWithUserNoCouples() {
		setUpDatabase();
		
		StorageUtils utils = new StorageUtils(connection);
		UUID platformUser1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID platformUser2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		UUID user1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID user2 = UUID.fromString("00001111-2222-3333-4444-555566667778");
		UUID[] expected = new UUID[0];
		UUID[] actual = null;
		
		try {
			utils.coupleUsers(platformUser1, user1);
			utils.coupleUsers(platformUser2, user1);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		actual = utils.getPlatformUsersAssociatedWithUser(user2);
		
		assertNotNull(actual);
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testValidateEDOProperties() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertTrue(StorageUtils.validateEDOProperties(props, true));
	}
	
	@Test
	public void testListCollectedObjects() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		StorageUtils utils = new StorageUtils(connection);
		UUID[] uuids = utils.listCollectedObjects(uuid, "collection1");
		
		assertEquals(2, uuids.length);
		assertTrue(uuids[0] instanceof UUID);
	}
	
	@Test
	public void testListCollectedObjectsCollectorHasMoreThanOneCollection() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e17");
		StorageUtils utils = new StorageUtils(connection);
		UUID[] uuids1 = utils.listCollectedObjects(uuid, "collection1");
		UUID[] uuids2 = utils.listCollectedObjects(uuid, "collection2");
		
		assertEquals(1, uuids1.length);
		assertTrue(uuids1[0] instanceof UUID);
		assertEquals(2, uuids2.length);
		assertTrue(uuids2[0] instanceof UUID);
	}
	
	@Test
	public void testListCollectedObjectsNonExistingCollection() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		StorageUtils utils = new StorageUtils(connection);
		UUID[] uuids = utils.listCollectedObjects(uuid, "collection3");
		
		assertNotNull(uuids);
		assertEquals(0, uuids.length);
	}
	
	@Test
	public void testListCollectedObjectsNoObjectsInCollection() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e60");
		StorageUtils utils = new StorageUtils(connection);
		UUID[] uuids = utils.listCollectedObjects(uuid, "collection1");
		
		assertNotNull(uuids);
		assertEquals(0, uuids.length);
	}
	
	@Test
	public void testDeleteCollections() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		StorageUtils utils = new StorageUtils(connection);
		
		utils.deleteCollections(uuid);
		
		assertEquals(3, countObjectsInCollections());
	}
	
	@Test
	public void testDeleteCollectionsNonExistingCollector() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e60");
		StorageUtils utils = new StorageUtils(connection);
		
		utils.deleteCollections(uuid);
		
		assertEquals(5, countObjectsInCollections());
	}
	
	@Test
	public void testDeleteCollectionsNonExistingCollectee() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e60");
		StorageUtils utils = new StorageUtils(connection);
		
		utils.deleteCollections(uuid);
		
		assertEquals(5, countObjectsInCollections());
	}
	
	@Test
	public void testDeleteFromCollections() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e13");
		StorageUtils utils = new StorageUtils(connection);
		
		utils.deleteFromCollections(uuid);
		
		assertEquals(3, countObjectsInCollections());
	}
	
	@Test
	public void testDeleteFromCollectionsNonExistingCollector() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e13");
		StorageUtils utils = new StorageUtils(connection);
		
		utils.deleteFromCollections(uuid);
		
		assertEquals(5, countObjectsInCollections());
	}
	
	@Test
	public void testDeleteFromCollectionsNonExistingCollectee() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("deadbeef-2378-4183-b478-ec915fd32e13");
		StorageUtils utils = new StorageUtils(connection);
		
		utils.deleteFromCollections(uuid);
		
		assertEquals(5, countObjectsInCollections());
	}
	
	@Test
	public void testUpdateCollectionNoChanges() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		ArrayList<UUID> collection = new ArrayList<UUID>();
		StorageUtils utils = new StorageUtils(connection);

		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e42"));
		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e13"));
		
		utils.updateCollection(uuid, collection, "collection1");

		assertEquals(5, countObjectsInCollections());
		assertEquals(2, countObjectsInCollection(uuid, "collection1"));
	}
	
	@Test
	public void testUpdateCollectionAddOne() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		ArrayList<UUID> collection = new ArrayList<UUID>();
		StorageUtils utils = new StorageUtils(connection);

		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e42"));
		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e13"));
		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e05")); // <- new
		
		utils.updateCollection(uuid, collection, "collection1");

		assertEquals(6, countObjectsInCollections());
		assertEquals(3, countObjectsInCollection(uuid, "collection1"));
	}
	
	@Test
	public void testUpdateCollectionAddTwo() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		ArrayList<UUID> collection = new ArrayList<UUID>();
		StorageUtils utils = new StorageUtils(connection);

		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e42"));
		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e13"));
		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e05")); // <- new
		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e17")); // <- new
		
		utils.updateCollection(uuid, collection, "collection1");

		assertEquals(7, countObjectsInCollections());
		assertEquals(4, countObjectsInCollection(uuid, "collection1"));
	}
	
	@Test
	public void testUpdateCollectionRemoveOne() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		ArrayList<UUID> collection = new ArrayList<UUID>();
		StorageUtils utils = new StorageUtils(connection);

		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e42"));
		// collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e13")); <- not added, i.e. removed
		
		utils.updateCollection(uuid, collection, "collection1");

		assertEquals(4, countObjectsInCollections());
		assertEquals(1, countObjectsInCollection(uuid, "collection1"));
	}
	
	@Test
	public void testUpdateCollectionRemoveTwo() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		ArrayList<UUID> collection = new ArrayList<UUID>();
		StorageUtils utils = new StorageUtils(connection);

		// collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e42")); <- not added, i.e. removed
		// collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e13")); <- not added, i.e. removed
		
		utils.updateCollection(uuid, collection, "collection1");

		assertEquals(3, countObjectsInCollections());
		assertEquals(0, countObjectsInCollection(uuid, "collection1"));
	}
	
	@Test
	public void testUpdateCollectionRemoveOneAddOne() {
		setUpDatabase();
		
		UUID uuid = UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e60");
		ArrayList<UUID> collection = new ArrayList<UUID>();
		StorageUtils utils = new StorageUtils(connection);

		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e42"));
		// collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e13")); <- not added, i.e. removed
		collection.add(UUID.fromString("c3677d61-2378-4183-b478-ec915fd32e05")); // <- new
		
		utils.updateCollection(uuid, collection, "collection1");

		assertEquals(5, countObjectsInCollections());
		assertEquals(2, countObjectsInCollection(uuid, "collection1"));
	}
	
	@Test
	public void testValidateEDOPropertiesNoDataId() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("ownerid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, true));
	}
	
	@Test
	public void testValidateEDOPropertiesDataIdIsNotUUID() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", "00001111-2222-3333-4444-555566667777");
		props.put("ownerid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, true));
	}
	
	@Test
	public void testValidateEDOPropertiesDataIdIsNotFirstObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("alice", "bob");
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, true));
	}
	
	@Test
	public void testValidateEDOPropertiesNoOwnerId() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, true));
	}
	
	@Test
	public void testValidateEDOPropertiesOwnerIdIsNotUUID() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", "00001111-2222-3333-4444-555566667777");
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, true));
	}
	
	@Test
	public void testValidateEDOPropertiesNoCreated() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, true));
	}
	
	@Test
	public void testValidateEDOPropertiesCreatedIsNotJodaDateTime() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", "2014-03-20 19:31:00");
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, true));
	}
	
	@Test
	public void testValidateEDOPropertiesNoExtraElement() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", DateTime.now());
		
		assertFalse(StorageUtils.validateEDOProperties(props, true));
	}
	
	@Test
	public void testValidateEDOPropertiesNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("ownerid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertTrue(StorageUtils.validateEDOProperties(props, false));
	}
	
	@Test
	public void testValidateEDOPropertiesNoOwnerIdNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, false));
	}
	
	@Test
	public void testValidateEDOPropertiesOwnerIdIsNotUUIDNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("ownerid", "00001111-2222-3333-4444-555566667777");
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, false));
	}
	
	@Test
	public void testValidateEDOPropertiesNoCreatedNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("ownerid", UUID.randomUUID());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, false));
	}
	
	@Test
	public void testValidateEDOPropertiesCreatedIsNotJodaDateTimeNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("ownerid", UUID.randomUUID());
		props.put("created", "2014-03-20 19:31:00");
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateEDOProperties(props, false));
	}
	
	@Test
	public void testValidateEDOPropertiesNoExtraElementNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("ownerid", UUID.randomUUID());
		props.put("created", DateTime.now());
		
		assertFalse(StorageUtils.validateEDOProperties(props, false));
	}
	
	@Test
	public void testValidateAbstractUserProperties() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("service_name", "mock_service");
		props.put("foo", "bar");
		
		assertTrue(StorageUtils.validateAbstractUserProperties(props, true));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesNoUUID() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("created", DateTime.now());
		props.put("service_name", "mock_service");
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, true));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesUUIDIsNotUUID() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", "00001111-2222-3333-4444-555566667777");
		props.put("created", DateTime.now());
		props.put("service_name", "mock_service");
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, true));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesUUIDIsNotFirstObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("alice", "bob");
		props.put("uuid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("service_name", "mock_service");
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, true));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesNoServiceName() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, true));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesServiceNameIsNotString() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("service_name", new Integer(42));
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, true));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesNoCreated() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", UUID.randomUUID());
		props.put("service_name", "mock_service");
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, true));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesCreatedIsNotJodaDateTime() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", UUID.randomUUID());
		props.put("created", "2014-03-20 19:35:00");
		props.put("service_name", "mock_service");
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, true));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesNoExtraElement() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", UUID.randomUUID());
		props.put("created", DateTime.now());
		props.put("service_name", "mock_service");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, true));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("created", DateTime.now());
		props.put("service_name", "mock_service");
		props.put("foo", "bar");
		
		assertTrue(StorageUtils.validateAbstractUserProperties(props, false));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesNoServiceNameNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("created", DateTime.now());
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, false));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesServiceNameIsNotStringNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("created", DateTime.now());
		props.put("service_name", new Integer(42));
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, false));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesNoCreatedNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("service_name", "mock_service");
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, false));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesCreatedIsNotJodaDateTimeNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("created", "2014-03-20 19:35:00");
		props.put("service_name", "mock_service");
		props.put("foo", "bar");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, false));
	}
	
	@Test
	public void testValidateAbstractUserPropertiesNoExtraElementNewObject() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("created", DateTime.now());
		props.put("service_name", "mock_service");
		
		assertFalse(StorageUtils.validateAbstractUserProperties(props, false));
	}
}
