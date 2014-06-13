package se.mah.elis.data.exceptions;

/**
 * The DataInitalizationException is thrown if a data initialization can't be
 * performed for some reason. It holds information on which service that failed
 * and some information on the reason for failing.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 2.0
 */
public class DataInitalizationException extends Exception {

	private static final long serialVersionUID = 401511393725267353L;
	private String service;
	private String reason;
	
	/**
	 * Initializes an empty instance of this class.
	 * 
	 * @since 2.0
	 */
	public DataInitalizationException() {
		super();
		
		service = "";
		reason = "";
	}
	
	/**
	 * Initializes an instance of this class with a short description on which
	 * service that failed.
	 * 
	 * @param service Name of the service that failed.
	 * @since 2.0
	 */
	public DataInitalizationException(String service) {
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
	 * @since 2.0
	 */
	public DataInitalizationException(String service, String reason) {
		super();
		
		this.service = service;
		this.reason = reason;
	}
	
	/**
	 * Returns a description of the service that failed.
	 * 
	 * @return The name of the service that failed.
	 * @since 2.0
	 */
	String getServiceThatFailed() {
		return service;
	}

	/**
	 * Returns a brief description of why the service failed.
	 * 
	 * @return The error description.
	 * @since 2.0
	 */
	String getReasonForFailure() {
		return reason;
	}
}
