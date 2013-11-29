package se.mah.elis.services.storage.query;


/**
 * <p>The ChainingPredicate interface describes a predicate used to link
 * several predicates together in a tree-like structure. By doing so, several
 * levels of criteria used in the data filtering can be used. The
 * ChainingPredicate uses an internal binary structure, the branches named
 * "left" and "right". Each branch is executed separately, before being
 * compared to its neighbor.</p>
 * 
 * <p>As all interfaces extending Predicate, ChainingPredicate makes use of
 * self references for method chaining.</p>
 * 
 * <p>Every implementation of ChainingPredicate must include a reference to the
 * {@link ChainingPredicates} enumeration, publicly visible under the name
 * <i>Predicates</i>.</p>
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface ChainingPredicate extends Predicate {
	
	/**
	 * Sets the left-hand branch of the predicate.
	 * 
	 * @param left The predicate of the left branch.
	 * @return A reference back to the ChainingPredicate object.
	 * @since 1.0
	 */
	
	ChainingPredicate setLeft(Predicate left);
	
	/**
	 * Sets the right-hand branch of the predicate.
	 * 
	 * @param right The predicate of the right branch.
	 * @return A reference back to the ChainingPredicate object.
	 * @since 1.0
	 */
	ChainingPredicate setRight(Predicate right);
}
