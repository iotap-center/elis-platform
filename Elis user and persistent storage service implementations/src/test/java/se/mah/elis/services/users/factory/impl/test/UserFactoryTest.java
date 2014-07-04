package se.mah.elis.services.users.factory.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import se.mah.elis.impl.services.users.factory.UserFactoryImpl;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;
import se.mah.elis.services.users.factory.impl.test.mock.AnotherMockUserProvider;
import se.mah.elis.services.users.factory.impl.test.mock.MockUserProvider;
import se.mah.elis.services.users.impl.test.mock.MockUser;

public class UserFactoryTest {

	private UserFactory uf;
	
	@Before
	public void setUp() throws Exception {
		uf = new UserFactoryImpl();
	}

	@After
	public void tearDown() throws Exception {
		uf = null;
	}

	@Test
	public void testBuild() {
		UserProvider provider = new MockUserProvider();
		User user = null;
		Properties props = new Properties();
		
		uf.registerProvider(provider);
		
		props.put("id_number", 1);
		props.put("username", "alice");
		props.put("password", "foo");
		props.put("uuid", UUID.randomUUID());
		props.put("stuff", "batman");
		props.put("whatever", 42);
		props.put("created", DateTime.now());

		try {
			user = uf.build("MockUser", "test", props);
		} catch (UserInitalizationException e) {
			fail("User should be initialized");
		}
		
		assertEquals(MockUser.class.getName(), user.getClass().getName());
		assertEquals("batman", ((MockUser) user).getStuff());
		assertEquals(42, ((MockUser) user).getWhatever());
	}


	@Test
	public void testBuildSeveralProviders() {
		UserProvider provider = new MockUserProvider();
		UserProvider provider2 = new AnotherMockUserProvider();
		User user = null;
		Properties props = new Properties();
		
		uf.registerProvider(provider);
		uf.registerProvider(provider2);
		
		props.put("id_number", 1);
		props.put("username", "alice");
		props.put("password", "foo");
		props.put("uuid", UUID.randomUUID());
		props.put("stuff", "batman");
		props.put("whatever", 42);
		props.put("created", DateTime.now());

		try {
			user = uf.build("MockUser", "test", props);
		} catch (UserInitalizationException e) {
			fail("User should be initialized");
		}
		
		assertEquals(MockUser.class.getName(), user.getClass().getName());
		assertEquals("batman", ((MockUser) user).getStuff());
		assertEquals(42, ((MockUser) user).getWhatever());
	}

	@Test
	public void testBuildNoProviders() {
		Properties props = new Properties();
		
		props.put("stuff", "batman");
		props.put("Whatever", (new Integer(42)).toString());

		try {
			uf.build("MockUser", "test", props);
			fail("User should not be initialized");
		} catch (UserInitalizationException e) {}
	}

	@Test
	public void testBuildBadUserType() {
		UserProvider provider = new MockUserProvider();
		Properties props = new Properties();
		
		uf.registerProvider(provider);
		
		props.put("stuff", "batman");
		props.put("Whatever", (new Integer(42)).toString());

		try {
			uf.build("GatewayUser", "test", props);
			fail("User should not be initialized");
		} catch (UserInitalizationException e) {}
	}

	@Test
	public void testBuildBadServiceName() {
		UserProvider provider = new MockUserProvider();
		Properties props = new Properties();
		
		uf.registerProvider(provider);
		
		props.put("stuff", "batman");
		props.put("Whatever", (new Integer(42)).toString());

		try {
			uf.build("MockUser", "foobar", props);
			fail("User should not be initialized");
		} catch (UserInitalizationException e) {}
	}

	@Test
	public void testBuildBadProperties() {
		UserProvider provider = new MockUserProvider();
		Properties props = new Properties();
		
		uf.registerProvider(provider);
		
		props.put("stuff", "batman");
		props.put("Whatever", "horses");

		try {
			uf.build("MockUser", "test", props);
			fail("User should not be initialized");
		} catch (UserInitalizationException e) {}
	}
	
	@Test
	public void testBuildPlatformUser() {
		Properties props = new Properties();
		PlatformUser pu = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		props.put("uuid", uuid);
		props.put("username", "arthur");
		props.put("password", "don't panic");
		props.put("first_name", "Arthur");
		props.put("last_name", "Dent");
		props.put("email", "arthur@heartofgold.net");
		props.put("created", now);
		
		try {
			pu = uf.build(props);
		} catch (UserInitalizationException e) {
			fail("User should have been initialized");
		}
		
		assertEquals(uuid, pu.getUserId());
		assertEquals("arthur", pu.getUsername());
		assertEquals("don't panic", pu.getPassword());
		assertEquals("Arthur", pu.getFirstName());
		assertEquals("Dent", pu.getLastName());
		assertEquals("arthur@heartofgold.net", pu.getEmail());
		assertEquals(now, pu.created());
	}
	
