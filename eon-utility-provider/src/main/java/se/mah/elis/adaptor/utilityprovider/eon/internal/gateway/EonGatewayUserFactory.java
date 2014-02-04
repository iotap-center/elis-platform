package se.mah.elis.adaptor.utilityprovider.eon.internal.gateway;

import javax.naming.AuthenticationException;
import javax.ws.rs.client.ResponseProcessingException;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.device.api.providers.GatewayUserProvider;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;

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

	public GatewayUser getUser(String username, String password,
			EonHttpBridge bridge) throws MethodNotSupportedException,
			AuthenticationException {
		EonGatewayUser user = createGatewayUser(username, password);
		EonGateway gateway = createGateway(username, password, bridge);
		user.setGateway(gateway);
		gateway.setUser(user);
		return user;
	}

	private EonGateway createGateway(String username, String password,
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
		EonGatewayUserIdentifer userIdentifier = new EonGatewayUserIdentifer();
		userIdentifier.setUsername(username);
		userIdentifier.setPassword(password);
		user.setIdentifier(userIdentifier);
		return user;
	}

	private EonHttpBridge createBridgeFromConfig() {
		// TODO: this should read from standard osgi configuration
		return new EonHttpBridge("http://ewpapi2.dev.appex.no", 80,
				"/v0_2/api/");
	}
}
