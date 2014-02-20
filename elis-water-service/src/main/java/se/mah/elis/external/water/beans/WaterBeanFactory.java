package se.mah.elis.external.water.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import se.mah.elis.data.WaterSample;

public class WaterBeanFactory {

	public static WaterBean create(Map<String, WaterSample> samples, String queryPeriod) {
		WaterBean bean = new WaterBean();
		
		System.out.println("creating bean");
		
		// only one meter id
		for (String meter : samples.keySet()) {
			bean.meterId = meter;
			bean.data = createDataListFromSamples(samples.get(meter));
		}
		
		bean.period = queryPeriod;
		
		System.out.println("bean created");
		
		return bean;
	}

	private static List<WaterDataPointBean> createDataListFromSamples(
			WaterSample waterSamples) {
		List<WaterDataPointBean> points = new ArrayList<>();
		WaterDataPointBean point = new WaterDataPointBean();
		point.timestamp = unixtime(waterSamples.getSampleTimestamp());
		point.volume = waterSamples.getVolume();
		points.add(point);
		return points;
	}

	private static String unixtime(DateTime sampleTimestamp) {
		Long unixtime = sampleTimestamp.getMillis();
		return unixtime.toString();
	}
}
