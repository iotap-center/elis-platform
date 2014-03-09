package se.mah.elis.impl.services.storage.query.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.impl.services.storage.query.DeleteQuery;
import se.mah.elis.impl.services.storage.query.MySQLQueryTranslator;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.QueryTranslator;
import se.mah.elis.services.storage.query.SimplePredicate;
import se.mah.elis.services.storage.query.SimplePredicate.CriterionType;

public class DeleteQueryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCompileDeleteQuery() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DeleteQuery query = new DeleteQuery();
		Class what = java.lang.String.class;
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		String actual = null;
		String expected = "DELETE FROM `java-lang-String` WHERE " +
						  "`foo` = 'bar';";

		eq.setField("foo");
		eq.setCriterion("bar");
		query.setDataType(what);
		query.setPredicate(eq);
		query.setTranslator(translator);
		try {
			actual = query.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}

}
