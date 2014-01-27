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
public class ChainingPredicate implements Predicate {
	
	/**
	 * The Type Enum tells what kind of chaining will be used.
	 * 
	 * @since 1.1
	 */
	public enum ChainingType {
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
		OR;
	}

	private ChainingType type;
	private Predicate left;
	private Predicate right;
	private QueryTranslator translator;
	
	/**
	 * Creates an instance of ChainingPredicate.
	 * 
	 * @param type The type of chaining to be used.
	 * @since 1.1
	 */
	public ChainingPredicate(ChainingType type) {
		this.type = type;
		left = null;
		right = null;
	}
	
	/**
	 * Sets the left-hand branch of the predicate.
	 * 
	 * @param left The predicate of the left branch.
	 * @return A reference back to the ChainingPredicate object.
	 * @since 1.0
	 */
	
	public ChainingPredicate setLeft(Predicate left) {
		this.left = left;
		
		return this;
	}
	
	/**
	 * Sets the right-hand branch of the predicate.
	 * 
	 * @param right The predicate of the right branch.
	 * @return A reference back to the ChainingPredicate object.
	 * @since 1.0
	 */
	public ChainingPredicate setRight(Predicate right) {
		this.right = right;
		
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
		String compiled = " ";
		
		left.setTranslator(translator);
		right.setTranslator(translator);
		
		switch (type) {
			case AND:
				compiled = translator.and(right, left);
				break;
			case OR:
				compiled = translator.or(right, left);
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
			case AND: state = "AND:\n";
				break;
			case OR: state = "OR:\n";
				break;
			default: state = "UNDEFINED:\n";
		}
		
		state += "  left: " + left + "\n" +
				 "  right: " + right + "\n" +
				 "  translator: " + translator;
		
		return state;
	}
}
