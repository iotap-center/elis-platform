package se.mah.elis.external.users.test;

import static org.junit.Assert.*;
import static org.fest.assertions.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.external.users.UserWebService;
import se.mah.elis.external.users.jaxbeans.GatewayUserBean;
import se.mah.elis.external.users.jaxbeans.PlatformUserBean;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.UserExistsException;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.impl.test.mock.GatewayUserProvider;
import se.mah.elis.impl.services.storage.StorageImpl;
import se.mah.elis.impl.services.users.factory.UserFactoryImpl;
import se.mah.elis.impl.services.users.PlatformUserIdentifierImpl;
import se.mah.elis.impl.services.users.PlatformUserImpl;
import se.mah.elis.impl.services.users.UserServiceImpl;
import se.mah.elis.services.users.impl.test.mock.GatewayUser;

public class UserWebServiceTest extends JerseyTest {
	
	@Override
	protected Application configure() {
		return new ResourceConfig(UserWebService.class);
	}
	
	private UserWebService uws;
	private UserService us;
	private UserFactory uf;
	
	private Connection connection;
	private Storage storage;
	
	private static String envelopeStart = "{\n";
	private static String envelopeEnd = "}";
	private static String responseEnd = "  }\n";
	private static String response200 = "  \"status\": \"OK\",\n"
			+ "  \"code\": 200,\n"
			+ "  \"response\": {\n";
	private static String response201 = "  \"status\": \"Created\",\n"
			+ "  \"code\": 201,\n"
			+ "  \"response\": {\n";
	private static String response204 = "  \"status\": \"No Content\",\n"
			+ "  \"code\": 204,\n"
			+ "  \"response\": {}\n";
	private static String response404 = "  \"errorType\": \"Not Found\",\n"
			+ "  \"errorDetail\": \"The requested URL was not found on this server.\",\n"
			+ "  \"status\": \"Error\",\n"
			+ "  \"code\": 404,\n"
			+ "  \"response\": {}\n";
	private static String response400 = "  \"errorType\": \"Bad Request\",\n"
			+ "  \"errorDetail\": \"The request cannot be fulfilled due to bad syntax.\",\n"
			+ "  \"status\": \"Error\",\n"
			+ "  \"code\": 400,\n"
			+ "  \"response\": {}\n";
	private static String response409 = "  \"errorType\": \"Conflict\",\n"
			+ "  \"errorDetail\": \"The proposed URL already exists on this server.\",\n"
			+ "  \"status\": \"Error\",\n"
			+ "  \"code\": 409,\n"
			+ "  \"response\": {}\n";

