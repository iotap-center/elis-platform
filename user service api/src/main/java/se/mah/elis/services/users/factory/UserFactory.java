/**
 * 
 */
package se.mah.elis.services.users.factory;

import java.util.Properties;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * The UserFactory builds users. A UserProvider can register with a UserFactory
 * so that the factory can produce the user types provided by the UserProvider.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface UserFactory {
	
	/**
	 * Registers a user provider with this factory.
	 * 
	 * @param provider The UserProvider to register.
	 * @since 1.0
	 */
	public void registerProvider(UserProvider provider);
	
	/**
	 * Unregisters a user provider from this factory.
	 * 
	 * @param provider The UserProvider to unregister.
	 * @since 1.0
	 */
	public void unregisterProvider(UserProvider provider);
	
	/**
	 * Builds a user. As a user type can be provided by several different
	 * supporting services, we must also provide the name of the service that
	 * the user should target.
	 * 
	 * @param userType The user type that we want to build. This should match
	 * 		  the name of the implemented User interface.
	 * @param serviceName The name of the service that we want to target.
	 * @param properties The properties of the new user, as specified by its
	 * 		  UserRecipe.
	 * @return The newly built User object.
	 * @throws UserInitalizationException if the user couldn't be built for
	 * 		   whatever reason.
	 * @since 1.0
	 */
	public User build(String userType, String serviceName, Properties properties)
			throws UserInitalizationException;
	
	/**
	 * Returns a list of all available user recipes.
	 * 
	 * @return An array of user recipes. If no user recipes are available, the
	 * 		   method returns an empty array.
	 * @since 1.0
	 */
	public UserRecipe[] getAvailableUserRecipes();
	
	/**
	 * Returns a user recipe.
	 * 
	 * @param userType The user type that we want a recipe for.
	 * @param systemName The name of the system providing the user.
	 * @return A user recipe.
	 * @since 1.0
	 */
	public UserRecipe getRecipe(String userType, String systemName);
}
