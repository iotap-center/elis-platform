package se.mah.elis.services.storage.query;

/**
 * The ResultPayloadTuple interface describes a row in a query result. It
 * contains the payload's class name, as well as the payload itself. 
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface ResultPayload {
	
	/**
	 * Returns the class of the payload.
	 * 
	 * @return Returns the class of the payload.
	 * @since 1.0
	 */
	Class getType();
	
	/**
	 * Returns the payload object.
	 * 
	 * @return Returns the payload object.
	 * @since 1.0
	 */
	Object getPayload();
}
