package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.client.ResponseProcessingException;

import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonActionObject;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonActionStatus;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * A virtual representation of the E.On power switch
 * 
 * @TODO implemenation for sampling still missing
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class EonPowerSwitchMeter extends EonDevice implements PowerSwitch, ElectricitySampler {

	private static final long serialVersionUID = 7361404151615176359L;
	private static final int FAIL_COUNT = 3;
	private boolean isOnline;
	private EonGateway gateway;
	private DeviceIdentifier deviceId;
	private String deviceName;
	private UUID uuid;

	/**
	 * Used to set initial device status when instantiating the device
	 * 
	 * @param online
	 */
	public void setOnline(boolean online) {
		isOnline = online;
	}
	
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

	/**
	 * Returns a reference to the gateway
	 */
	@Override
	public Gateway getGateway() {
		return gateway;
	}

	/**
	 * Set which E.On gateway this device belongs to
	 * 
	 * @param gateway
	 */
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
	/**
	 * Tells whether the power switch is turned on or off
	 */
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Turns on the device
	 */
	@Override
	public void turnOn() throws ActuatorFailedException {
		if (!isOnline()) {
			if (tryTurnOn())
				setOnline(true);
		}
	}


	/**
	 * Turns off the device
	 */
	@Override
	public void turnOff() throws ActuatorFailedException {
		if (isOnline()) {
			if (tryTurnOff())
				setOnline(false);
		}
	}

	private boolean tryTurnOff() throws ActuatorFailedException {
		boolean success = false;
		try {
			EonActionObject longRunningTask = 
					httpBridge.turnOff(this.gateway.getAuthenticationToken(),
							getGatewayAddress(), getId().toString());
			success = waitForSuccess(longRunningTask);
		} catch (ResponseProcessingException | ParseException e) {
			throw new ActuatorFailedException();
		}
		
		return success;
	}
	
	private boolean tryTurnOn() throws ActuatorFailedException {
		boolean success = false;
		
		try {
			EonActionObject longRunningTask = 
					httpBridge.turnOn(this.gateway.getAuthenticationToken(),
							getGatewayAddress(), getId().toString());
			success = waitForSuccess(longRunningTask);
		} catch (ResponseProcessingException | ParseException e) {
			throw new ActuatorFailedException();
		}
		
		return success;
	}

	private boolean waitForSuccess(EonActionObject longRunningTask) throws ParseException {
		return waitForSuccess(longRunningTask, 0);
	}
	
	private boolean waitForSuccess(EonActionObject longRunningTask, int testCounter) throws ParseException {
		if (testCounter > FAIL_COUNT)
			return false;
			
		EonActionObject report = 
				httpBridge.getActionObject(this.gateway.getAuthenticationToken(), 
				getGatewayAddress(), longRunningTask.getId());
			
		if (report.getStatus() == EonActionStatus.ACTION_SUCCESS) 
			return true;
		else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ignore) { }			
			return waitForSuccess(longRunningTask, testCounter+1);
		}
	}

	private String getGatewayAddress() {
		return getGateway().getAddress().toString();
	}
	
	/**
	 * Toggle the device on and off
	 */
	@Override
	public void toggle() throws ActuatorFailedException {
		if (isOnline())
			turnOff();
		else
			turnOn();
	}

	@Override
	public long getDataId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void setUniqueUserId(int userId) {
		
	}

	@Override
	public int getUniqueUserId() {
		return 0;
	}

	@Override
	public Properties getProperties() {
		return getPropertiesTemplate();
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("uuid", this.uuid.toString());
		props.put("name", this.deviceName);
		return props;
	}

	@Override
	public void populate(Properties props) {
		this.uuid = UUID.fromString((String) props.get("uuid"));
		this.deviceName = (String) props.get("name");
	}

}
