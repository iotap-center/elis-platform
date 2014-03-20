/**
 * 
 */
package se.mah.elis.adaptor.device.api.events;

import se.mah.elis.adaptor.device.api.entities.devices.Detector;

/**
 * The TripwireEvent is triggeded by
 * {@link se.mah.elis.adaptor.device.api.entities.devices.Tripwire}
 * detectors.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class TripwireEvent extends DetectorEvent {

	/**
	 * Creates a TripwireEvent object with a message.
	 * 
	 * @param detector The triggering detector.
	 * @param message A message to be passed on to the consumer.
	 * @since 1.0
	 */
	public TripwireEvent(Detector detector, String message) {
		super(detector, message);
	}

	/**
	 * Creates a TripwireEvent object without a message.
	 * 
	 * @param detector The triggering detector.
	 * @since 1.0
	 */
	public TripwireEvent(Detector detector) {
		super(detector);
	}

}
