package se.mah.elis.adaptor.fooprovider.internal.user;

import java.util.Properties;
import java.util.UUID;

import javax.naming.AuthenticationException;

import org.joda.time.DateTime;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.GatewayCommunicationException;
import se.mah.elis.adaptor.fooprovider.internal.FooGateway;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class FooGatewayUser implements GatewayUser {

	private UUID uuid;
	private Gateway gateway;
	private FooGatewayUserIdentifer gatewayUserIdentifier;
	private DateTime created = DateTime.now();
	
	public FooGatewayUser() {
		gateway = new FooGateway();
	}

	/**
	 * Will try to initialise the gateway if that has not been done before. 
	 * 
	 * @throws UserInitalizationException if initialisation of gateway fails
	 * @since 1.0
	 */
	@Override
	public void initialize() throws UserInitalizationException {
	}

	@Override
	public UserIdentifier getIdentifier() {
		return gatewayUserIdentifier;
	}

	@Override
	public void setIdentifier(UserIdentifier userIdentifier) {
		gatewayUserIdentifier = (FooGatewayUserIdentifer) userIdentifier;
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
		
		if (uuid != null)
			props.put("uuid", uuid);
		props.put("identifier", gatewayUserIdentifier);
		props.put("created", created);
		props.put("service_name", getServiceName());
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("uuid", uuid);
		props.put("identifier", gatewayUserIdentifier);
		props.put("created", created);
		props.put("service_name", "32");
		return props;
	}

	@Override
	public String getServiceName() {
		return new FooUserRecipe().getServiceName();
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		if (!props.containsKey("uuid")) {
			throw new IllegalArgumentException();
		}
		
		if (props.containsKey("created")) {
			created = (DateTime) props.get("created");
		}
		
		FooGatewayUserIdentifer gwId = new FooGatewayUserIdentifer();

		setIdentifier(gwId);
		setUserId((UUID) props.get("uuid"));
	}

	@Override
	public DateTime created() {
		return created;
	}

}
