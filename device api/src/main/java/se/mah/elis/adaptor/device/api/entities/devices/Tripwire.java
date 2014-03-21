/**
 * 
 */
package se.mah.elis.adaptor.device.api.entities.devices;

import se.mah.elis.exceptions.UnsupportedFunctionalityException;


/**
 * The Tripwire interface describes a sensor that will trigger when activated
 * by a certain stimulus. It can be realised as a magnetic sensor, a PIR
 * sensor, etc.
 * 
 * @author "Johan Holmberg, Malm�� University"
 * @since 1.0
 */
public interface Tripwire extends Detector {
	
	/**
	 * This method is used to see whether the sensor has been triggered. It is
	 * used by systems that must use polling as the main method of
	 * communication.
	 * 
	 * @return True if the tripwire has been triggered, otherwise false.
	 * @throws UnsupportedFunctionalityException if the sensor doesn't provide
	 * 		   this functionality
	 * @since 1.0
	 */
	boolean isTriggered() throws UnsupportedFunctionalityException;
	
	/**
	 * This method is used to put the sensor back in untriggered mode.
	 * 
	 * @since 1.0
	 */
	void release();
}