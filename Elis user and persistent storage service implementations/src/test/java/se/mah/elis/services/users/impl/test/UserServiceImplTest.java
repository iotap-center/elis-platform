package se.mah.elis.services.users.impl.test;

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

import se.mah.elis.impl.services.storage.StorageImpl;
import se.mah.elis.impl.services.users.PlatformUserIdentifierImpl;
import se.mah.elis.impl.services.users.PlatformUserImpl;
import se.mah.elis.impl.services.users.UserServiceImpl;
import se.mah.elis.impl.services.users.factory.UserFactoryImpl;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.exceptions.UserExistsException;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.impl.test.mock.AnotherMockUserProvider;
import se.mah.elis.services.users.factory.impl.test.mock.MockUserProvider;
import se.mah.elis.services.users.impl.test.mock.AnotherMockUser;
import se.mah.elis.services.users.impl.test.mock.MockUser;

public class UserServiceImplTest {
	
	private static int PU_COUNT = 3;
	
	private Connection connection;
	private Storage storage;

	@Before
	public void setUp() throws Exception {
		setUpDatabase();
		tearDownTables();
		populatePUTable();
		buildAndPopulateMUTable();
		UserFactory factory = new UserFactoryImpl();
		storage = new StorageImpl(connection, factory);
		factory.registerProvider(new MockUserProvider());
		factory.registerProvider(new AnotherMockUserProvider());
	}

	@After
	public void tearDown() throws Exception {
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
	
	private void tearDownTables() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("TRUNCATE TABLE object_lookup_table;");
			stmt.execute("TRUNCATE TABLE user_lookup_table;");
			stmt.execute("TRUNCATE TABLE `se-mah-elis-services-users-PlatformUser`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-services-users-impl-test-mock-MockUser`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-services-users-impl-test-mock-AnotherMockUser`;");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void buildAndPopulateMUTable() {
		String uuid1 = "000011112222deadbeef555566667771";
		String uuid2 = "000011112222deadbeef555566667772";
		String uuid3 = "000011112222deadbeef555566667773";
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE `se-mah-elis-services-users-impl-test-mock-MockUser` (" +
						"`uuid` BINARY(16) PRIMARY KEY, " +
						"`service_name` VARCHAR(9), " +
						"`id_number` INTEGER, " +
						"`username` VARCHAR(32), " +
						"`password` VARCHAR(32), " +
						"`stuff` VARCHAR(32), " +
						"`whatever` INTEGER, " +
						"`created` TIMESTAMP)");
			stmt.execute("INSERT INTO `se-mah-elis-services-users-impl-test-mock-MockUser` " +
					"VALUES (x'" + uuid1 +"', 'test', 1, 'Batman', 'Robin', 'Rajec', 21, '2000-01-01 00:00:00');");
			stmt.execute("INSERT INTO `se-mah-elis-services-users-impl-test-mock-MockUser` " +
					"VALUES (x'" + uuid2 +"', 'test', 1, 'Superman', 'Lois Lane', 'Vinea', 22, '2000-01-01 00:00:01');");
			stmt.execute("INSERT INTO `se-mah-elis-services-users-impl-test-mock-MockUser` " +
					"VALUES (x'" + uuid3 +"', 'test', 1, 'Spongebob Squarepants', 'Patrick Seastar', 'Kofola', 23, '2000-01-01 00:00:02');");

			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid1 +"', " +
					"'se-mah-elis-services-users-impl-test-mock-MockUser')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid2 +"', " +
					"'se-mah-elis-services-users-impl-test-mock-MockUser')");
			stmt.execute("INSERT INTO `object_lookup_table` VALUES (x'" + uuid3 +"', " +
					"'se-mah-elis-services-users-impl-test-mock-MockUser')");
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
	
	private int countPlatformUsers() {
		Statement statement;
		int bindings = -1;
		
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT count(*) FROM `se-mah-elis-services-users-PlatformUser`");
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
	
	private boolean findInArray(PlatformUser needle, PlatformUser[] haystack) {
		for (int i = 0; i < haystack.length; i++) {
			if (needle.toString().equals(haystack[i].toString())) {
				return true;
			}
		}
		
		return false;
	}

	@Test
	public void testGetUsers() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl();
		
		User[] users = us.getUsers(pu);
		
		assertNotNull(users);
		assertEquals(0, users.length);
	}
	
	@Test
	public void testGetUser() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl();
		UUID uuid = UUID.fromString("00001111-2222-dead-beef-555566667771");
		MockUser mu = new MockUser();
		
		mu.setUserId(uuid);
		mu.setStuff("Rajec");
		mu.setWhatever(21);
		
		MockUser user = (MockUser) us.getUser(pu, uuid);
		System.out.println(user);
		
		assertNotNull(user);
		assertEquals(mu.getIdentifier().toString(), user.getIdentifier().toString());
		assertEquals(mu.getUserId(), user.getUserId());
		assertEquals(mu.getStuff(), user.getStuff());
		assertEquals(mu.getWhatever(), user.getWhatever());
		assertTrue(user.created().isBefore(mu.created()));
	}
	
