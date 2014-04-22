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
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;

public class FooGateway implements Gateway {
	private static final long serialVersionUID = -8937996755742476107L;
	private String name;
	private GatewayAddress gatewayAddress;
	private GatewayUser gatewayUser;
	private int gatewayId;
	private UUID dataid;
	private UUID userId;
	private DateTime created = DateTime.now();
	private FooPowerMeter meter;

	public FooGateway() {
		meter = null;
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
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		return o instanceof FooPowerMeter;
	}

	@Override
	public Iterator<Device> iterator() {
		return new FooIterator(this);
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[1];
		array[0] = meter;
		
		return array;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return null;
	}

	@Override
	public boolean add(Device e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Device> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {
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
		this.userId = userId;
		meter = new FooPowerMeter(this, userId);
	}

	@Override
	public UUID getOwnerId() {
		return this.userId;
	}

	@Override
	public Properties getProperties() {
		OrderedProperties props = new OrderedProperties();
		props.put("dataid", this.dataid);
		props.put("ownerid", this.userId);
		props.put("gatewayId", this.gatewayId);
		props.put("gatewayName", this.name);
		props.put("created", created);
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("dataid", this.dataid);
		props.put("ownerid", this.userId);
		props.put("gatewayId", this.gatewayId);
		props.put("gatewayName", "64");
		props.put("created", created);
		return props;
	}

	@Override
	public void populate(Properties props) {
		setDataId((UUID) props.get("dataid"));
		setOwnerId((UUID) props.get("ownerid"));
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
	
	public FooPowerMeter getMeter() {
		return meter;
	}

	private void log(String message) {
		log(LogService.LOG_INFO, message);
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
