package se.mah.elis.demo.eon.driver.internal;

import se.mah.elis.adaptor.building.api.data.ElectricitySample;
import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;

public class EonDevice implements ElectricitySampler {

	private DeviceIdentifier id; 
	
	public String getDescription() {
		return "This device is a dummy and cannot be modified.";
	}

	public DeviceIdentifier getId() {
		return this.id;
	}

	public String getName() {
		return "E.On Dummy Driver Device";
	}

	public boolean isOnline() {
		return true;
	}

	public void setDescription(String arg0) throws StaticEntityException {
		// TODO Auto-generated method stub
	}

	public void setName(String arg0) throws StaticEntityException {
		// TODO Auto-generated method stub
	}

	public ElectricitySample getSample() throws SensorFailedException {
		ElectricitySample sample = new EonElectricitySample();
		return sample;
	}

	public ElectricitySample sample(int arg0) throws SensorFailedException {
		ElectricitySample sample = new EonElectricitySample();
		return null;
	}

	public void setId(DeviceIdentifier arg0) throws StaticEntityException {
		this.id = arg0;
	}
	
}
