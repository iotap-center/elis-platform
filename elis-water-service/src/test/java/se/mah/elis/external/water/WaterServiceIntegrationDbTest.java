package se.mah.elis.external.water;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import se.mah.elis.adaptor.water.mkb.MkbProvider;
import se.mah.elis.external.water.beans.WaterBean;
import se.mah.elis.impl.services.storage.StorageImpl;
import se.mah.elis.impl.services.users.PlatformUserIdentifierImpl;
import se.mah.elis.impl.services.users.UserServiceImpl;
import se.mah.elis.impl.services.users.factory.UserFactoryImpl;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.factory.UserFactory;

import com.google.gson.Gson;

public class WaterServiceIntegrationDbTest extends JerseyTest {

	private static final String METERID = "63408097";
	private static final String MKB_SERVICENAME = "mkb-water";
	private static final String MKB_USERTYPE = "MkbWaterUser";
	private static final String PLATFORMPASSWORD = "elis_platform_password";
	private static final String PLATFORMUSERNAME = "elis_platform_username";
	private static UserServiceImpl userService;
	private static LogService log;
	private static Connection connection;
	private static StorageImpl storage;
	private static UserFactory factory;

	private Gson gson = new Gson();
	private PlatformUser platformUser;

	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new WaterService(userService, log));
		return config;
	}

	@BeforeClass
	public static void setupClass() {
		setUpDatabase();
		factory = new UserFactoryImpl();
		storage = new StorageImpl(connection, factory);
		userService = new UserServiceImpl(storage, connection);
		log = mock(LogService.class);
		factory.registerProvider(new MkbProvider());
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
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setup() throws NoSuchUserException {
		platformUser = createPlatformUser();
		User user = createMkbUser();
		userService.registerUserToPlatformUser(user, platformUser);
	}

	private User createMkbUser() {
		Properties props = new Properties();
		props.put("id", METERID);
		User user = null;
		try {
			user = factory.build(MKB_USERTYPE, MKB_SERVICENAME, props);
		} catch (Exception e) {
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
		}
		return user;
	}

	@After
	public void tearDown() {
		tearDownTables();
	}

	@Test
	public void testRetrieveUsers() {
		User[] users = userService.getUsers(platformUser);
		assertEquals(1, users.length);
		assertNotNull(users[0]);
		assertEquals(METERID, users[0].getIdentifier());
	}

	@Test
	public void testGetWaterDataNow() {
		final String data = target(
				"/water/" + platformUserId() + "/now")
				.request().get(String.class);
		WaterBean bean = gson.fromJson(data, WaterBean.class);
		assertEquals(platformUserId(), bean.puid);
		assertEquals("now", bean.period);
		assertTrue(bean.devices.size() > 0);
		assertTrue(bean.devices.get(0).data.size() > 0);
	}

	private String platformUserId() {
		return platformUser.getUserId().toString();
	}
}
