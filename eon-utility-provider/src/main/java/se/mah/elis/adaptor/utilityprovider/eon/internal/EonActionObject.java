package se.mah.elis.adaptor.utilityprovider.eon.internal;

/**
 * The E.On api returns so called action objects for long-running HTTP requests.
 * This class embodies such a response. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 */
public class EonActionObject {

	private long id;
	private EonActionStatus status;
	private String message;
	
	public EonActionObject(long id, EonActionStatus s, String message) {
		this.id = id;
		this.status = s;
		this.message = message;
	}
	
	public EonActionObject(long id, EonActionStatus s) {
		this(id, s, null);
	}

	/**
	 * Get the ID of the action object
	 * 
	 * @return id
	 * @since 1.0
	 */
	public long getId() {
		return id;
	}

	/**
	 * Gets any messages associated with the action object
	 * 
	 * @return message
	 * @since 1.0
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * An action object always contains a status. The status is 
	 * defined in {@link EonActionStatus}. 
	 * 
	 * @return status
	 */
	public EonActionStatus getStatus() {
		return this.status;
	}
	
	

}
