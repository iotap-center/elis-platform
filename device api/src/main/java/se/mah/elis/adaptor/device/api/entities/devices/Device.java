/**
 * 
 */
package se.mah.elis.adaptor.device.api.entities.devices;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.data.ElisDataObject;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * The Device interface is a base interface for all device types, the Gateway
 * interface being excluded.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Device extends ElisDataObject {
	
	/**
	 * This method is used to get the id number of the device.
	 * 
	 * @return The id number of the device.
	 * @since 1.0
	 */
	DeviceIdentifier getId();
	
	/**
	 * This method is used to set the id number of the device.
	 * 
	 * @param id The device's new id number.
	 * @throws StaticEntityException if the device's id is locked.
	 * @since 1.0
	 */
	void setId(DeviceIdentifier id) throws StaticEntityException;
	
	/**
	 * This method is used to get the name of this device.
	 * 
	 * @return The name of the device.
	 * @since 1.0
	 */
	String getName();
	
	/**
	 * This method is used to set the name of this device.
	 * 
	 * @param name The device's new name.
	 * @throws StaticEntityException if the device's name is locked.
	 * @since 1.0
	 */
	void setName(String name) throws StaticEntityException;
	
	/**
	 * This method is used to get the description for the device.
	 * 
	 * @return The description of this device.
	 * @since 1.0
	 */
	String getDescription();
	
	/**
	 * This method is used to set the description of this device.
	 * 
	 * @param description The device's new description.
	 * @throws StaticEntityException if the device's description is locked.
	 * @since 1.0
	 */
	void setDescription(String description) throws StaticEntityException;
	
	/**
	 * This method is used to get a reference to the gateway being tested.
	 * 
	 * @return The gateway that this device belongs to.
	 * @since 1.0
	 */
	Gateway getGateway();
	
	/**
	 * This method is used to set the gateway that the device should belong to.
	 * 
	 * @param gw The device's new gateway.
	 * @throws StaticEntityException if the device's gateway is locked.
	 * @since 1.0
	 */
	void setGateway(Gateway gw) throws StaticEntityException;
	
	/**
	 * This method is used to figure out to which device sets this device
	 * belongs to.
	 * 
	 * @return The DeviceSets that this device belongs to.
	 * @since 1.0
	 */
	DeviceSet[] getDeviceSets();
	
	/**
	 * Checks whether a device is online or not. If it isn't online, then we
	 * shouldn't try to read stuff from it or perform actuating actions on it.
	 * 
	 * @return True if the device is online, otherwise false.
	 * @since 1.0
	 */
	boolean isOnline();
}
