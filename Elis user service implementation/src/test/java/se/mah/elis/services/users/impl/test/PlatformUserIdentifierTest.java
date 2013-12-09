package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.impl.services.users.PlatformUserIdentifierImpl;
import se.mah.elis.impl.services.users.PlatformUserImpl;

public class PlatformUserIdentifierTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlainObject() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();

		assertEquals(0, p.getId());
		assertEquals("", p.getUsername());
		assertEquals("", p.getPassword());
	}
	
	@Test
	public void testSetId() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		p.setId(17);

		assertEquals(17, p.getId());
	}
	
	@Test
	public void testSetIdZero() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		
		try {
			p.setId(0);
			fail("Didn't throw exception");
		} catch (IllegalArgumentException e) {
		}

		assertEquals(0, p.getId());
	}
	
	@Test
	public void testSetIdNegativeValue() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		
		try {
			p.setId(-1);
			fail("Didn't throw exception");
		} catch (IllegalArgumentException e) {
		}

		assertEquals(0, p.getId());
	}

	@Test
	public void testSetPassword() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		p.setPassword("batman");
		
		assertEquals("batman", p.getPassword());
	}

	@Test
	public void testSetPasswordWithWhitespaces() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		p.setPassword("  batman ");
		
		assertEquals("batman", p.getPassword());
	}

	@Test
	public void testSetPasswordNull() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		
		try {
			p.setPassword(null);
			fail("Should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
	}

	@Test
	public void testSetEmptyPassword() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
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
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
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
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		p.setPassword("batman");
		
		assertEquals("batman", p.getPassword());
		p.setPassword("superman");
		
		assertEquals("superman", p.getPassword());
	}

	@Test
	public void testSetUsername() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		p.setUsername("batman");
		
		assertEquals("batman", p.getUsername());
	}

	@Test
	public void testSetUsernameWithWhitespaces() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		p.setUsername(" batman   ");
		
		assertEquals("batman", p.getUsername());
	}

	@Test
	public void testSetUsernameNull() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		
		try {
			p.setUsername(null);
			fail("Should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
	}

	@Test
	public void testSetEmptyUsername() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
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
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
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
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		p.setUsername("batman");
		
		assertEquals("batman", p.getUsername());
		p.setUsername("superman");
		
		assertEquals("superman", p.getUsername());
	}

	@Test
	public void testExtendedConstructorWithSaneArgument() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl(13, "horses", "martians");

		assertEquals(13, p.getId());
		assertEquals("horses", p.getUsername());
		assertEquals("martians", p.getPassword());
	}

	@Test
	public void testExtendedConstructorWithEmptyPassword() {
		PlatformUserIdentifierImpl p = null;
		
		try {
			p = new PlatformUserIdentifierImpl(13, "batman", "");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testExtendedConstructorWithEmptyUsername() {
		PlatformUserIdentifierImpl p = null;
		
		try {
			p = new PlatformUserIdentifierImpl(13, "", "superman");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testExtendedConstructorWithNonPositiveId() {
		PlatformUserIdentifierImpl p = null;
		
		try {
			p = new PlatformUserIdentifierImpl(0, "batman", "superman");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testExtendedConstructorWithEmptyArguments() {
		PlatformUserIdentifierImpl p = null;
		
		try {
			p = new PlatformUserIdentifierImpl(0, "", "");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testConstructorWithSaneArgument() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl("horses", "martians");

		assertEquals("horses", p.getUsername());
		assertEquals("martians", p.getPassword());
	}

	@Test
	public void testConstructorWithEmptyPassword() {
		PlatformUserIdentifierImpl p = null;
		
		try {
			p = new PlatformUserIdentifierImpl("batman", "");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testConstructorWithEmptyUsername() {
		PlatformUserIdentifierImpl p = null;
		
		try {
			p = new PlatformUserIdentifierImpl("", "superman");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testConstructorWithEmptyArguments() {
		PlatformUserIdentifierImpl p = null;
		
		try {
			p = new PlatformUserIdentifierImpl("", "");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}
	
	@Test
	public void testIsEmptyValid() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		p.setUsername("batman");
		p.setPassword("robin");
		
		assertFalse(p.isEmpty());
	}
	
	@Test
	public void testIsEmptyValidUsername() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		try {
			p.setUsername("batman");
			p.setPassword("");
		} catch (IllegalArgumentException e) {}
		
		assertTrue(p.isEmpty());
	}
	
	@Test
	public void testIsEmptyValidPassword() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		try {
			p.setUsername("");
			p.setPassword("robin");
		} catch (IllegalArgumentException e) {}
		
		assertTrue(p.isEmpty());
	}
	
	@Test
	public void testIsEmptyReallyEmpty() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl();
		try {
			p.setUsername("");
			p.setPassword("");
		} catch (IllegalArgumentException e) {}
		
		assertTrue(p.isEmpty());
	}

	@Test
	public void testToString() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl(3, "batman", "superman");
		
		assertEquals("3: batman, superman", p.toString());
	}

	@Test
	public void testEqualsSameId() {
		PlatformUserIdentifierImpl pu1 = new PlatformUserIdentifierImpl(1, "batman", "superman");
		PlatformUserIdentifierImpl pu2 = new PlatformUserIdentifierImpl(1, "fred", "barney");
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsSameUsername() {
		PlatformUserIdentifierImpl pu1 = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserIdentifierImpl pu2 = new PlatformUserIdentifierImpl("batman", "george");
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsSameUsernameAndId() {
		PlatformUserIdentifierImpl pu1 = new PlatformUserIdentifierImpl(1, "batman", "superman");
		PlatformUserIdentifierImpl pu2 = new PlatformUserIdentifierImpl(1, "batman", "superman");
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsDifferentStuff() {
		PlatformUserIdentifierImpl pu1 = new PlatformUserIdentifierImpl(1, "batman", "superman");
		PlatformUserIdentifierImpl pu2 = new PlatformUserIdentifierImpl(2, "fred", "barney");
		
		assertFalse(pu1.equals(pu2));
	}
}
