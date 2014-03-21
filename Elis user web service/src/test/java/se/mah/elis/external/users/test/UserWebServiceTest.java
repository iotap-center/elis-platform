package se.mah.elis.external.users.test;

import static org.junit.Assert.*;
import static org.fest.assertions.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

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
		PlatformUser pu1 = null, pu2 = null, pu3 = null;
		
		try {
			pu1 = us.createPlatformUser("1", "a");
			pu2 = us.createPlatformUser("2", "A");
			pu3 = us.createPlatformUser("3", "1");
		} catch (UserExistsException e) {}
		
		String responseString = envelopeStart + response200
				+ "    \"userList\": [\n"
				+ "      {\n"
				+ "        \"userId\": \"" + pu1.getUserId() + "\",\n"
				+ "        \"username\": \"3\",\n"
				+ "        \"firstName\": \"\",\n"
				+ "        \"lastName\": \"\",\n"
				+ "        \"email\": \"\"\n"
				+ "      },\n"
				+ "      {\n"
				+ "        \"userId\": \"" + pu2.getUserId() + "\",\n"
				+ "        \"username\": \"2\",\n"
				+ "        \"firstName\": \"\",\n"
				+ "        \"lastName\": \"\",\n"
				+ "        \"email\": \"\"\n"
				+ "      },\n"
				+ "      {\n"
				+ "        \"userId\": \"" + pu3.getUserId() + "\",\n"
				+ "        \"username\": \"1\",\n"
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
		PlatformUser p1 = null, p2 = null;
		
		try {
			p1 = us.createPlatformUser("1", "b");
			p2 = us.createPlatformUser("2", "B");
		} catch (UserExistsException e) {}
		
		String responseString = envelopeStart + response200
				+ "    \"user\": {\n"
				+ "      \"userId\": \"" + p1.getUserId() + "\",\n"
				+ "      \"username\": \"1\",\n"
				+ "      \"firstName\": \"\",\n"
				+ "      \"lastName\": \"\",\n"
				+ "      \"email\": \"\"\n"
				+ "    }\n"
				+ responseEnd
				+ envelopeEnd;
		
		Response r = uws.getUser(p1.getUserId().toString());
		
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
		
		Response r = uws.getUser("deadbeef-2222-3333-4444-555566667777");
		
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
		PlatformUser pu;
		bean.userId = "";
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
		
		Response r = uws.addUser(bean);
		
		pu = us.getPlatformUser(new PlatformUserIdentifierImpl("1", "secret"));
		
		String responseString = envelopeStart + response201
				+ "    \"user\": {\n"
				+ "      \"userId\": \"" + pu.getUserId() + "\",\n"
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
		
		assertEquals(201, r.getStatus());
		assertEquals(responseString, r.getEntity());
		
		User[] users = us.getUsers(pu);
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
		bean.userId = "00001111-2222-3333-4444-555566667777";
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
		
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifierImpl("1", "secret")));
		
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
		PlatformUser pu = null;
		
		try {
			pu = us.createPlatformUser("1", "supersecret");
		} catch (UserExistsException e) {}
		PlatformUserBean bean = new PlatformUserBean();
		bean.userId = pu.getUserId().toString();
		bean.username = "1";
		bean.password = "supersecret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		
		User[] users = us.getUsers(pu);
		
		assertNotNull(users);
		assertEquals(0, users.length);
		
		String responseString = envelopeStart + response200
				+ "    \"user\": {\n"
				+ "      \"userId\": \"" + pu.getUserId() + "\",\n"
				+ "      \"username\": \"1\",\n"
				+ "      \"firstName\": \"Bruce\",\n"
				+ "      \"lastName\": \"Wayne\"\n"
				+ "    }\n"
				+ responseEnd
				+ envelopeEnd;
		
		Response r = uws.updateUser(bean.userId, bean);
		
		assertEquals(200, r.getStatus());
		assertEquals(responseString, r.getEntity());
		
		pu = null;
		pu = us.getPlatformUser(new PlatformUserIdentifierImpl("1", "supersecret"));
		
		assertNotNull(pu);
		assertEquals("Bruce", pu.getFirstName());
	}

	@Test
	public void testUpdateUserNoSuchUser() {
		PlatformUserBean bean = new PlatformUserBean();
		bean.userId = "deadbeef-2222-3333-4444-555566667777";
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
		PlatformUser pu = null;
		
		try {
			pu = us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		
		String responseString = envelopeStart + response204 + envelopeEnd;
		
		Response r = uws.deleteUser(pu.getUserId().toString());
		
		assertEquals(204, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDeleteUserNoSuchUser() {
		try {
			us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		
		String responseString = envelopeStart + response404 + envelopeEnd;
		
		Response r = uws.deleteUser("deadbeef-2222-3333-4444-555566667777");
		
		assertEquals(404, r.getStatus());  // TODO: This, or a 204?
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testCoupleGatewayWithUser() {
		PlatformUser pu = null;
		
		try {
			pu = us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		GatewayUserBean gw = new GatewayUserBean();
		
		gw.id = "deadbeef-2222-3333-4444-555566667777";
		gw.serviceName = "uwstest";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";

		String responseString = envelopeStart + response200
				+ "    \"user\": {\n"
				+ "      \"userId\": \"" + pu.getUserId() + "\",\n"
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
		
		Response r = uws.coupleGatewayWithUser("gateway", pu.getUserId().toString(), gw);
		User[] users = us.getUsers(pu);
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
		PlatformUser pu = null;
		
		try {
			pu = us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		
		GatewayUserBean gw = new GatewayUserBean();
		
		gw.serviceName = "uwstest";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		
		String responseString = envelopeStart + response400 + envelopeEnd;
		
		Response r = uws.coupleGatewayWithUser("gateway", "deadbeef-2222-3333-4444-555566667777", gw);
		User[] users = us.getUsers(pu);

		assertEquals(0, users.length);
		assertEquals(400, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDecoupleGatewayFromUser() {
		PlatformUserBean bean = new PlatformUserBean();
		GatewayUserBean gw = new GatewayUserBean();
		bean.userId = "00001111-2222-3333-4444-555566667777";
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
		
		r = uws.decoupleGatewayFromUser(bean.userId, "deadbeef-2222-3333-4444-555566667777");
		
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifierImpl("1", "secret")));
		
		assertEquals(0, users.length);
		assertEquals(404, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDecoupleGatewayFromUserNoSuchUser() {
		PlatformUser pu = null;
		PlatformUserBean bean = new PlatformUserBean();
		GatewayUserBean gw = new GatewayUserBean();
		bean.userId = "";
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
		
		pu = us.getPlatformUser(new PlatformUserIdentifierImpl("1", "secret"));
		
		Response r = uws.decoupleGatewayFromUser("deadbeef-2222-3333-4444-555566667777",
				"00001111-2222-3333-4444-555566667777");

		String responseString = envelopeStart + response404 + envelopeEnd;
		
		User[] users = us.getUsers(pu);
		
		assertEquals(1, users.length);
		assertEquals(404, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDecoupleGatewayFromUserBadUser() {
		PlatformUser pu = null;
		PlatformUserBean bean = new PlatformUserBean();
		GatewayUserBean gw = new GatewayUserBean();
		bean.userId = "deadbeef-2222-3333-4444-555566667777";
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
		
		Response r = uws.decoupleGatewayFromUser("deadbeef-2222-3333-4444-555566667777",
				"00001111-2222-3333-4444-555566667777");

		String responseString = envelopeStart + response404 + envelopeEnd;
		
		pu = us.getPlatformUser(new PlatformUserIdentifierImpl("1", "secret"));
		User[] users = us.getUsers(pu);
		
		assertEquals(1, users.length);
		assertEquals(404, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDecoupleGatewayFromUserNoSuchGateway() {
		PlatformUser pu = null;
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
		
		try {
			pu = us.createPlatformUser("1", "secret");
		} catch (UserExistsException e) {
			fail("This shouldn't happen");
		}
		
		String responseString = envelopeStart + response404
				+ envelopeEnd;
		
		Response r = uws.decoupleGatewayFromUser(pu.getUserId().toString(),
				"deadbeef-2222-3333-4444-555566667777");
		
		User[] users = us.getUsers(pu);
		
		assertEquals(0, users.length);
		assertEquals(404, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

}
