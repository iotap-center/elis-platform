package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import javax.ws.rs.client.ResponseProcessingException;

import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonActionObject;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.auxiliaries.data.ElectricitySample;

/**
 * A virtual representation of the E.On power switch
 * 
 * @TODO implemenation for sampling still missing
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class EonPowerSwitchMeter implements PowerSwitch, ElectricitySampler {

	private boolean isOnline;
	private EonHttpBridge httpBridge;
	private EonGateway gateway;
	private DeviceIdentifier deviceId;
	private String deviceName;

	/**
	 * Used to set initial device status when instantiating the device
	 * 
	 * @param online
	 */
	public void setOnline(boolean online) {
		isOnline = online;
	}

	/**
	 * Attach a HTTP bridge to the device - mock bridges can be used during
	 * testing.
	 * 
	 * @param bridge
	 */
	public void setHttpBridge(EonHttpBridge bridge) {
		httpBridge = bridge;
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
		// TODO Auto-generated method stub
		return null;
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
			if (trySwitchPss())
				setOnline(true);
		}
	}


	/**
	 * Turns off the device
	 */
	@Override
	public void turnOff() throws ActuatorFailedException {
		if (isOnline()) {
			if (trySwitchPss())
				setOnline(false);
		}
	}

	private boolean trySwitchPss() throws ActuatorFailedException {
		boolean success = false;
		
		try {
			EonActionObject longRunningTask = 
					httpBridge.switchPSS(this.gateway.getAuthenticationToken(),
					getGateway().getAddress().toString(), getId().toString());
			success = waitForSuccess(longRunningTask);
		} catch (ResponseProcessingException | ParseException e) {
			throw new ActuatorFailedException();
		}
		
		return success;
	}

	private boolean waitForSuccess(EonActionObject longRunningTask) {
		// TODO: Implement
		return true;
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

}
