package se.mah.elis.external.energy.beans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Instant;

import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;

public class EnergyBeanFactory {

	private static final boolean HISTORY = true;

	/**
	 * Creates an Energy Bean response using current energy data.
	 * 
	 * @param meters A list of electricity meters, from which we want to read
	 * 		the energy data.
	 * @param period Denotes the desired length of samples.
	 * @param pu The user owning the meters.
	 * @return An energy bean to be sent to the user consuming an endpoint.
	 */
	public static EnergyBean create(List<ElectricitySampler> meters, String period,
			PlatformUser pu) {
		Map<ElectricitySampler, List<ElectricitySample>> samples = collectSamples(meters);
		EnergyBean bean = new EnergyBean();
		bean.puid = pu.getUserId().toString();
		bean.period = period;
		bean.devices = createDeviceList(samples);
		//bean.summary = createSummary(samples);
		return bean;
	}

	/**
	 * Creates an Energy Bean response using historic energy data.
	 * 
	 * @param meters A list of electricity meters, from which we want to read
	 * 		the energy data.
	 * @param period Denotes the desired length of samples.
	 * @param from An instant in time, from when we want to start reading samples.
	 * @param to  An instant in time, to when we want to read samples.
	 * @param pu The user owning the meters.
	 * @return An energy bean to be sent to the user consuming an endpoint.
	 */
	public static EnergyBean create(List<ElectricitySampler> meters, String period,
			DateTime from, DateTime to, PlatformUser pu) {
		Map<ElectricitySampler, List<ElectricitySample>> samples = collectHistory(meters,
				from, to);
		EnergyBean bean = new EnergyBean();
		bean.puid = pu.getUserId().toString();
		bean.period = period;
		bean.devices = createDeviceList(samples, HISTORY);
		bean.summary = createSummary(samples);
		return bean;
	}
	
	public static Map<ElectricitySampler, List<ElectricitySample>>
			pCollectHistory(List<ElectricitySampler> meters, DateTime from, DateTime to) {
		return collectHistory(meters, from, to);
	}

	private static Map<ElectricitySampler, List<ElectricitySample>> collectHistory(
			List<ElectricitySampler> meters, DateTime from, DateTime to) {
		Map<ElectricitySampler, List<ElectricitySample>> deviceSampleMap = new HashMap<ElectricitySampler, List<ElectricitySample>>();

		for (ElectricitySampler meter : meters) {
			deviceSampleMap.put(meter,
					collectSamplesFor((ElectricitySampler) meter, from, to));
		}

		return deviceSampleMap;
	}
	
	public static Map<ElectricitySampler, List<ElectricitySample>>
			pCollectSamples(List<ElectricitySampler> meters) {
		return collectSamples(meters);
	}

	private static Map<ElectricitySampler, List<ElectricitySample>>
			collectSamples(List<ElectricitySampler> meters) {
		Map<ElectricitySampler, List<ElectricitySample>> deviceSampleMap =
				new HashMap<ElectricitySampler, List<ElectricitySample>>();

		for (ElectricitySampler meter : meters)
			deviceSampleMap.put(meter, collectSampleFor(meter));

		return deviceSampleMap;
	}

	public static List<ElectricitySample> pCollectSampleFor(ElectricitySampler meter) {
		return collectSampleFor(meter);
	}
	
	private static List<ElectricitySample> collectSampleFor(ElectricitySampler meter) {
		List<ElectricitySample> samples = new ArrayList<ElectricitySample>();

		try {
			ElectricitySample sample = meter.getSample();
			samples.add(sample);
		} catch (SensorFailedException e) {
		}

		return samples;
	}

	public static List<ElectricitySample> pCollectSamplesFor(ElectricitySampler meter, DateTime from, DateTime to) {
		return collectSamplesFor(meter, from, to);
	}

	private static List<ElectricitySample> collectSamplesFor(ElectricitySampler sampler,
			DateTime from, DateTime to) {
		List<ElectricitySample> samples = new ArrayList<ElectricitySample>();

		try {
			samples.addAll(sampler.getSamples(from, to));
		} catch (SensorFailedException e) {
		}

		return samples;
	}

	private static EnergySummaryBean createSummary(
			Map<ElectricitySampler, List<ElectricitySample>> meters) {
		EnergySummaryBean summary = new EnergySummaryBean();

		summary.kwh = calculateTotalConsumption(meters);

		return summary;
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

	private static List<EnergyDeviceBean> createDeviceList(
			Map<ElectricitySampler, List<ElectricitySample>> meters) {
		boolean no_history = false;
		return createDeviceList(meters, no_history);
	}

	private static List<EnergyDeviceBean> createDeviceList(
			Map<ElectricitySampler, List<ElectricitySample>> meters, boolean isHistory) {
		List<EnergyDeviceBean> devices = new ArrayList<EnergyDeviceBean>();

		for (ElectricitySampler meter : meters.keySet()) {
			devices.add(createEnergyDeviceBean((ElectricitySampler) meter,
					meters.get(meter), isHistory));
		}

		return devices;
	}

	private static EnergyDeviceBean createEnergyDeviceBean(
			ElectricitySampler meter, List<ElectricitySample> samples, boolean isHistory) {
		EnergyDeviceBean device = new EnergyDeviceBean();
		device.deviceId = meter.getId().toString();
		device.deviceName = encodeString(meter.getName());
		device.data = createSampleData(samples, isHistory);
		return device;
	}

	private static String encodeString(String stringToEncode) {
		String name = "";
		try {
			name = URLEncoder.encode(stringToEncode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			name = stringToEncode;
		}
		return name;
	}

	private static List<EnergyDataBean> createSampleData(
			List<ElectricitySample> samples, boolean isHistory) {
		List<EnergyDataBean> data = new ArrayList<EnergyDataBean>();
		
		for (ElectricitySample sample : samples) {
			if (sample != null) {
				data.add(createEnergyDataBeanFrom(sample, isHistory));
			}
		}
		
		if (data.size() == 0) {
			EnergyDataBean bean = new EnergyDataBean();
			bean.kwh = -1;
			bean.watts = -1;
			bean.timestamp = DateTime.now().getMillis();
			bean.humanReadableTimestamp = DateTime.now().toString();
			data.add(bean);
		}

		return data;
	}

	private static EnergyDataBean createEnergyDataBeanFrom(
			ElectricitySample sample, boolean isHistory) {
		EnergyDataBean sampleBean = new EnergyDataBean();
		
		if (isHistory) {
			sampleBean.kwh = sample.getTotalEnergyUsageInWh() / 1000.0;
		} else { 
			sampleBean.watts = sample.getCurrentPower() / 1000.0;
		}
		
		sampleBean.timestamp = sample.getSampleTimestamp().getMillis();
		sampleBean.humanReadableTimestamp = sample.getSampleTimestamp().toString();
		
		return sampleBean;
	}
}