	@Test
	public void testGetUserMultipleUserTypes() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		User mu = new MockUser();
		
		mu.setUserId(uuid);
		
		try {
			us.registerUserToPlatformUser(mu, pu);
			us.registerUserToPlatformUser(new AnotherMockUser(), pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User user = us.getUser(pu, uuid);
		
		assertNotNull(user);
		assertEquals(user, mu);
	}
	
	@Test
	public void testGetUserMultipeUsersOfSameType() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		User mu = new MockUser();
		
		mu.setUserId(uuid);
		
		try {
			us.registerUserToPlatformUser(mu, pu);
			us.registerUserToPlatformUser(new MockUser(), pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User user = us.getUser(pu, uuid);
		
		assertNotNull(user);
		assertEquals(user, mu);
	}
	
	@Test
	public void testGetUserNoSuchUser() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		User mu = new MockUser();
		
		try {
			us.registerUserToPlatformUser(new AnotherMockUser(), pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User user = us.getUser(pu, uuid);
		
		assertNull(user);
	}
	
	@Test
	public void testGetUserNoSuchPlatformUser() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl(
				new PlatformUserIdentifierImpl(1, "a", "b"));
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		User mu = new MockUser();
		
		mu.setUserId(uuid);
		
		try {
			us.registerUserToPlatformUser(mu, pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User user = us.getUser(new PlatformUserImpl(), uuid);
		
		assertNull(user);
	}

	@Test
	public void testRegisterUserToPlatformUser() {
		UserService userService = new UserServiceImpl(storage, connection);
		PlatformUser platformUser = null;
		MockUser mockUser = new MockUser();
		
		mockUser.setStuff("foo");
		mockUser.setWhatever(1);
		
		try {
			platformUser = userService.createPlatformUser("Ada", "Lovelace");
			userService.registerUserToPlatformUser(mockUser, platformUser);
		} catch (NoSuchUserException | UserExistsException e) {
			fail("Register no workie");
		}
		
		User[] users = userService.getUsers(platformUser);
		
		assertNotNull(users);
		assertEquals(1, users.length);
		assertEquals("I'm a MockUserIndentifier", users[0].getIdentifier().toString());
		assertEquals(MockUser.MOCK_UUID, users[0].getUserId());
	}

	@Test
	public void testRegisterUserToPlatformUserMultiple() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu = null;
		MockUser mu = new MockUser();
		AnotherMockUser amu = new AnotherMockUser();
		
		try {
			pu = us.createPlatformUser("George", "Carlin");
			us.registerUserToPlatformUser(mu, pu);
			us.registerUserToPlatformUser(amu, pu);
		} catch (NoSuchUserException | UserExistsException e) {
			fail("Register no workie");
		}
		
		User[] users = us.getUsers(pu);
		
		assertNotNull(users);
		assertEquals(2, users.length);
		assertEquals("I'm a MockUserIndentifier", users[0].getIdentifier().toString());
		assertEquals("I'm an AnotherMockUserIndentifier", users[1].getIdentifier().toString());
	}

	@Test
	public void testUnregisterUserFromPlatformUser() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl();
		User mu = new MockUser();
		User amu = new AnotherMockUser();
		
		try {
			us.registerUserToPlatformUser(mu, pu);
			us.registerUserToPlatformUser(amu, pu);
			us.unregisterUserFromPlatformUser(mu, pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User[] users = us.getUsers(pu);
		
		assertNotNull(users);
		assertEquals(1, users.length);
		assertEquals("I'm an AnotherMockUserIndentifier", users[0].getIdentifier().toString());
		assertEquals(AnotherMockUser.MOCK_UUID, users[0].getUserId());
	}

	@Test
	public void testUnregisterAllUsersFromPlatformUser() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl();
		User mu = new MockUser();
		User amu = new AnotherMockUser();
		
		try {
			us.registerUserToPlatformUser(mu, pu);
			us.registerUserToPlatformUser(amu, pu);
			us.unregisterUserFromPlatformUser(mu, pu);
			us.unregisterUserFromPlatformUser(amu, pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User[] users = us.getUsers(pu);
		
		assertNotNull(users);
		assertEquals(0, users.length);
	}

	@Test
	public void testGetPlatformUser() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl();
		PlatformUser actual = null;
		
		try {
			pu = us.createPlatformUser("Fred", "Barney");
		} catch (UserExistsException e) {}

		assertEquals("PlatformUser Fred (4)", pu.toString());
		assertEquals(PU_COUNT + 1, countPlatformUsers());
		
		actual = us.getPlatformUser(pu.getIdentifier());
		
		assertEquals(actual, pu);
	}

	@Test
	public void testGetPlatformUserMultipleUsers() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		PlatformUser actual = null;
		
		try {
			pu1 = us.createPlatformUser("Fred", "Barney");
			pu2 = us.createPlatformUser("Modesty", "Blaise");
		} catch (UserExistsException e) {}

		assertEquals(PU_COUNT + 2, countPlatformUsers());
		
		actual = us.getPlatformUser(pu2.getIdentifier());
		
		assertEquals(actual, pu2);
	}

	@Test
	public void testGetPlatformUserWithString() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		PlatformUser actual = null;
		
		try {
			pu1 = us.createPlatformUser("Fred", "Barney");
			pu2 = us.createPlatformUser("Modesty", "Blaise");
		} catch (UserExistsException e) {}

		assertEquals(PU_COUNT + 2, countPlatformUsers());
		
		actual = us.getPlatformUser("5");
		
		assertNotNull(actual);
		assertEquals(pu2, actual);
	}

	@Test
	public void testGetPlatformUserNotFound() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		PlatformUser actual = null;
		
		try {
			pu1 = us.createPlatformUser("Fred", "Barney");
			pu2 = us.createPlatformUser("Modesty", "Blaise");
		} catch (UserExistsException e) {}

		assertEquals(PU_COUNT + 2, countPlatformUsers());
		
		actual = us.getPlatformUser(new PlatformUserIdentifierImpl(3, "Arthur", "Douglas"));
		
		assertNull(actual);
	}

	@Test
	public void testCreatePlatformUser() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl();
		try {
			pu = us.createPlatformUser("Fred", "Barney");
		} catch (UserExistsException e) {}

		assertEquals("PlatformUser Fred (4)", pu.toString());
		assertEquals(PU_COUNT + 1, countPlatformUsers());
	}

