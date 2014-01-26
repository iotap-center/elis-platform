package se.mah.elis.services.storage.query.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.storage.query.ChainingPredicate;
import se.mah.elis.services.storage.query.ChainingPredicate.Type;
import se.mah.elis.services.storage.query.test.mock.MockPredicate;
import se.mah.elis.services.storage.query.test.mock.MockTranslator;

public class ChainingPredicateTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		ChainingPredicate cp = null;
	}
	
	@Test
	public void testAnd() {
		ChainingPredicate cp = new ChainingPredicate(Type.AND);
		String expected = "AND:\n" +
						  "  left: null\n" +
						  "  right: null\n" +
						  "  translator: null";
		
		assertEquals(expected, cp.toString());
	}
	
	@Test
	public void testOr() {
		ChainingPredicate cp = new ChainingPredicate(Type.OR);
		String expected = "OR:\n" +
						  "  left: null\n" +
						  "  right: null\n" +
						  "  translator: null";
		
		assertEquals(expected, cp.toString());
	}

	@Test
	public void testSetLeft() {
		ChainingPredicate cp = new ChainingPredicate(Type.AND);
		String expected = "AND:\n" +
						  "  left: MockPredicate 1\n" +
						  "  right: null\n" +
						  "  translator: null";
		
		cp.setLeft(new MockPredicate(1));
		
		assertEquals(expected, cp.toString());
	}

	@Test
	public void testSetLeftReset() {
		ChainingPredicate cp = new ChainingPredicate(Type.AND);
		String expected = "AND:\n" +
						  "  left: MockPredicate 2\n" +
						  "  right: null\n" +
						  "  translator: null";

		cp.setLeft(new MockPredicate(1));
		cp.setLeft(new MockPredicate(2));
		
		assertEquals(expected, cp.toString());
	}

	@Test
	public void testSetRight() {
		ChainingPredicate cp = new ChainingPredicate(Type.AND);
		String expected = "AND:\n" +
						  "  left: null\n" +
						  "  right: MockPredicate 1\n" +
						  "  translator: null";
		
		cp.setRight(new MockPredicate(1));
		
		assertEquals(expected, cp.toString());
	}

	@Test
	public void testSetRightReset() {
		ChainingPredicate cp = new ChainingPredicate(Type.AND);
		String expected = "AND:\n" +
						  "  left: null\n" +
						  "  right: MockPredicate 2\n" +
						  "  translator: null";

		cp.setRight(new MockPredicate(1));
		cp.setRight(new MockPredicate(2));
		
		assertEquals(expected, cp.toString());
	}

	@Test
	public void testSetRightAndSetLeft() {
		ChainingPredicate cp = new ChainingPredicate(Type.AND);
		String expected = "AND:\n" +
						  "  left: MockPredicate 1\n" +
						  "  right: MockPredicate 2\n" +
						  "  translator: null";

		cp.setLeft(new MockPredicate(1));
		cp.setRight(new MockPredicate(2));
		
		assertEquals(expected, cp.toString());
	}

	@Test
	public void testSetTranslator() {
		ChainingPredicate cp = new ChainingPredicate(Type.AND);
		String expected = "AND:\n" +
						  "  left: null\n" +
						  "  right: null\n" +
						  "  translator: Translate:\n" +
						  "  oldestFirst: false";
		
		cp.setTranslator(new MockTranslator());
		
		assertEquals(expected, cp.toString());
	}

	@Test
	public void testCompile() {
		ChainingPredicate cp = new ChainingPredicate(Type.AND);
		String expected = "AND:\n" +
						  "  left: MockPredicate 1\n" +
						  "  right: MockPredicate 2\n" +
						  "  translator: Translate:\n" +
						  "  oldestFirst: false";

		cp.setLeft(new MockPredicate(1));
		cp.setRight(new MockPredicate(2));
		cp.setTranslator(new MockTranslator());
		
		assertEquals(expected, cp.toString());
	}	
}
