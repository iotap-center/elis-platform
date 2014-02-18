package se.mah.elis.services.rest.demo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

@Path("/demo")
@Produces("text/plain")
@Component(name = "ElisHTTPDemo", immediate = true)
@Service(value = RestDemoService.class)
public class RestDemoService {

	@Reference
	private UserService userService;
	
	public RestDemoService() {}
	
	protected void bindUserService(UserService us) {
		this.userService = us;
		
	}
	
	@GET
	public String getDemo() {
		return "200: OK";
	}
	
	@GET
	@Path("/{id}")
	public String getDeviceList(@PathParam("id") String id) {
		if (userService != null) {
			PlatformUser pu = userService.getPlatformUser(id);
			User[] users = userService.getUsers(pu);
			String devices = "";
			for (User user : users) {
				if (user instanceof GatewayUser) {
					for (Device device : ((GatewayUser) user).getGateway())
						devices += ";" + device.getName();
				}
			}
			return devices;
		} else {
			return "504: Platform is broken";
		}
	}

	protected void unbindUserService(UserService us) {
		this.userService = null;
	}
}
