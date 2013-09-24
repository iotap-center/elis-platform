package se.mah.elis.adaptor.utilityprovider.eon.devices.test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.ResponseProcessingException;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonPowerMeter;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.auxiliaries.data.ElectricitySample;

public class EonPowerMeterTest {

	private EonPowerMeter eonPowerMeter;
	private EonHttpBridge bridge;
	private EonGateway gateway;
	private double DUMMY_KWH = 23.0;
	
	@Before
	public void setUp() throws ResponseProcessingException, ParseException, StaticEntityException{
		
		bridge = mock(EonHttpBridge.class);
		when(bridge.getPowerMeterKWh(anyString(), anyString(), anyString())).thenReturn(DUMMY_KWH);
		
		GatewayAddress gwaddr = mock(GatewayAddress.class);
		when(gwaddr.toString()).thenReturn("gateway");
		
		DeviceIdentifier psmId = mock(DeviceIdentifier.class);
		when(psmId.toString()).thenReturn("device");
		
		gateway = mock(EonGateway.class);
		when(gateway.getAddress()).thenReturn(gwaddr);
		when(gateway.getAuthenticationToken()).thenReturn("someToken");
		
		eonPowerMeter = new EonPowerMeter();
		eonPowerMeter.setHttpBridge(bridge);
		eonPowerMeter.setGateway(gateway);
		eonPowerMeter.setId(psmId);
	}
	
	@Test
	public void testGetSample() throws SensorFailedException{
		Object powerSample = eonPowerMeter.getSample();
		assertTrue(powerSample instanceof ElectricitySample);
		
		ElectricitySample sample = (ElectricitySample) powerSample;
		assertEquals(DUMMY_KWH*1000, sample.getTotalEnergyUsageInWh(), 0.01);
	}
}
