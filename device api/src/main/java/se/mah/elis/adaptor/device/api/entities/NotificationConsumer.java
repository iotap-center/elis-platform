/**
 * 
 */
package se.mah.elis.adaptor.device.api.entities;

import se.mah.elis.adaptor.device.api.events.SensorEvent;

/**
 * The NotificationConsumer interface is used by services that wish to
 * subscribe to notifications or alarms originating from various sensors.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface NotificationConsumer {
	
	/**
	 * Consumes a triggered event.
	 * 
	 * @param event The event to be consumed.
	 * @since 1.0
	 */
	void triggerEvent(SensorEvent event);
}
