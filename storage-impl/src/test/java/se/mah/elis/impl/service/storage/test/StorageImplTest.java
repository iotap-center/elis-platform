package se.mah.elis.impl.service.storage.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.impl.service.storage.test.mock.EmptyMockUserIdentifier;
import se.mah.elis.impl.service.storage.test.mock.MockConnection;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject1;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject2;
import se.mah.elis.impl.service.storage.test.mock.MockPlatformUser;
import se.mah.elis.impl.service.storage.test.mock.MockPlatformUserIdentifier;
import se.mah.elis.impl.service.storage.test.mock.MockUser1;
import se.mah.elis.impl.service.storage.test.mock.MockUser2;
import se.mah.elis.impl.service.storage.test.mock.MockUser3;
import se.mah.elis.impl.service.storage.test.mock.MockUserIdentifier;
import se.mah.elis.impl.services.storage.StorageImpl;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.ChainingPredicate;
import se.mah.elis.services.storage.query.Query;
import se.mah.elis.services.storage.query.SimplePredicate;
import se.mah.elis.services.storage.query.ChainingPredicate.ChainingType;
import se.mah.elis.services.storage.query.SimplePredicate.CriterionType;
import se.mah.elis.services.users.AbstractUser;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;

public class StorageImplTest {

	private static int EDO1_COUNT = 6;
	private static int EDO2_COUNT = 3;
	private static int AU1_COUNT = 3;
	private static int AU2_COUNT = 3;
	private static int PU_COUNT = 3;
	
	private Connection connection;
	
	@Before
	public void setUp() throws Exception {
		setUpDatabase();
		tearDownTables();
	}

