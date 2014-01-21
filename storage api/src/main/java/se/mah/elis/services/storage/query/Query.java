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
public class Query {

	protected Predicate predicate;
	protected Class dataType;
	protected int start;
	protected int size;
	protected boolean oldestFirst;
	protected QueryTranslator translator;
	
	/**
	 * Creates an instance of the class.
	 * 
	 * @since 1.1
	 */
	public Query() {
		predicate = null;
		dataType = null;
		start = -1;
		size = -1;
		oldestFirst = true;
	}
	
	/**
	 * Tells the query what kind of data to look for.
	 * 
	 * @param c The class of the data to look for.
	 * @return A reference back to the query object.
	 * @since 1.0
	 */
	public Query setDataType(Class c) {
		dataType = c;
		
		return this;
	}
	
	/**
	 * Tells the user what kind of data this query will look for.
	 * 
	 * @return The class of data that the query will look for.
	 * @since 1.1
	 */
	public Class getDataType() {
		return dataType;
	}
	
	/**
	 * Limits the number of objects returned by the storage.
	 * 
	 * @param start The number of objects in the full list that will be
	 * 		ignored in the result from the storage engine.
	 * @param size The size of the returned list.
	 * @return A reference back to the query object.
	 * @since 1.0
	 */
	public Query limit(int start, int size) {
		this.start = start;
		this.size = size;
		
		return this;
	}
	
	/**
	 * Sets the predicate telling the storage engine what data to look for. If
	 * no predicate is set, all data objects (as defined by setDataType)
	 * visible to the current user will be returned.
	 * 
	 * @param p The predicate to match against.
	 * @return A reference back to the query object.
	 * @since 1.0
	 */
	public Query setPredicate(Predicate p) {
		predicate = p;
		
		return this;
	}
	
	/**
	 * Sets the order in which the results will be returned.
	 * 
	 * @param oldestFirst True if the oldest data objects should be returned
	 * 		first (i.e. list[0] is the oldest object), otherwise false.
	 * @return A reference back to the query object.
	 * @since 1.0
	 */
	public Query setOrder(boolean oldestFirst) {
		this.oldestFirst = oldestFirst;
		
		return this;
	}
	
	/**
	 * Sets the translator to be used when translating the query.
	 * 
	 * @param translator The QueryTranslator provided by the backend
	 * 		implementation.
	 * @return A reference back to the query object.
	 * @since 1.1
	 */
	public Query setTranslator(QueryTranslator translator) {
		this.translator = translator;
		
		return this;
	}
	
	/**
	 * Translates a query into a string suitable for the backend database.
	 * 
	 * @return The string representation of the query.
	 * @since 1.1
	 */
	public String compile() {
		return translator.what(dataType).where(predicate)
				.limit(start, size).order(oldestFirst).compile();
	}
}
