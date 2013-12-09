package se.mah.elis.adaptor.building.ninjablock.devices.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.building.ninjablock.NinjaBuiltInHumidity;
import se.mah.elis.adaptor.building.ninjablock.NinjaBuiltInThermometer;
import se.mah.elis.adaptor.building.ninjablock.beans.DataBean;
import se.mah.elis.adaptor.building.ninjablock.internal.NinjaDeviceIdentifier;

	public class NinjaBuiltInHumidityTest {

		private static final Float DUMMY_HUMIDITY = 48f; // Change value to match the "real" value from Ninjablock Dashboard.

		private NinjaBuiltInHumidity ninjaDevice;
		
		@Before
		public void setUp() {
			ninjaDevice = new NinjaBuiltInHumidity();
		}

		@Test
		public void testGetCurrentHumidity() {
			try {
				assertEquals(DUMMY_HUMIDITY, ninjaDevice.getCurrentHumidity(), 0.001);
			} catch (SensorFailedException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void testCurrentHumidityIsFloat() {
			
			try {
				assertThat(ninjaDevice.getCurrentHumidity(),instanceOf(float.class));
			} catch (SensorFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

