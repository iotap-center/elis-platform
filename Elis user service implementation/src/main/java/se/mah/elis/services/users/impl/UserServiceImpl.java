/**
 * 
 */
package se.mah.elis.services.users.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

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
	private int counter;
	
	/**
	 * 
	 */
	public UserServiceImpl() {
		// TODO This isn't kosher
		map = new HashMap<PlatformUser, ArrayList<User>>();
		counter = 0;
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
		
		for (User user : users) {
			try {
				user.initialize();
			} catch (UserInitalizationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return users;
	}

	@Override
	public synchronized void registerUserToPlatformUser(User u, PlatformUser pu)
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
	public synchronized void unregisterUserFromPlatformUser(User u, PlatformUser pu)
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

	@Override
	public synchronized PlatformUser createPlatformUser(String username, String password) {
		ArrayList<User> list = null;
		PlatformUser pu =
				new PlatformUserImpl(new PlatformUserIdentifier(username,
						password));
		
		if (!map.containsKey(pu)) {
			((PlatformUserIdentifier) pu.getId()).setId(++counter);
			
			list = new ArrayList<User>();
			map.put(pu, list);
		}
		
		return pu;
	}

	@Override
	public PlatformUser[] getPlatformUsersAssociatedWithUser(User u)
			throws NoSuchUserException {
		
		PlatformUser[] pus = new PlatformUserImpl[0];
		Set<PlatformUser> set = new HashSet<PlatformUser>();
		
		for (Entry<PlatformUser, ArrayList<User>> entry : map.entrySet()) {
			for (User user : entry.getValue()) {
				if (user == u) {
					set.add(entry.getKey());
				}
			}
		}
		
		return set.toArray(pus);
	}

	@Override
	public PlatformUser[] getPlatformUsers() {
		PlatformUser[] pus = new PlatformUserImpl[0];
		
		return map.keySet().toArray(pus);
	}

	@Override
	public synchronized void updatePlatformUser(PlatformUser pu) throws NoSuchUserException {
		if (map.containsKey(pu)) {
			ArrayList<User> users = map.get(pu);
			map.remove(pu);
			map.put(pu, users);
		} else {
			throw new NoSuchUserException();
		}
	}
	
	/**
	 * This is used mainly for testing.
	 * 
	 * @return The number of registered platform users.
	 */
	public int getNbrOfPlatformUsers() {
		return map.size();
	}

}
