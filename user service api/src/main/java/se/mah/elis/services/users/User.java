package se.mah.elis.services.users;

import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * The User interface describes a generic non-platform user in the platform. A
 * User object typically holds the user credentials for external systems, be it
 * a domotic system, a social media account, or even a gaming account.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public interface User extends AbstractUser {

	/**
	 * Initializes this user and fetches data associated with this user. The
	 * identifier must be set in order for this method to work.
	 * 
	 * @throws UserInitalizationException if the initialization didn't succeed.
	 * @since 1.0
	 */
	void initialize() throws UserInitalizationException;
}
