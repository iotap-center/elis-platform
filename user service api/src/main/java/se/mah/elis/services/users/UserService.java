package se.mah.elis.services.users;

import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.exceptions.UserExistsException;

/**
 * The UserBroker interface describes a user service.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface UserService {
	
	/**
	 * Fetches all users associated with this platform user account.
	 * 
	 * @param pu The platform user.
	 * @return An array of User objects. If no users are found, an empty array
	 *         is returned. The user objects are all initialized when returned.
	 * @since 1.0
	 */
	User[] getUsers(PlatformUser pu);
	
	/**
	 * Fetches a user based on its owning platform user and its user
	 * id number.
	 * 
	 * @param pu The platform user.
	 * @param uid The user object's id number.
	 * @return A User object. If no user is found, null is returned.
	 * @since 1.0
	 */
	User getUser(PlatformUser pu, int uid);
	
	/**
	 * Fetches all platform users.
	 * 
	 * @return An array of PlatformUser objects. If no users are found, an
	 *         empty array is returned.
	 * @since 1.0
	 */
	PlatformUser[] getPlatformUsers();
	
	/**
	 * Fetches a platform user, based on a proper UserIdentifier object.
	 * 
	 * @param identifier A UserIdentifier object.
	 * @return A PlatformUser object. If no user is found, null is returned.
	 * @since 1.0
	 */
	PlatformUser getPlatformUser(UserIdentifier identifier);
	
	/**
	 * Fetches a platform user, based on a String representation of the main
	 * identifier.
	 * 
	 * @param id The main identifier.
	 * @return A PlatformUser object. If no user is found, null is returned.
	 * @since 1.0
	 */
	PlatformUser getPlatformUser(String id);
	
	/**
	 * Fetches all platform users associated with this user account.
	 * 
	 * @param u The user.
	 * @return An array of PlatformUser objects. If no platform users are
	 * 		   found, an empty array is returned.
	 * @throws NoSuchUserException
	 * @since 1.0
	 */
	PlatformUser[] getPlatformUsersAssociatedWithUser(User u)
			throws NoSuchUserException;
	
	/**
	 * Associates a user account with a platform user.
	 * 
	 * @param u The user to associate with a PlatformUser.
	 * @param pu The PlatformUser to associate with.
	 * @throws NoSuchUserException if no such PlatformUser exists.
	 * @since 1.0
	 */
	void registerUserToPlatformUser(User u, PlatformUser pu)
			throws NoSuchUserException;
	
	/**
	 * Disassociates a user account with a platform user.
	 * 
	 * @param u The user to disassociate with the PlatformUser.
	 * @param pu The PlatformUser.
	 * @throws NoSuchUserException if no such PlatformUser exists.
	 * @since 1.0
	 */
	void unregisterUserFromPlatformUser(User u, PlatformUser pu)
			throws NoSuchUserException;
	
	/**
	 * Creates a PlatformUser object.
	 * 
	 * @param username The platform user's username.
	 * @param password The platform user's password.
	 * @return The newly created PlatformUser.
	 * @throws NoSuchUserException if the PlatformUser already exists.
	 * @since 1.0
	 */
	PlatformUser createPlatformUser(String username, String password)
			throws UserExistsException;
	
	/**
	 * Updates a platform user.
	 * 
	 * @param pu The platform user to update.
	 * @throws NoSuchUserException if no such PlatformUser exists.
	 * @since 1.0
	 */
	void updatePlatformUser(PlatformUser pu)
			throws NoSuchUserException;
	
	/**
	 * Deletes a platform user.
	 * 
	 * @param pu The platform user to delete.
	 * @throws NoSuchUserException if no such PlatformUser exists.
	 * @since 1.0
	 */
	void deletePlatformUser(PlatformUser pu)
			throws NoSuchUserException;
}