	@Test
	public void testCreatePlatformUserBadData() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu = new PlatformUserImpl();
		try {
			pu = us.createPlatformUser("", "Barney");
			fail("IllegalArgumentException should've been raised");
		} catch (UserExistsException e) {
			fail("IllegalArgumentException should've been raised");
		} catch (IllegalArgumentException e) {}
		
		assertEquals(PU_COUNT, countPlatformUsers());
	}

	@Test
	public void testCreatePlatformUserExistingUser() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu1;
		
		try {
			pu1 = us.createPlatformUser("Fred", "Barney");
		} catch (UserExistsException e) {
			fail("Adding a platform user should work");
		}
		
		try {
			PlatformUser pu2 = us.createPlatformUser("Fred", "Barney");
			fail("Readding an existing user shouldn't work.");
		} catch (UserExistsException e) {}
		
		assertEquals(PU_COUNT + 1, countPlatformUsers());
	}

	@Test
	public void testCreatePlatformUserTwoUsers() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		try {
			pu1 = us.createPlatformUser("Fred", "Barney");
			pu2 = us.createPlatformUser("Bilbo", "Baggins");
		} catch (UserExistsException e) {}

		assertEquals("PlatformUser Fred (4)", pu1.toString());
		assertEquals("PlatformUser Bilbo (5)", pu2.toString());
		assertEquals(PU_COUNT + 2, countPlatformUsers());
	}

	@Test
	public void testGetPlatformUsersAssociatedWithUser() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu = null;
		User mu = null;
		
		try {
			pu = us.createPlatformUser("Fred", "Barney");
			mu = storage.readUser(UUID.fromString("00001111-2222-dead-beef-555566667771"));
			System.out.println(mu.getClass().getName());
		} catch (UserExistsException | StorageException e1) {}
		
		try {
			us.registerUserToPlatformUser(mu, pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		PlatformUser[] pus = null;
		try {
			pus = us.getPlatformUsersAssociatedWithUser(mu);
		} catch (NoSuchUserException e) {
			fail("No workie");
		}
		
		assertNotNull(pus);
		assertEquals(1, pus.length);
		assertEquals("1: Fred, Barney", pus[0].getIdentifier().toString());
	}

	@Test
	public void testGetPlatformUsersAssociatedWithUserTwoPlatformUsers() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl("a", "b"));
		PlatformUser pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl("1", "2"));
		User mu = new MockUser();
		
		try {
			us.registerUserToPlatformUser(mu, pu1);
			us.registerUserToPlatformUser(mu, pu2);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		PlatformUser[] pus = null;
		try {
			pus = us.getPlatformUsersAssociatedWithUser(mu);
		} catch (NoSuchUserException e) {
			fail("No workie");
		}
		
		assertNotNull(pus);
		assertEquals(2, pus.length);
		assertTrue(findInArray(pu1,  pus));
		assertTrue(findInArray(pu2,  pus));
	}

	@Test
	public void testGetPlatformUsersAssociatedWithUserNonExistingUser() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl("a", "b"));
		PlatformUser pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl("1", "2"));
		User mu1 = new MockUser();
		User mu2 = new AnotherMockUser();
		
		try {
			us.registerUserToPlatformUser(mu1, pu1);
			us.registerUserToPlatformUser(mu1, pu2);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		PlatformUser[] pus = null;
		try {
			pus = us.getPlatformUsersAssociatedWithUser(mu2);
		} catch (NoSuchUserException e) {
			fail("No workie");
		}
		
		assertNotNull(pus);
		assertEquals(0, pus.length);
	}

	@Test
	public void testGetPlatformUsersAssociatedWithUserNToMCase() {
		UserService us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl("a", "b"));
		PlatformUser pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl("1", "2"));
		User mu1 = new MockUser();
		User mu2 = new AnotherMockUser();
		
		try {
			us.registerUserToPlatformUser(mu1, pu1);
			us.registerUserToPlatformUser(mu2, pu1);
			us.registerUserToPlatformUser(mu2, pu2);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		PlatformUser[] pus = null;
		try {
			pus = us.getPlatformUsersAssociatedWithUser(mu1);
		} catch (NoSuchUserException e) {
			fail("No workie");
		}
		
		assertNotNull(pus);
		assertEquals(1, pus.length);
		assertEquals("1: a, b", pus[0].getIdentifier().toString());
	}
	
	@Test
	public void testGetPlatformUsers() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = null, pu2 = null, pu3 = null;
		
		try {
			pu1 = us.createPlatformUser("Fred", "Barney");
			pu2 = us.createPlatformUser("Bilbo", "Baggins");
			pu3 = us.createPlatformUser("Orvar", "Säfström");
		} catch (UserExistsException e) {}
		PlatformUser[] pus = us.getPlatformUsers();
		assertNotNull(pus);
		assertEquals(PU_COUNT + 3, pus.length);
		assertTrue(findInArray(pu1, pus));
		assertTrue(findInArray(pu2, pus));
		assertTrue(findInArray(pu3, pus));
	}
	
	@Test
	public void testGetPlatformUsersNoUsers() {
		runQuery("TRUNCATE TABLE `se-mah-elis-services-users-PlatformUser`;");
		
		UserServiceImpl us = new UserServiceImpl(storage, connection);

		PlatformUser[] pus = us.getPlatformUsers();
		assertNotNull(pus);
		assertEquals(0, pus.length);
	}
	
	@Test
	public void testGetPlatformUsersOneUser() {
		runQuery("TRUNCATE TABLE `se-mah-elis-services-users-PlatformUser`;");
		
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		
		try {
			us.createPlatformUser("Fred", "Barney");
		} catch (UserExistsException e) {}

		PlatformUser[] pus = us.getPlatformUsers();
		assertNotNull(pus);
		assertEquals(1, pus.length);
	}
	
	@Test
	public void testUpdatePlatformUser() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu = null;
		
		try {
			pu = us.createPlatformUser("Fred", "Barney");
		} catch (UserExistsException e1) {}
		
		assertEquals("PlatformUser Fred (4)", pu.toString());
		assertEquals(PU_COUNT + 1, countPlatformUsers());

		pu.setFirstName("Fred");
		pu.setLastName("Flintstone");
		
		try {
			us.updatePlatformUser(pu);
			pu = null;
			pu = us.getPlatformUser(new PlatformUserIdentifierImpl("Fred", "Barney"));
		} catch (NoSuchUserException e) {
			fail("Bad stuff going on");
		}
		
		assertEquals("Fred", pu.getFirstName());
		assertEquals("Flintstone", pu.getLastName());
	}
	
	@Test
	public void testUpdatePlatformUserNonExistingUser() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		
		try {
			pu1 = us.createPlatformUser("Fred", "Barney");
			pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl("1", "a"));
		} catch (UserExistsException e1) {}
		
		
		assertEquals("PlatformUser Fred (4)", pu1.toString());
		assertEquals(PU_COUNT + 1, countPlatformUsers());

		pu2.setFirstName("Bruce");
		pu2.setLastName("Wayne");
		
		try {
			us.updatePlatformUser(pu2);
			fail("No exception rose");
		} catch (NoSuchUserException e) {}
	}
	
	@Test
	public void testDeletePlatformUser() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		try {
			pu1 = us.createPlatformUser("Fred", "Barney");
			pu2 = us.createPlatformUser("Bilbo", "Baggins");
		} catch (UserExistsException e) {}

		try {
			us.deletePlatformUser(pu1);
		} catch (NoSuchUserException e) {
			fail("Didn't find the user");
		}
		
		PlatformUser pu3 = us.getPlatformUser(pu1.getIdentifier());
		
		assertNull(pu3);
		assertEquals(PU_COUNT + 1, countPlatformUsers());
	}
	
	@Test
	public void testDeletePlatformUserNoSuchUser() {
		UserServiceImpl us = new UserServiceImpl(storage, connection);
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		
		try {
			pu1 = us.createPlatformUser("Fred", "Barney");
			pu2 = us.createPlatformUser("Bilbo", "Baggins");
		} catch (UserExistsException e) {}

		try {
			us.deletePlatformUser(new PlatformUserImpl(new PlatformUserIdentifierImpl(3, "george", "tarzan")));
		} catch (NoSuchUserException e) {
		}
		
		PlatformUser pu3 = us.getPlatformUser(pu1.getIdentifier());
		
		assertNotNull(pu3);
		assertEquals(PU_COUNT + 2, countPlatformUsers());
	}
}
