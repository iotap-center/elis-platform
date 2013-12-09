package se.mah.elis.services.users.factory;

import java.util.Properties;

/**
 * The UserRecipe interface describes a way to build a user, using a
 * UserFactory.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public interface UserRecipe {
	
	/**
	 * Returns the user type description. This should match the name of the
	 * User interface being implemented.
	 * 
	 * @return The name of the user type.
	 * @since 1.0
	 */
	public String getUserType();
	
	/**
	 * Returns the service name that provides this user type.
	 * 
	 * @return The name of the providing service.
	 * @since 1.0
	 */
	public String getServiceName();
	
	/**
	 * Returns a list of properties needed to successfully build a user.
	 * 
	 * @return A list of properties.
	 * @since 1.0
	 */
	public Properties getProperties();
}
