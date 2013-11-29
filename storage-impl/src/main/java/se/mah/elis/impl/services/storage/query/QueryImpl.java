package se.mah.elis.impl.services.storage.query;

import se.mah.elis.services.storage.query.Predicate;
import se.mah.elis.services.storage.query.Query;

public class QueryImpl implements Query {

	private Predicate predicate;
	private Class dataType;
	private int start;
	private int size;
	private boolean oldestFirst;
	
	public QueryImpl() {
		predicate = null;
		dataType = null;
		start = -1;
		size = -1;
		oldestFirst = true;
	}

	@Override
	public Query setDataType(Class c) {
		dataType = c;
		
		return this;
	}

	@Override
	public Query limit(int start, int size) {
		this.start = start;
		this.size = size;
		
		return this;
	}

	@Override
	public Query setPredicate(Predicate p) {
		predicate = p;
		
		return this;
	}

	@Override
	public Query setOrder(boolean oldestFirst) {
		this.oldestFirst = oldestFirst;
		
		return this;
	}

	public String compile() {
		String compiled = "SELECT * FROM " + dataType.getSimpleName();
		
		if (predicate != null) {
			compiled += " WHERE " + predicate.compile();
		}
		
		if (start > -1 && size > -1) {
			compiled += " LIMIT " + start + ", " + size + ";";
		}
		
		return compiled;
	}
}
