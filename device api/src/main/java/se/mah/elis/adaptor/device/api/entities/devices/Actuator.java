/**
 * 
 */
package se.mah.elis.adaptor.device.api.entities.devices;

/**
 * The Sensor interface describe a device with actuator capabilities. The
 * interface on its own doesn't bring much to the table, but rather serves as a
 * lowest common denominator for all other actuator types, making it suitable
 * for filtering, grouping and such.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Actuator extends Device {
}
