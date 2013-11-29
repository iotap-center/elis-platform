package se.mah.elis.services.storage.query;

/**
 * <p>Query is used to look for data objects in the Elis storage. It makes use
 * of self references for method chaining. A Query will always return full
 * objects, as opposed to proper database systems, where the user can freely
 * choose what fields to return. Also, in its current incarnation, a Query
 * can't join several data object types in a nice manner. The data retrieved
 * from the storage will be returned as oldest-first by default, meaning that
 * if the user runs a query limited by start = 1, size = 1, the object returned
 * will always be the oldest.</p>
 * 
 * <p>Please note, that the query will never return data that isn't visible to
 * the user performing the query.</p>
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Query {
	
	/**
	 * Tells the query what kind of data to look for.
	 * 
	 * @param c The class of the data to look for.
	 * @return A reference back to the query object.
	 * @since 1.0
	 */
	Query setDataType(Class c);
	
	/**
	 * Limits the number of objects returned by the storage.
	 * 
	 * @param start The number of objects in the full list that will be
	 * 		ignored in the result from the storage engine.
	 * @param size The size of the returned list.
	 * @return A reference back to the query object.
	 * @since 1.0
	 */
	Query limit(int start, int size);
	
	/**
	 * Sets the predicate telling the storage engine what data to look for. If
	 * no predicate is set, all data objects (as defined by setDataType)
	 * visible to the current user will be returned.
	 * 
	 * @param p The predicate to match against.
	 * @return A reference back to the query object.
	 * @since 1.0
	 */
	Query setPredicate(Predicate p);
	
	/**
	 * Sets the order in which the results will be returned.
	 * 
	 * @param oldestFirst True if the oldest data objects should be returned
	 * 		first (i.e. list[0] is the oldest object), otherwise false.
	 * @return A reference back to the query object.
	 * @since 1.0
	 */
	Query setOrder(boolean oldestFirst);
}
