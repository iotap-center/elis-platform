package se.mah.elis.services.storage.query;

import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.AbstractUser;

/**
 * A QueryTranslator is needed to convert a Query object into a query that can
 * be run by the backend of the storage service. Any vendor providing or
 * preferring another database backend than the one provide by the Elis project
 * will have to implement a QueryTranslator of their own.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
 */
public interface QueryTranslator {
	
	/**
	 * Tells the query what kind of data to look for.
	 * 
	 * @param c The class of the data to look for.
	 * @return A reference back to the query translator object.
	 * @since 2.0
	 */
	QueryTranslator what(Class dataType);
	
	/**
	 * Limits the number of objects returned by the storage.
	 * 
	 * @param start The number of objects in the full list that will be
	 * 		ignored in the result from the storage engine.
	 * @param size The size of the returned list.
	 * @return A reference back to the query translator object.
	 * @since 2.0
	 */
	QueryTranslator where(Predicate predicate);
	
	/**
	 * Sets the predicate telling the storage engine what data to look for. If
	 * no predicate is set, all data objects (as defined by setDataType)
	 * visible to the current user will be returned.
	 * 
	 * @param p The predicate to match against.
	 * @return A reference back to the query translator object.
	 * @since 2.0
	 */
	QueryTranslator limit(int start, int limit);
	
	/**
	 * Sets the order in which the results will be returned.
	 * 
	 * @param oldestFirst True if the oldest data objects should be returned
	 * 		first (i.e. list[0] is the oldest object), otherwise false.
	 * @return A reference back to the query translator object.
	 * @since 2.0
	 */
	QueryTranslator order(boolean oldestFirst);
	
	/**
	 * Compiles an Or predicate.
	 * 
	 * @param left The left-hand predicate.
	 * @param right The right-hand predicate.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	String or(Predicate left, Predicate right);
	
	/**
	 * Compiles an And predicate.
	 * 
	 * @param left The left-hand predicate.
	 * @param right The right-hand predicate.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	String and(Predicate left, Predicate right);
	
	/**
	 * Compiles an Equals predicate.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	String eq(String field, Object criterion);
	
	/**
	 * Compiles a Neq (not equals) predicate.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	String neq(String field, Object criterion);
	
	/**
	 * Compiles a Like predicate. Any Number or String is permitted as a
	 * criterion for this method.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	String like(String field, Object criterion);
	
	/**
	 * Compiles an Lt (less than) predicate. Any Number or DateTime is
	 * permitted as a criterion for this method. 
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	String lt(String field, Object criterion);
	
	/**
	 * Compiles an Lte (less than or equal) predicate. Any Number or DateTime
	 * is permitted as a criterion for this method.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	String lte(String field, Object criterion);
	
	/**
	 * Compiles a Gt (greater than) predicate. Any Number or DateTime is
	 * permitted as a criterion for this method.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	String gt(String field, Object criterion);
	
	/**
	 * Compiles a Gte (greater than or equal) predicate. Any Number or DateTime
	 * is permitted as a criterion for this method.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	String gte(String field, Object criterion);
	
	/**
	 * Compiles a user predicate.
	 * 
	 * @param user The user to match.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	String user(AbstractUser user);
	
	/**
	 * Translates a query into a string suitable for the backend database.
	 * 
	 * @return The string representation of the query.
	 * @throws StorageException if the predicate couldn't be compiled.
	 * @since 2.0
	 */
	String compile() throws StorageException;
}
