package se.mah.elis.services.storage.query;

/**
 * Lists all predicates available to the ChainingPredicate interface.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public enum ChainingPredicates {
	
	/**
	 * Used to link two predicates by using a logical AND statement.
	 * 
	 * @since 1.0
	 */
	AND,
	
	/**
	 * Used to link two predicates by using a logical OR statement.
	 * 
	 * @since 1.0
	 */
	OR
}
