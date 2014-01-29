package se.mah.elis.impl.service.storage.test;

import static org.junit.Assert.*;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.impl.services.storage.TableBuilder;
import se.mah.elis.services.storage.exceptions.StorageException;

public class TableBuilderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildModel() {
		String tableName = java.lang.String.class.getName();
		Properties props = new OrderedProperties();
		UUID uuid = UUID.fromString("38aeb66c-88dd-11e3-a86e-d231feb1dc81");
		DateTime dt = DateTime.now();
		String expected = null;
		String actual = null;
		
		expected = "CREATE TABLE `java-lang-String` (" +
						"`a_uuid` BINARY(16) PRIMARY KEY, " +
						"`a_string` VARCHAR(32), " +
						"`another_string` TEXT, " +
						"`an_integer` INTEGER, " +
						"`a_long` BIGINT, " +
						"`a_float` DOUBLE, " +
						"`a_double` DOUBLE, " +
						"`a_boolean` BOOLEAN, " +
						"`a_datetime` TIMESTAMP" +
					");";

		props.put("a uuid", uuid);
		props.put("a string", "32");
		props.put("another string", "");
		props.put("an integer", 0);
		props.put("a long", Long.MAX_VALUE);
		props.put("a float", 1.0);
		props.put("a double", Double.MAX_VALUE);
		props.put("a boolean", false);
		props.put("a datetime", dt);
		
		try {
			actual = TableBuilder.buildModel(tableName, props);
		} catch (StorageException e) {
			fail("This shouldn't happen.");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testBuildModelEmptyTableName() {
		String tableName = "";
		Properties props = new OrderedProperties();
		UUID uuid = UUID.fromString("38aeb66c-88dd-11e3-a86e-d231feb1dc81");
		DateTime dt = DateTime.now();

		props.put("a uuid", uuid);
		props.put("a string", "32");
		props.put("another string", "");
		props.put("an integer", 0);
		props.put("a long", Long.MAX_VALUE);
		props.put("a float", 1.0);
		props.put("a double", Double.MAX_VALUE);
		props.put("a boolean", false);
		props.put("a datetime", dt);
		
		try {
			TableBuilder.buildModel(tableName, props);
			fail("This shouldn't happen.");
		} catch (StorageException e) {
		}
	}
	
	@Test
	public void testBuildModelTableNameIsNull() {
		String tableName = null;
		Properties props = new OrderedProperties();
		UUID uuid = UUID.fromString("38aeb66c-88dd-11e3-a86e-d231feb1dc81");
		DateTime dt = DateTime.now();

		props.put("a uuid", uuid);
		props.put("a string", "32");
		props.put("another string", "");
		props.put("an integer", 0);
		props.put("a long", Long.MAX_VALUE);
		props.put("a float", 1.0);
		props.put("a double", Double.MAX_VALUE);
		props.put("a boolean", false);
		props.put("a datetime", dt);
		
		try {
			TableBuilder.buildModel(tableName, props);
			fail("This shouldn't happen.");
		} catch (StorageException e) {
		}
	}
	
	@Test
	public void testBuildEmptyProperties() {
		String tableName = java.lang.String.class.getSimpleName();
		Properties props = new OrderedProperties();
		
		try {
			TableBuilder.buildModel(tableName, props);
			fail("This shouldn't happen.");
		} catch (StorageException e) {
		}
	}
	
	@Test
	public void testBuildPropertiesIsNull() {
		String tableName = java.lang.String.class.getSimpleName();
		Properties props = null;
		
		try {
			TableBuilder.buildModel(tableName, props);
			fail("This shouldn't happen.");
		} catch (StorageException e) {
		}
	}
	
	@Test
	public void testBuildWeirdProperties() {
		String tableName = java.lang.String.class.getSimpleName();
		Properties props = new OrderedProperties();
		UUID uuid = UUID.fromString("38aeb66c-88dd-11e3-a86e-d231feb1dc81");
		DateTime dt = DateTime.now();

		props.put("a uuid", uuid);
		props.put("a string", "-32"); // Whoa, this isn't allowed!
		props.put("another string", "");
		props.put("an integer", 0);
		props.put("a long", Long.MAX_VALUE);
		props.put("a float", 1.0);
		props.put("a double", Double.MAX_VALUE);
		props.put("a boolean", false);
		props.put("a datetime", dt);
		
		try {
			TableBuilder.buildModel(tableName, props);
			fail("This shouldn't happen.");
		} catch (StorageException e) {
		}
	}
}
