package se.mah.elis.adaptor.building.ninjablock.devices.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.building.ninjablock.NinjaBuiltInThermometer;
import se.mah.elis.adaptor.building.ninjablock.beans.DataBean;
import se.mah.elis.adaptor.building.ninjablock.internal.NinjaDeviceIdentifier;

public class NinjaBuiltInThermometerTest {

	private static final Float DUMMY_TEMPERATURE = 22f; // Change value to match the "real" value from Ninjablock Dashboard.

	private NinjaBuiltInThermometer ninjaDevice;
	private NinjaDeviceIdentifier id;
	
	@Before
	public void setUp() {
		
		ninjaDevice = new NinjaBuiltInThermometer();
		
		DataBean bean = new DataBean();
		
		id = new NinjaDeviceIdentifier(bean);
	}
	
	@Test
	public void testGetId() {
		assertThat(id, instanceOf(DeviceIdentifier.class));
    }
	
	@Test
	public void testSetIdToNull() {
		DeviceIdentifier deviceIdentifier = null;
		try {
			ninjaDevice.setId(deviceIdentifier);
			fail("Exception should've been thrown");
		} catch (IllegalArgumentException e) {
			
		} catch (StaticEntityException e){
			
		}
	}
	
	@Test
	public void testSetNameToNull() {
		try {
			ninjaDevice.setName(null);
			fail("Exception should've been thrown");
		} catch (IllegalArgumentException e) {
			
		} catch (StaticEntityException e){
			
		}
	}
	
	@Test
	public void testGetCurrentTemperatureInCelsius() {
		//TODO: Make a better test that mocks the value from Ninjablock and compare it to dummy_temp.
		try {
			assertEquals(DUMMY_TEMPERATURE, ninjaDevice.getCurrentTemperature().getCelsius(), 0.001);
		} catch (SensorFailedException e) {
			e.printStackTrace();
		}
	}
}
