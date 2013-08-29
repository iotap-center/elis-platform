package se.mah.elis.adaptor.utilityprovider.eon.internal.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.ResponseProcessingException;

import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.exceptions.GatewayCommunicationException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.building.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonDeviceIdentifier;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonPowerSwitchMeter;

public class EonGateway implements Gateway {

	private String authenticationToken;
	private EonHttpBridge httpBridge;
	private List<Device> devices;
	private String name;
	private GatewayAddress gatewayAddress;
	private GatewayUser gatewayUser;
	private boolean gatewayHasConnected;

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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(int id) throws StaticEntityException {
		// TODO Auto-generated method stub

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
		
		List<Map<String, Object>> data = httpBridge.getDevices(
				getAuthenticationToken(), getAddress().toString());
		
		for (Map<String, Object> deviceMap : data) {
			try {
				Device device = convertToDevice(deviceMap);
				this.add(device);
			} catch (MethodNotSupportedException e) {
				e.printStackTrace();
				continue; // TODO: add logging
			} catch (StaticEntityException e) {
				e.printStackTrace();
				continue; // TODO: add logging
			}
		}
	}

	private Device convertToDevice(Map<String, Object> deviceMap)
			throws MethodNotSupportedException, StaticEntityException {
		
		if ((boolean) deviceMap.get("IsPowerSwitch")) {
			return createPowerSwitch(deviceMap);
		} else {
			throw new MethodNotSupportedException();
		}
	}

	private Device createPowerSwitch(Map<String, Object> deviceMap) throws StaticEntityException {
		EonPowerSwitchMeter psm = new EonPowerSwitchMeter();
		String id = (String) deviceMap.get("Id");
		
		psm.setGateway(this);
		psm.setHttpBridge(getHttpBridge());
		psm.setId(new EonDeviceIdentifier(id));
		psm.setName((String) deviceMap.get("Name"));
		
		return psm;
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

}