	@Test
	public void testBuildPlatformUserNoUsername() {
		Properties props = new Properties();
		PlatformUser pu = null;
		
		props.put("id", 42);
		props.put("password", "don't panic");
		props.put("first_name", "Arthur");
		props.put("last_name", "Dent");
		props.put("email", "arthur@heartofgold.net");
		
		try {
			pu = uf.build(props);
			fail("User should not be initialized");
		} catch (UserInitalizationException e) {
		}
	}
	
	@Test
	public void testBuildPlatformUserEmptyUsername() {
		Properties props = new Properties();
		PlatformUser pu = null;
		
		props.put("id", 42);
		props.put("username", "");
		props.put("password", "don't panic");
		props.put("first_name", "Arthur");
		props.put("last_name", "Dent");
		props.put("email", "arthur@heartofgold.net");
		
		try {
			pu = uf.build(props);
			fail("User should not be initialized");
		} catch (UserInitalizationException e) {
		}
	}
	
	@Test
	public void testBuildPlatformUserNoPassword() {
		Properties props = new Properties();
		PlatformUser pu = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		props.put("uuid", uuid);
		props.put("username", "arthur");
		props.put("first_name", "Arthur");
		props.put("last_name", "Dent");
		props.put("email", "arthur@heartofgold.net");
		props.put("created", now);
		
		try {
			pu = uf.build(props);
		} catch (UserInitalizationException e) {
			fail("User should have been initialized");
		}
		
		assertEquals(uuid, pu.getUserId());
		assertEquals("arthur", pu.getUsername());
		assertNull(pu.getPassword());
		assertEquals("Arthur", pu.getFirstName());
		assertEquals("Dent", pu.getLastName());
		assertEquals("arthur@heartofgold.net", pu.getEmail());
		assertEquals(now, pu.created());
	}
	
	@Test
	public void testBuildPlatformUserEmptyPassword() {
		Properties props = new Properties();
		PlatformUser pu = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		props.put("uuid", uuid);
		props.put("username", "arthur");
		props.put("password", "");
		props.put("first_name", "Arthur");
		props.put("last_name", "Dent");
		props.put("email", "arthur@heartofgold.net");
		props.put("created", now);
		
		try {
			pu = uf.build(props);
		} catch (UserInitalizationException e) {
			fail("User should have been initialized");
		}
	}
	
	@Test
	public void testBuildPlatformUserEmptyFirstName() {
		Properties props = new Properties();
		PlatformUser pu = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		props.put("uuid", uuid);
		props.put("username", "arthur");
		props.put("password", "don't panic");
		props.put("first_name", "");
		props.put("last_name", "Dent");
		props.put("email", "arthur@heartofgold.net");
		props.put("created", now);
		
		try {
			pu = uf.build(props);
		} catch (UserInitalizationException e) {
			fail("This should not happen");
		}
		
		assertEquals(uuid, pu.getUserId());
		assertEquals("arthur", pu.getUsername());
		assertEquals("don't panic", pu.getPassword());
		assertEquals("", pu.getFirstName());
		assertEquals("Dent", pu.getLastName());
		assertEquals("arthur@heartofgold.net", pu.getEmail());
		assertEquals(now, pu.created());
	}
	
	@Test
	public void testBuildPlatformUserNoFirstName() {
		Properties props = new Properties();
		PlatformUser pu = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		props.put("uuid", uuid);
		props.put("username", "arthur");
		props.put("password", "don't panic");
		props.put("last_name", "Dent");
		props.put("email", "arthur@heartofgold.net");
		props.put("created", now);
		
		try {
			pu = uf.build(props);
		} catch (UserInitalizationException e) {
			fail("This should not happen");
		}
		
		assertEquals(uuid, pu.getUserId());
		assertEquals("arthur", pu.getUsername());
		assertEquals("don't panic", pu.getPassword());
		assertEquals("", pu.getFirstName());
		assertEquals("Dent", pu.getLastName());
		assertEquals("arthur@heartofgold.net", pu.getEmail());
		assertEquals(now, pu.created());
	}
	
