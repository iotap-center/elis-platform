package se.mah.elis.impl.services.storage.exceptions;

import se.mah.elis.exceptions.TypeMismatchException;

/**
 * This exception is thrown when an impossible predicate is created.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
 */
public class YouAreDoingItWrongException extends TypeMismatchException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5973472834648561399L;

	/**
	 * Initializes an instance of this exception.
	 * 
	 * @since 2.0
	 */
	public YouAreDoingItWrongException() {
		super("This combination of predicate and condition isn't permitted.");
	}
}
