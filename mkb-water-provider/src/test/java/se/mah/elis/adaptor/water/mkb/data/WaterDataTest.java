package se.mah.elis.adaptor.water.mkb.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.water.mkb.data.WaterData;
import se.mah.elis.adaptor.water.mkb.data.WaterDataPoint;

public class WaterDataTest {

	
	private static final String METERID = "63408104";
	private WaterData waterData;
	private DateTimeFormatter fmt;
	
	public WaterDataTest() {
		fmt = getFormatter();
	}

	@Before
	public void setup() {
		Map<String, List<WaterDataPoint>> fakeData = createFakeData(); 
		waterData = new WaterData();
		waterData.setWaterData(fakeData);
	}

	private Map<String, List<WaterDataPoint>> createFakeData() {
		Map<String, List<WaterDataPoint>> samples = new HashMap<>();
		String[] testLines = WaterDataLoader.testData().split("\n");
		for (String line : testLines) {
			String[] parts = line.split(";");
			DateTime registered = fmt.parseDateTime(parts[0]);
			String meterId = parts[1];
			float value = Float.parseFloat(parts[3].replace(',', '.'));
			WaterDataPoint sample = new WaterDataPoint(registered, value);
			if (samples.containsKey(meterId))
				samples.get(meterId).add(sample);
			else {
				samples.put(meterId, new ArrayList<WaterDataPoint>());
				samples.get(meterId).add(sample);
			}
		}
		return samples;
	}

	private DateTimeFormatter getFormatter() {
		return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	}
	
	@Test
	public void testGetFullRange() {
		DateTime start = fmt.parseDateTime("2014-02-18 00:00:00");
		DateTime end = fmt.parseDateTime("2014-02-18 07:00:00");
		List<WaterDataPoint> samples = waterData.getRange(METERID, start, end);
		assertEquals(8, samples.size());
	}
	
	@Test
	public void testGetBeforeExisting() {
		DateTime start = fmt.parseDateTime("2012-01-01 00:00:00");
		List<WaterDataPoint> samples = waterData.getRange(METERID, start);
		assertEquals(8, samples.size());
	}
	
	@Test
	public void testGetRangeFrom() {
		DateTime start = fmt.parseDateTime("2014-02-18 01:00:00");
		DateTime end = fmt.parseDateTime("2014-02-18 03:00:00");
		List<WaterDataPoint> samples = waterData.getRange(METERID, start, end);
		assertEquals(3, samples.size());
	}
	
	@Test 
	public void testGetRangeOneHour() {
		DateTime start = fmt.parseDateTime("2014-02-18 00:00:00");
		DateTime end = fmt.parseDateTime("2014-02-18 01:00:00");
		List<WaterDataPoint> samples = waterData.getRange(METERID, start, end);
		assertEquals(1, samples.size());
	}
	
	@Test 
	public void testGetRangeTwoHours() {
		DateTime start = fmt.parseDateTime("2014-02-18 00:00:00");
		DateTime end = fmt.parseDateTime("2014-02-18 02:00:00");
		List<WaterDataPoint> samples = waterData.getRange(METERID, start, end);
		assertEquals(2, samples.size());
	}
	
	@Test 
	public void testGetRangeZeroHours() {
		DateTime start = fmt.parseDateTime("2014-02-18 00:00:00");
		DateTime end = fmt.parseDateTime("2014-02-18 00:00:00");
		List<WaterDataPoint> samples = waterData.getRange(METERID, start, end);
		assertEquals(0, samples.size());
	}
	
	@Test
	public void testGetAll() {
		List<WaterDataPoint> samples = waterData.getAllValues(METERID);
		assertEquals(8, samples.size());
	}
	
	@Test 
	public void testGetRangeInMiddle() {
		DateTime start = fmt.parseDateTime("2014-02-23 00:00:00");
		DateTime end = fmt.parseDateTime("2014-02-24 00:00:00");
		List<WaterDataPoint> samples = waterData.getRange("63408103", start, end);
		assertEquals(1, samples.size());
	}
	
	@Test
	public void testGetEarliestDate() {
		DateTime start = fmt.parseDateTime("2014-02-18 00:00:00");
		assertEquals(start, waterData.getEarliestDate(METERID));
	}
	
	@Test
	public void testGetLastDate() {
		DateTime last = fmt.parseDateTime("2014-02-18 07:00:00");
		assertEquals(last, waterData.getLastDate(METERID));
	}
	
	@Test 
	public void testGetLatestSample() {
		DateTime end = fmt.parseDateTime("2014-02-18 07:00:00");
		WaterDataPoint sample = new WaterDataPoint(end, 18.519f);
		assertTrue(sample.getValue() == waterData.getLatestSample(METERID).getValue());
	}

}
