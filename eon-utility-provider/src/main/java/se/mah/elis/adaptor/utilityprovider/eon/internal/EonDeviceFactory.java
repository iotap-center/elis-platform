package se.mah.elis.adaptor.utilityprovider.eon.internal;

import org.json.simple.JSONObject;

import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonDevice;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonDeviceIdentifier;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonPowerSwitchMeter;

/**
 * Factory to create java representations of E.On devices from JSON responses
 * 
 * @author Marcus Ljungblad
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
		if (deviceType == EonDevice.TYPE_POWERSWITCH_METER)
			device = createPowerSwitchMeter(jsonDevice);

		if (device == null)
			throw new MethodNotSupportedException();

		return device;
	}

	private static int getDeviceType(JSONObject any) {
		Long deviceTypeId = (Long) any.get("DeviceTypeId");
		return deviceTypeId.intValue();
	}

	private static Device createPowerSwitchMeter(JSONObject any)
			throws StaticEntityException {
		EonPowerSwitchMeter device = new EonPowerSwitchMeter();
		device = setGenericProperties(device, any);
		return device;
	}

	private static EonPowerSwitchMeter setGenericProperties(
			EonPowerSwitchMeter device, JSONObject any)
			throws StaticEntityException {
		String deviceId = (String) any.get("Id");
		device.setId(new EonDeviceIdentifier(deviceId));
		device.setName((String) any.get("Name")); 
		device.setDescription((String) any.get("Description"));
		return device;
	}
}
