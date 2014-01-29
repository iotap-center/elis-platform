package se.mah.elis.impl.services.storage.exceptions;

import se.mah.elis.exceptions.TypeMismatchException;
import se.mah.elis.services.storage.exceptions.StorageException;

/**
 * This exception is thrown when an impossible predicate is created.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
 */
public class YouAreDoingItWrongException extends TypeMismatchException {

	/**
	 * Initializes an instance of this exception.
	 * 
	 * @since 2.0
	 */
	public YouAreDoingItWrongException() {
		super("This combination of predicate and condition isn't permitted.");
	}
}
