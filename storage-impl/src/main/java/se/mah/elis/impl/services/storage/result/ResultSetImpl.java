package se.mah.elis.impl.services.storage.result;

import java.util.ArrayList;
import java.util.Arrays;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.result.ResultSet;

/**
 * Implementation of {@link ResultSet}.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.1
 */
public class ResultSetImpl implements ResultSet {

	// Points at the current element in the set.
	private int pointer;
	
	// Holds information on the class of the elements.
	private Class objectType;
	
	// The result elements.
	private Object[] rows;
	
	/**
	 * Creates an instance of this class.
	 * 
	 * @param objectType The class declaration of the result elements.
	 * @param results The result elements.
	 * @since 1.1
	 */
	public ResultSetImpl(Class objectType, Object[] results) {
		pointer = -1;
		this.objectType = objectType;
		if (results != null) {
			rows = results;
		} else {
			rows = new Object[0];
		}
	}
	
	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.result.ResultSet#size() size()}.
	 * 
	 * @return The number of rows.
	 * @since 1.1
	 */
	@Override
	public int size() {
		return rows.length;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.result.ResultSet#get(int) get(int)}.
	 * 
	 * @param index The position of the desired object.
	 * @return The desired object.
	 * @throws IndexOutOfBoundsException if the index was out of bounds.
	 * @since 1.1
	 */
	@Override
	public Object get(int index) throws IndexOutOfBoundsException {
		return rows[index];
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.result.ResultSet#getArray() getArray()}.
	 * 
	 * @return The results as an array. If no results are found, the method
	 * 		returns a null reference.
	 * @since 1.1
	 */
	@Override
	public Object[] getArray() {
		return rows;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.result.ResultSet#reset() reset()}.
	 * 
	 * @since 1.1
	 */
	@Override
	public void reset() {
		pointer = -1;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.result.ResultSet#hasNext() hasNext()}.
	 * 
	 * @return True if there are more rows, otherwise false.
	 * @since 1.1
	 */
	@Override
	public boolean hasNext() {
		return (pointer + 2) < rows.length;
	}


	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.result.ResultSet#next() next()}.
	 * 
	 * @return The next element in the result set.
	 * @since 1.1
	 */
	@Override
	public Object next() {
		return rows[++pointer];
	}


	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.result.ResultSet#first() first()}.
	 * 
	 * @return The first element in the result set.
	 * @since 1.1
	 */
	@Override
	public Object first() {
		if (rows.length > 0) {
			return rows[0];
		}
		
		return null;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.result.ResultSet#last() last()}.
	 * 
	 * @param data The data object to be stored.
	 * @throws StorageException Thrown when the object couldn't be stored.
	 * @since 1.1
	 */
	@Override
	public Object last() {
		if (rows.length > 0) {
			return rows[rows.length - 1];
		}
		
		return null;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.result.ResultSet#getObjectType() getObjectType()}.
	 * 
	 * @return The last element in the result set.
	 * @since 1.1
	 */
	@Override
	public Class getObjectType() {
		return objectType;
	}

}
