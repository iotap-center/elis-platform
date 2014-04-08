package se.mah.elis.adaptor.fooprovider.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Reference;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.data.GatewayAddress;
import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.GatewayCommunicationException;
import se.mah.elis.adaptor.fooprovider.internal.user.FooGatewayUser;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * Implementation of an E.On panel (in Elis terms: gateway)
 * 
 * An instance of gateway must have a
 * {@link se.mah.elis.adaptor.energy.eon.internal.EonHttpBridge} to function
 * properly. Most likely you also want to populate the gateway with data from
 * the E.On service. This is done using {@link FooGateway#connect()}.
 * 
 * The recommended way to retrieve a new gateway instance is to use
 * {@link FooGatewayUserFactory#getUser(String, String)} and retrieve the
 * gateway instance via the {@link FooGatewayUser} object.
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class FooGateway implements Gateway {
	private static final long serialVersionUID = -8937996755742476107L;
	private List<Device> devices;
	private String name;
	private GatewayAddress gatewayAddress;
	private GatewayUser gatewayUser;
	private int gatewayId;
	private UUID dataid;
	private UUID uniqueUserId;
	private DateTime created = DateTime.now();

	public FooGateway() {
		this.devices = new ArrayList<Device>();
	}

	@Reference
	private LogService log;

	@Override
	public int getId() {
		return this.gatewayId;
	}

	@Override
	public void setId(int id) throws StaticEntityException {
		this.gatewayId = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) throws StaticEntityException {
		this.name = name;
	}

	@Override
	public int size() {
		return devices.size();
	}

	@Override
	public boolean isEmpty() {
		return devices.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return devices.contains(o);
	}

	@Override
	public Iterator<Device> iterator() {
		return devices.iterator();
	}

	@Override
	public Object[] toArray() {
		return devices.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return devices.toArray(a);
	}

	@Override
	public boolean add(Device e) {
		return devices.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return devices.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return devices.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Device> c) {
		return devices.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return devices.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return devices.retainAll(c);
	}

	@Override
	public void clear() {
		devices.clear();
	}

	@Override
	public GatewayAddress getAddress() {
		return gatewayAddress;
	}

	@Override
	public void setAddress(GatewayAddress address) {
		gatewayAddress = address;
	}

	@Override
	public GatewayUser getUser() {
		return gatewayUser;
	}

	@Override
	public void setUser(GatewayUser user) {
		gatewayUser = user;
	}

	/**
	 * Populates the gateway with all known devices and maintains them in
	 * memory. Upon success this will cause hasConnected() to return true.
	 */
	@Override
	public void connect() throws GatewayCommunicationException {
		add(new FooPowerMeter());
		log("Mock gateway connected: " + getName());
	}

	@Override
	public boolean hasConnected() {
		return true;
	}

	@Override
	public boolean isOnline() {
		return true;
	}

	@Override
	public UUID getDataId() {
		return this.dataid;
	}

	@Override
	public void setOwnerId(UUID userId) {
		this.uniqueUserId = userId;
	}

	@Override
	public UUID getOwnerId() {
		return this.uniqueUserId;
	}

	@Override
	public Properties getProperties() {
		OrderedProperties props = new OrderedProperties();
		props.put("uuid", this.dataid);
		props.put("gatewayId", this.gatewayId);
		props.put("gatewayName", this.name);
		props.put("uniqueUserId", this.uniqueUserId);
		props.put("created", created);
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("uuid", this.dataid);
		props.put("gatewayId", this.gatewayId);
		props.put("gatewayName", "64");
		props.put("uniqueUserId", this.uniqueUserId);
		props.put("created", created);
		return props;
	}

	@Override
	public void populate(Properties props) {
		setDataId(UUID.fromString((String) props.get("uuid")));
		setOwnerId((UUID) props.get("uniqueUserId"));
		gatewayId = (int) props.get("gatewayId");
		name = (String) props.get("gatewayName");
		created = (DateTime) props.get("created");
	}

	@Override
	public void setDataId(UUID uuid) {
		this.dataid = uuid;
	}

	@Override
	public DateTime created() {
		return created;
	}

	private void log(String message) {
		log(LogService.LOG_INFO, message);
	}

	private void logWarning(String message) {
		log(LogService.LOG_WARNING, message);
	}

	private void log(int level, String message) {
		if (log != null)
			log.log(level, message);
	}

	protected void bindLog(LogService ls) {
		log = ls;
	}

	protected void unbindLog(LogService ls) {
		log = null;
	}
}
