package se.mah.elis.services.users.impl.test;

import static org.junit.Assert.*;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.impl.services.users.PlatformUserImpl;

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
	public void testSetUserId() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		UUID uuid = UUID.randomUUID();
		
		try {
			pu.setUserId(uuid);
		} catch (Exception e) {
			fail("This shouldn't happen");
		}
	}
	
	@Test
	public void testSetUserIdNull() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		UUID uuid = UUID.randomUUID();
		
		try {
			pu.setUserId(uuid);
		} catch (Exception e) {
			fail("This shouldn't happen");
		}
	}
	
	@Test
	public void testGetUserId() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		UUID expected = UUID.randomUUID();
		UUID actual = null;
		
		pu.setUserId(expected);
		actual = pu.getUserId();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSetUserIdNotSet() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		UUID actual = null;
		
		actual = pu.getUserId();
		
		assertNull(actual);
	}

	@Test
	public void testSetPassword() {
		PlatformUserImpl p = new PlatformUserImpl();
		p.setPassword("batman");
		
		assertEquals("batman", p.getPassword());
	}

	@Test
	public void testSetPasswordWithWhitespaces() {
		PlatformUserImpl p = new PlatformUserImpl();
		p.setPassword("  batman ");
		
		assertEquals("batman", p.getPassword());
	}

	@Test
	public void testSetPasswordNull() {
		PlatformUserImpl p = new PlatformUserImpl();
		
		try {
			p.setPassword(null);
		} catch (IllegalArgumentException e) {
			fail("Shouldn't throw an IllegalArgumentException");
		}
	}

	@Test
	public void testSetEmptyPassword() {
		PlatformUserImpl p = new PlatformUserImpl();
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
		PlatformUserImpl p = new PlatformUserImpl();
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
		PlatformUserImpl p = new PlatformUserImpl();
		p.setPassword("batman");
		
		assertEquals("batman", p.getPassword());
		p.setPassword("superman");
		
		assertEquals("superman", p.getPassword());
	}

	@Test
	public void testSetUsername() {
		PlatformUserImpl p = new PlatformUserImpl();
		p.setUsername("batman");
		
		assertEquals("batman", p.getUsername());
	}

	@Test
	public void testSetUsernameWithWhitespaces() {
		PlatformUserImpl p = new PlatformUserImpl();
		p.setUsername(" batman   ");
		
		assertEquals("batman", p.getUsername());
	}

	@Test
	public void testSetUsernameNull() {
		PlatformUserImpl p = new PlatformUserImpl();
		
		try {
			p.setUsername(null);
			fail("Should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
	}

	@Test
	public void testSetEmptyUsername() {
		PlatformUserImpl p = new PlatformUserImpl();
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
		PlatformUserImpl p = new PlatformUserImpl();
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
		PlatformUserImpl p = new PlatformUserImpl();
		p.setUsername("batman");
		
		assertEquals("batman", p.getUsername());
		p.setUsername("superman");
		
		assertEquals("superman", p.getUsername());
	}
	
	@Test
	public void testGetFirstName() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setFirstName("Bruce");
		
		assertEquals("Bruce", pu.getFirstName());
		
	}
	
	@Test
	public void testGetFirstNameNoName() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setFirstName("");
		
		assertEquals("", pu.getFirstName());
	}
	
	@Test
	public void testSetFirstName() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setFirstName("Bruce");
		
		assertEquals("Bruce", pu.getFirstName());
	}
	
	@Test
	public void testSetFirstNameEmptyName() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setFirstName("Bruce");
		pu.setFirstName("");
		
		assertEquals("", pu.getFirstName());
	}
	
	@Test
	public void testSetFirstNameNull() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setFirstName("Bruce");
		pu.setFirstName(null);
		
		assertEquals("", pu.getFirstName());
	}
	
	@Test
	public void testGetLastName() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setLastName("Wayne");
		
		assertEquals("Wayne", pu.getLastName());
		
	}
	
	@Test
	public void testGetLastNameNoName() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		
		assertEquals("", pu.getLastName());
	}
	
	@Test
	public void testSetLastName() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setLastName("Wayne");
		
		assertEquals("Wayne", pu.getLastName());
	}
	
	@Test
	public void testSetLastNameNoName() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setLastName("Wayne");
		pu.setLastName("");
		
		assertEquals("", pu.getLastName());	
	}
	
	@Test
	public void testSetLastNameNull() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setLastName("Wayne");
		pu.setLastName(null);
		
		assertEquals("", pu.getLastName());	
	}
	
	@Test
	public void testGetEmail() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setEmail("batman@gotham.net");
		
		assertEquals("batman@gotham.net", pu.getEmail());
	}
	
	@Test
	public void testGetEmailNoAddress() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		
		assertEquals("", pu.getEmail());
	}
	
	@Test
	public void testSetEmail() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setEmail("batman@gotham.net");
		
		assertEquals("batman@gotham.net", pu.getEmail());
	}
	
	@Test
	public void testSetEmailNoAddress() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setEmail("batman@gotham.net");
		pu.setEmail("");
		
		assertEquals("", pu.getEmail());
	}
	
	@Test
	public void testSetEmailBadAddress() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setEmail("batman@gotham.net");
		pu.setEmail("batman@gotham");
		
		assertEquals("", pu.getEmail());
	}
	
	@Test
	public void testSetEmailNull() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		pu.setEmail("batman@gotham.net");
		pu.setEmail(null);
		
		assertEquals("", pu.getEmail());
	}

	@Test
	public void testToString() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		
		assertEquals("PlatformUser batman (null)", pu.toString());
	}

	@Test
	public void testEqualsSameId() {
		UUID uuid = UUID.randomUUID();
		PlatformUserImpl pu1 = new PlatformUserImpl("batman", "superman");
		PlatformUserImpl pu2 = new PlatformUserImpl("fred", "barney");
		
		pu1.setUserId(uuid);
		pu2.setUserId(uuid);
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsSameName() {
		UUID uuid = UUID.randomUUID();
		PlatformUserImpl pu1 = new PlatformUserImpl("batman", "superman");
		PlatformUserImpl pu2 = new PlatformUserImpl("batman", "superman");
		
		pu1.setUserId(uuid);
		pu2.setUserId(uuid);
		
		assertTrue(pu1.equals(pu2));
	}

	@Test
	public void testEqualsSameNameAndId() {
		PlatformUserImpl pu1 = new PlatformUserImpl("batman", "superman");
		PlatformUserImpl pu2 = new PlatformUserImpl("batman", "superman");
		
		assertFalse(pu1.equals(pu2));
	}

	@Test
	public void testEqualsDifferentStuff() {
		PlatformUserImpl pu1 = new PlatformUserImpl("batman", "superman");
		PlatformUserImpl pu2 = new PlatformUserImpl("fred", "barney");
		
		assertFalse(pu1.equals(pu2));
	}
	
	@Test
	public void testCompareToABeforeB() {
		PlatformUserImpl pu1 = new PlatformUserImpl("batman", "superman");
		PlatformUserImpl pu2 = new PlatformUserImpl("fred", "barney");
		
		assertTrue(pu2.compareTo(pu1) < 0);
	}
	
	@Test
	public void testCompareToAAfterB() {
		PlatformUserImpl pu1 = new PlatformUserImpl("batman", "superman");
		PlatformUserImpl pu2 = new PlatformUserImpl("fred", "barney");
		
		assertTrue(pu1.compareTo(pu2) > 0);
	}
	
	@Test
	public void testCompareToAEqualToB() {
		PlatformUserImpl pu1 = new PlatformUserImpl("batman", "superman");
		PlatformUserImpl pu2 = pu1;
		
		pu2.setUsername("fred");
		pu2.setPassword("barney");
		
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
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
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
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		Properties expected = new Properties();
		Properties actual = null;
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
		
		expected.put("username", "batman");
		expected.put("password", "superman");
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
	public void testGetPropertiesEmptyFirstName() {
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		Properties expected = new Properties();
		Properties actual = null;
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
		
		expected.put("username", "batman");
		expected.put("password", "superman");
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
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		Properties expected = new Properties();
		Properties actual = null;
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
		
		expected.put("username", "batman");
		expected.put("password", "superman");
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
		PlatformUserImpl pu = new PlatformUserImpl("batman", "superman");
		Properties expected = new Properties();
		Properties actual = null;
		UUID uuid = UUID.randomUUID();
		
		pu.setUserId(uuid);
		
		expected.put("username", "batman");
		expected.put("password", "superman");
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
		expected.put("username", "batman");
		expected.put("password", "superman");
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateFlatIdentifier() {
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
		expected.put("username", "batman");
		expected.put("password", "superman");
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateNoId() {
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
		
		expected.put("username", "batman");
		expected.put("password", "superman");
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
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		Properties expected = new Properties();
		Properties actual = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.randomUUID();

		props.put("uuid", uuid);
		props.put("username", "batman");
		props.put("password", "superman");
		props.put("last_name", "Wayne");
		props.put("email", "batman@gotham.gov");
		props.put("created", now);
		
		pu.populate(props);
		
		expected.put("uuid", uuid);
		expected.put("username", "batman");
		expected.put("password", "superman");
		expected.put("first_name", "");
		expected.put("last_name", "Wayne");
		expected.put("email", "batman@gotham.gov");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateMissingLastName() {
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
		props.put("email", "batman@gotham.gov");
		props.put("created", now);
		
		pu.populate(props);
		
		expected.put("uuid", uuid);
		expected.put("username", "batman");
		expected.put("password", "superman");
		expected.put("first_name", "Bruce");
		expected.put("last_name", "");
		expected.put("email", "batman@gotham.gov");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateMissingEmail() {
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
		props.put("created", now);
		
		pu.populate(props);
		
		expected.put("uuid", uuid);
		expected.put("username", "batman");
		expected.put("password", "superman");
		expected.put("first_name", "Bruce");
		expected.put("last_name", "Wayne");
		expected.put("email", "");
		expected.put("created", now);
		
		actual = pu.getProperties();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPopulateMissingCreated() {
		PlatformUserImpl pu = new PlatformUserImpl();
		Properties props = new Properties();
		DateTime now = DateTime.now();
		UUID uuid = UUID.randomUUID();
		
		props.put("uuid", uuid);
		props.put("username", "batman");
		props.put("password", "superman");
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
		props.put("horses", "Sleipnir, Shadowfax, Brunte");
		props.put("created", now);
		
		pu.populate(props);
		
		expected.put("uuid", uuid);
		expected.put("username", "batman");
		expected.put("password", "superman");
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
