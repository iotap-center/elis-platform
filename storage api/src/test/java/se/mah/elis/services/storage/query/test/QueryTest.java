package se.mah.elis.services.storage.query.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		
		query.setDataType(java.lang.Number.class);
		
		assertEquals(expected, query.getDataType());
	}
	@Test
	public void testSetDataTypeReset() {
		Query query = new Query();
		Class expected = java.lang.Number.class;

		query.setDataType(java.lang.String.class);
		query.setDataType(java.lang.Number.class);
		
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
		
		query.setTranslator(translator);
		query.setPredicate(p);
		query.limit(start, limit);
		query.setDataType(what);
		actual = query.compile();
		
		expected = "Translate:\n" +
				   "  what: java.lang.String\n" +
				   "  where: MockPredicate 1\n" +
				   "  limits: 0, 10\n" +
				   "  oldestFirst: true";
		
		assertEquals(expected, actual);
	}

}
