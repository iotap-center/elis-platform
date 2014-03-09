package se.mah.elis.impl.service.storage.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
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
import se.mah.elis.impl.service.storage.test.mock.MockUser1Provider;
import se.mah.elis.impl.service.storage.test.mock.MockUser2;
import se.mah.elis.impl.service.storage.test.mock.MockUser2Provider;
import se.mah.elis.impl.service.storage.test.mock.MockUser3;
import se.mah.elis.impl.service.storage.test.mock.MockUserIdentifier;
import se.mah.elis.impl.services.storage.StorageImpl;
import se.mah.elis.impl.services.storage.StorageUtils;
import se.mah.elis.impl.services.users.factory.UserFactoryImpl;
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
import se.mah.elis.services.users.factory.UserFactory;

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
		String dataId1 = "00001111222233334444555566667777";
		String dataId2 = "00001111222233334444555566667778";
		String dataId3 = "00001111222233334444555566667779";
		String dataId4 = "0000111122223333444455556666777A";
		String dataId5 = "0000111122223333444455556666777B";
		String dataId6 = "0000111122223333444455556666777C";
		String ownerId1 = "000011112222deadbeef555566667771";
		String ownerId2 = "000011112222deadbeef555566667772";
		String ownerId3 = "000011112222deadbeef555566667773";
		
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` (" +
						"`dataid` BINARY(16) PRIMARY KEY, " +
						"`ownerid` BINARY(16), " +
						"`foo` INTEGER, " +
						"`bar` VARCHAR( 16 ), " +
						"`created` TIMESTAMP);");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + dataId1 +"', x'" + ownerId1 + "', 42, 'Baba Roga', '2000-01-01 00:00:00')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + dataId2 +"', x'" + ownerId1 + "', 13, 'Baba Jaga', '2000-01-01 00:00:01')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + dataId3 +"', x'" + ownerId1 + "', 17, 'Jezibaba', '2000-01-01 00:00:02')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + dataId4 +"', x'" + ownerId2 + "', 17, 'Domovoj', '2000-01-01 00:00:03')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + dataId5 +"', x'" + ownerId2 + "', 5, 'Domovik', '2000-01-01 00:00:04')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + dataId6 +"', x'" + ownerId3 + "', 5, 'Perun', '2000-01-01 00:00:05')");
			
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + dataId1 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + dataId2 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + dataId3 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + dataId4 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + dataId5 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + dataId6 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void buildAndPopulateMDO2Table() {
		String dataId1 = "00001111222233334444555566667771";
		String dataId2 = "00001111222233334444555566667772";
		String dataId3 = "00001111222233334444555566667773";
		String ownerId1 = "000011112222deadbeef555566667771";
		String ownerId2 = "000011112222deadbeef555566667772";
		
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` (" +
						"`dataid` BINARY(16) PRIMARY KEY, " +
						"`ownerid` BINARY(16), " +
						"`baz` FLOAT, " +
						"`created` TIMESTAMP)");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` " +
					"VALUES (x'" + dataId1 +"', x'" + ownerId1 + "', 1.1, '2000-01-01 00:00:00');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` " +
					"VALUES (x'" + dataId2 +"', x'" + ownerId1 + "', 0.5, '2000-01-01 00:00:01');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` " +
					"VALUES (x'" + dataId3 +"', x'" + ownerId2 + "', 7.1, '2000-01-01 00:00:02');");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + dataId1 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + dataId2 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + dataId3 +"', " +
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
						"`uuid` BINARY(16) PRIMARY KEY, " +
						"`service_name` VARCHAR(9), " +
						"`id_number` INTEGER, " +
						"`username` VARCHAR(32), " +
						"`password` VARCHAR(32), " +
						"`stuff` VARCHAR(32), " +
						"`whatever` INTEGER, " +
						"`created` TIMESTAMP)");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser1` " +
					"VALUES (x'" + uuid1 +"', 'test', 1, 'Batman', 'Robin', 'Rajec', 21, '2000-01-01 00:00:00');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser1` " +
					"VALUES (x'" + uuid2 +"', 'test', 1, 'Superman', 'Lois Lane', 'Vinea', 22, '2000-01-01 00:00:01');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser1` " +
					"VALUES (x'" + uuid3 +"', 'test', 1, 'Spongebob Squarepants', 'Patrick Seastar', 'Kofola', 23, '2000-01-01 00:00:02');");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid1 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid2 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid3 +"', " +
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
						"`uuid` BINARY(16) PRIMARY KEY, " +
						"`service_name` VARCHAR(9), " +
						"`id_number` INTEGER, " +
						"`username` VARCHAR(32), " +
						"`password` VARCHAR(32), " +
						"`stuff` VARCHAR(32), " +
						"`created` TIMESTAMP)");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser2` " +
					"VALUES (x'" + uuid1 +"', 'test', 1, 'Batman', 'Robin', 'Kvass', '2000-01-01 00:00:00');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser2` " +
					"VALUES (x'" + uuid2 +"', 'test', 1, 'Superman', 'Lois Lane', 'Kompot', '2000-01-01 00:00:01');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser2` " +
					"VALUES (x'" + uuid3 +"', 'test', 1, 'Spongebob Squarepants', 'Patrick Seastar', 'Slivovice', '2000-01-01 00:00:02');");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid1 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid2 +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid3 +"', " +
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
					"VALUES (1, 'Batman', PASSWORD('Robin'), 'Bruce', 'Wayne', 'bruce@waynecorp.com', '2000-01-01 00:00:00');");
			stmt.execute("INSERT INTO `se-mah-elis-services-users-PlatformUser` " +
					"VALUES (2, 'Superman', PASSWORD('Lois Lane'), 'Clark', 'Kent', 'clark.kent@dailyplanet.com', '2000-01-01 00:00:01');");
			stmt.execute("INSERT INTO `se-mah-elis-services-users-PlatformUser` " +
					"VALUES (3, 'Spongebob Squarepants', PASSWORD('Patrick Seastar'), 'Spongebob', 'Squarepants', 'spongebob@krustykrab.com', '2000-01-01 00:00:02');");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void makeMDO1MDO2() {
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` TO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2`;");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'10001111222233334444555566667777' where `dataid` = x'00001111222233334444555566667777';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'10001111222233334444555566667778' where `dataid` = x'00001111222233334444555566667778';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'10001111222233334444555566667779' where `dataid` = x'00001111222233334444555566667779';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'1000111122223333444455556666777A' where `dataid` = x'0000111122223333444455556666777A';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'1000111122223333444455556666777B' where `dataid` = x'0000111122223333444455556666777B';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'1000111122223333444455556666777C' where `dataid` = x'0000111122223333444455556666777C';");
		runQuery("UPDATE `object_lookup_table` SET `stored_in` = 'se-mah-elis-impl-service-storage-test-mock-MockDataObject2';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'10001111222233334444555566667777' where `id` = x'00001111222233334444555566667777';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'10001111222233334444555566667778' where `id` = x'00001111222233334444555566667778';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'10001111222233334444555566667779' where `id` = x'00001111222233334444555566667779';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000111122223333444455556666777A' where `id` = x'0000111122223333444455556666777A';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000111122223333444455556666777B' where `id` = x'0000111122223333444455556666777B';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000111122223333444455556666777C' where `id` = x'0000111122223333444455556666777C';");
		buildAndPopulateMDO1Table();
	}

	private void makeMDO2MDO1() {
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` TO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1`;");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` SET `dataid` = x'10001111222233334444555566667771' where `dataid` = x'00001111222233334444555566667771';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` SET `dataid` = x'10001111222233334444555566667772' where `dataid` = x'00001111222233334444555566667772';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` SET `dataid` = x'10001111222233334444555566667773' where `dataid` = x'00001111222233334444555566667773';");
		runQuery("UPDATE `object_lookup_table` SET `stored_in` = 'se-mah-elis-impl-service-storage-test-mock-MockDataObject1';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'10001111222233334444555566667771' where `id` = x'00001111222233334444555566667771';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'10001111222233334444555566667772' where `id` = x'00001111222233334444555566667772';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'10001111222233334444555566667773' where `id` = x'00001111222233334444555566667773';");
		buildAndPopulateMDO2Table();
	}

	private void makeMU1MU2() {
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockUser1` TO `se-mah-elis-impl-service-storage-test-mock-MockUser2`;");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockUser2` SET `uuid` = x'100011112222deadbeef555566667771' where `uuid` = x'000011112222deadbeef555566667771';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockUser2` SET `uuid` = x'100011112222deadbeef555566667772' where `uuid` = x'000011112222deadbeef555566667772';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockUser2` SET `uuid` = x'100011112222deadbeef555566667773' where `uuid` = x'000011112222deadbeef555566667773';");
		runQuery("UPDATE `object_lookup_table` SET `stored_in` = 'se-mah-elis-impl-service-storage-test-mock-MockUser2';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'100011112222deadbeef555566667771' where `id` = x'000011112222deadbeef555566667771';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'100011112222deadbeef555566667772' where `id` = x'000011112222deadbeef555566667772';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'100011112222deadbeef555566667773' where `id` = x'000011112222deadbeef555566667773';");
		buildAndPopulateMU1Table();
	}

	private void makeMU2MU1() {
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockUser2` TO `se-mah-elis-impl-service-storage-test-mock-MockUser1`;");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockUser1` SET `uuid` = x'1000deadbeef33334444555566667771' where `uuid` = x'0000deadbeef33334444555566667771';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockUser1` SET `uuid` = x'1000deadbeef33334444555566667772' where `uuid` = x'0000deadbeef33334444555566667772';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockUser1` SET `uuid` = x'1000deadbeef33334444555566667773' where `uuid` = x'0000deadbeef33334444555566667773';");
		runQuery("UPDATE `object_lookup_table` SET `stored_in` = 'se-mah-elis-impl-service-storage-test-mock-MockUser1';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000deadbeef33334444555566667771' where `id` = x'0000deadbeef33334444555566667771';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000deadbeef33334444555566667772' where `id` = x'0000deadbeef33334444555566667772';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000deadbeef33334444555566667773' where `id` = x'0000deadbeef33334444555566667773';");
		buildAndPopulateMU2Table();
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
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockUser3`;");
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
	
	private void printContents(AbstractUser user) {
		Statement statement;
		int bindings = -1;
		String query = "";
		String table = user.getIdentifier().identifies().getName().replace('.', '-');
		
		query = "SELECT * FROM `" + table + "`";
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				System.out.println(rs.getInt(3) + " " + rs.getString(4) + ", " + rs.getString(6) + " " + rs.getInt(7));
			}
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
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		
		mdo.setOwnerId(uuid);
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
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		
		mdo.setOwnerId(uuid);
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
		UUID dataId = UUID.fromString("ffffeeee-dddd-cccc-bbbb-aaaa99998888");
		UUID ownerId = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		MockDataObject1 mdo = new MockDataObject1();
		
		mdo.setDataId(dataId);
		mdo.setOwnerId(ownerId);
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
		UUID ownerId = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		
		mdo.setOwnerId(ownerId);
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
		
		UUID dataId = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID ownerId = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		MockDataObject1 mdo = new MockDataObject1();
		
		mdo.setDataId(dataId);
		mdo.setOwnerId(ownerId);
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
		UUID ownerId1 = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		UUID ownerId2 = UUID.fromString("deadbeef-2222-3333-4444-5555deadbeef");
		
		mdo1.setOwnerId(ownerId1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setOwnerId(ownerId1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setOwnerId(ownerId2);
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
		UUID ownerId1 = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		UUID ownerId2 = UUID.fromString("deadbeef-2222-3333-4444-5555deadbeef");
		
		mdo1.setOwnerId(ownerId1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setOwnerId(ownerId1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setOwnerId(ownerId2);
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
		UUID ownerId1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID ownerId2 = UUID.fromString("deadbeef-2222-3333-4444-5555deadbeef");
		
		mdo1.setOwnerId(ownerId1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setDataId(uuid);
		mdo2.setOwnerId(ownerId1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setOwnerId(ownerId2);
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
		UUID ownerId1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID ownerId2 = UUID.fromString("deadbeef-2222-3333-4444-5555deadbeef");
		
		mdo1.setOwnerId(ownerId1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setDataId(uuid);
		mdo2.setOwnerId(ownerId1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setOwnerId(ownerId2);
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
		makeMDO1MDO2();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		ElisDataObject[] mdos = new ElisDataObject[3];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject1 mdo2 = new MockDataObject1();
		MockDataObject2 mdo3 = new MockDataObject2();
		UUID ownerId1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID ownerId2 = UUID.fromString("deadbeef-2222-3333-4444-5555deadbeef");
		
		mdo1.setOwnerId(ownerId1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setDataId(uuid);
		mdo2.setOwnerId(ownerId1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setOwnerId(ownerId2);
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
		makeMDO1MDO2();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		ElisDataObject[] mdos = new ElisDataObject[3];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject1 mdo2 = new MockDataObject1();
		MockDataObject2 mdo3 = new MockDataObject2();
		UUID ownerId1 = UUID.fromString("00001111-2222-3333-4444-555566667777");
		UUID ownerId2 = UUID.fromString("deadbeef-2222-3333-4444-5555deadbeef");
		
		mdo1.setOwnerId(ownerId1);
		mdo1.setBar("Testing");
		mdo1.setFoo(42);
		
		mdo2.setDataId(uuid);
		mdo2.setOwnerId(ownerId1);
		mdo2.setBar("Testing");
		mdo2.setFoo(13);
		
		mdo3.setOwnerId(ownerId2);
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
		assertEquals(4, ((PlatformUserIdentifier) pu.getIdentifier()).getId());
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
		assertEquals(42, ((PlatformUserIdentifier) pu.getIdentifier()).getId());
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
		assertEquals(42, ((PlatformUserIdentifier) pu.getIdentifier()).getId());
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
		assertEquals(4, ((PlatformUserIdentifier) pu.getIdentifier()).getId());
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
	public void testInsertAbstractUserPlatformExistingUserName() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUserIdentifier pid = new MockPlatformUserIdentifier("Batman", "kvack");
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
		
		assertNotNull(mu.getUserId());
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

		assertNotNull(mu.getUserId());
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

		assertEquals(UUID.fromString("00001111-2222-3333-4444-deadbeef7777"), mu.getUserId());
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
		
		assertEquals(UUID.fromString("00001111-2222-dead-beef-555566667771"), mu.getUserId());
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

		assertNotNull(mu1.getUserId());
		assertNotNull(mu2.getUserId());
		assertNotNull(mu3.getUserId());
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

		assertNotNull(mu1.getUserId());
		assertNotNull(mu2.getUserId());
		assertNotNull(mu3.getUserId());
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

		assertNotNull(mu1.getUserId());
		assertNotNull(mu2.getUserId());
		assertNotNull(mu3.getUserId());
		assertEquals(2, countBindingsInDB(mu1));
		assertEquals(1, countBindingsInDB(mu2));
		assertEquals(3, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserArrayOneUserDoesntMatchTable() {
		buildAndPopulateMU2Table();
		makeMU2MU1();
		
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

		assertNotNull(mu1.getUserId());
		assertNotNull(mu2.getUserId());
		assertNotNull(mu3.getUserId());
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

		assertNotNull(mu1.getUserId());
		assertNotNull(mu2.getUserId());
		assertNotNull(mu3.getUserId());
		assertEquals(AU1_COUNT + 3, countBindingsInDB(mu1));
		assertEquals(AU1_COUNT + 3, countBindingsInDB());
	}

	@Test
	public void testReadDataUUID() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject edo = null;
		UUID dataid = UUID.fromString("00001111-2222-3333-4444-555566667779");
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		
		try {
			edo = storage.readData(dataid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(edo);
		assertTrue(edo instanceof MockDataObject1);
		assertEquals(dataid, ((MockDataObject1) edo).getDataId());
		assertEquals("Jezibaba", ((MockDataObject1) edo).getBar());
		assertEquals(17, ((MockDataObject1) edo).getFoo());
		assertEquals(ownerid, ((MockDataObject1) edo).getOwnerId());
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
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
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
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		AbstractUser au = null;
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
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
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		AbstractUser au = null;
		UUID uuid = UUID.fromString("0000dead-beef-3333-4444-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
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
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		AbstractUser au = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667778");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			au = storage.readUser(uuid);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertNull(au);
	}

	@Test
	public void testReadUserUserIdentifier() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUserIdentifier uid = new MockUserIdentifier();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
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
		assertEquals(21, ((MockUser1) au).getWhatever());
		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserUserIdentifierMalformedIdentifierNoIdentifyingData() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUserIdentifier uid = new MockUserIdentifier(0, "", "");
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.identifies(MockUser1.class);
		
		try {
			au = storage.readUser(uid);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertNull(au);
	}

	@Test
	public void testReadUserUserIdentifierUserDoesntExist() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUserIdentifier uid = new MockUserIdentifier(2, "George", "Carlin");
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.identifies(MockUser1.class);
		
		try {
			au = storage.readUser(uid);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertNull(au);
	}

	@Test
	public void testReadUserUserIdentifierUserSeveralHits() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUserIdentifier uid = new MockUserIdentifier(1, "Batman", "Robin");
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667750");
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		runQuery("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser1` " +
				"VALUES (x'" + StorageUtils.stripDashesFromUUID(uuid) +
				"', 'test', 1, 'Batman', 'Robin', " +
				"'Water', 1, '2014-02-22 16:18:00');");
		runQuery("INSERT INTO `object_lookup_table` VALUES (x'" +
				StorageUtils.stripDashesFromUUID(uuid) +
				"', 'se-mah-elis-impl-service-storage-test-mock-MockUser1');");
		
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
		assertEquals(1, ((MockUser1) au).getWhatever());
		assertEquals("Water", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserUserIdentifierPlatformUser() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockPlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			au = storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(au);
		assertTrue(au instanceof PlatformUser);
		assertEquals(1, ((PlatformUserIdentifier) au.getIdentifier()).getId());
		assertEquals("Batman", ((PlatformUserIdentifier) au.getIdentifier()).getUsername());
		assertFalse(((PlatformUserIdentifier) au.getIdentifier()).getPassword().isEmpty());
		assertEquals("Bruce", ((PlatformUser) au).getFirstName());
		assertEquals("Wayne", ((PlatformUser) au).getLastName());
		assertEquals("bruce@waynecorp.com", ((PlatformUser) au).getEmail());
	}

	@Test
	public void testReadUserAbstractUser() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUserIdentifier uid = new MockUserIdentifier();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
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
		assertEquals(21, ((MockUser1) au).getWhatever());
		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserAbstractUserNoId() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUserIdentifier uid = new MockUserIdentifier();
		AbstractUser au = new MockUser1();
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.identifies(MockUser1.class);
		au.setIdentifier(uid);
		
		try {
			au = storage.readUser(au);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testReadUserAbstractUserNoIdentifier() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUserIdentifier uid = new MockUserIdentifier();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
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
		assertEquals(21, ((MockUser1) au).getWhatever());
		assertEquals("Rajec", ((MockUser1) au).getStuff());
	}

	@Test
	public void testReadUserAbstractUserNeitherIdNorIdentifier() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			au = storage.readUser(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testReadUserAbstractUserNeitherIdNorValidIdentifier() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUserIdentifier uid = new MockUserIdentifier(0, "", "");
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.identifies(MockUser1.class);
		mu.setIdentifier(uid);
		
		try {
			au = storage.readUser(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testReadUserAbstractUserDoesntExist() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUserIdentifier uid = new MockUserIdentifier();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566660000");
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.identifies(MockUser1.class);
		mu.setIdentifier(uid);
		mu.setUserId(uuid);
		
		try {
			au = storage.readUser(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}
	
	@Test
	public void testReadUsers() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		AbstractUser[] users = null;
		UUID expectedUUID = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("stuff", "Vinea");
		
		users = storage.readUsers(MockUser1.class, props);
		
		assertNotNull(users);
		assertEquals(1, users.length);
		assertTrue(users[0] instanceof MockUser1);
		assertEquals(expectedUUID, ((MockUser1) users[0]).getUserId());
		assertEquals(22, ((MockUser1) users[0]).getWhatever());
		assertEquals("Vinea", ((MockUser1) users[0]).getStuff());
	}
	
	@Test
	public void testReadUsersNoMatches() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		AbstractUser[] users = null;
		UUID expectedUUID = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("stuff", "Julmust");
		
		users = storage.readUsers(MockUser1.class, props);
		
		assertNotNull(users);
		assertEquals(0, users.length);
	}
	
	@Test
	public void testReadUsersNoSuchClass() {
		buildAndPopulateMU1Table();
	
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		AbstractUser[] users = null;
		UUID expectedUUID = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("stuff", "Vinea");
		
		users = storage.readUsers(MockUser2.class, props);
		
		assertNotNull(users);
		assertEquals(0, users.length);
	}
	
	@Test
	public void testReadUsersNoProperties() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		AbstractUser[] users = null;
		UUID expectedUUID = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
		users = storage.readUsers(MockUser1.class, new Properties());
		
		assertNotNull(users);
		assertEquals(3, users.length);
		assertTrue(users[0] instanceof MockUser1);
	}
	
	@Test
	public void testReadUsersPasswordShouldBeNeglected() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		AbstractUser[] users = null;
		UUID expectedUUID = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("password", "Robin");
		
		users = storage.readUsers(MockUser1.class, props);
		
		assertNotNull(users);
		assertEquals(3, users.length);
	}
	
	@Test
	public void testReadUsersMoreThanOneCriterion() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		AbstractUser[] users = null;
		UUID expectedUUID = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("stuff", "Vinea");
		props.put("whatever", 22);
		
		users = storage.readUsers(MockUser1.class, props);
		
		assertNotNull(users);
		assertEquals(1, users.length);
		assertTrue(users[0] instanceof MockUser1);
		assertEquals(expectedUUID, ((MockUser1) users[0]).getUserId());
		assertEquals(22, ((MockUser1) users[0]).getWhatever());
		assertEquals("Vinea", ((MockUser1) users[0]).getStuff());
	}
	
	@Test
	public void testReadUsersCriterionIsNotString() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		AbstractUser[] users = null;
		UUID expectedUUID = UUID.fromString("00001111-2222-dead-beef-555566667771");
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("whatever", "21");
		
		users = storage.readUsers(MockUser1.class, props);
		
		assertNotNull(users);
		assertEquals(1, users.length);
		assertTrue(users[0] instanceof MockUser1);
		assertEquals(expectedUUID, ((MockUser1) users[0]).getUserId());
		assertEquals(21, ((MockUser1) users[0]).getWhatever());
		assertEquals("Rajec", ((MockUser1) users[0]).getStuff());
	}
	
	@Test
	public void testReadUsersCriterionIsString() throws InterruptedException {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		AbstractUser[] users = null;
		MockUser1 mu1 = new MockUser1(null, "Voda", 1);
		MockUser1 mu2 = new MockUser1(null, "Káva", 2);
		
		mu1.setIdentifier(new MockUserIdentifier(13, "man", "Secret"));
		mu2.setIdentifier(new MockUserIdentifier(17, "mandibles", "Arthropod"));
		
		// Set timestamp to a second into the future to preserve order.
		mu2.setCreated(new DateTime(DateTime.now().getMillis() + 1000));
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			storage.insert(mu1);
			storage.insert(mu2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		props.put("username", "man");
		
		users = storage.readUsers(MockUser1.class, props);
		
		assertNotNull(users);
		assertEquals(4, users.length);
		assertTrue(users[0] instanceof MockUser1);
		
		// Batman
		props = users[0].getIdentifier().getProperties();
		assertEquals("Batman", props.get("username"));
		assertEquals(21, ((MockUser1) users[0]).getWhatever());
		assertEquals("Rajec", ((MockUser1) users[0]).getStuff());
		
		// Superman
		props = users[1].getIdentifier().getProperties();
		assertEquals("Superman", props.get("username"));
		assertEquals(22, ((MockUser1) users[1]).getWhatever());
		assertEquals("Vinea", ((MockUser1) users[1]).getStuff());
		
		// man
		props = users[2].getIdentifier().getProperties();
		assertEquals("man", props.get("username"));
		assertEquals(1, ((MockUser1) users[2]).getWhatever());
		assertEquals("Voda", ((MockUser1) users[2]).getStuff());
		
		// mandibles
		props = users[3].getIdentifier().getProperties();
		assertEquals("mandibles", props.get("username"));
		assertEquals(2, ((MockUser1) users[3]).getWhatever());
		assertEquals("Káva", ((MockUser1) users[3]).getStuff());
	}
	
	@Test
	public void testReadPlatformUsers() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		PlatformUser[] users = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("first_name", "Spongebob");
		
		users = storage.readPlatformUsers(props);
		
		assertNotNull(users);
		assertEquals(1, users.length);
		assertEquals("Spongebob", users[0].getFirstName());
		assertEquals("Squarepants", users[0].getLastName());
	}
	
	@Test
	public void testReadPlatformUsersNoMatches() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		PlatformUser[] users = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("first_name", "George");
		
		users = storage.readPlatformUsers(props);
		
		assertNotNull(users);
		assertEquals(0, users.length);
	}
	
	@Test
	public void testReadPlatformUsersNoProperties() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		PlatformUser[] users = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		users = storage.readPlatformUsers(props);
		
		assertNotNull(users);
		assertEquals(3, users.length);
	}
	
	@Test
	public void testReadPlatformUsersPasswordShouldBeNeglected() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		PlatformUser[] users = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("password", "qwerty");
		
		users = storage.readPlatformUsers(props);
		
		assertNotNull(users);
		assertEquals(3, users.length);
	}
	
	@Test
	public void testReadPlatformUsersMoreThanOneCriterion() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		PlatformUser[] users = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("first_name", "Spongebob");
		props.put("last_name", "Kent");
		
		users = storage.readPlatformUsers(props);
		
		assertNotNull(users);
		assertEquals(0, users.length);
	}
	
	@Test
	public void testReadPlatformUsersCriterionIsNotString() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		PlatformUser[] users = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("id", 2);
		
		users = storage.readPlatformUsers(props);
		
		assertNotNull(users);
		assertEquals(1, users.length);
		assertEquals("Clark", users[0].getFirstName());
		assertEquals("Kent", users[0].getLastName());
	}
	
	@Test
	public void testReadPlatformUsersCriterionIsString() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		Properties props = new Properties();
		PlatformUser[] users = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("email", "ne");
		
		users = storage.readPlatformUsers(props);
		
		assertNotNull(users);
		assertEquals(2, users.length);
		assertEquals("Bruce", users[0].getFirstName());
		assertEquals("Wayne", users[0].getLastName());
		assertEquals("bruce@waynecorp.com", users[0].getEmail());
		assertEquals("Clark", users[1].getFirstName());
		assertEquals("Kent", users[1].getLastName());
		assertEquals("clark.kent@dailyplanet.com", users[1].getEmail());
	}

	@Test
	public void testDeleteElisDataObject() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667779");
		MockDataObject1 mdo = new MockDataObject1();
		mdo.setDataId(uuid);
		
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
		mdo.setDataId(uuid);
		
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
		mdo.setDataId(uuid);
		
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

		mdo1.setDataId(UUID.fromString("00001111-2222-3333-4444-555566667779"));
		mdo2.setDataId(UUID.fromString("00001111-2222-3333-4444-55556666777B"));
		
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

		mdo1.setDataId(UUID.fromString("00001111-2222-3333-4444-555566667779"));
		mdo2.setDataId(UUID.fromString("00001111-2222-3333-4444-555566667772"));
		
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

		mdo1.setDataId(UUID.fromString("00001111-2222-3333-4444-555566667779"));
		mdo2.setDataId(UUID.fromString("00001111-2222-3333-4444-555566660000"));
		
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
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		
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
		assertEquals(uuid, mdo.getDataId());
		assertEquals("Veles", mdo.getBar());
		assertEquals(5, mdo.getFoo());
		assertEquals(ownerid, mdo.getOwnerId());
	}

	@Test
	public void testUpdateElisDataObjectNoId() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1 mdo = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667779");
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		
		try {
			mdo = (MockDataObject1) storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		mdo.setDataId(null);
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
		assertEquals(uuid, mdo.getDataId());
		assertEquals("Jezibaba", mdo.getBar());
		assertEquals(17, mdo.getFoo());
		assertEquals(ownerid, mdo.getOwnerId());
	}

	@Test
	public void testUpdateElisDataObjectNonExistingId() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		MockDataObject1 mdo = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667779");
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		
		try {
			mdo = (MockDataObject1) storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		mdo.setDataId(UUID.fromString("0000dead-beef-3333-4444-555566667777"));
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
		assertEquals(uuid, mdo.getDataId());
		assertEquals("Jezibaba", mdo.getBar());
		assertEquals(17, mdo.getFoo());
		assertEquals(ownerid, mdo.getOwnerId());
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
		UUID ownerid1 = UUID.fromString("00001111-2222-dead-beef-555566667771");
		UUID ownerid2 = UUID.fromString("00001111-2222-dead-beef-555566667772");
	
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
		assertEquals(uuid1, mdo1.getDataId());
		assertEquals("Veles", mdo1.getBar());
		assertEquals(5, mdo1.getFoo());
		assertEquals(ownerid1, mdo1.getOwnerId());
	
		assertNotNull(mdo2);
		assertEquals(uuid2, mdo2.getDataId());
		assertEquals("Perun", mdo2.getBar());
		assertEquals(7, mdo2.getFoo());
		assertEquals(ownerid2, mdo2.getOwnerId());
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
		UUID dataid1 = UUID.fromString("00001111-2222-3333-4444-555566667779");
		UUID dataid2 = UUID.fromString("00001111-2222-3333-4444-555566667772");
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		
		try {
			mdo1 = (MockDataObject1) storage.readData(dataid1);
			mdo2 = (MockDataObject2) storage.readData(dataid2);
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
			mdo1 = (MockDataObject1) storage.readData(dataid1);
			mdo2 = (MockDataObject2) storage.readData(dataid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mdo1);
		assertEquals(dataid1, mdo1.getDataId());
		assertEquals("Veles", mdo1.getBar());
		assertEquals(5, mdo1.getFoo());
		assertEquals(ownerid, mdo1.getOwnerId());
		
		assertNotNull(mdo2);
		assertEquals(dataid2, mdo2.getDataId());
		assertEquals(7.1, mdo2.getBaz(), 0.000001);
		assertEquals(ownerid, mdo2.getOwnerId());
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
		UUID ownerid1 = UUID.fromString("00001111-2222-dead-beef-555566667771");
		UUID ownerid2 = UUID.fromString("00001111-2222-dead-beef-555566667772");
	
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
		mdo2.setDataId(null);
	
		edos[0] = mdo1;
		edos[1] = mdo2;
	
		try {
			storage.update(edos);
			fail("This shouldn't happen");
		} catch (StorageException e) {
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
		assertEquals(uuid1, mdo1.getDataId());
		assertEquals("Veles", mdo1.getBar());
		assertEquals(5, mdo1.getFoo());
		assertEquals(ownerid1, mdo1.getOwnerId());
	
		assertNotNull(mdo2);
		assertEquals(uuid2, mdo2.getDataId());
		assertEquals("Domovoj", mdo2.getBar());
		assertEquals(17, mdo2.getFoo());
		assertEquals(ownerid2, mdo2.getOwnerId());
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
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		String password = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		password = ((PlatformUserIdentifier) pu.getIdentifier()).getPassword();
		
		uid.setUsername("George");
		uid.setPassword("Horse");
		pu.setIdentifier(uid);
		pu.setFirstName("George");
		pu.setLastName("Ofthejungle");
		pu.setEmail("george@djungle.za");
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(1, ((PlatformUserIdentifier) pu.getIdentifier()).getId());
		assertEquals("George", ((PlatformUserIdentifier) pu.getIdentifier()).getUsername());
		assertFalse(password.equals(((PlatformUserIdentifier) pu.getIdentifier()).getPassword()));
		assertEquals("George", ((PlatformUser) pu).getFirstName());
		assertEquals("Ofthejungle", ((PlatformUser) pu).getLastName());
		assertEquals("george@djungle.za", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoId() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		uid.setId(0);
		pu.setIdentifier(uid);
		
		try {
			storage.update(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoUserName() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		uid.setUsername(null);
		pu.setIdentifier(uid);
		
		try {
			storage.update(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserPlatformUserEmptyUserName() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		uid.setUsername("");
		pu.setIdentifier(uid);
		
		try {
			storage.update(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoPassword() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		String password = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		password = ((PlatformUserIdentifier) pu.getIdentifier()).getPassword();
		
		uid.setUsername("George");
		pu.setIdentifier(uid);
		pu.setFirstName("George");
		pu.setLastName("Ofthejungle");
		pu.setEmail("george@djungle.za");
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(1, ((PlatformUserIdentifier) pu.getIdentifier()).getId());
		assertEquals("George", ((PlatformUserIdentifier) pu.getIdentifier()).getUsername());
		assertEquals(password, ((PlatformUserIdentifier) pu.getIdentifier()).getPassword());
		assertEquals("George", ((PlatformUser) pu).getFirstName());
		assertEquals("Ofthejungle", ((PlatformUser) pu).getLastName());
		assertEquals("george@djungle.za", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserEmptyPassword() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockPlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		String password = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		password = ((PlatformUserIdentifier) pu.getIdentifier()).getPassword();
		
		uid.setUsername("George");
		uid.setPassword("");
		pu.setIdentifier(uid);
		pu.setFirstName("George");
		pu.setLastName("Ofthejungle");
		pu.setEmail("george@djungle.za");
		
		try {
			storage.update(pu);
			pu = null;
			uid.setPassword("Robin");
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(1, ((PlatformUserIdentifier) pu.getIdentifier()).getId());
		assertEquals("George", ((PlatformUserIdentifier) pu.getIdentifier()).getUsername());
		assertEquals(password, ((PlatformUserIdentifier) pu.getIdentifier()).getPassword());
		assertEquals("George", ((PlatformUser) pu).getFirstName());
		assertEquals("Ofthejungle", ((PlatformUser) pu).getLastName());
		assertEquals("george@djungle.za", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoFirstName() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockPlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setFirstName(null);
		
		try {
			storage.update(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserPlatformUserEmptyFirstName() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockPlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setFirstName("");
		
		try {
			storage.update(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoLastName() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockPlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setLastName(null);
		
		try {
			storage.update(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserPlatformUserEmptyLastName() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockPlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setLastName("");
		
		try {
			storage.update(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoEmail() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockPlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setEmail(null);
		
		try {
			storage.update(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserPlatformUserEmptyEmail() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockPlatformUserIdentifier uid = new MockPlatformUserIdentifier("Batman", "Robin");
		PlatformUser pu = null;
		
		factory.registerProvider(new MockUser1Provider());
		
		uid.setId(1);
		
		try {
			pu = (PlatformUser) storage.readUser(uid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setEmail("");
		
		try {
			storage.update(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUser() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUser1 mu = null;
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
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
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUser1 mu = null;
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
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
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUser1 mu = new MockUser1(null, "", 0);
		
		factory.registerProvider(new MockUser1Provider());
		
		mu.setIdentifier(null);
		
		try {
			storage.update(mu);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
	}

	@Test
	public void testUpdateAbstractUserArray() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		User[] users = new User[2];
		MockUser1 mu1 = null;
		MockUser1 mu2 = null;
		UUID uuid1 = UUID.fromString("00001111-2222-dead-beef-555566667772");
		UUID uuid2 = UUID.fromString("00001111-2222-dead-beef-555566667773");
		
		factory.registerProvider(new MockUser1Provider());
		
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
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		User[] users = new User[2];
		MockUser1 mu1 = null;
		MockUser2 mu2 = null;
		UUID uuid1 = UUID.fromString("00001111-2222-dead-beef-555566667772");
		UUID uuid2 = UUID.fromString("0000dead-beef-3333-4444-555566667772");

		factory.registerProvider(new MockUser1Provider());
		factory.registerProvider(new MockUser2Provider());
		
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
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		User[] users = new User[2];
		MockUser1 mu1 = null;
		MockUser1 mu2 = null;
		UUID uuid1 = UUID.fromString("00001111-2222-dead-beef-555566667772");
		UUID uuid2 = UUID.fromString("00001111-2222-dead-beef-555566667773");
		
		factory.registerProvider(new MockUser1Provider());
		
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
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		User[] users = new User[2];
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			storage.update(users);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
	}

	@Test
	public void testUpdateAbstractUserArrayOneUserIsEmpty() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		User[] users = new User[2];
		MockUser1 mu1 = null;
		MockUser1 mu2 = new MockUser1(null, "", 0);
		UUID uuid1 = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockUser1Provider());
		
		mu2.setIdentifier(null);
		
		try {
			mu1 = (MockUser1) storage.readUser(uuid1);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		mu1.setStuff("Veles");
		mu1.setWhatever(5);
		
		users[0] = mu1;
		users[1] = mu2;
		
		try {
			storage.update(users);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
	}

//	@Test
//	public void testSelectOneHit() {
//		buildAndPopulateMDO1Table();
//
//		UserFactory factory = new UserFactoryImpl();
//		Storage storage = new StorageImpl(connection, factory);
//		se.mah.elis.services.storage.result.ResultSet rs = null;
//		MockDataObject1 mdo = null;
//		Query query = new Query();
//		
//		query.setDataType(MockDataObject1.class)
//			.setPredicate((new SimplePredicate(CriterionType.EQ))
//					.setField("bar")
//					.setCriterion("Jezibaba"));
//		
//		try {
//			rs = storage.select(query);
//		} catch (StorageException e) {
//			e.printStackTrace();
//			fail("This shouldn't happen");
//		}
//		
//		assertNotNull(rs);
//		assertEquals(1, rs.size());
//		assertEquals(MockDataObject1.class, rs.getObjectType());
//		
//		mdo = (MockDataObject1) rs.first();
//		
//		assertEquals(UUID.fromString("00001111-2222-3333-4444-555566667779"), mdo.getDataId());
//		assertEquals("Jezibaba", mdo.getBar());
//		assertEquals(17, mdo.getFoo());
//		assertEquals(1, mdo.getOwnerId());
//	}
//
//	@Test
//	public void testSelectOneHitAbstractUser() {
//		buildAndPopulateMU1Table();
//
//		UserFactory factory = new UserFactoryImpl();
//		Storage storage = new StorageImpl(connection, factory);
//		se.mah.elis.services.storage.result.ResultSet rs = null;
//		MockUser1 mu = null;
//		Query query = new Query();
//		
//		factory.registerProvider(new MockUser1Provider());
//		
//		query.setDataType(MockUser1.class)
//			.setPredicate((new SimplePredicate(CriterionType.EQ))
//					.setField("stuff")
//					.setCriterion("Vinea"));
//		
//		try {
//			rs = storage.select(query);
//		} catch (StorageException e) {
//			e.printStackTrace();
//			fail("This shouldn't happen");
//		}
//		
//		assertNotNull(rs);
//		assertEquals(1, rs.size());
//		assertEquals(MockUser1.class, rs.getObjectType());
//		
//		mu = (MockUser1) rs.first();
//		
//		assertEquals(UUID.fromString("00001111-2222-dead-beef-555566667772"), mu.getUserId());
//		assertEquals("Vinea", mu.getStuff());
//		assertEquals(22, mu.getWhatever());
//	}
//
//	@Test
//	public void testSelectSeveralHits() {
//		buildAndPopulateMDO1Table();
//
//		UserFactory factory = new UserFactoryImpl();
//		Storage storage = new StorageImpl(connection, factory);
//		se.mah.elis.services.storage.result.ResultSet rs = null;
//		MockDataObject1 mdo = null;
//		Query query = new Query();
//		
//		query.setDataType(MockDataObject1.class)
//			.setPredicate((new ChainingPredicate(ChainingType.OR))
//				.setLeft((new SimplePredicate(CriterionType.EQ))
//					.setField("bar")
//					.setCriterion("Jezibaba"))
//				.setRight((new SimplePredicate(CriterionType.EQ))
//					.setField("foo")
//					.setCriterion(42)));
//		
//		try {
//			rs = storage.select(query);
//		} catch (StorageException e) {
//			e.printStackTrace();
//			fail("This shouldn't happen");
//		}
//		
//		assertNotNull(rs);
//		assertEquals(2, rs.size());
//		assertEquals(MockDataObject1.class, rs.getObjectType());
//		
//		mdo = (MockDataObject1) rs.first();
//		
//		assertEquals(UUID.fromString("00001111-2222-3333-4444-555566667779"), mdo.getDataId());
//		assertEquals("Jezibaba", mdo.getBar());
//		assertEquals(17, mdo.getFoo());
//		assertEquals(1, mdo.getOwnerId());
//		
//		mdo = (MockDataObject1) rs.next();
//		
//		assertEquals(UUID.fromString("00001111-2222-3333-4444-555566667777"), mdo.getDataId());
//		assertEquals("Baba Roga", mdo.getBar());
//		assertEquals(17, mdo.getFoo());
//		assertEquals(1, mdo.getOwnerId());
//	}

//	@Test
//	public void testSelectNoHits() {
//		buildAndPopulateMDO1Table();
//
//		UserFactory factory = new UserFactoryImpl();
//		Storage storage = new StorageImpl(connection, factory);
//		se.mah.elis.services.storage.result.ResultSet rs = null;
//		Query query = new Query();
//		
//		query.setDataType(MockDataObject1.class)
//			.setPredicate((new SimplePredicate(CriterionType.EQ))
//					.setField("bar")
//					.setCriterion("Bohovia sú mrtve"));
//		
//		try {
//			rs = storage.select(query);
//		} catch (StorageException e) {
//			e.printStackTrace();
//			fail("This shouldn't happen");
//		}
//		
//		assertNotNull(rs);
//		assertEquals(0, rs.size());
//		assertEquals(MockDataObject1.class, rs.getObjectType());
//	}
//
//	@Test
//	public void testDeleteQuery() {
//		buildAndPopulateMDO1Table();
//
//		UserFactory factory = new UserFactoryImpl();
//		Storage storage = new StorageImpl(connection, factory);
//		se.mah.elis.services.storage.result.ResultSet rs = null;
//		Query query = new Query();
//		
//		query.setDataType(MockDataObject1.class)
//			.setPredicate((new SimplePredicate(CriterionType.EQ))
//					.setField("bar")
//					.setCriterion("Jezibaba"));
//		
//		try {
//			storage.delete(query);
//		} catch (StorageException e) {
//			e.printStackTrace();
//			fail("This shouldn't happen");
//		}
//		
//		assertEquals(EDO1_COUNT - 1, countBindingsInDB(new MockDataObject1()));
//		assertEquals(EDO1_COUNT - 1, countBindingsInDB());
//	}
//
//	@Test
//	public void testDeleteQueryNoMatch() {
//		buildAndPopulateMDO1Table();
//
//		UserFactory factory = new UserFactoryImpl();
//		Storage storage = new StorageImpl(connection, factory);
//		se.mah.elis.services.storage.result.ResultSet rs = null;
//		Query query = new Query();
//		
//		query.setDataType(MockDataObject1.class)
//			.setPredicate((new SimplePredicate(CriterionType.EQ))
//					.setField("bar")
//					.setCriterion("Bohovie sú mrtve"));
//		
//		try {
//			storage.delete(query);
//		} catch (StorageException e) {
//			e.printStackTrace();
//			fail("This shouldn't happen");
//		}
//		
//		assertEquals(EDO1_COUNT - 1, countBindingsInDB(new MockDataObject1()));
//		assertEquals(EDO1_COUNT - 1, countBindingsInDB());
//	}
//
//	@Test
//	public void testDeleteQuerySeveralMatches() {
//		buildAndPopulateMDO1Table();
//
//		UserFactory factory = new UserFactoryImpl();
//		Storage storage = new StorageImpl(connection, factory);
//		se.mah.elis.services.storage.result.ResultSet rs = null;
//		Query query = new Query();
//		
//		query.setDataType(MockDataObject1.class)
//		.setPredicate((new ChainingPredicate(ChainingType.OR))
//			.setLeft((new SimplePredicate(CriterionType.EQ))
//				.setField("bar")
//				.setCriterion("Jezibaba"))
//			.setRight((new SimplePredicate(CriterionType.EQ))
//				.setField("foo")
//				.setCriterion(42)));
//		
//		try {
//			storage.delete(query);
//		} catch (StorageException e) {
//			e.printStackTrace();
//			fail("This shouldn't happen");
//		}
//		
//		assertEquals(EDO1_COUNT - 2, countBindingsInDB(new MockDataObject1()));
//		assertEquals(EDO1_COUNT - 2, countBindingsInDB());
//	}

}
