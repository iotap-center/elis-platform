package se.mah.elis.services.storage.result;

/**
 * <p>The ResultSet interface describes a result from a query to the Elis
 * storage service.</p>
 * 
 * <p>ResultSet has an internal pointer and methods for iterating over the
 * found objects. The developer can also fetch an object at any given position
 * within the result set.</p>
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface ResultSet {
	
	/**
	 * Returns the type of the objects in the result set.
	 * 
	 * @return The class name of the returned objects.
	 * @since 1.0
	 */
	Class getObjectType();
	
	/**
	 * Counts the number of rows in a result.
	 * 
	 * @return The number of rows.
	 * @since 1.0
	 */
	int size();
	
	/**
	 * Fetches the nth object in a result set.
	 * 
	 * @param index The position of the desired object.
	 * @return The desired object.
	 * @throws IndexOutOfBoundsException if the index was out of bounds.
	 * @since 1.0
	 */
	Object get(int index) throws IndexOutOfBoundsException;
	
	/**
	 * Returns the result set as an array of ResultSets.
	 * 
	 * @return The results as an array. If no results are found, the method
	 * returns a null reference.
	 * @since 1.0
	 */
	Object[] getArray();
	
	/**
	 * Resets the internal pointer.
	 * 
	 * @since 1.0
	 */
	void reset();
	
	/**
	 * Checks whether there are more rows after the current row.
	 * 
	 * @return True if there are more rows, otherwise false.
	 * @since 1.0
	 */
	boolean hasNext();
	
	/**
	 * Fetches the next element in the result set and increments the internal
	 * pointer by one.
	 * 
	 * @return The next element in the result set.
	 * @since 1.0
	 */
	Object next();
	
	/**
	 * Fetches the first element in the result set.
	 * 
	 * @return The first element in the result set.
	 * @since 1.0
	 */
	Object first();
	
	/**
	 * Fetches the last element in the result set.
	 * 
	 * @return The last element in the result set.
	 * @since 1.0
	 */
	Object last();
}
