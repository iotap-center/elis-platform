package se.mah.elis.adaptor.energy.eon.internal.devices;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.energy.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.energy.eon.internal.gateway.EonGateway;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * Class used as a base class for all other E.On device implementations. It is
 * an abstract class, and must be sub-classed in order to be used. When
 * sub-classing EonDevice, make sure to call its getProperties(),
 * getOrderedProperties() and populate() methods in order to retain the values
 * needed by the storage service. 
 * 
 * @author Marcus Ljungblad
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 * @version 1.0.0
 */
public abstract class EonDevice implements Device {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9149366951066322178L;
	// constants
	public final static int TYPE_POWERSWITCH_METER = 49;
	public final static int TYPE_THERMOMETER = 101;
	public final static int TYPE_POWERMETER = 51;
	public final static int TYPE_THERMOSTAT = 96;

	// fields
	protected EonHttpBridge httpBridge;
	protected UUID dataid;
	protected UUID ownerid;
	protected DateTime created = DateTime.now();
	protected boolean isOnline;
	protected EonGateway gateway;
	protected String deviceName = "";
	protected String description = "";

	/**
	 * Attach a HTTP bridge to the device - mock bridges can be used during
	 * testing.
	 * 
	 * @param bridge
	 */
	public void setHttpBridge(EonHttpBridge bridge) {
		httpBridge = bridge;
	}

	@Override
	public UUID getDataId() {
		return dataid;
	}

	@Override
	public void setDataId(UUID uuid) {
		dataid = uuid;
	}

	@Override
	public void setOwnerId(UUID userId) {
		ownerid = userId;
	}

	@Override
	public UUID getOwnerId() {
		return ownerid;
	}

	@Override
	public DateTime created() {
		return created;
	}

	@Override
	public String getName() {
		return deviceName;
	}

	@Override
	public void setName(String name) throws StaticEntityException {
		deviceName = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) throws StaticEntityException {
		this.description = description;
	}

	@Override
	public Gateway getGateway() {
		return gateway;
	}

	@Override
	public void setGateway(Gateway gateway) throws StaticEntityException {
		if (!(gateway instanceof EonGateway))
			throw new StaticEntityException();
		this.gateway = (EonGateway) gateway;
	}

	@Override
	public DeviceSet[] getDeviceSets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOnline() {
		return isOnline;
	}

	@Override
	public Properties getProperties() {
		OrderedProperties props = new OrderedProperties();
		props.put("dataid", dataid);
		props.put("ownerid", ownerid);
		props.put("created", created);
		props.put("device_name", deviceName);
		props.put("description", description);
		props.put("gateway", gateway.getDataId());
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", created);
		props.put("device_name", "64");
		props.put("description", "256");
		props.put("gateway", UUID.randomUUID());
		return props;
	}

	@Override
	public void populate(Properties props) {
		this.dataid = (UUID) props.get("dataid");
		this.ownerid = (UUID) props.get("ownerid");
		this.created = (DateTime) props.get("created");
		this.deviceName = (String) props.get("device_name");
		this.description = props.getProperty("description");
		
		// TODO Create gateway
	}
}
