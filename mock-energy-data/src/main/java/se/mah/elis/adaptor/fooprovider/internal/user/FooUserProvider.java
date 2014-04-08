package se.mah.elis.adaptor.fooprovider.internal.user;

import java.util.Properties;
import java.util.UUID;

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
public class FooUserProvider implements UserProvider {

	@Override
	public User build(Properties properties) throws UserInitalizationException {
		GatewayUser user = new FooGatewayUser();
		
		user.populate(properties);
		
		return user;
	}

	@Override
	public UserRecipe getRecipe() {
		return new FooUserRecipe();
	}

}
