package se.mah.elis.adaptor.utilityprovider.eon.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.AuthenticationException;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.exceptions.GatewayCommunicationException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.EonApiActor;

public class EonGateway implements Gateway {

	private final String DEMO_USER = "eon2hem@gmail.com";
	private final String DEMO_PASS = "02DCBD";
	
	private long gatewayId;
	private List<Device> devices;
	private EonApiActor apiBridge; 

	public EonGateway() {
		this.devices = new ArrayList<Device>();
		this.apiBridge = new EonApiActor();
	}
	
	public EonApiActor getApiBridge() {
		return apiBridge;
	}

	public void setApiBridge(EonApiActor apiBridge) {
		this.apiBridge = apiBridge;
	}

	public int getId() {
		return (int) gatewayId;
	}

	public void setId(int id) throws StaticEntityException {
		gatewayId = id;
	}

	public String getName() {
		return "eon2hem"; // FIXME
	}

	public void setName(String name) throws StaticEntityException {
		// TODO Auto-generated method stub
	}

	public int size() {
		return devices.size();
	}

	public boolean isEmpty() {
		return devices.isEmpty();
	}

	public boolean contains(Object o) {
		return devices.contains(o);
	}

	public Iterator<Device> iterator() {
		return devices.iterator();
	}

	public Object[] toArray() {
		return devices.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return devices.toArray(a);
	}

	public boolean add(Device e) {
		return devices.add(e);
	}

	public boolean remove(Object o) {
		return devices.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return devices.containsAll(c);
	}

	public boolean addAll(Collection<? extends Device> c) {
		return devices.addAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return devices.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return devices.retainAll(c);
	}

	public void clear() {
		devices.clear();
	}

	public GatewayAddress getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAddress(GatewayAddress address) {
		// TODO Auto-generated method stub
	}

	public GatewayUser getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setUser(GatewayUser user) {
		// TODO Auto-generated method stub
	}

	public void connect() throws GatewayCommunicationException {
		try {
			this.apiBridge.initialise(DEMO_USER, DEMO_PASS);
		} catch (AuthenticationException | IOException e) {
			throw new GatewayCommunicationException();
		}
		
		setGatewayId();
		retrieveGatewayDevices();
	}

	private void retrieveGatewayDevices() throws GatewayCommunicationException {
		try {
			for (String id : apiBridge.getDevices(gatewayId)) {
				EonElectricitySampler device = new EonElectricitySampler();
				// populate device
				DeviceIdentifier deviceIdentifer = new EonDeviceIdentifier(id); 
				device.setId(deviceIdentifer);
				device.setGateway(this);
				add(device);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GatewayCommunicationException();
		}
	}

	private void setGatewayId() throws GatewayCommunicationException {
		try {
			gatewayId = apiBridge.getPanels();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GatewayCommunicationException();
		}	
	}

	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}

}
