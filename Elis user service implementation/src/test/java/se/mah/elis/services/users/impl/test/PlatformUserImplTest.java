package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.impl.PlatformUserIdentifier;
import se.mah.elis.services.users.impl.PlatformUserImpl;

public class PlatformUserImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlatformUserImpl() {
		PlatformUserImpl pu = new PlatformUserImpl();
		
		assertNotNull(pu);
	}

	@Test
	public void testPlatformUserImplUserIdentifier() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		
		assertNotNull(pu);
		assertEquals("batman", ((PlatformUserIdentifier) pu.getId()).getUsername());
		assertEquals("superman", ((PlatformUserIdentifier) pu.getId()).getPassword());
	}

	@Test
	public void testGetId() {
		PlatformUserImpl pu = new PlatformUserImpl();

		assertEquals("username", ((PlatformUserIdentifier) pu.getId()).getUsername());
		assertEquals("password", ((PlatformUserIdentifier) pu.getId()).getPassword());
	}

	@Test
	public void testSetId() {
		PlatformUserImpl pu = new PlatformUserImpl();
		
		assertEquals("username", ((PlatformUserIdentifier) pu.getId()).getUsername());
		
		pu.setId(new PlatformUserIdentifier("batman", "superman"));
		assertEquals("batman", ((PlatformUserIdentifier) pu.getId()).getUsername());
	}

	@Test
	public void testSetIdWithNull() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		
		assertEquals("batman", ((PlatformUserIdentifier) pu.getId()).getUsername());
		
		pu.setId(null);
		assertNotNull(pu.getId());
		assertEquals("username", ((PlatformUserIdentifier) pu.getId()).getUsername());
	}

	@Test
	public void testToString() {
		PlatformUserImpl pu = new PlatformUserImpl();
		pu.setId(new PlatformUserIdentifier("batman", "superman"));
		
		assertEquals("PlatformUser batman", pu.toString());
	}

}
