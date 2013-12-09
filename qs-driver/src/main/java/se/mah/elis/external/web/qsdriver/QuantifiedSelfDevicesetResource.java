package se.mah.elis.external.web.qsdriver;

import java.util.ArrayList;

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

import se.mah.elis.services.qsdriver.internal.beans.Device;
import se.mah.elis.services.qsdriver.internal.beans.DeviceSet;
import se.mah.elis.services.qsdriver.internal.beans.DeviceSetResponse;
import se.mah.elis.services.qsdriver.internal.beans.ErrorResponse;
import se.mah.elis.services.qsdriver.internal.beans.Location;
import se.mah.elis.services.qsdriver.internal.beans.Sets;
import se.mah.elis.services.qsdriver.internal.beans.SetsResponse;
import se.mah.elis.services.qsdriver.internal.beans.User;


@Path("/deviceset")
@Produces({MediaType.APPLICATION_JSON})
public class QuantifiedSelfDevicesetResource {

	@GET
	public Response getDeviceSetList(){
		
		// Setup new Location, adding Bedroom.
		final Location location1 = new Location();
		location1.relative = new ArrayList<String>(){{add("Bedroom");}};
		
		// Setup new Location, adding Kitchen.
		final Location location2 = new Location();
		location2.relative = new ArrayList<String>(){{add("Kitchen");}};
		
		// Setup new Location, adding Living room and Hallway.
		final Location location3 = new Location();
		location3.relative = new ArrayList<String>(){{add("Living room");{add("Hallway");}}};

		// setup dummy device "Lamp"
		final Device device1 = new Device();
		device1.id = "1";
		device1.name = "Lamp";
		device1.location = location1;
		device1.actions = new ArrayList<String>(){{add("turnOff");}{add("turnOn");{add("toggle");};}};
		
		// setup dummy device "Thermometer"
		final Device device2 = new Device();
		device2.id = "2";
		device2.name = "Thermometer";
		device2.location = location2;
		device2.actions = new ArrayList<String>(){{add("getCurrentTemperature");}};
		
		// setup dummy device "Lamp"
		final Device device3 = new Device();
		device3.id = "3";
		device3.name = "Motion sensor";
		device3.location = location3;
		
		// Dummy DeviceSet1
		final DeviceSet deviceSet = new DeviceSet();
		deviceSet.id = "1";
		deviceSet.devices = new ArrayList<Device>(){{add(device1);}};
		
		// Dummy DeviceSet2
		final DeviceSet deviceSet2 = new DeviceSet();
		deviceSet2.id = "2";
		deviceSet2.devices = new ArrayList<Device>(){{add(device2);{add(device3);}}};
		
		// Dummy DeviceSetList
		Sets deviceSetList = new Sets();
		deviceSetList.sets = new ArrayList<DeviceSet>() {{add(deviceSet);{add(deviceSet2);};}};
		
		final SetsResponse response = new SetsResponse();
		response.status = "OK";
		response.code = 200;
		response.response = deviceSetList;
		
		return Response.status(Response.Status.OK).entity(response).build();
	} 
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDeviceSet(DeviceSet deviceSet) {
		
		
		final DeviceSetResponse response = new DeviceSetResponse();
		response.status = "Created";
		response.code = 201;
		response.response = deviceSet;
		

		return Response.status(Response.Status.CREATED).entity(response).build();	
	}
	
