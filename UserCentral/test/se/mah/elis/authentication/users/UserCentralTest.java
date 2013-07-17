package se.mah.elis.authentication.users;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.authentication.users.User;
import se.mah.elis.authentication.users.UserCentral;
import se.mah.elis.authentication.users.impl.UserCentralImpl;

public class UserCentralTest {

	private UserCentral userCentral;
	private User user;
	
	@Before
	public void setUp() {
		userCentral = new UserCentralImpl();
		user = new User();
		user.firstName = "Demo";
		user.lastName = "Demosson";
		user.userName = "demoMan";
	}
	
	@Test
	public void testAddUser() {
		User addedUser = userCentral.addUser(user);
		assertTrue(addedUser.equals(user));
	}

	@Test
	public void testRemoveUser() {
		userCentral.addUser(user);
		assertTrue(userCentral.getUser(user.userName).equals(user));
		userCentral.removeUser(user.userName);
		assertNull(userCentral.getUser(user.userName));
	}

	@Test
	public void testLinkUserToSystem() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnlinkUserToSystem() {
		fail("Not yet implemented");
	}

}
