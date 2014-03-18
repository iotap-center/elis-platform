package se.mah.elis.adaptor.water.mkb;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.GatewayCommunicationException;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MkbGatwayUser implements GatewayUser {

	private UUID uuid;
	private UserIdentifier userIdentifier;
	private Gateway gateway;
	private DateTime created = DateTime.now();

	@Override
	public UUID getUserId() {
		return this.uuid;
	}

	@Override
	public void setUserId(UUID id) {
		this.uuid = id;
	}

	@Override
	public void initialize() throws UserInitalizationException {
		if (!gateway.hasConnected()) {
			try {
				addWaterMeter();
				gateway.connect();
			} catch (GatewayCommunicationException | StaticEntityException e) {
				throw new UserInitalizationException();
			}
		}
	}

	private void addWaterMeter() throws StaticEntityException {
		MkbWaterMeter meter = new MkbWaterMeter();
		meter.setGateway(gateway);
		meter.setName(userIdentifier.toString());
		meter.setId(new MkbWaterMeterDeviceIdentifier(userIdentifier.toString()));
		gateway.add(meter);
	}

	@Override
	public UserIdentifier getIdentifier() {
		return this.userIdentifier;
	}

	@Override
	public void setIdentifier(UserIdentifier id) {
		this.userIdentifier = id;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		props.put("uuid", uuid);
		props.put("userIdentifier", getIdentifier());
		props.put("created", created);
		props.put("service_name", getServiceName());
		props.put("meterId", getIdentifier().toString());
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("uuid", uuid);
		props.put("userIdentifier", getIdentifier());
		props.put("created", created);
		props.put("service_name", getServiceName().length());
		props.put("meterId", "32");
		return props;
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		if (!props.containsKey("uuid") &&
				!props.containsKey("userIdentifier") &&
				!props.containsKey("meterId"))
			throw new IllegalArgumentException("Missing properties");
		
		if (props.containsKey("created"))
			created = (DateTime) props.get("created");
		
		String meterId = (String) props.get("meterId");
		MkbUserIdentifier id = new MkbUserIdentifier(meterId);
		setIdentifier(id);
		MkbGatewayUserFactory factory = new MkbGatewayUserFactory();
		setGateway(factory.createGateway(this));
		gateway.setUser(this);
	}

	@Override
	public String getServiceName() {
		return "mkb-water-gateway-user";
	}

	@Override
	public Gateway getGateway() {
		return this.gateway;
	}

	@Override
	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public DateTime created() {
		return created;
	}

}
