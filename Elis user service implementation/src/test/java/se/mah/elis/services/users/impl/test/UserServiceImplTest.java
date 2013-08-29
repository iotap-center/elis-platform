package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.impl.PlatformUserImpl;
import se.mah.elis.services.users.impl.UserServiceImpl;
import se.mah.elis.services.users.impl.test.mock.AnotherMockUser;
import se.mah.elis.services.users.impl.test.mock.MockUser;

public class UserServiceImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetUsers() {
		UserService us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		
		User[] users = us.getUsers(pu);
		
		assertNotNull(users);
		assertEquals(0, users.length);
	}

	@Test
	public void testRegisterUserToPlatformUser() {
		UserService us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		User mu = new MockUser();
		
		try {
			us.registerUserToPlatformUser(mu, pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User[] users = us.getUsers(pu);
		
		assertNotNull(users);
		assertEquals(1, users.length);
		assertEquals("I'm a MockUserIndentifier", users[0].getId().toString());
	}

	@Test
	public void testRegisterUserToPlatformUserMultiple() {
		UserService us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		User mu = new MockUser();
		User amu = new AnotherMockUser();
		
		try {
			us.registerUserToPlatformUser(mu, pu);
			us.registerUserToPlatformUser(amu, pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User[] users = us.getUsers(pu);
		
		assertNotNull(users);
		assertEquals(2, users.length);
		assertEquals("I'm a MockUserIndentifier", users[0].getId().toString());
		assertEquals("I'm an AnotherMockUserIndentifier", users[1].getId().toString());
	}

	@Test
	public void testUnregisterUserFromPlatformUser() {
		UserService us = new UserServiceImpl();
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
		assertEquals("I'm an AnotherMockUserIndentifier", users[0].getId().toString());
	}

	@Test
	public void testUnregisterAllUsersFromPlatformUser() {
		UserService us = new UserServiceImpl();
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
	public void testCreatePlatformUser() {
		UserService us = new UserServiceImpl();
		PlatformUser pu = us.createPlatformUser("batman", "superman");
		
		assertEquals("PlatformUser batman", pu.toString());
	}
}
