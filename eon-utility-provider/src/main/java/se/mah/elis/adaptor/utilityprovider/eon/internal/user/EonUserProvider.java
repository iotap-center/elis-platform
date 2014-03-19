package se.mah.elis.adaptor.utilityprovider.eon.internal.user;

import java.util.Properties;

import javax.naming.AuthenticationException;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.device.api.providers.GatewayUserProvider;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;

/**
 * 
 * When the adaptor starts this user provider is registered with the platform's
 * user factory. It contains logic to create and authenticate E.On users.   
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
public class EonUserProvider implements UserProvider {

	/**
	 * Create an E.On user using the properties "email" and "password" which 
	 * must be contained in the properties param. 
	 * 
	 * @param properties with "email" and "password" keys
	 * @return an instance of an {@link EonGatewayUser}
	 * @since 1.0  
	 */
	@Override
	public User build(Properties properties) throws UserInitalizationException {
		String email = (String) properties.getProperty("email");
		String password = (String) properties.getProperty("password");
		
		GatewayUserProvider eonUserFactory = new EonGatewayUserFactory();
		GatewayUser user = null;
		
		try {
			user = eonUserFactory.getUser(email, password);
		} catch (AuthenticationException ae) {
			throw new UserInitalizationException("eon", "Could not log in user");
		} catch (MethodNotSupportedException mnse) {
			throw new UserInitalizationException("eon", "Method not supported");
		}
		
		return user;
	}

	@Override
	public UserRecipe getRecipe() {
		return new EonUserRecipe();
	}

}
