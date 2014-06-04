package se.mah.elis.adaptor.energy.eon.internal.devices;

import javax.ws.rs.client.ResponseProcessingException;

import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.energy.eon.internal.EonActionObject;
import se.mah.elis.adaptor.energy.eon.internal.EonActionStatus;

/**
 * A virtual representation of the E.On power switch
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class EonDinPowerSwitchMeter extends EonPowerMeter
		implements PowerSwitch, ElectricitySampler {

	private static final long serialVersionUID = 2179934112904287252L;
	private static final int FAIL_COUNT = 3;

	/**
	 * Used to set initial device status when instantiating the device
	 * 
	 * @param online
	 */
	public void setOnline(boolean online) {
		isOnline = online;
	}

	/**
	 * Turns on the device
	 * @since 1.0
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
	 * @since 1.0 
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

	/**
	 * Toggle the device on and off
	 * @since 1.0
	 */
	@Override
	public void toggle() throws ActuatorFailedException {
		if (isOnline())
			turnOff();
		else
			turnOn();
	}

}
