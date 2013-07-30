package se.mah.elis.external.web.usage.electricity;

import java.util.Calendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.services.electricity.ElectricityService;
import se.mah.elis.services.electricity.internal.DeviceResponse;
import se.mah.elis.services.electricity.internal.DeviceSetRequest;
import se.mah.elis.services.electricity.internal.UsageRequest;
import se.mah.elis.services.electricity.internal.Usage;

@Path("/usage/electricity")
@Produces({MediaType.APPLICATION_JSON})
public class ElectricityResource {
	
	private ServiceTracker electricityServiceTracker;
	
	public ElectricityResource(ServiceTracker tracker) {
		this.electricityServiceTracker = tracker;
	}
	
	@GET
	public String nothingHere() {
		return "nothing here";
	}
	
	/**
	 * Accepts JSON requests in the following format:
	 * {
		"deviceSets": [
			"id": "string"
		] 
	   }
	 *
	 * JSON response in the following format
	 * {
            	"usage": {
            		"timestamp": "",
            		"deviceSetUsage": [
            			"totalUsage": "",
            			"deviceSetId": "",
            			"device": {
							"type": "",
							"unit": "",
							"amount": ""
            			}            			
            		]
            	}
            }
	 *
	 * @return a Response object 
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response deviceSets(UsageRequest request) {
		Response response = null;
		System.out.println("Requested: " + request.deviceSets.get(0).id);
		// TODO: handle errors
		// using request token, find real user
		// User user = Authentication.validateToken(request.getHeader('elis-platform-auth')
		
		// get electricity service		
		ElectricityService service = null;
		try {
			service = (ElectricityService) electricityServiceTracker.waitForService(5000);
			if (service != null) {
				// create response object
				Usage usage = service.getDeviceSetUsage(request);
				response = Response.status(Response.Status.OK).entity(usage).build();
			} else {
				response = createError("Electricity usage service went away");
			}
		} catch (InterruptedException e) {
			response = createError("Could not find electricity usage service in time");
		}
		
		return response;
	}
	
	private Response createError(String msg) {
		System.out.println("created error message: " + msg);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(msg).build();
	}
}
