package se.mah.elis.impl.services.storage.query;

import se.mah.elis.services.storage.query.ChainingPredicate;
import se.mah.elis.services.storage.query.ChainingPredicates;
import se.mah.elis.services.storage.query.Predicate;

public class ChainingPredicateImpl implements ChainingPredicate {

	public ChainingPredicates Predicates;
	private Predicate left;
	private Predicate right;
	
	private ChainingPredicateImpl() {
		left = null;
		right = null;
	}

	@Override
	public ChainingPredicate setLeft(Predicate left) {
		this.left = left;
		
		return this;
	}

	@Override
	public ChainingPredicate setRight(Predicate right) {
		this.right = right;
		
		return this;
	}

	@Override
	public String compile() {
		String compiled = " ";
		
		switch (this.Predicates) {
			case AND:
				compiled = "(" + left.compile() + " AND " + right.compile() + ")";
				break;
			case OR:
				compiled = left.compile() + " OR " + right.compile();
		}
		
		return compiled;
	}

}
