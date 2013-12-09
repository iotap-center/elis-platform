/**
 * 
 */
package se.mah.elis.adaptor.building.api.plan;

import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;

/**
 * The Room interface describes a logical room in a house. A room can contain a number of devices.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface Room extends BuildingPartition {
	/**
	 * Returns all devices associated with this room. If the room has no
	 * devices associated with it, null is returned.
	 * 
	 * @return An array of all available devices.
	 */
	Device[] getDevicesInRoom();
	
	/**
	 * Adds a device to this room.
	 * 
	 * @param device The device to be added to this room.
	 * @throws StaticEntityException if the device list is locked.
	 */
	void addDeviceToRoom(Device device) throws StaticEntityException;
	
	/**
	 * Removes a device from this room.
	 * 
	 * @param device The device to be removed to this room.
	 * @throws StaticEntityException if the device list is locked.
	 */
	void removeDeviceFromRoom(Device device) throws StaticEntityException;
	
	/**
	 * Checks whether a device belongs to a room or not.
	 * 
	 * @param device The device to be removed to this room.
	 * @return True if the device is in the room, otherwise false.
	 */
	boolean isDeviceInRoom(Device device);
}
