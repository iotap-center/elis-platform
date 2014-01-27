/**
 * 
 */
package se.mah.elis.exceptions;

/**
 * This exception is thrown when e.g. a payload object doesn't match the type
 * specified by a protocol, interface or any other specification.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.1
 */
public class TypeMismatchException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8465305841743803074L;

	/**
	 * Creates an instance of this exception.
	 * 
	 * @since 1.1
	 */
	public TypeMismatchException() {
		
	}
	
	/**
	 * Creates an instance of this exception with a description of the error.
	 * 
	 * @param message A message to be passed with the exception.
	 * @since 1.1
	 */
	public TypeMismatchException(String message) {
		super(message);
	}
}
