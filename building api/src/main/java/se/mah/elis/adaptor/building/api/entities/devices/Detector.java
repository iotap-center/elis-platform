/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.entities.NotificationConsumer;
import se.mah.elis.adaptor.building.api.exceptions.UnsupportedFunctionalityException;

/**
 * A detector is a sensor that can trigger alarms and notifications. It is used
 * by {@link se.mah.elis.adaptor.building.api.entities.NotificationConsumer} to
 * collect data in semi-real time.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 * @see NotificationConsumer
 */
public interface Detector extends Sensor {

	
	/**
	 * This method is used to connect the sensor to a notification provider.
	 * This is the method used by systems that rely on the sensors to provide
	 * information in semi-real time.
	 * 
	 * @param consumer The notification provider that will consume the sensor's
	 * 		  notifications.
	 * @throws UnsupportedFunctionalityException if the sensor doesn't provide
	 * 		   this functionality
	 * @since 1.0
	 */
	void setNotifier(NotificationConsumer consumer)
			throws UnsupportedFunctionalityException;
}
