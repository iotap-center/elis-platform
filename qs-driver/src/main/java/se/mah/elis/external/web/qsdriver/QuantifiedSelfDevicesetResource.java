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

import se.mah.elis.services.qsdriver.internal.DeviceSet;
import se.mah.elis.services.qsdriver.internal.DeviceSetList;


@Path("/deviceset")
@Produces({MediaType.APPLICATION_JSON})
public class QuantifiedSelfDevicesetResource {

	public QuantifiedSelfDevicesetResource() {
	}
	
	@GET
	public Response getDeviceSetList(){

		// Dummy DeviceSet
		final DeviceSet deviceSet = new DeviceSet();
		deviceSet.id = "1";
		deviceSet.devices = new ArrayList<String>() {{add("1");}};
		
		// Dummy DeviceSetList
		DeviceSetList deviceSetList = new DeviceSetList();
		deviceSetList.sets = new ArrayList<DeviceSet>() {{add(deviceSet);}};
		
		return Response.status(Response.Status.OK).entity(deviceSetList).build();
	} 
	
	@POST
	public Response addDeviceSet() {
		return Response.status(Response.Status.OK).build();	
	}
	
	@GET
	@Path("{id}")
	public Response getDeviceSet(@PathParam("id") String deviceSetId) {
		
		// Dummy DeviceSet with list of devices
		DeviceSet deviceSet = new DeviceSet();
		deviceSet.id = deviceSetId;
		deviceSet.devices = new ArrayList<String>() {{add("1");}};
		
		return Response.status(Response.Status.OK).entity(deviceSet).build();
	}
	
	@PUT
	@Path("{id}")
	public Response updateDeviceSet(@PathParam("id") String deviceSetId) {
		return Response.status(Response.Status.OK).build();	
	}
	
	@DELETE
	@Path("{id}")
	public Response removeDeviceSet(@PathParam("id") String deviceSetId) {
		return Response.status(Response.Status.OK).build();	
	}
	
	@POST
	@Path("{id}/{devId}")
	public Response addDeviceToDeviceSet(@PathParam("id") String deviceSetId, @PathParam("devId") String deviceId ) {
		return Response.status(Response.Status.OK).build();	
	}
	
	@DELETE
	@Path("{id}/{devId}")
	public Response removeDeviceFromDeviceSet(@PathParam("id") String deviceSetId, @PathParam("devId") String deviceId) {
		return Response.status(Response.Status.OK).entity("delete deviceSetId:"+deviceSetId+" and device id:"+deviceId+"\n").build();	
	}


}
