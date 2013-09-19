package se.mah.elis.adaptor.utilityprovider.eon.devices.test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.building.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.building.api.entities.devices.Thermometer;
import se.mah.elis.adaptor.building.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonDeviceFactory;
import se.mah.elis.adaptor.utilityprovider.eon.test.EonParserTest;

public class EonDeviceFactoryTest {

	private JSONParser parser = new JSONParser();
	private static JSONObject POWERSWITCH_METER;
	private static JSONObject THERMOMETER;
	private static JSONObject POWERMETER;
	
	@Before
	public void setUp() throws ParseException {
		createSamplePowerSwitchMeter();
		createSampleThermometer();
		createSamplePowerMeter();
	}
	
	@Test
	public void testCreatePowerSwitchMeter() {
		Device sample;
		try {
			sample = EonDeviceFactory.createFrom(POWERSWITCH_METER);
			assertTrue(sample instanceof PowerSwitch);
			assertFalse(sample.getId().toString().isEmpty());
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
			assertFalse(sample.getId().toString().isEmpty());
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
			assertFalse(sample.getId().toString().isEmpty());
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
		THERMOMETER = (JSONObject) parser.parse(EonParserTest.SAMPLE_TERMOMETER);
	}

	private void createSamplePowerSwitchMeter() throws ParseException {
		POWERSWITCH_METER = (JSONObject) parser.parse(EonParserTest.SAMPLE_POWERSWITCH);
	}
	
	private void createSamplePowerMeter() throws ParseException {
		POWERMETER = (JSONObject) parser.parse(EonParserTest.SAMPLE_POWERMETER);
	}
}
