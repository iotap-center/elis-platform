package se.mah.elis.external.water.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import se.mah.elis.data.WaterSample;

public class WaterBeanFactory {

	public static WaterBean create(Map<String, List<WaterSample>> samples, String queryPeriod, String puid) {
		WaterBean bean = new WaterBean();
		
		bean.puid = puid;
		bean.period = queryPeriod;
		bean.devices = createDeviceList(samples);
		bean.summary = summarize(samples);
		
		return bean;
	}

	private static List<WaterDeviceBean> createDeviceList(
			Map<String, List<WaterSample>> samples) {
		List<WaterDeviceBean> devices = new ArrayList<WaterDeviceBean>();
		
		for (String deviceName : samples.keySet()) {
			WaterDeviceBean device = new WaterDeviceBean();
			device.deviceId = deviceName;
			device.data = createDataListFromSamples(samples.get(deviceName));
			devices.add(device);
		}
		
		return devices;
	}

	private static WaterSummaryBean summarize(
			Map<String, List<WaterSample>> samples) {
		WaterSummaryBean summary = new WaterSummaryBean();
		
		float totalVolume = 0.0f;
		
		for (String deviceName : samples.keySet()) {
			for (WaterSample sample : samples.get(deviceName)) {
				totalVolume += sample.getVolume();
			}
		}
		
		summary.totalVolume = totalVolume;
		
		return summary;
	}

	private static List<WaterDataPointBean> createDataListFromSamples(
			List<WaterSample> waterSamples) {
		List<WaterDataPointBean> points = new ArrayList<>();
		for (WaterSample sample : waterSamples) {
			if (sample.getSampleTimestamp() != null) {
				WaterDataPointBean point = new WaterDataPointBean();
				point.timestamp = unixtime(sample.getSampleTimestamp());
				point.humanReadableTimestamp = sample.getSampleTimestamp().toString();
				point.volume = sample.getVolume();
				points.add(point);			
			}
		}
		return points;
	}

	private static String unixtime(DateTime sampleTimestamp) {
		Long unixtime = sampleTimestamp.getMillis();
		return unixtime.toString();
	}
}
