package se.mah.elis.adaptor.energy.eon.internal;

import java.util.UUID;

import org.json.simple.JSONObject;

import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonDevice;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonDinPowerSwitchMeter;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonMainPowerMeter;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonPowerMeter;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonPowerSwitchMeter;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonThermometer;
import se.mah.elis.adaptor.energy.eon.internal.devices.EonThermostat;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * Factory to create java representations of E.On devices from JSON responses
 * 
 * @author Marcus Ljungblad
 * @author Johan Holmberg
 * @since 1.0
 * @version 1.0.0
 * 
 */
public class EonDeviceFactory {
	
	/**
	 * Method to create internal representations of E.On devices. 
	 * 
	 * Note that the device created is not attached to a particular gateway. Use 
	 * {@link Device#setGateway(se.mah.elis.adaptor.building.api.entities.devices.Gateway)}
	 * to do this. 
	 *  
	 * @param jsonDevice
	 * @return an {@link EonDevice}
	 * @throws MethodNotSupportedException
	 * @throws StaticEntityException
	 * @since 1.0
	 */
	public static Device createFrom(JSONObject jsonDevice)
			throws MethodNotSupportedException, StaticEntityException {
		Device device = null;
		
		int deviceType = getDeviceType(jsonDevice);
		switch (deviceType) {
		case EonDevice.TYPE_POWERSWITCH_METER:
			if (jsonDevice.get("ControllerDeviceId") == null &&
					(Long) jsonDevice.get("UsageAreaId") == 7) {
				// This is a DIN device over at Rönnen
				device = createDinPowerSwitchMeter(jsonDevice);
			} else {
				// This is a power meter of some kind
				device = createPowerSwitchMeter(jsonDevice);
			}
			break;
		case EonDevice.TYPE_POWERMETER:
			if (((Boolean) jsonDevice.get("IsSummaryDevice"))) {
				device = createMainPowerMeter(jsonDevice);
			} else {
				device = createPowerMeter(jsonDevice);
			}
			break;
		case EonDevice.TYPE_THERMOSTAT:
			device = createThermostat(jsonDevice);
			break;
		case EonDevice.TYPE_THERMOMETER:
			device = createThermometer(jsonDevice);
		}
		
		if (device == null) {
			throw new MethodNotSupportedException();
		}

		return device;
	}

	private static Device createThermometer(JSONObject any)
		throws StaticEntityException {
		Device device = new EonThermometer();
		device = setGenericProperties(device, any);
		return device;
	}
	
	private static Device createPowerMeter(JSONObject any)
		throws StaticEntityException{
		Device device = new EonPowerMeter();
		device = setGenericProperties(device, any);
		return device;
	}
	
	private static Device createMainPowerMeter(JSONObject any)
		throws StaticEntityException{
		Device device = new EonMainPowerMeter();
		device = setGenericProperties(device, any);
		return device;
	}
	
	private static Device createDinPowerSwitchMeter(JSONObject any)
		throws StaticEntityException{
		Device device = new EonDinPowerSwitchMeter();
		device = setGenericProperties(device, any);
		return device;
	}
	
	private static Device createThermostat(JSONObject any)
		throws StaticEntityException {
		Device device = new EonThermostat();
		device = setGenericProperties(device, any);
		return device;
	}

	private static int getDeviceType(JSONObject any) {
		Long deviceTypeId = (Long) any.get("DeviceTypeId");
		return deviceTypeId.intValue();
	}

	private static Device createPowerSwitchMeter(JSONObject any)
			throws StaticEntityException {
		Device device = new EonPowerSwitchMeter();
		device = setGenericProperties(device, any);
		return device;
	}

	private static Device setGenericProperties(
			Device device, JSONObject any)
			throws StaticEntityException {
		String deviceId = (String) any.get("Id");
		device.setDataId(UUID.fromString(deviceId));
		// TODO device.setOwnerId(userId);
		device.setName((String) any.get("Name")); 
		device.setDescription((String) any.get("Description"));
		
		return device;
	}
}
