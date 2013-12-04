package se.mah.elis.services.storage.query;

/**
 * A QueryTranslator is needed to convert a Query object into a query that can
 * be run by the backend of the storage service. Any vendor providing or
 * preferring another database backend than the one provide by the Elis project
 * will have to implement a QueryTranslator of their own.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.1
 */
public interface QueryTranslator {
	
	/**
	 * Tells the query what kind of data to look for.
	 * 
	 * @param c The class of the data to look for.
	 * @return A reference back to the query object.
	 * @since 1.1
	 */
	QueryTranslator what(Class dataType);
	
	/**
	 * Limits the number of objects returned by the storage.
	 * 
	 * @param start The number of objects in the full list that will be
	 * 		ignored in the result from the storage engine.
	 * @param size The size of the returned list.
	 * @return A reference back to the query object.
	 * @since 1.1
	 */
	QueryTranslator where(Predicate predicate);
	
	/**
	 * Sets the predicate telling the storage engine what data to look for. If
	 * no predicate is set, all data objects (as defined by setDataType)
	 * visible to the current user will be returned.
	 * 
	 * @param p The predicate to match against.
	 * @return A reference back to the query object.
	 * @since 1.1
	 */
	QueryTranslator limit(int start, int limit);
//	if (start > -1 && size > -1) {
//		translator.limit(start, size);
//	}
	
	/**
	 * Sets the order in which the results will be returned.
	 * 
	 * @param oldestFirst True if the oldest data objects should be returned
	 * 		first (i.e. list[0] is the oldest object), otherwise false.
	 * @return A reference back to the query object.
	 * @since 1.1
	 */
	QueryTranslator order(boolean oldestFirst);
	
	/**
	 * Compiles an Or predicate.
	 * 
	 * @param right The right-hand predicate.
	 * @param left The left-hand predicate.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	String or(Predicate right, Predicate left);
	
	/**
	 * Compiles an And predicate.
	 * 
	 * @param right The right-hand predicate.
	 * @param left The left-hand predicate.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	String and(Predicate right, Predicate left);
	
	/**
	 * Compiles an Equals predicate.
	 * 
	 * @param predicate The predicate to compile.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	String eq(String field, Object criterion);
	
	/**
	 * Compiles a Neq predicate.
	 * 
	 * @param predicate The predicate to compile.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	String neq(String field, Object criterion);
	
	/**
	 * Compiles a Like predicate.
	 * 
	 * @param predicate The predicate to compile.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	String like(String field, Object criterion);
	
	/**
	 * Compiles an Lt predicate.
	 * 
	 * @param predicate The predicate to compile.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	String lt(String field, Object criterion);
	
	/**
	 * Compiles an Lte predicate.
	 * 
	 * @param predicate The predicate to compile.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	String lte(String field, Object criterion);
	
	/**
	 * Compiles a Gt predicate.
	 * 
	 * @param predicate The predicate to compile.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	String gt(String field, Object criterion);
	
	/**
	 * Compiles a Gte predicate.
	 * 
	 * @param predicate The predicate to compile.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	String gte(String field, Object criterion);
	
	/**
	 * Translates a query into a string suitable for the backend database.
	 * 
	 * @return The string representation of the query.
	 * @since 1.1
	 */
	String compile();
}
