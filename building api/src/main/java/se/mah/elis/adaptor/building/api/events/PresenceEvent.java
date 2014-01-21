/**
 * 
 */
package se.mah.elis.adaptor.building.api.events;

import se.mah.elis.adaptor.building.api.entities.devices.Detector;

/**
 * The TripwireEvent is triggeded by
 * {@link se.mah.elis.adaptor.building.api.entities.devices.PresenceDetector}
 * objects.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class PresenceEvent extends DetectorEvent {

	/**
	 * Denotes a detected presence.
	 * 
	 * @since 1.0
	 */
	public static int PRESENCE_DETECTED = 0;
	
	/**
	 * Denotes a detected absence.
	 * 
	 * @since 1.0
	 */
	public static int ABSENCE_DETECTED = 1;
	
	/**
	 * Denotes whether an absence or presence was detected.
	 * 
	 * @since 1.0
	 * @serial
	 */
	private int type;
	
	/**
	 * Creates a PresenceEvent object with a message.<br>
	 * The constructor takes an extra argument compared to its base class. This
	 * extra argument denotes whether the detector has detected a new presence
	 * or an absence. The constructor will fail if any other value than
	 * PresenceEvent.<i>PRESENCE_DETECTED</i> or
	 * PresenceEvent.<i>ABESENCE_DETECTED</i> is passed to it.
	 * 
	 * @param detector The triggering detector.
	 * @param message A message to be passed on to the consumer.
	 * @param type The type of event.
	 * @since 1.0
	 */
	public PresenceEvent(Detector detector, String message, int type) {
		super(detector, message);
		setType(type);
	}

	/**
	 * Creates a PresenceEvent object without a message.<br>
	 * The constructor takes an extra argument compared to its base class. This
	 * extra argument denotes whether the detector has detected a new presence
	 * or an absence. The constructor will fail if any other value than
	 * PresenceEvent.<i>PRESENCE_DETECTED</i> or
	 * PresenceEvent.<i>ABSENCE_DETECTED</i> is passed to it.
	 * 
	 * @param detector The triggering detector.
	 * @param type The type of event.
	 * @since 1.0
	 */
	public PresenceEvent(Detector detector, int type) {
		super(detector);
		setType(type);
	}
	
	/**
	 * Use this method to see what kind of event this actually is. If the
	 * detector detected a new presence, the value will match
	 * PresenceEvent.<i>PRESENCE_DETECTED</i>, otherwise it will match
	 * PresenceEvent.<i>ABSENCE_DETECTED</i>.
	 * 
	 * @return The event type.
	 * @since 1.0
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Checks whether the <i>type</i> argument is valid or not. If yes, then
	 * set the type field. Otherwise, throw a nasty exception.
	 * 
	 * @param type
	 * @throws IllegalArgumentException
	 * @since 1.0
	 */
	private void setType(int type) throws IllegalArgumentException {
		if (type == PresenceEvent.PRESENCE_DETECTED ||
				type == PresenceEvent.ABSENCE_DETECTED) {
			this.type = type;
		} else {
			throw new IllegalArgumentException("Type must be " +
					"PresenceEvent.PRESENCE_DETECTED or " +
					"PresenceEvent.ABSENCE_DETECTED");
		}
	}

}
