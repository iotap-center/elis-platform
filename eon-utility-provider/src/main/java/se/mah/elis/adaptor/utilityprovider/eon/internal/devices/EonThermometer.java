package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.Properties;
import java.util.UUID;

import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.Thermometer;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.data.TemperatureData;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * A virtual representation of the E.On Thermometer
 * 
 * @author Joakim Lithell
 * @version 1.0.0
 * @since 1.0
 *
 */

public class EonThermometer extends EonDevice implements Thermometer {

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

	@Override
	public long getDataId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUUID(UUID uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUniqueUserId(int userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getUniqueUserId() {
		// TODO Auto-generated method stub
		return 0;
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

}
