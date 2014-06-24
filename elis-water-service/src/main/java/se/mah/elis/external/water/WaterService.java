package se.mah.elis.external.water;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.joda.time.Instant;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.WaterMeterSampler;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.WaterSample;
import se.mah.elis.external.beans.helpers.ElisResponseBuilder;
import se.mah.elis.external.water.beans.WaterBean;
import se.mah.elis.external.water.beans.WaterBeanFactory;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This is a HTTP service to retrieve water data statistics.
 * 
 * @author Marcus Ljungblad
 * @author Johan Holmberg
 * @version 1.0
 * @since 1.0 
 *
 */
@Path("/water")
@Produces("application/json")
@Component(name = "ElisWaterService", immediate = true)
@Service(value = WaterService.class)
public class WaterService {

	private static final String QUERY_PERIOD_NOW = "now";
	private static final String QUERY_PERIOD_DAILY = "daily";
	private static final String QUERY_PERIOD_WEEKLY = "weekly";
	private static final String QUERY_PERIOD_MONTHLY = "monthly";

	private Gson gson;

	@Reference
	private UserService userService;
	
	@Reference
	private LogService log;

	public WaterService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}
	
	public WaterService(UserService us, LogService log) {
		this();
		this.userService = us;
		this.log = log;
	}

	/**
	 * Gets the current water meter reading, i.e., how much water has been 
	 * consumed since the meter was last reset. Resetting cannot be done through 
	 * the Elis platform.  
	 * 
	 * @param puid
	 * @return
	 */
	@GET
	@Path("/{puid}/now")
	public Response getCurrentWaterConsumption(@PathParam("puid") String puid) {
		Response response = null;
		UUID uuid = null;
		
		logRequest("now", puid);

		try {
			uuid = UUID.fromString(puid);
		} catch (Exception e) {
			response = ElisResponseBuilder.buildBadRequestResponse();
			logWarning("Bad UUID");
		}

		if (isAvailable(userService) && uuid != null) {
			PlatformUser pu = userService.getPlatformUser(uuid);
			if (pu != null) {
				response = buildCurrentWaterConsumptionResponseFrom(pu);
			} else {
				response = ElisResponseBuilder.buildNotFoundResponse();
				logWarning("Could not find user: " + uuid.toString());
			}
		} else if (response == null) {
			response = ElisResponseBuilder.buildInternalServerErrorResponse();
			logError("User service not available");
		}

		return response;
	}

	private boolean isAvailable(Object o) {
		return o != null;
	}

	private Response buildCurrentWaterConsumptionResponseFrom(
			PlatformUser pu) {
		Response response;
		List<WaterMeterSampler> waterMeters = waterMetersForUser(pu);
		Map<String, List<WaterSample>> samples = getCurrentSamplesForMeters(waterMeters);
		WaterBean waterConsumptionBean = buildWaterBean(samples, QUERY_PERIOD_NOW, pu);
		response = ElisResponseBuilder.buildOKResponse(waterConsumptionBean);
		
		return response;
	}
	
	private Map<String, List<WaterSample>> getCurrentSamplesForMeters(
			List<WaterMeterSampler> waterMeters) {
		Map<String, List<WaterSample>> samples = new HashMap<>();
		
		for (WaterMeterSampler sampler : waterMeters) {
			tryAdd(samples, sampler);
		}
		
		return samples;
	}

	/**
	 * Get water data usage aggregated by day between two timestamps. 
	 * 
	 * @param puid
	 * @param from as unix timestamp
	 * @param to as unix timestamp
	 * @return
	 */
	@GET
	@Path("/{puid}/daily")
	public Response getDailyWaterConsumption(
			@PathParam("puid") String puid,
			@QueryParam("from") String from,
			@DefaultValue("") @QueryParam("to") String to) {
		ResponseBuilder response = null;
		UUID uuid = null;
		DateTime fromDt = null;
		DateTime toDt = null;
		
		logRequest("daily", puid, from, to);
		
		try {
			uuid = UUID.fromString(puid);
		} catch (Exception e) {
			logWarning("UUID is malformed");
		}
		
		try {
			fromDt = parseFromDate(from);
			toDt = parseToDate(to);
		} catch (NumberFormatException nfe) {
			logWarning("Date is in the future or malformed");
		}

		
		if (isAvailable(userService)) { 
			if (isValidRequest(uuid, fromDt, toDt)) {
				PlatformUser pu = userService.getPlatformUser(uuid);
				if (pu != null)
					response = buildDailyWaterConsumptionResponseFrom(pu, fromDt, toDt);
				else {
					response = Response.status(Response.Status.NOT_FOUND);
					logWarning("Could not find user: " + uuid.toString());
				}
			} else {
				response = Response.status(Response.Status.BAD_REQUEST);
			}
		} else if (response == null) {
			response = Response.serverError();
			logError("User service not available");
		}
		
		return response.build();
	}

	private ResponseBuilder buildDailyWaterConsumptionResponseFrom(
			PlatformUser pu, DateTime fromDt, DateTime toDt) {
		
		ResponseBuilder response;
		List<WaterMeterSampler> waterMeters = waterMetersForUser(pu);
		Map<String, List<WaterSample>> samples = getDailySamplesForMeters(
				waterMeters, fromDt, toDt);
		WaterBean waterConsumptionBean = buildWaterBean(samples, QUERY_PERIOD_DAILY, pu);
		response = Response.ok(gson.toJson(waterConsumptionBean));
		return response;
	}
	
	private Map<String, List<WaterSample>> getDailySamplesForMeters(
			List<WaterMeterSampler> waterMeters, DateTime fromDt, DateTime toDt) {
		Map<String, List<WaterSample>> samples = new HashMap<String, List<WaterSample>>();

		while (!fromDt.isAfter(toDt)) {
			for (WaterMeterSampler sampler : waterMeters) {
				tryAddRange(samples, sampler, fromDt, fromDt.plusDays(1));
			}
			fromDt = fromDt.plusDays(1);
			
			if (fromDt.equals(toDt))
				break;
		}

		return samples;
	}
	
	/**
	 * Get water data usage aggregated by week between two timestamps. 
	 * 
	 * @param puid
	 * @param from as unix timestamp
	 * @param to as unix timestamp
	 * @return
	 */
	@GET
	@Path("/{puid}/weekly")
	public Response getWeeklyWaterConsumption(
			@PathParam("puid") String puid,
			@QueryParam("from") String from,
			@DefaultValue("") @QueryParam("to") String to) {
		ResponseBuilder response = null;
		UUID uuid = null;
		DateTime fromDt = null;
		DateTime toDt = null;
		
		logRequest("weekly", puid, from, to);
		
		try {
			uuid = UUID.fromString(puid);
		} catch (Exception e) {
			logWarning("UUID is malformed");
		}
		
		try {
			fromDt = parseFromDate(from);
			toDt = parseToDate(to);
		} catch (NumberFormatException nfe) {
			logWarning("Date is in the future or malformed");
		}

		
		if (isAvailable(userService)) { 
			if (isValidRequest(uuid, fromDt, toDt)) {
				PlatformUser pu = userService.getPlatformUser(uuid);
				if (pu != null)
					response = buildWeeklyWaterConsumptionResponseFrom(pu, fromDt, toDt);
				else {
					logWarning("Could not find user: " + uuid.toString());
					response = Response.status(Response.Status.NOT_FOUND);
				}
			} else {
				response = Response.status(Response.Status.BAD_REQUEST);
			}
		} else if (response == null) {
			response = Response.serverError();
			logError("User service not available");
		}
		
		return response.build();
	}

	private ResponseBuilder buildWeeklyWaterConsumptionResponseFrom(
			PlatformUser pu, DateTime from, DateTime to) {
		ResponseBuilder response;
		List<WaterMeterSampler> waterMeters = waterMetersForUser(pu);
		Map<String, List<WaterSample>> samples = getWeeklySamplesForMeters(
				waterMeters, from, to);
		WaterBean waterConsumptionBean = buildWaterBean(samples, QUERY_PERIOD_WEEKLY, pu);
		response = Response.ok(gson.toJson(waterConsumptionBean));
		return response;
	}
	
	private Map<String, List<WaterSample>> getWeeklySamplesForMeters(
			List<WaterMeterSampler> waterMeters, DateTime fromDt, DateTime toDt) {
		Map<String, List<WaterSample>> samples = new HashMap<String, List<WaterSample>>();
		
		while (!fromDt.isAfter(toDt)) {
			for (WaterMeterSampler sampler : waterMeters) {
				tryAddRange(samples, sampler, fromDt, fromDt.plusDays(7));
			}
			fromDt = fromDt.plusDays(7);
			
			if (fromDt.equals(toDt))
				break;
		}

		return samples;
	}

	/**
	 * Get water data usage aggregated by month between two timestamps. 
	 * 
	 * @param puid
	 * @param from as unix timestamp
	 * @param to as unix timestamp
	 * @return
	 */
	@GET
	@Path("/{puid}/monthly")
	public Response getMonthlyWaterConsumption(
			@PathParam("puid") String puid,
			@QueryParam("from") String from,
			@DefaultValue("") @QueryParam("to") String to) {
		ResponseBuilder response = null;
		UUID uuid = null;
		DateTime fromDt = null;
		DateTime toDt = null;
		
		logRequest("monthly", puid, from, to);
		
		try {
			uuid = UUID.fromString(puid);
		} catch (Exception e) {
			logWarning("UUID is malformed");
		}
		
		try {
			fromDt = parseFromDate(from);
			toDt = parseToDate(to);
		} catch (NumberFormatException nfe) {
			logWarning("Date is in the future or malformed");
		}

		
		if (isAvailable(userService)) { 
			if (isValidRequest(uuid, fromDt, toDt)) {
				PlatformUser pu = userService.getPlatformUser(uuid);
				if (pu != null)
					response = buildMonthlyWaterConsumptionResponseFrom(pu, fromDt, toDt);
				else {
					logWarning("No such user: " + puid);
					response = Response.status(Response.Status.NOT_FOUND);
				}
			} else {
				response = Response.status(Response.Status.BAD_REQUEST);
			}
		} else if (response == null) {
			response = Response.serverError();
			logError("No user service found");
		}

		return response.build();
	}

	private boolean isValidRequest(UUID uuid, DateTime fromDt, DateTime toDt) {
		return (uuid != null && fromDt != null && toDt != null);
	}

	private ResponseBuilder buildMonthlyWaterConsumptionResponseFrom(
			PlatformUser pu, DateTime from, DateTime to) {
		ResponseBuilder response;
		List<WaterMeterSampler> waterMeters = waterMetersForUser(pu);
		Map<String, List<WaterSample>> samples = getMonthlySamplesForMeters(
				waterMeters, from, to);
		WaterBean waterConsumptionBean = buildWaterBean(samples, QUERY_PERIOD_MONTHLY, pu);
		response = Response.ok(gson.toJson(waterConsumptionBean));
		return response;
	}

	private Map<String, List<WaterSample>> getMonthlySamplesForMeters(
			List<WaterMeterSampler> waterMeters, DateTime from, DateTime to) {
		Map<String, List<WaterSample>> samples = new HashMap<String, List<WaterSample>>();
		
		while (!from.isAfter(to)) {
			for (WaterMeterSampler sampler : waterMeters) {
				tryAddRange(samples, sampler, from, from.plusMonths(1));
			}
			from = from.plusMonths(1);
			
			if (from.equals(to))
				break;
		}

		return samples;
	}

	private void tryAddRange(Map<String, List<WaterSample>> samples,
			WaterMeterSampler sampler, DateTime fromDt, DateTime fromDtPlusRange) {
		try {
			WaterSample sample = sampler.getSample(fromDt, fromDtPlusRange);
			if (!samples.containsKey(sampler.getName()))
				samples.put(sampler.getName(), new ArrayList<WaterSample>());
			samples.get(sampler.getName()).add(sample);
		} catch (SensorFailedException e) {
			logWarning("Could not retrieve sample from meter (" + sampler.getName() + ")"
					+ " between " + fromDt + " and " + fromDtPlusRange);
		}
	}

	private List<WaterMeterSampler> waterMetersForUser(PlatformUser pu) {
		List<WaterMeterSampler> meters = new ArrayList<>();
		
		User[] users = userService.getUsers(pu);
		
		for (User user : users) {
			if (user instanceof GatewayUser) {
				Gateway gateway = ((GatewayUser) user).getGateway();
				for (Device device : gateway) {
					if (device instanceof WaterMeterSampler) {
						logInfo("Found water metering device: " + device.getName());
						meters.add((WaterMeterSampler) device);
					}
				}
			}
		}
		return meters;
	}

	private void tryAdd(Map<String, List<WaterSample>> samples,
			WaterMeterSampler sampler) {
		try {
			WaterSample sample = sampler.getSample();
			if (!samples.containsKey(sampler.getName()))
				samples.put(sampler.getName(), new ArrayList<WaterSample>());
			samples.get(sampler.getName()).add(sample);
		} catch (SensorFailedException sfe) {
			// ignore this sample
		}
	}
	
	private DateTime parseFromDate(String from) throws NumberFormatException {
		long value = Long.parseLong(from);
		DateTime dt = new Instant(value).toDateTime();
		if (dt.isAfterNow())
			throw new NumberFormatException();
		return dt;
	}

	private DateTime parseToDate(String to) throws NumberFormatException {
		DateTime toDt;
		
		if (!to.isEmpty()) {
			long value = Long.parseLong(to);
			toDt = new Instant(value).toDateTime();
		} else { 
			toDt = DateTime.now();
		}
		
		if (toDt.isAfter(DateTime.now()))
			toDt = DateTime.now();	
		
		return toDt;
	}

	private WaterBean buildWaterBean(Map<String, List<WaterSample>> samples,
			String queryPeriod, PlatformUser pu) {
		return WaterBeanFactory.create(samples, queryPeriod, pu.getUserId().toString());
	}
	
	private void logRequest(String endpoint, String puid, String from, String to) {
		logInfo("Request: /water/" + puid + "/" + endpoint
				+ "?from=" + from + "&to=" + to);
	}
	
	private void logRequest(String endpoint, String puid) {
		logInfo("Request: /water/" + puid + "/" + endpoint);
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

	protected void bindUserService(UserService us) {
		this.userService = us;
	}

	protected void unbindUserService(UserService us) {
		this.userService = null;
	}
	
	protected void bindLog(LogService log) {
		this.log = log;
	}
	
	protected void unbindLog(LogService log) {
		this.log = null;
	}
}
