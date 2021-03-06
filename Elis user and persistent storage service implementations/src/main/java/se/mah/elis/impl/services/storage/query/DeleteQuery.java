package se.mah.elis.impl.services.storage.query;

import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.Query;

/**
 * The DeleteQuery class is a very simple extension of the standard Query
 * class, catering for delete queries in MySQL. 
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
 */
public class DeleteQuery extends Query {
	
	private Query query;
	
	public DeleteQuery() {
		this.query = new Query();
	}
	
	public DeleteQuery(Query query) {
		this.query = query;
	}
	
	public void setQuery(Query query) {
		this.query = query;
	}
	
	/**
	 * Translates a query into a string suitable for the backend database.
	 * 
	 * @return The string representation of the query.
	 * @throws StorageException if the query couldn't be compiled.
	 * @since 2.0
	 */
	public String compile() throws StorageException {
		return ((MySQLQueryTranslator) translator
				.what(dataType).where(predicate)).compileDeleteQuery();
	}
}
