/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;

/**
 * The Device interface is a base interface for all device types, the Gateway
 * interface being excluded.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface Device {
	
	/**
	 * This method is used to get the id number of the device.
	 * 
	 * @return The id number of the device.
	 */
	int getId();
	
	/**
	 * This method is used to set the id number of the device.
	 * 
	 * @param id The device's new id number.
	 * @throws StaticEntityException if the device's id is locked.
	 */
	void setId(int id) throws StaticEntityException;
	
	/**
	 * This method is used to get the name of this device.
	 * 
	 * @return The name of the device.
	 */
	String getName();
	
	/**
	 * This method is used to set the name of this device.
	 * 
	 * @param name The device's new name.
	 * @throws StaticEntityException if the device's name is locked.
	 */
	void setName(String name) throws StaticEntityException;
	
	/**
	 * This method is used to get the description for the device.
	 * 
	 * @return The description of this device.
	 */
	String getDescription();
	
	/**
	 * This method is used to set the description of this device.
	 * 
	 * @param description The device's new description.
	 * @throws StaticEntityException if the device's description is locked.
	 */
	void setDescription(String description) throws StaticEntityException;
	
	/**
	 * Checks whether a device is online or not. If it isn't online, then we
	 * shouldn't try to read stuff from it or perform actuating actions on it.
	 * 
	 * @return True if the device is online, otherwise false.
	 */
	boolean isOnline();
}
