package se.mah.elis.adaptor.water.mkb;

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
 * Provider which is registered with the Elis user factory and is able 
 * to create MKB users from the properties provided. 
 * 
 * @author Marcus Ljungblad
 * @version 1.0
 * @since 1.0
 *
 */
public class MkbProvider implements UserProvider {

	/**
	 * Provided with a properties object containing "id" (the meter id) is able 
	 * to instantiate {@link MkbGatewayUser}s.
	 * 
	 * @return an {@link MkbGatewayUser}
	 */
	@Override
	public User build(Properties properties) throws UserInitalizationException {
		if (!properties.containsKey("meterId")) {
			throw new UserInitalizationException();
		}
		
		String meterId = (String) properties.getProperty("meterId");
		
		GatewayUserProvider gatewayUserProvider = new MkbGatewayUserProvider();
		GatewayUser user = null;

		try {
			user = gatewayUserProvider.getUser(meterId, "");
			if (properties.containsKey("uuid"))
				user.setUserId((UUID) properties.get("uuid"));
		} catch (AuthenticationException | MethodNotSupportedException e) {
			throw new UserInitalizationException();
		}
		
		return user;
	}

	@Override
	public UserRecipe getRecipe() {
		return new MkbUserRecipe();
	}

}
