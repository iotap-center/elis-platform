package se.mah.elis.adaptor.water.mkb;

import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Reference;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.GatewayCommunicationException;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * 
 * MKB Gateway user. When initialised also adds the meter registered with the user 
 * account. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
public class MkbGatewayUser implements GatewayUser {

	@Reference
	private LogService log;
	
	private UUID uuid;
	private UserIdentifier userIdentifier = new MkbUserIdentifier(null);
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
		if (uuid != null)
			props.put("uuid", uuid);
		props.putAll(getIdentifier().getProperties());
		props.put("created", created);
		props.put("service_name", getServiceName());
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("uuid", uuid);
		props.putAll(getIdentifier().getPropertiesTemplate());
		props.put("created", created);
		props.put("service_name", "32");
		return props;
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		if (!props.containsKey("uuid") && !props.containsKey("meterId"))
			throw new IllegalArgumentException("Missing properties");
		
		if (props.containsKey("created"))
			created = (DateTime) props.get("created");
		
		String meterId = (String) props.get("meterId");

		log.log(LogService.LOG_INFO, "Populating MKB user: " + meterId);
		
		MkbUserIdentifier id = new MkbUserIdentifier(meterId);
		setIdentifier(id);
		MkbGatewayUserProvider factory = new MkbGatewayUserProvider();
		setGateway(factory.createGateway(this));
		gateway.setUser(this);
		
		try {
			initialize();
		} catch (UserInitalizationException e) {
			log.log(LogService.LOG_ERROR, "Failed to initialise gateway for user: " + meterId);
		}
		
		log.log(LogService.LOG_INFO, "Done populating MKB user: " + meterId);
	}

	@Override
	public String getServiceName() {
		return new MkbUserRecipe().getServiceName();
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
	
	protected void bindLog(LogService log) {
		this.log = log;
	}
	
	protected void unbindLog(LogService log) {
		this.log = null;
	}

}
