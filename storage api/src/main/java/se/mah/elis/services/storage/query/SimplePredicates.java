package se.mah.elis.services.storage.query;

/**
 * Lists all predicates available to the SimplePredicate interface.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public enum SimplePredicates {
	
	/**
	 * The field value is strictly lesser than the criterion.
	 * 
	 * @since 1.0
	 */
	LT,
	
	/**
	 * The field value is lesser than or equal to the criterion.
	 * 
	 * @since 1.0
	 */
	LTE,
	
	/**
	 * The field value is equal to the criterion.
	 * 
	 * @since 1.0
	 */
	EQ,
	
	/**
	 * The field value is greater than or equal to the criterion.
	 * 
	 * @since 1.0
	 */
	GTE,
	
	/**
	 * The field value is strictly greater than the criterion.
	 * 
	 * @since 1.0
	 */
	GT,
	
	/**
	 * The field value is is not equal to the criterion.
	 * 
	 * @since 1.0
	 */
	NEQ,
	
	/**
	 * The field value looks like the criterion.
	 * 
	 * @since 1.0
	 */
	LIKE 
}
