package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.Actuator;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.entities.devices.Thermometer;
import se.mah.elis.adaptor.building.api.entities.devices.Thermostat;
import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonActionObject;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.auxiliaries.data.TemperatureData;

/**
 * A virtual representation of the E.On Thermostat
 * 
 * @author Joakim Lithell
 * @version 1.0.0
 * @since 1.0
 */

public class EonThermostat extends EonDevice implements Actuator,Thermostat{

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
		this.gateway = (EonGateway) gateway;	}

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
	public void setDesiredTemperature(TemperatureData goal)
			throws ActuatorFailedException {
		
		float goalTemp = goal.getCelsius();

		try {
			httpBridge.setDesiredTemperature(this.gateway.getAuthenticationToken(), getGatewayAddress(), getId().toString(), goalTemp);
		} catch (ParseException e) {
			throw new ActuatorFailedException();
		}
				
	}

	@Override
	public TemperatureData getCurrentTemperature() throws SensorFailedException {
		float currentTemperature = 0;

		try {
			currentTemperature = httpBridge.getTemperature(
					this.gateway.getAuthenticationToken(), getGatewayAddress(),
					getId().toString());
		} catch (ParseException e) {
			throw new SensorFailedException();
		}

		TemperatureData temperatureData = new TemperatureDataImpl(
				currentTemperature);

		return temperatureData;
	}

	private String getGatewayAddress() {
		return getGateway().getAddress().toString();
	}

}
