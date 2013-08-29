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
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier(13));
		
		assertNotNull(pu);
		assertEquals(13, ((PlatformUserIdentifier) pu.getId()).getId());
	}

	@Test
	public void testGetId() {
		PlatformUserImpl pu = new PlatformUserImpl();
		
		assertEquals(0, ((PlatformUserIdentifier) pu.getId()).getId());
	}

	@Test
	public void testSetId() {
		PlatformUserImpl pu = new PlatformUserImpl();
		
		assertEquals(0, ((PlatformUserIdentifier) pu.getId()).getId());
		
		pu.setId(new PlatformUserIdentifier(13));
		assertEquals(13, ((PlatformUserIdentifier) pu.getId()).getId());
	}

	@Test
	public void testSetIdWithNull() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier(11));
		
		assertEquals(11, ((PlatformUserIdentifier) pu.getId()).getId());
		
		pu.setId(null);
		assertNotNull(pu.getId());
		assertEquals(0, ((PlatformUserIdentifier) pu.getId()).getId());
	}

	@Test
	public void testToString() {
		PlatformUserImpl pu = new PlatformUserImpl();
		pu.setId(new PlatformUserIdentifier(13));
		
		assertEquals("PlatformUser 13", pu.toString());
	}

}
