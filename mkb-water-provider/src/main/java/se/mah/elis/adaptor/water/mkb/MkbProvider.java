package se.mah.elis.adaptor.water.mkb;

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
	 * to instantiate {@link MkbGatwayUser}s.
	 * 
	 * @return an {@link MkbGatwayUser}
	 */
	@Override
	public User build(Properties properties) throws UserInitalizationException {
		String meterId = (String) properties.getProperty("id");
		GatewayUserProvider userFactory = new MkbGatewayUserFactory();
		GatewayUser user = null;
		try {
			user = userFactory.getUser(meterId, "");
		} catch (AuthenticationException | MethodNotSupportedException e) {
			System.out.println("Could not get meter for id: " + meterId);
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public UserRecipe getRecipe() {
		return new MkbUserRecipe();
	}

}
