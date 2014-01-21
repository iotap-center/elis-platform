/**
 * 
 */
package se.mah.elis.adaptor.building.api.events;

import se.mah.elis.adaptor.building.api.entities.devices.Detector;

/**
 * The DetectorEvent class is a baseline class for events triggered by various
 * detectors. It is very similar to {@link SensorEvent}, but can only be
 * created by Detector objects.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class DetectorEvent extends SensorEvent {

	/**
	 * Creates a DetectorEvent object with a message.
	 * 
	 * @param detector The triggering detector.
	 * @param message A message to be passed on to the consumer.
	 * @since 1.0
	 */
	public DetectorEvent(Detector detector, String message) {
		super(detector, message);
	}
	
	/**
	 * Creates a DetectorEvent object without a message.
	 * 
	 * @param detector The triggering detector.
	 * @since 1.0
	 */
	public DetectorEvent(Detector detector) {
		super(detector);
	}
	
	/**
	 * Gets the triggering detector.
	 * 
	 * @return The detector that triggered the event.
	 * @since 1.0
	 */
	@Override
	public Detector getOrigin() {
		return (Detector) sensor;
	}
}
