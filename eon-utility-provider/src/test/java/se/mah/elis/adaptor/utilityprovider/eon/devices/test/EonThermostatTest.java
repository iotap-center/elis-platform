package se.mah.elis.adaptor.utilityprovider.eon.devices.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonActionObject;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonThermometer;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonThermostat;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.TemperatureDataImpl;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.auxiliaries.data.TemperatureData;

public class EonThermostatTest {
	
	private static final float DUMMY_TEMPERATURE = 25.0f;
	private	EonThermostat eonThermostat; 
	private EonHttpBridge bridge;
	private EonGateway gateway;

	@Before
	public void setUp() throws ParseException, StaticEntityException {
		// Create a mockup httpbridge
		bridge = mock(EonHttpBridge.class);
		when(bridge.getTemperature(anyString(), anyString(), anyString()))
			.thenReturn(DUMMY_TEMPERATURE);

		GatewayAddress gwaddr = mock(GatewayAddress.class);
		when(gwaddr.toString()).thenReturn("gateway");
		
		DeviceIdentifier psmId = mock(DeviceIdentifier.class);
		when(psmId.toString()).thenReturn("device");
		
		gateway = mock(EonGateway.class);
		when(gateway.getAddress()).thenReturn(gwaddr);
		when(gateway.getAuthenticationToken()).thenReturn("someToken");
		
		eonThermostat = new EonThermostat();
		eonThermostat.setHttpBridge(bridge);
		eonThermostat.setGateway(gateway);
		eonThermostat.setId(psmId);
		
	}
	
	@Test
	public void testSetDesiredTemperature() throws ActuatorFailedException	{

		TemperatureData goal = new TemperatureDataImpl(DUMMY_TEMPERATURE);
		
		try {
			eonThermostat.setDesiredTemperature(goal);
		} catch (ActuatorFailedException e) {
			fail();
		}
	}
	
	@Test
	public void testGetCurrentTemperature() throws SensorFailedException{
		TemperatureData temp = eonThermostat.getCurrentTemperature();
		assertEquals(DUMMY_TEMPERATURE, temp.getCelsius(), 0.001);
	}
	
	
}
