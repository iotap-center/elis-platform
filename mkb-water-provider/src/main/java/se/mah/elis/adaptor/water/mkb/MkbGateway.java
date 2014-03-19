package se.mah.elis.adaptor.water.mkb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.adaptor.device.api.data.GatewayAddress;
import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.GatewayCommunicationException;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * MKB gateway to connect with water meters.
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
public class MkbGateway implements Gateway {

	private static final long serialVersionUID = 7109328443818562681L;
	private List<Device> devices;
	private String gatewayName;
	private UUID uuid;
	private GatewayUser gatewayUser;
	private boolean hasConnected;
	private boolean isOnline;
	private UUID userOwner;
	
	
	public MkbGateway() {
		devices = new ArrayList<>();
	}

	@Override
	public int getId() {
		// not used
		return 0;
	}

	@Override
	public void setId(int id) throws StaticEntityException {
		// not used
	}

	@Override
	public String getName() {
		return gatewayName;
	}

	@Override
	public void setName(String name) throws StaticEntityException {
		gatewayName = name;
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
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populate(Properties props) {
		// TODO Auto-generated method stub

	}

	@Override
	public GatewayAddress getAddress() {
		// not used
		return null;
	}

	@Override
	public void setAddress(GatewayAddress address) {
		// not used
	}

	@Override
	public GatewayUser getUser() {
		return gatewayUser;
	}

	@Override
	public void setUser(GatewayUser user) {
		this.gatewayUser = user;
	}

	@Override
	public void connect() throws GatewayCommunicationException {
		hasConnected = true;
		isOnline = true;
	}

	@Override
	public boolean hasConnected() {
		return hasConnected;
	}

	@Override
	public boolean isOnline() {
		return isOnline;
	}

	@Override
	public UUID getDataId() {
		return uuid;
	}

	@Override
	public void setDataId(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void setOwnerId(UUID userId) {
		this.userOwner = userId;
	}

	@Override
	public UUID getOwnerId() {
		return userOwner;
	}

	@Override
	public DateTime created() {
		return null;
	}

}
