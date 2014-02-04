package se.mah.elis.data;

import java.util.Properties;

/**
 * The Identifier interface describes unique entities in the Elis platform. It
 * is mainly associated with user objects and entities that need to be
 * addressable outside the platform itself.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.1
 */
public interface Identifier {
	/**
	 * Get a Properties-based representation of the object. This method is
	 * primarily used by the persistent storage service.
	 * 
	 * @return A Properties object with all of the object's properties.
	 * @since 1.1
	 */
	Properties getProperties();

	/**
	 * <p>Get a description of the Identifier object. This method is primarily
	 * used by the persistent storage service to determine the layout
	 * to be used by the underlying storage engine.</p>
	 * 
	 * <p>Each key-value pair must consist of a String (the key name, which
	 * will be used to identify the value in the storage) and the value itself.
	 * The value must be a non-null object, but doesn't need to hold any
	 * particular value (except for Strings, (explained below), as the storage
	 * engine only cares about the object type.</p>
	 * 
	 * <p>Some storage engine, e.g. some SQL dialects, support both variable
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
	 * Populate the values of an Identifier instance by providing a set of
	 * properties, e.g. from a persistent storage retrieval.
	 * 
	 * @param props The properties to populate the object with.
	 * @since 1.1
	 */
	void populate(Properties props);
}
