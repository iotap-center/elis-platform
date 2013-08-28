/**
 * 
 */
package se.mah.elis.services.users;

/**
 * The User interface describes a generic user in the platform.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface AbstractUser {
	
	/**
	 * Get this user's identifier.
	 * 
	 * @return The identifier.
	 * @since 1.0
	 */
	UserIdentifier getId();
	
	/**
	 * Set this user's identifier.
	 * 
	 * @param id The identifier.
	 * @since 1.0
	 */
	void setId(UserIdentifier id);

}
