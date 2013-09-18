/**
 * 
 */
package se.mah.elis.services.users;

/**
 * The PlatformUser interface describes a platform user.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface PlatformUser extends AbstractUser {
	
	/**
	 * Sets the user's first name.
	 * 
	 * @param name The name. Must not be null. Any leading or trailing spaces
	 * 		  will be stripped away.
	 * @since 1.0
	 */
	void setFirstName(String name);
	
	/**
	 * Gets the user's first name.
	 * 
	 * @return The name.
	 * @since 1.0
	 */
	String getFirstName();
	
	/**
	 * Sets the user's last name.
	 * 
	 * @param name The name. Must not be null. Any leading or trailing spaces
	 * 		  will be stripped away.
	 * @since 1.0
	 */
	void setLastName(String name);
	
	/**
	 * Gets the user's last name.
	 * 
	 * @return The name.
	 * @since 1.0
	 */
	String getLastName();
	
	/**
	 * Sets the user's e-mail address.
	 * 
	 * @param name The address. The address must be a valid mail address.
	 * @since 1.0
	 */
	void setEmail(String address);
	
	/**
	 * Gets the user's e-mail address.
	 * 
	 * @return The e-mail address.
	 * @since 1.0
	 */
	String getEmail();
}
