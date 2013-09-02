package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.users.impl.PlatformUserIdentifier;

public class PlatformUserIdentifierTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetId() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();

		assertEquals("username", p.getUsername());
		assertEquals("password", p.getPassword());
	}

	@Test
	public void testSetPassword() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setPassword("batman");
		
		assertEquals("batman", p.getPassword());
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
	public void testSetUsernameAgain() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setUsername("batman");
		
		assertEquals("batman", p.getUsername());
		p.setUsername("superman");
		
		assertEquals("superman", p.getUsername());
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
	public void testToString() {
		PlatformUserIdentifier p = new PlatformUserIdentifier("batman", "superman");
		
		assertEquals("batman, superman", p.toString());
	}
}
