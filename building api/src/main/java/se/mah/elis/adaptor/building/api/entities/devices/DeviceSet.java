/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import java.util.Collection;

import se.mah.elis.exceptions.StaticEntityException;

/**
 * The DeviceSet interface describes a logical set of devices. It is used to
 * group a number of devices together, e.g. as a group of lamps in different
 * rooms, or a set of appliances in a room.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface DeviceSet extends Collection<Device> {
	
	/**
	 * This method is used to get the id number of the set.
	 * 
	 * @return The id number of the set.
	 * @since 1.0
	 */
	int getId();
	
	/**
	 * This method is used to set the id number of the set.
	 * 
	 * @param id The set's new id number.
	 * @throws StaticEntityException if the set's id is locked.
	 * @since 1.0
	 */
	void setId(int id) throws StaticEntityException;
	
	/**
	 * This method is used to get the name of this set.
	 * 
	 * @return The name of the set.
	 * @since 1.0
	 */
	String getName();
	
	/**
	 * This method is used to set the name of this set.
	 * 
	 * @param name The set's new name.
	 * @throws StaticEntityException if the set's name is locked.
	 * @since 1.0
	 */
	void setName(String name) throws StaticEntityException;
}
