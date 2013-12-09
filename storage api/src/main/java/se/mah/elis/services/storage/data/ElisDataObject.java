package se.mah.elis.services.storage.data;

import java.io.Serializable;

/**
 * <p>ElisDataObject is a baseline interface for every saveable data object. By
 * implementing it, the Elis platform is able to store and retrieve the object
 * in question in a uniform way.</p>
 * 
 * <p>User classes should NOT implement this interface.</p>
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface ElisDataObject extends Serializable {
	
	/**
	 * Returns the data object's identifier. The identifier is set on a
	 * collection basis; that is, the identifier isn't truly unique on its own,
	 * but constitutes a unique identifier in combination with the associated
	 * collection's name.
	 * 
	 * @return The identifier number.
	 * @since 1.0
	 */
	long getDataId();
	
	/**
	 * Sets the owner of this data object.
	 * 
	 * @param userId 
	 * @since 1.0
	 */
	void setUniqueUserId(int userId);
	
	/**
	 * Returns the associated unique user id. This id number is associated with
	 * the underlying systems' users, meaning that each piece of data is
	 * associated with the system that provided it.
	 * 
	 * @return the unique user id.
	 * @since 1.0
	 */
	int getUniqueUserId();
}
