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
	private String meterId;
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
	
	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}
	
	public String getMeterId() {
		return meterId;
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
		meter.setName(meterId);
		gateway.add(meter);
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		if (uuid != null)
			props.put("uuid", uuid);
		props.put("meterId", meterId);
		props.put("created", created);
		props.put("service_name", getServiceName());
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("uuid", uuid);
		props.put("meterId", "32");
		props.put("created", created);
		props.put("service_name", "32");
		return props;
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		if (!props.containsKey("uuid") && !props.containsKey("meterId"))
			throw new IllegalArgumentException("Missing properties");
		
		if (props.containsKey("created")) {
			created = (DateTime) props.get("created");
		}

		meterId = (String) props.get("meterId");

		log(LogService.LOG_INFO, "Populating MKB user: " + meterId);
		
		MkbGatewayUserProvider factory = new MkbGatewayUserProvider();
		setGateway(factory.createGateway(this));
		gateway.setUser(this);
		
		try {
			initialize();
		} catch (UserInitalizationException e) {
			log(LogService.LOG_ERROR, "Failed to initialise gateway for user: " + meterId);
		}
		
		log(LogService.LOG_INFO, "Done populating MKB user: " + meterId);
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
	
	@Override
	public String toString() {
		return meterId;
	}
	
	protected void bindLog(LogService log) {
		this.log = log;
	}
	
	protected void unbindLog(LogService log) {
		this.log = null;
	}

	protected void log(int level, String message) {
		if (log != null) {
			log.log(level, message);
		}
	}
}
