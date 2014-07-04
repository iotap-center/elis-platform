package se.mah.elis.services.storage.query;

import java.util.Properties;

import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.AbstractUser;

/**
 * <p>UserPredicate describes a predicate coupling a user to a dataset.</p>
 * 
 * <p>As all interfaces extending Predicate, SimplePredicate makes use of self
 * references for method chaining.</p>
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
 */
public class UserPredicate implements Predicate {

	private QueryTranslator translator;
	private AbstractUser user;
	
	/**
	 * Creates an instance of UserPredicate.
	 * 
	 * @since 2.0
	 */
	public UserPredicate() {
		translator = null;
		user = null;
	}
	
	/**
	 * Creates an instance of UserPredicate.
	 * 
	 * @param user The user to look for.
	 * @since 2.0
	 */
	public UserPredicate(AbstractUser user) {
		translator = null;
		this.user = user;
	}
	
	/**
	 * Sets the user to look for.
	 * 
	 * @param user The user to look for.
	 * @since 2.0
	 */
	public void setUser(AbstractUser user) {
		this.user = user;
	}
	
	@Override
	public String compile() throws StorageException {
		if (translator == null) {
			throw new StorageException("No translator set");
		} else if (user == null) {
			throw new StorageException("No user set");
		}
		
		return translator.user(user);
	}

	@Override
	public void setTranslator(QueryTranslator translator) {
		this.translator = translator;
	}
	
	@Override
	/**
	 * Returns a string representation of the object.
	 * 
	 * @return A string representation of the object.
	 * @since 2.0
	 */
	public String toString() {
		String translatorString = null;
		String userString = null;
		
		if (translator != null) {
			translatorString = translator.getClass().getName();
		}
		
		if (user != null) {
			userString = "{" + user + "}";
		}
		
		return "UserPredicate:\n" +
			   "  Translator: " + translatorString + "\n" +
			   "  User: " + userString;
	}
}
