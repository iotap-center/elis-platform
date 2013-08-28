package se.mah.elis.services.users;

import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * The User interface describes a generic user in the platform.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface User extends AbstractUser {

	/**
	 * Initializes this user and fetches data associated with this user.
	 * 
	 * @throws UserInitalizationException if the initialization didn't succeed.
	 * @since 1.0
	 */
	void initialize() throws UserInitalizationException;
}
