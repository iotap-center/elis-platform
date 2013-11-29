package se.mah.elis.impl.services.storage.query;

import java.util.Date;

import se.mah.elis.services.storage.query.SimplePredicate;
import se.mah.elis.services.storage.query.SimplePredicates;
import se.mah.elis.services.users.UserIdentifier;

public class SimplePredicateImpl implements SimplePredicate {

	public SimplePredicates Predicates;
	private Criterion type;
	private String field;
	private Object criterion;

	private enum Criterion {
		BOOLEAN, FLOAT, DOUBLE, INT, LONG, BYTE, STRING, DATE, USERIDENTIFIER;
	}

	private SimplePredicateImpl() {
		field = null;
		criterion = null;
		type = Criterion.INT;
	}

	@Override
	public SimplePredicate setField(String field) {
		this.field = field;
		
		return this;
	}

	@Override
	public SimplePredicate setCriterion(boolean criterion) {
		this.criterion = criterion;
		type = Criterion.BOOLEAN;
		
		return this;
	}

	@Override
	public SimplePredicate setCriterion(float criterion) {
		this.criterion = criterion;
		type = Criterion.FLOAT;
		
		return this;
	}

	@Override
	public SimplePredicate setCriterion(double criterion) {
		this.criterion = criterion;
		type = Criterion.DOUBLE;
		
		return this;
	}

	@Override
	public SimplePredicate setCriterion(int criterion) {
		this.criterion = criterion;
		type = Criterion.INT;
		
		return this;
	}

	@Override
	public SimplePredicate setCriterion(long criterion) {
		this.criterion = criterion;
		type = Criterion.LONG;
		
		return this;
	}

	@Override
	public SimplePredicate setCriterion(byte criterion) {
		this.criterion = criterion;
		type = Criterion.BYTE;
		
		return this;
	}

	@Override
	public SimplePredicate setCriterion(String criterion) {
		this.criterion = criterion;
		type = Criterion.STRING;
		
		return this;
	}

	@Override
	public SimplePredicate setCriterion(Date criterion) {
		this.criterion = criterion;
		type = Criterion.DATE;
		
		return this;
	}

	@Override
	public SimplePredicate setCriterion(UserIdentifier criterion) {
		this.criterion = criterion;
		type = Criterion.USERIDENTIFIER;
		
		return this;
	}

	@Override
	public String compile() {
		String compiled = field;
		boolean doLike = false;
		
		switch (Predicates) {
			case LT:
				compiled += " < ";
				break;
			case LTE:
				compiled += " <= ";
				break;
			case EQ:
				compiled += " = ";
				break;
			case GTE:
				compiled += " >= ";
				break;
			case GT:
				compiled += " > ";
				break;
			case NEQ:
				compiled += " <> ";
				break;
			case LIKE:
				doLike = true; 
				break;
		}
		
		switch (type) {
			case BOOLEAN:
				if ((Boolean) criterion) {
					compiled += "TRUE";
				} else {
					compiled += "FALSE";
				}
				break;
			case FLOAT:
			case DOUBLE:
			case INT:
			case LONG:
			case USERIDENTIFIER:
				compiled += criterion.toString();
				break;
			case STRING:
				if (doLike) {
					compiled += "\"%" + criterion.toString() + "%\"";
				} else {
					compiled += "\"" + criterion.toString() + "\"";
				}
				break;
			case DATE:
				compiled += "\"" + criterion.toString() + "\"";
		}
		
		return compiled;
	}

}
