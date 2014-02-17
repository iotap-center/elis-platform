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

public class EonUserProvider implements UserProvider {

//	private static final Logger log = LoggerFactory.getLogger(EonUserProvider.class);
	
	@Override
	public User build(Properties properties) throws UserInitalizationException {
		String email = properties.getProperty("email");
		GatewayUserProvider eonProvider = new EonGatewayUserFactory();
		GatewayUser user = null;
		
		try {
			user = eonProvider.getUser(email, properties.getProperty("password"));
		} catch (AuthenticationException ae) {
//			log.warn("E.On provider try to login for user: " + email);
			throw new UserInitalizationException("eon", "Could not log in user");
		} catch (MethodNotSupportedException mnse) {
//			log.error("User login failed. " + mnse.getMessage());
			throw new UserInitalizationException();
		}
		
		return user;
	}

	@Override
	public UserRecipe getRecipe() {
		return new EonUserRecipe();
	}

}
