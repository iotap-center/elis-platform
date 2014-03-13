package se.mah.elis.impl.services.storage.result.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.impl.services.storage.result.ResultSetImpl;
import se.mah.elis.services.storage.result.ResultSet;

public class ResultSetImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testResultSetImpl() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		
		assertNotNull(rs);
	}

	@Test
	public void testResultSetImplObjectTypeIsNull() {
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(null, strings);
		
		assertNotNull(rs);
	}

	@Test
	public void testResultSetImplResultsIsNull() {
		Class clazz = java.lang.String.class;
		ResultSet rs = new ResultSetImpl(clazz, null);
		
		assertNotNull(rs);
	}

	@Test
	public void testResultSetImplAllNull() {
		ResultSet rs = new ResultSetImpl(null, null);
		
		assertNotNull(rs);
	}
	
	@Test
	public void testSize() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		int expected = 3;
		int actual = 0;
		ResultSet rs = new ResultSetImpl(clazz, strings);
		
		actual = rs.size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSizeResultsIsNull() {
		Class clazz = java.lang.String.class;
		int expected = 0;
		int actual = 0;
		ResultSet rs = new ResultSetImpl(clazz, null);
		
		actual = rs.size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSizeResultsIsEmpty() {
		Class clazz = java.lang.String.class;
		String[] strings = new String[0];
		int expected = 0;
		int actual = 0;
		ResultSet rs = new ResultSetImpl(clazz, strings);
		
		actual = rs.size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetObjectType() {
		Class clazz = java.lang.String.class;
		String[] strings = new String[0];
		Class expected = clazz;
		Class actual = null;
		ResultSet rs = new ResultSetImpl(clazz, strings);
		
		actual = rs.getObjectType();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetObjectTypeObjectTypeIsNull() {
		String[] strings = new String[0];
		ResultSet rs = new ResultSetImpl(null, strings);
		
		assertNull(rs.getObjectType());
	}
	
	@Test
	public void testGet() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "123";
		Object actual = rs.get(1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetRowsAreNull() {
		Class clazz = java.lang.String.class;
		ResultSet rs = new ResultSetImpl(clazz, null);
		
		try {
			rs.get(0);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// This should happen.
		}
	}
	
	@Test
	public void testGetNoRows() {
		Class clazz = java.lang.String.class;
		String[] strings = {};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		
		try {
			rs.get(0);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// This should happen.
		}
	}
	
	@Test
	public void testGetIndexIsZero() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "abc";
		Object actual = rs.get(0);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetIndexOutOfBounds() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		
		try {
			rs.get(4);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// This should happen.
		}
	}
	
	@Test
	public void testGetNegativeIndex() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		
		try {
			rs.get(-1);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// This should happen.
		}
	}
	
	@Test
	public void testGetArray() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String[] expected = {"abc", "123", "chocolate"};
		Object[] actual = rs.getArray();
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetArrayResultsAreNull() {
		Class clazz = java.lang.String.class;
		ResultSet rs = new ResultSetImpl(clazz, null);
		String[] expected = {};
		Object[] actual = rs.getArray();
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetArrayNoResults() {
		Class clazz = java.lang.String.class;
		String[] strings = {};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String[] expected = {};
		Object[] actual = rs.getArray();
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetArrayOnlyOneResult() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String[] expected = {"abc"};
		Object[] actual = rs.getArray();
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testNext() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "abc";
		Object actual = null;
		
		actual = rs.next();
		assertEquals(expected, actual);
		
		expected = "123";
		actual = rs.next();
		assertEquals(expected, actual);
		
		expected = "chocolate";
		actual = rs.next();
		assertEquals(expected, actual);
		
		assertNull(rs.next());
	}
	
	@Test
	public void testNextOnlyOne() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "abc";
		Object actual = null;
		
		actual = rs.next();
		assertEquals(expected, actual);
		
		assertNull(rs.next());
	}
	
	@Test
	public void testNextNoResults() {
		Class clazz = java.lang.String.class;
		ResultSet rs = new ResultSetImpl(clazz, null);
		
		assertNull(rs.next());
	}
	
	@Test
	public void testHasNext() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		
		rs.next();
		rs.next();
		assertTrue(rs.hasNext());
		
		rs.next();
		assertFalse(rs.hasNext());
	}
	
	@Test
	public void testHasNextOnlyOne() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		
		assertTrue(rs.hasNext());
		
		rs.next();
		assertFalse(rs.hasNext());
	}
	
	@Test
	public void testHasNextNoResults() {
		Class clazz = java.lang.String.class;
		ResultSet rs = new ResultSetImpl(clazz, null);
		
		assertFalse(rs.hasNext());
	}
	
	@Test
	public void testFirst() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "abc";
		Object actual = null;
		
		actual = rs.first();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFirstAfterNext() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "abc";
		Object actual = null;
		
		rs.next();
		actual = rs.first();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFirstOnlyOne() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "abc";
		Object actual = null;
		
		actual = rs.first();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFirstNoResults() {
		Class clazz = java.lang.String.class;
		ResultSet rs = new ResultSetImpl(clazz, null);
		
		assertNull(rs.first());
	}
	
	@Test
	public void testLast() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "chocolate";
		Object actual = null;
		
		actual = rs.last();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLastAfterNext() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "chocolate";
		Object actual = null;
		
		rs.next();
		actual = rs.last();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLastOnlyOne() {
		Class clazz = java.lang.String.class;
		String[] strings = {"chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "chocolate";
		Object actual = null;
		
		actual = rs.last();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLastNoResult() {
		Class clazz = java.lang.String.class;
		ResultSet rs = new ResultSetImpl(clazz, null);
		
		assertNull(rs.last());
	}
	
	@Test
	public void testReset() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc", "123", "chocolate"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "123";
		Object actual = null;

		rs.next();
		actual = rs.next();
		assertEquals(expected, actual);
		
		rs.reset();
		actual = rs.next();
		expected = "abc";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testResetOnlyOne() {
		Class clazz = java.lang.String.class;
		String[] strings = {"abc"};
		ResultSet rs = new ResultSetImpl(clazz, strings);
		String expected = "abc";
		Object actual = null;

		actual = rs.next();
		assertEquals(expected, actual);
		
		rs.reset();
		actual = rs.next();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testResetNoResult() {
		Class clazz = java.lang.String.class;
		ResultSet rs = new ResultSetImpl(clazz, null);

		rs.next();
		rs.reset();
		assertNull(rs.next());
	}
}
