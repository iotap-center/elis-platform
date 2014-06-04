package se.mah.elis.adaptor.energy.eon.devices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.data.GatewayAddress;
import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.energy.eon.internal.EonActionObject;
import se.mah.elis.adaptor.energy.eon.internal.EonActionStatus;
import se.mah.elis.adaptor.energy.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonDinPowerSwitchMeter;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonPowerSwitchMeter;
import se.mah.elis.adaptor.energy.eon.internal.gateway.EonGateway;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.exceptions.StaticEntityException;

public class EonDinPowerSwitchMeterTest {

	private double DUMMY_KWH = 23.0;
	private EonDinPowerSwitchMeter dinPowerSwitchMeter;
	private EonHttpBridge bridge;
	private EonGateway gateway;
	
	@Before
	public void setUp() throws StaticEntityException, ParseException {
		EonActionObject mockActionObject = mock(EonActionObject.class);
		when(mockActionObject.getId()).thenReturn((long) 1111);
		when(mockActionObject.getStatus()).thenReturn(EonActionStatus.ACTION_SUCCESS);
		
		bridge = mock(EonHttpBridge.class);
		when(bridge.getActionObject(anyString(), anyString(), anyInt()))
			.thenReturn(mockActionObject);
		when(bridge.turnOn(anyString(), anyString(), anyString()))
			.thenReturn(mockActionObject);
		when(bridge.turnOff(anyString(), anyString(), anyString()))
			.thenReturn(mockActionObject);
		when(bridge.getPowerMeterKWh(anyString(), anyString(), anyString()))
			.thenReturn(DUMMY_KWH);
		
		DeviceIdentifier psmId = mock(DeviceIdentifier.class);
		when(psmId.toString()).thenReturn("device");
		
		GatewayAddress gwaddr = mock(GatewayAddress.class);
		when(gwaddr.toString()).thenReturn("gateway"); 
		
		gateway = mock(EonGateway.class);
		when(gateway.getAddress()).thenReturn(gwaddr);
		when(gateway.getAuthenticationToken()).thenReturn("someToken");
		
		dinPowerSwitchMeter = new EonDinPowerSwitchMeter();
		dinPowerSwitchMeter.setHttpBridge(bridge);
		dinPowerSwitchMeter.setGateway(gateway);
		dinPowerSwitchMeter.setId(psmId);
	}

	@Test
	public void testTurnOffDevice() {
		// setup
		dinPowerSwitchMeter.setOnline(true);
		
		// test
		assertTrue(dinPowerSwitchMeter.isOnline());
		try {
			dinPowerSwitchMeter.turnOff();
		} catch (ActuatorFailedException e) {
			e.printStackTrace();
			fail();
		}
		assertFalse(dinPowerSwitchMeter.isOnline());
	}

	@Test
	public void testTurnOnDevice() {
		// setup
		dinPowerSwitchMeter.setOnline(false);

		// test
		assertFalse(dinPowerSwitchMeter.isOnline());
		try {
			dinPowerSwitchMeter.turnOn();
		} catch (ActuatorFailedException e) {
			fail();
		}
		assertTrue(dinPowerSwitchMeter.isOnline());
	}
	
	@Test
	public void testGetSample() throws SensorFailedException{
		Object powerSample = dinPowerSwitchMeter.getSample();
		assertTrue(powerSample instanceof ElectricitySample);
		
		ElectricitySample sample = (ElectricitySample) powerSample;
		assertEquals(DUMMY_KWH*1000, sample.getTotalEnergyUsageInWh(), 0.01);
	}
}
