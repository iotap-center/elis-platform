package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.impl.PlatformUserIdentifierImpl;
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
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		
		assertNotNull(pu);
		assertEquals("batman", ((PlatformUserIdentifierImpl) pu.getIdentifier()).getUsername());
		assertEquals("superman", ((PlatformUserIdentifierImpl) pu.getIdentifier()).getPassword());
	}

	@Test
	public void testGetId() {
		PlatformUserImpl pu = new PlatformUserImpl();

		assertEquals("", ((PlatformUserIdentifierImpl) pu.getIdentifier()).getUsername());
		assertEquals("", ((PlatformUserIdentifierImpl) pu.getIdentifier()).getPassword());
	}

	@Test
	public void testSetId() {
		PlatformUserImpl pu = new PlatformUserImpl();
		
		assertEquals("", ((PlatformUserIdentifierImpl) pu.getIdentifier()).getUsername());
		
		pu.setIdentifier(new PlatformUserIdentifierImpl("batman", "superman"));
		assertEquals("batman", ((PlatformUserIdentifierImpl) pu.getIdentifier()).getUsername());
	}

	@Test
	public void testSetIdWithNull() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		
		assertEquals("batman", ((PlatformUserIdentifierImpl) pu.getIdentifier()).getUsername());
		
		pu.setIdentifier(null);
		assertNotNull(pu.getIdentifier());
		assertEquals("", ((PlatformUserIdentifierImpl) pu.getIdentifier()).getUsername());
	}
	
	@Test
	public void testGetFirstName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setFirstName("Bruce");
		
		assertEquals("Bruce", pu.getFirstName());
		
	}
	
	@Test
	public void testGetFirstNameNoName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setFirstName("");
		
		assertEquals("", pu.getFirstName());
	}
	
	@Test
	public void testSetFirstName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setFirstName("Bruce");
		
		assertEquals("Bruce", pu.getFirstName());
	}
	
	@Test
	public void testSetFirstNameEmptyName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setFirstName("Bruce");
		pu.setFirstName("");
		
		assertEquals("", pu.getFirstName());
	}
	
	@Test
	public void testSetFirstNameNull() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setFirstName("Bruce");
		pu.setFirstName(null);
		
		assertEquals("", pu.getFirstName());
	}
	
	@Test
	public void testGetLastName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setLastName("Wayne");
		
		assertEquals("Wayne", pu.getLastName());
		
	}
	
	@Test
	public void testGetLastNameNoName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		
		assertEquals("", pu.getLastName());
	}
	
	@Test
	public void testSetLastName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setLastName("Wayne");
		
		assertEquals("Wayne", pu.getLastName());
	}
	
	@Test
	public void testSetLastNameNoName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setLastName("Wayne");
		pu.setLastName("");
		
		assertEquals("", pu.getLastName());	
	}
	
	@Test
	public void testSetLastNameNull() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setLastName("Wayne");
		pu.setLastName(null);
		
		assertEquals("", pu.getLastName());	
	}
	
	@Test
	public void testGetEmail() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setEmail("batman@gotham.net");
		
		assertEquals("batman@gotham.net", pu.getEmail());
	}
	
	@Test
	public void testGetEmailNoAddress() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		
		assertEquals("", pu.getEmail());
	}
	
	@Test
	public void testSetEmail() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setEmail("batman@gotham.net");
		
		assertEquals("batman@gotham.net", pu.getEmail());
	}
	
	@Test
	public void testSetEmailNoAddress() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setEmail("batman@gotham.net");
		pu.setEmail("");
		
		assertEquals("", pu.getEmail());
	}
	
	@Test
	public void testSetEmailBadAddress() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setEmail("batman@gotham.net");
		pu.setEmail("batman@gotham");
		
		assertEquals("", pu.getEmail());
	}
	
	@Test
	public void testSetEmailNull() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		pu.setEmail("batman@gotham.net");
		pu.setEmail(null);
		
		assertEquals("", pu.getEmail());
	}

	@Test
	public void testToString() {
		PlatformUserImpl pu = new PlatformUserImpl();
		pu.setIdentifier(new PlatformUserIdentifierImpl(1, "batman", "superman"));
		
		assertEquals("PlatformUser batman (1)", pu.toString());
	}

	@Test
	public void testEqualsSameId() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "fred", "barney"));
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsSameName() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl(2, "batman", "superman"));
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsSameNameAndId() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "batman", "superman"));
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsDifferentStuff() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl(2, "fred", "barney"));
		
		assertFalse(pu1.equals(pu2));
	}
	
	@Test
	public void testCompareToABeforeB() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl(2, "fred", "barney"));
		
		assertEquals(-1, pu1.compareTo(pu2));
	}
	
	@Test
	public void testCompareToAAfterB() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl(2, "fred", "barney"));
		
		assertEquals(1, pu2.compareTo(pu1));
	}
	
	@Test
	public void testCompareToAEqualToB() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl(1, "fred", "barney"));
		
		assertEquals(0, pu1.compareTo(pu2));
	}
}
