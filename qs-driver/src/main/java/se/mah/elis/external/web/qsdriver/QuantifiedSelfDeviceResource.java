package se.mah.elis.external.web.qsdriver;

import java.awt.List;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.mah.elis.services.qsdriver.internal.beans.Device;
import se.mah.elis.services.qsdriver.internal.beans.DeviceResponse;
import se.mah.elis.services.qsdriver.internal.beans.DeviceSet;
import se.mah.elis.services.qsdriver.internal.beans.DeviceSetResponse;
import se.mah.elis.services.qsdriver.internal.beans.ErrorResponse;
import se.mah.elis.services.qsdriver.internal.beans.Location;
import se.mah.elis.services.qsdriver.internal.beans.Power;
import se.mah.elis.services.qsdriver.internal.beans.PowerResponse;

@Path("/device/")
@Produces({ MediaType.APPLICATION_JSON })
public class QuantifiedSelfDeviceResource {

	@GET
	public Response getAllDevices() {
		
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
		
		final DeviceSet deviceSet = new DeviceSet();
		deviceSet.id = "1";
		deviceSet.devices = new ArrayList<Device>(){{add(device1);}{add(device2);}{add(device3);}};


		final DeviceSetResponse response = new DeviceSetResponse();
		response.status = "OK";
		response.code = 200;
		response.response = deviceSet;

		// return response
		return Response.status(Response.Status.OK).entity(response).build();
	}

	@GET
	@Path("{id}")
	public Response getDevice(@PathParam("id") String id) {

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
		
		
		if (id.equals("1")) {
			// setup dummy response
			final DeviceResponse responseBean1 = new DeviceResponse();
			responseBean1.code = 200;
			responseBean1.status = "OK";
			responseBean1.response = device1;
			return Response.status(Response.Status.OK).entity(responseBean1).build(); 
		} else if (id.equals("2")) {
			// setup dummy response
			final DeviceResponse responseBean2 = new DeviceResponse();
			responseBean2.code = 200;
			responseBean2.status = "OK";
			responseBean2.response = device2;
			return Response.status(Response.Status.OK).entity(responseBean2).build();
		} else if (id.equals("3")) {
			// setup dummy response
			final DeviceResponse responseBean3 = new DeviceResponse();
			responseBean3.code = 200;
			responseBean3.status = "OK";
			responseBean3.response = device3;
			return Response.status(Response.Status.OK).entity(responseBean3).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDevice(@PathParam("id") String id, Device device) {
		
		// setup dummy response
		final DeviceResponse responseBean = new DeviceResponse();
		responseBean.code = 201;
		responseBean.status = "Created";
		responseBean.response = device;
		
		int paramId = Integer.parseInt(id);
		// If id entered is 1-3.
		if (paramId < 4){
			// Return updated user
			return Response.status(Response.Status.CREATED).entity(responseBean).build();
		} else {
			final ErrorResponse existingDeviceError = new ErrorResponse();
			existingDeviceError.code = 404;
			existingDeviceError.status = "Error";
			existingDeviceError.errorType = "Not Found";
			existingDeviceError.errorDetail = "The requested URL was not found on this server.";
			return Response.status(Response.Status.NOT_FOUND).entity(existingDeviceError).build();
		}

	}

	@GET
	@Path("{id}/power")
	public Response getDevicePower(@PathParam("id") String id) {


		int Id = Integer.parseInt(id);
		
		if (Id == 1){
			// Setup dummy power response
			Power power1 = new Power();
			power1.status = 1;
			power1.unit = "kWh";
			power1.amount = 12.4f;
			
			// setup dummy response
			final PowerResponse responseBean = new PowerResponse();
			responseBean.code = 200;
			responseBean.status = "Ok";
			responseBean.response = power1;

			return Response.status(Response.Status.OK).entity(responseBean).build();

		} else if (Id == 2){
			// Setup dummy power response
			Power power2 = new Power();
			power2.status = 1f;
			power2.unit = "kWh";
			power2.amount = 1.6f;
			
			// setup dummy response
			final PowerResponse responseBean = new PowerResponse();
			responseBean.code = 200;
			responseBean.status = "Ok";
			responseBean.response = power2;

			return Response.status(Response.Status.OK).entity(responseBean).build();
		} else if (Id == 3){
			// Setup dummy power response
			Power power3 = new Power();
			power3.status = 1f;
			power3.amount = 12f;
			power3.unit = "kWh";
			power3.amount = 32.2f;
			
			// setup dummy response
			final PowerResponse responseBean = new PowerResponse();
			responseBean.code = 200;
			responseBean.status = "Ok";
			responseBean.response = power3;

			return Response.status(Response.Status.OK).entity(responseBean).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		

	}

	@POST
	@Path("{id}/power")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setDevicePower(@PathParam("id") String id, Power power) {

		
		if (id.equals("1")) {
			
			// setup dummy response
			final PowerResponse responseBean = new PowerResponse();
			responseBean.code = 201;
			responseBean.status = "Created";
			responseBean.response.status = power.status;
			responseBean.response.amount = 12.4f;
			responseBean.response.unit = "kWh";

			return Response.status(Response.Status.CREATED).entity(responseBean).build();
			
		} else if (id.equals("2")) {
			// setup dummy response
			final PowerResponse responseBean = new PowerResponse();
			responseBean.code = 201;
			responseBean.status = "Created";
			responseBean.response.status = power.status;
			responseBean.response.amount = 1.6f;
			responseBean.response.unit = "kWh";

			return Response.status(Response.Status.CREATED).entity(responseBean).build();
		} else if (id.equals("3")) {
			// setup dummy response
			final PowerResponse responseBean = new PowerResponse();
			responseBean.code = 201;
			responseBean.status = "Created";
			responseBean.response.status = power.status;
			responseBean.response.amount = 32.2f;
			responseBean.response.unit = "kWh";

			return Response.status(Response.Status.CREATED).entity(responseBean).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
			
		}
		

	}

}
