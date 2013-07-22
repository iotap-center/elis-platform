/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;

/**
 * The Dimmable interface describes a dimmable power switch.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface Dimmable extends PowerSwitch {
	
	/**
	 * 
	 * 
	 * @param level
	 * @throws ActuatorFailedException
	 */
	void setDimLevel(int level) throws ActuatorFailedException;
}
