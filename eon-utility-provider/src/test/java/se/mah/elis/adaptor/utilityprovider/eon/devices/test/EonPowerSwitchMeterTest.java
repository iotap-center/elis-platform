package se.mah.elis.adaptor.utilityprovider.eon.devices.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonActionObject;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonActionStatus;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonPowerSwitchMeter;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;

public class EonPowerSwitchMeterTest {

	private EonPowerSwitchMeter powerSwitchMeter;
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
		
		DeviceIdentifier psmId = mock(DeviceIdentifier.class);
		when(psmId.toString()).thenReturn("device");
		
		GatewayAddress gwaddr = mock(GatewayAddress.class);
		when(gwaddr.toString()).thenReturn("gateway"); 
		
		gateway = mock(EonGateway.class);
		when(gateway.getAddress()).thenReturn(gwaddr);
		when(gateway.getAuthenticationToken()).thenReturn("someToken");
		
		powerSwitchMeter = new EonPowerSwitchMeter();
		powerSwitchMeter.setHttpBridge(bridge);
		powerSwitchMeter.setGateway(gateway);
		powerSwitchMeter.setId(psmId);
	}

	@Test
	public void testTurnOffDevice() {
		// setup
		powerSwitchMeter.setOnline(true);
		
		// test
		assertTrue(powerSwitchMeter.isOnline());
		try {
			powerSwitchMeter.turnOff();
		} catch (ActuatorFailedException e) {
			e.printStackTrace();
			fail();
		}
		assertFalse(powerSwitchMeter.isOnline());
	}

	@Test
	public void testTurnOnDevice() {
		// setup
		powerSwitchMeter.setOnline(false);

		// test
		assertFalse(powerSwitchMeter.isOnline());
		try {
			powerSwitchMeter.turnOn();
		} catch (ActuatorFailedException e) {
			fail();
		}
		assertTrue(powerSwitchMeter.isOnline());
	}
}
