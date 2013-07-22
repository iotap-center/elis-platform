/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;

/**
 * The Sensor interface describe a device with actuator capabilities. The
 * interface on its own doesn't bring much to the table, but rather serves as a
 * lowest common denominator for all other actuator types, making it suitable
 * for filtering, grouping and such.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface Actuator extends Device {
}
