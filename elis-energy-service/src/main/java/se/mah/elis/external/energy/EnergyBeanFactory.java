package se.mah.elis.external.energy;

import java.util.ArrayList;
import java.util.List;

import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.external.energy.beans.EnergyBean;
import se.mah.elis.external.energy.beans.EnergyDataBean;
import se.mah.elis.external.energy.beans.EnergyDeviceBean;
import se.mah.elis.external.energy.beans.EnergySummaryBean;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;

public class EnergyBeanFactory {

	public static EnergyBean create(List<Device> meters, String period, PlatformUser pu) {
		EnergyBean bean = new EnergyBean();
		bean.puid = getPuid(pu);
		bean.period = period;
		bean.devices = createDeviceList(meters);
		bean.summary = createSummary(meters);
		return bean;
	}

	private static EnergySummaryBean createSummary(List<Device> meters) {
		return new EnergySummaryBean();
	}

	private static List<EnergyDeviceBean> createDeviceList(List<Device> meters) {
		List<EnergyDeviceBean> devices = new ArrayList<EnergyDeviceBean>();
		for (Device meter : meters) {
			if (meter instanceof ElectricitySampler) {
				devices.add(createEnergyDeviceBean((ElectricitySampler) meter));
			}
		}
		return devices;
	}

	private static EnergyDeviceBean createEnergyDeviceBean(ElectricitySampler meter) {
		EnergyDeviceBean device = new EnergyDeviceBean();
		device.deviceId = meter.getName();
		device.data = createSampleData(meter);
		return device;
	}

	private static List<EnergyDataBean> createSampleData(ElectricitySampler meter) {
		List<EnergyDataBean> data = new ArrayList<EnergyDataBean>();
		
		try {
			EnergyDataBean sampleBean = createEnergyDataBeanFrom(meter.getSample());
			data.add(sampleBean);
		} catch (SensorFailedException e) {
			// TODO: Log this
			e.printStackTrace();
		}
		
		return data;
	}

	private static EnergyDataBean createEnergyDataBeanFrom(
			ElectricitySample sample) {
		EnergyDataBean sampleBean = new EnergyDataBean();
		sampleBean.kwh = sample.getTotalEnergyUsageInWh();
		sampleBean.timestamp = sample.getSampleTimestamp().toInstant().toString();
		return sampleBean;
	}

	private static String getPuid(PlatformUser pu) {
		Integer puid = ((PlatformUserIdentifier) pu.getIdentifier()).getId();
		return puid.toString();
	}
}