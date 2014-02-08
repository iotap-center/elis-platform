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
import se.mah.elis.impl.service.storage.test.mock.MockUserIdentifier;
import se.mah.elis.impl.services.storage.StorageImpl;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.AbstractUser;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;

public class StorageImplTest {

	private static int EDO1_COUNT = 6;
	private static int EDO2_COUNT = 3;
	private static int AU1_COUNT = 3;
	private static int AU2_COUNT = 3;
	private static int PU_COUNT = 3;
	
	private Connection connection;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		tearDownTables();
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
			stmt.execute("CREATE TABLE `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` (" +
						"`uuid` VARBINARY(16) PRIMARY KEY, " +
						"`userid` INTEGER, " +
						"`foo` INTEGER, " +
						"`bar` VARCHAR( 16 ))");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` " +
					"VALUES (UNHEX('" + uuid1 +"'), 1, 42, 'Baba Roga')");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` " +
					"VALUES (UNHEX('" + uuid2 +"'), 1, 13, 'Baba Jaga')");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` " +
					"VALUES (UNHEX('" + uuid3 +"'), 1, 17, 'Jezibaba')");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` " +
					"VALUES (UNHEX('" + uuid4 +"'), 2, 17, 'Domovoj')");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` " +
					"VALUES (UNHEX('" + uuid5 +"'), 2, 5, 'Domovik')");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` " +
					"VALUES (UNHEX('" + uuid6 +"'), 3, 5, 'Perun')");
			
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid1 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid2 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid3 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid4 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid5 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid6 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockDataObject1')");
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
			stmt.execute("CREATE TABLE `se_mah_elis_impl_service_storage_test_mock_MockDataObject2` (" +
						"`uuid` VARBINARY(16) PRIMARY KEY, " +
						"`userid` INTEGER, " +
						"`baz` FLOAT)");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` " +
					"VALUES (UNHEX('" + uuid1 +"'), 1, 1.1");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` " +
					"VALUES (UNHEX('" + uuid2 +"'), 1, 0.5)");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` " +
					"VALUES (UNHEX('" + uuid3 +"'), 2, 7.1)");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid1 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockDataObject2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid2 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockDataObject2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid3 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockDataObject2')");
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
			stmt.execute("CREATE TABLE `se_mah_elis_impl_service_storage_test_mock_MockUser1` (" +
						"`uuid` VARBINARY(16) PRIMARY KEY, " +
						"`id_number` INTEGER, " +
						"`username` VARCHAR(32), " +
						"`password` VARCHAR(32), " +
						"`userid` INTEGER, " +
						"`stuff` VARCHAR(32), " +
						"`whatever` INTEGER)");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockUser1` " +
					"VALUES (UNHEX('" + uuid1 +"'), 1, 'Batman', 'Robin', 42. 'Rajec', 21");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockUser1` " +
					"VALUES (UNHEX('" + uuid2 +"'), 1, 'Superman', 'Lois Lane', 13. 'Vinea', 22");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockUser1` " +
					"VALUES (UNHEX('" + uuid3 +"'), 1, 'Spongebob Squarepants', 'Patrick Seastar', 17. 'Kofola', 23");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid1 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockUser1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid2 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockUser1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid3 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockUser1')");
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
			stmt.execute("CREATE TABLE `se_mah_elis_impl_service_storage_test_mock_MockUser2` (" +
						"`uuid` VARBINARY(16) PRIMARY KEY, " +
						"`id_number` INTEGER, " +
						"`username` VARCHAR(32), " +
						"`password` VARCHAR(32), " +
						"`userid` INTEGER, " +
						"`whatever` INTEGER)");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockUser2` " +
					"VALUES (UNHEX('" + uuid1 +"'), 1, 'Batman', 'Robin', 42. 21");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockUser2` " +
					"VALUES (UNHEX('" + uuid2 +"'), 1, 'Superman', 'Lois Lane', 13. 22");
			stmt.execute("INSERT INTO `se_mah_elis_impl_service_storage_test_mock_MockUser2` " +
					"VALUES (UNHEX('" + uuid3 +"'), 1, 'Spongebob Squarepants', 'Patrick Seastar', 17. 23");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid1 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockUser2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid2 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockUser2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (UNHEX('" + uuid3 +"'), " +
					"'se_mah_elis_impl_service_storage_test_mock_MockUser2')");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void populatePUTable() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("INSERT INTO `se_mah_elis_services_user_PlatformUser` " +
					"VALUES (1, 'Batman', 'Robin', 'Bruce', 'Wayne', 'bruce@waynecorp.com');");
			stmt.execute("INSERT INTO `se_mah_elis_services_user_PlatformUser` " +
					"VALUES (2, 'Superman', 'Lois Lane', 'Clark', 'Kent', 'clark.kent@dailyplanet.com');");
			stmt.execute("INSERT INTO `se_mah_elis_services_user_PlatformUser` " +
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
			stmt.execute("TRUNCATE TABLE se_mah_elis_services_user_PlatformUser;");
			stmt.execute("DROP TABLE IF EXISTS `se_mah_elis_impl_service_storage_test_mock_MockDataObject1`;");
			stmt.execute("DROP TABLE IF EXISTS `se_mah_elis_impl_service_storage_test_mock_MockDataObject2`;");
			stmt.execute("DROP TABLE IF EXISTS `se_mah_elis_impl_service_storage_test_mock_MockUser1`;");
			stmt.execute("DROP TABLE IF EXISTS `se_mah_elis_impl_service_storage_test_mock_MockUser2`;");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int countBindingsInDB(ElisDataObject edo) {
		Statement statement;
		int bindings = -1;
		String query = "";
		
		if (edo instanceof MockDataObject1) {
			query = "SELECT count(*) FROM `se_mah_elis_impl_service_storage_test_mock_MockDataObject1`";
		} else if (edo instanceof MockDataObject2) {
			query = "SELECT count(*) FROM `se_mah_elis_impl_service_storage_test_mock_MockDataObject2`";
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
		String table = user.getIdentifier().identifies().getName().replace('.', '_');
		
		if (user instanceof PlatformUser) {
			query = "SELECT count(*) FROM `se_mah_elis_services_user_PlatformUser`";
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
			statement.executeQuery(query);
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
		setUpDatabase();
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
		setUpDatabase();
		
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
		setUpDatabase();
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("ffffeeeeddddccccbbbbaaaa999988887777");
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
		setUpDatabase();
		buildAndPopulateMDO2Table(); // NB! MockDataObject2
		
		// Rename MDO2 table to MDO1
		runQuery("RENAME TABLE `se_mah_elis_impl_service_storage_test_mock_MockDataObject2` TO `se_mah_elis_impl_service_storage_test_mock_MockDataObject1`;");
		
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
		setUpDatabase();
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1 mdo = new MockDataObject1();
		
		try {
			storage.insert(mdo);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(EDO1_COUNT, countBindingsInDB(mdo));
	}
	
	@Test
	public void testInsertElisDataObjectAlreadyExists() {
		setUpDatabase();
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111222233334444555566667777");
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
		setUpDatabase();
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
		setUpDatabase();
		
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
		setUpDatabase();
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("000011112222333344445555deadbeef");
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
		setUpDatabase();
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
		setUpDatabase();
		buildAndPopulateMDO1Table();
		buildAndPopulateMDO2Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("000011112222333344445555deadbeef");
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
		setUpDatabase();
		buildAndPopulateMDO1Table();
		runQuery("RENAME TABLE `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` TO `se_mah_elis_impl_service_storage_test_mock_MockDataObject2`;");
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("000011112222333344445555deadbeef");
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
		assertEquals(EDO2_COUNT, countBindingsInDB(mdo3));
	}

	@Test
	public void testInsertElisDataObjectArrayRandomObjectDoesntMatchTable() {
		setUpDatabase();
		buildAndPopulateMDO1Table();
		runQuery("RENAME TABLE `se_mah_elis_impl_service_storage_test_mock_MockDataObject1` TO `se_mah_elis_impl_service_storage_test_mock_MockDataObject2`;");
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("000011112222333344445555deadbeef");
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
		assertEquals(EDO2_COUNT, countBindingsInDB(mdo3));
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
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
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
		MockUser1 mu = new MockUser1("Horses", 2);
		
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
	public void testInsertAbstractUserHasNoIdFirstUserInTable() {		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1("Horses", 2);
		
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
		MockUser1 mu = new MockUser1("Horses", 2);
		mu.setUserId(UUID.fromString("00001111222233334444deadbeef7777"));
		
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
		MockUser1 mu = new MockUser1("Horses", 2);
		mu.setUserId(UUID.fromString("000011112222deadbeef555566667771"));
		
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
		runQuery("RENAME TABLE `se_mah_elis_impl_service_storage_test_mock_MockUser2` TO `se_mah_elis_impl_service_storage_test_mock_MockUser1`;");
		
		Storage storage = new StorageImpl(connection);
		MockUser1 mu = new MockUser1("Horses", 2);
		
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
		MockUser1 mu = new MockUser1("Horses", 2);
		mu.setIdentifier(new EmptyMockUserIdentifier());
		
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
		fail("Not yet implemented");
	}

	@Test
	public void testInsertAbstractUserArray() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[3];
		MockUser1 mu1 = new MockUser1("Horses", 2);
		MockUser1 mu2 = new MockUser1("Lice", 4);
		MockUser1 mu3 = new MockUser1("Scorpions", 8);
		
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
		MockUser1 mu1 = new MockUser1("Horses", 2);
		MockUser1 mu2 = new MockUser1("Lice", 4);
		MockUser1 mu3 = new MockUser1("Scorpions", 8);
		
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
		MockUser1 mu1 = new MockUser1("Horses", 2);
		MockUser2 mu2 = new MockUser2("Lice");
		MockUser1 mu3 = new MockUser1("Scorpions", 8);
		
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
		runQuery("RENAME TABLE `se_mah_elis_impl_service_storage_test_mock_MockUser2` TO `se_mah_elis_impl_service_storage_test_mock_MockUser1`;");
		buildAndPopulateMU2Table();
		
		Storage storage = new StorageImpl(connection);
		User[] users = new User[3];
		MockUser2 mu1 = new MockUser2("Horses");
		MockUser2 mu2 = new MockUser2("Lice");
		MockUser1 mu3 = new MockUser1("Scorpions", 8);
		
		users[0] = mu1;
		users[1] = mu2;
		users[2] = mu3;
		
		try {
			storage.insert(users);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(AU2_COUNT + 2, countBindingsInDB(mu1));
		assertEquals(AU1_COUNT, countBindingsInDB(mu3));
		assertEquals(2, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserArrayOneUserIsAPlatformUser() {
		buildAndPopulateMU1Table();
		
		Storage storage = new StorageImpl(connection);
		AbstractUser[] users = new AbstractUser[4];
		MockUser1 mu1 = new MockUser1("Horses", 2);
		MockUser1 mu2 = new MockUser1("Lice", 4);
		MockUser1 mu3 = new MockUser1("Scorpions", 8);
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
		assertEquals(PU_COUNT, countBindingsInDB(pu));
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
		MockUser1 mu1 = new MockUser1("Horses", 2);
		MockUser1 mu2 = new MockUser1("Lice", 4);
		MockUser1 mu3 = new MockUser1("Scorpions", 8);
		
		mu2.setUserId(UUID.fromString("deadbeef222233334444555566667777"
				+ ""));
		
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
		setUpDatabase();
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject edo = null;
		UUID uuid = UUID.fromString("00001111222233334444555566667779");
		
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
		setUpDatabase();
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject edo = null;
		UUID uuid = UUID.fromString("00001111222233334444123466667779");
		
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
		setUpDatabase();
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject edo = null;
		UUID uuid = UUID.fromString("000011112222deadbeef555566667771");
		
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
		setUpDatabase();
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		AbstractUser au = null;
		UUID uuid = UUID.fromString("0000deadbeef33334444555566667772");
		
		try {
			au = storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(au);
		assertTrue(au instanceof MockUser2);
		assertEquals(uuid, ((MockUser2) au).getUserId());
		assertEquals(13, ((MockUser2) au).getStuff());
	}

	@Test
	public void testReadUserUUIDUserNotFound() {
		setUpDatabase();
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		AbstractUser au = null;
		UUID uuid = UUID.fromString("0000deadbeef33334444555566667772");
		
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
		setUpDatabase();
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		AbstractUser au = null;
		UUID uuid = UUID.fromString("0000deadbeef33334444555566667772");
		
		try {
			au = storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(au);
		assertTrue(au instanceof MockUser2);
		assertEquals(uuid, ((MockUser2) au).getUserId());
		assertEquals(13, ((MockUser2) au).getStuff());
	}

	@Test
	public void testReadUserUserIdentifier() {
//		setUpDatabase();
//		buildAndPopulateMDO1Table();
//		
//		Storage storage = new StorageImpl(connection);
//		MockUserIdentifier uid = new MockUserIdentifier();
//		UUID uuid = uuid.fromString("000011112222deadbeef555566667771");
//		AbstractUser au = null;
//		
//		uid.
//		
//		try {
//			au = storage.readUser(uid);
//		} catch (StorageException e) {
//			e.printStackTrace();
//			fail("This shouldn't happen");
//		}
//		
//		assertNotNull(au);
//		assertTrue(au instanceof MockUser1);
//		assertEquals(uuid, ((MockUser1) au).getUserId());
//		assertEquals(42, ((MockUser1) au).getWhatever());
//		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserUserIdentifierMalformedIdentifierNoIdentifyingData() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadUserUserIdentifierMalformedIdentifierNoServiceReference() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadUserUserIdentifierUserDoesntExist() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadUserUserIdentifierUserSeveralHits() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadUserAbstractUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadUserAbstractUserNoId() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadUserAbstractUserNoIdentifier() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadUserAbstractUserNeitherIdNorIdentifier() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadUserAbstractUserNeitherIdNorValidIdentifier() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadUserAbstractUserDoesntExist() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadUserAbstractUserSeveralHits() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteElisDataObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteElisDataObjectDoesntExist() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteElisDataObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteElisDataObjectArrayDifferentObjectTypes() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteElisDataObjectArrayOneObjectDoesntExist() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAbstractUserPlatformUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAbstractUserPlatformUserDoesntExist() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAbstractUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAbstractUserUserDoesntExist() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAbstractUserArrayPlatformUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAbstractUserArrayPlatformUsersOneUserDoesntExist() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAbstractUserArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAbstractUserArrayOneUserDoesntExist() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAbstractUserArrayDifferentUserTypes() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteAbstractUserArrayEmptyArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteQuery() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteQueryNoMatch() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteQuerySeveralMatches() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateElisDataObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateElisDataObjectNoId() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateElisDataObjectNonExistingId() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateElisDataObjectNoLongerMatchesTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateElisDataObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateElisDataObjectArrayDifferentObjectTypes() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateElisDataObjectArrayOneOfTheObjectsLacksId() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateElisDataObjectArrayEmptyArray() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserNoId() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserNoLongerMatchesTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserEmpty() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserArrayDifferentUserTypes() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserArrayOneUserHasNoId() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserArrayOneUserNoLongerMatchesTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserArrayEmptyArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAbstractUserArrayOneUserIsEmpty() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectOneHit() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectSeveralHits() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectNoHits() {
		fail("Not yet implemented");
	}

}
