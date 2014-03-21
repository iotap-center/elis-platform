package se.mah.elis.impl.services.storage.exceptions;

import se.mah.elis.services.storage.exceptions.StorageException;

/**
 * This exception is thrown when an empty DeleteQuery is "accidentally" run.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
 */
public class YouAreBreakingTheInternetException extends StorageException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2673197740248490235L;

	/**
	 * Initializes an instance of this exception.
	 * 
	 * @since 2.0
	 */
	public YouAreBreakingTheInternetException() {
		super("Deletion with empty query is not permitted.");
	}
}
