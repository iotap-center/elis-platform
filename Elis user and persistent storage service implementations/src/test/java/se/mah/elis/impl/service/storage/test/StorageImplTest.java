package se.mah.elis.impl.service.storage.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.impl.service.storage.test.mock.MockConnection;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject1;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject1Provider;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject2;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject2Provider;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject3;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject3Provider;
import se.mah.elis.impl.service.storage.test.mock.MockPlatformUser;
import se.mah.elis.impl.service.storage.test.mock.MockUser1;
import se.mah.elis.impl.service.storage.test.mock.MockUser1Provider;
import se.mah.elis.impl.service.storage.test.mock.MockUser2;
import se.mah.elis.impl.service.storage.test.mock.MockUser2Provider;
import se.mah.elis.impl.service.storage.test.mock.MockUser3;
import se.mah.elis.impl.service.storage.test.mock.MockUser4;
import se.mah.elis.impl.service.storage.test.mock.MockUser4Provider;
import se.mah.elis.impl.services.storage.StorageImpl;
import se.mah.elis.impl.services.storage.StorageUtils;
import se.mah.elis.impl.services.storage.factory.DataObjectFactoryImpl;
import se.mah.elis.impl.services.users.factory.UserFactoryImpl;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.factory.DataObjectFactory;
import se.mah.elis.services.storage.query.ChainingPredicate;
import se.mah.elis.services.storage.query.Query;
import se.mah.elis.services.storage.query.SimplePredicate;
import se.mah.elis.services.storage.query.ChainingPredicate.ChainingType;
import se.mah.elis.services.storage.query.SimplePredicate.CriterionType;
import se.mah.elis.services.users.AbstractUser;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.factory.UserFactory;

public class StorageImplTest {

	// Table counts
	private static int MDO1_COUNT = 6;
	private static int MDO2_COUNT = 3;
	private static int MDO3_COUNT = 4;
	private static int MU1_COUNT = 3;
	private static int MU2_COUNT = 3;
	private static int MU4_COUNT = 3;
	private static int PU_COUNT = 3;
	
	// Mock object IDs
	
	// MDO1
	private static String MDO1_1s = "00001111222233334444555566667777";
	private static UUID MDO1_1u = UUID.fromString("00001111-2222-3333-4444-555566667777");
	private static String MDO1_2s = "00001111222233334444555566667778";
	private static UUID MDO1_2u = UUID.fromString("00001111-2222-3333-4444-555566667778");
	private static String MDO1_3s = "00001111222233334444555566667779";
	private static UUID MDO1_3u = UUID.fromString("00001111-2222-3333-4444-555566667779");
	private static String MDO1_4s = "0000111122223333444455556666777A";
	private static UUID MDO1_4u = UUID.fromString("00001111-2222-3333-4444-55556666777A");
	private static String MDO1_5s = "0000111122223333444455556666777B";
	private static UUID MDO1_5u = UUID.fromString("00001111-2222-3333-4444-55556666777B");
	private static String MDO1_6s = "0000111122223333444455556666777C";
	private static UUID MDO1_6u = UUID.fromString("00001111-2222-3333-4444-55556666777C");
	
	// MDO2
	private static String MDO2_1s = "00001111222233334444555566667771";
	private static UUID MDO2_1u = UUID.fromString("00001111-2222-3333-4444-555566667771");
	private static String MDO2_2s = "00001111222233334444555566667772";
	private static UUID MDO2_2u = UUID.fromString("00001111-2222-3333-4444-555566667772");
	private static String MDO2_3s = "00001111222233334444555566667773";
	private static UUID MDO2_3u = UUID.fromString("00001111-2222-3333-4444-555566667773");
	
	// MDO3
	private static String MDO3_1s = "10001111222233334444555566667771";
	private static UUID MDO3_1u = UUID.fromString("10001111-2222-3333-4444-555566667771");
	private static String MDO3_2s = "10001111222233334444555566667772";
	private static UUID MDO3_2u = UUID.fromString("10001111-2222-3333-4444-555566667772");
	private static String MDO3_3s = "10001111222233334444555566667773";
	private static UUID MDO3_3u = UUID.fromString("10001111-2222-3333-4444-555566667773");
	private static String MDO3_4s = "10001111222233334444555566667774";
	private static UUID MDO3_4u = UUID.fromString("10001111-2222-3333-4444-555566667774");
	
	// MU1
	private static String MU1_1s = "000011112222deadbeef555566667771";
	private static UUID MU1_1u = UUID.fromString("00001111-2222-dead-beef-555566667771");
	private static String MU1_2s = "000011112222deadbeef555566667772";
	private static UUID MU1_2u = UUID.fromString("00001111-2222-dead-beef-555566667772");
	private static String MU1_3s = "000011112222deadbeef555566667773";
	private static UUID MU1_3u = UUID.fromString("00001111-2222-dead-beef-555566667773");
	
	// MU2
	private static String MU2_1s = "0000deadbeef33334444555566667771";
	private static UUID MU2_1u = UUID.fromString("0000dead-beef-3333-4444-555566667771");
	private static String MU2_2s = "0000deadbeef33334444555566667772";
	private static UUID MU2_2u = UUID.fromString("0000dead-beef-3333-4444-555566667772");
	private static String MU2_3s = "0000deadbeef33334444555566667773";
	private static UUID MU2_3u = UUID.fromString("0000dead-beef-3333-4444-555566667773");
	
