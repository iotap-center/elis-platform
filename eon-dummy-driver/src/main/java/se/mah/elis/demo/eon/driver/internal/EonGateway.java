package se.mah.elis.demo.eon.driver.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.exceptions.GatewayCommunicationException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;

public class EonGateway implements Gateway {

	private int id;
	private List<Device> devices;
	private GatewayUser gatewayUser; 
	
	public EonGateway() {
		this.devices = new ArrayList<Device>();
	}
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return "eon-gateway";
	}

	public void setId(int arg0) throws StaticEntityException {
		this.id = arg0;
	}

	public void setName(String arg0) throws StaticEntityException {
		// not implemented
	}

	public int size() {
		return this.devices.size();
	}

	public boolean isEmpty() {
		return this.devices.isEmpty();
	}

	public boolean contains(Object o) {
		return this.devices.contains(o);
	}

	public Iterator<Device> iterator() {
		return this.devices.iterator();
	}

	public Object[] toArray() {
		return this.devices.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return this.devices.toArray(a);
	}

	public boolean add(Device e) {
		return this.devices.add(e);
	}

	public boolean remove(Object o) {
		return this.devices.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return this.devices.containsAll(c);
	}

	public boolean addAll(Collection<? extends Device> c) {
		return this.devices.addAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return this.devices.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return this.devices.retainAll(c);
	}

	public void clear() {	
	}

	public void connect() throws GatewayCommunicationException {
		// don't know what to put here
	}

	public GatewayAddress getAddress() {
		return new EonGatewayAddress();
	}

	public GatewayUser getUser() {
		return this.gatewayUser;
	}

	public boolean isOnline() {
		return true;
	}

	public void setAddress(GatewayAddress arg0) {
		// mute
	}

	public void setUser(GatewayUser arg0) {
		this.gatewayUser = arg0;
	}

}