	@Test
	public void testBuildPlatformUserEmptyLastName() {
		Properties props = new Properties();
		PlatformUser pu = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		props.put("uuid", uuid);
		props.put("username", "arthur");
		props.put("password", "don't panic");
		props.put("first_name", "Arthur");
		props.put("last_name", "");
		props.put("email", "arthur@heartofgold.net");
		props.put("created", now);
		
		try {
			pu = uf.build(props);
		} catch (UserInitalizationException e) {
			fail("This should not happen");
		}
		
		assertEquals(uuid, pu.getUserId());
		assertEquals("arthur", pu.getUsername());
		assertEquals("don't panic", pu.getPassword());
		assertEquals("Arthur", pu.getFirstName());
		assertEquals("", pu.getLastName());
		assertEquals("arthur@heartofgold.net", pu.getEmail());
		assertEquals(now, pu.created());
	}
	
	@Test
	public void testBuildPlatformUserNoLastName() {
		Properties props = new Properties();
		PlatformUser pu = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		props.put("uuid", uuid);
		props.put("username", "arthur");
		props.put("password", "don't panic");
		props.put("first_name", "Arthur");
		props.put("email", "arthur@heartofgold.net");
		props.put("created", now);
		
		try {
			pu = uf.build(props);
		} catch (UserInitalizationException e) {
			fail("User should not be initialized");
		}
		
		assertEquals(uuid, pu.getUserId());
		assertEquals("arthur", pu.getUsername());
		assertEquals("don't panic", pu.getPassword());
		assertEquals("Arthur", pu.getFirstName());
		assertEquals("", pu.getLastName());
		assertEquals("arthur@heartofgold.net", pu.getEmail());
		assertEquals(now, pu.created());
	}
	
	@Test
	public void testBuildPlatformUserEmptyEmail() {
		Properties props = new Properties();
		PlatformUser pu = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		props.put("uuid", uuid);
		props.put("username", "arthur");
		props.put("password", "don't panic");
		props.put("first_name", "Arthur");
		props.put("last_name", "Dent");
		props.put("email", "");
		props.put("created", now);
		
		try {
			pu = uf.build(props);
		} catch (UserInitalizationException e) {
			fail("User should not be initialized");
		}
		
		assertEquals(uuid, pu.getUserId());
		assertEquals("arthur", pu.getUsername());
		assertEquals("don't panic", pu.getPassword());
		assertEquals("Arthur", pu.getFirstName());
		assertEquals("Dent", pu.getLastName());
		assertEquals("", pu.getEmail());
		assertEquals(now, pu.created());
	}
	
	@Test
	public void testBuildPlatformUserNoEmail() {
		Properties props = new Properties();
		PlatformUser pu = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		props.put("uuid", uuid);
		props.put("username", "arthur");
		props.put("password", "don't panic");
		props.put("first_name", "Arthur");
		props.put("last_name", "Dent");
		props.put("created", now);
		
		try {
			pu = uf.build(props);
		} catch (UserInitalizationException e) {
			fail("User should not be initialized");
		}
		
		assertEquals(uuid, pu.getUserId());
		assertEquals("arthur", pu.getUsername());
		assertEquals("don't panic", pu.getPassword());
		assertEquals("Arthur", pu.getFirstName());
		assertEquals("Dent", pu.getLastName());
		assertEquals("", pu.getEmail());
		assertEquals(now, pu.created());
	}
	
	@Test
	public void testBuildPlatformUserMalformedEmail() {
		Properties props = new Properties();
		PlatformUser pu = null;
		DateTime now = DateTime.now();
		UUID uuid = UUID.fromString("00001111-2222-3333-4444-555566667777");
		
		props.put("uuid", uuid);
		props.put("username", "arthur");
		props.put("password", "don't panic");
		props.put("first_name", "Arthur");
		props.put("last_name", "Dent");
		props.put("email", "arthur@earth");
		props.put("created", now);
		
		try {
			pu = uf.build(props);
		} catch (UserInitalizationException e) {
			fail("User should not be initialized");
		}
		
		assertEquals(uuid, pu.getUserId());
		assertEquals("arthur", pu.getUsername());
		assertEquals("don't panic", pu.getPassword());
		assertEquals("Arthur", pu.getFirstName());
		assertEquals("Dent", pu.getLastName());
		assertEquals("", pu.getEmail());
		assertEquals(now, pu.created());
	}

	@Test
	public void testGetAvailableUserRecipes() {
		UserProvider provider = new MockUserProvider();
		UserRecipe[] recipes = null;
		
		uf.registerProvider(provider);
		
		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(1, recipes.length);
	}

