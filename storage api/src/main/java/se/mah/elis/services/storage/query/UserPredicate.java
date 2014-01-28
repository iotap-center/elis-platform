package se.mah.elis.services.storage.query;

import java.util.Properties;

import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;

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
	private UserIdentifier uid;
	
	/**
	 * Creates an instance of UserPredicate.
	 * 
	 * @since 2.0
	 */
	public UserPredicate() {
		translator = null;
		uid = null;
	}
	
	/**
	 * Creates an instance of UserPredicate.
	 * 
	 * @param id The user to look for.
	 * @since 2.0
	 */
	public UserPredicate(UserIdentifier id) {
		translator = null;
		uid = id;
	}
	
	/**
	 * Creates an instance of UserPredicate.
	 * 
	 * @param user The user to look for.
	 * @since 2.0
	 */
	public UserPredicate(User user) {
		translator = null;
		uid = user.getIdentifier();
	}
	
	/**
	 * Creates an instance of UserPredicate.
	 * 
	 * @param user The user to look for.
	 * @since 2.0
	 */
	public UserPredicate(PlatformUser user) {
		translator = null;
		uid = user.getIdentifier();
	}

	/**
	 * Sets the user to look for.
	 * 
	 * @param id The user to look for.
	 * @since 2.0
	 */
	public void setUser(UserIdentifier id) {
		uid = id;
	}
	
	/**
	 * Sets the user to look for.
	 * 
	 * @param user The user to look for.
	 * @since 2.0
	 */
	public void setUser(User user) {
		uid = user.getIdentifier();
	}
	
	/**
	 * Sets the user to look for.
	 * 
	 * @param user The user to look for.
	 * @since 2.0
	 */
	public void setUser(PlatformUser user) {
		uid = user.getIdentifier();
	}
	
	@Override
	public String compile() throws StorageException {
		if (translator == null) {
			throw new StorageException("No translator set");
		} else if (uid == null) {
			throw new StorageException("No user set");
		}
		
		return translator.user(uid);
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
		Properties user = null;
		String translatorString = null;
		
		if (translator != null) {
			translatorString = translator.getClass().getName();
		}
		if (uid != null) {
			user = uid.getProperties();
		}
		
		return "UserPredicate:\n" +
			   "  Translator: " + translatorString + "\n" +
			   "  User: " + user;
	}
}
