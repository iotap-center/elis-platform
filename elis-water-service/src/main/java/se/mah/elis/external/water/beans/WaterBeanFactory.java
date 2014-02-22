package se.mah.elis.external.water.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import se.mah.elis.data.WaterSample;

public class WaterBeanFactory {

	public static WaterBean create(Map<String, List<WaterSample>> samples, String queryPeriod) {
		WaterBean bean = new WaterBean();
		
		// only one meter id - will cause bug for one user
		for (String meter : samples.keySet()) {
			bean.meterId = meter;
			bean.data = createDataListFromSamples(samples.get(meter));
		}
		
		bean.period = queryPeriod;
		
		return bean;
	}

	private static List<WaterDataPointBean> createDataListFromSamples(
			List<WaterSample> waterSamples) {
		List<WaterDataPointBean> points = new ArrayList<>();
		for (WaterSample sample : waterSamples) {
			WaterDataPointBean point = new WaterDataPointBean();
			point.timestamp = unixtime(sample.getSampleTimestamp());
			point.humanReadableTimestamp = sample.getSampleTimestamp().toString();
			point.volume = sample.getVolume();
			points.add(point);			
		}
		return points;
	}

	private static String unixtime(DateTime sampleTimestamp) {
		Long unixtime = sampleTimestamp.getMillis();
		return unixtime.toString();
	}
}
