/**
 * 
 */
package se.mah.elis.adaptor.device.api.entities.devices;

import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;

/**
 * The Dimmable interface describes a dimmable actuator.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Dimmer extends Actuator {
	
	/**
	 * Sets the desired dim level of the device. The desired level is given as
	 * a float ranging between 0 and 1, where 0 is off and 1 is the highest
	 * possible value.
	 * 
	 * @param level The desired dim level.
	 * @throws ActuatorFailedException if the level couldn't be set.
	 * @since 1.0
	 */
	void setDimLevel(float level) throws ActuatorFailedException;
	
	/**
	 * Use this method to get the current dim level of the device. The level
	 * is given as a float ranging between 0 and 1, where 0 is off and 1 is the
	 * highest possible value.
	 * 
	 * @return The current dim level.
	 * @throws ActuatorFailedException if the level couldn't be read.
	 * @since 1.0
	 */
	float getDimLevel() throws ActuatorFailedException;
}
