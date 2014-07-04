package se.mah.elis.adaptor.energy.eon.internal.user;

import javax.naming.AuthenticationException;
import javax.ws.rs.client.ResponseProcessingException;

import org.apache.felix.scr.annotations.Reference;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.device.api.providers.GatewayUserProvider;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.energy.eon.EonAdaptor;
import se.mah.elis.adaptor.energy.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.energy.eon.internal.gateway.EonGateway;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * E.On user factory
 * 
 * Create a new E.On user instance by calling {@link #getUser(String, String)}.
 * Afterwards you most likely want to run {@link EonGatewayUser#initialize()} to
 * populate the gateway with data and devices from the E.On service.
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class EonGatewayUserFactory implements GatewayUserProvider {
	
	/**
	 * Creates an gateway user with an uninitialised gateway
	 * 
	 * @param username
	 * @param password
	 * @return an E.On gateway user
	 * @throws MethodNotSupportedException
	 * @throws AuthenticationException
	 */
	public GatewayUser getUser(String username, String password)
			throws AuthenticationException, MethodNotSupportedException {
		EonHttpBridge bridge = createBridgeFromConfig();
		return this.getUser(username, password, bridge);
	}

	/**
	 * Create a gateway user and gateway using a custom HTTP bridge
	 * 
	 * @param username
	 * @param password
	 * @param bridge
	 * @return
	 * @throws MethodNotSupportedException
	 * @throws AuthenticationException
	 */
	public GatewayUser getUser(String username, String password,
			EonHttpBridge bridge) throws MethodNotSupportedException,
			AuthenticationException {
		EonGatewayUser user = createGatewayUser(username, password);
		EonGateway gateway = createGateway(username, password, bridge);
		user.setGateway((Gateway) gateway);
		
		try {
			user.initialize();
		} catch (UserInitalizationException e) {
		}
		
		return user;
	}

	/**
	 * Create a gateway using the default E.On HTTP bridge 
	 *  
	 * @param username
	 * @param password
	 * @return
	 * @throws AuthenticationException
	 */
	public EonGateway createGateway(String username, String password)
			throws AuthenticationException {
		EonHttpBridge bridge = createBridgeFromConfig();
		return createGateway(username, password, bridge);
	}

	/**
	 * Create a gateway using a custom HTTP bridge
	 * 
	 * @param username
	 * @param password
	 * @param httpBridge
	 * @return
	 * @throws AuthenticationException
	 */
	public EonGateway createGateway(String username, String password,
			EonHttpBridge httpBridge) throws AuthenticationException {
		EonGateway gateway = new EonGateway();

		String token;
		try {
			token = httpBridge.authenticate(username, password);
		} catch (AuthenticationException | ResponseProcessingException e) {
			throw new AuthenticationException(
					"Could not authenticate against E.On for " + username);
		}

		gateway.setAuthenticationToken(token);
		gateway.setHttpBridge(httpBridge);

		return gateway;
	}

	private EonGatewayUser createGatewayUser(String username, String password) {
		EonGatewayUser user = new EonGatewayUser();
		user.setUsername(username);
		user.setPassword(password);
		return user;
	}

	private EonHttpBridge createBridgeFromConfig() {
		String host = (String) EonAdaptor.properties.get(EonAdaptor.TARGET_HOST);
		int port = (Integer) EonAdaptor.properties.get(EonAdaptor.TARGET_PORT);
		String prefix = (String) EonAdaptor.properties.get(EonAdaptor.TARGET_APIPREFIX);
		
		return new EonHttpBridge(host, port, prefix);
	}
}
