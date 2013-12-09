/**
 * 
 */
package se.mah.elis.services.users;

import se.mah.elis.services.users.UserIdentifier;

/**
 * This is the identifier interface for PlatformUser objects. Since the
 * PlatformUser is the key to most user interaction, it makes sense to make
 * this particular identifier "open" for the rest of the platform to see. 
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public interface PlatformUserIdentifier extends UserIdentifier {

	/**
	 * Sets the id number of this platform user.
	 * 
	 * @param id The platform user's id number. Must not be less than 1. 
	 * @throws IllegalArgumentException if the user id is malformed.
	 * @since 1.0
	 */
	public void setId(int id) throws IllegalArgumentException;
	
	/**
	 * Gets the id number of this platform user.
	 * 
	 * @return The platform user's id number. Zero if not set.
	 * @since 1.0
	 */
	public int getId();
	
	
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
}
