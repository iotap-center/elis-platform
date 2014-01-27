package se.mah.elis.services.storage.query.test;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.SimplePredicate;
import se.mah.elis.services.storage.query.SimplePredicate.CriterionType;
import se.mah.elis.services.storage.query.test.mock.MockTranslator;
import se.mah.elis.services.storage.query.test.mock.MockUserIdentifier;

public class SimplePredicateTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEQ() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testNEQ() {
		SimplePredicate sp = new SimplePredicate(CriterionType.NEQ);
		String expected = "NEQ:\n" +
						  "  field: null\n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testLIKE() {
		SimplePredicate sp = new SimplePredicate(CriterionType.LIKE);
		String expected = "LIKE:\n" +
						  "  field: null\n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testLT() {
		SimplePredicate sp = new SimplePredicate(CriterionType.LT);
		String expected = "LT:\n" +
						  "  field: null\n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testLTE() {
		SimplePredicate sp = new SimplePredicate(CriterionType.LTE);
		String expected = "LTE:\n" +
						  "  field: null\n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testGT() {
		SimplePredicate sp = new SimplePredicate(CriterionType.GT);
		String expected = "GT:\n" +
						  "  field: null\n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testGTE() {
		SimplePredicate sp = new SimplePredicate(CriterionType.GTE);
		String expected = "GTE:\n" +
						  "  field: null\n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(expected, sp.toString());
	}
	
	@Test
	public void testSetField() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: TEST\n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(sp, sp.setField("TEST"));
		
		assertEquals(expected, sp.toString());
	}
	
	@Test
	public void testSetFieldReset() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: RESET\n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(sp, sp.setField("TEST"));
		assertEquals(sp, sp.setField("RESET"));
		
		assertEquals(expected, sp.toString());
	}
	
	@Test
	public void testSetFieldEmptyString() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: \n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(sp, sp.setField("TEST"));
		assertEquals(sp, sp.setField(""));
		
		assertEquals(expected, sp.toString());
	}
	
	@Test
	public void testSetFieldNull() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: null\n" +
						  "  translator: null";
		
		assertEquals(sp, sp.setField("TEST"));
		assertEquals(sp, sp.setField(null));
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionBoolean() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: true\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(true));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionBooleanReset() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: false\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(true));
			assertEquals(sp, sp.setCriterion(false));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionFloat() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: 1.1\n" +
						  "  translator: null";
	
		try {
			assertEquals(sp, sp.setCriterion(1.1));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
	
		assertEquals(expected, sp.toString());
	}
	
	@Test
	public void testSetCriterionFloatReset() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: 1.2\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(1.1));
			assertEquals(sp, sp.setCriterion(1.2));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionDouble() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: " + Double.MAX_VALUE + "\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(Double.MAX_VALUE));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionDoubleReset() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: " + (Double.MAX_VALUE - 1) + "\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(Double.MAX_VALUE));
			assertEquals(sp, sp.setCriterion(Double.MAX_VALUE - 1));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionInt() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: 13\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(13));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionIntReset() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: 42\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(13));
			assertEquals(sp, sp.setCriterion(42));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionLong() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: " +  Long.MAX_VALUE + "\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(Long.MAX_VALUE));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionLongReset() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: " + Long.MIN_VALUE + "\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(Long.MAX_VALUE));
			assertEquals(sp, sp.setCriterion(Long.MIN_VALUE));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionByte() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: 65\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion('A'));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionByteReset() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: 66\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion('A'));
			assertEquals(sp, sp.setCriterion('B'));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionString() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: foo\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion("foo"));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionStringReset() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: bar\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion("foo"));
			assertEquals(sp, sp.setCriterion("bar"));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}
	
	@Test
	public void testSetCriterionDateTime() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		DateTime dt = DateTime.now();
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: " + dt.toString() + "\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(dt));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testSetCriterionDateTimeReset() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		DateTime dt = DateTime.now();
		DateTime dt2 = dt.plusHours(1);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: " + dt2.toString() + "\n" +
						  "  translator: null";
		
		try {
			assertEquals(sp, sp.setCriterion(dt));
			assertEquals(sp, sp.setCriterion(dt2));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		assertEquals(expected, sp.toString());
	}
	
	@Test
	public void testEqSetCriterion() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		
		try {
			sp.setCriterion(1); // Integer
			sp.setCriterion(Long.MAX_VALUE); // Long
			sp.setCriterion(1.1); // Float
			sp.setCriterion(Double.MAX_VALUE); // Double
			sp.setCriterion(Byte.SIZE); // Byte
			sp.setCriterion(true); // Boolean
			sp.setCriterion(DateTime.now()); // DateTime
			sp.setCriterion("foo"); // String
			sp.setCriterion(new MockUserIdentifier()); // UserIdentifier
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
	}
	
	@Test
	public void testNeqSetCriterion() {
		SimplePredicate sp = new SimplePredicate(CriterionType.NEQ);
		
		try {
			sp.setCriterion(1); // Integer
			sp.setCriterion(Long.MAX_VALUE); // Long
			sp.setCriterion(1.1); // Float
			sp.setCriterion(Double.MAX_VALUE); // Double
			sp.setCriterion(Byte.SIZE); // Byte
			sp.setCriterion(true); // Boolean
			sp.setCriterion(DateTime.now()); // DateTime
			sp.setCriterion("foo"); // String
			sp.setCriterion(new MockUserIdentifier()); // UserIdentifier
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
	}
	
	@Test
	public void testLikeSetCriterion() {
		SimplePredicate sp = new SimplePredicate(CriterionType.LIKE);
		
		try {
			sp.setCriterion(1); // Integer
			sp.setCriterion(Long.MAX_VALUE); // Long
			sp.setCriterion(1.1); // Float
			sp.setCriterion(Double.MAX_VALUE); // Double
			sp.setCriterion(Byte.SIZE); // Byte
			sp.setCriterion(DateTime.now()); // DateTime
			sp.setCriterion("foo"); // String
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		try {
			sp.setCriterion(true); // Boolean
			sp.setCriterion(new MockUserIdentifier()); // UserIdentifier
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}
	
	@Test
	public void testLtSetCriterion() {
		SimplePredicate sp = new SimplePredicate(CriterionType.LT);
		
		try {
			sp.setCriterion(1); // Integer
			sp.setCriterion(Long.MAX_VALUE); // Long
			sp.setCriterion(1.1); // Float
			sp.setCriterion(Double.MAX_VALUE); // Double
			sp.setCriterion(Byte.SIZE); // Byte
			sp.setCriterion(DateTime.now()); // DateTime
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		try {
			sp.setCriterion(true); // Boolean
			sp.setCriterion("foo"); // String
			sp.setCriterion(new MockUserIdentifier()); // UserIdentifier
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}
	
	@Test
	public void testLteSetCriterion() {
		SimplePredicate sp = new SimplePredicate(CriterionType.LTE);
		
		try {
			sp.setCriterion(1); // Integer
			sp.setCriterion(Long.MAX_VALUE); // Long
			sp.setCriterion(1.1); // Float
			sp.setCriterion(Double.MAX_VALUE); // Double
			sp.setCriterion(Byte.SIZE); // Byte
			sp.setCriterion(DateTime.now()); // DateTime
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		try {
			sp.setCriterion(true); // Boolean
			sp.setCriterion("foo"); // String
			sp.setCriterion(new MockUserIdentifier()); // UserIdentifier
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}
	
	@Test
	public void testGtSetCriterion() {
		SimplePredicate sp = new SimplePredicate(CriterionType.GT);
		
		try {
			sp.setCriterion(1); // Integer
			sp.setCriterion(Long.MAX_VALUE); // Long
			sp.setCriterion(1.1); // Float
			sp.setCriterion(Double.MAX_VALUE); // Double
			sp.setCriterion(Byte.SIZE); // Byte
			sp.setCriterion(DateTime.now()); // DateTime
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		try {
			sp.setCriterion(true); // Boolean
			sp.setCriterion("foo"); // String
			sp.setCriterion(new MockUserIdentifier()); // UserIdentifier
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}
	
	@Test
	public void testGteSetCriterion() {
		SimplePredicate sp = new SimplePredicate(CriterionType.GTE);
		
		try {
			sp.setCriterion(1); // Integer
			sp.setCriterion(Long.MAX_VALUE); // Long
			sp.setCriterion(1.1); // Float
			sp.setCriterion(Double.MAX_VALUE); // Double
			sp.setCriterion(Byte.SIZE); // Byte
			sp.setCriterion(DateTime.now()); // DateTime
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		
		try {
			sp.setCriterion(true); // Boolean
			sp.setCriterion("foo"); // String
			sp.setCriterion(new MockUserIdentifier()); // UserIdentifier
			fail("This shouldn't happen");
		} catch (StorageException e) {
		}
	}

	@Test
	public void testSetTranslator() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: null\n" +
						  "  criterion: null\n" +
						  "  translator: Translate:\n" +
						  "  oldestFirst: false";
		
		sp.setTranslator(new MockTranslator());
		
		assertEquals(expected, sp.toString());
	}

	@Test
	public void testCompile() {
		SimplePredicate sp = new SimplePredicate(CriterionType.EQ);
		String expected = "EQ:\n" +
						  "  field: Foo\n" +
						  "  criterion: Bar\n" +
						  "  translator: Translate:\n" +
						  "  oldestFirst: false";
		
		assertEquals(sp, sp.setField("Foo"));
		try {
			assertEquals(sp, sp.setCriterion("Bar"));
		} catch (StorageException e) {
			fail("This shouldn't happen");
		}
		sp.setTranslator(new MockTranslator());
		
		assertEquals(expected, sp.toString());
	}

}
