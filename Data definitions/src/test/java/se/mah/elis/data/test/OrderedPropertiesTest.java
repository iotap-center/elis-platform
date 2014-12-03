package se.mah.elis.data.test;

import static org.junit.Assert.*;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.data.OrderedProperties;

public class OrderedPropertiesTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClear() {
		OrderedProperties props = new OrderedProperties();
		
		props.put(1, "a");
		props.put(2, "b");
		
		assertEquals(2, props.size());
		
		props.clear();
		
		assertEquals(0, props.size());
	}

	@Test
	public void testClearEmptyList() {
		OrderedProperties props = new OrderedProperties();
		
		assertEquals(0, props.size());
		
		props.clear();
		
		assertEquals(0, props.size());
	}

	@Test
	public void testPropertyNames() {
		// ProptertyNames should come out in the same order as they are put in.
		OrderedProperties props = new OrderedProperties();
		Enumeration<Object> propertyNames;
		
		props.put("a", 1);
		props.put("b", 2);
		props.put("c", 3);
		props.put("batman", false);
		props.put("d", 42);
		
		propertyNames = (Enumeration<Object>) props.propertyNames();

		assertEquals("a", (String) propertyNames.nextElement());
		assertEquals("b", (String) propertyNames.nextElement());
		assertEquals("c", (String) propertyNames.nextElement());
		assertEquals("batman", (String) propertyNames.nextElement());
		assertEquals("d", (String) propertyNames.nextElement());
	}

	@Test
	public void testElements() {
		// ProptertyNames should come out in the same order as they are put in.
		OrderedProperties props = new OrderedProperties();
		Enumeration<Object> elements;

		props.put("a", 1);
		props.put("b", 2);
		props.put("c", 3);
		props.put("batman", false);
		props.put("d", 42);

		elements = (Enumeration<Object>) props.elements();

		assertEquals("a", (String) elements.nextElement());
		assertEquals("b", (String) elements.nextElement());
		assertEquals("c", (String) elements.nextElement());
		assertEquals("batman", (String) elements.nextElement());
		assertEquals("d", (String) elements.nextElement());
	}

	@Test
	public void testKeys() {
		// Keys should come out in the same order as they are put in.
		OrderedProperties props = new OrderedProperties();
		Enumeration<Object> keys;
		
		props.put("a", 1);
		props.put("b", 2);
		props.put("c", 3);
		props.put("batman", false);
		props.put("d", 42);
		
		keys = (Enumeration<Object>) props.keys();

		assertEquals("a", (String) keys.nextElement());
		assertEquals("b", (String) keys.nextElement());
		assertEquals("c", (String) keys.nextElement());
		assertEquals("batman", (String) keys.nextElement());
		assertEquals("d", (String) keys.nextElement());
	}
	
	@Test
	public void testEntrySet() {
		// Entry set's content should be ordered in the same order as the were
		// put in.
		OrderedProperties props = new OrderedProperties();
		Set<Map.Entry<Object, Object>> entries;
		int i = 0;
		
		props.put("a", 1);
		props.put("b", 2);
		props.put("c", 3);
		props.put("batman", false);
		props.put("d", 42);
		
		entries = props.entrySet();
		assertEquals(5, entries.size());

		for (Map.Entry<Object, Object> e : entries) {
			switch (i) {
				case 0:
					assertEquals("a", e.getKey());
					assertEquals(1, e.getValue());
					break;
				case 1:
					assertEquals("b", e.getKey());
					assertEquals(2, e.getValue());
					break;
				case 2:
					assertEquals("c", e.getKey());
					assertEquals(3, e.getValue());
					break;
				case 3:
					assertEquals("batman", e.getKey());
					assertEquals(false, e.getValue());
					break;
				case 4:
					assertEquals("d", e.getKey());
					assertEquals(42, e.getValue());
			}
			++i;
		}
	}

}
