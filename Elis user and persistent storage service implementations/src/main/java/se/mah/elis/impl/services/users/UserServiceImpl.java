/**
 * 
 */
package se.mah.elis.impl.services.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.impl.services.storage.StorageUtils;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.exceptions.UserExistsException;

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
	
	private StorageUtils utils;
	
	/**
	 * 
	 */
	public UserServiceImpl() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// TODO Replace with non-static stuff later on
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost/elis?"
						+	"user=elis&password=notallthatsecret");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.utils = new StorageUtils(connection);
	}
	
	public UserServiceImpl(Storage storage, Connection connection) {
		this.storage = storage;
		this.utils = new StorageUtils(connection);
	}

	/* (non-Javadoc)
	 * @see se.mah.elis.services.users.UserService#getUsers(se.mah.elis.services.users.PlatformUser)
	 */
	@Override
	public User[] getUsers(PlatformUser pu) {
		UUID[] userIds = null;
		User[] users = null;
		
		userIds = utils.getUsersAssociatedWithPlatformUser(
				((PlatformUserIdentifier) pu.getIdentifier()).getId());
		
		users = new User[userIds.length];
		
		for (int i = 0; i < users.length; i++) {
			try {
				users[i] = (User) storage.readUser(userIds[i]);
			} catch (StorageException e) {
			}
		}
		
		return users;
	}

	@Override
	public User getUser(PlatformUser pu, UUID uuid) {
		User user = null;
		
		try {
			if (storage.readUser((PlatformUserIdentifier)
					pu.getIdentifier()) != null) {
				user = storage.readUser(uuid);
			}
		} catch (StorageException e) {
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
		try {
			storage.insert(user);
		} catch (StorageException e1) {
		}
		
		try {
			utils.coupleUsers(((PlatformUserIdentifierImpl)
					platformUser.getIdentifier()).getId(), user.getUserId());
		} catch (StorageException e) {
		}
	}

	@Override
	public synchronized void unregisterUserFromPlatformUser(User u, PlatformUser pu)
			throws NoSuchUserException {
		utils.decoupleUsers(((PlatformUserIdentifierImpl)
				pu.getIdentifier()).getId(), u.getUserId());
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
		ArrayList<PlatformUser> platformUsers = new ArrayList<PlatformUser>();
		int[] platformUserIds = new int[0];
		Properties props = new Properties();
		
		platformUserIds = utils.getPlatformUsersAssociatedWithUser(u.getUserId());
		
		for (int i = 0; i < platformUserIds.length; i++) {
			props.put("id", platformUserIds[i]);
			
			platformUsers.add(storage.readPlatformUsers(props)[0]);
		}
		
		return platformUsers.toArray(new PlatformUser[0]);
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