	@Before
	public void setUp() throws Exception {
		setUpDatabase();
		tearDownTables();

		uf = new UserFactoryImpl();
		storage = new StorageImpl(connection, uf);
		us = new UserServiceImpl(storage, connection);
		uws = new UserWebService(us, uf);
		
		uf.registerProvider(new GatewayUserProvider());
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
		
		us = null;
		uf = null;
		uws = null;
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
			stmt.execute("TRUNCATE TABLE `object_lookup_table`;");
			stmt.execute("TRUNCATE TABLE `user_bindings`;");
			stmt.execute("TRUNCATE TABLE `se-mah-elis-services-users-PlatformUser`;");
			stmt.execute("DROP TABLE IF EXISTS `se-mah-elis-services-users-impl-test-mock-GatewayUser`;");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetUsers() {
		try {
			us.createPlatformUser("1", "a");
			us.createPlatformUser("2", "A");
			us.createPlatformUser("3", "1");
		} catch (UserExistsException e) {}
		
		String responseString = envelopeStart + response200
				+ "    \"userList\": [\n"
				+ "      {\n"
				+ "        \"userId\": \"1\",\n"
				+ "        \"username\": \"1\",\n"
				+ "        \"firstName\": \"\",\n"
				+ "        \"lastName\": \"\",\n"
				+ "        \"email\": \"\"\n"
				+ "      },\n"
				+ "      {\n"
				+ "        \"userId\": \"2\",\n"
				+ "        \"username\": \"2\",\n"
				+ "        \"firstName\": \"\",\n"
				+ "        \"lastName\": \"\",\n"
				+ "        \"email\": \"\"\n"
				+ "      },\n"
				+ "      {\n"
				+ "        \"userId\": \"3\",\n"
				+ "        \"username\": \"3\",\n"
				+ "        \"firstName\": \"\",\n"
				+ "        \"lastName\": \"\",\n"
				+ "        \"email\": \"\"\n"
				+ "      }\n"
				+ "    ]\n"
				+ responseEnd
				+ envelopeEnd;
		
		Response r = uws.getUsers();
		
		assertEquals(200, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testGetUser() {
		try {
			us.createPlatformUser("1", "b");
			us.createPlatformUser("2", "B");
		} catch (UserExistsException e) {}
		
		String responseString = envelopeStart + response200
				+ "    \"user\": {\n"
				+ "      \"userId\": \"1\",\n"
				+ "      \"username\": \"1\",\n"
				+ "      \"firstName\": \"\",\n"
				+ "      \"lastName\": \"\",\n"
				+ "      \"email\": \"\"\n"
				+ "    }\n"
				+ responseEnd
				+ envelopeEnd;
		
		Response r = uws.getUser("1");
		
		assertEquals(200, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testGetUserNoSuchUser() {
		try {
			us.createPlatformUser("1", "b");
			us.createPlatformUser("2", "B");
		} catch (UserExistsException e) {}
		
		String responseString = envelopeStart + response404 + envelopeEnd;
		
		Response r = uws.getUser("3");
		
		assertEquals(404, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testAddUser() {
		PlatformUserBean bean = new PlatformUserBean();
		bean.userId = "1";
		bean.username = "1";
		bean.password = "secret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		bean.email = "batman@batcave.org";
		
		String responseString = envelopeStart + response201
				+ "    \"user\": {\n"
				+ "      \"userId\": \"1\",\n"
				+ "      \"username\": \"1\",\n"
				+ "      \"firstName\": \"Bruce\",\n"
				+ "      \"lastName\": \"Wayne\",\n"
				+ "      \"email\": \"batman@batcave.org\"\n"
				+ "    }\n"
				+ responseEnd
				+ envelopeEnd;
		
		Response r = uws.addUser(bean);
				
		assertEquals(201, r.getStatus());
		assertEquals(responseString, (String) r.getEntity());
	}

	@Test
	public void testAddUserWithGateway() {
		PlatformUserBean bean = new PlatformUserBean();
		GatewayUserBean gw = new GatewayUserBean();
		bean.userId = "1";
		bean.username = "1";
		bean.password = "secret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		bean.email = "batman@batcave.org";
		bean.gatewayUser = gw;
		gw.id = "00001111-2222-3333-4444-555566667777";
		gw.serviceName = "uwstest";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		
		String responseString = envelopeStart + response201
				+ "    \"user\": {\n"
				+ "      \"userId\": \"1\",\n"
				+ "      \"username\": \"1\",\n"
				+ "      \"firstName\": \"Bruce\",\n"
				+ "      \"lastName\": \"Wayne\",\n"
				+ "      \"email\": \"batman@batcave.org\",\n"
				+ "      \"gatewayUser\": {\n"
				+ "        \"id\": \"00001111-2222-3333-4444-555566667777\",\n"
				+ "        \"serviceName\": \"uwstest\",\n"
				+ "        \"serviceUserName\": \"batman\",\n"
				+ "        \"servicePassword\": \"robin\"\n"
				+ "      }\n"
				+ "    }\n"
				+ responseEnd
				+ envelopeEnd;
		
		Response r = uws.addUser(bean);
		
		assertEquals(201, r.getStatus());
		assertEquals(responseString, r.getEntity());
		
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "1", "secret")));
		User user = users[0];

		assertNotNull(users);
		assertNotNull(user);
		assertEquals(1, users.length);
		assertThat(user.getClass().getName()).matches(GatewayUser.class.getName());
		assertThat(((GatewayUser) user).getServiceUserName()).matches(gw.serviceUserName);
	}

	@Test
	public void testAddUserWithGatewayStrangeType() {
		PlatformUserBean bean = new PlatformUserBean();
		GatewayUserBean gw = new GatewayUserBean();
		bean.userId = "1";
		bean.username = "1";
		bean.password = "secret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		bean.email = "batman@batcave.org";
		bean.gatewayUser = gw;
		gw.serviceName = "horses";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		
		String responseString = envelopeStart + response400 + envelopeEnd;
		
		Response r = uws.addUser(bean);

		assertEquals(400, r.getStatus());
		assertEquals(responseString, r.getEntity());
		
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "1", "secret")));
		
		assertNotNull(users);
		assertEquals(0, users.length);
	}

	@Test
	public void testAddUserExistingUser() {
		PlatformUserBean bean = new PlatformUserBean();
		GatewayUserBean gw = new GatewayUserBean();
		bean.userId = "1";
		bean.username = "1";
		bean.password = "secret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		bean.email = "batman@batcave.org";
		bean.gatewayUsers = new GatewayUserBean[1];
		bean.gatewayUsers[0] = gw;
		gw.serviceName = "uwstest";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";

		String responseString = envelopeStart + response409 + envelopeEnd;
		
		try {
			us.createPlatformUser("1", "secret");
		} catch (UserExistsException e) {}
		Response r = uws.addUser(bean);
		
		assertEquals(409, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testAddUserStrangeData() {
		PlatformUserBean bean = new PlatformUserBean();
		GatewayUserBean gw = new GatewayUserBean();
		bean.userId = "";
		bean.username = "";
		bean.password = "";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		bean.email = "batman@batcave.org";
		bean.gatewayUsers = new GatewayUserBean[1];
		bean.gatewayUsers[0] = gw;
		gw.serviceName = "uwstest";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "1";

		String responseString = envelopeStart + response400 + envelopeEnd;
		
		Response r = uws.addUser(bean);
		
		assertEquals(400, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testUpdateUser() {
		try {
			us.createPlatformUser("1", "secret");
		} catch (UserExistsException e) {}
		PlatformUserBean bean = new PlatformUserBean();
		bean.userId = "1";
		bean.username = "1";
		bean.password = "supersecret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "1", "secret")));
		
		assertNotNull(users);
		assertEquals(0, users.length);
		
		String responseString = envelopeStart + response200
				+ "    \"user\": {\n"
				+ "      \"userId\": \"1\",\n"
				+ "      \"username\": \"1\",\n"
				+ "      \"firstName\": \"Bruce\",\n"
				+ "      \"lastName\": \"Wayne\"\n"
				+ "    }\n"
				+ responseEnd
				+ envelopeEnd;
		
		Response r = uws.updateUser(bean.userId, bean);
		
		assertEquals(200, r.getStatus());
		assertEquals(responseString, r.getEntity());
		
		PlatformUser pu = us.getPlatformUser("1");
		
		assertNotNull(pu);
		assertEquals("Bruce", pu.getFirstName());
	}

	@Test
	public void testUpdateUserNoSuchUser() {
		PlatformUserBean bean = new PlatformUserBean();
		bean.userId = "1";
		bean.username = "1";
		bean.password = "secret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		
		String responseString = envelopeStart + response404 + envelopeEnd;
		
		Response r = uws.updateUser(bean.userId, bean);
		
		assertEquals(404, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDeleteUser() {
		try {
			us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		
		String responseString = envelopeStart + response204 + envelopeEnd;
		
		Response r = uws.deleteUser("1");
		
		assertEquals(204, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDeleteUserNoSuchUser() {
		try {
			us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		
		String responseString = envelopeStart + response404 + envelopeEnd;
		
		Response r = uws.deleteUser("3");
		
		assertEquals(404, r.getStatus());  // TODO: This, or a 204?
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testCoupleGatewayWithUser() {
		try {
			us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		GatewayUserBean gw = new GatewayUserBean();
		
		gw.id = "deadbeef-2222-3333-4444-555566667777";
		gw.serviceName = "uwstest";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";

		String responseString = envelopeStart + response200
				+ "    \"user\": {\n"
				+ "      \"userId\": \"1\",\n"
				+ "      \"username\": \"1\",\n"
				+ "      \"firstName\": \"\",\n"
				+ "      \"lastName\": \"\",\n"
				+ "      \"email\": \"\",\n"
				+ "      \"gatewayUser\": {\n"
				+ "        \"id\": \"deadbeef-2222-3333-4444-555566667777\",\n"
				+ "        \"serviceName\": \"uwstest\",\n"
				+ "        \"serviceUserName\": \"batman\",\n"
				+ "        \"servicePassword\": \"robin\"\n"
				+ "      }\n"
				+ "    }\n"
				+ responseEnd
				+ envelopeEnd;
		
		Response r = uws.coupleGatewayWithUser("gateway", "1", gw);
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "1", "secret")));
		User user = users[0];
		
		assertNotNull(users);
		assertNotNull(user);
		assertEquals(1, users.length);
		assertThat(user.getClass().getName()).matches(GatewayUser.class.getName());
		assertThat(((GatewayUser) user).getServiceUserName()).matches(gw.serviceUserName);
		assertEquals(200, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testCoupleGatewayWithUserNoSuchUser() {
		try {
			us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		
		GatewayUserBean gw = new GatewayUserBean();
		
		gw.serviceName = "uwstest";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		
		String responseString = envelopeStart + response400 + envelopeEnd;
		
		Response r = uws.coupleGatewayWithUser("gateway", "2", gw);
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "1", "secret")));

		assertEquals(0, users.length);
		assertEquals(400, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDecoupleGatewayFromUser() {
		PlatformUserBean bean = new PlatformUserBean();
		GatewayUserBean gw = new GatewayUserBean();
		bean.userId = "1";
		bean.username = "1";
		bean.password = "secret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		bean.email = "batman@batcave.org";
		bean.gatewayUsers = new GatewayUserBean[1];
		bean.gatewayUsers[0] = gw;
		gw.serviceName = "uwstest";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "deadbeef-2222-3333-4444-555566667777";
		
		String responseString = envelopeStart + response404 + envelopeEnd;
		
		Response r = uws.addUser(bean);
		
		assertEquals(201, r.getStatus());
		
		r = uws.decoupleGatewayFromUser("1", "deadbeef-2222-3333-4444-555566667777");
		
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "1", "secret")));
		
		assertEquals(0, users.length);
		assertEquals(404, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDecoupleGatewayFromUserNoSuchUser() {
		PlatformUserBean bean = new PlatformUserBean();
		GatewayUserBean gw = new GatewayUserBean();
		bean.userId = "1";
		bean.username = "1";
		bean.password = "secret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		bean.email = "batman@batcave.org";
		bean.gatewayUser = gw;
		gw.serviceName = "uwstest";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "00001111-2222-3333-4444-555566667777";
		
		uws.addUser(bean);
		
		Response r = uws.decoupleGatewayFromUser("2", "00001111-2222-3333-4444-555566667777");

		String responseString = envelopeStart + response404 + envelopeEnd;
		
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "1", "secret")));
		
		assertEquals(1, users.length);
		assertEquals(404, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDecoupleGatewayFromUserNoSuchGateway() {
		PlatformUserBean bean = new PlatformUserBean();
		GatewayUserBean gw = new GatewayUserBean();
		bean.userId = "1";
		bean.username = "1";
		bean.password = "secret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		bean.email = "batman@batcave.org";
		bean.gatewayUser = gw;
		gw.serviceName = "uwstest";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "deadbeef-2222-3333-4444-555566667777";
		
		String responseString = envelopeStart + response200
				+ "    \"user\": {\n"
				+ "      \"userId\": \"1\",\n"
				+ "      \"username\": \"1\",\n"
				+ "      \"firstName\": \"Bruce\",\n"
				+ "      \"lastName\": \"Wayne\",\n"
				+ "      \"email\": \"batman@batcave.org\"\n"
				+ "    }\n"
				+ "  }\n"
				+ envelopeEnd;
		
		Response r = uws.addUser(bean);
		
		assertEquals(201, r.getStatus());
		
		r = uws.decoupleGatewayFromUser("1", "deadbeef-2222-3333-4444-555566667777");
		
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "1", "secret")));
		
		assertEquals(1, users.length);
		assertEquals(200, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

}
