package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.devices.Actuator;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.Thermostat;
import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.data.TemperatureData;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * A virtual representation of the E.On Thermostat
 * 
 * @author Joakim Lithell
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */

public class EonThermostat extends EonDevice implements Actuator,Thermostat {

	private static final long serialVersionUID = -4835713744024929647L;
	private EonGateway gateway;
	private DeviceIdentifier deviceId;
	private String deviceName;
	private String description;
	private boolean isOnline;
	private UUID dataid;
	private UUID ownerid;
	private DateTime created = DateTime.now();
	
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
		return description;
	}

	@Override
	public void setDescription(String description) throws StaticEntityException {
		this.description = description;
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

	@Override
	public Properties getProperties() {
		OrderedProperties props = new OrderedProperties();
		props.put("dataid", dataid);
		props.put("ownerid", ownerid);
		props.put("created", created);
		props.putAll((new EonDeviceIdentifier("")).getPropertiesTemplate());
		props.put("name", deviceName);
		props.put("description", description);
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("created", created);
		props.putAll((new EonDeviceIdentifier("")).getPropertiesTemplate());
		props.put("name", "64");
		props.put("description", "256");
		return props;
	}

	@Override
	public void populate(Properties props) {
		dataid = (UUID) props.get("dataid");
		ownerid = (UUID) props.get("ownerid");
		created = (DateTime) props.get("created");
		deviceName = (String) props.get("name");
		description = (String) props.get("description");

		deviceId = new EonDeviceIdentifier("");
		deviceId.populate(props);
		
		// TODO Create gateway
	}

	@Override
	public UUID getDataId() {
		return dataid;
	}

	@Override
	public void setDataId(UUID uuid) {
		dataid = uuid;
	}

	@Override
	public void setOwnerId(UUID userId) {
		ownerid = userId;
	}

	@Override
	public UUID getOwnerId() {
		return ownerid;
	}

	@Override
	public DateTime created() {
		return created;
	}

}
