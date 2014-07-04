/**
 * 
 */
package se.mah.elis.services.users;

/**
 * The PlatformUser interface describes a platform user.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public interface PlatformUser extends AbstractUser {
	
	
	/**
	 * Sets the user name of the platform user.
	 * 
	 * @param username The platform user's user name. The user name will be
	 * 		  trimmed of any initial or trailing white spaces.
	 * @throws IllegalArgumentException if the user name is malformed.
	 * @since 1.0
	 */
	public void setUsername(String username) throws IllegalArgumentException;
	
	/**
	 * Returns the user name of the platform user.
	 * 
	 * @return The user name. If not set, an empty string is returned.
	 * @since 1.0
	 */
	public String getUsername();

	/**
	 * Sets the user's password.
	 * 
	 * @param password The user's password.
	 * @since 1.0
	 */
	public void setPassword(String password);
	
	/**
	 * Gets the password of the platform user.
	 * 
	 * @return The user's password.
	 * @since 1.0
	 */
	public String getPassword();
	
	/**
	 * Checks whether the platform user is properly addressed. The platform
	 * user is considered to be non-empty if the user name AND the password are
	 * both set.
	 * 
	 * @return True if empty, otherwise false.
	 * @since 1.0
	 */
	public boolean isEmpty();
	
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
