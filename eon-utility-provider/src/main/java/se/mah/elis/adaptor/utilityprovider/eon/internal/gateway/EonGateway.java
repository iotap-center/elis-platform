package se.mah.elis.adaptor.utilityprovider.eon.internal.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.client.ResponseProcessingException;

import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.device.api.data.GatewayAddress;
import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.GatewayCommunicationException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonDevice;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * Implementation of an E.On panel (in Elis terms: gateway)
 * 
 * An instance of gateway must have a {@link se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge} to function properly. 
 * Most likely you also want to populate the gateway with data from the E.On service. 
 * This is done using {@link EonGateway#connect()}. 
 * 
 * The recommended way to retrieve a new gateway instance is to use 
 * {@link EonGatewayUserFactory#getUser(String, String)} and retrieve the gateway instance
 * via the {@link EonGatewayUser} object. 
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class EonGateway implements Gateway {

	private static final long serialVersionUID = 4987546440772318353L;
	private String authenticationToken;
	private EonHttpBridge httpBridge;
	private List<Device> devices;
	private String name;
	private GatewayAddress gatewayAddress;
	private GatewayUser gatewayUser;
	private boolean gatewayHasConnected;
	private int gatewayId;
	private UUID uuid;
	private int uniqueUserId;

	public EonGateway() {
		this.devices = new ArrayList<Device>();
		this.gatewayHasConnected = false;
	}

	/**
	 * Get the active E.On authentication token
	 * 
	 * @return
	 */
	public String getAuthenticationToken() {
		return authenticationToken;
	}

	/**
	 * Set active E.On authentication token
	 * 
	 * @param authenticationToken
	 */
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public EonHttpBridge getHttpBridge() {
		return httpBridge;
	}

	public void setHttpBridge(EonHttpBridge httpBridge) {
		this.httpBridge = httpBridge;
	}

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
		try {
			addGatewayData();
			getAllDevices();
			markAsConnected();
		} catch (Exception e) {
			throw new GatewayCommunicationException();
		}
	}

	private void addGatewayData() throws ResponseProcessingException,
			ParseException, StaticEntityException {
		Map<String, Object> data = httpBridge
				.getGateway(getAuthenticationToken());
		setName((String) data.get("Name"));
		setId(Integer.parseInt((String) data.get("EwpPanelId")));
	}

	private void getAllDevices() throws ResponseProcessingException,
			ParseException {
		
		List<Device> devices = httpBridge.getDevices(
				getAuthenticationToken(), getAddress().toString());
		
		for (Device device : devices) {
			try {
				setEonProperties(device);
				this.add(device);
			} catch (StaticEntityException e) {
				e.printStackTrace();
				continue; // TODO: add logging
			}
		}
	}

	private void setEonProperties(Device device) throws StaticEntityException {
		device.setGateway(this);
		if (device instanceof EonDevice) {
			EonDevice eonDevice = (EonDevice) device;
			eonDevice.setHttpBridge(getHttpBridge());
		}
		
	}

	private void markAsConnected() {
		this.gatewayHasConnected = true;
	}

	@Override
	public boolean hasConnected() {
		return gatewayHasConnected;
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getDataId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void setUniqueUserId(int userId) {
		this.uniqueUserId = userId;
	}

	@Override
	public int getUniqueUserId() {
		return this.uniqueUserId;
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		
		props.put("uuid", this.uuid);
		props.put("gatewayId", this.gatewayId);
		props.put("gatewayName", this.name);
		props.put("uniqueUserId", this.uniqueUserId);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();

		props.put("uuid", "256");
		props.put("gatewayName", "256");
		props.put("gatewayId", new Integer(0));
		props.put("uniqueUserId", new Integer(0));
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		setUUID(UUID.fromString((String) props.get("uuid")));
		setUniqueUserId((Integer) props.get("uniqueUserId"));
		gatewayId = (Integer) props.get("gatewayId");
		name = (String) props.get("gatewayName");
	}
}
