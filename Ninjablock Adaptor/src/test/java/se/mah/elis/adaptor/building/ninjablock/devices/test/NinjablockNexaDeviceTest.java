package se.mah.elis.adaptor.building.ninjablock.devices.test;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.ninjablock.NinjaBuiltInPowerSwitch;
import se.mah.elis.adaptor.building.ninjablock.NinjablockNexaDevice;

public class NinjablockNexaDeviceTest {

	private NinjablockNexaDevice ninjaDevice;

	@Before
	public void setUp() throws SensorFailedException {
		ninjaDevice = new NinjablockNexaDevice("4412BB000319_0_0_11");
	}
	
	@Test
	public void testTurnOnNexa() {
		ninjaDevice.turnOn("010000000000000000010101");
	}
	
	@Test
	public void testTurnOffNexa() {
		ninjaDevice.turnOff("010000000000000000010100");
	}

}
