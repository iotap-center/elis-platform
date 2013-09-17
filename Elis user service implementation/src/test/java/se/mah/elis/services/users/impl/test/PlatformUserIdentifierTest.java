package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.users.impl.PlatformUserIdentifier;
import se.mah.elis.services.users.impl.PlatformUserImpl;

public class PlatformUserIdentifierTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlainObject() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();

		assertEquals(0, p.getId());
		assertEquals("", p.getUsername());
		assertEquals("", p.getPassword());
	}
	
	@Test
	public void testSetId() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setId(17);

		assertEquals(17, p.getId());
	}
	
	@Test
	public void testSetIdZero() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		
		try {
			p.setId(0);
			fail("Didn't throw exception");
		} catch (IllegalArgumentException e) {
		}

		assertEquals(0, p.getId());
	}
	
	@Test
	public void testSetIdNegativeValue() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		
		try {
			p.setId(-1);
			fail("Didn't throw exception");
		} catch (IllegalArgumentException e) {
		}

		assertEquals(0, p.getId());
	}

	@Test
	public void testSetPassword() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setPassword("batman");
		
		assertEquals("batman", p.getPassword());
	}

	@Test
	public void testSetPasswordWithWhitespaces() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setPassword("  batman ");
		
		assertEquals("batman", p.getPassword());
	}

	@Test
	public void testSetPasswordNull() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		
		try {
			p.setPassword(null);
			fail("Should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
	}

	@Test
	public void testSetEmptyPassword() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setPassword("batman");
		
		assertEquals("batman", p.getPassword());
		try {
			p.setPassword("");
			fail("Should've triggered an exception.");
		} catch (IllegalArgumentException e) {}
		
		assertEquals("batman", p.getPassword());
	}

	@Test
	public void testSetBlankPassword() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setPassword("batman");
		
		assertEquals("batman", p.getPassword());
		try {
			p.setPassword(" ");
			fail("Should've triggered an exception.");
		} catch (IllegalArgumentException e) {}
		
		assertEquals("batman", p.getPassword());
	}

	@Test
	public void testSetPasswordAgain() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setPassword("batman");
		
		assertEquals("batman", p.getPassword());
		p.setPassword("superman");
		
		assertEquals("superman", p.getPassword());
	}

	@Test
	public void testSetUsername() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setUsername("batman");
		
		assertEquals("batman", p.getUsername());
	}

	@Test
	public void testSetUsernameWithWhitespaces() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setUsername(" batman   ");
		
		assertEquals("batman", p.getUsername());
	}

	@Test
	public void testSetUsernameNull() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		
		try {
			p.setUsername(null);
			fail("Should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
	}

	@Test
	public void testSetEmptyUsername() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setUsername("batman");
		
		assertEquals("batman", p.getUsername());
		try {
			p.setUsername("");
			fail("Should've triggered an exception.");
		} catch (IllegalArgumentException e) {}
		
		assertEquals("batman", p.getUsername());
	}

	@Test
	public void testSetBlankUsername() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setUsername("batman");
		
		assertEquals("batman", p.getUsername());
		try {
			p.setUsername("");
			fail("Should've triggered an exception.");
		} catch (IllegalArgumentException e) {}
		
		assertEquals("batman", p.getUsername());
	}

	@Test
	public void testSetUsernameAgain() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setUsername("batman");
		
		assertEquals("batman", p.getUsername());
		p.setUsername("superman");
		
		assertEquals("superman", p.getUsername());
	}

	@Test
	public void testExtendedConstructorWithSaneArgument() {
		PlatformUserIdentifier p = new PlatformUserIdentifier(13, "horses", "martians");

		assertEquals(13, p.getId());
		assertEquals("horses", p.getUsername());
		assertEquals("martians", p.getPassword());
	}

	@Test
	public void testExtendedConstructorWithEmptyPassword() {
		PlatformUserIdentifier p = null;
		
		try {
			p = new PlatformUserIdentifier(13, "batman", "");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testExtendedConstructorWithEmptyUsername() {
		PlatformUserIdentifier p = null;
		
		try {
			p = new PlatformUserIdentifier(13, "", "superman");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testExtendedConstructorWithNonPositiveId() {
		PlatformUserIdentifier p = null;
		
		try {
			p = new PlatformUserIdentifier(0, "batman", "superman");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testExtendedConstructorWithEmptyArguments() {
		PlatformUserIdentifier p = null;
		
		try {
			p = new PlatformUserIdentifier(0, "", "");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testConstructorWithSaneArgument() {
		PlatformUserIdentifier p = new PlatformUserIdentifier("horses", "martians");

		assertEquals("horses", p.getUsername());
		assertEquals("martians", p.getPassword());
	}

	@Test
	public void testConstructorWithEmptyPassword() {
		PlatformUserIdentifier p = null;
		
		try {
			p = new PlatformUserIdentifier("batman", "");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testConstructorWithEmptyUsername() {
		PlatformUserIdentifier p = null;
		
		try {
			p = new PlatformUserIdentifier("", "superman");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testConstructorWithEmptyArguments() {
		PlatformUserIdentifier p = null;
		
		try {
			p = new PlatformUserIdentifier("", "");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}
	
	@Test
	public void testIsEmptyValid() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setUsername("batman");
		p.setPassword("robin");
		
		assertFalse(p.isEmpty());
	}
	
	@Test
	public void testIsEmptyValidUsername() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		try {
			p.setUsername("batman");
			p.setPassword("");
		} catch (IllegalArgumentException e) {}
		
		assertTrue(p.isEmpty());
	}
	
	@Test
	public void testIsEmptyValidPassword() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		try {
			p.setUsername("");
			p.setPassword("robin");
		} catch (IllegalArgumentException e) {}
		
		assertTrue(p.isEmpty());
	}
	
	@Test
	public void testIsEmptyReallyEmpty() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		try {
			p.setUsername("");
			p.setPassword("");
		} catch (IllegalArgumentException e) {}
		
		assertTrue(p.isEmpty());
	}

	@Test
	public void testToString() {
		PlatformUserIdentifier p = new PlatformUserIdentifier(3, "batman", "superman");
		
		assertEquals("3: batman, superman", p.toString());
	}

	@Test
	public void testEqualsSameId() {
		PlatformUserIdentifier pu1 = new PlatformUserIdentifier(1, "batman", "superman");
		PlatformUserIdentifier pu2 = new PlatformUserIdentifier(1, "fred", "barney");
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsSameUsername() {
		PlatformUserIdentifier pu1 = new PlatformUserIdentifier("batman", "superman");
		PlatformUserIdentifier pu2 = new PlatformUserIdentifier("batman", "george");
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsSameUsernameAndId() {
		PlatformUserIdentifier pu1 = new PlatformUserIdentifier(1, "batman", "superman");
		PlatformUserIdentifier pu2 = new PlatformUserIdentifier(1, "batman", "superman");
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsDifferentStuff() {
		PlatformUserIdentifier pu1 = new PlatformUserIdentifier(1, "batman", "superman");
		PlatformUserIdentifier pu2 = new PlatformUserIdentifier(2, "fred", "barney");
		
		assertFalse(pu1.equals(pu2));
	}
}
