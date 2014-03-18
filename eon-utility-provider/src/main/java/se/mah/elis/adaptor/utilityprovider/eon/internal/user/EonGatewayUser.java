package se.mah.elis.adaptor.utilityprovider.eon.internal.user;

import java.util.Properties;
import java.util.UUID;

import javax.naming.AuthenticationException;

import org.joda.time.DateTime;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.GatewayCommunicationException;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * Representation of a gateway user for E.On 
 * @author Marcus Ljungblad
 * @version 1.1.0
 * @since 1.0
 */
public class EonGatewayUser implements GatewayUser {

	private UUID uuid;
	private Gateway gateway;
	private EonGatewayUserIdentifer gatewayUserIdentifier;
	private DateTime created = DateTime.now();

	/**
	 * Will try to initialise the gateway if that has not been done before. 
	 * 
	 * @throws UserInitalizationException if initialisation of gateway fails
	 */
	@Override
	public void initialize() throws UserInitalizationException {
		if (!gateway.hasConnected()) {
			try {
				gateway.connect();
			} catch (GatewayCommunicationException e) {
				throw new UserInitalizationException();
			}
		}
	}

	@Override
	public UserIdentifier getIdentifier() {
		return gatewayUserIdentifier;
	}

	@Override
	public void setIdentifier(UserIdentifier userIdentifier) {
		gatewayUserIdentifier = (EonGatewayUserIdentifer) userIdentifier;
		gatewayUserIdentifier.identifies(this.getClass());
	}

	@Override
	public Gateway getGateway() {
		return gateway;
	}

	@Override
	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public UUID getUserId() {
		return uuid;
	}

	@Override
	public void setUserId(UUID id) {
		this.uuid = id;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", uuid);
		props.put("gatewayUserIdentifier", gatewayUserIdentifier);
		props.put("gateway", gateway.getId());
		props.put("created", created);
		props.put("service_name", getServiceName());
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("uuid", uuid);
		props.put("gatewayUserIdentifier", gatewayUserIdentifier);
		props.put("gateway", new Integer(0));
		props.put("created", created);
		props.put("service_name", "3");
		return props;
	}

	@Override
	public String getServiceName() {
		return "eon";
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		if (!props.containsKey("username") && 
			!props.containsKey("password") &&
			!props.containsKey("uuid"))
			throw new IllegalArgumentException();
		
		if (props.containsKey("created")) 
			created = (DateTime) props.get("created");
		
		String username = (String) props.get("username");
		String password = (String) props.get("password");
		
		EonGatewayUserIdentifer gwId = new EonGatewayUserIdentifer();
		gwId.setPassword(password);
		gwId.setUsername(username);

		setIdentifier(gwId);
		setUserId((UUID) props.get("uuid"));
		
		EonGatewayUserFactory factory = new EonGatewayUserFactory();
		try {
			setGateway(factory.createGateway(username, password));
			gateway.setUser(this);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		
	}

	@Override
	public DateTime created() {
		return created;
	}

}
