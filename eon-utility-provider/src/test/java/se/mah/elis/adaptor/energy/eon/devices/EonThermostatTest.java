package se.mah.elis.adaptor.energy.eon.devices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.device.api.data.GatewayAddress;
import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.energy.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonThermostat;
import se.mah.elis.adaptor.energy.eon.internal.devices.TemperatureDataImpl;
import se.mah.elis.adaptor.energy.eon.internal.gateway.EonGateway;
import se.mah.elis.data.TemperatureData;
import se.mah.elis.exceptions.StaticEntityException;

public class EonThermostatTest {
	
	private static final float DUMMY_TEMPERATURE = 25.0f;
	private	EonThermostat eonThermostat; 
	private EonHttpBridge bridge;
	private EonGateway gateway;

	@Before
	public void setUp() throws ParseException, StaticEntityException {
		// Create a mockup httpbridge
		bridge = mock(EonHttpBridge.class);
		when(bridge.getTemperature(anyString(), anyString(), any(UUID.class)))
			.thenReturn(DUMMY_TEMPERATURE);

		GatewayAddress gwaddr = mock(GatewayAddress.class);
		when(gwaddr.toString()).thenReturn("gateway");
		
		
		gateway = mock(EonGateway.class);
		when(gateway.getAddress()).thenReturn(gwaddr);
		when(gateway.getAuthenticationToken()).thenReturn("someToken");
		
		eonThermostat = new EonThermostat();
		eonThermostat.setHttpBridge(bridge);
		eonThermostat.setGateway(gateway);
		eonThermostat.setName("device");
		eonThermostat.setDescription("device");
		
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
