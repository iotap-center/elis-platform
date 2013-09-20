package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.exceptions.UserExistsException;
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
	public void testGetUser() {
		UserService us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		User mu = new MockUser();
		
		try {
			us.registerUserToPlatformUser(mu, pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User user = us.getUser(pu, mu.getIdentifier());
		
		assertNotNull(user);
		assertEquals(user, mu);
	}
	
	@Test
	public void testGetUserMultipleUserTypes() {
		UserService us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		User mu = new MockUser();
		
		try {
			us.registerUserToPlatformUser(mu, pu);
			us.registerUserToPlatformUser(new AnotherMockUser(), pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User user = us.getUser(pu, mu.getIdentifier());
		
		assertNotNull(user);
		assertEquals(user, mu);
	}
	
	@Test
	public void testGetUserMultipeUsersOfSameType() {
		UserService us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		User mu = new MockUser();
		
		try {
			us.registerUserToPlatformUser(mu, pu);
			us.registerUserToPlatformUser(new MockUser(), pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User user = us.getUser(pu, mu.getIdentifier());
		
		assertNotNull(user);
		assertEquals(user, mu);
	}
	
	@Test
	public void testGetUserNoSuchUser() {
		UserService us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		User mu = new MockUser();
		
		try {
			us.registerUserToPlatformUser(new AnotherMockUser(), pu);
		} catch (NoSuchUserException e) {
			fail("Register no workie");
		}
		
		User user = us.getUser(pu, mu.getIdentifier());
		
		assertNull(user);
	}
	
	@Test
	public void testGetUserNoSuchPlatformUser() {
		fail("Not yet implemented");
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
		assertEquals("I'm a MockUserIndentifier", users[0].getIdentifier().toString());
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
		assertEquals("I'm a MockUserIndentifier", users[0].getIdentifier().toString());
		assertEquals("I'm an AnotherMockUserIndentifier", users[1].getIdentifier().toString());
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
		assertEquals("I'm an AnotherMockUserIndentifier", users[0].getIdentifier().toString());
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
	public void testGetPlatformUser() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		PlatformUser actual = null;
		try {
			pu = us.createPlatformUser("batman", "superman");
		} catch (UserExistsException e) {}

		assertEquals("PlatformUser batman (1)", pu.toString());
		assertEquals(1, us.getNbrOfPlatformUsers());
		
		actual = us.getPlatformUser(pu.getIdentifier());
		
		assertEquals(actual, pu);
	}

	@Test
	public void testGetPlatformUserMultipleUsers() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		PlatformUser actual = null;
		try {
			pu1 = us.createPlatformUser("batman", "superman");
			pu2 = us.createPlatformUser("fred", "barney");
		} catch (UserExistsException e) {}

		assertEquals(2, us.getNbrOfPlatformUsers());
		
		actual = us.getPlatformUser(pu2.getIdentifier());
		
		assertEquals(actual, pu2);
	}

	@Test
	public void testGetPlatformUserWithString() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		PlatformUser actual = null;
		try {
			pu1 = us.createPlatformUser("batman", "superman");
			pu2 = us.createPlatformUser("fred", "barney");
		} catch (UserExistsException e) {}

		assertEquals(2, us.getNbrOfPlatformUsers());
		
		actual = us.getPlatformUser("2");
		
		assertEquals(actual, pu2);
	}

	@Test
	public void testGetPlatformUserNotFound() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		PlatformUser actual = null;
		try {
			pu1 = us.createPlatformUser("batman", "superman");
			pu2 = us.createPlatformUser("fred", "barney");
		} catch (UserExistsException e) {}

		assertEquals(2, us.getNbrOfPlatformUsers());
		
		actual = us.getPlatformUser(new PlatformUserIdentifier(3, "arthur", "douglas"));
		
		assertNull(actual);
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
		PlatformUser pu = new PlatformUserImpl();
		try {
			pu = us.createPlatformUser("batman", "superman");
		} catch (UserExistsException e) {}

		assertEquals("PlatformUser batman (1)", pu.toString());
		assertEquals(1, us.getNbrOfPlatformUsers());
	}

	@Test
	public void testCreatePlatformUserBadData() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		try {
			pu = us.createPlatformUser("", "superman");
			fail("IllegalArgumentException should've been raised");
		} catch (UserExistsException e) {
			fail("IllegalArgumentException should've been raised");
		} catch (IllegalArgumentException e) {}
	}

	@Test
	public void testCreatePlatformUserExistingUser() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu1;
		try {
			pu1 = us.createPlatformUser("batman", "superman");
		} catch (UserExistsException e) {
			fail("Adding a platform user should work");
		}
		
		try {
			PlatformUser pu2 = us.createPlatformUser("batman", "robin");
			fail("Readding an existing user shouldn't work.");
		} catch (UserExistsException e) {}
	}

	@Test
	public void testCreatePlatformUserTwoUsers() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		try {
			pu1 = us.createPlatformUser("batman", "superman");
			pu2 = us.createPlatformUser("bilbo", "baggins");
		} catch (UserExistsException e) {}

		assertEquals("PlatformUser batman (1)", pu1.toString());
		assertEquals("PlatformUser bilbo (2)", pu2.toString());
		assertEquals(2, us.getNbrOfPlatformUsers());
	}

	@Test
	public void testGetPlatformUsersAssociatedWithUser() {
		UserService us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		try {
			pu = us.createPlatformUser("batman", "superman");
		} catch (UserExistsException e1) {}
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
		assertEquals("1: batman, superman", pus[0].getIdentifier().toString());
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
		assertEquals("1: a, b", pus[0].getIdentifier().toString());
	}
	
	@Test
	public void testGetPlatformUsers() {
		UserServiceImpl us = new UserServiceImpl();
		try {
			PlatformUser pu1 = us.createPlatformUser("batman", "superman");
			PlatformUser pu2 = us.createPlatformUser("bilbo", "baggins");
			PlatformUser pu3 = us.createPlatformUser("orvar", "säfström");
		} catch (UserExistsException e) {}
		PlatformUser[] pus = us.getPlatformUsers();
		assertNotNull(pus);
		assertEquals(3, pus.length);
	}
	
	@Test
	public void testGetPlatformUsersNoUsers() {
		UserServiceImpl us = new UserServiceImpl();

		PlatformUser[] pus = us.getPlatformUsers();
		assertNotNull(pus);
		assertEquals(0, pus.length);
	}
	
	@Test
	public void testGetPlatformUsersOneUser() {
		UserServiceImpl us = new UserServiceImpl();
		try {
			PlatformUser pu1 = us.createPlatformUser("batman", "superman");
		} catch (UserExistsException e) {}

		PlatformUser[] pus = us.getPlatformUsers();
		assertNotNull(pus);
		assertEquals(1, pus.length);
	}
	
	@Test
	public void testUpdatePlatformUser() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu = new PlatformUserImpl();
		try {
			pu = us.createPlatformUser("batman", "superman");
		} catch (UserExistsException e1) {}
		
		assertEquals("PlatformUser batman (1)", pu.toString());
		assertEquals(1, us.getNbrOfPlatformUsers());

		pu.setFirstName("Bruce");
		pu.setLastName("Wayne");
		
		try {
			us.updatePlatformUser(pu);
		} catch (NoSuchUserException e) {
			fail("Bad stuff going on");
		}
	}
	
	@Test
	public void testUpdatePlatformUserNonExistingUser() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		try {
			pu1 = us.createPlatformUser("batman", "superman");
			pu2 = new PlatformUserImpl(new PlatformUserIdentifier("1", "a"));
		} catch (UserExistsException e1) {}
		
		
		assertEquals("PlatformUser batman (1)", pu1.toString());
		assertEquals(1, us.getNbrOfPlatformUsers());

		pu2.setFirstName("Bruce");
		pu2.setLastName("Wayne");
		
		try {
			us.updatePlatformUser(pu2);
			fail("No exception rose");
		} catch (NoSuchUserException e) {}
	}
	
	@Test
	public void testDeletePlatformUser() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		try {
			pu1 = us.createPlatformUser("batman", "superman");
			pu2 = us.createPlatformUser("bilbo", "baggins");
		} catch (UserExistsException e) {}

		try {
			us.deletePlatformUser(pu1);
		} catch (NoSuchUserException e) {
			fail("Didn't find the user");
		}
		
		PlatformUser pu3 = us.getPlatformUser(pu1.getIdentifier());
		
		assertNull(pu3);
		assertEquals(1, us.getNbrOfPlatformUsers());
	}
	
	@Test
	public void testDeletePlatformUserNoSuchUser() {
		UserServiceImpl us = new UserServiceImpl();
		PlatformUser pu1 = new PlatformUserImpl();
		PlatformUser pu2 = new PlatformUserImpl();
		try {
			pu1 = us.createPlatformUser("batman", "superman");
			pu2 = us.createPlatformUser("bilbo", "baggins");
		} catch (UserExistsException e) {}

		try {
			us.deletePlatformUser(new PlatformUserImpl(new PlatformUserIdentifier(3, "george", "tarzan")));
			fail("Didn't delete the user");
		} catch (NoSuchUserException e) {
		}
		
		PlatformUser pu3 = us.getPlatformUser(pu1.getIdentifier());
		
		assertNotNull(pu3);
		assertEquals(2, us.getNbrOfPlatformUsers());
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
