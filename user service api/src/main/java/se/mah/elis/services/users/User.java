package se.mah.elis.services.users;

import java.util.Properties;
import java.util.UUID;

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
	 * Gets this user object's id number. The id number is unique throughout
	 * the Elis platform.
	 * 
	 * @return The id number.
	 * @since 1.1
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
	 * @since 1.1
	 */
	void setUserId(UUID id);

	/**
	 * Initializes this user and fetches data associated with this user. The
	 * identifier must be set in order for this method to work.
	 * 
	 * @throws UserInitalizationException if the initialization didn't succeed.
	 * @since 1.0
	 */
	void initialize() throws UserInitalizationException;
}
