/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;

/**
 * The PowerSwitch interface describes an actuator with power switching
 * abilities. A device using this interface can turn itself on and off. It also
 * comes equipped with toggling functionality.
 * 
 * @author "Johan Holmberg, Malmö University"
 *
 */
public interface PowerSwitch extends Actuator {
	
	/**
	 * This method is used to put the actuator in <i>on mode</i>.
	 * 
	 * @throws ActuatorFailedException if the action couldn't be executed.
	 */
	void turnOn() throws ActuatorFailedException;
	
	/**
	 * This method is used to put the actuator in <i>off mode</i>.
	 * 
	 * @throws ActuatorFailedException if the action couldn't be executed.
	 */
	void turnOff() throws ActuatorFailedException;
	
	/**
	 * Toggles the <i>on/off mode</i> of the actuator, i.e. if the current
	 * mode is <i>off</i>, the mode will switch to <i>on</i> and vice versa.
	 * 
	 * @throws ActuatorFailedException if the action couldn't be executed.
	 */
	void toggle() throws ActuatorFailedException;

}
