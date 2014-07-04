/**
 * 
 */
package se.mah.elis.services.users;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;

/**
 * <p>The AbstractUser interface describes a generic user in the platform.</p>
 * 
 * <p>In order for a user object to be storable its properties, as returned by
 * {@link #getProperties()} and {@link #getPropertiesTemplate()}, must contain
 * at least the three fields <i>uuid</i> (a {@link java.util.UUID} object
 * identifying this user object), <i>created</i> (a
 * {@link org.joda.time.DateTime} object, holding the date and time when
 * this object was first created), and finally <i>service_name</i> (a
 * {@link java.lang.String} containing the name of the service providing this
 * user type).</p>
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public interface AbstractUser {
	
	/**
	 * Gets this user object's id number. The id number is unique throughout
	 * the Elis platform.
	 * 
	 * @return The id number.
	 * @since 2.0
	 */
	UUID getUserId();
	
	/**
	 * <p>Sets this user object's id number. This id number is used internally
	 * by the Elis platform and shall not in any way depend on any underlying
	 * system's identification mechanism. Any system specific identification is
	 * handled by the {@link UserIdentifier} interface.</p>
	 * 
	 * <p>If no user id is set, the Elis system will assign the user id on its
	 * own when storing it persistently. This is the recommended way of working
	 * with user objects.</p>
	 * 
	 * @param id The id number.
	 * @since 2.0
	 */
	void setUserId(UUID id);

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
	 * @return An OrderedProperties object with all of the object's properties.
	 * @since 1.1
	 */
	OrderedProperties getProperties();

	/**
	 * <p>Get a description of the AbstractUser object.</p>
	 * 
	 * <p>The first object in the properties collection must be the global user
	 * id, which must be a UUID object. The identifier object may be included
	 * under the key "identifier", or in its flattened form. This method is
	 * primarily used by the persistent storage service to determine the layout
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
	 * <p>Populate the values of an AbstractUser instance by providing a set of
	 * properties, e.g. from a persistent storage retrieval.</p>
	 * 
	 * <p>The Properties object may or may not contain a UserIdentifier object.
	 * If not, the method will try to create a UserIdentifier of its own.</p>
	 * 
	 * @param props The properties to populate the object with.
	 * @throws IllegalArgumentException if the properties aren't correct.
	 * @since 1.1
	 */
	void populate(Properties props) throws IllegalArgumentException;
	
	/**
	 * Gets the service name associated with the user type.
	 * 
	 * @return The name of the service associated with the user.
	 * @since 1.1
	 */
	String getServiceName();
	
	/**
	 * Returns the date when the user was first created.
	 * 
	 * @return The time that the user was first created.
	 * @since 20.
	 */
	DateTime created();
}
