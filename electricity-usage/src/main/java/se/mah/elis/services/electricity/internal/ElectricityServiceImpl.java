package se.mah.elis.services.electricity.internal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.osgi.util.tracker.ServiceTracker;

import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.utilityprovider.api.UtilityProvider;
import se.mah.elis.services.electricity.ElectricityService;

public class ElectricityServiceImpl implements ElectricityService {

	private ServiceTracker providerTracker;

	public ElectricityServiceImpl(ServiceTracker tracker) {
		providerTracker = tracker;
	}

	public Usage getDeviceSetUsage(UsageRequest usageRequest)
			throws InterruptedException {
		Usage usage = new Usage();
		usage.setTimestamp(getCurrentTimestamp());

		// execute request, provide real user for auth and access
		for (DeviceSetRequest set : usageRequest.deviceSets) {
			DeviceSet deviceSet;
			deviceSet = getDeviceSet(set.id);
			DeviceSetResponse deviceResponse = createDeviceSetResponse(deviceSet);
			usage.addDeviceSetResponse(deviceResponse);
		}
		return usage;
	}

	private DeviceSetResponse createDeviceSetResponse(DeviceSet deviceSet) {
		DeviceSetResponse response = new DeviceSetResponse();
		List<DeviceResponse> deviceResponses = createDeviceResponses(deviceSet);
		response.setDeviceSetResponses(deviceResponses);
		response.setTotalUsage(sumForDevices(deviceSet));
		response.setDeviceSetId(String.format("%d", deviceSet.getId()));
		return response;
	}

	private List<DeviceResponse> createDeviceResponses(DeviceSet deviceSet)
			throws NullPointerException {
		List<DeviceResponse> responses = new ArrayList<DeviceResponse>();

		for (Device device : deviceSet) {
			DeviceResponse deviceResponse = new DeviceResponse();
			try {
				ElectricitySampler sampler = (ElectricitySampler) device;
				deviceResponse.setAmount(sampler.getSample().getCurrentPower());
				deviceResponse.setType("electricity");
				deviceResponse.setUnit("kwh");
				deviceResponse.setDeviceId(sampler.getId().toString());
				responses.add(deviceResponse);
			} catch (SensorFailedException sfe) {
				System.err.println("Failed to retreive sensor value");
				continue;
			} catch (ClassCastException cce) {
				System.err
						.println("Could not cast from Device to ElectricitySampler");
				continue;
			}
		}

		return responses;
	}

	private DeviceSet getDeviceSet(String id) throws InterruptedException {
		UtilityProvider bmsService = (UtilityProvider) providerTracker
				.waitForService(5000);
		DeviceSet set = null;
		if (bmsService != null) {
			set = bmsService.getDeviceSet("", id);
		}
		return set;
	}

	private String getCurrentTimestamp() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		return new Timestamp(now.getTime()).toString();
	}

	private double sumForDevices(DeviceSet devices) {
		double total = 0;
		
		for (Device device : devices) {
			try {
				ElectricitySampler samplerDevice = (ElectricitySampler) device;
				total += samplerDevice.getSample().getCurrentPower();
			} catch (SensorFailedException sfe) {
				System.err.println("sfe in sum");
				continue; // FIXME: log error
			} catch (ClassCastException cce) {
				System.err.println("cce in sum");
				continue;
			} catch (NullPointerException npe) {
				System.err.println("npe in sum");
				continue;
			}
		}

		return total;
	}

}
