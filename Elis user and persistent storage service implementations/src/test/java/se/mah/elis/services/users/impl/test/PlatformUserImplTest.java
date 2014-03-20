package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.impl.services.users.PlatformUserIdentifierImpl;
import se.mah.elis.impl.services.users.PlatformUserImpl;
import se.mah.elis.services.users.UserIdentifier;

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
	public void testSetUserId() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		UUID uuid = UUID.randomUUID();
		
		try {
			pu.setUserId(uuid);
		} catch (Exception e) {
			fail("This shouldn't happen");
		}
	}
	
	@Test
	public void testSetUserIdNull() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		UUID uuid = UUID.randomUUID();
		
		try {
			pu.setUserId(uuid);
		} catch (Exception e) {
			fail("This shouldn't happen");
		}
	}
	
	@Test
	public void testGetUserId() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		UUID expected = UUID.randomUUID();
		UUID actual = null;
		
		pu.setUserId(expected);
		actual = pu.getUserId();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSetUserIdNotSet() {
		PlatformUserImpl pu = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		UUID actual = null;
		
		actual = pu.getUserId();
		
		assertNull(actual);
	}

	@Test
	public void testGetIdentifier() {
		PlatformUserImpl pu = new PlatformUserImpl();

		assertEquals("", ((PlatformUserIdentifierImpl) pu.getIdentifier()).getUsername());
		assertEquals("", ((PlatformUserIdentifierImpl) pu.getIdentifier()).getPassword());
	}

	@Test
	public void testSetIdentifier() {
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
		pu.setIdentifier(new PlatformUserIdentifierImpl("batman", "superman"));
		
		assertEquals("PlatformUser batman (null)", pu.toString());
	}

	@Test
	public void testEqualsSameId() {
		UUID uuid = UUID.randomUUID();
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl("fred", "barney"));
		
		pu1.setUserId(uuid);
		pu2.setUserId(uuid);
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsSameName() {
		UUID uuid = UUID.randomUUID();
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		
		pu1.setUserId(uuid);
		pu2.setUserId(uuid);
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsSameNameAndId() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsDifferentStuff() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl("fred", "barney"));
		
		assertFalse(pu1.equals(pu2));
	}
	
	@Test
	public void testCompareToABeforeB() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl("fred", "barney"));
		
		assertTrue(pu2.compareTo(pu1) < 0);
	}
	
	@Test
	public void testCompareToAAfterB() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		PlatformUserImpl pu2 = new PlatformUserImpl(new PlatformUserIdentifierImpl("fred", "barney"));
		
		assertTrue(pu1.compareTo(pu2) > 0);
	}
	
	@Test
	public void testCompareToAEqualToB() {
		PlatformUserImpl pu1 = new PlatformUserImpl(new PlatformUserIdentifierImpl("batman", "superman"));
		PlatformUserImpl pu2 = pu1;
		
		pu2.setIdentifier(new PlatformUserIdentifierImpl("fred", "barney"));
		
		assertEquals(0, pu1.compareTo(pu2));
	}
	
	@Test
	public void testGetServiceName() {
		PlatformUserImpl pu = new PlatformUserImpl();
		String expected = "se.mah.elis.services.users.PlatformUser";
		String actual = pu.getServiceName();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreated() {
		PlatformUserImpl pu = new PlatformUserImpl();
		DateTime now = DateTime.now();
		
		assertNotNull(pu.created());
		assertFalse(now.isBefore(pu.created().getMillis()));
	}
	
	@Test
	public void testGetPropertiesTemplate() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl(puid);
		Properties expected = new OrderedProperties();
		Properties actual = pu.getPropertiesTemplate();
		
		expected.put("username", "256");
		expected.put("password", "256");
		expected.put("first_name", "32");
		expected.put("last_name", "32");
		expected.put("email", "256");
		
		assertNotNull(actual.get("uuid"));
		assertTrue(actual.get("uuid") instanceof UUID);
		actual.remove("uuid");
		
		assertNotNull(actual.get("created"));
		assertTrue(actual.get("created") instanceof DateTime);
		actual.remove("created");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetProperties() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl(puid);
		Properties expected = new Properties();
		Properties actual = null;
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
		
		expected.putAll(puid.getProperties());
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");

		pu.setFirstName("Bruce");
		pu.setLastName("Wayne");
		pu.setEmail("batman@gotham.gov");
		
		actual = pu.getProperties();
		
		assertNotNull(pu.getUserId());
		actual.remove("uuid");
		
		assertNotNull(pu.created());
		assertFalse(DateTime.now().isBefore(((DateTime) actual.get("created")).getMillis()));
		actual.remove("created");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPropertiesEmptyIdentifier() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl();
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties expected = new Properties();
		Properties actual = null;
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
		
		expected.putAll(puid.getProperties());
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");

		pu.setFirstName("Bruce");
		pu.setLastName("Wayne");
		pu.setEmail("batman@gotham.gov");
		
		actual = pu.getProperties();
		
		assertNotNull(pu.getUserId());
		actual.remove("uuid");
		
		assertNotNull(pu.created());
		assertFalse(DateTime.now().isBefore((DateTime) actual.get("created")));
		actual.remove("created");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPropertiesEmptyFirstName() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl(puid);
		Properties expected = new Properties();
		Properties actual = null;
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
		
		expected.putAll(puid.getProperties());
		expected.put("first_name", "");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");

		pu.setLastName("Wayne");
		pu.setEmail("batman@gotham.gov");
		
		actual = pu.getProperties();
		
		assertNotNull(pu.getUserId());
		actual.remove("uuid");
		
		assertNotNull(pu.created());
		assertFalse(DateTime.now().isBefore((DateTime) actual.get("created")));
		actual.remove("created");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPropertiesEmptyLastName() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl(puid);
		Properties expected = new Properties();
		Properties actual = null;
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
		
		expected.putAll(puid.getProperties());
		expected.put("first_name", "Bruce");
		expected.put("last_name", "");
		expected.put("email", "batman@gotham.gov");

		pu.setFirstName("Bruce");
		pu.setEmail("batman@gotham.gov");
		
		actual = pu.getProperties();
		
		assertNotNull(pu.getUserId());
		actual.remove("uuid");
		
		assertNotNull(pu.created());
		assertFalse(DateTime.now().isBefore((DateTime) actual.get("created")));
		actual.remove("created");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPropertiesEmptyEmail() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl(puid);
		Properties expected = new Properties();
		Properties actual = null;
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
		
		expected.putAll(puid.getProperties());
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "");

		pu.setFirstName("Bruce");
		pu.setLastName("Wayne");
		
		actual = pu.getProperties();
		
		assertNotNull(pu.getUserId());
		actual.remove("uuid");
		
		assertNotNull(pu.created());
		assertFalse(DateTime.now().isBefore((DateTime) actual.get("created")));
		actual.remove("created");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulate() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		Properties expected = new Properties();
		Properties actual = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.randomUUID();
		
		props.put("uuid", uuid);
		props.put("identifier", puid);
		props.put("first_name", "Bruce");
		props.put("last_name", "Wayne");
		props.put("email", "batman@gotham.gov");
		props.put("created", now);
		
		pu.populate(props);
		
		expected.put("uuid", uuid);
		expected.putAll(puid.getProperties());
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateFlatIdentifier() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		Properties expected = new Properties();
		Properties actual = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.randomUUID();

		props.put("uuid", uuid);
		props.put("username", "batman");
		props.put("password", "superman");
		props.put("first_name", "Bruce");
		props.put("last_name", "Wayne");
		props.put("email", "batman@gotham.gov");
		props.put("created", now);
		
		pu.populate(props);
		
		expected.put("uuid", uuid);
		expected.putAll(puid.getProperties());
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateNoId() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		Properties expected = new Properties();
		Properties actual = null;
		DateTime now = DateTime.now();

		props.put("username", "batman");
		props.put("password", "superman");
		props.put("first_name", "Bruce");
		props.put("last_name", "Wayne");
		props.put("email", "batman@gotham.gov");
		props.put("created", now);
		
		try {
			pu.populate(props);
		} catch (IllegalArgumentException e) {
			fail("This shouldn't happen.");
		}
		
		expected.putAll(puid.getProperties());
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateMissingIdentifier() {
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		DateTime now = DateTime.now();

		props.put("first_name", "Bruce");
		props.put("last_name", "Wayne");
		props.put("email", "batman@gotham.gov");
		props.put("created", now);
		
		try {
			pu.populate(props);
			fail("Shouldn't get this far.");
		} catch (IllegalArgumentException e) {
			// This should happen.
		}
	}
	
	@Test
	public void testPopulateMissingFirstName() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		Properties expected = new Properties();
		Properties actual = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.randomUUID();

		props.put("uuid", uuid);
		props.putAll(puid.getProperties());
		props.put("last_name", "Wayne");
		props.put("email", "batman@gotham.gov");
		props.put("created", now);
		
		pu.populate(props);
		
		expected.put("uuid", uuid);
		expected.putAll(puid.getProperties());
		expected.put("first_name", "");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateMissingLastName() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		Properties expected = new Properties();
		Properties actual = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.randomUUID();

		props.put("uuid", uuid);
		props.putAll(puid.getProperties());
		props.put("first_name", "Bruce");
		props.put("email", "batman@gotham.gov");
		props.put("created", now);
		
		pu.populate(props);
		
		expected.put("uuid", uuid);
		expected.putAll(puid.getProperties());
		expected.put("first_name", "Bruce");
		expected.put("last_name", "");
		expected.put("email", "batman@gotham.gov");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateMissingEmail() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		Properties expected = new Properties();
		Properties actual = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.randomUUID();

		props.put("uuid", uuid);
		props.putAll(puid.getProperties());
		props.put("first_name", "Bruce");
		props.put("last_name", "Wayne");
		props.put("created", now);
		
		pu.populate(props);
		
		expected.put("uuid", uuid);
		expected.putAll(puid.getProperties());
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateMissingCreated() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		DateTime now = DateTime.now();
		UUID uuid = UUID.randomUUID();
		
		props.put("uuid", uuid);
		props.put("identifier", puid);
		props.put("first_name", "Bruce");
		props.put("last_name", "Wayne");
		props.put("email", "batman@gotham.gov");
		
		try {
			pu.populate(props);
			fail("Shouldn't get this far.");
		} catch (IllegalArgumentException e) {
			// This should happen.
		}
	}
	
	@Test
	public void testPopulateExcessiveProperties() {
		PlatformUserIdentifierImpl puid = new PlatformUserIdentifierImpl("batman", "superman");
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		Properties expected = new Properties();
		Properties actual = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.randomUUID();
		
		props.put("uuid", uuid);
		props.putAll(puid.getProperties());
		props.put("first_name", "Bruce");
		props.put("last_name", "Wayne");
		props.put("email", "batman@gotham.gov");
		props.put("horses", "Sleipnir, Shadowfax, Brunte");
		props.put("created", now);
		
		pu.populate(props);
		
		expected.put("uuid", uuid);
		expected.putAll(puid.getProperties());
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");
		
		actual = pu.getProperties();
		
		assertNotNull(pu.created());
		assertFalse(DateTime.now().isBefore(((DateTime) actual.get("created")).getMillis()));
		actual.remove("created");
		
		assertEquals(expected, actual);
	}
}
