package se.mah.elis.services.rest.demo;

import java.util.Properties;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Component;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.GatewayCommunicationException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.exceptions.UserExistsException;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;

@Component(name = "ElisHTTPDemo", immediate = true)
@Service(value = RestDemoService.class)
public class RestDemoService {

	private boolean populated;
	
	@Reference
	private UserService userService;
	
	@Reference
	private UserFactory userFactory;
	
	public RestDemoService() {
		populated = false;
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
	
	private void populate() {
		if (userService == null || userFactory == null)
			return;
		PlatformUser pu = createMljungblad();
		createDemoUser(pu);
		runDemo(pu);
	}

	private void runDemo(PlatformUser pu) {
		User[] users = userService.getUsers(pu);
		for (User user : users) {
			if (user instanceof GatewayUser)
				runDemoWithGatewayUser((GatewayUser) user);
		}
	}
	
	private void runDemoWithGatewayUser(GatewayUser user) {
		System.out.println("Listing devices for " + user.getIdentifier().toString());
		Gateway gateway = user.getGateway();
		for (Device device : gateway) {
			System.out.println(device.getName());
		}
	}

	private void createDemoUser(PlatformUser pu) {
		User eonUser = createEonUser();
		
		try {
			userService.registerUserToPlatformUser(eonUser, pu);
		} catch (NoSuchUserException e) {
			e.printStackTrace();
		}
		
		populated = true;
		
		System.out.println("Created a demo user called mljungblad/elis");
	}

	private User createEonUser() {
		Properties props = new Properties();
		props.put("email", "marcus.ljungblad@mah.se");
		props.put("password", "medeamah2012");
		User eonUser = null;
		
		try {
			eonUser = userFactory.build("EonUser", "eon", props);
		} catch (UserInitalizationException e) {
			e.printStackTrace();
		}
		
		return eonUser;
	}

	private PlatformUser createMljungblad() {
		PlatformUser mljungblad = null;
		try {
			mljungblad = userService.createPlatformUser("mljungblad", "elis");
			mljungblad.setFirstName("Marcus");
			mljungblad.setLastName("Ljungblad");
			mljungblad.setEmail("marcus@ljungblad.nu");
			userService.updatePlatformUser(mljungblad);
		} catch (UserExistsException e) {
			e.printStackTrace();
		} catch (NoSuchUserException e) {
			e.printStackTrace();
		}
		return mljungblad;
	}

	protected void unbindUserService(UserService us) {
		this.userService = null;
	}
	
	protected void unbindUserFactory(UserFactory uf) {
		this.userFactory = null;
	}
}
