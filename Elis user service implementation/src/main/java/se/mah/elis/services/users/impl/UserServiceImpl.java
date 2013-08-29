/**
 * 
 */
package se.mah.elis.services.users.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;

/**
 * An implementation of {@link se.mah.elis.services.users.UserService}. For the
 * time being, all data is stored in memory and isn't persistent in any way.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class UserServiceImpl implements UserService {

	// TODO This is a placeholer. It has to be replaced with a persistent storage at a later stage.
	private Map<PlatformUser, ArrayList<User>> map;
	
	/**
	 * 
	 */
	public UserServiceImpl() {
		// TODO This isn't kosher
		map = new HashMap<PlatformUser, ArrayList<User>>();
	}

	/* (non-Javadoc)
	 * @see se.mah.elis.services.users.UserService#getUsers(se.mah.elis.services.users.PlatformUser)
	 */
	@Override
	public User[] getUsers(PlatformUser pu) {
		// TODO This isn't kosher yet
		
		User[] users = new User[0];
		
		if (map.containsKey(pu)) {
			users = (User[]) ((ArrayList<User>) map.get(pu)).toArray(users);
		}
		
		return users;
	}

	@Override
	public void registerUserToPlatformUser(User u, PlatformUser pu)
			throws NoSuchUserException {
		// TODO This isn't kosher
		
		ArrayList<User> list = null;
		
		if (!map.containsKey(pu)) {
			list = new ArrayList<User>();
			map.put(pu, list);
		} else {
			list = (ArrayList<User>) map.get(pu);
		}
		
		list.add(u);
	}

	@Override
	public void unregisterUserFromPlatformUser(User u, PlatformUser pu)
			throws NoSuchUserException {
		// TODO This isn't kosher
		
		ArrayList<User> list = (ArrayList<User>) map.get(pu);
		
		if (list != null) {
			list.remove(u);
			
			if (list.size() == 0) {
				map.remove(pu);
			}
		}
	}

}
