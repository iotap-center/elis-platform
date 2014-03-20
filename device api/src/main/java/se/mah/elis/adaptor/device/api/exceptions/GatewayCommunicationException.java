/**
 * 
 */
package se.mah.elis.adaptor.device.api.exceptions;

/**
 * Thrown when the communication between the platform and the local gateway
 * device is interrupted or fails to be established.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class GatewayCommunicationException extends Exception {

	private static final long serialVersionUID = -4745221381027469389L;
	
	private Exception original;
	
	/**
	 * Creates an instance of this exception.
	 * 
	 * @since 1.0
	 */
	public GatewayCommunicationException() {
		super();
		original = null;
	}
	
	/**
	 * Creates an instance of this exception, with another exception being
	 * saved as a reference to what originally triggered this exception.
	 * 
	 * @param origin The originating exception.
	 * @since 1.0
	 */
	public GatewayCommunicationException(Exception origin) {
		super();
		original = origin;
	}
	
	/**
	 * Returns the exception that originally raised this exception.
	 * 
	 * @return The originating exception.
	 * @since 1.0
	 */
	public Exception getOriginalException() {
		return original;
	}
}
