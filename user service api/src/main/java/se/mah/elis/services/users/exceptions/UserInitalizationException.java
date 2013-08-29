package se.mah.elis.services.users.exceptions;

/**
 * The UserInitalizationException is thrown if a user initialization can't be
 * performed for some reason. It holds information on which service that failed
 * and some information on the reason for failing.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class UserInitalizationException extends Exception {

	private static final long serialVersionUID = -4894938694755726909L;
	private String service;
	private String reason;
	
	/**
	 * Initializes an empty instance of this class.
	 * 
	 * @since 1.0
	 */
	public UserInitalizationException() {
		super();
		
		service = "";
		reason = "";
	}
	
	/**
	 * Initializes an instance of this class with a short description on which
	 * service that failed.
	 * 
	 * @param service Name of the service that failed.
	 * @since 1.0
	 */
	public UserInitalizationException(String service) {
		super();
		
		this.service = service;
		this.reason = "";
	}
	
	/**
	 * Initializes an instance of this class with a short description on which
	 * service that failed, and why it did so.
	 * 
	 * @param service Name of the service that failed.
	 * @param reason Reason for failing.
	 * @since 1.0
	 */
	public UserInitalizationException(String service, String reason) {
		super();
		
		this.service = service;
		this.reason = reason;
	}
	
	/**
	 * Returns a description of the service that failed.
	 * 
	 * @return The name of the service that failed.
	 * @since 1.0
	 */
	String getServiceThatFailed() {
		return service;
	}

	/**
	 * Returns a brief description of why the service failed.
	 * 
	 * @return The error description.
	 * @since 1.0
	 */
	String getReasonForFailure() {
		return reason;
	}
}
