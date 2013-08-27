package se.mah.elis.services.users;

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
	
}
