package se.mah.elis.adaptor.utilityprovider.eon.devices.test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.building.api.entities.devices.Thermometer;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonDeviceFactory;
import se.mah.elis.adaptor.utilityprovider.eon.test.EonParserTest;

public class EonDeviceFactoryTest {

	private JSONParser parser = new JSONParser();
	private static JSONObject POWERSWITCH_METER;
	private static JSONObject THERMOMETER;
	
	@Before
	public void setUp() throws ParseException {
		createSamplePowerSwitchMeter();
		createSampleThermometer();
	}
	
	@Test
	public void testCreatePowerSwitchMeter() {
		Device sample = EonDeviceFactory.createFrom(POWERSWITCH_METER);
		assertTrue(sample instanceof PowerSwitch);
		assertFalse(sample.getId().toString().isEmpty());
		assertFalse(sample.getName().isEmpty());
	}
	
	@Test
	public void testCreateThermometer() {
		Device sample = EonDeviceFactory.createFrom(THERMOMETER);
		assertTrue(sample instanceof Thermometer);
		assertFalse(sample.getId().toString().isEmpty());
		assertFalse(sample.getName().isEmpty());
	}

	private void createSampleThermometer() throws ParseException {
		THERMOMETER = (JSONObject) parser.parse(EonParserTest.SAMPLE_TERMOMETER);
	}

	private void createSamplePowerSwitchMeter() throws ParseException {
		POWERSWITCH_METER = (JSONObject) parser.parse(EonParserTest.SAMPLE_POWERSWITCH);
	}
}
