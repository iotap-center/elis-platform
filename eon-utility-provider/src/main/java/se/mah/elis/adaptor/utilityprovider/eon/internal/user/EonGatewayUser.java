package se.mah.elis.adaptor.utilityprovider.eon.internal.user;

import java.util.Properties;
import java.util.UUID;

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

	private Gateway gateway;
	private EonGatewayUserIdentifer gatewayUserIdentifier;
	private UUID id;
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
		return id;
	}

	@Override
	public void setUserId(UUID id) {
		this.id = id;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("userid", id);
		props.putAll(gatewayUserIdentifier.getProperties());
		props.put("gateway", gateway.getId());
		props.put("created", created);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceName() {
		return "eon";
	}

	@Override
	public void populate(Properties arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DateTime created() {
		return created;
	}

}
