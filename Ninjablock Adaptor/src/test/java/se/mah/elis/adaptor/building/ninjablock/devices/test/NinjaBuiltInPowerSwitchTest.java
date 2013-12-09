package se.mah.elis.adaptor.building.ninjablock.devices.test;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.ninjablock.NinjaBuiltInPowerSwitch;

public class NinjaBuiltInPowerSwitchTest {
	
	private NinjaBuiltInPowerSwitch ninjaDevice;

	@Before
	public void setUp() throws SensorFailedException {
		ninjaDevice = new NinjaBuiltInPowerSwitch();
	}
	
	@Test
	public void testTurnOn() throws ActuatorFailedException {
		ninjaDevice.turnOn();
	}
	
	@Test
	public void testTurnOff() throws ActuatorFailedException {
		ninjaDevice.turnOff();
	}
	
	@Test
	public void testToggle() throws ActuatorFailedException {
		ninjaDevice.toggle();
	}
}
