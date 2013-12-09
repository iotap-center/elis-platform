package se.mah.elis.external.web.qsdriver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.mah.elis.services.qsdriver.internal.beans.Device;
import se.mah.elis.services.qsdriver.internal.beans.DeviceSet;
import se.mah.elis.services.qsdriver.internal.beans.Location;
import se.mah.elis.services.qsdriver.internal.beans.Sets;

@Path("/usage")
@Produces({ MediaType.APPLICATION_JSON })
public class QuantifiedSelfUsageResource {
	
	public QuantifiedSelfUsageResource() {
	}
	
	@GET
	@Path("/electricity")
	public Response get() {
		
		Device device = new Device();
		device.id = "1";
		device.name = "Lampa";
		device.location = new Location();
		
		DeviceSet deviceSet = new DeviceSet();
		deviceSet.id = "1";
		deviceSet.devices.add(device);
		
		Sets deviceSetList1 = new Sets();
		deviceSetList1.sets.add(deviceSet);
		
		return Response.status(Response.Status.OK).entity("hejsan").build();
	}


}
