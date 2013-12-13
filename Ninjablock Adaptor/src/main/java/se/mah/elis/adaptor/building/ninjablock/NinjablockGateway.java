package se.mah.elis.adaptor.building.ninjablock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.exceptions.GatewayCommunicationException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.building.ninjablock.beans.GatewayBean;
import se.mah.elis.adaptor.building.ninjablock.communication.BlockCommunicator;
import se.mah.elis.adaptor.building.ninjablock.communication.Communicator;


public class NinjablockGateway implements Gateway{
	
	private List<Device> devices;
	private String name;
	private int gatewayId;
	private GatewayAddress gatewayAddress;
	private GatewayUser gatewayUser;
	private String response;
	private GatewayBean gatewayBean;
	private boolean gatewayHasConnected;


	public NinjablockGateway() {
		this.devices = new ArrayList<Device>();
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

	@Override
	public void connect() throws GatewayCommunicationException {
		
		try {
			addGatewayData();
//			getAllDevices();
			markAsConnected();
		} catch (Exception e) {
			throw new GatewayCommunicationException();
		}
	}

	private void markAsConnected() {
		gatewayHasConnected = true;
	}

	private void getAllDevices() {
		// TODO Get all devices in gateway and set NinjablockGateway as there gateway.	
	}

	private void addGatewayData() {
		
		Communicator com = new Communicator();
		BlockCommunicator blockCom = new BlockCommunicator(com);
		try {
			response = blockCom.getBlocks();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();
		gatewayBean = gson.fromJson(response, GatewayBean.class);
		
		try {
			setName(gatewayBean.getGatewayDataBean().getGatewayDetails().getShort_name());
			System.out.println(gatewayBean.getGatewayDataBean().getGatewayDetails().getShort_name());
		} catch (StaticEntityException e) {
			e.printStackTrace();
		}

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
