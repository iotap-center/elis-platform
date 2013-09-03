/**
 * 
 */
package se.mah.elis.external.users;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import se.mah.elis.external.users.jaxbeans.PlatformUserBean;
import se.mah.elis.external.users.jaxbeans.GatewayUserBean;
import se.mah.elis.services.users.UserService;

/**
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@Path("/user")
public class UserWebService {

	private UserService userService;
	
	/**
	 * Creates an instance of this class.
	 * 
	 * @since 1.0
	 */
	public UserWebService() {
		userService = null;
	}
	
	/**
	 * Creates an instance of this class, fully equipped with a UserService and
	 * ready to go.
	 * 
	 * @param us The UserService to be used.
	 * @since 1.0
	 */
	public UserWebService(UserService us) {
		userService = us;
	}

	/**
	 * Sets the user service.
	 * 
	 * @param us
	 * @since 1.0
	 */
	public void setUserService(UserService us) {
		userService = us;
	}
	
	/**
	 * Gets all registered users.
	 * 
	 * @return All registered users.
	 * @since 1.0
	 */
	@GET
	public Response getUsers() {
		return null;
	}
	
	/**
	 * Adds a new platform user.
	 * 
	 * @return A response object.
	 * @since 1.0
	 */
	@POST
	@Consumes("application/json")
	public Response addUser(final PlatformUserBean input) {
		return null;
	}
	
	/**
	 * Gets a platform user.
	 * 
	 * @return A user response object.
	 * @since 1.0
	 */
	@GET
	@Path("/{userId}")
	public Response getUser(@PathParam("userId") String userId) {
		return null;
	}
	
	/**
	 * Updates a platform user.
	 * 
	 * @return A response object.
	 * @since 1.0
	 */
	@PUT
	@Consumes("application/json")
	@Path("/{userId}")
	public Response updateUser(@PathParam("userId") String userId,
						final PlatformUserBean input) {
		return null;
	}
	
	/**
	 * Removes a platform user from the system.
	 * 
	 * @return A response object.
	 * @since 1.0
	 */
	@DELETE
	@Path("/{userId}")
	public Response deleteUser(@PathParam("userId") String userId) {
		return null;
	}
	
	/**
	 * Couples a gateway and a platform user.
	 * 
	 * @return A response object.
	 * @since 1.0
	 */
	@POST
	@Consumes("application/json")
	@Path("/{userId}/gateway")
	public Response coupleGatewayWithUser(@PathParam("userId") String userId,
						final GatewayUserBean input) {
		return null;
	}
	
	/**
	 * Decouples a gateway and a platform user.
	 * 
	 * @return A response object.
	 * @since 1.0
	 */
	@DELETE
	@Path("/{userId}/gateway/{gatewayId}")
	public Response decoupleGatewayFromUser(
						@PathParam("userId") String userId,
						@PathParam("gatewayId") String gatewayId) {
		return null;
	}
}
