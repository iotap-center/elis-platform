package se.mah.elis.services.storage.query;

import java.util.Date;

import se.mah.elis.services.users.UserIdentifier;

/**
 * <p>The SimplePredicate describes a predicate that compares a data value with
 * a given criterion. By calling a setCriterion method, the user also tells the
 * predicate what kind of data type to compare against.</p>
 * 
 * <p>As all interfaces extending Predicate, SimplePredicate makes use of self
 * references for method chaining.</p>
 * 
 * <p>Every implementation of SimplePredicate must include a reference to the
 * {@link SimplePredicates} enumeration, publicly visible under the name
 * <i>Predicates</i>.</p>
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface SimplePredicate extends Predicate {
	
	/**
	 * Sets the name of the field to look at. The name is typically given by
	 * asking the data class in question what fields it uses.
	 * 
	 * @param field The name of the field to look at.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	SimplePredicate setField(String field);
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A boolean value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	SimplePredicate setCriterion(boolean criterion);
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A float value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	SimplePredicate setCriterion(float criterion);
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A double value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	SimplePredicate setCriterion(double criterion);
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion An int value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	SimplePredicate setCriterion(int criterion);
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A long value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	SimplePredicate setCriterion(long criterion);
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A byte value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	SimplePredicate setCriterion(byte criterion);
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A String value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	SimplePredicate setCriterion(String criterion);
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A Date value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	SimplePredicate setCriterion(Date criterion);
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A UserIdentifier value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	SimplePredicate setCriterion(UserIdentifier criterion);
}
