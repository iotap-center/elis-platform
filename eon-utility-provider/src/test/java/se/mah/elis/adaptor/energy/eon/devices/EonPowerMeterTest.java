package se.mah.elis.adaptor.energy.eon.devices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.ResponseProcessingException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.data.GatewayAddress;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.energy.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonPowerMeter;
import se.mah.elis.adaptor.energy.eon.internal.gateway.EonGateway;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.exceptions.StaticEntityException;

public class EonPowerMeterTest {

	private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
	private EonPowerMeter eonPowerMeter;
	private EonHttpBridge bridge;
	private EonGateway gateway;
	private double DUMMY_KWH = 23.0;
	
	@Before
	public void setUp() throws ResponseProcessingException, ParseException, StaticEntityException{
		List<Map<String, Object>> history = createHistoricSamples();
		
		bridge = mock(EonHttpBridge.class);
		when(bridge.getPowerMeterKWh(anyString(), anyString(), anyString())).thenReturn(DUMMY_KWH);
		when(bridge.getStatData(anyString(), anyString(), anyString(), 
				anyString(), anyInt())).thenReturn(history);
		
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
	
	private List<Map<String, Object>> createHistoricSamples() {
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < 24; i++) 
			results.add(createSample(i));
		return results;
	}

	private Map<String, Object> createSample(int i) {
		Map<String, Object> sample = new HashMap<String, Object>();
		sample.put("Key", formatKey(i));
		sample.put("Value", 20.0);
		sample.put("ValueCost", 2.5);
		return sample;
	}

	private String formatKey(int i) {
		if (i < 10)
			return String.format("0001-01-01 0%d:00", i);
		else
			return String.format("0001-01-01 %d:00", i);
	}

	@Test
	public void testGetSample() throws SensorFailedException{
		Object powerSample = eonPowerMeter.getSample();
		assertTrue(powerSample instanceof ElectricitySample);
		
		ElectricitySample sample = (ElectricitySample) powerSample;
		assertEquals(DUMMY_KWH*1000, sample.getTotalEnergyUsageInWh(), 0.01);
	}
	
	@Test
	public void testGetSamples() throws SensorFailedException {
		DateTime from = fmt.parseDateTime("2014-01-01 00:00");
		DateTime to = fmt.parseDateTime("2014-01-02 00:00");
		
		List<ElectricitySample> samples = eonPowerMeter.getSamples(from, to);
		assertEquals(24, samples.size());
		assertEquals(from, samples.get(0).getSampleTimestamp());
		assertEquals(to.minusHours(1), samples.get(samples.size() - 1).getSampleTimestamp());
	}
	
	@Test
	public void testGetSamplesOverMultipleDays() throws ParseException, SensorFailedException {
		DateTime from = fmt.parseDateTime("2014-02-01 00:00");
		DateTime to = fmt.parseDateTime("2014-02-03 00:00");

		// execute
		List<ElectricitySample> samples = eonPowerMeter.getSamples(from, to);
		
		// test
		assertEquals(48, samples.size());
		assertEquals(from, samples.get(0).getSampleTimestamp());
		assertEquals(to.minusHours(1), samples.get(samples.size()-1).getSampleTimestamp());
	}
	
	@Test 
	public void testFormatDate() {
		DateTime date = fmt.parseDateTime("2014-01-01 05:00");
		assertEquals("2014-01-01 05:00", eonPowerMeter.formatDate(date));
	}
	
}
