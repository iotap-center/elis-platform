package se.mah.elis.external.energy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.energy.eon.internal.user.EonUserProvider;
import se.mah.elis.adaptor.energy.eon.internal.user.EonUserRecipe;
import se.mah.elis.impl.services.storage.StorageImpl;
import se.mah.elis.impl.services.users.UserServiceImpl;
import se.mah.elis.impl.services.users.factory.UserFactoryImpl;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.NoSuchUserException;

public class EnergyServiceIntegrationDbTest extends JerseyTest {

	private static final String PLATFORMPASSWORD = "password";
	private static final String PLATFORMUSERNAME = "username";
	private static Connection connection;
	private static UserFactoryImpl factory;
	private static StorageImpl storage;
	private static UserServiceImpl userService;
	private static LogService log;
	private PlatformUser platformUser;

	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new EnergyService(userService, log));
		return config;
	}
	
	@BeforeClass
	public static void setupClass() {
		setUpDatabase();
		factory = new UserFactoryImpl();
		storage = new StorageImpl(connection, factory);
		userService = new UserServiceImpl(storage, connection);
		log = mock(LogService.class);
		factory.registerProvider(new EonUserProvider());
	}
	
	private static void setUpDatabase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost/elis_test?"
							+ "user=elis_test&password=elis_test");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void tearDownClass() {
		tearDownTables();
		closeConnection();
	}
	
	private static void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
		}
	}
	
	private static void tearDownTables() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("TRUNCATE TABLE object_lookup_table;");
			stmt.execute("TRUNCATE TABLE user_bindings;");
			stmt.execute("TRUNCATE TABLE `se-mah-elis-services-users-PlatformUser`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-adaptor-energy-eon-internal-user-EonGatewayUser`;");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void setup() throws NoSuchUserException {
		platformUser = createPlatformUser();
		User user = createEonUser();
		userService.registerUserToPlatformUser(user, platformUser);
	}
	
	private User createEonUser() {
		Properties props = new Properties();
		props.put("username", "hems7@eon.se");
		props.put("password", "02DCD0");
		
		EonUserRecipe recipe = new EonUserRecipe();
		
		User user = null;
		try {
			user = factory.build(recipe.getUserType(), recipe.getServiceName(), props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

	private PlatformUser createPlatformUser() {
		PlatformUser user = null;
		try {
			user = userService.createPlatformUser(PLATFORMUSERNAME,
					PLATFORMPASSWORD);
			user.setFirstName("testuser");
			user.setLastName("testuserlastname");
			user.setEmail("test@mah.se");
			userService.updatePlatformUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	@After
	public void teardown() {
		tearDownTables();
	}
	
	@Test
	public void testRetrieveUsers() {
		User[] users = userService.getUsers(platformUser);
		assertEquals(1, users.length);
		assertNotNull(users[0]);
		assertNotNull(users[0].getUserId());
		assertTrue(users[0].getIdentifier().toString().contains("hems7"));
	}
}
