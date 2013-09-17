package se.mah.elis.external.users.test;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import se.mah.elis.external.users.UserWebService;
import se.mah.elis.external.users.jaxbeans.GatewayUserBean;
import se.mah.elis.external.users.jaxbeans.PlatformUserBean;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.UserExistsException;
import se.mah.elis.services.users.impl.PlatformUserIdentifier;
import se.mah.elis.services.users.impl.PlatformUserImpl;
import se.mah.elis.services.users.impl.UserServiceImpl;
import se.mah.elis.services.users.impl.test.mock.MockUser;

public class UserWebServiceTest {
	
	private UserWebService uws;
	private UserService us;
	
	private static String responseStart = "elisApi({\n";
	private static String responseEnd = "})";
	private static String response200 = "  \"status\": \"OK\",\n"
			+ "  \"code\": \"200\",\n"
			+ "  \"response\": ";
	private static String response201 = "  \"status\": \"CREATED\",\n"
			+ "  \"code\": \"201\",\n"
			+ "  \"response\": ";
	private static String response204 = "  \"status\": \"NO CONTENT\",\n"
			+ "  \"code\": \"204\",\n"
			+ "  \"response\": {\n"
			+ "  }\n";
	private static String response404 = "  \"status\": \"ERROR\",\n"
			+ "  \"code\": \"404\",\n"
			+ "  \"errorType\": \"Not Found\",\n"
			+ "  \"errorDetail\": \"The requested URL was not found on this server.\",\n"
			+ "  \"response\": {}\n";
	private static String response400 = "  \"status\": \"ERROR\",\n"
			+ "  \"code\": \"400\",\n"
			+ "  \"errorType\": \"Bad Request\",\n"
			+ "  \"errorDetail\": \"The request cannot be fulfilled due to bad syntax.\",\n"
			+ "  \"response\": {}\n";
	private static String response409 = "  \"status\": \"CONFLICT\",\n"
			+ "  \"code\": \"409\",\n"
			+ "  \"errorType\": \"Conflict\",\n"
			+ "  \"errorDetail\": \"The proposed URL already exists on this server.\",\n"
			+ "  \"response\": {}\n";

	@Before
	public void setUp() throws Exception {
		us = new UserServiceImpl();
		uws = new UserWebService(us);
	}

	@After
	public void tearDown() throws Exception {
		us = null;
		uws = null;
	}

