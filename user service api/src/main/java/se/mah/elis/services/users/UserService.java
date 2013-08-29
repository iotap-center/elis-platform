package se.mah.elis.services.users;

import se.mah.elis.services.users.exceptions.NoSuchUserException;

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
}
