/**
 * 
 */
package se.mah.elis.services.users.factory;

import java.util.Properties;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * The UserProvider interface is used to register user providing services to
 * a UserFactory. Each service that requires a user of some kind can implement
 * UserProvider and let UserFactory produce users. 
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public interface UserProvider {
	
	/**
	 * Builds a user.
	 * 
	 * @param properties A set of properties needed to build this kind of user.
	 * @return The newly built user object.
	 * @throws UserInitalizationException if the user couldn't be built for
	 * 		   whatever reason.
	 * @since 1.0
	 */
	public User build(Properties properties)throws UserInitalizationException;
	
	/**
	 * Returns the user recipe describing the data needed to successfully build
	 * a user of this kind. 
	 * 
	 * @return The user recipe.
	 * @since 1.0
	 */
	public UserRecipe getRecipe();
}
