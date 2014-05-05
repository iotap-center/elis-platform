package se.mah.elis.external.energy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.PowerSwitch;
import se.mah.elis.external.energy.beans.EnergyBean;
import se.mah.elis.external.energy.beans.EnergyBeanFactory;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/energy")
@Produces("application/json")
@Component(name = "ElisEnergyService", immediate = true)
@Service(value = EnergyService.class)
public class EnergyService {

	@Reference
	private UserService userService;
	
	@Reference
	private LogService log;

	private Gson gson;

	public EnergyService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}
	
	public EnergyService(UserService userService, LogService log) {
		this();
		this.userService = userService;
		this.log = log;
	}

	@GET
	@Path("/{puid}/now")
	public Response getCurrentEnergyConsumption(@PathParam("puid") String puid) {
		ResponseBuilder response = null;
		UUID uuid = null;
		
		logRequest("now", puid);
		
		try {
			uuid = UUID.fromString(puid);
		} catch (Exception e) {
			response = Response.status(Response.Status.BAD_REQUEST);
			logWarning("Bad UUID");
		}
		
		if (userService != null && uuid != null) {
			PlatformUser pu = userService.getPlatformUser(uuid);
			if (pu != null)
				response = buildCurrentEnergyConsumptionResponse("now", pu);
			else {
				response = Response.status(Response.Status.NOT_FOUND);
				logWarning("Could not find user: " + uuid.toString());
			}
		} else if (response == null) {
			response = Response.serverError();
			logError("User service not available");
		}

		return response.build();
	}

	private ResponseBuilder buildCurrentEnergyConsumptionResponse(
			String period, PlatformUser pu) {
		List<Device> availableEnergyMeters = getMeters(pu);
		EnergyBean bean = EnergyBeanFactory.create(availableEnergyMeters, period, pu);
		return Response.ok(gson.toJson(bean));
	}
	
	@GET
	@Path("/{puid}/hourly")
	public Response getHourlyEnergyConsumption(@PathParam("puid") String puid,
			@QueryParam("from") String from,
			@DefaultValue("") @QueryParam("to") String to) {
		ResponseBuilder response = null;
		UUID uuid = null;
		
		logRequest("hourly", puid, from, to);
		
		try {
			uuid = UUID.fromString(puid);
		} catch (Exception e) {
			response = Response.status(Response.Status.BAD_REQUEST);
			logWarning("Bad UUID");
		}
		
		if (userService != null && uuid != null) {
			PlatformUser pu = userService.getPlatformUser(uuid);
			if (pu != null) {
				response = buildPeriodicEnergyConsumptionResponse("hourly", from, to, pu);
			} else {
				response = Response.status(Response.Status.NOT_FOUND);
				logWarning("Could not find user: " + uuid.toString());
			}
		} else if (response == null) {
			response = Response.serverError();
			logError("User service not available");
		}
		
		return response.build();
	}

	private ResponseBuilder buildPeriodicEnergyConsumptionResponse(
			String period, String from, String to, PlatformUser pu) {
		List<Device> meters = getMeters(pu);
		logInfo("Building periodic response with " + meters.size() + " meters");
		EnergyBean bean = EnergyBeanFactory.create(meters, period, from, to, pu);
		logInfo("Bean: " + bean.toString());
		return Response.ok(gson.toJson(bean));
	}

	private List<Device> getMeters(PlatformUser pu) {
		User[] users = userService.getUsers(pu);
		List<Device> meters = getDevices(users);
		return meters;
	}

	private List<Device> getMeters(GatewayUser user) {
		List<Device> meters = new ArrayList<>();
		logInfo(user.getClass().getSimpleName() + " is looking for meters at " + user.getGateway().getId());
		for (Device device : user.getGateway()) {
			logInfo("Trying to look at a device: " + device);
			if (device instanceof ElectricitySampler && !(device instanceof PowerSwitch))
				meters.add(device);
		}
		return meters;
	}

	private List<Device> getDevices(User[] users) {
		List<Device> meters = new ArrayList<>();
		for (User user : users) {
			if (user instanceof GatewayUser) {
				logInfo("Found a GatewayUser");
				meters.addAll(getMeters((GatewayUser) user));
			}
		}
		logInfo("Found " + meters.size() + " devices");
		return meters;
	}
	
	private void logRequest(String endpoint, String puid, String from, String to) {
		logInfo("Request: /energy/" + puid + "/" + endpoint
				+ "?from=" + from + "&to=" + to);
	}
	
	private void logRequest(String endpoint, String puid) {
		logInfo("Request: /energy/" + puid + "/" + endpoint);
	}
	
	private void logInfo(String msg) {
		log.log(LogService.LOG_INFO, msg);
	}
	
	private void logWarning(String msg) {
		log.log(LogService.LOG_WARNING, msg);
	}
	
	private void logError(String msg) {
		log.log(LogService.LOG_ERROR, msg);
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
