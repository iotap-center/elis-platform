package se.mah.elis.services.demo.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.exceptions.UserExistsException;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;

@Component(name = "ElisUserLoader", immediate = true)
@Service(value = UserLoaderService.class)
public class UserLoaderService {

	private boolean populated;
	private List<String> demoUsers;
	
	@Reference
	private UserService userService;
	
	@Reference
	private UserFactory userFactory;
	
	public UserLoaderService() {
		populated = false;
		demoUsers = new ArrayList<String>();
		
		// read from file?
		demoUsers.add("marcus;elis;Marcus;Ljungblad;marcus@ljungblad.nu;EonUser;eon;marcus.ljungblad@mah.se;medeamah2012");
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
		registerGwUserToPlatformUser(gwuser, pu);
		System.out.println("Added: " + userinfo);
	}
	
	private void registerGwUserToPlatformUser(User user, PlatformUser pu) {
		try {
			userService.registerUserToPlatformUser(user, pu);
		} catch (NoSuchUserException e) {
			e.printStackTrace();
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
}
