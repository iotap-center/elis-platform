package se.mah.elis.services.storage;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.Query;
import se.mah.elis.services.storage.result.ResultSet;
import se.mah.elis.services.users.AbstractUser;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;

/**
 * The Storage interface defines a set of methods used to store stuff in the
 * Elis platform. It deals with instances of {@link ElisDataObject} and
 * {@link AbstractUser}.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Storage {
	
	/**
	 * Inserts a data object into the Elis storage.
	 * 
	 * @param data The data to be stored.
	 * @throws StorageException if the data couldn't be stored.
	 * @since 1.0
	 */
	void insert(ElisDataObject data) throws StorageException;
	
	/**
	 * Inserts a set of data objects into the Elis storage.
	 * 
	 * @param data The data to be stored.
	 * @throws StorageException if any of the data couldn't be stored.
	 * @since 1.0
	 */
	void insert(ElisDataObject[] data) throws StorageException;

	/**
	 * Inserts a user into the Elis storage.
	 * 
	 * @param user The user to be stored.
	 * @throws StorageException if the user couldn't be stored.
	 * @since 1.0
	 */
	void insert(AbstractUser user) throws StorageException;
	
	/**
	 * Inserts a set of users into the Elis storage. 
	 * 
	 * @param user The user to be stored.
	 * @throws StorageException if any of the users couldn't be stored.
	 * @since 1.0
	 */
	void insert(AbstractUser[] user) throws StorageException;
	
	/**
	 * Updates a data object in the storage.
	 * 
	 * @param data The data object to be updated.
	 * @throws StorageException if the data couldn't be updated.
	 * @since 1.0
	 */
	void update(ElisDataObject data) throws StorageException;
	
	/**
	 * Updates a set of data objects in the storage.
	 * 
	 * @param data The data objects to be updated.
	 * @throws StorageException if any of the data couldn't be updated.
	 * @since 1.0
	 */
	void update(ElisDataObject[] data) throws StorageException;
	
	/**
	 * Updates a user in the Elis storage.
	 * 
	 * @param user The user to be updated.
	 * @throws StorageException if the user couldn't be updated.
	 * @since 1.0
	 */
	void update(AbstractUser user) throws StorageException;
	
	/**
	 * Updates a set of users in the Elis storage. 
	 * 
	 * @param user The users to be updated.
	 * @throws StorageException if any of the users couldn't be updated.
	 * @since 1.0
	 */
	void update(AbstractUser[] user) throws StorageException;
	
	/**
	 * Deletes a data object from the Elis storage.
	 * 
	 * @param data The data object to delete.
	 * @throws StorageException if the data couldn't be deleted.
	 * @since 1.0
	 */
	void delete(ElisDataObject data) throws StorageException;
	
	/**
	 * Deletes a set of data objects from the Elis storage.
	 * 
	 * @param data The data objects to delete.
	 * @throws StorageException if any of the data couldn't be deleted.
	 * @since 1.0
	 */
	void delete(ElisDataObject[] data) throws StorageException;

	/**
	 * Deletes a user from the Elis storage.
	 * 
	 * @param user The user to delete.
	 * @throws StorageException if the user couldn't be deleted.
	 * @since 1.0
	 */
	void delete(AbstractUser user) throws StorageException;
	
	/**
	 * Deletes a set of users from the Elis storage.
	 * 
	 * @param user The users to delete.
	 * @throws StorageException if any of the users couldn't be deleted.
	 * @since 1.0
	 */
	void delete(AbstractUser[] user) throws StorageException;
	
	/**
	 * Deletes data based on a query.
	 * 
	 * @param query A query describing what data to delete.
	 * @throws StorageException if the query wasn't processed.
	 * @since 1.0
	 */
	void delete(Query query) throws StorageException;
	
	/**
	 * Reads out a specific data object from the storage.
	 * 
	 * @param id The unique data id.
	 * @return An ElisDataObject containing the wanted data.
	 * @throws StorageException if the data wasn't found.
	 * @since 1.0
	 */
	ElisDataObject readData(UUID id) throws StorageException;
	
	/**
	 * Reads out a specific data object from the storage.
	 * 
	 * @param edo The data object to read.
	 * @return An ElisDataObject containing the wanted data.
	 * @throws StorageException if the data wasn't found.
	 * @since 2.0
	 */
	ElisDataObject readData(ElisDataObject edo) throws StorageException;
	
	/**
	 * Reads out a specific user from the storage. This method will not look
	 * for any PlatformUsers. Instead, use {@link #readUser(AbstractUser)} or
	 * {@link #readUser(UserIdentifier)} for that.
	 * 
	 * @param id The unique data id.
	 * @return A User object.
	 * @throws StorageException if the user wasn't found.
	 * @since 2.0
	 */
	AbstractUser readUser(UUID id) throws StorageException;
	
	/**
	 * Reads out a specific user from the storage.
	 * 
	 * @param id An AbstractUser object containing a UserIdentifier.
	 * @return An AbstractUser object.
	 * @throws StorageException if the user wasn't found.
	 * @since 2.0
	 */
	AbstractUser readUser(AbstractUser user) throws StorageException;
	
	/**
	 * <p>Reads out all users matching a set of criteria. The criteria will
	 * work as a filter.<p>
	 * 
	 * <p>Any string criterion will be searched for in a wild card-pattern,
	 * i.e. "man" will match "Batman", "mandible" and "man". Non-string
	 * criteria will be used to look for exact matches. Any criteria trying to
	 * match against fields named "password" will be neglected.</p>
	 * 
	 * @param userType The type of users to be read.
	 * @param criteria The criteria to match against.
	 * @return An array of AbstractUser objects.
	 * @since 2.0
	 */
	AbstractUser[] readUsers(Class userType, Properties criteria);
	
	/**
	 * <p>Reads out all platform users matching a set of criteria. The criteria
	 * will work as a filter.<p>
	 * 
	 * <p>Any string will be searched for in a wild card-pattern, i.e. "man"
	 * will match "Batman", "mandible" and "man". Non-string criteria will be
	 * used to look for exact matches. Any criteria trying to match against
	 * fields named "password" will be neglected.</p>
	 * 
	 * @param criteria The criteria to match against.
	 * @return An array of PlatformUser objects.
	 * @since 2.0
	 */
	PlatformUser[] readPlatformUsers(Properties criteria);
	
	/**
	 * Runs a select query on the Elis storage.
	 * 
	 * @param query The query to run.
	 * @return A Result object, containing the data found by the currently run
	 * 		query.
	 * @throws StorageException if the query wasn't processed.
	 * @since 1.0
	 */
	ResultSet select(Query query) throws StorageException;
	
	/**
	 * Checks whether an object exists in the data storage or not.
	 * 
	 * @param object The id of the object to look for.
	 * @return True if the object exists, otherwise false.
	 * @since 2.1
	 */
	boolean objectExists(UUID object);
}
