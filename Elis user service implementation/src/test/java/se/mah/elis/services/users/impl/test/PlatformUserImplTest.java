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
	public void testGetFirstName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setFirstName("Bruce");
		
		assertEquals("Bruce", pu.getFirstName());
		
	}
	
	@Test
	public void testGetFirstNameNoName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setFirstName("");
		
		assertEquals("", pu.getFirstName());
	}
	
	@Test
	public void testSetFirstName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setFirstName("Bruce");
		
		assertEquals("Bruce", pu.getFirstName());
	}
	
	@Test
	public void testSetFirstNameEmptyName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setFirstName("Bruce");
		pu.setFirstName("");
		
		assertEquals("", pu.getFirstName());
	}
	
	@Test
	public void testSetFirstNameNull() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setFirstName("Bruce");
		pu.setFirstName(null);
		
		assertEquals("", pu.getFirstName());
	}
	
	@Test
	public void testGetLastName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setLastName("Wayne");
		
		assertEquals("Wayne", pu.getLastName());
		
	}
	
	@Test
	public void testGetLastNameNoName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		
		assertEquals("", pu.getLastName());
	}
	
	@Test
	public void testSetLastName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setLastName("Wayne");
		
		assertEquals("Wayne", pu.getLastName());
	}
	
	@Test
	public void testSetLastNameNoName() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setLastName("Wayne");
		pu.setLastName("");
		
		assertEquals("", pu.getLastName());	
	}
	
	@Test
	public void testSetLastNameNull() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setLastName("Wayne");
		pu.setLastName(null);
		
		assertEquals("", pu.getLastName());	
	}
	
	@Test
	public void testGetEmail() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setEmail("batman@gotham.net");
		
		assertEquals("batman@gotham.net", pu.getEmail());
	}
	
	@Test
	public void testGetEmailNoAddress() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		
		assertEquals("", pu.getEmail());
	}
	
	@Test
	public void testSetEmail() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setEmail("batman@gotham.net");
		
		assertEquals("batman@gotham.net", pu.getEmail());
	}
	
	@Test
	public void testSetEmailNoAddress() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setEmail("batman@gotham.net");
		pu.setEmail("");
		
		assertEquals("", pu.getEmail());
	}
	
	@Test
	public void testSetEmailBadAddress() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setEmail("batman@gotham.net");
		pu.setEmail("batman@gotham");
		
		assertEquals("", pu.getEmail());
	}
	
	@Test
	public void testSetEmailNull() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifier("batman", "superman"));
		pu.setEmail("batman@gotham.net");
		pu.setEmail(null);
		
		assertEquals("", pu.getEmail());
	}

	@Test
	public void testToString() {
		PlatformUserImpl pu = new PlatformUserImpl();
		pu.setId(new PlatformUserIdentifier("batman", "superman"));
		
		assertEquals("PlatformUser batman", pu.toString());
	}

}
