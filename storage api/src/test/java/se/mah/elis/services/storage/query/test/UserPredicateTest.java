package se.mah.elis.services.storage.query.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.UserPredicate;
import se.mah.elis.services.storage.query.test.mock.MockPlatformUser;
import se.mah.elis.services.storage.query.test.mock.MockTranslator;
import se.mah.elis.services.storage.query.test.mock.MockUser;
import se.mah.elis.services.storage.query.test.mock.MockUserIdentifier;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;

public class UserPredicateTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUserPredicate() {
		UserPredicate up = new UserPredicate();
		String actual = up.toString();
		String expected = "UserPredicate:\n" +
						  "  Translator: null\n" +
						  "  User: null";
		
		assertEquals(expected, actual);
	}

	@Test
	public void testUserPredicateUserIdentifier() {
		MockUserIdentifier id = new MockUserIdentifier();
		UserPredicate up = new UserPredicate(id);
		String actual = up.toString();
		String expected = "UserPredicate:\n" +
						  "  Translator: null\n" +
						  "  User: {id_number=42}";
		
		assertEquals(expected, actual);
	}

	@Test
	public void testUserPredicateUser() {
		User user = new MockUser();
		UserPredicate up = new UserPredicate(user);
		String actual = up.toString();
		String expected = "UserPredicate:\n" +
						  "  Translator: null\n" +
						  "  User: {id_number=42}";
		
		assertEquals(expected, actual);
	}

	@Test
	public void testUserPredicatePlatformUser() {
		PlatformUser user = new MockPlatformUser();
		UserPredicate up = new UserPredicate(user);
		String actual = up.toString();
		String expected = "UserPredicate:\n" +
						  "  Translator: null\n" +
						  "  User: {id_number=42}";
		
		assertEquals(expected, actual);
	}

	@Test
	public void testSetUserUserIdentifier() {
		MockUserIdentifier id = new MockUserIdentifier();
		UserPredicate up = new UserPredicate();
		up.setUser(id);
		String actual = up.toString();
		String expected = "UserPredicate:\n" +
						  "  Translator: null\n" +
						  "  User: {id_number=42}";
		
		assertEquals(expected, actual);
	}

	@Test
	public void testSetUserUser() {
		User user = new MockUser();
		UserPredicate up = new UserPredicate();
		up.setUser(user);
		String actual = up.toString();
		String expected = "UserPredicate:\n" +
						  "  Translator: null\n" +
						  "  User: {id_number=42}";
		
		assertEquals(expected, actual);
	}

	@Test
	public void testSetUserPlatformUser() {
		PlatformUser user = new MockPlatformUser();
		UserPredicate up = new UserPredicate();
		up.setUser(user);
		String actual = up.toString();
		String expected = "UserPredicate:\n" +
						  "  Translator: null\n" +
						  "  User: {id_number=42}";
		
		assertEquals(expected, actual);
	}

	@Test
	public void testSetTranslator() {
		UserPredicate up = new UserPredicate();
		up.setTranslator(new MockTranslator());
		String actual = up.toString();
		String expected = "UserPredicate:\n" +
						  "  Translator: " + MockTranslator.class.getName() + "\n" +
						  "  User: null";
		
		assertEquals(expected, actual);
	}

	@Test
	public void testCompile() {
		UserPredicate up = new UserPredicate(new MockUserIdentifier());
		up.setTranslator(new MockTranslator());
		String actual = null;
		String expected = " user: " + MockUserIdentifier.class.getSimpleName() +
						  ": id_number: 42";
		
		try {
			actual = up.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}

	@Test
	public void testCompileNoTranslator() {
		UserPredicate up = new UserPredicate(new MockUserIdentifier());
		
		try {
			up.compile();
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testCompileNoUser() {
		UserPredicate up = new UserPredicate();
		up.setTranslator(new MockTranslator());;
		
		try {
			up.compile();
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

}
