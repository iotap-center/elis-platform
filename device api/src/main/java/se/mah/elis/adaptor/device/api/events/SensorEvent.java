package se.mah.elis.adaptor.device.api.events;

import java.io.Serializable;
import java.util.Date;

import se.mah.elis.adaptor.device.api.entities.NotificationConsumer;
import se.mah.elis.adaptor.device.api.entities.devices.Sensor;

/**
 * The SensorEvent class is a baseline class for events triggered by various
 * sensors. SensorEvents are consumed by
 * {@link se.mah.elis.adaptor.device.api.entities.NotificationConsumer}
 * objects.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 * @see NotificationConsumer
 */
public class SensorEvent implements Serializable {
	
	/**
	 * A reference to the sensor object that triggered this event.
	 * 
	 * @since 1.0
	 * @serial
	 */
	protected Sensor sensor;
	
	/**
	 * An optional message to be sent with the event.
	 * 
	 * @since 1.0
	 * @serial
	 */
	protected String message;
	
	/**
	 * A timestamp for keeping track of when this event was triggered.
	 * 
	 * @since 1.0
	 * @serial
	 */
	protected final Date triggered = new Date();

	/**
	 * Initializes a SensorEvent with a message.
	 * 
	 * @param sensor The triggering sensor.
	 * @param message A message to be passed on to the consumer.
	 * @since 1.0
	 */
	public SensorEvent(Sensor sensor, String message) {
		this.sensor = sensor;
		this.message = message;
	}
	
	/**
	 * Initializes a SensorEvent without a message.
	 * 
	 * @param sensor The triggering sensor.
	 * @since 1.0
	 */
	public SensorEvent(Sensor sensor) {
		this.sensor = sensor;
		this.message = "";
	}
	
	/**
	 * Gets the triggering detector.
	 * 
	 * @return The detector that triggered the event.
	 * @since 1.0
	 */
	public Sensor getOrigin() {
		return sensor;
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
