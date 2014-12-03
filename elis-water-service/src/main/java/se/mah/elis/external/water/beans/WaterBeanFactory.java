package se.mah.elis.external.water.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.WaterSample;
import se.mah.elis.external.beans.PeriodicityBean;

/**
 * The WaterBeanFactory creates various water bean objects.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class WaterBeanFactory {

	/**
	 * This constant is used to tell the factory to set the bean's user field.
	 * 
	 * @since 1.0
	 */
	public static final int IS_USER = 1;
	
	/**
	 * This constant is used to tell the factory to set the bean's device
	 * field.
	 * 
	 * @since 1.0
	 */
	public static final int IS_DEVICE = 2;
	
	/**
	 * This constant is used to tell the factory to set the bean's device set
	 * field.
	 * 
	 * @since 1.0
	 */
	public static final int IS_DEVICESET = 3;

	/**
	 * Creates a WaterBean object.
	 * 
	 * @param samples The data samples to embed into the water bean.
	 * @param queryPeriod The periodicity, denoted as a string.
	 * @param uuid The UUID of the sampled object.
	 * @param mode Tells whether the sampled object is a user (by using
	 * 		WaterBeanFactory.IS_USER), a device (by using
	 * 		WaterBeanFactory.IS_DEVICE), or a device set (by using
	 * 		WaterBeanFactory.IS_DEVICESET).
	 * @return A WaterBean object.
	 * @since 1.0
	 */
	public static WaterBean create(Map<String, List<WaterSample>> samples,
			String queryPeriod, UUID uuid, int mode) {
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
	
	/**
	 * Creates a WaterBean object.
	 * 
	 * @param samples The data samples to embed into the water bean.
	 * @param queryPeriod The periodicity, denoted as a string.
	 * @param uuid The UUID of the sampled object.
	 * @param mode Tells whether the sampled object is a user (by using
	 * 		WaterBeanFactory.IS_USER), a device (by using
	 * 		WaterBeanFactory.IS_DEVICE), or a device set (by using
	 * 		WaterBeanFactory.IS_DEVICESET).
	 * @param from When the data sampling started.
	 * @param to When the data sampling ended.
	 * @return A WaterBean object.
	 * @since 1.0
	 */
	public static WaterBean create(Map<String, List<WaterSample>> samples,
			String queryPeriod, UUID uuid, int mode,
			DateTime from, DateTime to) {
		WaterBean bean = create(samples, queryPeriod, uuid, mode);
		
		bean.period.from = from;
		bean.period.to = to;
		
		return bean;
	}
	
	/**
	 * Creates a WaterBean object.
	 * 
	 * @param samples The data samples to embed into the water bean.
	 * @param queryPeriod The periodicity, denoted as a string.
	 * @param uuid The UUID of the sampled object.
	 * @param mode Tells whether the sampled object is a user (by using
	 * 		WaterBeanFactory.IS_USER), a device (by using
	 * 		WaterBeanFactory.IS_DEVICE), or a device set (by using
	 * 		WaterBeanFactory.IS_DEVICESET).
	 * @param from When the data was sampled.
	 * @return A WaterBean object.
	 * @since 1.0
	 */
	public static WaterBean create(Map<String, List<WaterSample>> samples,
			String queryPeriod, UUID uuid, int mode, DateTime when) {
		WaterBean bean = create(samples, queryPeriod, uuid, mode);
		
		bean.period.when = when;
		
		return bean;
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
					bean.timestamp = entry.getValue().get(i).getSampleTimestamp().getMillis();
					points.add(i, bean);
				}
				bean.volume += entry.getValue().get(i).getVolume();
			}
		}
		
		return points;
	}
}
