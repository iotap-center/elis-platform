/**
 * 
 */
package se.mah.elis.adaptor.building.api.providers;

import javax.naming.AuthenticationException;

import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.exceptions.MethodNotSupportedException;

/**
 * The GatewayUserProvider interface is used to describe a set of methods to
 * retrieve a GatewayUser.
 * 
 * @since 1.0
 * @author "Johan Holmberg, Malmö University"
 */
public interface GatewayUserProvider {
	
	/**
	 * Retrieves a GatewayUser based on a username and a password.
	 * 
	 * @param username The username.
	 * @param password The password.
	 * @return A populated GatewayUser object.
	 * @throws MethodNotSupportedException if the method isn't supported.
	 * @throws AuthenticationException if the user data couldn't be
	 * 		   authenticated.
	 */
	GatewayUser getUser(String username, String password)
			throws MethodNotSupportedException, AuthenticationException;
}