	@Test
	public void testGetAvailableUserRecipesNoRecipes() {
		UserRecipe[] recipes = null;
		
		recipes = uf.getAvailableUserRecipes();
		
		assertNotNull(recipes);
		assertEquals(0, recipes.length);
	}

	@Test
	public void testGetAvailableUserRecipesSeveralProviders() {
		UserProvider provider = new MockUserProvider();
		UserProvider provider2 = new AnotherMockUserProvider();
		UserRecipe[] recipes = null;
		
		uf.registerProvider(provider);
		uf.registerProvider(provider2);
		
		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(2, recipes.length);
		assertNotSame(recipes[0], recipes[1]);
		assertThat(recipes[0].getClass().getName()).doesNotMatch(recipes[1].getClass().getName());
	}
	
	@Test
	public void testGetUserRecipe() {
		UserRecipe recipe = null;
		
		uf.registerProvider(new MockUserProvider());
		uf.registerProvider(new AnotherMockUserProvider());
		
		recipe = uf.getRecipe("MockUser", "test");
		
		assertNotNull(recipe);
		assertThat(recipe.getUserType()).matches("MockUser");
		assertThat(recipe.getServiceName()).matches("test");
	}
	
	@Test
	public void testGetUserRecipeNotFirstlyAdded() {
		UserRecipe recipe = null;
		
		uf.registerProvider(new MockUserProvider());
		uf.registerProvider(new AnotherMockUserProvider());
		
		recipe = uf.getRecipe("AnotherMockUser", "test");
		
		assertNotNull(recipe);
		assertThat(recipe.getUserType()).matches("AnotherMockUser");
		assertThat(recipe.getServiceName()).matches("test");
	}
	
	@Test
	public void testGetUserRecipeNoSuchUserType() {
		UserRecipe recipe = null;
		
		uf.registerProvider(new MockUserProvider());
		uf.registerProvider(new AnotherMockUserProvider());
		
		recipe = uf.getRecipe("MoccaUser", "test");
		
		assertThat(recipe).isNull();
	}
	
	@Test
	public void testGetUserRecipeNoSuchSystemName() {
		UserRecipe recipe = null;
		
		uf.registerProvider(new MockUserProvider());
		uf.registerProvider(new AnotherMockUserProvider());
		
		recipe = uf.getRecipe("MockUser", "horse");
		
		assertThat(recipe).isNull();
	}

	@Test
	public void testRegisterProvider() {
		UserProvider provider = new MockUserProvider();
		UserRecipe[] recipes = null;
		
		uf.registerProvider(provider);

		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(1, recipes.length);
	}

	@Test
	public void testRegisterProviderTwoProviders() {
		UserProvider provider = new MockUserProvider();
		UserProvider provider2 = new AnotherMockUserProvider();
		UserRecipe[] recipes = null;
		
		uf.registerProvider(provider);
		uf.registerProvider(provider2);
		
		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(2, recipes.length);
	}

	@Test
	public void testRegisterProviderTwoIdenticalProviders() {
		UserProvider provider = new MockUserProvider();
		UserProvider provider2 = new MockUserProvider();
		UserRecipe[] recipes = null;
		
		uf.registerProvider(provider);
		uf.registerProvider(provider2);
		
		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(1, recipes.length);
	}

	@Test
	public void testUnregisterProvider() {
		UserProvider provider = new MockUserProvider();
		UserRecipe[] recipes = null;
		
		uf.registerProvider(provider);

		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(1, recipes.length);
		
		uf.unregisterProvider(provider);

		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(0, recipes.length);
	}

	@Test
	public void testUnregisterProviderTwoProviders() {
		UserProvider provider = new MockUserProvider();
		UserProvider provider2 = new AnotherMockUserProvider();
		UserRecipe[] recipes = null;
		
		uf.registerProvider(provider);
		uf.registerProvider(provider2);
		
		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(2, recipes.length);
		
		uf.unregisterProvider(provider);

		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(1, recipes.length);
	}

	@Test
	public void testUnregisterProviderNonExistingProviders() {
		UserProvider provider = new MockUserProvider();
		UserProvider provider2 = new AnotherMockUserProvider();
		UserRecipe[] recipes = null;
		
		uf.registerProvider(provider);
		
		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(1, recipes.length);
		
		uf.unregisterProvider(provider2);

		recipes = uf.getAvailableUserRecipes();
		
		assertEquals(1, recipes.length);
	}
}
