/**
 * 
 */
package se.mah.elis.impl.services.users;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.exceptions.UserExistsException;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * An implementation of {@link se.mah.elis.services.users.UserService}. For the
 * time being, all data is stored in memory and isn't persistent in any way.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@Component(name = "Elis User service")
@Service
public class UserServiceImpl implements UserService {

	// TODO This is a placeholer. It has to be replaced with a persistent storage at a later stage.
	private Map<PlatformUser, ArrayList<User>> map;
	private int platformUserCounter, userCounter;
	
	/**
	 * 
	 */
	public UserServiceImpl() {
		// TODO This isn't kosher
		map = new TreeMap<PlatformUser, ArrayList<User>>();
		platformUserCounter = 0;
		userCounter = 0;
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
	public User getUser(PlatformUser pu, int uid) {
		User user = null;
		ArrayList<User> users = map.get(pu);
		
		if (users != null) {
			for (User u : users) {
				if (u.getIdNumber() == uid) {
					user = u;
				}
			}
		}
		
		return user;
	}

	@Override
	public PlatformUser getPlatformUser(UserIdentifier identifier) {
		PlatformUser pu = null;
		
		for (PlatformUser key: map.keySet()) {
			if (key.getIdentifier().equals(identifier)) {
				pu = key;
				break;
			}
		}
		
		return pu;
	}

	@Override
	public PlatformUser getPlatformUser(String id) {
		PlatformUserIdentifierImpl identifier = new PlatformUserIdentifierImpl();
		identifier.setId(Integer.parseInt(id));
		
		return getPlatformUser(identifier);
	}

	@Override
	public synchronized void registerUserToPlatformUser(User u, PlatformUser pu)
			throws NoSuchUserException {
		// TODO This isn't kosher
		
		ArrayList<User> list = null;
		
		if (u.getIdNumber() < 1) {
			u.setIdNumber(++userCounter);
		}
		
		if (!map.containsKey(pu)) {
			list = new ArrayList<User>();
			((PlatformUserIdentifierImpl) pu.getIdentifier()).setId(++platformUserCounter);
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
	public synchronized PlatformUser createPlatformUser(String username,
			String password)
					throws UserExistsException, IllegalArgumentException {
		ArrayList<User> list = null;
		PlatformUser pu = null;
		try {
			pu = new PlatformUserImpl(new PlatformUserIdentifierImpl(username,
																 password));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException();
		}
		
		if (!map.containsKey(pu)) {
			((PlatformUserIdentifierImpl) pu.getIdentifier()).setId(++platformUserCounter);
			
			list = new ArrayList<User>();
			map.put(pu, list);
		} else {
			throw new UserExistsException();
		}
		
		return pu;
	}

	@Override
	public PlatformUser[] getPlatformUsersAssociatedWithUser(User u)
			throws NoSuchUserException {
		
		PlatformUser[] pus = new PlatformUserImpl[0];
		Set<PlatformUser> set = new TreeSet<PlatformUser>();
		
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

	@Override
	public synchronized void deletePlatformUser(PlatformUser pu) throws NoSuchUserException {
		if (map.remove(pu) == null) {
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