package se.mah.elis.services.storage.query.test.mock;

import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.Predicate;
import se.mah.elis.services.storage.query.QueryTranslator;
import se.mah.elis.services.users.UserIdentifier;

public class MockTranslator implements QueryTranslator {
	
	private Class clazz;
	private Predicate predicate;
	private int start, limit;
	private boolean oFirst;

	public MockTranslator() {
		clazz = null;
		predicate = null;
		start = limit = -1;
		oFirst = false;
	}
	
	@Override
	public QueryTranslator what(Class dataType) {
		clazz = dataType;
		
		return this;
	}

	@Override
	public QueryTranslator where(Predicate predicate) {
		this.predicate = predicate;
		
		return this;
	}

	@Override
	public QueryTranslator limit(int start, int limit) {
		this.start = start;
		this.limit = limit;
		
		return this;
	}

	@Override
	public QueryTranslator order(boolean oldestFirst) {
		oFirst = oldestFirst;
		
		return this;
	}

	@Override
	public String or(Predicate right, Predicate left) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String and(Predicate right, Predicate left) {
		try {
			return right.compile() + " and " + left.compile();
		} catch (StorageException e) {
			return "";
		}
	}

	@Override
	public String eq(String field, Object criterion) {
		return " eq: " + field + ", " + criterion;
	}

	@Override
	public String neq(String field, Object criterion) {
		return " neq: " + field + ", " + criterion;
	}

	@Override
	public String like(String field, Object criterion) {
		return " like: " + field + ", " + criterion;
	}

	@Override
	public String lt(String field, Object criterion) {
		return " lt: " + field + ", " + criterion;
	}

	@Override
	public String lte(String field, Object criterion) {
		return " lte: " + field + ", " + criterion;
	}

	@Override
	public String gt(String field, Object criterion) {
		return " gt: " + field + ", " + criterion;
	}

	@Override
	public String gte(String field, Object criterion) {
		return " gte: " + field + ", " + criterion;
	}

	@Override
	public String user(UserIdentifier user) {
		return " user: " + user.toString();
	}

	@Override
	public String compile() throws StorageException {
		String compiled = "Translate:\n";
		
		if (clazz != null) {
			compiled += "  what: " + clazz.getName() + "\n";
		}
		if (predicate != null) {
			compiled += "  where: " + predicate.compile() + "\n";
		}
		if (start >= 0 && limit > 0) {
			compiled += "  limits: " + start + ", " + limit + "\n";
		}
		compiled += "  oldestFirst: " + oFirst;
				
		return compiled;
	}

	@Override
	public String toString() {
		try {
			return compile();
		} catch (StorageException e) {
			return "";
		}
	}
}
