/**
 * 
 */
package se.mah.elis.impl.services.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

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
@Component(name = "Elis User Service")
@Service(value = UserService.class)
public class UserServiceImpl implements UserService {

	@Reference
	private Storage storage;
	
	@Reference
	private LogService log;
	
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
		User[] users = new User[0];
		
		log(LogService.LOG_INFO, "Fetching users belonging to " + pu);
		
		if (pu.getUserId() != null) {
			userIds = utils.getUsersAssociatedWithPlatformUser(pu.getUserId());
			
			users = new User[userIds.length];
			
			for (int i = 0; i < users.length; i++) {
				try {
					users[i] = (User) storage.readUser(userIds[i]);
				} catch (StorageException e) {
					log(LogService.LOG_WARNING, "Couldn't read user " + i +
							" belonging to " + pu, e);
				}
			}
		}
		
		return users;
	}

	@Override
	public User getUser(PlatformUser pu, UUID uuid) {
		User user = null;
		
		log(LogService.LOG_INFO, "Fetching user " + uuid);
		
		try {
			if (storage.readUser((PlatformUserIdentifier)
					pu.getIdentifier()) != null) {
				user = (User) storage.readUser(uuid);
			}
		} catch (StorageException e) {
			log(LogService.LOG_WARNING, "Couldn't find user " + uuid, e);
		}
		
		return user;
	}

	@Override
	public PlatformUser getPlatformUser(UserIdentifier identifier) {
		PlatformUser user = null;
		
		log(LogService.LOG_INFO, "Fetching user " + identifier);
		
		try {
			user = (PlatformUser) storage.readUser(identifier);
		} catch (StorageException e) {
			log(LogService.LOG_WARNING, "Couldn't find user " + identifier, e);
		}
		
		return user;
	}

	@Override
	public PlatformUser getPlatformUser(UUID id) {
		PlatformUser user = null;
		
		log(LogService.LOG_INFO, "Fetching platform user " + id);
		
		try {
			user = (PlatformUser) storage.readUser(id);
		} catch (StorageException e) {
			log(LogService.LOG_INFO, "Couldn't find platform user " + id, e);
		}
		
		return user;
	}

	@Override
	public synchronized void registerUserToPlatformUser(User user, PlatformUser platformUser)
			throws NoSuchUserException {
		log(LogService.LOG_INFO, "Registering user " + user + " to " + platformUser);
		
		if (platformUser.getUserId() == null) {
			throw new NoSuchUserException();
		} else {
			try {
				if (storage.readUser(platformUser.getUserId()) == null) {
					log(LogService.LOG_WARNING, "Tried to register to non-existing platform user");
					throw new NoSuchUserException();
				}
			} catch (StorageException e) {
				log(LogService.LOG_WARNING, "Couldn't register to platform user", e);
				throw new NoSuchUserException();
			}
		}
		
		
		try {
			storage.insert(user);
		} catch (StorageException e1) {
			log(LogService.LOG_WARNING, "Couldn't store user " + user);
		}
		
		try {
			utils.coupleUsers(platformUser.getUserId(), user.getUserId());
		} catch (StorageException e) {
			log(LogService.LOG_WARNING, "Couldn't associate user " + user +
					" with platform user " + platformUser, e);
		}
	}

	@Override
	public synchronized void unregisterUserFromPlatformUser(User u, PlatformUser pu)
			throws NoSuchUserException {
		log(LogService.LOG_INFO, "Unregistering user " + u + " from " + pu);
		
		utils.decoupleUsers(pu.getUserId(), u.getUserId());
	}

	@Override
	public synchronized PlatformUser createPlatformUser(String username,
			String password)
					throws UserExistsException, IllegalArgumentException {
		PlatformUserIdentifier id = new PlatformUserIdentifierImpl();
		PlatformUser pu = new PlatformUserImpl(id);
		
		log(LogService.LOG_INFO, "Creating " + username);
		
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
			log(LogService.LOG_WARNING, "Couldn't create platform user " + username, e);
			throw new IllegalArgumentException();
		}
		
		return pu;
	}

	@Override
	public PlatformUser[] getPlatformUsersAssociatedWithUser(User u)
			throws NoSuchUserException {
		ArrayList<PlatformUser> platformUsers = new ArrayList<PlatformUser>();
		UUID[] platformUserIds = new UUID[0];
		Properties props = new Properties();
		
		log(LogService.LOG_INFO, "Fetching platform users associated with " + u);
		
		platformUserIds = utils.getPlatformUsersAssociatedWithUser(u.getUserId());
		
		for (int i = 0; i < platformUserIds.length; i++) {
			props.put("uuid", platformUserIds[i]);
			
			platformUsers.add(storage.readPlatformUsers(props)[0]);
		}
		
		return platformUsers.toArray(new PlatformUser[0]);
	}

	@Override
	public PlatformUser[] getPlatformUsers() {
		log(LogService.LOG_INFO, "Fetching all platform users");
		
		return storage.readPlatformUsers(new Properties());
	}

	@Override
	public synchronized void updatePlatformUser(PlatformUser pu) throws NoSuchUserException {
		log(LogService.LOG_INFO, "Updating platform user " + pu);
		
		try {
			storage.update(pu);
		} catch (StorageException e) {
			log(LogService.LOG_WARNING,"Couldn't update user " + pu, e);
			throw new NoSuchUserException();
		}
	}

	@Override
	public synchronized void deletePlatformUser(PlatformUser pu) throws NoSuchUserException {
		log(LogService.LOG_INFO, "Deleting platform user " + pu);
		
		try {
			storage.delete(pu);
		} catch (StorageException e) {
			log(LogService.LOG_WARNING, "Couldn't delete user " + pu, e);
			throw new NoSuchUserException();
		}
	}
	
	private void log(int level, String message, Throwable t) {
		if (log != null) {
			log.log(level, "UserServiceImpl: " + message, t);
		}
	}
	
	private void log(int level, String message) {
		if (log != null) {
			log.log(level, "UserServiceImpl: " + message);
		}
	}

	protected void bindStorage(Storage storage) {
		this.storage = storage;
	}

	protected void unbindStorage(Storage storage) {
		this.storage = null;
	}
	
	protected void bindLog(LogService log) {
		this.log = log;
	}
	
	protected void unbindLog(LogService log) {
		this.log = null;
	}
}
