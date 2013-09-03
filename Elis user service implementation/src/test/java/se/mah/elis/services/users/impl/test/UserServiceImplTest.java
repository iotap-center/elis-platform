package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.impl.PlatformUserIdentifier;
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
	public void testGetNbrOfPlatformUsers() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		User mu = new MockUser();

		try {
			us.registerUserToPlatformUser(mu, pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		assertEquals(1, us.getNbrOfPlatformUsers());
	}
	
	@Test
	public void testGetNbrOfPlatformUsersNoUsers() {
		UserServiceImpl us = new UserServiceImpl();
		
		assertEquals(0, us.getNbrOfPlatformUsers());
	}

	@Test
	public void testCreatePlatformUser() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu = us.createPlatformUser("batman", "superman");
		
		assertEquals("PlatformUser batman", pu.toString());
		assertEquals(1, us.getNbrOfPlatformUsers());
	}

	@Test
	public void testCreatePlatformUserTwoUsers() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu1 = us.createPlatformUser("batman", "superman");
		PlatformUser pu2 = us.createPlatformUser("bilbo", "baggins");

		assertEquals("PlatformUser batman", pu1.toString());
		assertEquals("PlatformUser bilbo", pu2.toString());
		assertEquals(2, us.getNbrOfPlatformUsers());
	}

	@Test
	public void testGetPlatformUsersAssociatedWithUser() {
		UserService us = new UserServiceImpl();
		PlatformUser pu = us.createPlatformUser("batman", "superman");
		User mu = new MockUser();
		
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
		assertEquals("batman, superman", pus[0].getId().toString());
	}

	@Test
	public void testGetPlatformUsersAssociatedWithUserTwoPlatformUsers() {
		UserService us = new UserServiceImpl();
		PlatformUser pu1 = new PlatformUserImpl(new PlatformUserIdentifier("a", "b"));
		PlatformUser pu2 = new PlatformUserImpl(new PlatformUserIdentifier("1", "2"));
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
		UserService us = new UserServiceImpl();
		PlatformUser pu1 = new PlatformUserImpl(new PlatformUserIdentifier("a", "b"));
		PlatformUser pu2 = new PlatformUserImpl(new PlatformUserIdentifier("1", "2"));
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
		UserService us = new UserServiceImpl();
		PlatformUser pu1 = new PlatformUserImpl(new PlatformUserIdentifier("a", "b"));
		PlatformUser pu2 = new PlatformUserImpl(new PlatformUserIdentifier("1", "2"));
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
		assertEquals("a, b", pus[0].getId().toString());
	}
	
	private boolean findInArray(PlatformUser needle, PlatformUser[] haystack) {
		for (int i = 0; i < haystack.length; i++) {
			if (needle.toString().equals(haystack[i].toString())) {
				return true;
			}
		}
		
		return false;
	}
}
