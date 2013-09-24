package se.mah.elis.adaptor.utilityprovider.eon.devices.test;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonThermometer;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.TemperatureDataImpl;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.auxiliaries.data.TemperatureData;

public class EonThermometerTest {
	
	private static final float DUMMY_TEMPERATURE = 25.0f;
	private	EonThermometer eonThermometer; 
	private EonHttpBridge bridge;
	private EonGateway gateway;
	
	@Before
	public void setUp() throws StaticEntityException, ParseException{
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
		
		eonThermometer = new EonThermometer();
		eonThermometer.setHttpBridge(bridge);
		eonThermometer.setGateway(gateway);
		eonThermometer.setId(psmId);
	}
	
	@Test
	public void testGetCurrentTemperature() throws SensorFailedException{
		TemperatureData temp = eonThermometer.getCurrentTemperature();
		assertEquals(DUMMY_TEMPERATURE, temp.getCelsius(), 0.001);
	}

}
