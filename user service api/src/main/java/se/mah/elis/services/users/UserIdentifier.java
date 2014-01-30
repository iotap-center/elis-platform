/**
 * 
 */
package se.mah.elis.services.users;

import se.mah.elis.data.Identifier;

/**
 * The UserIdentifier interface is used identify a platform user.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public interface UserIdentifier extends Identifier {
	
	/**
	 * Returns the class definition of the object identified by this
	 * identifier.
	 * 
	 * @return The class definition of the object identified by this
	 * 		identifier.
	 * @since 1.1
	 */
	Class identifies();
	
	/**
	 * Sets the class definition of the object identified by the identifier.
	 * 
	 * @param clazz The class that this identifier identifies.
	 * @since 1.1
	 */
	void identifies(Class clazz);
}
