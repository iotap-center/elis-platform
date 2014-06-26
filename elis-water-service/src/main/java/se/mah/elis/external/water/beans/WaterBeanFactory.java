package se.mah.elis.external.water.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import se.mah.elis.data.WaterSample;
import se.mah.elis.external.beans.PeriodicityBean;

public class WaterBeanFactory {

	public static final int IS_USER = 1;
	public static final int IS_DEVICE = 2;
	public static final int IS_DEVICESET = 3;

	public static WaterBean create(Map<String, List<WaterSample>> samples,
			String queryPeriod, String uuid, int mode) {
		WaterBean bean = new WaterBean();
		
		switch (mode) {
		case IS_USER:
			bean.user = uuid;
			break;
		case IS_DEVICE:
			bean.device = uuid;
			break;
		case IS_DEVICESET:
			bean.deviceset = uuid;
		}
		bean.period = new PeriodicityBean();
		bean.period.periodicity = queryPeriod;
		bean.samples = createDataListFromSamples(samples);
		bean.summary = summarize(samples);
		
		return bean;
	}
	
	public static WaterBean create(Map<String, List<WaterSample>> samples,
			String queryPeriod, String uuid, int mode,
			DateTime from, DateTime to) {
		WaterBean bean = create(samples, queryPeriod, uuid, mode);
		
		bean.period.from = from;
		bean.period.to = to;
		
		return bean;
	}
	
	public static WaterBean create(Map<String, List<WaterSample>> samples,
			String queryPeriod, String uuid, int mode, DateTime when) {
		WaterBean bean = create(samples, queryPeriod, uuid, mode);
		
		bean.period.when = when;
		
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
			Map<String, List<WaterSample>> waterSamples) {
		List<WaterDataPointBean> points = new ArrayList<>();
		WaterDataPointBean bean = null;
		
		for (Entry<String, List<WaterSample>> entry : waterSamples.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				try {
					bean = points.get(i);
				} catch (IndexOutOfBoundsException e) {
					bean = new WaterDataPointBean();
					bean.humanReadableTimestamp = entry.getValue().get(i).getSampleTimestamp().toString();
					bean.timestamp = String.valueOf(entry.getValue().get(i).getSampleTimestamp().getMillis());
					points.add(i, bean);
				}
				bean.volume += entry.getValue().get(i).getVolume();
			}
		}
		
		return points;
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
