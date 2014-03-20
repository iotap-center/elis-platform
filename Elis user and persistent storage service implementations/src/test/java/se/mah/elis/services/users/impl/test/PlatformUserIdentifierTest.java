package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import java.util.Properties;

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

		assertEquals("", p.getUsername());
		assertEquals("", p.getPassword());
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
		} catch (IllegalArgumentException e) {
			fail("Shouldn't throw an IllegalArgumentException");
		}
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
	public void testExtendedConstructorWithSaneArguments() {
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl("horses", "martians");

		assertEquals("horses", p.getUsername());
		assertEquals("martians", p.getPassword());
	}

	@Test
	public void testExtendedConstructorWithEmptyPassword() {
		PlatformUserIdentifierImpl p = null;
		
		try {
			p = new PlatformUserIdentifierImpl("batman", "");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testExtendedConstructorWithEmptyUsername() {
		PlatformUserIdentifierImpl p = null;
		
		try {
			p = new PlatformUserIdentifierImpl("", "superman");
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testExtendedConstructorWithEmptyArguments() {
		PlatformUserIdentifierImpl p = null;
		
		try {
			p = new PlatformUserIdentifierImpl("", "");
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
		PlatformUserIdentifierImpl p = new PlatformUserIdentifierImpl("batman", "superman");
		
		assertEquals("PlatformUserIdentifier: batman", p.toString());
	}

	@Test
	public void testEqualsSameUsername() {
		PlatformUserIdentifierImpl pu1 = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserIdentifierImpl pu2 = new PlatformUserIdentifierImpl("batman", "george");
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsDifferentStuff() {
		PlatformUserIdentifierImpl pu1 = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserIdentifierImpl pu2 = new PlatformUserIdentifierImpl("fred", "barney");
		
		assertFalse(pu1.equals(pu2));
	}
	
	@Test
	public void testIdentifies() {
		PlatformUserIdentifierImpl pu = new PlatformUserIdentifierImpl("batman", "superman");
		Class expected = se.mah.elis.services.users.PlatformUser.class;
		Class actual = pu.identifies();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetProperties() {
		PlatformUserIdentifierImpl pu = new PlatformUserIdentifierImpl("batman", "superman");
		Properties expected = new Properties();
		Properties actual = pu.getProperties();
		
		expected.put("username", "batman");
		expected.put("password", "superman");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPropertiesEmtpyUsernameAndPassword() {
		PlatformUserIdentifierImpl pu = new PlatformUserIdentifierImpl();
		Properties expected = new Properties();
		Properties actual = pu.getProperties();
		
		expected.put("username", "");
		expected.put("password", "");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPropertiesNoId() {
		PlatformUserIdentifierImpl pu = new PlatformUserIdentifierImpl("batman", "superman");
		Properties expected = new Properties();
		Properties actual = pu.getProperties();
		
		expected.put("username", "batman");
		expected.put("password", "superman");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void getGetPropertiesTemplate() {
		PlatformUserIdentifierImpl pu = new PlatformUserIdentifierImpl("batman", "superman");
		Properties expected = new Properties();
		Properties actual = pu.getPropertiesTemplate();
		
		expected.put("username", "256");
		expected.put("password", "256");
		
		assertEquals(expected, actual);
	}
}
