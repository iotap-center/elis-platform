package se.mah.elis.external.web.qsdriver;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.mah.elis.services.qsdriver.internal.Device;
import se.mah.elis.services.qsdriver.internal.DeviceSet;
import se.mah.elis.services.qsdriver.internal.Location;
import se.mah.elis.services.qsdriver.internal.Power;

@Path("/device")
@Produces({ MediaType.APPLICATION_JSON })
public class QuantifiedSelfDeviceResource {

	public QuantifiedSelfDeviceResource() {
	}

	@GET
	public Response getRootDeviceSet() {
		// setup dummy response
		DeviceSet rootSet = new DeviceSet();
		rootSet.id = "1";
		rootSet.devices = new ArrayList<String>() {
			{
				add("1");
			}
		};

		// return response
		return Response.status(Response.Status.OK).entity(rootSet).build();
	}

	@GET
	@Path("{id}")
	public Response getDevice(@PathParam("id") String id) {

		
		// Setup new Location, adding Kitchen.
		Location location = new Location();
		location.relative = new ArrayList<String>(){{add("Kitchen");}};
		
		// setup dummy device "Refrigerator"
		Device device = new Device();
		device.id = id;
		device.location=location;
		device.actions = new ArrayList<String>(){{add("String");};};
		device.name = "Refrigerator";

		return Response.status(Response.Status.OK).entity(device).build();
	}

	@PUT
	@Path("{id}")
	public Response updateDevice(@PathParam("id") String id) {
		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("{id}/power")
	public Response getDevicePower(@PathParam("id") String id) {

		// Setup dummy power response
		Power power = new Power();
		power.status = 1;
		power.amount = 34;
		power.unit = "kWh";

		return Response.status(Response.Status.OK).entity(power).build();
	}

	@POST
	@Path("{id}/power/")
	public Response setDevicePower(@PathParam("id") String id) {

		// Setup dummy power response
		Power power = new Power();
		power.status = 1;
		power.amount = 34;
		power.unit = "kWh";

		return Response.status(Response.Status.OK).entity(power + id).build();
	}

}
