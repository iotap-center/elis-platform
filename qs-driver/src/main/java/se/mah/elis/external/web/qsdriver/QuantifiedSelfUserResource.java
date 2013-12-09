package se.mah.elis.external.web.qsdriver;

import java.util.ArrayList;
import java.util.List;

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

import se.mah.elis.services.qsdriver.internal.beans.DeleteUserResponse;
import se.mah.elis.services.qsdriver.internal.beans.ErrorResponse;
import se.mah.elis.services.qsdriver.internal.beans.GatewayUserBean;
import se.mah.elis.services.qsdriver.internal.beans.User;
import se.mah.elis.services.qsdriver.internal.beans.UserList;
import se.mah.elis.services.qsdriver.internal.beans.UserListResponse;
import se.mah.elis.services.qsdriver.internal.beans.UserResponse;

@Path("/user/")
@Produces(MediaType.APPLICATION_JSON)
public class QuantifiedSelfUserResource {
	
	@GET
	public Response getAllUsers() {
		
		// setup dummy user1
		final User user1 = new User();
		user1.userId = "1";
		user1.username = "Ollego";
		user1.firstName = "Olle";
		user1.lastName = "Svensson";
		user1.email = "olle.svensson@mah.se";
		
		// setup dummy user2
		final User user2 = new User();
		user2.userId = "2";
		user2.username = "Brollebo";
		user2.firstName = "Nils";
		user2.lastName = "Rågerstedt";
		user2.email = "min.mailn@mah.se";
		
		// setup dummy user3
		final User user3 = new User();
		user3.userId = "3";
		user3.username = "VolvoPower";
		user3.firstName = "Jensa";
		user3.lastName = "Karloff";
		user3.email = "volvo.240.turbo@mah.se";
		
		// setup empty dummy Gateway user.
		final GatewayUserBean emptGateUser = new GatewayUserBean();
		
		// setup dummy Gateway user.
		final GatewayUserBean gateUser = new GatewayUserBean();
		gateUser.id="5";
		gateUser.serviceName="Eon SmartHome";
		gateUser.serviceUserName="UserName";
		gateUser.servicePassword="Password";
		
		// Add gatewayUser to users 1 and 2. And an empty for 3.
		user1.gatewayUser = gateUser;
		user2.gatewayUser = gateUser;
		user3.gatewayUser = emptGateUser;

		
		//setup dummy userList
		UserList userList = new UserList();
		userList.users = new ArrayList<User>() {{add(user1);add(user2);add(user3);}};
		
		// setup dummy response
		final UserListResponse responseBean = new UserListResponse();
		responseBean.code = 200;
		responseBean.status = "OK";
		responseBean.response = userList;
		
		// return response
		return Response.status(Response.Status.OK).entity(responseBean).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewUser(User newUser){
		
		
		// setup dummy response
		final UserResponse responseBean = new UserResponse();
		responseBean.code = 201;
		responseBean.status = "Created";
		responseBean.response = newUser;
		
		int userId = Integer.parseInt(newUser.userId);
		
		// If userId entered is above 3.
		if (userId > 3){
			// New User is created
			return Response.status(Response.Status.CREATED).entity(responseBean).build();
		} else {
			// Else: Error ,status 409
			final ErrorResponse existingUserError = new ErrorResponse();
			return Response.status(Response.Status.CONFLICT).entity(existingUserError).build();
		}
		
	}
	
	@GET
	@Path("{id}")
	public Response getUser(@PathParam("id") String id) {
	
		// setup dummy users

		final User user1 = new User();
		user1.userId = id;
		user1.username = "Ollego";
		user1.firstName = "Olle";
		user1.lastName = "Svensson";
		user1.email = "olle.svensson@mah.se";
		
		// setup dummy user2
		final User user2 = new User();
		user2.userId = id;
		user2.username = "Brollebo";
		user2.firstName = "Nils";
		user2.lastName = "Rågerstedt";
		user2.email = "min.mailn@mah.se";
		
		// setup dummy user3
		final User user3 = new User();
		user3.userId = id;
		user3.username = "VolvoPower";
		user3.firstName = "Jensa";
		user3.lastName = "Karloff";
		user3.email = "volvo.240.turbo@mah.se";
		
		// setup dummy Gateway user
		final GatewayUserBean gateUser = new GatewayUserBean();
		gateUser.id="5";
		gateUser.serviceName="Eon SmartHome";
		gateUser.serviceUserName="UserName";
		gateUser.servicePassword="Password";
		
		// setup empty dummy Gateway user.
		final GatewayUserBean emptGateUser = new GatewayUserBean();

		user1.gatewayUser = gateUser;
		user2.gatewayUser = gateUser;
		user3.gatewayUser = emptGateUser;
		
		if (id.equals("1")) {
			// setup dummy response
			final UserResponse responseBean = new UserResponse();
			responseBean.code = 200;
			responseBean.status = "OK";
			responseBean.response = user1;
			return Response.status(Response.Status.OK).entity(responseBean).build(); 
		} else if (id.equals("2")) {
			// setup dummy response
			final UserResponse responseBean = new UserResponse();
			responseBean.code = 200;
			responseBean.status = "OK";
			responseBean.response = user2;
			return Response.status(Response.Status.OK).entity(responseBean).build();
		} else if (id.equals("3")) {
			// setup dummy response
			final UserResponse responseBean = new UserResponse();
			responseBean.code = 200;
			responseBean.status = "OK";
			responseBean.response = user3;
			return Response.status(Response.Status.OK).entity(responseBean).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("id") String id, User user) {
		
		// setup dummy response
		final UserResponse responseBean = new UserResponse();
		responseBean.code = 200;
		responseBean.status = "OK";
		responseBean.response = user;
		
		int paramId = Integer.parseInt(id);
		// If userId entered is 1-3.
		if (paramId < 4){
			// Return updated user
			return Response.status(Response.Status.OK).entity(responseBean).build();
		} else {
			final ErrorResponse existingUserError = new ErrorResponse();
			existingUserError.code = 404;
			existingUserError.status = "Error";
			existingUserError.errorType = "Not Found";
			existingUserError.errorDetail = "The requested URL was not found on this server.";
			return Response.status(Response.Status.NOT_FOUND).entity(existingUserError).build();
		}
		
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id") String id) {
		
		// Convert id from string to int.
		int userId = Integer.parseInt(id);
		
		// U can remove user 1,2 and 3. Otherwise u will get 404 Error.
		if (userId > 3){
			final ErrorResponse noUserResponse = new ErrorResponse();
			noUserResponse.errorType = "Not Found";
			noUserResponse.errorDetail = "The requested URL was not found on this server.";
			noUserResponse.status = "Error";
			noUserResponse.code = 404;
			noUserResponse.response = new User();
			
			return Response.status(Response.Status.NOT_FOUND).entity(noUserResponse).build();
			
		} else {
			
			// setup dummy response
			final DeleteUserResponse responseBean = new DeleteUserResponse();
			responseBean.code = 204;
			responseBean.status = "No Content";
			responseBean.response = new User();
			
			return Response.status(Response.Status.OK).entity(responseBean).build();

		}

				
	}
	
	@POST
	@Path("{id}/gateway")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response attachGatewayUser(@PathParam("id") String id, GatewayUserBean gatewayUserObject){

		
		int userId = Integer.parseInt(id);
		
		if (userId == 3){

			final User user3 = new User();
			user3.userId = "3";
			user3.username = "VolvoPower";
			user3.firstName = "Jensa";
			user3.lastName = "Karloff";
			user3.email = "volvo.240.turbo@mah.se";
			user3.gatewayUser = gatewayUserObject;
			
			// setup dummy response
			final UserResponse responseBean = new UserResponse();
			responseBean.code = 201;
			responseBean.status = "Created";
			responseBean.response = user3;
			
			return Response.status(Response.Status.CREATED).entity(responseBean).build();
			
		} else {
			final ErrorResponse existingUserError = new ErrorResponse();
			return Response.status(Response.Status.CONFLICT).entity(existingUserError).build();
		}
		
	}
	
	@DELETE
	@Path("{id}/gateway/{gatewayUserId}")
	public Response deleteGatewayUser(@PathParam("id") String id, @PathParam("gatewayUserId") String gateUseId) {
		
	
		if (id.equals("1") && gateUseId.equals("5")){
			final User user1 = new User();
			user1.userId = id;
			user1.username = "Ollego";
			user1.firstName = "Olle";
			user1.lastName = "Svensson";
			user1.email = "olle.svensson@mah.se";
			
			final UserResponse responseBean = new UserResponse();
			responseBean.code = 201;
			responseBean.status = "Created";
			responseBean.response = user1;
			
			return Response.status(Response.Status.CREATED).entity(responseBean).build();
		} else {
			final ErrorResponse existingUserError = new ErrorResponse();
			existingUserError.code = 404;
			existingUserError.status = "Error";
			existingUserError.errorType = "Not Found";
			existingUserError.errorDetail = "The requested URL was not found on this server.";
			return Response.status(Response.Status.NOT_FOUND).entity(existingUserError).build();
		}
		
	}
	
	
	
}


