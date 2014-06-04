package se.mah.elis.external.energy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.external.beans.helpers.ElisResponseBuilder;
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
	// TODO: make this endpoint accept device and device set ids as well.
	public Response getCurrentEnergyConsumption(@PathParam("puid") String puid) {
		Response response = null;
		UUID uuid = null;
		
		logRequest("now", puid);
		
		try {
			uuid = UUID.fromString(puid);
		} catch (Exception e) {
			response = ElisResponseBuilder.buildBadRequestResponse();
			logWarning("Bad UUID");
		}
		
		if (userService != null && uuid != null) {
			PlatformUser pu = userService.getPlatformUser(uuid);
			if (pu != null)
				response = buildCurrentEnergyConsumptionResponse("now", pu);
			else {
				response = ElisResponseBuilder.buildNotFoundResponse();
				logWarning("Could not find user: " + uuid.toString());
			}
		} else if (response == null) {
			response = ElisResponseBuilder.buildInternalServerErrorResponse();
			logError("User service not available");
		}

		return response;
	}

	private Response buildCurrentEnergyConsumptionResponse(
			String period, PlatformUser pu) {
		List<ElectricitySampler> availableEnergyMeters = getMeters(pu);
		EnergyBean bean = EnergyBeanFactory.create(availableEnergyMeters, period, pu);
		return ElisResponseBuilder.buildOKResponse(bean);
	}
	
	@GET
	@Path("/{puid}/hourly")
	// TODO: make this endpoint accept device and device set ids as well.
	public Response getHourlyEnergyConsumption(@PathParam("puid") String puid,
			@QueryParam("from") String from,
			@QueryParam("to") String to) {
		Response response = ElisResponseBuilder.buildInternalServerErrorResponse();
		UUID uuid = null;
		DateTime startDate = null;
		DateTime endDate = null;
		
		logRequest("hourly", puid, from, to);
		
		if (to == null || to.length() == 0) {
			to = "0";
		}
		
		try {
			// Try to parse the platform user id.
			uuid = UUID.fromString(puid);
		} catch (Exception e) {
			response = ElisResponseBuilder.buildBadRequestResponse();
			logWarning("Bad UUID");
			return response;
		}
		try {
			// Let's try to parse the dates
			startDate = new DateTime(Long.parseLong(from));
			endDate = new DateTime(Long.parseLong(to));
			
			// The end date should not be set in the future. If it is, let's
			// just look up times up until this instant. Also, if set to zero,
			// we should do pretty much the same.
			if (endDate.isAfter(DateTime.now()) || endDate.getMillis() == 0) {
				endDate = DateTime.now();
			}
			
			// Time paradoxes might be fun, but not in our code. if the end
			// date is set to be before the start date, let's just bail out.
			if (endDate.isBefore(startDate)) {
				throw new Exception();
			}
		} catch (Exception e) {
			response = ElisResponseBuilder.buildBadRequestResponse();
			logWarning("Bad dates");
			return response;
		}
		
		if (userService != null && uuid != null) {
			PlatformUser pu = userService.getPlatformUser(uuid);
			if (pu != null) {
				response = buildPeriodicEnergyConsumptionResponse("hourly", startDate, endDate, pu);
			} else {
				response = ElisResponseBuilder.buildNotFoundResponse();
				logWarning("Could not find user: " + uuid.toString());
			}
		} else if (response == null) {
			logError("User service not available");
		}
		
		return response;
	}

	private Response buildPeriodicEnergyConsumptionResponse(
			String period, DateTime startDate, DateTime endDate, PlatformUser pu) {
		List<ElectricitySampler> meters = getMeters(pu);
		logDebug("Building periodic response with " + meters.size() + " meters, starting at " + startDate + " and ending at " + endDate);
		EnergyBean bean = EnergyBeanFactory.create(meters, period, startDate, endDate, pu);
		
		List<ElectricitySample> samples = EnergyBeanFactory.pCollectSamplesFor(meters.get(0), startDate, endDate);
		
		return ElisResponseBuilder.buildOKResponse(bean);
	}

	private List<ElectricitySampler> getMeters(PlatformUser pu) {
		User[] users = userService.getUsers(pu);
		List<Device> devices = getDevices(users);
		List<ElectricitySampler> meters = new ArrayList<ElectricitySampler>();
		String info = "Seems to have found " + meters.size() + " meters:\n";
		
		for (Device device : devices) {
			if (device instanceof ElectricitySampler) {
				meters.add((ElectricitySampler) device);
				info += device.getName() + "; " + device.getDataId() + "\n";
			}
		}
		
		logDebug(info);
		
		return meters;
	}

	private List<Device> getMeters(GatewayUser user) {
		List<Device> meters = new ArrayList<>();
		for (Device device : user.getGateway()) {
			if (device instanceof ElectricitySampler)
				meters.add(device);
		}
		return meters;
	}

	private List<Device> getDevices(User[] users) {
		List<Device> meters = new ArrayList<>();
		for (User user : users) {
			if (user instanceof GatewayUser) {
				logDebug("Found a GatewayUser");
				meters.addAll(getMeters((GatewayUser) user));
			}
		}
		logDebug("Found " + meters.size() + " devices");
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
	
	private void logDebug(String msg) {
		log.log(LogService.LOG_DEBUG, msg);
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
