package se.mah.elis.impl.services.storage.query.test;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.exceptions.TypeMismatchException;
import se.mah.elis.impl.service.storage.test.mock.MockUser1;
import se.mah.elis.impl.services.storage.query.MySQLQueryTranslator;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.ChainingPredicate;
import se.mah.elis.services.storage.query.ChainingPredicate.ChainingType;
import se.mah.elis.services.storage.query.Query;
import se.mah.elis.services.storage.query.QueryTranslator;
import se.mah.elis.services.storage.query.SimplePredicate;
import se.mah.elis.services.storage.query.SimplePredicate.CriterionType;
import se.mah.elis.services.storage.query.UserPredicate;
import se.mah.elis.services.users.User;

public class MySQLQueryTranslatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCompileEmptyQuery() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "SELECT * FROM  ORDER BY `created` ASC;";
		String actual = "";
		
		try {
			actual = translator.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testWhat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		Class what = java.lang.Byte.class;
		String expected = "SELECT * FROM `java-lang-Byte` ORDER BY `created` ASC;";
		String actual = "";
		
		assertEquals(translator, translator.what(what));
		try {
			actual = translator.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testWhere() {
		QueryTranslator translator = new MySQLQueryTranslator();
		SimplePredicate where = new SimplePredicate(CriterionType.EQ);
		String expected = "SELECT * FROM  WHERE `foo` = 'bar' ORDER BY `created` ASC;";
		String actual = "";
		
		try {
			where.setField("foo");
			where.setCriterion("bar");
			assertEquals(translator, translator.where(where));
			actual = translator.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLimit() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "SELECT * FROM  LIMIT 1, 10 ORDER BY `created` ASC;";
		String actual = "";
		
		assertEquals(translator, translator.limit(1, 10));
		try {
			actual = translator.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLimitNegativeStartValue() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "SELECT * FROM  ORDER BY `created` ASC;";
		String actual = "";
		
		assertEquals(translator, translator.limit(-1, 10));
		try {
			actual = translator.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLimitNegativeLimit() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "SELECT * FROM  ORDER BY `created` ASC;";
		String actual = "";
		
		assertEquals(translator, translator.limit(1, -10));
		try {
			actual = translator.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testOrder() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "SELECT * FROM  ORDER BY `created` DESC;";
		String actual = "";
		
		assertEquals(translator, translator.order(false));
		try {
			actual = translator.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testOr() {
		QueryTranslator translator = new MySQLQueryTranslator();
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		SimplePredicate neq = new SimplePredicate(CriterionType.NEQ);
		String expected = "`foo` = 'bar' OR `a` <> 1";
		String actual = "";
		
		try {
			eq.setField("foo");
			eq.setCriterion("bar");
			neq.setField("a");
			neq.setCriterion(1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		actual = translator.or(eq, neq);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testAnd() {
		QueryTranslator translator = new MySQLQueryTranslator();
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		SimplePredicate neq = new SimplePredicate(CriterionType.NEQ);
		String expected = "`foo` = 'bar' AND `a` <> 1";
		String actual = "";
		
		try {
			eq.setField("foo");
			eq.setCriterion("bar");
			neq.setField("a");
			neq.setCriterion(1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		actual = translator.and(eq, neq);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testAndOrCombo() {
		QueryTranslator translator = new MySQLQueryTranslator();
		ChainingPredicate and = new ChainingPredicate(ChainingType.AND);
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		SimplePredicate neq = new SimplePredicate(CriterionType.NEQ);
		SimplePredicate gt = new SimplePredicate(CriterionType.GT);
		String expected = "(`foo` = 'bar' AND `a` <> 1) OR `b` > 0";
		String actual = "";
		
		try {
			eq.setField("foo");
			eq.setCriterion("bar");
			and.setLeft(eq);
			neq.setField("a");
			neq.setCriterion(1);
			and.setRight(neq);
			gt.setField("b");
			gt.setCriterion(0);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		actual = translator.or(and, gt);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testOrAndCombo() {
		QueryTranslator translator = new MySQLQueryTranslator();
		ChainingPredicate or = new ChainingPredicate(ChainingType.OR);
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		SimplePredicate neq = new SimplePredicate(CriterionType.NEQ);
		SimplePredicate gt = new SimplePredicate(CriterionType.GT);
		String expected = "(`foo` = 'bar' OR `a` <> 1) AND `b` > 0";
		String actual = "";
		
		try {
			eq.setField("foo");
			eq.setCriterion("bar");
			or.setLeft(eq);
			neq.setField("a");
			neq.setCriterion(1);
			or.setRight(neq);
			gt.setField("b");
			gt.setCriterion(0);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		actual = translator.and(or, gt);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testOrAndOrCombo() {
		QueryTranslator translator = new MySQLQueryTranslator();
		ChainingPredicate or = new ChainingPredicate(ChainingType.OR);
		ChainingPredicate or2 = new ChainingPredicate(ChainingType.OR);
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		SimplePredicate neq = new SimplePredicate(CriterionType.NEQ);
		SimplePredicate gt = new SimplePredicate(CriterionType.GT);
		String expected = "(`foo` = 'bar' OR `a` <> 1) AND " +
						  "(`b` > 0 OR `foo` = 'bar')";
		String actual = "";
		
		try {
			eq.setField("foo");
			eq.setCriterion("bar");
			or.setLeft(eq);
			neq.setField("a");
			neq.setCriterion(1);
			or.setRight(neq);
			gt.setField("b");
			gt.setCriterion(0);
			or2.setLeft(gt);
			or2.setRight(eq);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		actual = translator.and(or, or2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = 1";
		String actual = "";
		
		try {
			actual = translator.eq("foo", 1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = " + Long.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.eq("foo", Long.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = 1.1";
		String actual = "";
		
		try {
			actual = translator.eq("foo", 1.1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = " + Double.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.eq("foo", Double.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqBoolean() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = true";
		String actual = "";
		
		try {
			actual = translator.eq("foo", true);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = 66";
		String actual = "";
		
		try {
			actual = translator.eq("foo", 66);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqString() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = 'bar'";
		String actual = "";
		
		try {
			actual = translator.eq("foo", "bar");
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` = " + dt.getMillis();
		String actual = "";
		
		try {
			actual = translator.eq("foo", dt);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void testNeqInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> 1";
		String actual = "";
		
		try {
			actual = translator.neq("foo", 1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> " + Long.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.neq("foo", Long.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> 1.1";
		String actual = "";
		
		try {
			actual = translator.neq("foo", 1.1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> " + Double.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.neq("foo", Double.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqBoolean() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> true";
		String actual = "";
		
		try {
			actual = translator.neq("foo", true);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> 66";
		String actual = "";
		
		try {
			actual = translator.neq("foo", 66);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqString() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> 'bar'";
		String actual = "";
		
		try {
			actual = translator.neq("foo", "bar");
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` <> " + dt.getMillis();
		String actual = "";
		
		try {
			actual = translator.neq("foo", dt);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%1%'";
		String actual = "";
		
		try {
			actual = translator.like("foo", 1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%" + Long.MAX_VALUE + "%'";
		String actual = "";
		
		try {
			actual = translator.like("foo", Long.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%1.1%'";
		String actual = "";
		
		try {
			actual = translator.like("foo", 1.1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%" + Double.MAX_VALUE + "%'";
		String actual = "";
		
		try {
			actual = translator.like("foo", Double.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeBoolean() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%true%'";
		String actual = "";
		
		try {
			actual = translator.like("foo", true);
			fail("This shouldn't happen");
		} catch (TypeMismatchException e) {
		}
	}
	
	@Test
	public void testLikeByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%66%'";
		String actual = "";
		
		try {
			actual = translator.like("foo", 66);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeString() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%bar%'";
		String actual = "";
		
		try {
			actual = translator.like("foo", "bar");
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		
		try {
			translator.like("foo", dt);
			fail("This shouldn't happen");
		} catch (TypeMismatchException e) {
		}
	}
	
	@Test
	public void testLtInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` < 1";
		String actual = "";
		
		try {
			actual = translator.lt("foo", 1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` < " + Long.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.lt("foo", Long.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` < 1.1";
		String actual = "";
		
		try {
			actual = translator.lt("foo", 1.1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` < " + Double.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.lt("foo", Double.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtBoolean() {
		QueryTranslator translator = new MySQLQueryTranslator();
		
		try {
			translator.lt("foo", false);
			fail("This shouldn't happen");
		} catch (TypeMismatchException e) {
		}
	}
	
	@Test
	public void testLtByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` < 66";
		String actual = "";
		
		try {
			actual = translator.lt("foo", 66);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtString() {
		QueryTranslator translator = new MySQLQueryTranslator();
		
		try {
			translator.lt("foo", "bar");
			fail("This shouldn't happen");
		} catch (TypeMismatchException e) {
		}
	}
	
	@Test
	public void testLtDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` < " + dt.getMillis();
		String actual = "";
		
		try {
			actual = translator.lt("foo", dt);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <= 1";
		String actual = "";
		
		try {
			actual = translator.lte("foo", 1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <= " + Long.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.lte("foo", Long.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <= 1.1";
		String actual = "";
		
		try {
			actual = translator.lte("foo", 1.1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <= " + Double.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.lte("foo", Double.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteBoolean() {
		QueryTranslator translator = new MySQLQueryTranslator();
		
		try {
			translator.lte("foo", false);
			fail("This shouldn't happen");
		} catch (TypeMismatchException e) {
		}
	}
	
	@Test
	public void testLteByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <= 66";
		String actual = "";
		
		try {
			actual = translator.lte("foo", 66);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteString() {
		QueryTranslator translator = new MySQLQueryTranslator();
		
		try {
			translator.lte("foo", "bar");
			fail("This shouldn't happen");
		} catch (TypeMismatchException e) {
		}
	}
	
	@Test
	public void testLteDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` <= " + dt.getMillis();
		String actual = "";
		
		try {
			actual = translator.lte("foo", dt);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGtInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` > 1";
		String actual = "";
		
		try {
			actual = translator.gt("foo", 1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);		
	}
	
	@Test
	public void testGtLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` > " + Long.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.gt("foo", Long.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGtFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` > 1.1";
		String actual = "";
		
		try {
			actual = translator.gt("foo", 1.1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGtDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` > " + Double.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.gt("foo", Double.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGtBoolean() {
		QueryTranslator translator = new MySQLQueryTranslator();
		
		try {
			translator.gt("foo", false);
			fail("This shouldn't happen");
		} catch (TypeMismatchException e) {
		}
	}
	
	@Test
	public void testGtByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` > 66";
		String actual = "";
		
		try {
			actual = translator.gt("foo", 66);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGtString() {
		QueryTranslator translator = new MySQLQueryTranslator();
		
		try {
			translator.gt("foo", "bar");
			fail("This shouldn't happen");
		} catch (TypeMismatchException e) {
		}
	}
	
	@Test
	public void testGtDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` > " + dt.getMillis();
		String actual = "";
		
		try {
			actual = translator.gt("foo", dt);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGteInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` >= 1";
		String actual = "";
		
		try {
			actual = translator.gte("foo", 1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGteLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` >= " + Long.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.gte("foo", Long.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGteFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` >= 1.1";
		String actual = "";
		
		try {
			actual = translator.gte("foo", 1.1);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGteDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` >= " + Double.MAX_VALUE;
		String actual = "";
		
		try {
			actual = translator.gte("foo", Double.MAX_VALUE);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGteBoolean() {
		QueryTranslator translator = new MySQLQueryTranslator();
		
		try {
			translator.gte("foo", false);
			fail("This shouldn't happen");
		} catch (TypeMismatchException e) {
		}
	}
	
	@Test
	public void testGteByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` >= 66";
		String actual = "";
		
		try {
			actual = translator.gte("foo", 66);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGteString() {
		QueryTranslator translator = new MySQLQueryTranslator();
	
		try {
			translator.gte("foo", "bar");
			fail("This shouldn't happen");
		} catch (TypeMismatchException e) {
		}
	}
	
	@Test
	public void testGteDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` >= " + dt.getMillis();
		String actual = "";
		
		try {
			actual = translator.gte("foo", dt);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUser() {
		QueryTranslator translator = new MySQLQueryTranslator();
		User user = new MockUser1();
		String expected = "(`uuid` = '00001111-2222-dead-beef-555566667777' " +
				  "AND `service_name` = 'test' AND `id_number` = 1 " +
				  "AND `username` = 'Batman' AND `password` = 'Robin' " +
				  "AND `whatever` = 0 AND `created` = " + 
				  user.created().getMillis() + ")";
		String actual = "";
		
		try {
			actual = translator.user(user);
		} catch (TypeMismatchException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCompileComplexPredicate() {
		QueryTranslator translator = new MySQLQueryTranslator();
		Class what = java.lang.String.class;
		ChainingPredicate or = new ChainingPredicate(ChainingType.OR);
		ChainingPredicate or2 = new ChainingPredicate(ChainingType.OR);
		ChainingPredicate and = new ChainingPredicate(ChainingType.AND);
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		SimplePredicate neq = new SimplePredicate(CriterionType.NEQ);
		SimplePredicate gt = new SimplePredicate(CriterionType.GT);
		String expected = "SELECT * FROM `java-lang-String` WHERE " +
						  "(`foo` = 'bar' OR `a` <> 1) AND " +
						  "(`b` > 0 OR `foo` = 'bar') ORDER BY `created` ASC;";
		String actual = "";
		
		try {
			eq.setField("foo");
			eq.setCriterion("bar");
			or.setLeft(eq);
			neq.setField("a");
			neq.setCriterion(1);
			or.setRight(neq);
			gt.setField("b");
			gt.setCriterion(0);
			or2.setLeft(gt);
			or2.setRight(eq);
			and.setLeft(or);
			and.setRight(or2);
			assertEquals(translator, translator.where(and));
			assertEquals(translator, translator.what(what));
			actual = translator.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCompileQuery() {
		Query query = new Query();
		QueryTranslator translator = new MySQLQueryTranslator();
		Class what = java.lang.String.class;
		ChainingPredicate or = new ChainingPredicate(ChainingType.OR);
		ChainingPredicate or2 = new ChainingPredicate(ChainingType.OR);
		ChainingPredicate and = new ChainingPredicate(ChainingType.AND);
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		SimplePredicate neq = new SimplePredicate(CriterionType.NEQ);
		SimplePredicate gt = new SimplePredicate(CriterionType.GT);
		String expected = "SELECT * FROM `java-lang-String` WHERE " +
						  "(`foo` = 'bar' OR `a` <> 1) AND " +
						  "(`b` > 0 OR `foo` = 'bar') LIMIT 0, 10 ORDER BY `created` DESC;";
		String actual = "";
		
		try {
			eq.setField("foo");
			eq.setCriterion("bar");
			or.setLeft(eq);
			neq.setField("a");
			neq.setCriterion(1);
			or.setRight(neq);
			gt.setField("b");
			gt.setCriterion(0);
			or2.setLeft(gt);
			or2.setRight(eq);
			and.setLeft(or);
			and.setRight(or2);
			assertEquals(translator, translator.where(and));
			assertEquals(translator, translator.what(what));
			query.setTranslator(translator);
			query.setDataType(what);
			query.setPredicate(and);
			query.limit(0, 10);
			query.setOrder(false);
			actual = query.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCompileQueryWithUser() {
		Query query = new Query();
		QueryTranslator translator = new MySQLQueryTranslator();
		Class what = java.lang.String.class;
		ChainingPredicate or = new ChainingPredicate(ChainingType.OR);
		ChainingPredicate or2 = new ChainingPredicate(ChainingType.OR);
		ChainingPredicate or3 = new ChainingPredicate(ChainingType.OR);
		ChainingPredicate and = new ChainingPredicate(ChainingType.AND);
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		SimplePredicate neq = new SimplePredicate(CriterionType.NEQ);
		SimplePredicate gt = new SimplePredicate(CriterionType.GT);
		UserPredicate up = new UserPredicate();
		User user = new MockUser1();
		String expected = "SELECT * FROM `java-lang-String` WHERE " +
						  "(`foo` = 'bar' OR `a` <> 1) AND " +
						  "((`uuid` = '00001111-2222-dead-beef-555566667777' " +
						  "AND `service_name` = 'test' AND `id_number` = 1 " +
						  "AND `username` = 'Batman' AND `password` = 'Robin' " +
						  "AND `whatever` = 0 AND `created` = " + 
						  user.created().getMillis() + ") OR " +
						  "(`b` > 0 OR `foo` = 'bar')) LIMIT 0, 10 ORDER BY `created` DESC;";
		String actual = "";
		
		try {
			eq.setField("foo");
			eq.setCriterion("bar");
			or.setLeft(eq);
			neq.setField("a");
			neq.setCriterion(1);
			or.setRight(neq);
			gt.setField("b");
			gt.setCriterion(0);
			or2.setLeft(gt);
			or2.setRight(eq);
			up.setUser(user);
			or3.setLeft(up);
			or3.setRight(or2);
			and.setLeft(or);
			and.setRight(or3);
			assertEquals(translator, translator.where(and));
			assertEquals(translator, translator.what(what));
			query.setTranslator(translator);
			query.setDataType(what);
			query.setPredicate(and);
			query.limit(0, 10);
			query.setOrder(false);
			actual = query.compile();
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCompileDeleteQuery() {
		MySQLQueryTranslator translator = new MySQLQueryTranslator();
		Class what = java.lang.String.class;
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		String actual = null;
		String expected = "DELETE FROM `java-lang-String` WHERE " +
						  "`foo` = 'bar';";
		
		eq.setField("foo");
		eq.setCriterion("bar");
		translator.what(what);
		translator.where(eq);
		try {
			actual = translator.compileDeleteQuery();
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCompileDeleteQueryWithUser() {
		MySQLQueryTranslator translator = new MySQLQueryTranslator();
		Class what = java.lang.String.class;
		MockUser1 user = new MockUser1();
		UserPredicate up = new UserPredicate(user);
		String actual = null;
		String expected = "DELETE FROM `java-lang-String` WHERE " +
						  "(`uuid` = '00001111-2222-dead-beef-555566667777' " +
						  "AND `service_name` = 'test' AND `id_number` = 1 " +
						  "AND `username` = 'Batman' AND `password` = 'Robin' " +
						  "AND `whatever` = 0 AND `created` = " + 
						  user.created().getMillis() + ");";
		
		
		translator.what(what);
		translator.where(up);
		try {
			actual = translator.compileDeleteQuery();
		} catch (StorageException e) {
			e.printStackTrace();
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, actual);
	}
}
