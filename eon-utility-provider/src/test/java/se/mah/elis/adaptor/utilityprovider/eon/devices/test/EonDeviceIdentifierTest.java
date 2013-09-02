package se.mah.elis.adaptor.utilityprovider.eon.devices.test;

import static org.junit.Assert.*;

import org.junit.Test;

import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonDeviceIdentifier;

public class EonDeviceIdentifierTest {

	@Test
	public void testEqualsTrue() {
		EonDeviceIdentifier dev1 = new EonDeviceIdentifier("apa");
		EonDeviceIdentifier dev2 = new EonDeviceIdentifier("apa");
		assertTrue(dev1.equals(dev2));
	}
	
	@Test 
	public void testEqualsFalse() {
		EonDeviceIdentifier dev1 = new EonDeviceIdentifier("apa");
		EonDeviceIdentifier dev2 = new EonDeviceIdentifier("not a monkey");
		assertFalse(dev1.equals(dev2));
	}
	
	@Test 
	public void testEqualsFalseDifferentTypes() {
		EonDeviceIdentifier dev1 = new EonDeviceIdentifier("apa");
		Integer dev2 = new Integer(1234);
		assertFalse(dev1.equals(dev2));
	}
}
