package se.mah.elis.external.energy.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.services.users.PlatformUser;

public class EnergyBeanFactory {

	/**
	 * Creates an Energy Bean response using current energy data.
	 * 
	 * @param samples A list of electric samples.
	 * @param period Denotes the desired length of samples.
	 * @param pu The user owning the meters.
	 * @return An energy bean to be sent to the user consuming an endpoint.
	 */
	public static EnergyBean create(
			Map<ElectricitySampler, List<ElectricitySample>> samples,
			String period, PlatformUser pu) {
		EnergyBean bean = new EnergyBean();
		bean.user = pu.getUserId().toString();
		bean.samples = createDataListFromSamples(samples);
		bean.summary = createSummary(samples);
		bean.period.periodicity = period;
		bean.period.when = DateTime.now();
				
		return bean;
	}
	
	public static EnergyBean create(
			Map<ElectricitySampler, List<ElectricitySample>> samples,
			String period, ElectricitySampler sampler) {
		EnergyBean bean = new EnergyBean();
		bean.device = sampler.getDataId().toString();
		bean.samples = createDataListFromSamples(samples);
		bean.summary = createSummary(samples);
		bean.period.periodicity = period;
		bean.period.when = DateTime.now();
		
		return bean;
	}
	
	public static EnergyBean create(
			Map<ElectricitySampler, List<ElectricitySample>> samples,
			String period, DeviceSet set) {
		EnergyBean bean = new EnergyBean();
		bean.deviceset = set.getDataId().toString();
		bean.samples = createDataListFromSamples(samples);
		bean.summary = createSummary(samples);
		bean.period.periodicity = period;
		bean.period.when = DateTime.now();
		
		return bean;
	}

	/**
	 * Creates an Energy Bean response using historic energy data.
	 * 
	 * @param samples A list of electric samples.
	 * @param period Denotes the desired length of samples.
	 * @param from An instant in time, from when we want to start reading samples.
	 * @param to  An instant in time, to when we want to read samples.
	 * @param pu The user owning the meters.
	 * @return An energy bean to be sent to the user consuming an endpoint.
	 */
	public static EnergyBean create(
			Map<ElectricitySampler, List<ElectricitySample>> samples,
			String period, DateTime from, DateTime to, PlatformUser pu) {
		EnergyBean bean = new EnergyBean();
		bean.user = pu.getUserId().toString();
		bean.samples = createDataListFromSamples(samples);
		bean.summary = createSummary(samples);
		bean.period.periodicity = period;
		bean.period.from = from;
		bean.period.to = to;
				
		return bean;
	}

	/**
	 * Creates an Energy Bean response using historic energy data.
	 * 
	 * @param samples A list of electric samples.
	 * @param period Denotes the desired length of samples.
	 * @param from An instant in time, from when we want to start reading samples.
	 * @param to  An instant in time, to when we want to read samples.
	 * @param pu The user owning the meters.
	 * @return An energy bean to be sent to the user consuming an endpoint.
	 */
	public static EnergyBean create(
			Map<ElectricitySampler, List<ElectricitySample>> samples,
			String period, DateTime from, DateTime to, ElectricitySampler sampler) {
		EnergyBean bean = new EnergyBean();
		bean.device = sampler.getDataId().toString();
		bean.samples = createDataListFromSamples(samples);
		bean.summary = createSummary(samples);
		bean.period.periodicity = period;
		bean.period.from = from;
		bean.period.to = to;
				
		return bean;
	}

	/**
	 * Creates an Energy Bean response using historic energy data.
	 * 
	 * @param samples A list of electric samples.
	 * @param period Denotes the desired length of samples.
	 * @param from An instant in time, from when we want to start reading samples.
	 * @param to  An instant in time, to when we want to read samples.
	 * @param pu The user owning the meters.
	 * @return An energy bean to be sent to the user consuming an endpoint.
	 */
	public static EnergyBean create(
			Map<ElectricitySampler, List<ElectricitySample>> samples,
			String period, DateTime from, DateTime to, DeviceSet set) {
		EnergyBean bean = new EnergyBean();
		bean.device = set.getDataId().toString();
		bean.samples = createDataListFromSamples(samples);
		bean.summary = createSummary(samples);
		bean.period.periodicity = period;
		bean.period.from = from;
		bean.period.to = to;
				
		return bean;
	}

	private static EnergySummaryBean createSummary(
			Map<ElectricitySampler, List<ElectricitySample>> meters) {
		EnergySummaryBean summary = new EnergySummaryBean();

		summary.kwh = calculateTotalConsumption(meters);

		return summary;
	}

	private static List<EnergyDataBean> createDataListFromSamples(
			Map<ElectricitySampler, List<ElectricitySample>> samples) {
		List<EnergyDataBean> points = new ArrayList<>();
		EnergyDataBean bean = null;
		
		for (Entry<ElectricitySampler, List<ElectricitySample>> entry : samples.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				try {
					bean = points.get(i);
				} catch (IndexOutOfBoundsException e) {
					bean = new EnergyDataBean();
					bean.humanReadableTimestamp = entry.getValue().get(i).getSampleTimestamp().toString();
					bean.timestamp = entry.getValue().get(i).getSampleTimestamp().getMillis();
					points.add(i, bean);
				}
				bean.watts += entry.getValue().get(i).getCurrentVoltage();
				bean.kwh += entry.getValue().get(i).getCurrentPower() / 1000;
			}
		}
		
		return points;
	}

	private static double calculateTotalConsumption(
			Map<ElectricitySampler, List<ElectricitySample>> meters) {

		double totalkWh = 0.0;

		for (List<ElectricitySample> samples : meters.values()) {
			if (samples != null) {
				for (ElectricitySample sample : samples) {
					totalkWh += sample.getTotalEnergyUsageInWh() / 1000.0;
				}
			}
		}

		return totalkWh;
	}
}
