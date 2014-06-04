package se.mah.elis.adaptor.energy.eon.internal.user;

import java.util.Properties;
import java.util.UUID;

import javax.naming.AuthenticationException;

import org.apache.felix.scr.annotations.Reference;
import org.osgi.service.log.LogService;

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

	@Reference
	private LogService log;

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
		log("Building E.On user");
		
		String username = (String) properties.getProperty("username");
		String password = (String) properties.getProperty("password");
		
		GatewayUserProvider eonUserFactory = new EonGatewayUserFactory();
		GatewayUser user = null;
		
		try {
			user = eonUserFactory.getUser(username, password);
			if (properties.containsKey("uuid"))
				user.setUserId((UUID) properties.get("uuid"));
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
	
	protected void bindLog(LogService service) {
		log = service;
	}
	
	protected void unbindLog(LogService service) {
		log = null;
	}
	
	private void log(String message) {
		log(LogService.LOG_INFO, message);
	}
	
	private void log(int logLevel, String message) {
		if (log != null) {
			log.log(logLevel, message);
		}
	}

}
