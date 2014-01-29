package se.mah.elis.data;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

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
	 * <p>Returns the data object's identifier. The identifier is set on a
	 * collection basis; that is, the identifier isn't truly unique on its own,
	 * but constitutes a unique identifier in combination with the associated
	 * collection's name.</p>
	 * 
	 * <p>This method was replaced by the getUUID() method in version 1.1.</p>
	 * 
	 * @return The identifier number.
	 * @deprecated As of version 1.1, replaced by {@link #getUUID()}.
	 * @since 1.0
	 */
	long getDataId();
	
	/**
	 * Returns the data object's identifier. The identifier is globally unique,
	 * making the data object traceable.
	 * 
	 * @return The data object's identifier.
	 * @since 1.1
	 */
	UUID getUUID();
	
	/**
	 * Sets the data object's identifier. The identifier is globally unique,
	 * making the data object traceable.
	 * 
	 * @param uuid The object's identifier.
	 * @since 1.1
	 */
	void setUUID(UUID uuid);
	
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

	/**
	 * <p>Get a Properties-based representation of the object. This method is
	 * primarily used by the persistent storage service.</p>
	 * 
	 * <p>Each key-value pair must consist of a String (the key name, which
	 * will be used to identify the value in the storage) and the value itself.
	 * The value can be of any object type, but the type itself must be remain
	 * invariable, as the underlying storage engine might be too inflexible to
	 * store different kinds of values for each field.</p>
	 * 
	 * @return A Properties object with all of the object's properties.
	 * @since 1.1
	 */
	Properties getProperties();
	
	/**
	 * <p>Get a description of the ElisDataObject layout. This method is
	 * primarily used by the persistent storage service to determine the layout
	 * to be used by the underlying storage engine.</p>
	 * 
	 * <p>Each key-value pair must consist of a String (the key name, which
	 * will be used to identify the value in the storage) and the value itself.
	 * The value must be a non-null object, but doesn't need to hold any
	 * particular value (except for Strings, (explained below), as the storage
	 * engine only cares about the object type.</p>
	 * 
	 * <p>Some storage engines, e.g. some SQL dialects, support both variable
	 * and fixed length strings to be stored. By assigning a String an integer
	 * value greater than zero, it is possible to tell the storage how much
	 * space it should reserve for the value. For storage engines that don't
	 * support this functionality, the value is gracefully ignored.</p>
	 * 
	 * @return A Properties object with all of the object's properties.
	 * @since 1.1
	 */
	OrderedProperties getPropertiesTemplate();
	
	/**
	 * Populate the values of an ElisDataObject instance by providing a set of
	 * properties, e.g. from a persistent storage retrieval.
	 * 
	 * @param props The properties to populate the object with.
	 * @since 1.1
	 */
	void populate(Properties props);
}
