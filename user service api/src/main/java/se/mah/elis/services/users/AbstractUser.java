/**
 * 
 */
package se.mah.elis.services.users;

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

}
