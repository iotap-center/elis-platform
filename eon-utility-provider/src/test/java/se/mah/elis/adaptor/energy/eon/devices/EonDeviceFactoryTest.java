package se.mah.elis.adaptor.energy.eon.devices;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
		Device sample;
		try {
			sample = EonDeviceFactory.createFrom(POWERSWITCH_METER);
			assertTrue(sample instanceof PowerSwitch);
			assertNotNull(sample.getDataId());
			assertFalse(sample.getName().isEmpty());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreateThermometer() {
		Device sample;
		try {
			sample = EonDeviceFactory.createFrom(THERMOMETER);
			assertTrue(sample instanceof Thermometer);
			assertNotNull(sample.getDataId());
			assertFalse(sample.getName().isEmpty());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreateThermostat() {
		Device sample;
		try {
			sample = EonDeviceFactory.createFrom(THERMOSTAT);
			assertTrue(sample instanceof Thermostat);
			assertNotNull(sample.getDataId());
			assertFalse(sample.getName().isEmpty());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreatePowerMeter() {
		Device sample;
		try {
			sample = EonDeviceFactory.createFrom(POWERMETER);
			assertTrue(sample instanceof ElectricitySampler);
			assertNotNull(sample.getDataId());
			assertFalse(sample.getName().isEmpty());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreateDinPowerMeter() {
		Device sample;
		try {
			sample = EonDeviceFactory.createFrom(DIN_POWERSWITCH_METER);
			assertTrue(sample instanceof ElectricitySampler);
			assertTrue(sample instanceof EonDinPowerSwitchMeter);
			assertNotNull(sample.getDataId());
			assertFalse(sample.getName().isEmpty());
		} catch (MethodNotSupportedException | StaticEntityException e) {
			fail();
		}
	}

	@Test
	public void testCreateRonnenPowerMeter() {
		Device sample;
		try {
			sample = EonDeviceFactory.createFrom(POWERSWITCH_METER_AT_RONNEN);
			assertTrue(sample instanceof ElectricitySampler);
			assertFalse(sample instanceof EonDinPowerSwitchMeter);
			assertNotNull(sample.getDataId());
			assertFalse(sample.getName().isEmpty());
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
			EonDeviceFactory.createFrom(dummy);
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