	@GET
	@Path("{id}")
	public Response getDeviceSet(@PathParam("id") String deviceSetId) {
		
		// Setup new Location, adding Bedroom.
		final Location location1 = new Location();
		location1.relative = new ArrayList<String>(){{add("Bedroom");}};
		
		// Setup new Location, adding Kitchen.
		final Location location2 = new Location();
		location2.relative = new ArrayList<String>(){{add("Kitchen");}};
		
		// Setup new Location, adding Living room and Hallway.
		final Location location3 = new Location();
		location3.relative = new ArrayList<String>(){{add("Living room");{add("Hallway");}}};

		// setup dummy device "Lamp"
		final Device device1 = new Device();
		device1.id = "1";
		device1.name = "Lamp";
		device1.location = location1;
		device1.actions = new ArrayList<String>(){{add("turnOff");}{add("turnOn");{add("toggle");};}};
		
		// setup dummy device "Thermometer"
		final Device device2 = new Device();
		device2.id = "2";
		device2.name = "Thermometer";
		device2.location = location2;
		device2.actions = new ArrayList<String>(){{add("getCurrentTemperature");}};
		
		// setup dummy device "Lamp"
		final Device device3 = new Device();
		device3.id = "3";
		device3.name = "Motion sensor";
		device3.location = location3;
		
		// Dummy DeviceSet1
		final DeviceSet deviceSet = new DeviceSet();
		deviceSet.id = deviceSetId;
		deviceSet.devices = new ArrayList<Device>(){{add(device1);}};
		
		// Dummy DeviceSet2
		final DeviceSet deviceSet2 = new DeviceSet();
		deviceSet2.id = deviceSetId;
		deviceSet2.devices = new ArrayList<Device>(){{add(device2);{add(device3);}}};
		
		
		if (deviceSetId.equals("1")) {
			// setup dummy response
			final DeviceSetResponse responseBean1 = new DeviceSetResponse();
			responseBean1.code = 200;
			responseBean1.status = "OK";
			responseBean1.response = deviceSet;
			return Response.status(Response.Status.OK).entity(responseBean1).build(); 
		} else if (deviceSetId.equals("2")) {
			// setup dummy response
			final DeviceSetResponse responseBean2 = new DeviceSetResponse();
			responseBean2.code = 200;
			responseBean2.status = "OK";
			responseBean2.response = deviceSet2;
			return Response.status(Response.Status.OK).entity(responseBean2).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
	}
	
	@PUT
	@Path("{id}")
	public Response updateDeviceSet(@PathParam("id") String deviceSetId, DeviceSet deviceSet) {
		
		if (deviceSetId.equals("1") || deviceSetId.equals("2")){
			// setup dummy response
			final DeviceSetResponse response = new DeviceSetResponse();
			response.code = 201;
			response.status = "Created";
			response.response = deviceSet;
			
			return Response.status(Response.Status.CREATED).entity(response).build();
		} else {
			final ErrorResponse noExistingDeviceSetError = new ErrorResponse();
			noExistingDeviceSetError.code = 404;
			noExistingDeviceSetError.status = "Error";
			noExistingDeviceSetError.errorType = "Not Found";
			noExistingDeviceSetError.errorDetail = "The requested URL was not found on this server.";
			return Response.status(Response.Status.NOT_FOUND).entity(noExistingDeviceSetError).build();
		}
		
		
			
	}
	
	@DELETE
	@Path("{id}")
	public Response removeDeviceSet(@PathParam("id") String deviceSetId) {
		if (deviceSetId.equals("1") || deviceSetId.equals("2")){
			
			final DeviceSetResponse deviceSetResponse = new DeviceSetResponse();
			deviceSetResponse.code = 204;
			deviceSetResponse.status = "No Content";
			deviceSetResponse.response = new DeviceSet();
			
			return Response.status(Response.Status.OK).entity(deviceSetResponse).build();
			
		} else {
			
			final ErrorResponse noDeviceSetResponse = new ErrorResponse();
			noDeviceSetResponse.errorType = "Not Found";
			noDeviceSetResponse.errorDetail = "The requested URL was not found on this server.";
			noDeviceSetResponse.status = "Error";
			noDeviceSetResponse.code = 404;
			noDeviceSetResponse.response = new User(); // Change response to empty DeviceSet.
			
			return Response.status(Response.Status.NOT_FOUND).entity(noDeviceSetResponse).build();
		}
		
		
	}
	
	@POST
	@Path("{id}/{devId}")
	public Response addDeviceToDeviceSet(@PathParam("id") String deviceSetId, @PathParam("devId") String deviceId ) {
		
		if (deviceSetId.equals("1") && deviceId.equals("4")){
			// Setup new Location, adding Bedroom.
			final Location location1 = new Location();
			location1.relative = new ArrayList<String>(){{add("Bedroom");}};
			
			// setup dummy device "Lamp"
			final Device device1 = new Device();
			device1.id = "1";
			device1.name = "Lamp";
			device1.location = location1;
			device1.actions = new ArrayList<String>(){{add("turnOff");}{add("turnOn");{add("toggle");};}};
			
			// setup new dummy device "Ceiling fan"
			final Device device2 = new Device();
			device2.id = "4";
			device2.name = "Ceiling fan";
			device2.location = location1;
			device2.actions = new ArrayList<String>(){{add("toggle");}};
			
			// Dummy DeviceSet1
			final DeviceSet deviceSet = new DeviceSet();
			deviceSet.id = "1";
			deviceSet.devices = new ArrayList<Device>(){{add(device1);{add(device2);}}};
			
			final DeviceSetResponse response = new DeviceSetResponse();
			response.status = "Created";
			response.code = 201;
			response.response = deviceSet;
			
			return Response.status(Response.Status.CREATED).entity(response).build();	
		} else {
			final ErrorResponse deviceToDeviceSetNotPossible = new ErrorResponse();
			deviceToDeviceSetNotPossible.code = 400;
			deviceToDeviceSetNotPossible.status = "Error";
			return Response.status(Response.Status.BAD_REQUEST).entity(deviceToDeviceSetNotPossible).build();	
		}

	}
	
	@DELETE
	@Path("{id}/{devId}")
	public Response removeDeviceFromDeviceSet(@PathParam("id") String id, @PathParam("devId") String devId) {
		
		if (id.equals("1") && devId.equals("4")){

			// Setup new Location, adding Bedroom.
			final Location location1 = new Location();
			location1.relative = new ArrayList<String>(){{add("Bedroom");}};
			
			// setup dummy device "Lamp"
			final Device device1 = new Device();
			device1.id = "1";
			device1.name = "Lamp";
			device1.location = location1;
			device1.actions = new ArrayList<String>(){{add("turnOff");}{add("turnOn");{add("toggle");};}};
			
			// Dummy DeviceSet1
			final DeviceSet deviceSet = new DeviceSet();
			deviceSet.id = "1";
			deviceSet.devices = new ArrayList<Device>(){{add(device1);}};
			
			final DeviceSetResponse response = new DeviceSetResponse();
			response.status = "No Content";
			response.code = 204;
			response.response = deviceSet;
			
			return Response.status(Response.Status.CREATED).entity(response).build();
			
		} else {
			final ErrorResponse deviceToDeviceSetNotPossible = new ErrorResponse();
			deviceToDeviceSetNotPossible.code = 400;
			deviceToDeviceSetNotPossible.status = "Error";
			return Response.status(Response.Status.BAD_REQUEST).entity(deviceToDeviceSetNotPossible).build();	
		}
	}


}
