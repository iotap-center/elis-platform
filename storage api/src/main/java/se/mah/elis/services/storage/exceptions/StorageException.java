/**
 * 
 */
package se.mah.elis.services.storage.exceptions;

/**
 * A StorageException is thrown when the persistent storage fails to write,
 * delete or retrieve an object.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class StorageException extends Exception {

	private static final long serialVersionUID = 1907776511239444176L;

	/**
	 * Creates an instance of StorageException.
	 * 
	 * @since 1.0 
	 */
	public StorageException() {
	}

	/**
	 * Creates an instance of StorageException with a short description of the
	 * nature of the error.
	 * 
	 * @param message The error message.
	 * @since 1.1
	 */
	public StorageException(String message) {
		super(message);
	}

}
