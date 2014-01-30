/**
 * 
 */
package se.mah.elis.adaptor.building.api.plan;

import java.util.Collection;

import se.mah.elis.exceptions.StaticEntityException;

/**
 * The Building interface describes a logical building, which consists of
 * either apartments or rooms.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Building extends Collection<BuildingPartition> {
	
	/**
	 * This method is used to get the id number of this building.
	 * 
	 * @return The id number of the building.
	 * @since 1.0
	 */
	int getId();
	
	/**
	 * This method is used to set the id number of a building.
	 * 
	 * @param id The device's new id number.
	 * @throws StaticEntityException if the building's id is locked.
	 * @since 1.0
	 */
	void setId(int id) throws StaticEntityException;
	
	/**
	 * This method is used to get the name of this building.
	 * 
	 * @return The name of the building collection.
	 * @since 1.0
	 */
	String getName();
	
	/**
	 * This method is used to set the name of a building.
	 * 
	 * @param name The device's new name.
	 * @throws StaticEntityException if the building's name is locked.
	 * @since 1.0
	 */
	void setName(String name) throws StaticEntityException;
}
