package se.mah.elis.external.energy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.UserService;

@Path("/energy")
@Produces("application/json")
@Component(name = "ElisEnergyService", immediate = true)
@Service(value = EnergyService.class)
public class EnergyService {

	@Reference
	private UserService userService;
	
	@Reference
	private LogService log;

	public EnergyService() {
		
	}
	
	public EnergyService(UserService userService, LogService log) {
		this();
		this.userService = userService;
		this.log = log;
	}

	@GET
	@Path("{puid}/now")
	public Response getCurrentEnergyConsumption(@PathParam("puid") String puid) {
		ResponseBuilder response = null;
		
		if (userService != null) {
			PlatformUser pu = userService.getPlatformUser(puid);
			if (pu != null)
				response = buildCurrentEnergyConsumptionResponse(pu);
			else
				response = Response.status(Response.Status.NOT_FOUND);	
		} else
			response = Response.serverError();

		return response.build();
	}

	private ResponseBuilder buildCurrentEnergyConsumptionResponse(
			PlatformUser pu) {
		return Response.noContent();
	}

	protected void unbindUserService() {
		this.userService = null;
	}

	protected void bindUserService(UserService userService) {
		this.userService = userService;
	}

	protected void unbindLog(LogService log) {
		this.log = null;
	}

	protected void bindLog(LogService log) {
		this.log = log;
	}
	
}
