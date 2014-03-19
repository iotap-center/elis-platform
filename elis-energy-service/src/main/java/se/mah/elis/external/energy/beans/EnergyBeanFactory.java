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

	/**
	 * Create an Energy Bean response using current energy data
	 * 
	 * @param meters
	 * @param period
	 * @param pu
	 * @return
	 */
	public static EnergyBean create(List<Device> meters, String period, PlatformUser pu) {
		Map<Device, List<ElectricitySample>> samples = collectSamples(meters);
		EnergyBean bean = new EnergyBean();
		bean.puid = getPuid(pu);
		bean.period = period;
		bean.devices = createDeviceList(samples);
		bean.summary = createSummary(samples);
		return bean;
	}
	
	/**
	 * Create an Energy Bean response using historic energy data  
	 * 
	 * @param meters
	 * @param period
	 * @param from
	 * @param to
	 * @param pu
	 * @return
	 */
	public static EnergyBean create(List<Device> meters, String period,
			String from, String to, PlatformUser pu) {
		Map<Device, List<ElectricitySample>> samples = collectHistory(meters, from, to);
		EnergyBean bean = new EnergyBean();
		bean.puid = getPuid(pu);
		bean.period = period;
		bean.devices = createDeviceList(samples);
		return bean;
	}

	private static Map<Device, List<ElectricitySample>> collectHistory(
			List<Device> meters, String from, String to) {
		Map<Device, List<ElectricitySample>> deviceSampleMap = new HashMap<Device, List<ElectricitySample>>();
		
		DateTime fromDate = parseDate(from);
		DateTime toDate = parseToDate(to);
		
		for (Device device : meters) 
			if (device instanceof ElectricitySampler) 
				deviceSampleMap.put(device, collectSampleFor(device, fromDate, toDate));
		
		return deviceSampleMap;
	}

	private static Map<Device, List<ElectricitySample>> collectSamples(
			List<Device> meters) {
		Map<Device, List<ElectricitySample>> deviceSampleMap = new HashMap<Device, List<ElectricitySample>>();
		
		for (Device device : meters)
			if (device instanceof ElectricitySampler)
				deviceSampleMap.put(device, collectSampleFor(device));
		
		return deviceSampleMap;
	}

	private static List<ElectricitySample> collectSampleFor(Device device) {
		List<ElectricitySample> samples = new ArrayList<ElectricitySample>();
		
		try {
			ElectricitySample sample = ((ElectricitySampler) device).getSample();
			samples.add(sample);
		} catch (SensorFailedException e) { }
		
		return samples; 
	}
	
	private static List<ElectricitySample> collectSampleFor(Device device,
			DateTime from, DateTime to) {
		List<ElectricitySample> samples = new ArrayList<ElectricitySample>();
		ElectricitySampler sampler = (ElectricitySampler) device;
		
		try {
			samples.addAll(sampler.getSamples(from, to));
		} catch (SensorFailedException e) { }
		
		return samples;
	}

	private static EnergySummaryBean createSummary(Map<Device, List<ElectricitySample>> meters) {
		EnergySummaryBean summary = new EnergySummaryBean();
		
		summary.deviceId = "aggregate";
		summary.kwh = calculateTotalConsumption(meters);
		
		return summary;
	}

	private static double calculateTotalConsumption(
			Map<Device, List<ElectricitySample>> meters) {
		
		double totalWh = 0.0;
		
		for (List<ElectricitySample> samples : meters.values())
			for (ElectricitySample sample : samples)
				totalWh += sample.getTotalEnergyUsageInWh(); // wont work with historic entries
		
		double totalKwh = totalWh / 1000.0;
		
		return totalKwh;
	}

	private static List<EnergyDeviceBean> createDeviceList(Map<Device, List<ElectricitySample>> meters) {
		List<EnergyDeviceBean> devices = new ArrayList<EnergyDeviceBean>();
		
		for (Device meter : meters.keySet()) 
			devices.add(createEnergyDeviceBean((ElectricitySampler) meter, meters.get(meter)));
			
		return devices;
	}

	private static EnergyDeviceBean createEnergyDeviceBean(ElectricitySampler meter, List<ElectricitySample> samples) {
		EnergyDeviceBean device = new EnergyDeviceBean();
		device.deviceId = meter.getId().toString();
		device.deviceName = encodeString(meter.getName());
		device.data = createSampleData(samples);
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

	private static List<EnergyDataBean> createSampleData(List<ElectricitySample> samples) {
		List<EnergyDataBean> data = new ArrayList<EnergyDataBean>();
		
		for (ElectricitySample sample : samples)
			data.add(createEnergyDataBeanFrom(sample));
		
		return data;
	}

	private static EnergyDataBean createEnergyDataBeanFrom(
			ElectricitySample sample) {
		EnergyDataBean sampleBean = new EnergyDataBean();
		sampleBean.kwh = sample.getTotalEnergyUsageInWh() / 1000.0;
		sampleBean.timestamp = unixtime(sample.getSampleTimestamp());
		return sampleBean;
	}

	private static String getPuid(PlatformUser pu) {
		Integer puid = ((PlatformUserIdentifier) pu.getIdentifier()).getId();
		return puid.toString();
	}
	
	private static String unixtime(DateTime sampleTimestamp) {
		Long unixtime = sampleTimestamp.getMillis();
		return unixtime.toString();
	}
	
	private static DateTime parseDate(String from) {
		return new Instant(Long.parseLong(from)).toDateTime();
	}
	
	private static DateTime parseToDate(String to) {
		DateTime toDt;
		if (!to.isEmpty())
			toDt = new Instant(Long.parseLong(to)).toDateTime();
		else 
			toDt = DateTime.now();
		
		if (toDt.isAfter(DateTime.now()))
			toDt = DateTime.now();	
		return toDt;
	}
}