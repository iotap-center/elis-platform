package se.mah.elis.services.storage.query;

import se.mah.elis.services.storage.exceptions.StorageException;

/**
 * The Predicate interface is a base interface used by {@link Query} to specify
 * what data to look for.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Predicate {
	
	/**
	 * Compiles the predicate.
	 * 
	 * @return A string holding the compiled predicate.
	 * @throws StorageException if the query couldn't be compiled.
	 * @since 1.0
	 */
	String compile() throws StorageException;
	
	/**
	 * Sets the translator to be used when translating the query.
	 * 
	 * @param translator The QueryTranslator provided by the backend
	 * 		implementation.
	 * @since 2.0
	 */
	void setTranslator(QueryTranslator translator);
}
