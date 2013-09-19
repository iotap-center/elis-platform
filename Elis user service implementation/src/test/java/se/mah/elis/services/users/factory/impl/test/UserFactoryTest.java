package se.mah.elis.services.users.factory.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;
import se.mah.elis.services.users.factory.impl.UserFactoryImpl;
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
	}

	@Test
	public void testBuild() {
		UserProvider provider = new MockUserProvider();
		User user = null;
		Properties props = new Properties();
		
		uf.registerProvider(provider);
		
		props.put("stuff", "batman");
		props.put("whatever", 42);
		
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
		
		props.put("stuff", "batman");
		props.put("whatever", 42);
		
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
		UserProvider provider = new MockUserProvider();
		User user = null;
		Properties props = new Properties();
		
		uf.registerProvider(provider);
		
		props.put("stuff", "batman");
		props.put("Whatever", 42);
		
		try {
			user = uf.build("MockUser", "test", props);
			fail("User should not be initialized");
		} catch (UserInitalizationException e) {}
	}

	@Test
	public void testBuildBadUserType() {
		UserProvider provider = new MockUserProvider();
		User user = null;
		Properties props = new Properties();
		
		uf.registerProvider(provider);
		
		props.put("stuff", "batman");
		props.put("Whatever", 42);
		
		try {
			user = uf.build("GatewayUser", "test", props);
			fail("User should not be initialized");
		} catch (UserInitalizationException e) {}
	}

	@Test
	public void testBuildBadServiceName() {
		UserProvider provider = new MockUserProvider();
		User user = null;
		Properties props = new Properties();
		
		uf.registerProvider(provider);
		
		props.put("stuff", "batman");
		props.put("Whatever", 42);
		
		try {
			user = uf.build("MockUser", "foobar", props);
			fail("User should not be initialized");
		} catch (UserInitalizationException e) {}
	}

	@Test
	public void testBuildBadProperties() {
		UserProvider provider = new MockUserProvider();
		User user = null;
		Properties props = new Properties();
		
		uf.registerProvider(provider);
		
		props.put("stuff", "batman");
		props.put("Whatever", "horses");
		
		try {
			user = uf.build("MockUser", "test", props);
			fail("User should not be initialized");
		} catch (UserInitalizationException e) {}
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
