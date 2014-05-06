/**
 * 
 */
package se.mah.elis.external.users;

import java.lang.reflect.Field;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.service.command.Descriptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.osgi.service.command.CommandProcessor;
import org.osgi.service.log.LogService;

import se.mah.elis.external.beans.EnvelopeBean;
import se.mah.elis.external.beans.ErrorBean;
import se.mah.elis.external.beans.helpers.ResponseBuilder;
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
 * @author "Johan Holmberg, Malm�� University"
 * @since 1.0
 */
@Path("/users")
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
	
	@Reference
	private LogService log;
	
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
		this(us, uf, null);
	}
	
	/**
	 * Create an instance with User Service, User Factory and Log Service
	 * @param us The UserService to be used.
	 * @param uf The UserFactory to be used.
	 * @param ls The LogService to be used. 
	 * @since 1.0
	 */
	public UserWebService(UserService us, UserFactory uf, LogService ls) {
		userService = us;
		userFactory = uf;
		log = ls;
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
		setUserService(us);
	}
	
	public void unbindUserService(UserService us) {
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
		setUserFactory(uf);
	}
	
	public void unbindUserFactory(UserFactory uf) {
		setUserFactory(null);
	}
	
	public void bindLog(LogService ls) {
		log = ls;
	}
	
	public void unbindLog(LogService ls) {
		log = null;
	}
	
	/**
	 * Gets all registered users.
	 * 
	 * @return All registered users.
	 * @since 1.0
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		Response response = null;
		EnvelopeBean envelope = new EnvelopeBean();
		UserContainerBean container = new UserContainerBean();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PlatformUserBean[] beans = null;
		PlatformUserBean bean = null;
		PlatformUserIdentifier id = null;
		
		logThis("GET /users");
		
		PlatformUser[] pus = userService.getPlatformUsers();
		beans = new PlatformUserBean[pus.length];
		
		for (int i = 0; i < pus.length; i++) {
			bean = new PlatformUserBean();
			id = (PlatformUserIdentifier) pus[i].getIdentifier();
			bean.userId = pus[i].getUserId().toString();
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(JAXBElement<PlatformUserBean> input) {
		return addUser(input.getValue());
	}
	
	/**
	 * Adds a new platform user.
	 * 
	 * @return A response object.
	 * @since 1.0
	 */
	public Response addUser(PlatformUserBean input) {
		Response response = null;
		EnvelopeBean envelope = new EnvelopeBean();
		UserContainerBean container = new UserContainerBean();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		UserRecipe recipe;
		PlatformUser pu;
		User u = null;
		Properties properties;
		
		logThis("POST /users");
		
		// First of all, count on things being bad.
		response = ResponseBuilder.buildBadRequestResponse();
		
		if (userService != null && userFactory != null) {
			logThis(input.username);
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
							return ResponseBuilder.buildBadRequestResponse();
						} catch (Exception e) {
							logError("UserFactory Could not build user - server error");
							return ResponseBuilder.buildInternalServerErrorResponse();
						}
					} else {
						
						return response;
					}
					
					userService.registerUserToPlatformUser(u, pu);
					input.userId = pu.getUserId().toString();
					input.gatewayUser.id = u.getUserId().toString();
					logThis("Created " + input.userId);
				}
				
				input.password = null;
				container.user = input;
				
				envelope.status = Status.CREATED.getReasonPhrase();
				envelope.code = Status.CREATED.getStatusCode();
				envelope.response = container;
				
				response = Response.status(Status.CREATED)
						.entity(gson.toJson(envelope)).build();
			} catch (UserExistsException e) {
				logWarning("User already exists: " + input.email);
				response = ResponseBuilder.buildConflictResponse();
			} catch (IllegalArgumentException e) {
				logWarning("Bad request: " + input.email);
				response = ResponseBuilder.buildBadRequestResponse();
			} catch (NoSuchUserException e) {
				response = ResponseBuilder.buildInternalServerErrorResponse();
			}
		} else {
			logError("No UserFactory or UserService available");
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("userId") String userId) {
		Response response = null;
		EnvelopeBean envelope = new EnvelopeBean();
		UserContainerBean userContainer = new UserContainerBean();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		logThis("GET /users/" + userId);
		
		PlatformUser pu = userService.getPlatformUser(UUID.fromString(userId));
		
		if (pu != null) {
			PlatformUserIdentifier id =
					(PlatformUserIdentifier) pu.getIdentifier();
			PlatformUserBean bean = new PlatformUserBean();
			
			bean.userId = pu.getUserId().toString();
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
			response = ResponseBuilder.buildNotFoundResponse();
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
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("userId") String userId,
			JAXBElement<PlatformUserBean> input) {
		return updateUser(userId, input.getValue());
	}

	public Response updateUser(String userId, PlatformUserBean input) {
		Response response = null;
		EnvelopeBean envelope = new EnvelopeBean();
		UserContainerBean container = new UserContainerBean();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PlatformUser pu = userService.getPlatformUser(UUID.fromString(userId));
		
		logThis("PUT /users/" + userId);
		
		if (pu != null) {
			pu.setFirstName(input.firstName);
			pu.setLastName(input.lastName);
			pu.setEmail(input.email);
			
			input.password = null;
			
			try {
				userService.updatePlatformUser(pu);
			} catch (NoSuchUserException e) {
				response = ResponseBuilder.buildInternalServerErrorResponse();
			}
			
			input.password = null;
			container.user = input;
			
			envelope.status = Status.OK.getReasonPhrase();
			envelope.code = Status.OK.getStatusCode();
			envelope.response = container;
			
			response = Response.ok().entity(gson.toJson(envelope)).build();
		} else {
			response = ResponseBuilder.buildNotFoundResponse();
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("userId") String userId) {
		Response response = null;
		
		logThis("DELETE /users/" + userId);
		
		PlatformUser pu = userService.getPlatformUser(UUID.fromString(userId));
		if (pu != null) {
			try {
				userService.deletePlatformUser(pu);
			} catch (NoSuchUserException e) {}
		}
		response = ResponseBuilder.buildNotFoundResponse();
		
		return response;
	}
	
	/**
	 * Couples a gateway and a platform user.
	 * 
	 * @return A response object.
	 * @since 1.0
	 */
	@POST
	@Path("/{pwUserId}/{usertype}/{userid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response coupleGatewayWithUser(@PathParam("usertype") String type,
			@PathParam("userid") String userId,
			JAXBElement<GatewayUserBean> input) {
		return coupleGatewayWithUser(type, userId, input.getValue());
	}
	
	public Response coupleGatewayWithUser(String type, String userId,
			GatewayUserBean input) {		
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PlatformUser pu = null;
		PlatformUserBean bean = new PlatformUserBean();
		EnvelopeBean envelope = new EnvelopeBean();
		UserContainerBean container = new UserContainerBean();
		User user = null;
		
		logThis("POST /users/" + type + "/" + userId);
		
		// First of all, count on things being bad.
		response = ResponseBuilder.buildBadRequestResponse();
		
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
				pu = userService.getPlatformUser(UUID.fromString(userId));
				
				if (pu == null) {
					throw new NoSuchUserException();
				}
				
				user = userFactory.build(recipe.getUserType(),
						recipe.getServiceName(), properties);
				userService.registerUserToPlatformUser(user, pu);

				bean.userId = pu.getUserId().toString();
				bean.username = ((PlatformUserIdentifier) pu.getIdentifier())
						.getUsername();
				bean.firstName = pu.getFirstName();
				bean.lastName = pu.getLastName();
				bean.email = pu.getEmail();
				bean.gatewayUser = input;
				input.id = user.getUserId().toString();
				
				container.user = bean;
				envelope.response = container;
				envelope.code = Status.OK.getStatusCode();
				envelope.status = Status.OK.getReasonPhrase();
				
				response = Response.status(200).entity(gson.toJson(envelope)).build();
			} catch (UserInitalizationException | NoSuchUserException e) {}
		} else {
			logWarning("No such recipe: " + input.serviceName);
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response decoupleGatewayFromUser(
			@PathParam("platformUserId") String platformUserId,
			@PathParam("userId") String userId) {
		Response response = ResponseBuilder.buildNotFoundResponse();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		UserContainerBean container = new UserContainerBean();
		EnvelopeBean envelope = new EnvelopeBean();
		PlatformUserBean bean = new PlatformUserBean();
		PlatformUser pu = userService.getPlatformUser(UUID.fromString(platformUserId));
		User u = null;
		
		logThis("DELETE /users/" + platformUserId + "/:userType/" + userId);
		
		if (pu != null) {
			u = userService.getUser(pu, UUID.fromString(userId));
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
			if (properties.containsKey("id") &&
					properties.get("id") instanceof String) {
				properties.put("id", UUID.fromString((String) properties.get("id")));
			}
		}
		
		return properties;
	}
	
	private void logThis(String msg) {
		logThis(LogService.LOG_INFO, msg);
	}

	private void logThis(int level, String msg) {
		if (log != null)
			log.log(level, msg);
	}
	
	private void logWarning(String msg) {
		logThis(LogService.LOG_WARNING, msg);
	}
	
	private void logError(String msg) {
		logThis(LogService.LOG_ERROR, msg);
	}
}