	@After
	public void tearDown() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setUpDatabase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection =  DriverManager
			          .getConnection("jdbc:mysql://localhost/elis_test?"
			                  + "user=elis_test&password=elis_test");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void buildAndPopulateMDO1Table() {
		String uuid1 = "00001111222233334444555566667777";
		String uuid2 = "00001111222233334444555566667778";
		String uuid3 = "00001111222233334444555566667779";
		String uuid4 = "0000111122223333444455556666777A";
		String uuid5 = "0000111122223333444455556666777B";
		String uuid6 = "0000111122223333444455556666777C";
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` (" +
						"`uuid` VARBINARY(16) PRIMARY KEY, " +
						"`userid` INTEGER, " +
						"`foo` INTEGER, " +
						"`bar` VARCHAR( 16 ))");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (UNHEX('" + uuid1 +"'), 1, 42, 'Baba Roga')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (UNHEX('" + uuid2 +"'), 1, 13, 'Baba Jaga')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (UNHEX('" + uuid3 +"'), 1, 17, 'Jezibaba')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (UNHEX('" + uuid4 +"'), 2, 17, 'Domovoj')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (UNHEX('" + uuid5 +"'), 2, 5, 'Domovik')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (UNHEX('" + uuid6 +"'), 3, 5, 'Perun')");
			
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid1 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid2 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid3 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid4 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid5 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid6 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void buildAndPopulateMDO2Table() {
		String uuid1 = "00001111222233334444555566667771";
		String uuid2 = "00001111222233334444555566667772";
		String uuid3 = "00001111222233334444555566667773";
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` (" +
						"`uuid` VARBINARY(16) PRIMARY KEY, " +
						"`userid` INTEGER, " +
						"`baz` FLOAT)");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` " +
					"VALUES (UNHEX('" + uuid1 +"'), 1, 1.1);");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` " +
					"VALUES (UNHEX('" + uuid2 +"'), 1, 0.5);");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` " +
					"VALUES (UNHEX('" + uuid3 +"'), 2, 7.1);");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid1 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid2 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid3 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject2')");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void buildAndPopulateMU1Table() {
		String uuid1 = "000011112222deadbeef555566667771";
		String uuid2 = "000011112222deadbeef555566667772";
		String uuid3 = "000011112222deadbeef555566667773";
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE `se-mah-elis-impl-service-storage-test-mock-MockUser1` (" +
						"`uuid` VARBINARY(16) PRIMARY KEY, " +
						"`service_name` VARCHAR(9), " +
						"`id_number` INTEGER, " +
						"`username` VARCHAR(32), " +
						"`password` VARCHAR(32), " +
						"`userid` INTEGER, " +
						"`stuff` VARCHAR(32), " +
						"`whatever` INTEGER)");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser1` " +
					"VALUES (UNHEX('" + uuid1 +"'), 'MockUser1', 1, 'Batman', 'Robin', 42, 'Rajec', 21);");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser1` " +
					"VALUES (UNHEX('" + uuid2 +"'), 'MockUser1', 1, 'Superman', 'Lois Lane', 13, 'Vinea', 22);");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser1` " +
					"VALUES (UNHEX('" + uuid3 +"'), 'MockUser1', 1, 'Spongebob Squarepants', 'Patrick Seastar', 17, 'Kofola', 23);");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid1 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid2 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid3 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser1')");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void buildAndPopulateMU2Table() {
		String uuid1 = "0000deadbeef33334444555566667771";
		String uuid2 = "0000deadbeef33334444555566667772";
		String uuid3 = "0000deadbeef33334444555566667773";
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE `se-mah-elis-impl-service-storage-test-mock-MockUser2` (" +
						"`uuid` VARBINARY(16) PRIMARY KEY, " +
						"`service_name` VARCHAR(9), " +
						"`id_number` INTEGER, " +
						"`username` VARCHAR(32), " +
						"`password` VARCHAR(32), " +
						"`userid` INTEGER, " +
						"`stuff` VARCHAR(32))");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser2` " +
					"VALUES (UNHEX('" + uuid1 +"'), 'MockUser2', 1, 'Batman', 'Robin', 42, 'Kvass');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser2` " +
					"VALUES (UNHEX('" + uuid2 +"'), 'MockUser2', 1, 'Superman', 'Lois Lane', 13, 'Kompot');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser2` " +
					"VALUES (UNHEX('" + uuid3 +"'), 'MockUser2', 1, 'Spongebob Squarepants', 'Patrick Seastar', 17, 'Slivovice');");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid1 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid2 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid3 +"'), " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser2')");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void populatePUTable() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("INSERT INTO `se-mah-elis-services-users-PlatformUser` " +
					"VALUES (1, 'Batman', 'Robin', 'Bruce', 'Wayne', 'bruce@waynecorp.com');");
			stmt.execute("INSERT INTO `se-mah-elis-services-users-PlatformUser` " +
					"VALUES (2, 'Superman', 'Lois Lane', 'Clark', 'Kent', 'clark.kent@dailyplanet.com');");
			stmt.execute("INSERT INTO `se-mah-elis-services-users-PlatformUser` " +
					"VALUES (3, 'Spongebob Squarepants', 'Patrick Seastar', 'Spongebob', 'Squarepants', 'spongebob@krustykrab.com');");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void tearDownTables() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("TRUNCATE TABLE object_lookup_table;");
			stmt.execute("TRUNCATE TABLE `se-mah-elis-services-users-PlatformUser`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockDataObject1`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockDataObject2`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockUser1`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockUser2`;");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			if (!(connection instanceof MockConnection)) {
				e.printStackTrace();
			}
		}
	}
	
	private int countBindingsInDB(ElisDataObject edo) {
		Statement statement;
		int bindings = -1;
		String query = "";
		
		if (edo instanceof MockDataObject1) {
			query = "SELECT count(*) FROM `se-mah-elis-impl-service-storage-test-mock-MockDataObject1`";
		} else if (edo instanceof MockDataObject2) {
			query = "SELECT count(*) FROM `se-mah-elis-impl-service-storage-test-mock-MockDataObject2`";
		}
		
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			bindings = rs.getInt(1);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bindings;
	}
	
	private int countBindingsInDB(AbstractUser user) {
		Statement statement;
		int bindings = -1;
		String query = "";
		String table = user.getIdentifier().identifies().getName().replace('.', '-');
		
		if (user instanceof PlatformUser) {
			query = "SELECT count(*) FROM `se-mah-elis-services-users-PlatformUser`";
		} else {
			query = "SELECT count(*) FROM `" + table + "`";
		}
		
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			bindings = rs.getInt(1);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bindings;
	}
	
	private int countBindingsInDB() {
		Statement statement;
		int bindings = -1;
		
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT count(*) FROM `object_lookup_table`");
			rs.next();
			bindings = rs.getInt(1);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bindings;
	}
	
	private void runQuery(String query) {
		Statement statement;
		
		try {
			statement = connection.createStatement();
			statement.execute(query);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStorageImpl() {
		MockConnection connection = new MockConnection();
		Storage storage = new StorageImpl(connection);
		
		assertNotNull(storage);
	}

	@Test
	public void testInsertElisDataObject() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1 mdo = new MockDataObject1();
		
		mdo.setUniqueUserId(1);
		mdo.setBar("Testing");
		mdo.setFoo(42);
		
		try {
			storage.insert(mdo);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT + 1, countBindingsInDB(mdo));
	}

	@Test
	public void testInsertElisDataObjectFirstObjectInTable() {
		Storage storage = new StorageImpl(connection);
		MockDataObject1 mdo = new MockDataObject1();
		
		mdo.setUniqueUserId(1);
		mdo.setBar("Testing");
		mdo.setFoo(42);
		
		try {
			storage.insert(mdo);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(1, countBindingsInDB(mdo));
	}

	@Test
	public void testInsertElisDataObjectAlreadyHasId() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("ffffeeee-dddd-cccc-bbbb-aaaa99998888");
		MockDataObject1 mdo = new MockDataObject1();
		
		mdo.setUUID(uuid);
		mdo.setUniqueUserId(1);
		mdo.setBar("Testing");
		mdo.setFoo(42);
		
		try {
			storage.insert(mdo);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT + 1, countBindingsInDB(mdo));
	}

	/*
	 * This test simulates an object, whose definition has changed in any way
	 * since its initial definition, and is therefore inconsistent with the
	 * data model in the storage engine.
	 */
	@Test
	public void testInsertElisDataObjectDoesntMatchTableDefinition() {
		buildAndPopulateMDO2Table(); // NB! MockDataObject2
		
		// Rename MDO2 table to MDO1
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` TO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1`;");
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1 mdo = new MockDataObject1();
		
		mdo.setUniqueUserId(1);
		mdo.setBar("Testing");
		mdo.setFoo(42);
		
		try {
			storage.insert(mdo);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(EDO2_COUNT, countBindingsInDB(mdo));
	}

	@Test
	public void testInsertElisDataObjectEmptyObject() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1 mdo = new MockDataObject1(null, 0, "");
		
		try {
			storage.insert(mdo);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(EDO1_COUNT, countBindingsInDB(mdo));
	}
	
	@Test
	public void testInsertElisDataObjectAlreadyExists() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		MockDataObject1 mdo = new MockDataObject1();
		
		mdo.setUUID(uuid);
		mdo.setUniqueUserId(1);
		mdo.setBar("Testing");
		mdo.setFoo(42);
		
		try {
			storage.insert(mdo);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(EDO1_COUNT, countBindingsInDB(mdo));
	}

	@Test
	public void testInsertElisDataObjectArray() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1[] mdos = new MockDataObject1[3];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject1 mdo2 = new MockDataObject1();
		MockDataObject1 mdo3 = new MockDataObject1();
		
		mdo1.setUniqueUserId(1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setUniqueUserId(1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setUniqueUserId(2);
		mdo3.setBar("With a herring!");
		mdo3.setFoo(17);
		
		mdos[0] = mdo1;
		mdos[1] = mdo2;
		mdos[2] = mdo3;
		
		try {
			storage.insert(mdos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT + 3, countBindingsInDB(mdo1));
	}

	@Test
	public void testInsertElisDataObjectArrayFirstObjectInTable() {
		Storage storage = new StorageImpl(connection);
		MockDataObject1[] mdos = new MockDataObject1[3];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject1 mdo2 = new MockDataObject1();
		MockDataObject1 mdo3 = new MockDataObject1();
		
		mdo1.setUniqueUserId(1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setUniqueUserId(1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setUniqueUserId(2);
		mdo3.setBar("With a herring!");
		mdo3.setFoo(17);
		
		mdos[0] = mdo1;
		mdos[1] = mdo2;
		mdos[2] = mdo3;
		
		try {
			storage.insert(mdos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(3, countBindingsInDB(mdo1));
	}

	@Test
	public void testInsertElisDataObjectArrayOneAlreadyHasId() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		MockDataObject1[] mdos = new MockDataObject1[3];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject1 mdo2 = new MockDataObject1();
		MockDataObject1 mdo3 = new MockDataObject1();
		
		mdo1.setUniqueUserId(1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setUUID(uuid);
		mdo2.setUniqueUserId(1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setUniqueUserId(2);
		mdo3.setBar("With a herring!");
		mdo3.setFoo(17);
		
		mdos[0] = mdo1;
		mdos[1] = mdo2;
		mdos[2] = mdo3;
		
		try {
			storage.insert(mdos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT + 3, countBindingsInDB(mdo1));
	}

	@Test
	public void testInsertElisDataObjectArrayEmptyArray() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1[] mdos = new MockDataObject1[0];
		
		try {
			storage.insert(mdos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT, countBindingsInDB(new MockDataObject1()));
	}

	@Test
	public void testInsertElisDataObjectArrayDifferentObjectTypes() {
		buildAndPopulateMDO1Table();
		buildAndPopulateMDO2Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		ElisDataObject[] mdos = new ElisDataObject[3];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject1 mdo2 = new MockDataObject1();
		MockDataObject2 mdo3 = new MockDataObject2();
		
		mdo1.setUniqueUserId(1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setUUID(uuid);
		mdo2.setUniqueUserId(1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setUniqueUserId(2);
		mdo3.setBaz((float) 1.7);
		
		mdos[0] = mdo1;
		mdos[1] = mdo2;
		mdos[2] = mdo3;
		
		try {
			storage.insert(mdos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		assertEquals(EDO1_COUNT + 2, countBindingsInDB(mdo1));
		assertEquals(EDO2_COUNT + 1, countBindingsInDB(mdo3));
	}

	@Test
	public void testInsertElisDataObjectArrayLastObjectDoesntMatchTable() {
		buildAndPopulateMDO1Table();
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` TO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2`;");
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		ElisDataObject[] mdos = new ElisDataObject[3];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject1 mdo2 = new MockDataObject1();
		MockDataObject2 mdo3 = new MockDataObject2();
		
		mdo1.setUniqueUserId(1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setUUID(uuid);
		mdo2.setUniqueUserId(1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setUniqueUserId(2);
		mdo3.setBaz((float) 1.7);
		
		mdos[0] = mdo1;
		mdos[1] = mdo2;
		mdos[2] = mdo3;
		
		try {
			storage.insert(mdos);
		} catch (StorageException e) {
		}

		assertEquals(EDO1_COUNT + 2, countBindingsInDB(mdo1));
		assertEquals(EDO1_COUNT, countBindingsInDB(mdo3));
	}

	@Test
	public void testInsertElisDataObjectArrayRandomObjectDoesntMatchTable() {
		buildAndPopulateMDO1Table();
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` TO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2`;");
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		ElisDataObject[] mdos = new ElisDataObject[3];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject1 mdo2 = new MockDataObject1();
		MockDataObject2 mdo3 = new MockDataObject2();
		
		mdo1.setUniqueUserId(1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setUUID(uuid);
		mdo2.setUniqueUserId(1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setUniqueUserId(2);
		mdo3.setBaz((float) 1.7);
		
		mdos[0] = mdo1;
		mdos[1] = mdo3;
		mdos[2] = mdo2;
		
		try {
			storage.insert(mdos);
		} catch (StorageException e) {
		}

		assertEquals(EDO1_COUNT + 1, countBindingsInDB(mdo1));
		assertEquals(EDO1_COUNT, countBindingsInDB(mdo3));
	}

	@Test
	public void testInsertAbstractUserPlatformUser() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Kalle", "kvack");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT + 1, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserAlreadyHasId() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Kalle", "kvack");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pid.setId(42);
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT + 1, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserAlreadyHasExistingId() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Kalle", "kvack");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pid.setId(42);
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
			storage.insert(pu); // Second one should fail
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT + 1, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserIsEmpty() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser();
		
		try {
			storage.insert(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasNonEmptyPassword() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Kalle", "kvack");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT + 1, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasEmptyPassword() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Kalle", "");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasNoUserName() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("", "kvack");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasNoFirstName() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Kalle", "kvack");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasNoLastName() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Kalle", "kvack");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pu.setFirstName("Kalle");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasNoEmailAddress() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Kalle", "kvack");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		
		try {
			storage.insert(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUser() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1(null, "Horses", 2);
		
		try {
			storage.insert(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT + 1, countBindingsInDB(mu));
		assertEquals(AU1_COUNT + 1, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserHasNoIdFirstUserInTable() {
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1(null, "Horses", 2);
		
		try {
			storage.insert(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(1, countBindingsInDB(mu));
		assertEquals(1, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserAlreadyHasId() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1(null, "Horses", 2);
		mu.setUserId(UUID.fromString("00001111-2222-3333-4444-deadbeef7777"));
		
		try {
			storage.insert(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT + 1, countBindingsInDB(mu));
		assertEquals(AU1_COUNT + 1, countBindingsInDB());
	}
	
	@Test
	public void testInsertUserAbstractUserAlreadyExistingId() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1(null, "Horses", 2);
		mu.setUserId(UUID.fromString("00001111-2222-dead-beef-555566667771"));
		
		try {
			storage.insert(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(AU1_COUNT, countBindingsInDB(mu));
		assertEquals(AU1_COUNT, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserDoesntMatchTable() {
		buildAndPopulateMU2Table();
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockUser2` TO `se-mah-elis-impl-service-storage-test-mock-MockUser1`;");
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1(null, "Horses", 2);
		
		try {
			storage.insert(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(AU2_COUNT, countBindingsInDB(mu));
		assertEquals(AU2_COUNT, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserEmptyIdentifier() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1(null, "Horses", 2);
		mu.setIdentifier(new EmptyMockUserIdentifier(mu.getClass()));
		
		try {
			storage.insert(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(AU1_COUNT, countBindingsInDB(mu));
		assertEquals(AU1_COUNT, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserHasNoServiceName() {
		Storage storage = new StorageImpl();
		MockUser3 mu = new MockUser3(null, "Foo");
		
		try {
			storage.insert(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testInsertAbstractUserArray() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[3];
		MockUser1 mu1 = new MockUser1(null, "Horses", 2);
		MockUser1 mu2 = new MockUser1(null, "Lice", 4);
		MockUser1 mu3 = new MockUser1(null, "Scorpions", 8);
		
		users[0] = mu1;
		users[1] = mu2;
		users[2] = mu3;
		
		try {
			storage.insert(users);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT + 3, countBindingsInDB(mu1));
		assertEquals(AU1_COUNT + 3, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserArrayOneUserIsFirstInItsTable() {
		Storage storage = new StorageImpl(connection);
		User[] users = new User[3];
		MockUser1 mu1 = new MockUser1(null, "Horses", 2);
		MockUser1 mu2 = new MockUser1(null, "Lice", 4);
		MockUser1 mu3 = new MockUser1(null, "Scorpions", 8);
		
		users[0] = mu1;
		users[1] = mu2;
		users[2] = mu3;
		
		try {
			storage.insert(users);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(3, countBindingsInDB(mu1));
		assertEquals(3, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserArrayTwoUsersAreFirstInTheirTables() {
		Storage storage = new StorageImpl(connection);
		User[] users = new User[3];
		MockUser1 mu1 = new MockUser1(null, "Horses", 2);
		MockUser2 mu2 = new MockUser2(null, "Lice");
		MockUser1 mu3 = new MockUser1(null, "Scorpions", 8);
		
		users[0] = mu1;
		users[1] = mu2;
		users[2] = mu3;
		
		try {
			storage.insert(users);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(2, countBindingsInDB(mu1));
		assertEquals(1, countBindingsInDB(mu2));
		assertEquals(3, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserArrayOneUserDoesntMatchTable() {
		buildAndPopulateMU2Table();
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockUser2` TO `se-mah-elis-impl-service-storage-test-mock-MockUser1`;");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockUser1` SET `uuid` = UNHEX('1000deadbeef33334444555566667771') where `uuid` = x'0000deadbeef33334444555566667771';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockUser1` SET `uuid` = UNHEX('1000deadbeef33334444555566667772') where `uuid` = x'0000deadbeef33334444555566667772';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockUser1` SET `uuid` = UNHEX('1000deadbeef33334444555566667773') where `uuid` = x'0000deadbeef33334444555566667773';");
		runQuery("UPDATE `object_lookup_table` SET `stored_in` = 'se-mah-elis-impl-service-storage-test-mock-MockUser1';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000deadbeef33334444555566667771' where `id` = x'0000deadbeef33334444555566667771';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000deadbeef33334444555566667772' where `id` = x'0000deadbeef33334444555566667772';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000deadbeef33334444555566667773' where `id` = x'0000deadbeef33334444555566667773';");
		buildAndPopulateMU2Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[3];
		MockUser2 mu1 = new MockUser2(null, "Lice");
		MockUser1 mu2 = new MockUser1(null, "Horses", 2);
		MockUser2 mu3 = new MockUser2(null, "Scorpions");
		
		users[0] = mu1;
		users[1] = mu2;
		users[2] = mu3;
		
		try {
			storage.insert(users);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(AU2_COUNT + 1, countBindingsInDB(mu1));
		assertEquals(AU1_COUNT, countBindingsInDB(mu2));
		assertEquals(2 * AU1_COUNT + 1, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserArrayOneUserIsAPlatformUser() {
		buildAndPopulateMU1Table();
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		AbstractUser[] users = new AbstractUser[4];
		MockUser1 mu1 = new MockUser1(null, "Horses", 2);
		MockUser1 mu2 = new MockUser1(null, "Lice", 4);
		MockUser1 mu3 = new MockUser1(null, "Scorpions", 8);
		MockPlatformUser pu = new MockPlatformUser();
		
		pu.setIdentifier(new MockPlatformUserIdentifier("user", "pass"));
		pu.setFirstName("first name");
		pu.setLastName("last name");
		pu.setEmail("name@email.com");
		
		users[0] = mu1;
		users[1] = mu2;
		users[2] = mu3;
		users[3] = pu;
		
		try {
			storage.insert(users);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT + 3, countBindingsInDB(mu1));
		assertEquals(AU1_COUNT + 3, countBindingsInDB());
		assertEquals(PU_COUNT + 1, countBindingsInDB(pu));
	}

	@Test
	public void testInsertAbstractUserArrayEmptyArray() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[3];
		
		try {
			storage.insert(users);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT, countBindingsInDB(new MockUser1()));
		assertEquals(AU1_COUNT, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserArrayOneUserAlreadyHasId() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[3];
		MockUser1 mu1 = new MockUser1(null, "Horses", 2);
		MockUser1 mu2 = new MockUser1(null, "Lice", 4);
		MockUser1 mu3 = new MockUser1(null, "Scorpions", 8);
		
		mu2.setUserId(UUID.fromString("deadbeef-2222-3333-4444-555566667777"));
		
		users[0] = mu1;
		users[1] = mu2;
		users[2] = mu3;
		
		try {
			storage.insert(users);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT + 3, countBindingsInDB(mu1));
		assertEquals(AU1_COUNT + 3, countBindingsInDB());
	}

	@Test
	public void testReadDataUUID() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject edo = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667779");
		
		try {
			edo = storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(edo);
		assertTrue(edo instanceof MockDataObject1);
		assertEquals(uuid, ((MockDataObject1) edo).getUUID());
		assertEquals("Jezibaba", ((MockDataObject1) edo).getBar());
		assertEquals(17, ((MockDataObject1) edo).getFoo());
		assertEquals(1, ((MockDataObject1) edo).getUniqueUserId());
	}

	@Test
	public void testReadDataUUIDDataNotFound() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject edo = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-123466667779");
		
		try {
			edo = storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNull(edo);
	}

	@Test
	public void testReadDataUUIDIsUser() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject edo = null;
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		
		try {
			edo = storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNull(edo);
	}

	@Test
	public void testReadUserUUID() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		AbstractUser au = null;
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		try {
			au = storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(au);
		assertTrue(au instanceof MockUser1);
		assertEquals(uuid, ((MockUser1) au).getUserId());
		assertEquals(22, ((MockUser1) au).getWhatever());
		assertEquals("Vinea", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserUUIDUserNotFound() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		AbstractUser au = null;
		UUID uuid = UUID.fromString("0000dead-beef-3333-4444-555566667772");
		
		try {
			au = storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNull(au);
	}

	@Test
	public void testReadUserUUIDIsElisDataObject() {
		buildAndPopulateMDO1Table();
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		AbstractUser au = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667778");
		
		try {
			au = storage.readUser(uuid);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertNull(au);
	}

	@Test
	public void testReadUserUserIdentifier() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUserIdentifier uid = new MockUserIdentifier();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		AbstractUser au = null;
		
		uid.identifies(MockUser1.class);
		
		try {
			au = storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(au);
		assertTrue(au instanceof MockUser1);
		assertEquals(uuid, ((MockUser1) au).getUserId());
		assertEquals(42, ((MockUser1) au).getWhatever());
		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserUserIdentifierMalformedIdentifierNoIdentifyingData() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUserIdentifier uid = new MockUserIdentifier(0, "", "");
		AbstractUser au = null;
		
		uid.identifies(MockUser1.class);
		
		try {
			au = storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNull(au);
	}

	@Test
	public void testReadUserUserIdentifierMalformedIdentifierNoServiceReference() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUserIdentifier uid = new MockUserIdentifier();
		AbstractUser au = null;
		
		uid.identifies(MockUser1.class);
		
		try {
			au = storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNull(au);
	}

	@Test
	public void testReadUserUserIdentifierUserDoesntExist() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUserIdentifier uid = new MockUserIdentifier(2, "George", "Carlin");
		AbstractUser au = null;
		
		uid.identifies(MockUser1.class);
		
		try {
			au = storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNull(au);
	}

	@Test
	public void testReadUserUserIdentifierUserSeveralHits() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUserIdentifier uid = new MockUserIdentifier();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667750");
		AbstractUser au = null;
		
		runQuery("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser1` " +
				"VALUES (UNHEX('" + uuid +"'), 1, 'Spongebob Squarepants', 'Patrick Seastar', 1, 'Water', 1);");
		runQuery("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid +"'), " +
				"'se-mah-elis-impl-service-storage-test-mock-MockUser1');");
		
		uid.identifies(MockUser1.class);
		
		try {
			au = storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(au);
		assertTrue(au instanceof MockUser1);
		assertEquals(uuid, ((MockUser1) au).getUserId());
		assertEquals(42, ((MockUser1) au).getWhatever());
		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserAbstractUser() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUserIdentifier uid = new MockUserIdentifier();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		uid.identifies(MockUser1.class);
		mu.setIdentifier(uid);
		mu.setUserId(uuid);
		
		try {
			au = storage.readUser(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mu);
		assertTrue(mu instanceof MockUser1);
		assertEquals(uuid, ((MockUser1) au).getUserId());
		assertEquals(42, ((MockUser1) au).getWhatever());
		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserAbstractUserNoId() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUserIdentifier uid = new MockUserIdentifier();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		AbstractUser au = new MockUser1();
		
		uid.identifies(MockUser1.class);
		au.setIdentifier(uid);
		
		try {
			au = storage.readUser(au);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(au);
		assertTrue(au instanceof MockUser1);
		assertEquals(uuid, ((MockUser1) au).getUserId());
		assertEquals(42, ((MockUser1) au).getWhatever());
		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserAbstractUserNoIdentifier() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUserIdentifier uid = new MockUserIdentifier();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		uid.identifies(MockUser1.class);
		mu.setUserId(uuid);
		
		try {
			au = storage.readUser(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(au);
		assertTrue(au instanceof MockUser1);
		assertEquals(uuid, ((MockUser1) au).getUserId());
		assertEquals(42, ((MockUser1) au).getWhatever());
		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserAbstractUserNeitherIdNorIdentifier() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		try {
			au = storage.readUser(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNull(au);
	}

	@Test
	public void testReadUserAbstractUserNeitherIdNorValidIdentifier() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUserIdentifier uid = new MockUserIdentifier(0, "", "");
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		uid.identifies(MockUser1.class);
		mu.setIdentifier(uid);
		
		try {
			au = storage.readUser(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(au);
		assertTrue(au instanceof MockUser1);
		assertEquals(uuid, ((MockUser1) au).getUserId());
		assertEquals(42, ((MockUser1) au).getWhatever());
		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserAbstractUserDoesntExist() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUserIdentifier uid = new MockUserIdentifier();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566660000");
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		uid.identifies(MockUser1.class);
		mu.setIdentifier(uid);
		mu.setUserId(uuid);
		
		try {
			au = storage.readUser(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(au);
		assertTrue(au instanceof MockUser1);
		assertEquals(uuid, ((MockUser1) au).getUserId());
		assertEquals(42, ((MockUser1) au).getWhatever());
		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testDeleteElisDataObject() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667779");
		MockDataObject1 mdo = new MockDataObject1();
		mdo.setUUID(uuid);
		
		try {
			storage.delete(mdo);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT - 1, countBindingsInDB(mdo));
		assertEquals(EDO1_COUNT - 1, countBindingsInDB());
	}

	@Test
	public void testDeleteElisDataObjectDoesntExist() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-0000-555566667779");
		MockDataObject1 mdo = new MockDataObject1();
		mdo.setUUID(uuid);
		
		try {
			storage.delete(mdo);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT, countBindingsInDB(mdo));
		assertEquals(EDO1_COUNT, countBindingsInDB());
	}

	@Test
	public void testDeleteElisDataObjectUUIDDoesntMatchObjectType() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-0000-555566667779");
		MockDataObject1 mdo = new MockDataObject1();
		mdo.setUUID(uuid);
		
		try {
			storage.delete(mdo);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT, countBindingsInDB(mdo));
		assertEquals(EDO1_COUNT, countBindingsInDB());
	}

	@Test
	public void testDeleteElisDataObjectArray() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject1 mdo2 = new MockDataObject1();

		mdo1.setUUID(UUID.fromString("00001111-2222-3333-4444-555566667779"));
		mdo2.setUUID(UUID.fromString("00001111-2222-3333-4444-55556666777B"));
		
		edos[0] = mdo1;
		edos[1] = mdo2;
		
		try {
			storage.delete(edos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT - 2, countBindingsInDB(mdo1));
		assertEquals(EDO1_COUNT - 2, countBindingsInDB());
	}

	@Test
	public void testDeleteElisDataObjectArrayDifferentObjectTypes() {
		buildAndPopulateMDO1Table();
		buildAndPopulateMDO2Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject2 mdo2 = new MockDataObject2();

		mdo1.setUUID(UUID.fromString("00001111-2222-3333-4444-555566667779"));
		mdo2.setUUID(UUID.fromString("00001111-2222-3333-4444-555566667772"));
		
		edos[0] = mdo1;
		edos[1] = mdo2;
		
		try {
			storage.delete(edos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT - 1, countBindingsInDB(mdo1));
		assertEquals(EDO2_COUNT - 1, countBindingsInDB(mdo2));
		assertEquals(EDO1_COUNT + EDO2_COUNT - 2, countBindingsInDB());
	}

	@Test
	public void testDeleteElisDataObjectArrayOneObjectDoesntExist() {
		buildAndPopulateMDO1Table();
		buildAndPopulateMDO2Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject2 mdo2 = new MockDataObject2();

		mdo1.setUUID(UUID.fromString("00001111-2222-3333-4444-555566667779"));
		mdo2.setUUID(UUID.fromString("00001111-2222-3333-4444-555566660000"));
		
		edos[0] = mdo1;
		edos[1] = mdo2;
		
		try {
			storage.delete(edos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT - 1, countBindingsInDB(mdo1));
		assertEquals(EDO2_COUNT, countBindingsInDB(mdo2));
		assertEquals(EDO1_COUNT + EDO2_COUNT - 1, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserPlatformUser() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Superman", "Louis Lane");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pid.setId(2);
		
		try {
			storage.delete(pu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT - 1, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserPlatformUserDoesntExist() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Superman", "Louis Lane");
		PlatformUser pu = new MockPlatformUser(pid);
		
		pid.setId(12);
		
		try {
			storage.delete(pu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUser() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1();
		
		mu.setUserId(UUID.fromString("00001111-2222-dead-beef-555566667772"));
		
		try {
			storage.delete(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT - 1, countBindingsInDB(mu));
		assertEquals(AU1_COUNT - 1, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserUserDoesntExist() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1();
		
		mu.setUserId(UUID.fromString("00001111-2222-3333-4444-5555deadbeef"));
		
		try {
			storage.delete(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT, countBindingsInDB(mu));
		assertEquals(AU1_COUNT, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserArrayPlatformUsers() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser[] pus = new PlatformUser[2];
		PlatformUserIdentifier pid1 = new MockPlatformUserIdentifier("Superman", "Louis Lane");
		PlatformUserIdentifier pid2 = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu1 = new MockPlatformUser(pid1);
		PlatformUser pu2 = new MockPlatformUser(pid2);
		
		pid1.setId(2);
		pid2.setId(1);
		
		pus[0] = pu1;
		pus[1] = pu2;
		
		try {
			storage.delete(pus);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT - 2, countBindingsInDB(pu1));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserArrayPlatformUsersOneUserDoesntExist() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser[] pus = new PlatformUser[2];
		PlatformUserIdentifier pid1 = new MockPlatformUserIdentifier("Superman", "Louis Lane");
		PlatformUserIdentifier pid2 = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu1 = new MockPlatformUser(pid1);
		PlatformUser pu2 = new MockPlatformUser(pid2);
		
		pid1.setId(2);
		pid2.setId(5);
		
		pus[0] = pu1;
		pus[1] = pu2;
		
		try {
			storage.delete(pus);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT - 1, countBindingsInDB(pu1));
		assertEquals(0, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserArray() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] mus = new User[2];
		MockUser1 mu1 = new MockUser1();
		MockUser1 mu2 = new MockUser1();

		mu1.setUserId(UUID.fromString("00001111-2222-dead-beef-555566667772"));
		mu2.setUserId(UUID.fromString("00001111-2222-dead-beef-555566667773"));
		
		mus[0] = mu1;
		mus[1] = mu2;
		
		try {
			storage.delete(mus);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT - 2, countBindingsInDB(mu1));
		assertEquals(AU1_COUNT - 2, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserArrayFirstUserDoesntExist() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] mus = new User[2];
		MockUser1 mu1 = new MockUser1();
		MockUser1 mu2 = new MockUser1();

		mu1.setUserId(UUID.fromString("00001111-2222-3333-4444-5555deadbeef"));
		mu2.setUserId(UUID.fromString("00001111-2222-dead-beef-555566667773"));

		mus[0] = mu1;
		mus[1] = mu2;
		
		try {
			storage.delete(mus);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT - 1, countBindingsInDB(mu1));
		assertEquals(AU1_COUNT - 1, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserArrayOneUserDoesntExist() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] mus = new User[2];
		MockUser1 mu1 = new MockUser1();
		MockUser1 mu2 = new MockUser1();

		mu1.setUserId(UUID.fromString("00001111-2222-dead-beef-555566667773"));
		mu2.setUserId(UUID.fromString("00001111-2222-3333-4444-5555deadbeef"));

		mus[0] = mu1;
		mus[1] = mu2;
		
		try {
			storage.delete(mus);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT - 1, countBindingsInDB(mu1));
		assertEquals(AU1_COUNT - 1, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserArrayDifferentUserTypes() {
		buildAndPopulateMU1Table();
		buildAndPopulateMU2Table();
		
		Storage storage = new StorageImpl(connection);
		User[] mus = new User[2];
		MockUser1 mu1 = new MockUser1();
		MockUser2 mu2 = new MockUser2();

		mu1.setUserId(UUID.fromString("00001111-2222-dead-beef-555566667772"));
		mu2.setUserId(UUID.fromString("0000dead-beef-3333-4444-555566667772"));
		
		mus[0] = mu1;
		mus[1] = mu2;
		
		try {
			storage.delete(mus);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT - 1, countBindingsInDB(mu1));
		assertEquals(AU2_COUNT - 1, countBindingsInDB(mu2));
		assertEquals(AU1_COUNT + AU2_COUNT - 2, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserArrayEmptyArray() {
		buildAndPopulateMU1Table();
		buildAndPopulateMU2Table();
		
		Storage storage = new StorageImpl(connection);
		User[] mus = new User[2];
		
		try {
			storage.delete(mus);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(AU1_COUNT, countBindingsInDB(new MockUser1()));
		assertEquals(AU2_COUNT, countBindingsInDB(new MockUser2()));
		assertEquals(AU1_COUNT + AU2_COUNT, countBindingsInDB());
	}

	@Test
	public void testUpdateElisDataObject() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1 mdo = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667779");
		
		try {
			mdo = (MockDataObject1) storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		mdo.setFoo(5);
		mdo.setBar("Veles");
		
		try {
			storage.update(mdo);
			mdo = null;
			mdo = (MockDataObject1) storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mdo);
		assertEquals(uuid, mdo.getUUID());
		assertEquals("Veles", mdo.getBar());
		assertEquals(5, mdo.getFoo());
		assertEquals(1, mdo.getUniqueUserId());
	}

	@Test
	public void testUpdateElisDataObjectNoId() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1 mdo = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667779");
		
		try {
			mdo = (MockDataObject1) storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		mdo.setUUID(null);
		mdo.setFoo(5);
		mdo.setBar("Veles");
		
		try {
			storage.update(mdo);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		try {
			mdo = (MockDataObject1) storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mdo);
		assertEquals(uuid, mdo.getUUID());
		assertEquals("Jezibaba", mdo.getBar());
		assertEquals(17, mdo.getFoo());
		assertEquals(1, mdo.getUniqueUserId());
	}

	@Test
	public void testUpdateElisDataObjectNonExistingId() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1 mdo = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667779");
		
		try {
			mdo = (MockDataObject1) storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		mdo.setUUID(UUID.fromString("0000dead-beef-3333-4444-555566667777"));
		mdo.setFoo(5);
		mdo.setBar("Veles");
		
		try {
			storage.update(mdo);
			fail("This shouldn't happen");
		} catch (StorageException e) {
			e.printStackTrace();
		}
		
		try {
			mdo = (MockDataObject1) storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mdo);
		assertEquals(uuid, mdo.getUUID());
		assertEquals("Jezibaba", mdo.getBar());
		assertEquals(17, mdo.getFoo());
		assertEquals(1, mdo.getUniqueUserId());
	}

	@Test
	public void testUpdateElisDataObjectArray() {
		buildAndPopulateMDO1Table();
	
		Storage storage = new StorageImpl(connection);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = null;
		MockDataObject1 mdo2 = null;
		UUID uuid1 = UUID.fromString("00001111-2222-3333-4444-555566667779");
		UUID uuid2 = UUID.fromString("00001111-2222-3333-4444-55556666777A");
	
		try {
			mdo1 = (MockDataObject1) storage.readData(uuid1);
			mdo2 = (MockDataObject1) storage.readData(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
	
		mdo1.setFoo(5);
		mdo1.setBar("Veles");
		mdo2.setFoo(7);
		mdo2.setBar("Perun");
	
		edos[0] = mdo1;
		edos[1] = mdo2;
	
		try {
			storage.update(edos);
			mdo1 = null;
			mdo2 = null;
			mdo1 = (MockDataObject1) storage.readData(uuid1);
			mdo2 = (MockDataObject1) storage.readData(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
	
		assertNotNull(mdo1);
		assertEquals(uuid1, mdo1.getUUID());
		assertEquals("Veles", mdo1.getBar());
		assertEquals(5, mdo1.getFoo());
		assertEquals(1, mdo1.getUniqueUserId());
	
		assertNotNull(mdo2);
		assertEquals(uuid2, mdo2.getUUID());
		assertEquals("Perun", mdo2.getBar());
		assertEquals(7, mdo2.getFoo());
		assertEquals(2, mdo2.getUniqueUserId());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testUpdateElisDataObjectArrayDifferentObjectTypes() {
		buildAndPopulateMDO1Table();
		buildAndPopulateMDO2Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = null;
		MockDataObject2 mdo2 = null;
		UUID uuid1 = UUID.fromString("00001111-2222-3333-4444-555566667779");
		UUID uuid2 = UUID.fromString("00001111-2222-3333-4444-555566667772");
		
		try {
			mdo1 = (MockDataObject1) storage.readData(uuid1);
			mdo2 = (MockDataObject2) storage.readData(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		mdo1.setFoo(5);
		mdo1.setBar("Veles");
		mdo2.setBaz((float) 7.1);
		
		edos[0] = mdo1;
		edos[1] = mdo2;
		
		try {
			storage.update(edos);
			mdo1 = null;
			mdo2 = null;
			mdo1 = (MockDataObject1) storage.readData(uuid1);
			mdo2 = (MockDataObject2) storage.readData(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mdo1);
		assertEquals(uuid1, mdo1.getUUID());
		assertEquals("Veles", mdo1.getBar());
		assertEquals(5, mdo1.getFoo());
		assertEquals(1, mdo1.getUniqueUserId());
		
		assertNotNull(mdo2);
		assertEquals(uuid2, mdo2.getUUID());
		assertEquals(7.1, mdo2.getBaz());
		assertEquals(1, mdo2.getUniqueUserId());
	}

	@Test
	public void testUpdateElisDataObjectArrayOneOfTheObjectsLacksId() {
		buildAndPopulateMDO1Table();
	
		Storage storage = new StorageImpl(connection);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = null;
		MockDataObject1 mdo2 = null;
		UUID uuid1 = UUID.fromString("00001111-2222-3333-4444-555566667779");
		UUID uuid2 = UUID.fromString("00001111-2222-3333-4444-55556666777A");
	
		try {
			mdo1 = (MockDataObject1) storage.readData(uuid1);
			mdo2 = (MockDataObject1) storage.readData(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
	
		mdo1.setFoo(5);
		mdo1.setBar("Veles");
		mdo2.setFoo(7);
		mdo2.setBar("Perun");
		mdo2.setUUID(null);
	
		edos[0] = mdo1;
		edos[1] = mdo2;
	
		try {
			storage.update(edos);
			fail("This shouldn't happen");
		} catch (StorageException e) {
			e.printStackTrace();
		}
		
		mdo1 = null;
		mdo2 = null;
		try {
			mdo1 = (MockDataObject1) storage.readData(uuid1);
			mdo2 = (MockDataObject1) storage.readData(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
	
		assertNotNull(mdo1);
		assertEquals(uuid1, mdo1.getUUID());
		assertEquals("Veles", mdo1.getBar());
		assertEquals(5, mdo1.getFoo());
		assertEquals(1, mdo1.getUniqueUserId());
	
		assertNotNull(mdo2);
		assertEquals(uuid2, mdo2.getUUID());
		assertEquals("Domovoj", mdo2.getBar());
		assertEquals(17, mdo2.getFoo());
		assertEquals(2, mdo2.getUniqueUserId());
	}

	@Test
	public void testUpdateElisDataObjectArrayEmptyArray() {
		buildAndPopulateMDO1Table();
	
		Storage storage = new StorageImpl(connection);
		ElisDataObject[] edos = new ElisDataObject[2];
	
		try {
			storage.update(edos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
	}

	@Test
	public void testUpdateAbstractUserPlatformUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoId() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoLongerMatchesTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserPlatformUserEmptyPlatformUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNewPassword() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoUserName() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoPassword() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoFirstName() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoFamilyName() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoEmail() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUser() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = null;
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		try {
			mu = (MockUser1) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		mu.setStuff("Veles");
		mu.setWhatever(5);
		
		try {
			storage.update(mu);
			mu = null;
			mu = (MockUser1) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mu);
		assertEquals(uuid, ((MockUser1) mu).getUserId());
		assertEquals(5, ((MockUser1) mu).getWhatever());
		assertEquals("Veles", ((MockUser1) mu).getStuff());
	}

	@Test
	public void testUpdateAbstractUserNoId() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667779");
		
		try {
			mu = (MockUser1) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		mu.setStuff("Veles");
		mu.setWhatever(5);
		mu.setUserId(null);
		
		try {
			storage.update(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserEmpty() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1(null, "", 0);
		
		try {
			storage.update(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserArray() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[2];
		MockUser1 mu1 = null;
		MockUser1 mu2 = null;
		UUID uuid1 = UUID.fromString("00001111-2222-3333-4444-555566667779");
		UUID uuid2 = UUID.fromString("00001111-2222-3333-4444-55556666777A");
		
		try {
			mu1 = (MockUser1) storage.readUser(uuid1);
			mu2 = (MockUser1) storage.readUser(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		mu1.setStuff("Veles");
		mu1.setWhatever(5);
		mu2.setStuff("Perun");
		mu2.setWhatever(7);
		
		users[0] = mu1;
		users[1] = mu2;
		
		try {
			storage.update(users);
			mu1 = null;
			mu2 = null;
			mu1 = (MockUser1) storage.readUser(uuid1);
			mu2 = (MockUser1) storage.readUser(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		assertNotNull(mu1);
		assertEquals(uuid1, mu1.getUserId());
		assertEquals(5, mu1.getWhatever());
		assertEquals("Veles", mu1.getStuff());
		
		assertNotNull(mu2);
		assertEquals(uuid2, mu2.getUserId());
		assertEquals(7, mu2.getWhatever());
		assertEquals("Perun", mu2.getStuff());
	}

	@Test
	public void testUpdateAbstractUserArrayDifferentUserTypes() {
		buildAndPopulateMU1Table();
		buildAndPopulateMU2Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[2];
		MockUser1 mu1 = null;
		MockUser2 mu2 = null;
		UUID uuid1 = UUID.fromString("00001111-2222-3333-4444-555566667779");
		UUID uuid2 = UUID.fromString("00001111-2222-3333-4444-555566667772");
		
		try {
			mu1 = (MockUser1) storage.readUser(uuid1);
			mu2 = (MockUser2) storage.readUser(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		mu1.setStuff("Veles");
		mu1.setWhatever(5);
		mu2.setStuff("Perun");
		
		users[0] = mu1;
		users[1] = mu2;
		
		try {
			storage.update(users);
			mu1 = null;
			mu2 = null;
			mu1 = (MockUser1) storage.readUser(uuid1);
			mu2 = (MockUser2) storage.readUser(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		assertNotNull(mu1);
		assertEquals(uuid1, mu1.getUserId());
		assertEquals(5, mu1.getWhatever());
		assertEquals("Veles", mu1.getStuff());
		
		assertNotNull(mu2);
		assertEquals(uuid2, mu2.getUserId());
		assertEquals("Perun", mu2.getStuff());
	}

	@Test
	public void testUpdateAbstractUserArrayOneUserHasNoId() {
		setUpDatabase();
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[2];
		MockUser1 mu1 = null;
		MockUser1 mu2 = null;
		UUID uuid1 = UUID.fromString("00001111-2222-3333-4444-555566667779");
		UUID uuid2 = UUID.fromString("00001111-2222-3333-4444-55556666777A");
		
		try {
			mu1 = (MockUser1) storage.readUser(uuid1);
			mu2 = (MockUser1) storage.readUser(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		mu1.setStuff("Veles");
		mu1.setWhatever(5);
		mu2.setStuff("Perun");
		mu2.setWhatever(7);
		mu2.setUserId(null);
		
		users[0] = mu1;
		users[1] = mu2;
		
		try {
			storage.update(users);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserArrayEmptyArray() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[2];
		
		try {
			storage.update(users);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserArrayOneUserIsEmpty() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[2];
		MockUser1 mu1 = null;
		MockUser1 mu2 = new MockUser1(null, "", 0);
		UUID uuid1 = UUID.fromString("00001111-2222-3333-4444-555566667779");
		
		try {
			mu1 = (MockUser1) storage.readUser(uuid1);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		mu1.setStuff("Veles");
		mu1.setWhatever(5);
		mu2.setStuff("Perun");
		
		users[0] = mu1;
		users[1] = mu2;
		
		try {
			storage.update(users);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testSelectOneHit() {
		buildAndPopulateMDO1Table();

		Storage storage = new StorageImpl(connection);
		se.mah.elis.services.storage.result.ResultSet rs = null;
		MockDataObject1 mdo = null;
		Query query = new Query();
		
		query.setDataType(MockDataObject1.class)
			.setPredicate((new SimplePredicate(CriterionType.EQ))
					.setField("bar")
					.setCriterion("Jezibaba"));
		
		try {
			rs = storage.select(query);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(rs);
		assertEquals(1, rs.size());
		assertEquals(MockDataObject1.class, rs.getObjectType());
		
		mdo = (MockDataObject1) rs.first();
		
		assertEquals(UUID.fromString("00001111-2222-3333-4444-555566667779"), mdo.getUUID());
		assertEquals("Jezibaba", mdo.getBar());
		assertEquals(17, mdo.getFoo());
		assertEquals(1, mdo.getUniqueUserId());
	}

	@Test
	public void testSelectOneHitAbstractUser() {
		buildAndPopulateMU1Table();

		Storage storage = new StorageImpl(connection);
		se.mah.elis.services.storage.result.ResultSet rs = null;
		MockUser1 mu = null;
		Query query = new Query();
		
		query.setDataType(MockUser1.class)
			.setPredicate((new SimplePredicate(CriterionType.EQ))
					.setField("stuff")
					.setCriterion("Vinea"));
		
		try {
			rs = storage.select(query);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(rs);
		assertEquals(1, rs.size());
		assertEquals(MockUser1.class, rs.getObjectType());
		
		mu = (MockUser1) rs.first();
		
		assertEquals(UUID.fromString("00001111-2222-dead-beef-555566667772"), mu.getUserId());
		assertEquals("Vinea", mu.getStuff());
		assertEquals(22, mu.getWhatever());
	}

	@Test
	public void testSelectSeveralHits() {
		buildAndPopulateMDO1Table();

		Storage storage = new StorageImpl(connection);
		se.mah.elis.services.storage.result.ResultSet rs = null;
		MockDataObject1 mdo = null;
		Query query = new Query();
		
		query.setDataType(MockDataObject1.class)
			.setPredicate((new ChainingPredicate(ChainingType.OR))
				.setLeft((new SimplePredicate(CriterionType.EQ))
					.setField("bar")
					.setCriterion("Jezibaba"))
				.setRight((new SimplePredicate(CriterionType.EQ))
					.setField("foo")
					.setCriterion(42)));
		
		try {
			rs = storage.select(query);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(rs);
		assertEquals(2, rs.size());
		assertEquals(MockDataObject1.class, rs.getObjectType());
		
		mdo = (MockDataObject1) rs.first();
		
		assertEquals(UUID.fromString("00001111-2222-3333-4444-555566667779"), mdo.getUUID());
		assertEquals("Jezibaba", mdo.getBar());
		assertEquals(17, mdo.getFoo());
		assertEquals(1, mdo.getUniqueUserId());
		
		mdo = (MockDataObject1) rs.next();
		
		assertEquals(UUID.fromString("00001111-2222-3333-4444-555566667777"), mdo.getUUID());
		assertEquals("Baba Roga", mdo.getBar());
		assertEquals(17, mdo.getFoo());
		assertEquals(1, mdo.getUniqueUserId());
	}

	@Test
	public void testSelectNoHits() {
		buildAndPopulateMDO1Table();

		Storage storage = new StorageImpl(connection);
		se.mah.elis.services.storage.result.ResultSet rs = null;
		Query query = new Query();
		
		query.setDataType(MockDataObject1.class)
			.setPredicate((new SimplePredicate(CriterionType.EQ))
					.setField("bar")
					.setCriterion("Bohovia sú mrtve"));
		
		try {
			rs = storage.select(query);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(rs);
		assertEquals(0, rs.size());
		assertEquals(MockDataObject1.class, rs.getObjectType());
	}

	@Test
	public void testDeleteQuery() {
		buildAndPopulateMDO1Table();

		Storage storage = new StorageImpl(connection);
		se.mah.elis.services.storage.result.ResultSet rs = null;
		Query query = new Query();
		
		query.setDataType(MockDataObject1.class)
			.setPredicate((new SimplePredicate(CriterionType.EQ))
					.setField("bar")
					.setCriterion("Jezibaba"));
		
		try {
			storage.delete(query);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT - 1, countBindingsInDB(new MockDataObject1()));
		assertEquals(EDO1_COUNT - 1, countBindingsInDB());
	}

	@Test
	public void testDeleteQueryNoMatch() {
		buildAndPopulateMDO1Table();

		Storage storage = new StorageImpl(connection);
		se.mah.elis.services.storage.result.ResultSet rs = null;
		Query query = new Query();
		
		query.setDataType(MockDataObject1.class)
			.setPredicate((new SimplePredicate(CriterionType.EQ))
					.setField("bar")
					.setCriterion("Bohovie sú mrtve"));
		
		try {
			storage.delete(query);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT - 1, countBindingsInDB(new MockDataObject1()));
		assertEquals(EDO1_COUNT - 1, countBindingsInDB());
	}

	@Test
	public void testDeleteQuerySeveralMatches() {
		buildAndPopulateMDO1Table();

		Storage storage = new StorageImpl(connection);
		se.mah.elis.services.storage.result.ResultSet rs = null;
		Query query = new Query();
		
		query.setDataType(MockDataObject1.class)
		.setPredicate((new ChainingPredicate(ChainingType.OR))
			.setLeft((new SimplePredicate(CriterionType.EQ))
				.setField("bar")
				.setCriterion("Jezibaba"))
			.setRight((new SimplePredicate(CriterionType.EQ))
				.setField("foo")
				.setCriterion(42)));
		
		try {
			storage.delete(query);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(EDO1_COUNT - 2, countBindingsInDB(new MockDataObject1()));
		assertEquals(EDO1_COUNT - 2, countBindingsInDB());
	}

}
