/**
 * 
 */
package se.mah.elis.external.users;

import java.lang.reflect.Field;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.service.command.Descriptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.osgi.service.command.CommandProcessor;

import se.mah.elis.external.users.jaxbeans.EnvelopeBean;
import se.mah.elis.external.users.jaxbeans.ErrorBean;
import se.mah.elis.external.users.jaxbeans.PlatformUserBean;
import se.mah.elis.external.users.jaxbeans.GatewayUserBean;
import se.mah.elis.external.users.jaxbeans.UserContainerBean;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.exceptions.NoSuchUserException;
import se.mah.elis.services.users.exceptions.UserExistsException;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserRecipe;

/**
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@Path("/user")
@Component(name = "Elis User web service")
@Service(value=UserWebService.class)
@org.apache.felix.scr.annotations.Properties({
	@Property(name = CommandProcessor.COMMAND_SCOPE, value = "elis"),
	@Property(name = CommandProcessor.COMMAND_FUNCTION, value = "uws") 
})
public class UserWebService {
	
	@Reference
	private UserService userService;
	
	@Reference
	private UserFactory userFactory;
	
	/**
	 * Creates an instance of this class.
	 * 
	 * @since 1.0
	 */
	public UserWebService() {
		userService = null;
		userFactory = null;
	}
	
	/**
	 * Creates an instance of this class, fully equipped with a UserService and
	 * and a UserFactory and ready to go.
	 * 
	 * @param us The UserService to be used.
	 * @param uf The UserFactory to be used.
	 * @since 1.0
	 */
	public UserWebService(UserService us, UserFactory uf) {
		userService = us;
		userFactory = uf;
	}
	
	@Descriptor("Lists current User Web Service configuration")
	public void uws() {
		String us = null, uf = null;

		if (null != userService) {
			us = userService.toString();
		}
		if (null != userFactory) {
			uf = userFactory.toString();
		}
		
		System.out.println("User Web Service running with:");
		System.out.println("UserService: " + us);
		System.out.println("UserFactory: " + uf);
	}

	/**
	 * Sets the user service.
	 * 
	 * @param us The UserService to be used.
	 * @since 1.0
	 */
	public void setUserService(UserService us) {
		userService = us;
	}
	
	public void bindUserService(UserService us) {
		System.out.println("Binding UserService");
		setUserService(us);
	}
	
	public void unbindUserService(UserService us) {
		System.out.println("Unbinding UserService");
		setUserService(null);
	}
	
	/**
	 * Sets the user factory.
	 * 
	 * @param uf The UserFactory to be used-
	 * @since 10.
	 */
	public void setUserFactory(UserFactory uf) {
		userFactory = uf;
	}
	
	public void bindUserFactory(UserFactory uf) {
		System.out.println("Binding UserFactory");
		setUserFactory(uf);
	}
	
	public void unbindUserFactory(UserFactory uf) {
		System.out.println("Unbinding UserFactory");
		setUserFactory(null);
	}
	
	/**
	 * Gets all registered users.
	 * 
	 * @return All registered users.
	 * @since 1.0
	 */
	@GET
	public Response getUsers() {
		Response response = null;
		EnvelopeBean envelope = new EnvelopeBean();
		UserContainerBean container = new UserContainerBean();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PlatformUserBean[] beans = null;
		PlatformUserBean bean = null;
		PlatformUserIdentifier id = null;
		
		PlatformUser[] pus = userService.getPlatformUsers();
		beans = new PlatformUserBean[pus.length];
		
		for (int i = 0; i < pus.length; i++) {
			bean = new PlatformUserBean();
			id = (PlatformUserIdentifier) pus[i].getIdentifier();
			bean.userId = Integer.toString(id.getId());
			bean.username = id.getUsername();
			bean.firstName = pus[i].getFirstName();
			bean.lastName = pus[i].getLastName();
			bean.email = pus[i].getEmail();
			
			beans[i] = bean;
			id = null;
			bean = null;
		}
		
		container.userList = beans;
		envelope.response = container;
		envelope.code = Status.OK.getStatusCode();
		envelope.status = Status.OK.getReasonPhrase();
		
		response = Response.ok(gson.toJson(envelope)).build();
		
		return response;
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
		Response response = null;
		EnvelopeBean envelope = new EnvelopeBean();
		UserContainerBean container = new UserContainerBean();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		UserRecipe recipe;
		PlatformUser pu;
		User u = null;
		Properties properties;
		
		// First of all, count on things being bad.
		response = buildBadRequestResponse(response);
		
		if (userService != null && userFactory != null) {
			pu = null;
			try {
				pu = userService.createPlatformUser(input.username,
													input.password);
				
				pu.setFirstName(input.firstName);
				pu.setLastName(input.lastName);
				pu.setEmail(input.email);
				
				userService.updatePlatformUser(pu);
				
				if (input.gatewayUser != null) {
					recipe = userFactory.getRecipe("GatewayUser", // TODO: This isn't generic at all.
										input.gatewayUser.serviceName);
					
					if (recipe != null) {
						try {
							properties = populateRecipe(input.gatewayUser, recipe);
							u = userFactory.build(recipe.getUserType(),
									recipe.getServiceName(), properties);
						} catch (UserInitalizationException e) {
							return buildBadRequestResponse(response);
						} catch (Exception e) {
							return buildInternalServerErrorResponse(response);
						}
					} else {
						
						return response;
					}
					
					userService.registerUserToPlatformUser(u, pu);
					input.gatewayUser.id = Integer.toString(u.getIdNumber());
				}
				
				input.password = null;
				container.user = input;
				
				envelope.status = Status.CREATED.getReasonPhrase();
				envelope.code = Status.CREATED.getStatusCode();
				envelope.response = container;
				
				response = Response.status(Status.CREATED)
						.entity(gson.toJson(envelope)).build();
			} catch (UserExistsException e) {
				response = buildConflictResponse(response);
			} catch (IllegalArgumentException e) {
				response = buildBadRequestResponse(response);
			} catch (NoSuchUserException e) {
				response = buildInternalServerErrorResponse(response);
			}
		} else {
			response = Response.serverError().build();
		}
		
		return response;
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
		Response response = null;
		EnvelopeBean envelope = new EnvelopeBean();
		UserContainerBean userContainer = new UserContainerBean();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		PlatformUser pu = userService.getPlatformUser(userId);
		
		if (pu != null) {
			PlatformUserIdentifier id =
					(PlatformUserIdentifier) pu.getIdentifier();
			PlatformUserBean bean = new PlatformUserBean();
			
			bean.userId = Integer.toString(id.getId());
			bean.username = id.getUsername();
			bean.firstName = pu.getFirstName();
			bean.lastName = pu.getLastName();
			bean.email = pu.getEmail();
			
			userContainer.user = bean;
			envelope.response = userContainer;
			envelope.code = Status.OK.getStatusCode();
			envelope.status = Status.OK.getReasonPhrase();
			
			response = Response.ok().entity(gson.toJson(envelope)).build();
		} else {
			response = buildNotFoundResponse(response);
		}
		
		return response;
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
		Response response = null;
		EnvelopeBean envelope = new EnvelopeBean();
		UserContainerBean container = new UserContainerBean();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PlatformUser pu = userService.getPlatformUser(userId);
		
		if (pu != null) {
			pu.setFirstName(input.firstName);
			pu.setLastName(input.lastName);
			pu.setEmail(input.email);
			
			input.password = null;
			
			try {
				userService.updatePlatformUser(pu);
			} catch (NoSuchUserException e) {
				response = buildInternalServerErrorResponse(response);
			}
			
			input.password = null;
			container.user = input;
			
			envelope.status = Status.OK.getReasonPhrase();
			envelope.code = Status.OK.getStatusCode();
			envelope.response = container;
			
			response = Response.ok().entity(gson.toJson(envelope)).build();
		} else {
			response = buildNotFoundResponse(response);
		}
		
		return response;
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
		Response response = null;

		PlatformUser pu = userService.getPlatformUser(userId);
		if (pu != null) {
			try {
				userService.deletePlatformUser(pu);
				response = buildNoContentResponse(response);
			} catch (NoSuchUserException e) {
				response = buildNotFoundResponse(response);
			}	
		} else {
			response = buildNotFoundResponse(response);
		}
		
		return response;
	}
	
	/**
	 * Couples a gateway and a platform user.
	 * 
	 * @return A response object.
	 * @since 1.0
	 */
	@POST
	@Consumes("application/json")
	@Path("/{pwUserId}/{usertype}/{userid}")
	public Response coupleGatewayWithUser(@PathParam("usertype") String type,
			@PathParam("userid") String userId,
			final GatewayUserBean input) {		
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PlatformUser pu = null;
		PlatformUserBean bean = new PlatformUserBean();
		EnvelopeBean envelope = new EnvelopeBean();
		UserContainerBean container = new UserContainerBean();
		User u = null;
		
		// First of all, count on things being bad.
		response = buildBadRequestResponse(response);
		
		// TODO: Fix this shit. Also, add bean stuff.
		String userType = Character.toUpperCase(type.charAt(0)) +
				type.substring(1) + "User";
		UserRecipe recipe = userFactory.getRecipe(userType, input.serviceName);
		
		if (recipe != null) {
			Properties properties;
			try {
				properties = populateRecipe(input, recipe);
			} catch (Exception e1) {
				return response; // TODO: Prettify this mess
			}
			
			try {
				pu = userService.getPlatformUser(userId);
				
				if (pu == null) {
					throw new NoSuchUserException();
				}
				
				u = userFactory.build(recipe.getUserType(),
						recipe.getServiceName(), properties);
				userService.registerUserToPlatformUser(u, pu);

				bean.userId = Integer.toString(((PlatformUserIdentifier) pu
						.getIdentifier()).getId());
				bean.username = ((PlatformUserIdentifier) pu.getIdentifier())
						.getUsername();
				bean.firstName = pu.getFirstName();
				bean.lastName = pu.getLastName();
				bean.email = pu.getEmail();
				bean.gatewayUser = input;
				input.id = Integer.toString(u.getIdNumber());
				
				container.user = bean;
				envelope.response = container;
				envelope.code = Status.OK.getStatusCode();
				envelope.status = Status.OK.getReasonPhrase();
				
				response = Response.status(200).entity(gson.toJson(envelope)).build();
			} catch (UserInitalizationException | NoSuchUserException e) {}
		}
		
		return response;
	}
	
	/**
	 * Decouples a gateway and a platform user.
	 * 
	 * @return A response object.
	 * @since 1.0
	 */
	@DELETE
	@Path("/{platformUserId}/{userType}/{userId}")
	public Response decoupleGatewayFromUser(
			@PathParam("platformUserId") String platformUserId,
			@PathParam("userId") String userId) {
		Response response = buildNotFoundResponse(null);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		UserContainerBean container = new UserContainerBean();
		EnvelopeBean envelope = new EnvelopeBean();
		PlatformUserBean bean = new PlatformUserBean();
		PlatformUser pu = userService.getPlatformUser(platformUserId);
		User u = null;
		
		if (pu != null) {
			u = userService.getUser(pu, Integer.parseInt(userId));
		}
		
		if (u != null) {
			try {
				userService.unregisterUserFromPlatformUser(u, pu);
				
				bean.userId = platformUserId;
				bean.username = ((PlatformUserIdentifier) pu.getIdentifier())
						.getUsername();
				bean.firstName = pu.getFirstName();
				bean.lastName = pu.getLastName();
				bean.email = pu.getEmail();
	
				// TODO: The stuff below is waaaay too GatewayUser centric. Also, mostly unimplemented.
	//			users = userService.getUsers(pu);
	//			if (users.length == 1) {
	//				GatewayUserBean gub = new GatewayUserBean();
	//				gub.id = Integer.toString(users[0].getIdNumber());
	//			}
	
				container.user = bean;
				envelope.response = container;
				envelope.code = Status.OK.getStatusCode();
				envelope.status = Status.OK.getReasonPhrase();
				
				response = Response.status(200).entity(gson.toJson(envelope))
						.build();
			} catch (NoSuchUserException e) {}
		}
		
		return response;
	}

	private Properties populateRecipe(final GatewayUserBean input,
			UserRecipe recipe)
			throws Exception {
		Properties properties = recipe.getProperties();
		String[] keys = properties.keySet()
				.toArray(new String[properties.keySet().size()]);
		
		for (String key : keys) {
			Field f = null;
			String payload = null;
			try {
				f = input.getClass().getField(key);
				payload = (String) f.get(input);
			} catch (NoSuchFieldException | SecurityException |
					IllegalArgumentException | IllegalAccessException e) {
				throw e;
			}
			if (payload != null) {
				properties.setProperty(key, payload);
			}
		}
		if (properties.getProperty("id") == "string") {
			properties.setProperty("id", "null");
		}
		return properties;
	}
	
	private Response buildBadRequestResponse(Response response) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.BAD_REQUEST.getStatusCode();
		((ErrorBean) envelope).errorType = Status.BAD_REQUEST
				.getReasonPhrase();
		((ErrorBean) envelope).errorDetail =
				"The request cannot be fulfilled due to bad syntax.";
		response = Response.status(Status.BAD_REQUEST)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	private Response buildNotFoundResponse(Response response) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.NOT_FOUND.getStatusCode();
		((ErrorBean) envelope).errorType = Status.NOT_FOUND.getReasonPhrase();
		((ErrorBean) envelope).errorDetail = "The requested URL was not found on this server.";
		response = Response.status(Status.NOT_FOUND)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	private Response buildInternalServerErrorResponse(Response response) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		((ErrorBean) envelope).errorType = Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
		((ErrorBean) envelope).errorDetail = "Someone set up us the bomb.";
		response = Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	private Response buildNoContentResponse(Response response) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = Status.NO_CONTENT.getReasonPhrase();
		envelope.code = Status.NO_CONTENT.getStatusCode();
		response = Response.status(Status.NO_CONTENT)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	private Response buildConflictResponse(Response response) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.CONFLICT.getStatusCode();
		((ErrorBean) envelope).errorType = Status.CONFLICT.getReasonPhrase();
		((ErrorBean) envelope).errorDetail = "The proposed URL already exists on this server.";
		response = Response.status(Status.CONFLICT)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
}