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
		
		assertEquals(0, p.getId());
	}

	@Test
	public void testSetId() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setId(13);
		
		assertEquals(13, p.getId());
	}

	@Test
	public void testSetIdToZero() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setId(13);
		
		assertEquals(13, p.getId());
		p.setId(0);
		
		assertEquals(0, p.getId());
	}

	@Test
	public void testSetIdAgain() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setId(13);
		
		assertEquals(13, p.getId());
		p.setId(11);
		
		assertEquals(11, p.getId());
	}

	@Test
	public void testSetIdNegative() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		try {
			p.setId(-3);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(0, p.getId());
		}
	}

	@Test
	public void testSetIdReplaceSaneWithNegative() {
		PlatformUserIdentifier p = new PlatformUserIdentifier();
		p.setId(17);
		
		assertEquals(17, p.getId());
		
		try {
			p.setId(-3);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(17, p.getId());
		}
	}

	@Test
	public void testConstructorWithSaneArgument() {
		PlatformUserIdentifier p = new PlatformUserIdentifier(17);
		
		assertEquals(17, p.getId());
	}

	@Test
	public void testConstructorWithNegativeArgument() {
		PlatformUserIdentifier p = null;
		
		try {
			p = new PlatformUserIdentifier(-1);
		} catch (IllegalArgumentException e) {
			assertNull(p);
		}
	}

	@Test
	public void testToString() {
		PlatformUserIdentifier p = new PlatformUserIdentifier(17);
		
		assertEquals("17", p.toString());
	}
}
