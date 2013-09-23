package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.Map;

import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.auxiliaries.data.ElectricitySample;
import se.mah.elis.auxiliaries.data.TemperatureData;

/**
 * A virtual representation of the E.On power meter
 * 
 * @author Joakim Lithell
 * @version 1.0.0
 * @since 1.0
 */

public class EonPowerMeter extends EonDevice implements ElectricitySampler {

	private boolean isOnline;
	private EonGateway gateway;
	private DeviceIdentifier deviceId;
	private String deviceName;
	
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
	public ElectricitySample getSample() throws SensorFailedException {
		double value;
		
		try {
			value = httpBridge.getPowerMeterKWh(this.gateway.getAuthenticationToken(), getGatewayAddress(),
					getId().toString());
		} catch (ParseException e) {
			throw new SensorFailedException();
		}
		
		ElectricitySample electricitySample = null;
		electricitySample = new ElectricitySampleImpl(value);

		return electricitySample;
	}

	@Override
	public ElectricitySample sample(int millis) throws SensorFailedException {
		// TODO Sample the current energy usage for a given amount of time.
		return null;
	}
	
	private String getGatewayAddress() {
		return getGateway().getAddress().toString();
	}

}
