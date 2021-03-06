package se.mah.elis.services.storage.query.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.Predicate;
import se.mah.elis.services.storage.query.Query;
import se.mah.elis.services.storage.query.QueryTranslator;
import se.mah.elis.services.storage.query.test.mock.MockPredicate;
import se.mah.elis.services.storage.query.test.mock.MockTranslator;

public class QueryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetDataType() {
		Query query = new Query();
		Class expected = java.lang.Number.class;
		
		assertEquals(query, query.setDataType(java.lang.Number.class));
		
		assertEquals(expected, query.getDataType());
	}
	@Test
	public void testSetDataTypeReset() {
		Query query = new Query();
		Class expected = java.lang.Number.class;

		assertEquals(query, query.setDataType(java.lang.String.class));
		assertEquals(query, query.setDataType(java.lang.Number.class));
		
		assertEquals(expected, query.getDataType());
	}

	@Test
	public void testCompile() {
		Query query = new Query();
		QueryTranslator translator = new MockTranslator();
		Class what = java.lang.String.class;
		int start = 0;
		int limit = 10;
		Predicate p = new MockPredicate(1);
		String actual, expected;
		
		assertEquals(query, query.setTranslator(translator));
		assertEquals(query, query.setPredicate(p));
		assertEquals(query, query.limit(start, limit));
		assertEquals(query, query.setDataType(what));
		try {
			actual = query.compile();
		} catch (StorageException e) {
			actual = "";
			fail("This shouldn't happen");
		}
		
		expected = "Translate:\n" +
				   "  what: java.lang.String\n" +
				   "  where: MockPredicate 1\n" +
				   "  limits: 0, 10\n" +
				   "  oldestFirst: true";
		
		assertEquals(expected, actual);
	}

	@Test
	public void testCompileBadQuery() {
		Query query = new Query();
		QueryTranslator translator = new MockTranslator();
		Class what = java.lang.String.class;
		int start = 0;
		int limit = 10;
		Predicate p = new MockPredicate(1);
		String actual, expected;
		
		assertEquals(query, query.setPredicate(p));
		assertEquals(query, query.limit(start, limit));
		assertEquals(query, query.setDataType(what));
		
		try {
			actual = query.compile();
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

}
