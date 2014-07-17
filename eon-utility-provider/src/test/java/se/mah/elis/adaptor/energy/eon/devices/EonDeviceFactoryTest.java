package se.mah.elis.adaptor.energy.eon.devices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.device.api.entities.devices.Thermometer;
import se.mah.elis.adaptor.device.api.entities.devices.Thermostat;
import se.mah.elis.adaptor.device.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.energy.eon.EonParserTest;
import se.mah.elis.adaptor.energy.eon.internal.EonDeviceFactory;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonDinPowerSwitchMeter;
import se.mah.elis.exceptions.StaticEntityException;

public class EonDeviceFactoryTest {

	private static final UUID USER_ID = UUID.fromString("00001111-2222-3333-4444-555566667777");
	private JSONParser parser = new JSONParser();
	private static JSONObject POWERSWITCH_METER;
	private static JSONObject POWERSWITCH_METER_AT_RONNEN;
	private static JSONObject DIN_POWERSWITCH_METER;
	private static JSONObject THERMOMETER;
	private static JSONObject POWERMETER;
	private static JSONObject THERMOSTAT;

	@Before
	public void setUp() throws ParseException {
		createSamplePowerSwitchMeter();
		createSampleDinPowerSwitchMeter();
		createSamplePowerSwitchMeterAtRonnen();
		createSampleThermometer();
		createSamplePowerMeter();
		createSampleThermostat();
	}


	@Test
	public void testCreatePowerSwitchMeter() {
		Device device;
		try {
			device = EonDeviceFactory.createFrom(POWERSWITCH_METER, USER_ID);
			assertTrue(device instanceof PowerSwitch);
			assertNotNull(device.getDataId());
			assertFalse(device.getName().isEmpty());
			assertEquals(USER_ID, device.getOwnerId());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreateThermometer() {
		Device device;
		try {
			device = EonDeviceFactory.createFrom(THERMOMETER, USER_ID);
			assertTrue(device instanceof Thermometer);
			assertNotNull(device.getDataId());
			assertFalse(device.getName().isEmpty());
			assertEquals(USER_ID, device.getOwnerId());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreateThermostat() {
		Device device;
		try {
			device = EonDeviceFactory.createFrom(THERMOSTAT, USER_ID);
			assertTrue(device instanceof Thermostat);
			assertNotNull(device.getDataId());
			assertFalse(device.getName().isEmpty());
			assertEquals(USER_ID, device.getOwnerId());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreatePowerMeter() {
		Device device;
		try {
			device = EonDeviceFactory.createFrom(POWERMETER, USER_ID);
			assertTrue(device instanceof ElectricitySampler);
			assertNotNull(device.getDataId());
			assertFalse(device.getName().isEmpty());
			assertEquals(USER_ID, device.getOwnerId());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreateDinPowerMeter() {
		Device device;
		try {
			device = EonDeviceFactory.createFrom(DIN_POWERSWITCH_METER, USER_ID);
			assertTrue(device instanceof ElectricitySampler);
			assertTrue(device instanceof EonDinPowerSwitchMeter);
			assertNotNull(device.getDataId());
			assertFalse(device.getName().isEmpty());
			assertEquals(USER_ID, device.getOwnerId());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreateRonnenPowerMeter() {
		Device device;
		try {
			device = EonDeviceFactory.createFrom(POWERSWITCH_METER_AT_RONNEN, USER_ID);
			assertTrue(device instanceof ElectricitySampler);
			assertFalse(device instanceof EonDinPowerSwitchMeter);
			assertNotNull(device.getDataId());
			assertFalse(device.getName().isEmpty());
			assertEquals(USER_ID, device.getOwnerId());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreateNotSupportedDeviceThrowsException() {
		long notSupportedType = -1;
		JSONObject dummy = mock(JSONObject.class);
		when(dummy.get(anyString())).thenReturn(notSupportedType);
		Device sample = null;
		try {
			EonDeviceFactory.createFrom(dummy, USER_ID);
		} catch (MethodNotSupportedException mnse) {
			// this should happen
		} catch (StaticEntityException e) {
			fail("Wrong exception thrown");
		} finally {
			assertNull(sample);
		}
	}

	private void createSampleThermometer() throws ParseException {
		THERMOMETER = (JSONObject) parser
				.parse(EonParserTest.SAMPLE_TERMOMETER);
	}

	private void createSampleDinPowerSwitchMeter() throws ParseException {
		DIN_POWERSWITCH_METER = (JSONObject) parser
				.parse(EonParserTest.SAMPLE_DIN_POWERMETER);
	}

	private void createSamplePowerSwitchMeter() throws ParseException {
		POWERSWITCH_METER = (JSONObject) parser
				.parse(EonParserTest.SAMPLE_POWERSWITCH);
	}

	private void createSamplePowerSwitchMeterAtRonnen() throws ParseException {
		POWERSWITCH_METER_AT_RONNEN = (JSONObject) parser
				.parse(EonParserTest.SAMPLE_MAIN_POWERMETER);
	}

	private void createSamplePowerMeter() throws ParseException {
		POWERMETER = (JSONObject) parser.parse(EonParserTest.SAMPLE_POWERMETER);
	}

	private void createSampleThermostat() throws ParseException {
		THERMOSTAT = (JSONObject) parser.parse(EonParserTest.SAMPLE_THERMOSTAT);
	}
}
