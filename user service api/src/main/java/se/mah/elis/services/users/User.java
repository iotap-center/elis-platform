package se.mah.elis.services.users;

import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * The User interface describes a generic user in the platform.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public interface User extends AbstractUser {
	
	/**
	 * Gets this user object's id number.
	 * 
	 * @return The id number.
	 * @since 1.0
	 */
	int getIdNumber();
	
	/**
	 * Sets this user object's id number. This id number is used internally by
	 * the Elis platform and shall not in any way depend on any underlying
	 * system's identification mechanism. Any system specific identification is
	 * handled by the {@link UserIdentifier} interface.
	 * 
	 * @param id The id number.
	 * @since 1.0
	 */
	void setIdNumber(int id);

	/**
	 * Initializes this user and fetches data associated with this user.
	 * 
	 * @throws UserInitalizationException if the initialization didn't succeed.
	 * @since 1.0
	 */
	void initialize() throws UserInitalizationException;
}
