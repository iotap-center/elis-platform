package se.mah.elis.exceptions;

/**
 * OutOfDateRangeException is thrown by various methods dealing with historical
 * data. In those methods, providing a date that is outside the supported date
 * range, an exception of this type may be thrown.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class OutOfDateRangeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3915145865986150072L;

}
