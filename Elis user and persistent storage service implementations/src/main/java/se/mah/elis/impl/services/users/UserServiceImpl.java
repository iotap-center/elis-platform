/**
 * 
 */
package se.mah.elis.impl.services.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
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
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
@Component(name = "Elis User service")
@Service
public class UserServiceImpl implements UserService {

	@Reference
	private Storage storage;
	
	// The database connection
	private Connection connection;
	
	// TODO This is a placeholer. It has to be replaced with a persistent storage at a later stage.
	private Map<PlatformUser, ArrayList<User>> map;
	private int platformUserCounter;
	
	/**
	 * 
	 */
	public UserServiceImpl() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// TODO Replace with non-static stuff later on
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost/elis?"
						+	"user=elis&password=notallthatsecret");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// TODO This isn't kosher
		map = new TreeMap<PlatformUser, ArrayList<User>>();
		platformUserCounter = 0;
	}
	
	public UserServiceImpl(Storage storage, Connection connection) {
		this.storage = storage;
		this.connection = connection;
		
		// TODO This isn't kosher - remove when done
		map = new TreeMap<PlatformUser, ArrayList<User>>();
		platformUserCounter = 0;
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
	public User getUser(PlatformUser pu, UUID uuid) {
		User user = null;
		ArrayList<User> users = map.get(pu);
		
		if (users != null) {
			for (User u : users) {
				if (u.getUserId() == uuid) {
					user = u;
				}
			}
		}
		
		return user;
	}

	@Override
	public PlatformUser getPlatformUser(UserIdentifier identifier) {
		PlatformUser user = null;
		
		try {
			user = (PlatformUser) storage.readUser(identifier);
		} catch (StorageException e) {}
		
		return user;
	}

	@Override
	public PlatformUser getPlatformUser(String id) {
		PlatformUserIdentifierImpl identifier = new PlatformUserIdentifierImpl();
		identifier.setId(Integer.parseInt(id));
		
		return getPlatformUser(identifier);
	}

	@Override
	public synchronized void registerUserToPlatformUser(User user, PlatformUser platformUser)
			throws NoSuchUserException {
		// TODO This isn't kosher
		
		ArrayList<User> list = null;
		
		if (user.getUserId() == null) {
			user.setUserId(UUID.randomUUID());
		}
		
		if (!map.containsKey(platformUser)) {
			list = new ArrayList<User>();
			((PlatformUserIdentifierImpl) platformUser.getIdentifier()).setId(++platformUserCounter);
			map.put(platformUser, list);
		} else {
			list = (ArrayList<User>) map.get(platformUser);
		}
		
		list.add(user);
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
		PlatformUserIdentifier id = new PlatformUserIdentifierImpl();
		PlatformUser pu = new PlatformUserImpl(id);
		
		id.setUsername(username);
		id.setPassword(password);
		
		try {
			storage.readUser(id);
			// If there is an existing user, this will happen
			throw new UserExistsException();
		} catch (StorageException e1) {
		}
		
		try {
			storage.insert(pu);
		} catch (StorageException e) {
			throw new IllegalArgumentException();
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
		return storage.readPlatformUsers(new Properties());
	}

	@Override
	public synchronized void updatePlatformUser(PlatformUser pu) throws NoSuchUserException {
		try {
			storage.update(pu);
		} catch (StorageException e) {
			throw new NoSuchUserException();
		}
	}

	@Override
	public synchronized void deletePlatformUser(PlatformUser pu) throws NoSuchUserException {
		try {
			storage.delete(pu);
		} catch (StorageException e) {
			throw new NoSuchUserException();
		}
	}
}
