package se.mah.elis.adaptor.utilityprovider.eon.devices.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonPowerSwitchMeter;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;

public class EonPowerSwitchMeterTest {

	private EonPowerSwitchMeter psm;
	private EonHttpBridge bridge;
	private EonGateway gateway;
	
	@Before
	public void setUp() throws StaticEntityException {
		bridge = mock(EonHttpBridge.class);
		
		DeviceIdentifier psmId = mock(DeviceIdentifier.class);
		when(psmId.toString()).thenReturn("device");
		
		GatewayAddress gwaddr = mock(GatewayAddress.class);
		when(gwaddr.toString()).thenReturn("gateway"); 
		
		gateway = mock(EonGateway.class);
		when(gateway.getAddress()).thenReturn(gwaddr);
		when(gateway.getAuthenticationToken()).thenReturn("someToken");
		
		psm = new EonPowerSwitchMeter();
		psm.setHttpBridge(bridge);
		psm.setGateway(gateway);
		psm.setId(psmId);
	}

	@Test
	public void testTurnOffDevice() {
		// setup
		psm.setOnline(true);
		
		// test
		assertTrue(psm.isOnline());
		try {
			psm.turnOff();
		} catch (ActuatorFailedException e) {
			fail();
		}
		assertFalse(psm.isOnline());
	}

	@Test
	public void testTurnOnDevice() {
		// setup
		psm.setOnline(false);

		// test
		assertFalse(psm.isOnline());
		try {
			psm.turnOn();
		} catch (ActuatorFailedException e) {
			fail();
		}
		assertTrue(psm.isOnline());
	}
}