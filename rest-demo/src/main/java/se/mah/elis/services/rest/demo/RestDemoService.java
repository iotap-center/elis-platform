package se.mah.elis.services.rest.demo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

@Component(name = "ElisHTTPDemo", immediate = true)
@Service(value = RestDemoService.class)
public class RestDemoService {

	private boolean populated;
	
	@Reference
	private UserService userService;
	
	public RestDemoService() {
		populated = false;
	}
	
	protected void bindUserService(UserService us) {
		this.userService = us;
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

	protected void unbindUserService(UserService us) {
		this.userService = null;
	}
}
