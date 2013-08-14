/**
 * 
 */
package se.mah.elis.adaptor.building.api.plan;

import java.util.Iterator;

import se.mah.elis.adaptor.building.api.data.PartitionIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;

/**
 * The BuildingPartition interface describes an abstract part of a building. It
 * is the base interface for the Apartment, RoomCollection and Room interfaces.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface BuildingPartition {
	
	/**
	 * This method is used to get the identifier of this partition.
	 * 
	 * @return The identifier of the partition.
	 * @since 1.0
	 */
	PartitionIdentifier getId();
	
	/**
	 * This method is used to set the identifier of a partition.
	 * 
	 * @param id The partition's new identifier.
	 * @throws StaticEntityException if the partition's id is locked.
	 * @since 1.0
	 */
	void setId(PartitionIdentifier id) throws StaticEntityException;
	
	/**
	 * This method is used to get the name of this partition.
	 * 
	 * @return The name of the partition.
	 * @since 1.0
	 */
	String getName();
	
	/**
	 * This method is used to set the name of this partition.
	 * 
	 * @param name The partition's new name.
	 * @throws StaticEntityException if the partition's name is locked.
	 * @since 1.0
	 */
	void setName(String name) throws StaticEntityException;
	
	/**
	 * This method is used to get the description for the partition.
	 * 
	 * @return The description of this partition.
	 * @since 1.0
	 */
	String getDescription();
	
	/**
	 * This method is used to set the description of this partition.
	 * 
	 * @param name The name of the partition.
	 * @throws StaticEntityException if the partition's description is locked.
	 * @since 1.0
	 */
	void getDescription(String name) throws StaticEntityException;
	
	/**
	 * Returns all devices associated with this partition. If the partition has
	 * no devices associated with it, null is returned. All devices in all
	 * associated device sets will be returned.
	 * 
	 * @return An array of all available devices.
	 * @since 1.0
	 */
	Device[] getDevices();
	
	/**
	 * Checks whether a device is a partition or not.
	 * 
	 * @param device The device to be removed to this partition.
	 * @return True if the device is in the partition, otherwise false.
	 * @since 1.0
	 */
	boolean isDeviceInPartition(Device device);
	
	/**
	 * Adds a device set to this apartment.
	 * 
	 * @param set The device set to be added.
	 * @since 1.0
	 */
	void addDeviceSet(DeviceSet set);
	
	/**
	 * Adds a number of device sets to this partition.
	 * 
	 * @param sets The device sets to be added.
	 * @since 1.0
	 */
	void addDeviceSets(DeviceSet[] sets);
	
	/**
	 * Removes a device set from this partition.
	 * 
	 * @param set The device set to be removed.
	 * @since 1.0
	 */
	void removeDeviceSet(DeviceSet set);
	
	/**
	 * Removes all device sets from this partition.
	 * 
	 * @since 1.0
	 */
	void flushDeviceSet();
	
	/**
	 * Checks whether a device set is part of this partition or not.
	 * 
	 * @param set The device set to check for.
	 * @return True if the device set is in this partition, otherwise false.
	 * @since 1.0
	 */
	boolean containsDeviceSet(DeviceSet set);
	
	/**
	 * Counts the number of unique device set in this partition.
	 * 
	 * @return The number of device sets.
	 * @since 1.0
	 */
	int getNumberOfDeviceSets();
	
	/**
	 * Returns an Iterator object for the list of device set.
	 * 
	 * @return The device set iterator.
	 * @since 1.0
	 */
	Iterator<DeviceSet> getDeviceSetIterator();
}
