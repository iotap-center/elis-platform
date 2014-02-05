package se.mah.elis.adaptor.utilityprovider.eon.internal.gateway;

import java.util.Properties;
import java.util.UUID;

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

	private Gateway gateway;
	private EonGatewayUserIdentifer gatewayUserIdentifier;
	private int id; // TODO: remove
	private UUID uuid;

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
	@Deprecated
	public int getIdNumber() {
		return id;
	}

	@Override
	@Deprecated
	public void setIdNumber(int id) {
		this.id = id;
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
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("uuid", getUserId().toString());
		props.put("gateway", gateway.getId());
		props.put("gatewayUserIdentifier", gatewayUserIdentifier);
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", "256");
		props.put("gateway", new Integer(0));
		
		return props;
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		this.uuid = UUID.fromString((String) props.get("uuid"));
		// TODO: create gateway from ID
	}

	@Override
	public String getServiceName() {
		return "eon-gateway-user";
	}

}
