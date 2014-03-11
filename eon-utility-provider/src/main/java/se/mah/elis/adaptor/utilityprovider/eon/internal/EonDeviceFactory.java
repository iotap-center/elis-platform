package se.mah.elis.adaptor.utilityprovider.eon.internal;

import org.json.simple.JSONObject;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonDevice;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonDeviceIdentifier;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonPowerMeter;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonPowerSwitchMeter;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonThermometer;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonThermostat;
import se.mah.elis.exceptions.StaticEntityException;

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
		if (deviceType == EonDevice.TYPE_POWERSWITCH_METER)	{
			device = createPowerSwitchMeter(jsonDevice);	
		} else if (deviceType == EonDevice.TYPE_TERMOMETER)	{
			device = createThermometer(jsonDevice);			
		} else if (deviceType == EonDevice.TYPE_POWERMETER)	{
			device = createPowerMeter(jsonDevice);
		} else if (deviceType == EonDevice.TYPE_THERMOSTAT)	{
			device = createThermostat(jsonDevice);
		}

		
		if (device == null)
			throw new MethodNotSupportedException();

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
		device.setId((DeviceIdentifier) new EonDeviceIdentifier(deviceId));
		device.setName((String) any.get("Name")); 
		device.setDescription((String) any.get("Description"));
		
		return device;
	}
}
