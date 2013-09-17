package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.entities.devices.Thermometer;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.auxiliaries.data.TemperatureData;

public class EonThermometer extends EonDevice implements Thermometer{

	//private static final TemperatureData tempData = new TemperatureDataImpl();
	private EonGateway gateway;
	private DeviceIdentifier deviceId;
	private String deviceName;
	private boolean isOnline;

	@Override
	public DeviceIdentifier getId() {
		return deviceId;
	}

	@Override
	public void setId(DeviceIdentifier id) throws StaticEntityException {
		deviceId = id;
	}

	@Override
	public String getName() {
		return deviceName;
	}

	@Override
	public void setName(String name) throws StaticEntityException {
		deviceName = name;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDescription(String description) throws StaticEntityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Gateway getGateway() {
		return gateway;
	}

	@Override
	public void setGateway(Gateway gateway) throws StaticEntityException {
		if (!(gateway instanceof EonGateway))
			throw new StaticEntityException();
		this.gateway = (EonGateway) gateway;
	}

	@Override
	public DeviceSet[] getDeviceSets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOnline() {
		return isOnline;
	}

	@Override
	public TemperatureData getCurrentTemperature() throws SensorFailedException {
		float currentTemperature = httpBridge.getTemperature(this.gateway.getAuthenticationToken(), getGatewayAddress(), getId().toString());
		TemperatureData temperatureData = new TemperatureDataImpl(currentTemperature);
		
		return temperatureData;
	}
	
	private String getGatewayAddress() {
		return getGateway().getAddress().toString();
	}
	
	

}
