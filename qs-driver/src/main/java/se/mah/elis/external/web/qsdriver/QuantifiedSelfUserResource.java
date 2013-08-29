package se.mah.elis.external.web.qsdriver;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.mah.elis.services.qsdriver.internal.Device;
import se.mah.elis.services.qsdriver.internal.DeviceObject;
import se.mah.elis.services.qsdriver.internal.GatewayUsers;
import se.mah.elis.services.qsdriver.internal.User;
import se.mah.elis.services.qsdriver.internal.Users;


	
@Path("/user")
@Produces({MediaType.APPLICATION_JSON})
public class QuantifiedSelfUserResource {
	
	public QuantifiedSelfUserResource() {
	}
	
	@GET
	public Response getAllUsers() {
		// setup dummy users
		final User user = new User();
		user.id="1";
		user.username="Ollego";
		user.firstname="Olle";
		user.lastname="Svensson";
		user.email="olle.svensson@mah.se";
		
		//setup dummy users
		Users users = new Users();
		users.users = new ArrayList<User>() {{add(user);}};
		
		// setup dummy Gateway user
		final GatewayUsers gateUser = new GatewayUsers();
		gateUser.id="";
		gateUser.serviceName="Eon SmartHome";
		gateUser.serviceUserName="username";
		gateUser.servicePassword="password";
		user.gatewayUsers = new ArrayList<GatewayUsers>() {{add(gateUser);}};
			
		// return response
		return Response.status(Response.Status.OK).entity(users).build();
	}
	
	@POST
	public Response addNewUser(){
		//TODO// implement response to user
		return Response.status(Response.Status.OK).build();
	}
	
	
	@GET
	@Path("{id}")
	public Response getUser(@PathParam("id") String id) {
		
		// setup dummy users
		final User user = new User();
		user.id=id;
		user.username="EliteHaxxor";
		user.firstname="Nils";
		user.lastname="Ollesson";
		user.email="nils.ollesson@mah.se";
		
		// setup dummy Gateway user
		final GatewayUsers gateUser = new GatewayUsers();
		gateUser.id="5";
		gateUser.serviceName="Eon SmartHome";
		gateUser.serviceUserName="UserName";
		gateUser.servicePassword="Password";
		user.gatewayUsers = new ArrayList<GatewayUsers>() {{add(gateUser);}};
		
		return Response.status(Response.Status.OK).entity(user).build(); 
	}
	
	@PUT
	@Path("{id}")
	public Response updateUser(@PathParam("id") String id) {
	   return Response.status(Response.Status.OK).build();
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id") String id) {
		   return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("{id}/gateway")
	public Response attachGatewayUser(@PathParam("id") String id){
		return Response.status(Response.Status.OK).build();
	}
	
	@DELETE
	@Path("{id}/gateway/{gatewayUserId}")
	public Response deleteGatewayUser(@PathParam("id") String id, @PathParam("gatewayUserId") String gateUseId) {
		return Response.status(Response.Status.OK).build();
	}
	
	
	
}