	@Test
	public void testGetUsers() {
		try {
			PlatformUser pu1 = us.createPlatformUser("1", "a");
			PlatformUser pu2 = us.createPlatformUser("2", "A");
			PlatformUser pu3 = us.createPlatformUser("3", "1");
		} catch (UserExistsException e) {}
		
		String responseString = responseStart + response200
				+ "  \"UserList\": [\n"
				+ "  {\n"
				+ "    \"userId\": \"1\",\n"
				+ "    \"username\": \"1\",\n"
				+ "    \"firstName\": \"\",\n"
				+ "    \"lastName\": \"\",\n"
				+ "    \"email\": \"\"\n"
				+ "  },\n"
				+ "  {\n"
				+ "    \"userId\": \"2\",\n"
				+ "    \"username\": \"2\",\n"
				+ "    \"firstName\": \"\",\n"
				+ "    \"lastName\": \"\",\n"
				+ "    \"email\": \"\"\n"
				+ "  },\n"
				+ "  {\n"
				+ "    \"userId\": \"3\",\n"
				+ "    \"username\": \"3\",\n"
				+ "    \"firstName\": \"\",\n"
				+ "    \"lastName\": \"\",\n"
				+ "    \"email\": \"\"\n"
				+ "  }\n"
				+ "]" + responseEnd;
		
		Response r = uws.getUsers();
		
		assertEquals(200, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testGetUser() {
		try {
			PlatformUser pu1 = us.createPlatformUser("1", "b");
			PlatformUser pu2 = us.createPlatformUser("2", "B");
		} catch (UserExistsException e) {}
		
		String responseString = responseStart + response200
				+ "\"User\": {\n"
				+ "  \"userId\": \"1\",\n"
				+ "  \"username\": \"1\",\n"
				+ "  \"firstName\": \"\",\n"
				+ "  \"lastName\": \"\",\n"
				+ "  \"email\": \"\"\n"
				+ "}"
				+ responseEnd;
		
		Response r = uws.getUser("1");
		
		assertEquals(200, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testGetUserNoSuchUser() {
		try {
			PlatformUser pu1 = us.createPlatformUser("1", "b");
			PlatformUser pu2 = us.createPlatformUser("2", "B");
		} catch (UserExistsException e) {}
		
		String responseString = responseStart + response404 + responseEnd;
		
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
		
		String responseString = responseStart + response201
				+ "  \"User\": {\n"
				+ "  \"userId\": \"1\",\n"
				+ "  \"username\": \"1\",\n"
				+ "  \"firstName\": \"Bruce\",\n"
				+ "  \"lastName\": \"Wayne\",\n"
				+ "  \"email\": \"batman@batcave.org\"\n"
				+ "}"
				+ responseEnd;
		
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
		bean.gatewayUsers = new GatewayUserBean[1];
		bean.gatewayUsers[0] = gw;
		gw.serviceName = "Waynecorp";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "1";
		
		String responseString = responseStart + response201
				+ "  \"User\": {\n"
				+ "  \"id\": \"1\",\n"
				+ "  \"username\": \"1\",\n"
				+ "  \"firstName\": \"Bruce\",\n"
				+ "  \"lastName\": \"Wayne\",\n"
				+ "  \"email\": \"batman@batcave.org\",\n"
				+ "  \"GatewayUser\": [\n"
				+ "    {\n"
				+ "      \"id\": \"1\",\n"
				+ "      \"serviceName\": \"Waynecorp\",\n"
				+ "      \"serviceUserName\": \"batman\",\n"
				+ "      \"servicePassword\": \"robin\"\n"
				+ "}]}"
				+ responseEnd;
		
		Response r = uws.addUser(bean);
		
		assertEquals(201, r.getStatus());
		assertEquals(responseString, r.getEntity());
		
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifier(1, "1", "secret")));
		
		assertNotNull(users);
		assertEquals(1, users.length);
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
		gw.serviceName = "Waynecorp";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "1";

		String responseString = responseStart + response409 + responseEnd;
		
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
		gw.serviceName = "Waynecorp";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "1";

		String responseString = responseStart + response400 + responseEnd;
		
		Response r = uws.addUser(bean);
		
		assertEquals(400, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testUpdateUser() {
		try {
			PlatformUser pu = us.createPlatformUser("1", "secret");
		} catch (UserExistsException e) {}
		PlatformUserBean bean = new PlatformUserBean();
		bean.userId = "1";
		bean.username = "1";
		bean.password = "supersecret";
		bean.firstName = "Bruce";
		bean.lastName = "Wayne";
		
		User[] users = us.getUsers(new PlatformUserImpl(new PlatformUserIdentifier(1, "1", "secret")));
		
		assertNotNull(users);
		assertEquals(0, users.length);
		
		String responseString = responseStart + response200
				+ "\"User\": {\n"
				+ "  \"userId\": \"1\",\n"
				+ "  \"username\": \"1\",\n"
				+ "  \"firstName\": \"Bruce\",\n"
				+ "  \"lastName\": \"Wayne\"\n"
				+ "}"
				+ responseEnd;
		
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
		
		String responseString = responseStart + response404 + responseEnd;
		
		Response r = uws.updateUser(bean.userId, bean);
		
		assertEquals(404, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDeleteUser() {
		try {
			PlatformUser pu = us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		
		String responseString = responseStart + response204 + responseEnd;
		
		Response r = uws.deleteUser("1");
		
		assertEquals(204, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testDeleteUserNoSuchUser() {
		try {
			PlatformUser pu = us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		
		String responseString = responseStart + response404 + responseEnd;
		
		Response r = uws.deleteUser("3");
		
		assertEquals(404, r.getStatus());  // TODO: This, or a 204?
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testCoupleGatewayWithUser() {
		try {
			PlatformUser pu = us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		GatewayUserBean gw = new GatewayUserBean();
		
		gw.serviceName = "Waynecorp";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "1";

		String responseString = responseStart + response200
				+ "    \"User\": {\n"
				+ "      \"id\": \"1\",\n"
				+ "      \"username\": \"1\",\n"
				+ "      \"firstName\": \"\",\n"
				+ "      \"lastName\": \"\",\n"
				+ "      \"email\": \"\",\n"
				+ "      \"GatewayUser\": [\n"
				+ "        {\n"
				+ "          \"id\": \"1\",\n"
				+ "          \"serviceName\": \"Waynecorp\",\n"
				+ "          \"serviceUserName\": \"batman\",\n"
				+ "          \"servicePassword\": \"robin\"\n"
				+ "        }\n"
				+ "      ]\n"
				+ "    }\n"
				+ responseEnd;
		
		Response r = uws.coupleGatewayWithUser("a", gw);
		
		assertEquals(201, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

	@Test
	public void testCoupleGatewayWithUserNoSuchUser() {
		try {
			PlatformUser pu = us.createPlatformUser("1", "2");
		} catch (UserExistsException e) {}
		
		GatewayUserBean gw = new GatewayUserBean();
		
		gw.serviceName = "Waynecorp";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "1";
		
		String responseString = responseStart + response404 + responseEnd;
		
		Response r = uws.coupleGatewayWithUser("2", gw);
		
		assertEquals(404, r.getStatus());
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
		gw.serviceName = "Waynecorp";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "2";
		
		String responseString = responseStart + response200
				+ "    \"User\": {\n"
				+ "      \"id\": \"1\",\n"
				+ "      \"username\": \"1\",\n"
				+ "      \"firstName\": \"\",\n"
				+ "      \"lastName\": \"\",\n"
				+ "      \"email\": \"\"\n"
				+ "    }\n"
				+ responseEnd;
		
		Response r = uws.addUser(bean);
		
		assertEquals(201, r.getStatus());
		
		r = uws.decoupleGatewayFromUser("1", "2");
		
		assertEquals(200, r.getStatus());
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
		bean.gatewayUsers = new GatewayUserBean[1];
		bean.gatewayUsers[0] = gw;
		gw.serviceName = "Waynecorp";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "2";
		
		String responseString = responseStart + response201 + responseEnd;
		
		Response r = uws.addUser(bean);
		
		assertEquals(201, r.getStatus());
		assertEquals(responseString, r.toString());
		
		r = uws.decoupleGatewayFromUser("2", "2");

		responseString = responseStart + response404 + responseEnd;
		
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
		bean.gatewayUsers = new GatewayUserBean[1];
		bean.gatewayUsers[0] = gw;
		gw.serviceName = "Waynecorp";
		gw.serviceUserName = "batman";
		gw.servicePassword = "robin";
		gw.id = "2";
		
		// TODO: This, or a 201?
		String responseString = responseStart + response404 + responseEnd;
		
		Response r = uws.addUser(bean);
		
		assertEquals(201, r.getStatus());
		
		r = uws.decoupleGatewayFromUser("1", "1");

		assertEquals(404, r.getStatus());
		assertEquals(responseString, r.getEntity());
	}

}
