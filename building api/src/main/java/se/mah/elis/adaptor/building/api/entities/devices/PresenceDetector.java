/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.UnsupportedFunctionalityException;

/**
 * The PresenceDetector interface describes a sensor that can detect presence
 * in its immediate surroundings. It is somewhat similar to the
 * {@link Tripwire} interface.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 * @see Tripwire
 */
public interface PresenceDetector extends Detector {

	/**
	 * This method is used to determine whether there is someone within the
	 * detector's field of presence or not.
	 * 
	 * @return True if someone is within the range o the sensor.
	 * @throws SensorFailedException if the presence detection failed.
	 * @throws UnsupportedFunctionalityException if the sensor doesn't provide
	 * 		   this functionality
	 * @since 1.0
	 */
	boolean anyoneThere() throws SensorFailedException,
								 UnsupportedFunctionalityException;
}
