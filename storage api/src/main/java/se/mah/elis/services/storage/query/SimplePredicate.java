package se.mah.elis.services.storage.query;

import java.util.Date;

import org.joda.time.DateTime;

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
public class SimplePredicate implements Predicate {
	
	/**
	 * The Type Enum tells what kind of predicate this is.
	 * 
	 * @since 1.1
	 */
	public enum CriterionType {
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
		LIKE;
	}

	private CriterionType type;
	private String field;
	private Object criterion;
	private QueryTranslator translator;

	/**
	 * Creates an instance of SimplePredicate.
	 * 
	 * @param type The type of chaining to be used.
	 * @since 1.1
	 */
	public SimplePredicate(CriterionType type) {
		this.type = type;
		field = null;
		criterion = null;
	}
	
	/**
	 * Sets the name of the field to look at. The name is typically given by
	 * asking the data class in question what fields it uses.
	 * 
	 * @param field The name of the field to look at.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	public SimplePredicate setField(String field) {
		this.field = field;
		
		return this;
	}
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A boolean value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	public SimplePredicate setCriterion(boolean criterion) {
		this.criterion = criterion;
		
		return this;
	}
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A float value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	public SimplePredicate setCriterion(float criterion) {
		this.criterion = criterion;
		
		return this;
	}
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A double value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	public SimplePredicate setCriterion(double criterion) {
		this.criterion = criterion;
		
		return this;
	}
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion An int value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	public SimplePredicate setCriterion(int criterion) {
		this.criterion = criterion;
		
		return this;
	}
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A long value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	public SimplePredicate setCriterion(long criterion) {
		this.criterion = criterion;
		
		return this;
	}
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A byte value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	public SimplePredicate setCriterion(byte criterion) {
		this.criterion = criterion;
		
		return this;
	}
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A String value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	public SimplePredicate setCriterion(String criterion) {
		this.criterion = criterion;
		
		return this;
	}
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A Date value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 * @deprecated As of version 1.1, replaced by
	 * 		{@link #setCriterion(DateTime)}.
	 */
	public SimplePredicate setCriterion(Date criterion) {
		this.criterion = criterion;
		
		return this;
	}
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A DateTime value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.1
	 */
	public SimplePredicate setCriterion(DateTime criterion) {
		this.criterion = criterion;
		
		return this;
	}
	
	/**
	 * Sets the criterion of the predicate.
	 * 
	 * @param criterion A UserIdentifier value.
	 * @return A reference back to the SimplePredicate object.
	 * @since 1.0
	 */
	public SimplePredicate setCriterion(UserIdentifier criterion) {
		this.criterion = criterion;
		
		return this;
	}

	/**
	 * Compiles the predicate.
	 * 
	 * @param translator The QueryTranslator provided by the backend
	 * 		implementation.
	 * @return A string holding the compiled predicate.
	 * @since 1.0
	 */
	public String compile() {
		String compiled = null;
		
		switch (type) {
			case LT:
				compiled = translator.lt(field, criterion);
				break;
			case LTE:
				compiled = translator.lte(field, criterion);
				break;
			case EQ:
				compiled = translator.eq(field, criterion);
				break;
			case GTE:
				compiled = translator.gte(field, criterion);
				break;
			case GT:
				compiled = translator.gt(field, criterion);
				break;
			case NEQ:
				compiled = translator.neq(field, criterion);
				break;
			case LIKE:
				compiled = translator.like(field, criterion);
				break;
		}
		
		return compiled;
	}

	/**
	 * Sets the translator to be used when translating the query.
	 * 
	 * @param translator The QueryTranslator provided by the backend
	 * 		implementation.
	 * @since 1.1
	 */
	@Override
	public void setTranslator(QueryTranslator translator) {
		this.translator = translator;
	}
	
	/**
	 * Returns a representation of this object as a string.
	 * 
	 * @return A representation of the current state of this object.
	 * @since 1.1
	 */
	@Override
	public String toString() {
		String state;
		
		switch (type) {
			case LT: state = "LT:\n";
				break;
			case LTE: state = "LTE:\n";
				break;
			case EQ: state = "EQ:\n";
				break;
			case NEQ: state = "NEQ:\n";
				break;
			case GT: state = "GT:\n";
				break;
			case GTE: state = "GTE:\n";
				break;
			case LIKE: state = "LIKE:\n";
				break;
			default: state = "UNDEFINED:\n";
		}
		
		state += "  field: " + field + "\n" +
				 "  criterion: " + criterion + "\n" +
				 "  translator: " + translator;
		
		return state;
	}
}
