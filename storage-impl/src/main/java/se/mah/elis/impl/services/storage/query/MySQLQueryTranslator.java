package se.mah.elis.impl.services.storage.query;

import se.mah.elis.services.storage.query.Predicate;
import se.mah.elis.services.storage.query.QueryTranslator;

public class MySQLQueryTranslator implements QueryTranslator {

	private Class what;
	private Predicate where;
	private int start, limit;
	private boolean oldestFirst;
	
	public MySQLQueryTranslator() {
		what = null;
		where = null;
		start = limit = -1;
		oldestFirst = true;
	}

	@Override
	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#what(Class) what(Class)}.
	 * 
	 * @param c The class of the data to look for.
	 * @return A reference back to the query translator object.
	 * @since 1.1
	 */
	public QueryTranslator what(Class dataType) {
		what = dataType;
		
		return this;
	}

	@Override
	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#where(Predicate) where(Predicate)}.
	 * 
	 * @param start The number of objects in the full list that will be
	 * 		ignored in the result from the storage engine.
	 * @param size The size of the returned list.
	 * @return A reference back to the query translator object.
	 * @since 1.1
	 */
	public QueryTranslator where(Predicate predicate) {
		where = predicate;
		where.setTranslator(this);
		
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
		this.oldestFirst = oldestFirst;

		return this;
	}

	@Override
	public String or(Predicate right, Predicate left) {
		right.setTranslator(this);
		left.setTranslator(this);
		
		return left.compile() + " OR " + right.compile();
	}

	@Override
	public String and(Predicate right, Predicate left) {
		right.setTranslator(this);
		left.setTranslator(this);
		
		return "(" + left.compile() + " AND " + right.compile() + ")";
	}

	@Override
	public String eq(String field, Object criterion) {
		return field + " = " + process(criterion);
	}

	@Override
	public String neq(String field, Object criterion) {
		return field + " <> " + process(criterion);
	}

	@Override
	public String like(String field, Object criterion) {
		return field + " LIKE \"%" + criterion + "%\"";
	}

	@Override
	public String lt(String field, Object criterion) {
		return field + " < " + process(criterion);
	}

	@Override
	public String lte(String field, Object criterion) {
		return field + " <= " + process(criterion);
	}

	@Override
	public String gt(String field, Object criterion) {
		return field + " > " + process(criterion);
	}

	@Override
	public String gte(String field, Object criterion) {
		return field + " >= " + process(criterion);
	}

	@Override
	public String compile() {
		String compiled = "SELECT * FROM " + what.getSimpleName();
		
		if (where != null) {
			compiled += " WHERE " + where.compile();
		}
		
		if (start > -1 && limit > -1) {
			compiled += " LIMIT " + start + ", " + limit;
		}
		
		if (oldestFirst) {
			compiled += " ASC;";
		} else {
			compiled += " DESC;";
		}
		
		return compiled;
	}
	
	public String compileDeleteQuery() {
		String compiled = "DELETE FROM `" + what.getSimpleName();
		
		if (where != null) {
			compiled += "` WHERE " + where.compile();
		}
		
		return compiled;
	}
	
	private String process(Object criterion) {
		String processed = null;
		
		if (criterion instanceof Number) {
			processed = criterion.toString();
		} else if (criterion instanceof Boolean) {
			if ((Boolean) criterion) {
				processed = "1";
			} else {
				processed = "0";
			}
		} else if (criterion == null) {
			processed = "NULL";
		} else {
			processed = "\"" + criterion.toString() + "\"";
		}
		
		return processed;
	}
}
