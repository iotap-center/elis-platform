package se.mah.elis.adaptor.utilityprovider.eon.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.utilityprovider.eon.EonUtilityProvider;
import se.mah.elis.auxiliaries.data.ElectricitySample;

public class EonUtilityProviderTest {
	
	private final String TEST_USER = "test";
	private final String TEST_DEVICE_SET = "testset";
	
	private EonUtilityProvider provider;
	
	@Before
	public void setUp() {
		provider = new EonUtilityProvider();
	}
	
	@Test
	public void testGetDeviceSet() {
		DeviceSet set = provider.getDeviceSet(TEST_USER, TEST_DEVICE_SET);
		assertTrue(set.size() > 0);
	}
	
	@Test
	public void testGetCurrentKwh() {
		DeviceSet set = provider.getDeviceSet(TEST_USER, TEST_DEVICE_SET);
		Object[] devices = set.toArray();
		ElectricitySampler sampler = (ElectricitySampler) devices[10];
		try {
			ElectricitySample sample = sampler.getSample();
			assertTrue(sample.getCurrentPower() >= 0.0);
		} catch (SensorFailedException e) {
			fail("unexpected exception in default case");
		}
	}
}
