package se.mah.elis.impl.service.storage.factory.test;

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
import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.exceptions.DataInitalizationException;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject1;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject1Provider;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject2Provider;
import se.mah.elis.impl.services.storage.factory.DataObjectFactoryImpl;
import se.mah.elis.impl.services.users.factory.UserFactoryImpl;
import se.mah.elis.services.storage.factory.DataObjectFactory;
import se.mah.elis.services.storage.factory.DataObjectProvider;
import se.mah.elis.services.storage.factory.DataObjectRecipe;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;
import se.mah.elis.services.users.factory.impl.test.mock.AnotherMockUserProvider;
import se.mah.elis.services.users.factory.impl.test.mock.MockUserProvider;
import se.mah.elis.services.users.impl.test.mock.MockUser;

public class DataObjectFactoryTest {

	private DataObjectFactory dof;
	
	@Before
	public void setUp() throws Exception {
		dof = new DataObjectFactoryImpl();
	}

	@After
	public void tearDown() throws Exception {
		dof = null;
	}

	@Test
	public void testBuild() {
		DataObjectProvider provider = new MockDataObject1Provider();
		ElisDataObject edo = null;
		Properties props = new Properties();
		
		dof.registerProvider(provider);
		
		props.put("id_number", 1);
		props.put("username", "alice");
		props.put("password", "foo");
		props.put("uuid", UUID.randomUUID());
		props.put("foo", 42);
		props.put("bar", "batman");
		props.put("created", DateTime.now());

		try {
			edo = dof.build("se.mah.elis.impl.service.storage.test.mock.MockDataObject1", props);
		} catch (DataInitalizationException e) {
			fail("Data object should be initialized");
		}
		
		assertEquals(MockDataObject1.class.getName(), edo.getClass().getName());
		assertEquals(42, ((MockDataObject1) edo).getFoo());
		assertEquals("batman", ((MockDataObject1) edo).getBar());
	}


	@Test
	public void testBuildSeveralProviders() {
		DataObjectProvider provider = new MockDataObject1Provider();
		DataObjectProvider provider2 = new MockDataObject2Provider();
		ElisDataObject edo = null;
		Properties props = new Properties();
		
		dof.registerProvider(provider);
		dof.registerProvider(provider2);
		
		props.put("id_number", 1);
		props.put("username", "alice");
		props.put("password", "foo");
		props.put("uuid", UUID.randomUUID());
		props.put("foo", 42);
		props.put("bar", "batman");
		props.put("created", DateTime.now());

		try {
			edo = dof.build("se.mah.elis.impl.service.storage.test.mock.MockDataObject1", props);
		} catch (DataInitalizationException e) {
			fail("Data object should be initialized");
		}
		
		assertEquals(MockDataObject1.class.getName(), edo.getClass().getName());
		props.put("foo", 42);
		props.put("bar", "batman");
	}

	@Test
	public void testBuildNoProviders() {
		Properties props = new Properties();
		
		props.put("stuff", "batman");
		props.put("Whatever", (new Integer(42)).toString());

		try {
			dof.build("se.mah.elis.impl.service.storage.test.mock.MockDataObject1", props);
			fail("Data object should not be initialized");
		} catch (DataInitalizationException e) {}
	}

	@Test
	public void testBuildBadDataType() {
		DataObjectProvider provider = new MockDataObject1Provider();
		Properties props = new Properties();
		
		dof.registerProvider(provider);

		props.put("foo", 42);
		props.put("bar", "batman");

		try {
			dof.build("se.mah.elis.impl.service.storage.test.mock.BatmanObject", props);
			fail("Data object should not be initialized");
		} catch (DataInitalizationException e) {}
	}

	@Test
	public void testBuildBadProperties() {
		DataObjectProvider provider = new MockDataObject1Provider();
		Properties props = new Properties();
		
		dof.registerProvider(provider);
		
		props.put("foo", "batman");
		props.put("bar", "horses");

		try {
			dof.build("se.mah.elis.impl.service.storage.test.mock.MockDataObject1", props);
			fail("Data object should not be initialized");
		} catch (DataInitalizationException e) {}
	}

	@Test
	public void testGetAvailableDataRecipes() {
		DataObjectProvider provider = new MockDataObject1Provider();
		DataObjectRecipe[] recipes = null;
		
		dof.registerProvider(provider);
		
		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(1, recipes.length);
	}

