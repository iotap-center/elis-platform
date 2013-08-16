package se.mah.elis.adaptor.utilityprovider.eon.internal;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.auxiliaries.data.ElectricitySample;

public class EonElectricitySampler implements ElectricitySampler {

	private Gateway gateway;
	private DeviceIdentifier id; 
	
	public DeviceIdentifier getId() {
		return id;
	}

	public void setId(DeviceIdentifier id) throws StaticEntityException {
		this.id = id;
	}
	
	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	public String getName() {
		return null;
	}

	public void setName(String name) throws StaticEntityException {
		// TODO Auto-generated method stub

	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDescription(String description) throws StaticEntityException {
		// TODO Auto-generated method stub

	}

	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}

	public ElectricitySample getSample() throws SensorFailedException {
		return new EonElectricitySample(getGateway(), getId());
	}

	public ElectricitySample sample(int millis) throws SensorFailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceSet[] getDeviceSets() {
		// TODO Auto-generated method stub
		return null;
	}

}
