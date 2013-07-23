/**
 * 
 */
package se.mah.elis.adaptor.building.api.events;

import java.util.Date;

import se.mah.elis.adaptor.building.api.entities.NotificationConsumer;
import se.mah.elis.adaptor.building.api.entities.devices.Detector;

/**
 * The DetectorEvent interface is a baseline interface for events triggered by
 * various detectors. DetectorEvents are consumed by NotificationConsumer
 * objects.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 * @see NotificationConsumer
 */
public class DetectorEvent {
	
	private Detector detector;
	private String message;
	private static final Date triggered = new Date();

	/**
	 * Creates a DetectorEvent object with a message.
	 * 
	 * @param detector The triggering detector.
	 * @param message A message to be passed on to the consumer.
	 * @since 1.0
	 */
	public DetectorEvent(Detector detector, String message) {
		this.detector = detector;
		this.message = message;
	}
	
	/**
	 * Creates a DetectorEvent object without a message.
	 * 
	 * @param detector The triggering detector.
	 * @since 1.0
	 */
	public DetectorEvent(Detector detector) {
		this.detector = detector;
		this.message = "";
	}
	
	/**
	 * Gets the triggering detector.
	 * 
	 * @return The detector that triggered the event.
	 * @since 1.0
	 */
	public Detector getOrigin() {
		return detector;
	}
	
	/**
	 * Gets the status message from the event.
	 * 
	 * @return The message
	 * @since 1.0
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Gets the time when the event was triggered.
	 * 
	 * @return The time when the event was triggered.
	 * @since 1.0
	 */
	public Date getTriggerTime() {
		return triggered;
	}
}