	@Test
	public void testGetAvailableDataRecipesNoRecipes() {
		DataObjectRecipe[] recipes = null;
		
		recipes = dof.getAvailableDataRecipes();
		
		assertNotNull(recipes);
		assertEquals(0, recipes.length);
	}

	@Test
	public void testGetAvailableDataRecipesSeveralProviders() {
		DataObjectProvider provider = new MockDataObject1Provider();
		DataObjectProvider provider2 = new MockDataObject2Provider();
		DataObjectRecipe[] recipes = null;
		
		dof.registerProvider(provider);
		dof.registerProvider(provider2);
		
		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(2, recipes.length);
		assertNotSame(recipes[0], recipes[1]);
		assertThat(recipes[0].getClass().getName()).doesNotMatch(recipes[1].getClass().getName());
	}
	
	@Test
	public void testGetDataRecipe() {
		DataObjectRecipe recipe = null;
		
		dof.registerProvider(new MockDataObject1Provider());
		dof.registerProvider(new MockDataObject2Provider());
		
		recipe = dof.getRecipe("se.mah.elis.impl.service.storage.test.mock.MockDataObject1");
		
		assertNotNull(recipe);
		assertThat(recipe.getDataType()).matches("se.mah.elis.impl.service.storage.test.mock.MockDataObject1");
	}
	
	@Test
	public void testGetDataRecipeNotFirstlyAdded() {
		DataObjectRecipe recipe = null;
		
		dof.registerProvider(new MockDataObject1Provider());
		dof.registerProvider(new MockDataObject2Provider());
		
		recipe = dof.getRecipe("se.mah.elis.impl.service.storage.test.mock.MockDataObject2");
		
		assertNotNull(recipe);
		assertThat(recipe.getDataType()).matches("se.mah.elis.impl.service.storage.test.mock.MockDataObject2");
	}
	
	@Test
	public void testGetDataRecipeNoSuchDataType() {
		DataObjectRecipe recipe = null;
		
		dof.registerProvider(new MockDataObject1Provider());
		dof.registerProvider(new MockDataObject2Provider());
		
		recipe = dof.getRecipe("se.mah.elis.impl.service.storage.test.mock.MoccaUser");
		
		assertThat(recipe).isNull();
	}

	@Test
	public void testRegisterProvider() {
		DataObjectProvider provider = new MockDataObject1Provider();
		DataObjectRecipe[] recipes = null;
		
		dof.registerProvider(provider);

		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(1, recipes.length);
	}

	@Test
	public void testRegisterProviderTwoProviders() {
		DataObjectProvider provider = new MockDataObject1Provider();
		DataObjectProvider provider2 = new MockDataObject2Provider();
		DataObjectRecipe[] recipes = null;
		
		dof.registerProvider(provider);
		dof.registerProvider(provider2);
		
		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(2, recipes.length);
	}

	@Test
	public void testRegisterProviderTwoIdenticalProviders() {
		DataObjectProvider provider = new MockDataObject1Provider();
		DataObjectProvider provider2 = new MockDataObject1Provider();
		DataObjectRecipe[] recipes = null;
		
		dof.registerProvider(provider);
		dof.registerProvider(provider2);
		
		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(1, recipes.length);
	}

	@Test
	public void testUnregisterProvider() {
		DataObjectProvider provider = new MockDataObject1Provider();
		DataObjectRecipe[] recipes = null;
		
		dof.registerProvider(provider);

		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(1, recipes.length);
		
		dof.unregisterProvider(provider);

		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(0, recipes.length);
	}

	@Test
	public void testUnregisterProviderTwoProviders() {
		DataObjectProvider provider = new MockDataObject1Provider();
		DataObjectProvider provider2 = new MockDataObject2Provider();
		DataObjectRecipe[] recipes = null;
		
		dof.registerProvider(provider);
		dof.registerProvider(provider2);
		
		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(2, recipes.length);
		
		dof.unregisterProvider(provider);

		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(1, recipes.length);
	}

	@Test
	public void testUnregisterProviderNonExistingProviders() {
		DataObjectProvider provider = new MockDataObject1Provider();
		DataObjectProvider provider2 = new MockDataObject2Provider();
		DataObjectRecipe[] recipes = null;
		
		dof.registerProvider(provider);
		
		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(1, recipes.length);
		
		dof.unregisterProvider(provider2);

		recipes = dof.getAvailableDataRecipes();
		
		assertEquals(1, recipes.length);
	}
}
