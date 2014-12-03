/**
 * 
 */
package se.mah.elis.adaptor.device.api.entities.devices;

import java.util.Properties;

import se.mah.elis.adaptor.device.api.data.Image;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * This interface describes a camera object.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Camera extends Sensor {
	
	/**
	 * This method is used to fetch an image from a camera sensor.
	 * 
	 * @return The image
	 * @throws SensorFailedException if the image couldn't be fetched.
	 * @since 1.0
	 */
	Image getSnapshot() throws SensorFailedException;
	
	/**
	 * This method is used to fetch all available images from a camera device.
	 * 
	 * @return An array of Image objects. If no images are available, the
	 * 		   method will return a null value.
	 * @throws SensorFailedException
	 * @since 1.0
	 */
	Image[] getAllImages() throws SensorFailedException;
	
	/**
	 * Flushes all images from the camera's memory.
	 * 
	 * @throws StaticEntityException if the images couldn't be removed.
	 * @since 1.0
	 */
	void flushImages() throws StaticEntityException;
	
	/**
	 * Removes the given image from the camera's image bank.
	 * 
	 * @param image The Image to be removed.
	 * @throws StaticEntityException if the image couldn't be removed.
	 * @since 1.0
	 */
	void removeImage(Image image) throws StaticEntityException;
	
	/**
	 * Sets the camera properties.
	 * 
	 * @param props A Properties object with all desired properties.
	 * @throws StaticEntityException if the properties couldn't be applied.
	 * @since 1.0
	 */
	void setCameraProperties(Properties props) throws StaticEntityException;
}