	// MU4
	private static String MU4_1s = "1000deadbeef33334444555566667771";
	private static UUID MU4_1u = UUID.fromString("1000dead-beef-3333-4444-555566667771");
	private static String MU4_2s = "1000deadbeef33334444555566667772";
	private static UUID MU4_2u = UUID.fromString("1000dead-beef-3333-4444-555566667772");
	private static String MU4_3s = "1000deadbeef33334444555566667773";
	private static UUID MU4_3u = UUID.fromString("1000dead-beef-3333-4444-555566667773");
	
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
					"VALUES (x'" + MDO1_1s +"', x'" + ownerId1 + "', 42, 'Baba Roga', '2000-01-01 00:00:00')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + MDO1_2s +"', x'" + ownerId1 + "', 13, 'Baba Jaga', '2000-01-01 00:00:01')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + MDO1_3s +"', x'" + ownerId1 + "', 17, 'Jezibaba', '2000-01-01 00:00:02')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + MDO1_4s +"', x'" + ownerId2 + "', 17, 'Domovoj', '2000-01-01 00:00:03')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + MDO1_5s +"', x'" + ownerId2 + "', 5, 'Domovik', '2000-01-01 00:00:04')");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` " +
					"VALUES (x'" + MDO1_6s +"', x'" + ownerId3 + "', 5, 'Perun', '2000-01-01 00:00:05')");
			
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO1_1s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO1_2s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO1_3s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO1_4s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO1_5s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO1_6s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject1')");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void buildAndPopulateMDO2Table() {
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
					"VALUES (x'" + MDO2_1s +"', x'" + ownerId1 + "', 1.1, '2000-01-01 00:00:00');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` " +
					"VALUES (x'" + MDO2_2s +"', x'" + ownerId1 + "', 0.5, '2000-01-01 00:00:01');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` " +
					"VALUES (x'" + MDO2_3s +"', x'" + ownerId2 + "', 7.1, '2000-01-01 00:00:02');");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO2_1s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO2_2s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject2')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO2_3s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject2')");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Please note that in order to test the collection mechanism used by MDO3
	 * objects, the buildAndPopulateMDO2Table() should also be run, as MDO3
	 * uses MDO2 objects in the collections.
	 */
	private void buildAndPopulateMDO3Table() {
		String ownerId1 = "000011112222deadbeef555566667771";
		String ownerId2 = "000011112222deadbeef555566667772";
		
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject3` (" +
						"`dataid` BINARY(16) PRIMARY KEY, " +
						"`ownerid` BINARY(16), " +
						"`baz` FLOAT, " +
						"`created` TIMESTAMP)");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject3` " +
					"VALUES (x'" + MDO3_1s +"', x'" + ownerId1 + "', 1.1, '2000-01-01 00:00:00');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject3` " +
					"VALUES (x'" + MDO3_2s +"', x'" + ownerId1 + "', 0.5, '2000-01-01 00:00:01');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject3` " +
					"VALUES (x'" + MDO3_3s +"', x'" + ownerId2 + "', 7.1, '2000-01-01 00:00:02');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockDataObject3` " +
					"VALUES (x'" + MDO3_4s +"', x'" + ownerId2 + "', 7.0, '2000-01-01 00:00:02');");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO3_1s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject3')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO3_2s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject3')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO3_3s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject3')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MDO3_4s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockDataObject3')");
			
			stmt.execute("INSERT INTO `collections` VALUES (x'" + MDO3_1s + "', " +
					"x'" + MDO2_1s + "', 'mdos')");
			stmt.execute("INSERT INTO `collections` VALUES (x'" + MDO3_1s + "', " +
					"x'" + MDO2_2s + "', 'mdos')");
			stmt.execute("INSERT INTO `collections` VALUES (x'" + MDO3_1s + "', " +
					"x'" + MDO2_3s + "', 'mdos')");
			stmt.execute("INSERT INTO `collections` VALUES (x'" + MDO3_2s + "', " +
					"x'" + MDO2_1s + "', 'mdos')");
			stmt.execute("INSERT INTO `collections` VALUES (x'" + MDO3_2s + "', " +
					"x'" + MDO2_3s + "', 'mdos')");
			stmt.execute("INSERT INTO `collections` VALUES (x'" + MDO3_4s + "', " +
					"x'" + MDO2_2s + "', 'mdos')");
			
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
	
	/*
	 * Please note that in order to test the collection mechanism used by MDO3
	 * objects, the buildAndPopulateMDO2Table() should also be run, as MDO3
	 * uses MDO2 objects in the collections.
	 */
	private void buildAndPopulateMU4Table() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE `se-mah-elis-impl-service-storage-test-mock-MockUser4` (" +
						"`uuid` BINARY(16) PRIMARY KEY, " +
						"`service_name` VARCHAR(9), " +
						"`id_number` INTEGER, " +
						"`username` VARCHAR(32), " +
						"`password` VARCHAR(32), " +
						"`stuff` VARCHAR(32), " +
						"`created` TIMESTAMP)");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser4` " +
					"VALUES (x'" + MU4_1s +"', 'test', 1, 'Batman', 'Robin', 'Kvass', '2000-01-01 00:00:00');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser4` " +
					"VALUES (x'" + MU4_2s +"', 'test', 1, 'Superman', 'Lois Lane', 'Kompot', '2000-01-01 00:00:01');");
			stmt.execute("INSERT INTO `se-mah-elis-impl-service-storage-test-mock-MockUser4` " +
					"VALUES (x'" + MU4_3s +"', 'test', 1, 'Spiderman', 'Mary Jane', 'Slivovica', '2000-01-01 00:00:01');");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MU4_1s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser4')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MU4_2s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser4')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + MU4_3s +"', " +
					"'se-mah-elis-impl-service-storage-test-mock-MockUser4')");

			stmt.execute("INSERT INTO `collections` VALUES (x'" + MU4_1s + "', " +
					"x'" + MDO2_1s + "', 'edos')");
			stmt.execute("INSERT INTO `collections` VALUES (x'" + MU4_1s + "', " +
					"x'" + MDO2_2s + "', 'edos')");
			stmt.execute("INSERT INTO `collections` VALUES (x'" + MU4_3s + "', " +
					"x'" + MDO2_2s + "', 'edos')");
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void populatePUTable() {
		String uuid1 = "11111111111111111111111111111111";
		String uuid2 = "11111111111111111111111111111112";
		String uuid3 = "11111111111111111111111111111113";
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("INSERT INTO `se-mah-elis-services-users-PlatformUser` " +
					"VALUES (x'" + uuid1 +"', 'Batman', PASSWORD('Robin'), 'Bruce', 'Wayne', 'bruce@waynecorp.com', '2000-01-01 00:00:00');");
			stmt.execute("INSERT INTO `se-mah-elis-services-users-PlatformUser` " +
					"VALUES (x'" + uuid2 +"', 'Superman', PASSWORD('Lois Lane'), 'Clark', 'Kent', 'clark.kent@dailyplanet.com', '2000-01-01 00:00:01');");
			stmt.execute("INSERT INTO `se-mah-elis-services-users-PlatformUser` " +
					"VALUES (x'" + uuid3 +"', 'Spongebob Squarepants', PASSWORD('Patrick Seastar'), 'Spongebob', 'Squarepants', 'spongebob@krustykrab.com', '2000-01-01 00:00:02');");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid1 +"', " +
					"'se-mah-elis-services-users-PlatformUser')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid2 +"', " +
					"'se-mah-elis-services-users-PlatformUser')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid3 +"', " +
					"'se-mah-elis-services-users-PlatformUser')");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void makeMDO1MDO2() {
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` TO `se-mah-elis-impl-service-storage-test-mock-MockDataObject2`;");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'10001111222233334444555566667777' where `dataid` = x'" + MDO1_1s + "';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'10001111222233334444555566667778' where `dataid` = x'" + MDO1_2s + "';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'10001111222233334444555566667779' where `dataid` = x'" + MDO1_3s + "';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'1000111122223333444455556666777A' where `dataid` = x'" + MDO1_4s + "';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'1000111122223333444455556666777B' where `dataid` = x'" + MDO1_5s + "';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` SET `dataid` = x'1000111122223333444455556666777C' where `dataid` = x'" + MDO1_6s + "';");
		runQuery("UPDATE `object_lookup_table` SET `stored_in` = 'se-mah-elis-impl-service-storage-test-mock-MockDataObject2';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'10001111222233334444555566667777' where `id` = x'" + MDO1_1s + "';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'10001111222233334444555566667778' where `id` = x'" + MDO1_2s + "';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'10001111222233334444555566667779' where `id` = x'" + MDO1_3s + "';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000111122223333444455556666777A' where `id` = x'" + MDO1_4s + "';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000111122223333444455556666777B' where `id` = x'" + MDO1_5s + "';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'1000111122223333444455556666777C' where `id` = x'" + MDO1_6s + "';");
		buildAndPopulateMDO1Table();
	}

	private void makeMDO2MDO1() {
		runQuery("RENAME TABLE `se-mah-elis-impl-service-storage-test-mock-MockDataObject2` TO `se-mah-elis-impl-service-storage-test-mock-MockDataObject1`;");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` SET `dataid` = x'" + MDO3_1s + "' where `dataid` = x'" + MDO2_1s + "';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` SET `dataid` = x'" + MDO3_2s + "' where `dataid` = x'" + MDO2_2s + "';");
		runQuery("UPDATE `se-mah-elis-impl-service-storage-test-mock-MockDataObject1` SET `dataid` = x'" + MDO3_3s + "' where `dataid` = x'" + MDO2_3s + "';");
		runQuery("UPDATE `object_lookup_table` SET `stored_in` = 'se-mah-elis-impl-service-storage-test-mock-MockDataObject1';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'" + MDO3_1s + "' where `id` = x'" + MDO2_1s + "';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'" + MDO3_2s + "' where `id` = x'" + MDO2_2s + "';");
		runQuery("UPDATE `object_lookup_table` SET `id` = x'" + MDO3_3s + "' where `id` = x'" + MDO2_3s + "';");
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
			stmt.execute("TRUNCATE TABLE collections;");
			stmt.execute("TRUNCATE TABLE `se-mah-elis-services-users-PlatformUser`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockDataObject1`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockDataObject2`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockDataObject3`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockUser1`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockUser2`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockUser3`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-impl-service-storage-test-mock-MockUser4`;");
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
		} else if (edo instanceof MockDataObject3) {
			query = "SELECT count(*) FROM `se-mah-elis-impl-service-storage-test-mock-MockDataObject3`";
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
		String table = user.getClass().getName().replace('.', '-');
		
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
	
	private int countObjectsInCollection(UUID collector) {
		Statement statement;
		int bindings = -1;
		
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT count(*) FROM `collections` " +
												"WHERE `collecting_object` = x'" +
					collector.toString().replace("-", "") + "';");
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
		String table = user.getClass().getName().replace('.', '-');
		
		query = "SELECT * FROM `" + table + "`";
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
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
		
		assertEquals(MDO1_COUNT + 1, countBindingsInDB(mdo));
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
		
		assertEquals(MDO1_COUNT + 1, countBindingsInDB(mdo));
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
		
		assertEquals(MDO2_COUNT, countBindingsInDB(mdo));
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
		
		assertEquals(MDO1_COUNT, countBindingsInDB(mdo));
	}
	
	@Test
	public void testInsertElisDataObjectAlreadyExists() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		
		UUID ownerId = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		MockDataObject1 mdo = new MockDataObject1();
		
		mdo.setDataId(MDO1_1u);
		mdo.setOwnerId(ownerId);
		mdo.setBar("Testing");
		mdo.setFoo(42);
		
		try {
			storage.insert(mdo);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(MDO1_COUNT, countBindingsInDB(mdo));
	}

	@Test
	public void testInsertElisDataObjectWithCollectionExistingObjects() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = new MockDataObject3();
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		Collection collection = mdo.getCollection();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		UUID user = UUID.fromString("00001111-2222-dead-beef-555566667771");

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		mdo.setDataId(uuid);
		mdo.setOwnerId(uuid);
		mdo.setBaz(17);

		collection.add(new MockDataObject2(MDO2_1u, user, 1.1f));
		collection.add(new MockDataObject2(MDO2_2u, user, 0.5f));
		
		try {
			storage.insert(mdo);
			mdo1 = (MockDataObject2) storage.readData(MDO2_1u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		assertEquals(MDO3_COUNT + 1, countBindingsInDB(mdo));
		assertEquals(MDO2_COUNT, countBindingsInDB(new MockDataObject2()));
		assertEquals(2, countObjectsInCollection(uuid));
		assertEquals(MDO2_1u, mdo1.getDataId());
		assertEquals(user, mdo1.getOwnerId());
		assertEquals(1.1, mdo1.getBaz(), 0.01);
		assertEquals(MDO2_2u, mdo2.getDataId());
		assertEquals(user, mdo2.getOwnerId());
		assertEquals(0.5, mdo2.getBaz(), 0.01);
	}

	@Test
	public void testInsertElisDataObjectWithCollectionExistingObjectsOneHasChanges() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = new MockDataObject3();
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		Collection collection = mdo.getCollection();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		UUID user = UUID.fromString("00001111-2222-dead-beef-555566667771");

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		mdo.setDataId(uuid);
		mdo.setOwnerId(user);
		mdo.setBaz(17);

		collection.add(new MockDataObject2(MDO2_1u, user, 1.1f));
		collection.add(new MockDataObject2(MDO2_2u, user, 0.7f)); // Sneaky, this baby has changes done to it.
		
		try {
			storage.insert(mdo);
			mdo1 = (MockDataObject2) storage.readData(MDO2_1u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		assertEquals(MDO3_COUNT + 1, countBindingsInDB(mdo));
		assertEquals(MDO2_COUNT, countBindingsInDB(new MockDataObject2()));
		assertEquals(2, countObjectsInCollection(uuid));
		assertEquals(MDO2_1u, mdo1.getDataId());
		assertEquals(user, mdo1.getOwnerId());
		assertEquals(1.1, mdo1.getBaz(), 0.01);
		assertEquals(MDO2_2u, mdo2.getDataId());
		assertEquals(user, mdo2.getOwnerId());
		assertEquals(0.7, mdo2.getBaz(), 0.01);
	}

	@Test
	public void testInsertElisDataObjectWithCollectionOneNonExistingObject() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = new MockDataObject3();
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		Collection collection = mdo.getCollection();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		UUID user = UUID.fromString("00001111-2222-dead-beef-555566667771");
		UUID newUuid = UUID.randomUUID();

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		mdo.setDataId(uuid);
		mdo.setOwnerId(user);
		mdo.setBaz(17);

		collection.add(new MockDataObject2(MDO2_1u, user, 1.1f));
		collection.add(new MockDataObject2(newUuid, user, 0.3f));
		
		try {
			storage.insert(mdo);
			mdo1 = (MockDataObject2) storage.readData(MDO2_1u);
			mdo2 = (MockDataObject2) storage.readData(newUuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		assertEquals(MDO3_COUNT + 1, countBindingsInDB(mdo));
		assertEquals(MDO2_COUNT + 1, countBindingsInDB(new MockDataObject2()));
		assertEquals(2, countObjectsInCollection(uuid));
		assertEquals(MDO2_1u, mdo1.getDataId());
		assertEquals(user, mdo1.getOwnerId());
		assertEquals(1.1, mdo1.getBaz(), 0.01);
		assertEquals(newUuid, mdo2.getDataId());
		assertEquals(user, mdo1.getOwnerId());
		assertEquals(0.3, mdo2.getBaz(), 0.01);
	}

	@Test
	public void testInsertElisDataObjectWithCollectionNoExistingObjects() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = new MockDataObject3();
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		Collection collection = mdo.getCollection();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		UUID user = UUID.fromString("00001111-2222-dead-beef-555566667771");
		UUID uuid1 = UUID.fromString("f0001111-2222-3333-4444-555566667771");
		UUID uuid2 = UUID.fromString("f0001111-2222-3333-4444-555566667772");

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		mdo.setDataId(uuid);
		mdo.setOwnerId(user);
		mdo.setBaz(17);

		collection.add(new MockDataObject2(uuid1, user, 1.1f));
		collection.add(new MockDataObject2(uuid2, user, 0.3f));
		
		try {
			storage.insert(mdo);
			mdo1 = (MockDataObject2) storage.readData(uuid1);
			mdo2 = (MockDataObject2) storage.readData(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		assertEquals(MDO3_COUNT + 1, countBindingsInDB(mdo));
		assertEquals(MDO2_COUNT + 2, countBindingsInDB(new MockDataObject2()));
		assertEquals(2, countObjectsInCollection(uuid));
		assertEquals(uuid1, mdo1.getDataId());
		assertEquals(user, mdo1.getOwnerId());
		assertEquals(1.1, mdo1.getBaz(), 0.01);
		assertEquals(uuid2, mdo2.getDataId());
		assertEquals(user, mdo2.getOwnerId());
		assertEquals(0.3, mdo2.getBaz(), 0.01);
	}

	@Test
	public void testInsertElisDataObjectWithEmptyCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = new MockDataObject3();
		Collection collection = mdo.getCollection();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		mdo.setOwnerId(uuid);
		mdo.setBaz(17);
		
		try {
			storage.insert(mdo);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}

		assertEquals(MDO3_COUNT + 1, countBindingsInDB(mdo));
		assertEquals(MDO2_COUNT, countBindingsInDB(new MockDataObject2()));
		assertEquals(0, countObjectsInCollection(uuid));
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
		
		assertEquals(MDO1_COUNT + 3, countBindingsInDB(mdo1));
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
		
		assertEquals(MDO1_COUNT + 3, countBindingsInDB(mdo1));
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
		
		assertEquals(MDO1_COUNT, countBindingsInDB(new MockDataObject1()));
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

		assertEquals(MDO1_COUNT + 2, countBindingsInDB(mdo1));
		assertEquals(MDO2_COUNT + 1, countBindingsInDB(mdo3));
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

		assertEquals(MDO1_COUNT + 2, countBindingsInDB(mdo1));
		assertEquals(MDO1_COUNT, countBindingsInDB(mdo3));
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

		assertEquals(MDO1_COUNT + 1, countBindingsInDB(mdo1));
		assertEquals(MDO1_COUNT, countBindingsInDB(mdo3));
	}

	@Test
	public void testInsertAbstractUserPlatformUser() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Kalle", "kvack");
		
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
		assertNotNull(pu.getUserId());
		assertEquals(PU_COUNT + 1, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserAlreadyHasId() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Kalle", "kvack");
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
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
		assertEquals(uuid, pu.getUserId());
		assertEquals(PU_COUNT + 1, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserAlreadyHasExistingId() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Kalle", "kvack");
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
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
		assertEquals(uuid, pu.getUserId());
		assertEquals(PU_COUNT + 1, countBindingsInDB());
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
		assertEquals(PU_COUNT, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasNonEmptyPassword() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Kalle", "kvack");
		
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
		assertNotNull(pu.getUserId());
		assertEquals(PU_COUNT + 1, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasEmptyPassword() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Kalle", "");
		
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(PU_COUNT, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformExistingUserName() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Batman", "kvack");
		
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(PU_COUNT, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasNoUserName() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("", "kvack");
		
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(PU_COUNT, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasNoFirstName() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Kalle", "kvack");
		
		pu.setLastName("Anka");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT + 1, countBindingsInDB(pu));
		assertNotNull(pu.getUserId());
		assertEquals(PU_COUNT + 1, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasNoLastName() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Kalle", "kvack");
		
		pu.setFirstName("Kalle");
		pu.setEmail("kalle.anka@margarinfabriken.nu");
		
		try {
			storage.insert(pu);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT + 1, countBindingsInDB(pu));
		assertNotNull(pu.getUserId());
		assertEquals(PU_COUNT + 1, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserPlatformUserHasNoEmailAddress() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Kalle", "kvack");
		
		pu.setFirstName("Kalle");
		pu.setLastName("Anka");
		
		try {
			storage.insert(pu);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT + 1, countBindingsInDB(pu));
		assertNotNull(pu.getUserId());
		assertEquals(PU_COUNT + 1, countBindingsInDB());
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
		assertEquals(MU1_COUNT + 1, countBindingsInDB(mu));
		assertEquals(MU1_COUNT + 1, countBindingsInDB());
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
		assertEquals(MU1_COUNT + 1, countBindingsInDB(mu));
		assertEquals(MU1_COUNT + 1, countBindingsInDB());
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
		assertEquals(MU1_COUNT, countBindingsInDB(mu));
		assertEquals(MU1_COUNT, countBindingsInDB());
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
		
		assertEquals(MU2_COUNT, countBindingsInDB(mu));
		assertEquals(MU2_COUNT, countBindingsInDB());
	}

	@Test
	public void testInsertAbstractUserHasNoServiceName() {
		Storage storage = new StorageImpl(connection);
		MockUser3 mu = new MockUser3(null, "Foo");
		
		try {
			storage.insert(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testInsertAbstractUserWithCollectionExistingObjects() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = new MockUser4("Horses");
		Collection collection = mu.getCollection();
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		mu.setUserId(uuid);

		collection.add(new MockDataObject2(MDO2_1u, uuid, 1.1f));
		collection.add(new MockDataObject2(MDO2_2u, uuid, 0.5f));
		
		try {
			storage.insert(mu);
			mdo1 = (MockDataObject2) storage.readData(MDO2_1u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mu.getUserId());
		assertEquals(MU4_COUNT + 1, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT, countBindingsInDB(mdo1));
		assertEquals(MDO2_1u, mdo1.getDataId());
		assertEquals(uuid, mdo1.getOwnerId());
		assertEquals(1.1, mdo1.getBaz(), 0.01);
		assertEquals(MDO2_2u, mdo2.getDataId());
		assertEquals(uuid, mdo2.getOwnerId());
		assertEquals(0.5, mdo2.getBaz(), 0.01);
	}

	@Test
	public void testInsertAbstractUserWithCollectionExistingObjectsOneHasChanged() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = new MockUser4("Horses");
		Collection collection = mu.getCollection();
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		mu.setUserId(uuid);

		collection.add(new MockDataObject2(MDO2_1u, uuid, 1.1f));
		collection.add(new MockDataObject2(MDO2_2u, uuid, 0.3f)); // BAM! Changed value.
		
		try {
			storage.insert(mu);
			mdo1 = (MockDataObject2) storage.readData(MDO2_1u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mu.getUserId());
		assertEquals(MU4_COUNT + 1, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT, countBindingsInDB(mdo1));
		assertEquals(MDO2_1u, mdo1.getDataId());
		assertEquals(uuid, mdo1.getOwnerId());
		assertEquals(1.1, mdo1.getBaz(), 0.01);
		assertEquals(MDO2_2u, mdo2.getDataId());
		assertEquals(uuid, mdo2.getOwnerId());
		assertEquals(0.3, mdo2.getBaz(), 0.01);
	}

	@Test
	public void testInsertAbstractUserWithCollectionOneExistingObject() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = new MockUser4("Horses");
		Collection collection = mu.getCollection();
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		UUID owner = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		UUID mdoId = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		mu.setUserId(owner);

		mdo2 = new MockDataObject2(owner, 0.5f);
		mdoId = mdo2.getDataId();
		
		collection.add(new MockDataObject2(MDO2_1u, owner, 1.1f));
		collection.add(mdo2);
		
		mdo2 = null;
		
		try {
			storage.insert(mu);
			mdo1 = (MockDataObject2) storage.readData(MDO2_1u);
			mdo2 = (MockDataObject2) storage.readData(mdoId);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mu.getUserId());
		assertEquals(MU4_COUNT + 1, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT + 1, countBindingsInDB(mdo1));
		assertEquals(MDO2_1u, mdo1.getDataId());
		assertEquals(owner, mdo1.getOwnerId());
		assertEquals(1.1, mdo1.getBaz(), 0.01);
		assertEquals(mdoId, mdo2.getDataId());
		assertEquals(owner, mdo2.getOwnerId());
		assertEquals(0.5, mdo2.getBaz(), 0.01);
	}

	@Test
	public void testInsertAbstractUserWithCollectionNoExistingObjects() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = new MockUser4("Horses");
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		Collection collection = mu.getCollection();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		UUID uuid1 = UUID.fromString("f0001111-2222-3333-4444-555566667771");
		UUID uuid2 = UUID.fromString("f0001111-2222-3333-4444-555566667772");
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		mu.setUserId(uuid);

		collection.add(new MockDataObject2(uuid1, uuid, 1.1f));
		collection.add(new MockDataObject2(uuid2, uuid, 0.5f));
		
		try {
			storage.insert(mu);
			mdo1 = (MockDataObject2) storage.readData(uuid1);
			mdo2 = (MockDataObject2) storage.readData(uuid2);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mu.getUserId());
		assertEquals(MU4_COUNT + 1, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT + 2, countBindingsInDB(new MockDataObject2()));
		assertEquals(uuid1, mdo1.getDataId());
		assertEquals(uuid, mdo1.getOwnerId());
		assertEquals(1.1, mdo1.getBaz(), 0.01);
		assertEquals(uuid2, mdo2.getDataId());
		assertEquals(uuid, mdo2.getOwnerId());
		assertEquals(0.5, mdo2.getBaz(), 0.01);
	}

	@Test
	public void testInsertAbstractUserWithCollectionEmptyCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = new MockUser4("Horses");
		Collection collection = mu.getCollection();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-5555deadbeef");
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		mu.setUserId(uuid);
		
		try {
			storage.insert(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mu.getUserId());
		assertEquals(MU4_COUNT + 1, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT, countBindingsInDB(new MockDataObject2()));
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
		assertEquals(MU1_COUNT + 3, countBindingsInDB(mu1));
		assertEquals(MU1_COUNT + 3, countBindingsInDB());
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
		
		assertEquals(MU2_COUNT + 1, countBindingsInDB(mu1));
		assertEquals(MU1_COUNT, countBindingsInDB(mu2));
		assertEquals(2 * MU1_COUNT + 1, countBindingsInDB());
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
		
		pu.setUsername("user");
		pu.setPassword("pass");
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
		assertEquals(MU1_COUNT + 3, countBindingsInDB(mu1));
		assertEquals(PU_COUNT + MU1_COUNT + 4, countBindingsInDB());
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
		
		assertEquals(MU1_COUNT, countBindingsInDB(new MockUser1()));
		assertEquals(MU1_COUNT, countBindingsInDB());
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
		assertEquals(MU1_COUNT + 3, countBindingsInDB(mu1));
		assertEquals(MU1_COUNT + 3, countBindingsInDB());
	}

	@Test
	public void testReadDataUUID() {
		buildAndPopulateMDO1Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, dof);
		ElisDataObject edo = null;
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		
		dof.registerProvider(new MockDataObject1Provider());
		
		try {
			edo = storage.readData(MDO1_3u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(edo);
		assertTrue(edo instanceof MockDataObject1);
		assertEquals(MDO1_3u, ((MockDataObject1) edo).getDataId());
		assertEquals("Jezibaba", ((MockDataObject1) edo).getBar());
		assertEquals(17, ((MockDataObject1) edo).getFoo());
		assertEquals(ownerid, ((MockDataObject1) edo).getOwnerId());
	}

	@Test
	public void testReadDataUUIDDataNotFound() {
		buildAndPopulateMDO1Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		ElisDataObject edo = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-123466667779");
		
		factory.registerProvider(new MockDataObject1Provider());
		
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
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		ElisDataObject edo = null;
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667772");
		
		factory.registerProvider(new MockDataObject1Provider());
		
		try {
			edo = storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNull(edo);
	}

	@Test
	public void testReadDataUUIDEmptyCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo = (MockDataObject3) storage.readData(MDO3_3u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mdo);
		assertEquals(MDO3_COUNT, countBindingsInDB(mdo));
		assertEquals(0, mdo.getCollection().size());
	}

	@Test
	public void testReadDataUUIDCollectionOneObject() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo1 = null;
		MockDataObject2 mdo2 = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo1 = (MockDataObject3) storage.readData(MDO3_4u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mdo1);
		assertEquals(MDO3_COUNT, countBindingsInDB(mdo1));
		assertEquals(MDO2_COUNT, countBindingsInDB(mdo2));
		assertEquals(1, mdo1.getCollection().size());
		assertTrue(mdo1.getCollection().contains(mdo2));
	}

	@Test
	public void testReadDataUUIDCollectionTwoObjects() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo1 = null;
		MockDataObject2 mdo2 = null;
		MockDataObject2 mdo3 = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo1 = (MockDataObject3) storage.readData(MDO3_2u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_1u);
			mdo3 = (MockDataObject2) storage.readData(MDO2_3u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mdo1);
		assertEquals(MDO3_COUNT, countBindingsInDB(mdo1));
		assertEquals(2, mdo1.getCollection().size());
		assertTrue(mdo1.getCollection().contains(mdo2));
		assertTrue(mdo1.getCollection().contains(mdo3));
	}

	@Test
	public void testReadDataUUIDCollectionNonExistingObject() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo = (MockDataObject3) storage.readData(UUID.fromString("deadbeef-2222-3333-4444-5555deadbeef"));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNull(mdo);
	}

	@Test
	public void testReadDataEDO() {
		buildAndPopulateMDO1Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		ElisDataObject expected = new MockDataObject1();
		ElisDataObject actual = null;
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");

		factory.registerProvider(new MockDataObject1Provider());
		
		expected.setDataId(MDO1_3u);
		
		try {
			actual = storage.readData(expected);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(actual);
		assertTrue(actual instanceof MockDataObject1);
		assertEquals(MDO1_3u, ((MockDataObject1) actual).getDataId());
		assertEquals("Jezibaba", ((MockDataObject1) actual).getBar());
		assertEquals(17, ((MockDataObject1) actual).getFoo());
		assertEquals(ownerid, ((MockDataObject1) actual).getOwnerId());
	}

	@Test
	public void testReadDataEDODataNotFound() {
		buildAndPopulateMDO1Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		ElisDataObject expected = new MockDataObject1();
		ElisDataObject actual = null;
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-123466667779");

		factory.registerProvider(new MockDataObject1Provider());
		
		expected.setDataId(uuid);
		
		try {
			actual = storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNull(actual);
	}

	@Test
	public void testReadDataEDOIsUser() {
		buildAndPopulateMDO1Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		UserFactory userFactory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, userFactory, factory);
		ElisDataObject expected = new MockDataObject1();
		ElisDataObject actual = null;
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667772");

		factory.registerProvider(new MockDataObject1Provider());
		
		expected.setDataId(uuid);
		
		try {
			actual = storage.readData(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNull(actual);
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
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			au = storage.readUser(MDO1_2u);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
		
		assertNull(au);
	}

	@Test
	public void testReadUserUUIDEmptyCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mu);
		assertEquals(MU4_COUNT, countBindingsInDB(mu));
		assertEquals(0, mu.getCollection().size());
	}

	@Test
	public void testReadUserUUIDCollectionOneObject() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		MockDataObject2 mdo = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_3u);
			mdo = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mu);
		assertEquals(MU4_COUNT, countBindingsInDB(mu));
		assertEquals(1, mu.getCollection().size());
		assertTrue(mu.getCollection().contains(mdo));
	}

	@Test
	public void testReadUserUUIDCollectionTwoObjects() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_1u);
			mdo1 = (MockDataObject2) storage.readData(MDO2_1u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mu);
		assertEquals(MU4_COUNT, countBindingsInDB(mu));
		assertEquals(2, mu.getCollection().size());
		assertTrue(mu.getCollection().contains(mdo1));
		assertTrue(mu.getCollection().contains(mdo2));
	}

	@Test
	public void testReadUserUUIDCollectionNonExistingObject() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readData(UUID.fromString("deadbeef-2222-3333-4444-5555deadbeef"));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNull(mu);
	}

	@Test
	public void testReadUserAbstractUser() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		UUID uuid = MU1_1u;
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
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
		AbstractUser au = new MockUser1();
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			au = storage.readUser(au);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testReadUserAbstractUserDoesntExist() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566660000");
		MockUser1 mu = new MockUser1();
		AbstractUser au = null;
		
		factory.registerProvider(new MockUser1Provider());
		
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
		
		mu1.setId(13);
		mu1.setUsername("man");
		mu1.setPassword("Secret");
		mu2.setId(17);
		mu2.setUsername("mandibles");
		mu2.setPassword("Arthropod");
		
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
		props = users[0].getProperties();
		assertEquals("Batman", props.get("username"));
		assertEquals(21, ((MockUser1) users[0]).getWhatever());
		assertEquals("Rajec", ((MockUser1) users[0]).getStuff());
		
		// Superman
		props = users[1].getProperties();
		assertEquals("Superman", props.get("username"));
		assertEquals(22, ((MockUser1) users[1]).getWhatever());
		assertEquals("Vinea", ((MockUser1) users[1]).getStuff());
		
		// man
		props = users[2].getProperties();
		assertEquals("man", props.get("username"));
		assertEquals(1, ((MockUser1) users[2]).getWhatever());
		assertEquals("Voda", ((MockUser1) users[2]).getStuff());
		
		// mandibles
		props = users[3].getProperties();
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
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111112");
		
		factory.registerProvider(new MockUser1Provider());
		
		props.put("uuid", uuid);
		
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
		MockDataObject1 mdo = new MockDataObject1();
		mdo.setDataId(MDO1_3u);
		
		try {
			storage.delete(mdo);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(MDO1_COUNT - 1, countBindingsInDB(mdo));
		assertEquals(MDO1_COUNT - 1, countBindingsInDB());
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
		
		assertEquals(MDO1_COUNT, countBindingsInDB(mdo));
		assertEquals(MDO1_COUNT, countBindingsInDB());
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
		
		assertEquals(MDO1_COUNT, countBindingsInDB(mdo));
		assertEquals(MDO1_COUNT, countBindingsInDB());
	}

	@Test
	public void testDeleteElisDataObjectEmptyCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo = (MockDataObject3) storage.readData(MDO3_3u);
			storage.delete(mdo);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(MDO3_COUNT - 1, countBindingsInDB(mdo));
		assertEquals(0, countObjectsInCollection(MDO3_3u));
	}

	@Test
	public void testDeleteElisDataObjectIsInCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject2 mdo = null;
		
		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo = (MockDataObject2) storage.readData(MDO2_1u);
			storage.delete(mdo);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(MDO2_COUNT - 1, countBindingsInDB(mdo));
		assertEquals(1, countObjectsInCollection(MDO3_2u));
	}

	@Test
	public void testDeleteElisDataObjectCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = null;
		
		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo = (MockDataObject3) storage.readData(MDO3_2u);
			storage.delete(mdo);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(MDO3_COUNT - 1, countBindingsInDB(mdo));
		assertEquals(0, countObjectsInCollection(MDO3_2u));
	}

	@Test
	public void testDeleteElisDataObjectCollectionShouldntDeleteObjects() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = null;
		
		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo = (MockDataObject3) storage.readData(MDO3_2u);
			storage.delete(mdo);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertEquals(MDO3_COUNT - 1, countBindingsInDB(mdo));
		assertEquals(MDO2_COUNT, countBindingsInDB(new MockDataObject2()));
		assertEquals(0, countObjectsInCollection(MDO3_2u));
	}

	@Test
	public void testDeleteElisDataObjectArray() {
		buildAndPopulateMDO1Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject1 mdo2 = new MockDataObject1();

		mdo1.setDataId(MDO1_3u);
		mdo2.setDataId(MDO1_5u);
		
		edos[0] = mdo1;
		edos[1] = mdo2;
		
		try {
			storage.delete(edos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(MDO1_COUNT - 2, countBindingsInDB(mdo1));
		assertEquals(MDO1_COUNT - 2, countBindingsInDB());
	}

	@Test
	public void testDeleteElisDataObjectArrayDifferentObjectTypes() {
		buildAndPopulateMDO1Table();
		buildAndPopulateMDO2Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject2 mdo2 = new MockDataObject2();

		mdo1.setDataId(MDO1_3u);
		mdo2.setDataId(MDO2_2u);
		
		edos[0] = mdo1;
		edos[1] = mdo2;
		
		try {
			storage.delete(edos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(MDO1_COUNT - 1, countBindingsInDB(mdo1));
		assertEquals(MDO2_COUNT - 1, countBindingsInDB(mdo2));
		assertEquals(MDO1_COUNT + MDO2_COUNT - 2, countBindingsInDB());
	}

	@Test
	public void testDeleteElisDataObjectArrayOneObjectDoesntExist() {
		buildAndPopulateMDO1Table();
		buildAndPopulateMDO2Table();
		
		Storage storage = new StorageImpl(connection);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = new MockDataObject1();
		MockDataObject2 mdo2 = new MockDataObject2();

		mdo1.setDataId(MDO1_3u);
		mdo2.setDataId(UUID.fromString("00001111-2222-3333-4444-555566660000"));
		
		edos[0] = mdo1;
		edos[1] = mdo2;
		
		try {
			storage.delete(edos);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(MDO1_COUNT - 1, countBindingsInDB(mdo1));
		assertEquals(MDO2_COUNT, countBindingsInDB(mdo2));
		assertEquals(MDO1_COUNT + MDO2_COUNT - 1, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserPlatformUser() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Superman", "Louis Lane");
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111112");
		
		pu.setUserId(uuid);
		
		try {
			storage.delete(pu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT - 1, countBindingsInDB(pu));
		assertEquals(PU_COUNT - 1, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserPlatformUserDoesntExist() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser pu = new MockPlatformUser("Superman", "Louis Lane");
		UUID uuid = UUID.fromString("00000000-1111-1111-1111-111111111111");
		
		pu.setUserId(uuid);
		
		try {
			storage.delete(pu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT, countBindingsInDB(pu));
		assertEquals(PU_COUNT, countBindingsInDB());
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
		
		assertEquals(MU1_COUNT - 1, countBindingsInDB(mu));
		assertEquals(MU1_COUNT - 1, countBindingsInDB());
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
		
		assertEquals(MU1_COUNT, countBindingsInDB(mu));
		assertEquals(MU1_COUNT, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserUserIsInCollection() {
		buildAndPopulateMU4Table();
		buildAndPopulateMDO2Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = new MockUser4();
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		mu.setUserId(MU4_1u);
		
		try {
			storage.delete(mu);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(MU4_COUNT - 1, countBindingsInDB(mu));
		assertEquals(MU4_COUNT + MDO2_COUNT - 1, countBindingsInDB());
		assertEquals(0, countObjectsInCollection(MU4_1u));
	}

	@Test
	public void testDeleteAbstractUserEmptyCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_2u);
			storage.delete(mu);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(MU4_COUNT - 1, countBindingsInDB(mu));
		assertEquals(0, countObjectsInCollection(MU4_2u));
	}

	@Test
	public void testDeleteAbstractUserCollectionWithObjects() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_1u);
			storage.delete(mu);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(MU4_COUNT - 1, countBindingsInDB(mu));
		assertEquals(0, countObjectsInCollection(MU4_1u));
	}

	@Test
	public void testDeleteAbstractUserCollectionShouldntDeleteObjects() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_1u);
			storage.delete(mu);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(MU4_COUNT - 1, countBindingsInDB(mu));
		assertEquals(0, countObjectsInCollection(MU4_1u));
		assertEquals(MDO2_COUNT, countBindingsInDB(new MockDataObject2()));
	}

	@Test
	public void testDeleteAbstractUserArrayPlatformUsers() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser[] pus = new PlatformUser[2];
		PlatformUser pu1 = new MockPlatformUser("Superman", "Louis Lane");
		PlatformUser pu2 = new MockPlatformUser("Batman", "Robin");
		UUID uuid1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID uuid2 = UUID.fromString("11111111-1111-1111-1111-111111111112");
		
		pu1.setUserId(uuid2);
		pu2.setUserId(uuid1);
		
		pus[0] = pu1;
		pus[1] = pu2;
		
		try {
			storage.delete(pus);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT - 2, countBindingsInDB(pu1));
		assertEquals(PU_COUNT - 2, countBindingsInDB());
	}

	@Test
	public void testDeleteAbstractUserArrayPlatformUsersOneUserDoesntExist() {
		populatePUTable();
		
		Storage storage = new StorageImpl(connection);
		PlatformUser[] pus = new PlatformUser[2];
		PlatformUser pu1 = new MockPlatformUser("Superman", "Louis Lane");
		PlatformUser pu2 = new MockPlatformUser("Batman", "Robin");
		UUID uuid1 = UUID.fromString("deadbeef-1111-1111-1111-111111111111");
		UUID uuid2 = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		pu1.setUserId(uuid1);
		pu2.setUserId(uuid2);
		
		pus[0] = pu1;
		pus[1] = pu2;
		
		try {
			storage.delete(pus);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(PU_COUNT - 1, countBindingsInDB(pu1));
		assertEquals(PU_COUNT - 1, countBindingsInDB());
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
		
		assertEquals(MU1_COUNT - 2, countBindingsInDB(mu1));
		assertEquals(MU1_COUNT - 2, countBindingsInDB());
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
		
		assertEquals(MU1_COUNT - 1, countBindingsInDB(mu1));
		assertEquals(MU1_COUNT - 1, countBindingsInDB());
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
		
		assertEquals(MU1_COUNT - 1, countBindingsInDB(mu1));
		assertEquals(MU1_COUNT - 1, countBindingsInDB());
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
		
		assertEquals(MU1_COUNT - 1, countBindingsInDB(mu1));
		assertEquals(MU2_COUNT - 1, countBindingsInDB(mu2));
		assertEquals(MU1_COUNT + MU2_COUNT - 2, countBindingsInDB());
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
		
		assertEquals(MU1_COUNT, countBindingsInDB(new MockUser1()));
		assertEquals(MU2_COUNT, countBindingsInDB(new MockUser2()));
		assertEquals(MU1_COUNT + MU2_COUNT, countBindingsInDB());
	}

	@Test
	public void testUpdateElisDataObject() {
		buildAndPopulateMDO1Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject1 mdo = null;
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");

		factory.registerProvider(new MockDataObject1Provider());
		
		try {
			mdo = (MockDataObject1) storage.readData(MDO1_3u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		mdo.setFoo(5);
		mdo.setBar("Veles");
		
		try {
			storage.update(mdo);
			mdo = null;
			mdo = (MockDataObject1) storage.readData(MDO1_3u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mdo);
		assertEquals(MDO1_3u, mdo.getDataId());
		assertEquals("Veles", mdo.getBar());
		assertEquals(5, mdo.getFoo());
		assertEquals(ownerid, mdo.getOwnerId());
	}

	@Test
	public void testUpdateElisDataObjectNoId() {
		buildAndPopulateMDO1Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject1 mdo = null;
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");

		factory.registerProvider(new MockDataObject1Provider());
		
		try {
			mdo = (MockDataObject1) storage.readData(MDO1_3u);
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
		} catch (StorageException | IllegalArgumentException e) {
		}
		
		try {
			mdo = (MockDataObject1) storage.readData(MDO1_3u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mdo);
		assertEquals(MDO1_3u, mdo.getDataId());
		assertEquals("Jezibaba", mdo.getBar());
		assertEquals(17, mdo.getFoo());
		assertEquals(ownerid, mdo.getOwnerId());
	}

	@Test
	public void testUpdateElisDataObjectNonExistingId() {
		buildAndPopulateMDO1Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject1 mdo = null;
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");

		factory.registerProvider(new MockDataObject1Provider());
		
		try {
			mdo = (MockDataObject1) storage.readData(MDO1_3u);
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
			mdo = (MockDataObject1) storage.readData(MDO1_3u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mdo);
		assertEquals(MDO1_3u, mdo.getDataId());
		assertEquals("Jezibaba", mdo.getBar());
		assertEquals(17, mdo.getFoo());
		assertEquals(ownerid, mdo.getOwnerId());
	}

	@Test
	public void testUpdateElisDataObjectCollectionNoChanges() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo = (MockDataObject3) storage.readData(MDO3_1u);
			storage.update(mdo);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mdo);
		assertEquals(MDO3_COUNT, countBindingsInDB(mdo));
		assertEquals(3, countObjectsInCollection(mdo.getDataId()));
		assertEquals(3, mdo.getCollection().size());
	}

	@Test
	public void testUpdateElisDataObjectEmptiedCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo = (MockDataObject3) storage.readData(MDO3_1u);
			mdo.getCollection().clear();
			storage.update(mdo);
			mdo = (MockDataObject3) storage.readData(MDO3_1u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mdo);
		assertEquals(MDO3_COUNT, countBindingsInDB(mdo));
		assertEquals(0, countObjectsInCollection(mdo.getDataId()));
		assertEquals(0, mdo.getCollection().size());
	}

	@Test
	public void testUpdateElisDataObjectCollectionObjectChanged() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo1 = null;
		MockDataObject2 mdo2 = null;
		MockDataObject2 mdo3 = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo1 = (MockDataObject3) storage.readData(MDO3_1u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		mdo2.setBaz(-4);
		
		try {
			storage.update(mdo1);
			mdo3 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mdo1);
		assertEquals(MDO3_COUNT, countBindingsInDB(mdo1));
		assertEquals(3, countObjectsInCollection(mdo1.getDataId()));
		assertEquals(3, mdo1.getCollection().size());
		assertEquals(mdo2.getDataId(), mdo3.getDataId());
	}

	@Test
	public void testUpdateElisDataObjectCollectionObjectRemovedFromCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo1 = null;
		MockDataObject2 mdo2 = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo1 = (MockDataObject3) storage.readData(MDO3_1u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		mdo1.getCollection().remove(mdo2);
		
		try {
			storage.update(mdo1);
			mdo1 = (MockDataObject3) storage.readData(MDO3_1u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mdo1);
		assertEquals(MDO3_COUNT, countBindingsInDB(mdo1));
		assertEquals(2, countObjectsInCollection(MDO3_1u));
		assertEquals(2, mdo1.getCollection().size());
	}

	@Test
	public void testUpdateElisDataObjectCollectionObjectAdded() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo1 = null;
		MockDataObject2 mdo2 = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo1 = (MockDataObject3) storage.readData(MDO3_2u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		mdo1.getCollection().add(mdo2);
		
		try {
			storage.update(mdo1);
			mdo1 = (MockDataObject3) storage.readData(MDO3_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mdo1);
		assertEquals(MDO3_COUNT, countBindingsInDB(mdo1));
		assertEquals(3, countObjectsInCollection(MDO3_2u));
		assertEquals(3, mdo1.getCollection().size());
	}

	@Test
	public void testUpdateElisDataObjectCollectionOneObjectDoesntExist() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMDO3Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockDataObject3 mdo1 = null;
		MockDataObject2 mdo2 = new MockDataObject2(null, MU1_1u, 42);
		MockDataObject2 mdo3 = null;

		factory.registerProvider(new MockDataObject2Provider());
		factory.registerProvider(new MockDataObject3Provider());
		
		try {
			mdo1 = (MockDataObject3) storage.readData(MDO3_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		mdo1.getCollection().add(mdo2);
		
		try {
			storage.update(mdo1);
			mdo1 = (MockDataObject3) storage.readData(MDO3_2u);
			mdo3 = (MockDataObject2) storage.readData(mdo2.getDataId());
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mdo1);
		assertEquals(MDO2_COUNT + 1, countBindingsInDB(mdo2));
		assertEquals(MDO3_COUNT, countBindingsInDB(mdo1));
		assertEquals(3, countObjectsInCollection(MDO3_2u));
		assertEquals(3, mdo1.getCollection().size());
		assertEquals(mdo2.getDataId(), mdo3.getDataId());
	}

	@Test
	public void testUpdateElisDataObjectArray() {
		buildAndPopulateMDO1Table();
	
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = null;
		MockDataObject1 mdo2 = null;
		UUID ownerid1 = UUID.fromString("00001111-2222-dead-beef-555566667771");
		UUID ownerid2 = UUID.fromString("00001111-2222-dead-beef-555566667772");

		factory.registerProvider(new MockDataObject1Provider());
	
		try {
			mdo1 = (MockDataObject1) storage.readData(MDO1_3u);
			mdo2 = (MockDataObject1) storage.readData(MDO1_4u);
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
			mdo1 = (MockDataObject1) storage.readData(MDO1_3u);
			mdo2 = (MockDataObject1) storage.readData(MDO1_4u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
	
		assertNotNull(mdo1);
		assertEquals(MDO1_3u, mdo1.getDataId());
		assertEquals("Veles", mdo1.getBar());
		assertEquals(5, mdo1.getFoo());
		assertEquals(ownerid1, mdo1.getOwnerId());
	
		assertNotNull(mdo2);
		assertEquals(MDO1_4u, mdo2.getDataId());
		assertEquals("Perun", mdo2.getBar());
		assertEquals(7, mdo2.getFoo());
		assertEquals(ownerid2, mdo2.getOwnerId());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testUpdateElisDataObjectArrayDifferentObjectTypes() {
		buildAndPopulateMDO1Table();
		buildAndPopulateMDO2Table();
		
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = null;
		MockDataObject2 mdo2 = null;
		UUID ownerid = UUID.fromString("00001111-2222-dead-beef-555566667771");

		factory.registerProvider(new MockDataObject1Provider());
		factory.registerProvider(new MockDataObject2Provider());
		
		try {
			mdo1 = (MockDataObject1) storage.readData(MDO1_3u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
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
			mdo1 = (MockDataObject1) storage.readData(MDO1_3u);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(mdo1);
		assertEquals(MDO1_3u, mdo1.getDataId());
		assertEquals("Veles", mdo1.getBar());
		assertEquals(5, mdo1.getFoo());
		assertEquals(ownerid, mdo1.getOwnerId());
		
		assertNotNull(mdo2);
		assertEquals(MDO2_2u, mdo2.getDataId());
		assertEquals(7.1, mdo2.getBaz(), 0.000001);
		assertEquals(ownerid, mdo2.getOwnerId());
	}

	@Test
	public void testUpdateElisDataObjectArrayOneOfTheObjectsLacksId() {
		buildAndPopulateMDO1Table();
	
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		ElisDataObject[] edos = new ElisDataObject[2];
		MockDataObject1 mdo1 = null;
		MockDataObject1 mdo2 = null;
		UUID ownerid1 = UUID.fromString("00001111-2222-dead-beef-555566667771");
		UUID ownerid2 = UUID.fromString("00001111-2222-dead-beef-555566667772");

		factory.registerProvider(new MockDataObject1Provider());
	
		try {
			mdo1 = (MockDataObject1) storage.readData(MDO1_3u);
			mdo2 = (MockDataObject1) storage.readData(MDO1_4u);
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
		} catch (StorageException | IllegalArgumentException e) {
		}
		
		mdo1 = null;
		mdo2 = null;
		try {
			mdo1 = (MockDataObject1) storage.readData(MDO1_3u);
			mdo2 = (MockDataObject1) storage.readData(MDO1_4u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
	
		assertNotNull(mdo1);
		assertEquals(MDO1_3u, mdo1.getDataId());
		assertEquals("Veles", mdo1.getBar());
		assertEquals(5, mdo1.getFoo());
		assertEquals(ownerid1, mdo1.getOwnerId());
	
		assertNotNull(mdo2);
		assertEquals(MDO1_4u, mdo2.getDataId());
		assertEquals("Domovoj", mdo2.getBar());
		assertEquals(17, mdo2.getFoo());
		assertEquals(ownerid2, mdo2.getOwnerId());
	}

	@Test
	public void testUpdateElisDataObjectArrayEmptyArray() {
		buildAndPopulateMDO1Table();
	
		DataObjectFactory factory = new DataObjectFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		ElisDataObject[] edos = new ElisDataObject[2];

		factory.registerProvider(new MockDataObject1Provider());
	
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
		PlatformUser pu = null;
		String password = null;
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		password = pu.getPassword();
		
		pu.setUsername("George");
		pu.setPassword("Horse");
		pu.setFirstName("George");
		pu.setLastName("Ofthejungle");
		pu.setEmail("george@djungle.za");
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(uuid, pu.getUserId());
		assertEquals("George", pu.getUsername());
		assertEquals("George", ((PlatformUser) pu).getFirstName());
		assertEquals("Ofthejungle", ((PlatformUser) pu).getLastName());
		assertEquals("george@djungle.za", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoId() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUser pu = null;
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setUserId(null);
		
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
		PlatformUser pu = null;
		String password = null;
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		password = pu.getPassword();
		
		pu.setUsername("George");
		pu.setFirstName("George");
		pu.setLastName("Ofthejungle");
		pu.setEmail("george@djungle.za");
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(uuid, pu.getUserId());
		assertEquals("George", pu.getUsername());
		assertNull(pu.getPassword());
		assertEquals("George", ((PlatformUser) pu).getFirstName());
		assertEquals("Ofthejungle", ((PlatformUser) pu).getLastName());
		assertEquals("george@djungle.za", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserEmptyPassword() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUser pu = null;
		String password = null;
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		password = pu.getPassword();
		
		pu.setUsername("George");
		pu.setFirstName("George");
		pu.setLastName("Ofthejungle");
		pu.setEmail("george@djungle.za");
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(uuid, pu.getUserId());
		assertEquals("George", pu.getUsername());
		assertNull(pu.getPassword());
		assertEquals("George", ((PlatformUser) pu).getFirstName());
		assertEquals("Ofthejungle", ((PlatformUser) pu).getLastName());
		assertEquals("george@djungle.za", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoFirstName() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUser pu = null;
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setFirstName(null);
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(uuid, pu.getUserId());
		assertEquals("Batman", pu.getUsername());
		assertEquals("", ((PlatformUser) pu).getFirstName());
		assertEquals("Wayne", ((PlatformUser) pu).getLastName());
		assertEquals("bruce@waynecorp.com", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserEmptyFirstName() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUser pu = null;
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setFirstName("");
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(uuid, pu.getUserId());
		assertEquals("Batman", pu.getUsername());
		assertEquals("", ((PlatformUser) pu).getFirstName());
		assertEquals("Wayne", ((PlatformUser) pu).getLastName());
		assertEquals("bruce@waynecorp.com", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoLastName() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUser pu = null;
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setLastName(null);
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(uuid, pu.getUserId());
		assertEquals("Batman", pu.getUsername());
		assertEquals("Bruce", ((PlatformUser) pu).getFirstName());
		assertEquals("", ((PlatformUser) pu).getLastName());
		assertEquals("bruce@waynecorp.com", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserEmptyLastName() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUser pu = null;
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setLastName("");
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(uuid, pu.getUserId());
		assertEquals("Batman", pu.getUsername());
		assertEquals("Bruce", ((PlatformUser) pu).getFirstName());
		assertEquals("", ((PlatformUser) pu).getLastName());
		assertEquals("bruce@waynecorp.com", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserNoEmail() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUser pu = null;
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setEmail(null);
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(uuid, pu.getUserId());
		assertEquals("Batman", pu.getUsername());
		assertEquals("Bruce", ((PlatformUser) pu).getFirstName());
		assertEquals("Wayne", ((PlatformUser) pu).getLastName());
		assertEquals("", ((PlatformUser) pu).getEmail());
	}

	@Test
	public void testUpdateAbstractUserPlatformUserEmptyEmail() {
		populatePUTable();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		PlatformUser pu = null;
		UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		pu.setEmail("");
		
		try {
			storage.update(pu);
			pu = null;
			pu = (PlatformUser) storage.readUser(uuid);
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertNotNull(pu);
		assertEquals(uuid, pu.getUserId());
		assertEquals("Batman", pu.getUsername());
		assertEquals("Bruce", ((PlatformUser) pu).getFirstName());
		assertEquals("Wayne", ((PlatformUser) pu).getLastName());
		assertEquals("", ((PlatformUser) pu).getEmail());
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
		} catch (StorageException | IllegalArgumentException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserEmpty() {
		buildAndPopulateMU1Table();
		
		UserFactory factory = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, factory);
		MockUser1 mu = new MockUser1(null, "", 0);
		
		factory.registerProvider(new MockUser1Provider());
		
		try {
			storage.update(mu);
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testUpdateAbstractUserCollectionNoChanges() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_3u);
			mdo1 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		try {
			storage.update(mu);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mu);
		assertEquals(MU4_COUNT, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT, countBindingsInDB(mdo1));
		assertEquals(1, mu.getCollection().size());
		assertTrue(mu.getCollection().contains(mdo1));
		assertEquals(mdo1, mdo2);
	}

	@Test
	public void testUpdateAbstractUserEmptiedCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_3u);
			mdo1 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		mu.getCollection().clear();
		
		try {
			storage.update(mu);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
			mu = (MockUser4) storage.readUser(MU4_3u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mu);
		assertNotNull(mdo2);
		assertEquals(MU4_COUNT, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT, countBindingsInDB(mdo1));
		assertEquals(0, countObjectsInCollection(MU4_3u));
		assertEquals(0, mu.getCollection().size());
	}

	@Test
	public void testUpdateAbstractUserCollectionObjectChanged() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_1u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		for (ElisDataObject e : mu.getCollection()) {
			if (e.getDataId().equals(MDO2_2u)) {
				mdo1 = (MockDataObject2) e;
				mdo1.setBaz(-2);
				break;
			}
		}
		
		try {
			storage.update(mu);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mu);
		assertEquals(MU4_COUNT, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT, countBindingsInDB(mdo1));
		assertEquals(2, countObjectsInCollection(MU4_1u));
		assertEquals(2, mu.getCollection().size());
		assertTrue(mu.getCollection().contains(mdo1));
		assertEquals(mdo1, mdo2);
	}

	@Test
	public void testUpdateAbstractUserCollectionObjectRemovedFromCollection() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		MockDataObject2 mdo1 = null;
		MockDataObject2 mdo2 = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_1u);
			mdo1 = (MockDataObject2) storage.readData(MDO2_2u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		mu.getCollection().remove(mdo1);
		
		try {
			storage.update(mu);
			mdo2 = (MockDataObject2) storage.readData(MDO2_2u);
			mu = (MockUser4) storage.readUser(MU4_1u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mu);
		assertEquals(MU4_COUNT, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT, countBindingsInDB(mdo1));
		assertEquals(1, countObjectsInCollection(MU4_1u));
		assertEquals(1, mu.getCollection().size());
		assertFalse(mu.getCollection().contains(mdo1));
		assertEquals(mdo1, mdo2);
	}

	@Test
	public void testUpdateAbstractUserCollectionObjectAdded() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		MockDataObject2 mdo1 = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_1u);
			mdo1 = (MockDataObject2) storage.readData(MDO2_3u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		mu.getCollection().add(mdo1);
		
		try {
			storage.update(mu);
			mu = (MockUser4) storage.readUser(MU4_1u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mu);
		assertEquals(MU4_COUNT, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT, countBindingsInDB(mdo1));
		assertEquals(3, countObjectsInCollection(MU4_1u));
		assertEquals(3, mu.getCollection().size());
		assertTrue(mu.getCollection().contains(mdo1));
	}

	@Test
	public void testUpdateAbstractUserCollectionOneObjectDoesntExist() {
		buildAndPopulateMDO2Table();
		buildAndPopulateMU4Table();
		
		DataObjectFactory dof = new DataObjectFactoryImpl();
		UserFactory uf = new UserFactoryImpl();
		Storage storage = new StorageImpl(connection, uf, dof);
		MockUser4 mu = null;
		MockDataObject2 mdo1 = new MockDataObject2();
		MockDataObject2 mdo2 = null;
		
		dof.registerProvider(new MockDataObject2Provider());
		uf.registerProvider(new MockUser4Provider());
		
		try {
			mu = (MockUser4) storage.readUser(MU4_1u);
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		mdo1.setBaz(-2);
		mdo1.setOwnerId(mu.getUserId());
		
		mu.getCollection().add(mdo1);
		
		try {
			storage.update(mu);
			mdo2 = (MockDataObject2) storage.readData(mdo1.getDataId());
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}

		assertNotNull(mu);
		assertEquals(MU4_COUNT, countBindingsInDB(mu));
		assertEquals(MDO2_COUNT + 1, countBindingsInDB(mdo1));
		assertEquals(3, countObjectsInCollection(MU4_1u));
		assertEquals(3, mu.getCollection().size());
		assertTrue(mu.getCollection().contains(mdo1));
		assertEquals(mdo1, mdo2);
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
		} catch (StorageException | IllegalArgumentException e) {
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
			fail("This shouldn't happen");
		} catch (StorageException e) {
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
