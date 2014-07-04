package se.mah.elis.adaptor.water.mkb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.water.mkb.data.WaterData;
import se.mah.elis.adaptor.water.mkb.data.WaterDataLoader;
import se.mah.elis.adaptor.water.mkb.data.WaterDataService;
import se.mah.elis.data.WaterSample;
import se.mah.elis.exceptions.StaticEntityException;

public class MkbWaterMeterTest {

	private static final String meterId = "63408104";
	private static final float epsilon = 0.0001f;
	private MkbWaterMeter meter;

	@Before
	public void setup() {
		WaterData data = WaterDataLoader.loadFromCode();
		WaterDataService service = new WaterDataService(data);
		meter = new MkbWaterMeter(service);
		changeMeter(meterId);
	}

	
	@Test
	public void testLatestSample() {
		try {
			WaterSample sample = meter.getSample();
			assertEquals(18.519f, sample.getVolume(), epsilon);
		} catch (SensorFailedException e) {
			fail("sensor failed");
		}
	}
	
	@Test
	public void testGetLatestSampleWhenNoDataSourceIsInstalled() {
		meter = new MkbWaterMeter(null);
		changeMeter(meterId);
		try {
			meter.getSample();
			fail("meter should have thrown SensorFailedException");
		} catch (SensorFailedException sfe) {}
	}
	
	@Test
	public void testRangeSampleTwoHours() {
		DateTime from = stringToDateTime("2014-02-18 00:00:00");
		DateTime to = stringToDateTime("2014-02-18 02:00:00");
		try {
			WaterSample sample = meter.getSample(from, to);
			assertEquals(1000*60*60, sample.getSampleLength());
			assertEquals(0.001f, sample.getVolume(), epsilon);
		} catch (SensorFailedException e) {
			fail("sensor failed");
		}
	}
	
	@Test
	public void testRangeSampleTwoDays() {
		changeMeter("63408103");
		DateTime from = stringToDateTime("2014-02-18 00:00:00");
		DateTime to = stringToDateTime("2014-02-22 00:00:00");
		long length = to.getMillis() - from.getMillis() - 86400000; // correct for day
		try {
			WaterSample sample = meter.getSample(from, to);
			assertEquals(length, sample.getSampleLength());
			assertEquals(0.001f, sample.getVolume(), epsilon);
		} catch (SensorFailedException e) {
			fail("sensor failed");
		}
	}
	
	@Test
	public void testRangeSampleWeekly() {
		WaterData source = new WaterData();
		String[] data = new String[2]; 
		data[0] = "2013-12-10 00:00:00;63408097;735999114002147734;46,512";
		data[1] = "2013-12-17 00:00:00;63408097;735999114002147734;48,791";
		source.setWaterData(WaterDataLoader.parseLines(data));
		meter = new MkbWaterMeter(new WaterDataService(source));
		changeMeter("63408097");
		
		DateTime from = stringToDateTime("2013-12-10 00:00:00");
		DateTime to = stringToDateTime("2013-12-17 00:00:00");
		
		float expectedVolume = 48.791f - 46.512f;
		
		try {
			WaterSample sample = meter.getSample(from, to);
			assertEquals(expectedVolume, sample.getVolume(), epsilon);
		} catch (SensorFailedException sfe) {
			fail("sensor failed");
		}
		
	}
	
	private void changeMeter(String mid) {
		try {
			meter.setName(mid);
			meter.setDescription(mid);
		} catch (StaticEntityException e) {
			fail("couldnt set meter id");
		}
	}


	private DateTime stringToDateTime(String datetime) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		return fmt.parseDateTime(datetime);
	}
}
