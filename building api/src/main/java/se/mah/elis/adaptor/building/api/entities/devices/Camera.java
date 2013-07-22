/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.data.Image;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;

/**
 * This interface describes a camera object.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface Camera extends Sensor {
	
	/**
	 * This method is used to get an image from a camera sensor.
	 * 
	 * @return The image
	 * @throws SensorFailedException if the image couldn't be fetched.
	 */
	Image getImage() throws SensorFailedException;
}
