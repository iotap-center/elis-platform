package se.mah.elis.impl.services.storage.query.test;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.impl.services.storage.query.DeleteQuery;
import se.mah.elis.impl.services.storage.query.MySQLQueryTranslator;
import se.mah.elis.services.storage.query.ChainingPredicate;
import se.mah.elis.services.storage.query.ChainingPredicate.ChainingType;
import se.mah.elis.services.storage.query.Query;
import se.mah.elis.services.storage.query.QueryTranslator;
import se.mah.elis.services.storage.query.SimplePredicate;
import se.mah.elis.services.storage.query.SimplePredicate.CriterionType;

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
		String expected = "SELECT * FROM  ASC;";
		String actual = translator.compile();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testWhat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		Class what = java.lang.Byte.class;
		String expected = "SELECT * FROM `java-lang-Byte` ASC;";
		String actual;
		
		assertEquals(translator, translator.what(what));
		actual = translator.compile();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testWhere() {
		QueryTranslator translator = new MySQLQueryTranslator();
		SimplePredicate where = new SimplePredicate(CriterionType.EQ);
		String expected = "SELECT * FROM  WHERE `foo` = 'bar' ASC;";
		String actual;
		
		where.setField("foo");
		where.setCriterion("bar");
		assertEquals(translator, translator.where(where));
		actual = translator.compile();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLimit() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "SELECT * FROM  LIMIT 1, 10 ASC;";
		String actual;
		
		assertEquals(translator, translator.limit(1, 10));
		actual = translator.compile();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLimitNegativeStartValue() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "SELECT * FROM  ASC;";
		String actual;
		
		assertEquals(translator, translator.limit(-1, 10));
		actual = translator.compile();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLimitNegativeLimit() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "SELECT * FROM  ASC;";
		String actual;
		
		assertEquals(translator, translator.limit(1, -10));
		actual = translator.compile();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testOrder() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "SELECT * FROM  DESC;";
		String actual;
		
		assertEquals(translator, translator.order(false));
		actual = translator.compile();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testOr() {
		QueryTranslator translator = new MySQLQueryTranslator();
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		SimplePredicate neq = new SimplePredicate(CriterionType.NEQ);
		String expected = "`foo` = 'bar' OR `a` <> 1";
		String actual;
		
		eq.setField("foo");
		eq.setCriterion("bar");
		neq.setField("a");
		neq.setCriterion(1);
		actual = translator.or(eq, neq);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testAnd() {
		QueryTranslator translator = new MySQLQueryTranslator();
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		SimplePredicate neq = new SimplePredicate(CriterionType.NEQ);
		String expected = "`foo` = 'bar' AND `a` <> 1";
		String actual;
		
		eq.setField("foo");
		eq.setCriterion("bar");
		neq.setField("a");
		neq.setCriterion(1);
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
		String actual;
		
		eq.setField("foo");
		eq.setCriterion("bar");
		and.setLeft(eq);
		neq.setField("a");
		neq.setCriterion(1);
		and.setRight(neq);
		gt.setField("b");
		gt.setCriterion(0);
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
		String actual;
		
		eq.setField("foo");
		eq.setCriterion("bar");
		or.setLeft(eq);
		neq.setField("a");
		neq.setCriterion(1);
		or.setRight(neq);
		gt.setField("b");
		gt.setCriterion(0);
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
		String actual;
		
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
		actual = translator.and(or, or2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = 1";
		String actual = translator.eq("foo", 1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = " + Long.MAX_VALUE;
		String actual = translator.eq("foo", Long.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = 1.1";
		String actual = translator.eq("foo", 1.1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = " + Double.MAX_VALUE;
		String actual = translator.eq("foo", Double.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqBoolean() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = true";
		String actual = translator.eq("foo", true);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = 66";
		String actual = translator.eq("foo", 66);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqString() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` = 'bar'";
		String actual = translator.eq("foo", "bar");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` = " + dt.getMillis();
		String actual = translator.eq("foo", dt);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEqUserIdentifier() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testNeqInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> 1";
		String actual = translator.neq("foo", 1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> " + Long.MAX_VALUE;
		String actual = translator.neq("foo", Long.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> 1.1";
		String actual = translator.neq("foo", 1.1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> " + Double.MAX_VALUE;
		String actual = translator.neq("foo", Double.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqBoolean() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> true";
		String actual = translator.neq("foo", true);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> 66";
		String actual = translator.neq("foo", 66);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqString() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <> 'bar'";
		String actual = translator.neq("foo", "bar");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` <> " + dt.getMillis();
		String actual = translator.neq("foo", dt);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNeqUserIdentifier() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testLikeInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%1%'";
		String actual = translator.like("foo", 1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%" + Long.MAX_VALUE + "%'";
		String actual = translator.like("foo", Long.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%1.1%'";
		String actual = translator.like("foo", 1.1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%" + Double.MAX_VALUE + "%'";
		String actual = translator.like("foo", Double.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeBoolean() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%true%'";
		String actual = translator.like("foo", true);
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testLikeByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%66%'";
		String actual = translator.like("foo", 66);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeString() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` LIKE '%bar%'";
		String actual = translator.like("foo", "bar");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLikeDateTime() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testLikeUserIdentifier() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testLtInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` < 1";
		String actual = translator.lt("foo", 1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` < " + Long.MAX_VALUE;
		String actual = translator.lt("foo", Long.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` < 1.1";
		String actual = translator.lt("foo", 1.1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` < " + Double.MAX_VALUE;
		String actual = translator.lt("foo", Double.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtBoolean() {
		fail("Need to figure this one out.");
	}
	
	@Test
	public void testLtByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` < 66";
		String actual = translator.lt("foo", 66);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtString() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testLtDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` < " + dt.getMillis();
		String actual = translator.lt("foo", dt);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLtUserIdentifier() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testLteInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <= 1";
		String actual = translator.lte("foo", 1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <= " + Long.MAX_VALUE;
		String actual = translator.lte("foo", Long.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <= 1.1";
		String actual = translator.lte("foo", 1.1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <= " + Double.MAX_VALUE;
		String actual = translator.lte("foo", Double.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteBoolean() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testLteByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` <= 66";
		String actual = translator.lte("foo", 66);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteString() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testLteDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` <= " + dt.getMillis();
		String actual = translator.lte("foo", dt);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLteUserIdentifier() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testGtInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` > 1";
		String actual = translator.gt("foo", 1);
		
		assertEquals(expected, actual);		
	}
	
	@Test
	public void testGtLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` > " + Long.MAX_VALUE;
		String actual = translator.gt("foo", Long.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGtFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` > 1.1";
		String actual = translator.gt("foo", 1.1);
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGtDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` > " + Double.MAX_VALUE;
		String actual = translator.gt("foo", Double.MAX_VALUE);
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGtBoolean() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testGtByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` > 66";
		String actual = translator.gt("foo", 66);
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGtString() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testGtDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` > " + dt.getMillis();
		String actual = translator.gt("foo", dt);
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGtUserIdentifier() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testGteInt() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` >= 1";
		String actual = translator.gte("foo", 1);
		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGteLong() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` >= " + Long.MAX_VALUE;
		String actual = translator.gte("foo", Long.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGteFloat() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` >= 1.1";
		String actual = translator.gte("foo", 1.1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGteDouble() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` >= " + Double.MAX_VALUE;
		String actual = translator.gte("foo", Double.MAX_VALUE);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGteBoolean() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testGteByte() {
		QueryTranslator translator = new MySQLQueryTranslator();
		String expected = "`foo` >= 66";
		String actual = translator.gte("foo", 66);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGteString() {
		fail("Need to figure this one out");
	}
	
	@Test
	public void testGteDateTime() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DateTime dt = DateTime.now();
		String expected = "`foo` >= " + dt.getMillis();
		String actual = translator.gte("foo", dt);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGteUserIdentifier() {
		fail("Need to figure this one out");
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
		String expected = "SELECT * FROM java-lang-String WHERE " +
						  "(`foo` = 'bar' OR `a` <> 1) AND " +
						  "(`b` > 0 OR `foo` = 'bar') ASC;";
		String actual;
		
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
		String expected = "SELECT * FROM java-lang-String WHERE " +
						  "(`foo` = 'bar' OR `a` <> 1) AND " +
						  "(`b` > 0 OR `foo` = 'bar') LIMIT 0, 10 DESC;";
		String actual;
		
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
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCompileDeleteQuery() {
		QueryTranslator translator = new MySQLQueryTranslator();
		DeleteQuery query = new DeleteQuery();
		Class what = java.lang.String.class;
		SimplePredicate eq = new SimplePredicate(CriterionType.EQ);
		String actual = null;
		String expected = "DELETE FROM java-lang-String WHERE " +
						  "`foo` = bar;";
		
		query.setDataType(what);
		query.setPredicate(eq);
		actual = translator.compile();
		
		assertEquals(expected, actual);
	}
}
