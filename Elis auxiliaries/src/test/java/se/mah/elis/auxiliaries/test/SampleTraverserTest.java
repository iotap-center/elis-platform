package se.mah.elis.auxiliaries.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.auxiliaries.SampleTraverser;
import se.mah.elis.auxiliaries.data.Sample;
import se.mah.elis.auxiliaries.exceptions.MismatchingSampleException;
import se.mah.elis.auxiliaries.test.mocks.ElectricitySampleMock;
import se.mah.elis.auxiliaries.test.mocks.SimpleSampleMock;

public class SampleTraverserTest {
	
	private List<Sample> list;

	@Before
	public void setUp() throws Exception {
		list = new ArrayList<Sample>();

		list.add(new ElectricitySampleMock(1));
		list.add(new ElectricitySampleMock(2));
		list.add(new ElectricitySampleMock(4));
		list.add(new ElectricitySampleMock(8));
		list.add(new ElectricitySampleMock(16));
	}

	@After
	public void tearDown() throws Exception {
		list = null;
	}
	
	@Test
	public void testEmptyList() {
		list.clear();
		
		try {
			Sample[] samples = new Sample[list.size()];
			samples = list.toArray(samples);
			assertEquals(0, SampleTraverser.sumUp(samples, "getCurrentCurrent"), 0);
		} catch (MismatchingSampleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Mismatch");
		}
	}
	
	@Test
	public void testUnaryList() {
		list.clear();
		
		list.add(new ElectricitySampleMock(1));
		try {
			Sample[] samples = new Sample[list.size()];
			samples = list.toArray(samples);
			assertEquals(1, SampleTraverser.sumUp(samples, "getCurrentCurrent"), 0);
		} catch (MismatchingSampleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Mismatch");
		}
	}
	
	@Test
	public void testBinaryList() {
		list.clear();
		list.add(new ElectricitySampleMock(1));
		list.add(new ElectricitySampleMock(2));
		
		try {
			Sample[] samples = new Sample[list.size()];
			samples = list.toArray(samples);
			assertEquals(3, SampleTraverser.sumUp(samples, "getCurrentCurrent"), 0);
		} catch (MismatchingSampleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Mismatch");
		}
	}
	
	@Test
	public void testMixedList() {
		list.add(new SimpleSampleMock());
		
		try {
			Sample[] samples = new Sample[list.size()];
			samples = list.toArray(samples);
			SampleTraverser.sumUp(samples, "getCurrentCurrent");
			fail();
		} catch (MismatchingSampleException e) {
			// Expected behaviour
		}
	}
	
	@Test
	public void testSumUpPower() {
		try {
			Sample[] samples = new Sample[list.size()];
			samples = list.toArray(samples);
			assertEquals(31, SampleTraverser.sumUp(samples, "getCurrentPower"), 0);
		} catch (MismatchingSampleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Mismatch");
		}
	}
	
	@Test
	public void testSumUpCurrent() {
		try {
			Sample[] samples = new Sample[list.size()];
			samples = list.toArray(samples);
			assertEquals(31, SampleTraverser.sumUp(samples, "getCurrentCurrent"), 0);
		} catch (MismatchingSampleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Mismatch");
		}
	}
	
	@Test
	public void testSumUpEnergyUsage() {
		try {
			Sample[] samples = new Sample[list.size()];
			samples = list.toArray(samples);
			assertEquals(10*(16*16+8*8+4*4+2*2+1), SampleTraverser.sumUp(samples, "getTotalEnergyUsageInJoules"), 0);
		} catch (MismatchingSampleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Mismatch");
		}
	}

	@Test
	public void testFindHighestValue() {
		list.add(new ElectricitySampleMock(3));
		
		try {
			Sample[] samples = new Sample[list.size()];
			samples = list.toArray(samples);
			assertEquals(16, SampleTraverser.findHighestValue(samples, "getCurrentVoltage"), 0);
		} catch (MismatchingSampleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Mismatch");
		}
	}

	@Test
	public void testFindLowestValue() {
		try {
			Sample[] samples = new Sample[list.size()];
			samples = list.toArray(samples);
			assertEquals(1, SampleTraverser.findLowestValue(samples, "getCurrentVoltage"), 0);
		} catch (MismatchingSampleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Mismatch");
		}
	}

}
