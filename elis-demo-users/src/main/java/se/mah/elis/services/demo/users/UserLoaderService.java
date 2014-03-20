package se.mah.elis.services.demo.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.exceptions.UserExistsException;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;

/**
 * This OSGI service installs the list of demo users defined in the constructor. The 
 * user must be linked to one E.On account and one MKB water meter.  
 * 
 * In a future version, the demo users should be loaded from a file rather
 * than hard coded in this class. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0 
 * @version 1.0
 */
@Component(name = "ElisUserLoader", immediate = true)
@Service(value = UserLoaderService.class)
public class UserLoaderService {

	private boolean populated;
	private List<String> demoUsers;
	
	@Reference
	private UserService userService;
	
	@Reference
	private UserFactory userFactory;
	
	@Reference
	private LogService log;
	
	public UserLoaderService() {
		populated = false;
		demoUsers = new ArrayList<String>();
		
		// read from file?
		demoUsers.add("marcus;elis;Marcus;Ljungblad;marcus@ljungblad.nu;EonUser;eon;eon2hem@gmail.com;02DCBD;MkbWaterUser;mkb-water;63408097");
	}
	
	protected void bindUserService(UserService us) {
		this.userService = us;
		
		if (!populated) 
			populate();
	}
	
	protected void bindUserFactory(UserFactory uf) {
		this.userFactory = uf;
		
		if (!populated) 
			populate();
	}
	
	private synchronized void populate() {
		if (userService == null || userFactory == null)
			return;
		
		for (String userinfo : demoUsers) 
			addUser(userinfo);
		
		populated = true;
	}
	
	private void addUser(String userinfo) {
		String[] user = parseUser(userinfo);
		PlatformUser pu = createPlatformUser(user[0], user[1], user[2], user[3], user[4]);
		User gwuser = createEonUser(user[5], user[6], user[7], user[8]);
		User mkbuser = createMkbUser(user[9], user[10], user[11]);
		registerGwUserToPlatformUser(gwuser, pu);
		registerGwUserToPlatformUser(mkbuser, pu);
		System.out.println("Added: " + userinfo);
	}
	
	private User createMkbUser(String userType, String serviceName, String meterId) {
		Properties props = new Properties();
		props.put("id", meterId);
		User mkbUser = null;
		
		try {
			mkbUser = userFactory.build(userType, serviceName, props);
		} catch (UserInitalizationException uie) {
			log.log(LogService.LOG_ERROR, uie.getMessage());
		}
		
		return mkbUser;
	}

	private void registerGwUserToPlatformUser(User user, PlatformUser pu) {
		try {
			userService.registerUserToPlatformUser(user, pu);
		} catch (NoSuchUserException e) {
			log.log(LogService.LOG_ERROR, e.getMessage());
		}
	}

	private User createEonUser(String userType, String serviceName, String username,
			String password) {
		User eonUser = null;

		Properties props = new Properties();
		props.put("email", username);
		props.put("password", password);
		
		try {
			eonUser = userFactory.build(userType, serviceName, props);
		} catch (UserInitalizationException e) {
			e.printStackTrace();
		}
		
		return eonUser;
	}

	private String[] parseUser(String userinfo) {
		return userinfo.split(";");
	}

	private PlatformUser createPlatformUser(String username, String password, 
			String firstname, String lastname, String email) {
		PlatformUser user = null;
		try {
			user = userService.createPlatformUser(username, password);
			user.setFirstName(firstname);
			user.setLastName(lastname);
			user.setEmail(email);
			userService.updatePlatformUser(user);
		} catch (UserExistsException e) {
			e.printStackTrace();
		} catch (NoSuchUserException e) {
			e.printStackTrace();
		}
		return user;
	}

	protected void unbindUserService(UserService us) {
		this.userService = null;
	}
	
	protected void unbindUserFactory(UserFactory uf) {
		this.userFactory = null;
	}
	
	protected void bindLog(LogService l) {
		log = l;
	}
	
	protected void unbindLog(LogService l) {
		log = null;
	}
}
