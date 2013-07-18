package se.mah.elis.external.web.users;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import se.mah.elis.authentication.users.User;
import se.mah.elis.authentication.users.UserCentral;

@Path("/users/")
public class UserResource {
	
	private final static long DEFAULT_TIMEOUT = 5000;
	
	private static ServiceTracker userCentralServiceTracker;
	
	public UserResource(BundleContext context) {
		if (userCentralServiceTracker == null) {
			userCentralServiceTracker = new ServiceTracker(context, UserCentral.class.getName(), null);
		}
	}
	
	@GET
	@Path("/{userName}")
	public Response getUser(@PathParam("userName") String userName) {
		// System.out.println("Get user: " + userName);
		Response response;
		
		try {
			UserCentral uc = getUserCentralInstance();
			User user;
			if ((user = uc.getUser(userName)) != null) {
				response = Response.ok(user).build();  // normal case 
			} else {
				response = Response.status(Response.Status.NOT_FOUND).entity("No user with that name").build();
			}
		} catch (Exception generalException) {
			// log error message somewhere
			response = noServiceAvailable();
		}
		
		return response;
	}
	
	@PUT
	public Response addUser(User user) {
		Response response;
		
		try {
			UserCentral uc = getUserCentralInstance();
			if (uc.addUser(user) != null) {
				response = Response.ok(user).build();
			} else { 
				response = Response.status(Response.Status.NOT_ACCEPTABLE).entity("User already exists").build();
			}
		} catch (Exception generalException) {
			response = noServiceAvailable();
		}
		
		return response;
	}
	
	private UserCentral getUserCentralInstance() throws InterruptedException {
		return (UserCentral) userCentralServiceTracker.waitForService(DEFAULT_TIMEOUT);
	}
	
	private Response noServiceAvailable() {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User service unavailable").build();
	}
	
}
