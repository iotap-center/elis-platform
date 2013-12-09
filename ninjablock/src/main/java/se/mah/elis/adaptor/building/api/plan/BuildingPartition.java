/**
 * 
 */
package se.mah.elis.adaptor.building.api.plan;

import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;

/**
 * The BuildingPartition interface describes an abstract part of a building. It
 * is the base interface for the Apartment and Room interfaces.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface BuildingPartition {
	
	/**
	 * This method is used to get the id number of this partition.
	 * 
	 * @return The id number of the partition.
	 */
	int getId();
	
	/**
	 * This method is used to set the id number of a partition.
	 * 
	 * @param id The partition's new id number.
	 * @throws StaticEntityException if the partition's id is locked.
	 */
	void setId(int id) throws StaticEntityException;
	
	/**
	 * This method is used to get the name of this partition.
	 * 
	 * @return The name of the partition.
	 */
	String getName();
	
	/**
	 * This method is used to set the name of this partition.
	 * 
	 * @param name The partition's new name.
	 * @throws StaticEntityException if the partition's name is locked.
	 */
	void setName(String name) throws StaticEntityException;
	
	/**
	 * This method is used to get the description for the partition.
	 * 
	 * @return The description of this partition.
	 */
	String getDescription();
	
	/**
	 * This method is used to set the description of this partition.
	 * 
	 * @param name The name of the partition.
	 * @throws StaticEntityException if the partition's description is locked.
	 */
	void getDescription(String name) throws StaticEntityException;
}
