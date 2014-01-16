/**
 * 
 */
package se.mah.elis.services.users;

import java.util.Properties;

/**
 * The User interface describes a generic user in the platform.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public interface AbstractUser {
	
	/**
	 * Get this user's identifier.
	 * 
	 * @return The identifier.
	 * @since 1.0
	 */
	UserIdentifier getIdentifier();
	
	/**
	 * Set this user's identifier.
	 * 
	 * @param id The identifier.
	 * @since 1.0
	 */
	void setIdentifier(UserIdentifier id);

	/**
	 * <p>Get a Properties-based representation of the object, the identifier
	 * included as the first object under key "identifier". This method is
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
	 * <p>Get a description of the AbstractUser object, the identifier included
	 * as the first object under key "identifier". This method is primarily
	 * used by the persistent storage service to determine the layout to be
	 * used by the underlying storage engine.</p>
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
	Properties getPropertiesTemplate();
	
	/**
	 * <p>Populate the values of an AbstractUser instance by providing a set of
	 * properties, e.g. from a persistent storage retrieval.</p>
	 * 
	 * <p>The Properties object may or may not contain a UserIdentifier object.
	 * If not, the method will try to create a UserIdentifier of its own.</p>
	 * 
	 * @param props The properties to populate the object with.
	 * @since 1.1
	 */
	void populate(Properties props);
}
