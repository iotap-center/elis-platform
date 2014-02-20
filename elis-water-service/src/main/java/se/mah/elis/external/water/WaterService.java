package se.mah.elis.external.water;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.WaterMeterSampler;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.WaterSample;
import se.mah.elis.external.water.beans.WaterBean;
import se.mah.elis.external.water.beans.WaterBeanFactory;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	public WaterService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	@GET
	@Path("/{puid}/now")
	public Response getCurrentWaterConsumption(@PathParam("puid") String puid) {
		ResponseBuilder response = null;

		if (userService != null) {
			PlatformUser pu = userService.getPlatformUser(puid);
			if (pu != null) {
				response = buildCurrentWaterConsumptionResponseFrom(pu);
			} else {
				response = Response.status(Response.Status.NOT_FOUND);
			}
		} else {
			response = Response.serverError();
		}

		return response.build();
	}

	private ResponseBuilder buildCurrentWaterConsumptionResponseFrom(
			PlatformUser pu) {
		ResponseBuilder response;
		List<WaterMeterSampler> waterMeters = waterMetersForUser(pu);
		Map<String, List<WaterSample>> samples = getCurrentSamplesForMeters(waterMeters);
		WaterBean waterConsumptionBean = buildWaterBean(samples, QUERY_PERIOD_NOW);
		String json = gson.toJson(waterConsumptionBean);
		response = Response.ok(json);
		return response;
	}

	@GET
	@Path("/{puid}/daily")
	public Response getDailyWaterConsumption(
			@PathParam("puid") String puid,
			@QueryParam("from") String from,
			@DefaultValue("") @QueryParam("to") String to) {
		ResponseBuilder response = null;

		if (userService != null) {
			PlatformUser pu = userService.getPlatformUser(puid);
			if (pu != null)
				response = buildDailyWaterConsumptionResponseFrom(pu, from, to);
			else
				response = Response.status(Response.Status.NOT_FOUND);
		} else
			response = Response.serverError();
		
		return response.build();
	}

	private ResponseBuilder buildDailyWaterConsumptionResponseFrom(
			PlatformUser pu, String from, String to) {
		ResponseBuilder response;
		List<WaterMeterSampler> waterMeters = waterMetersForUser(pu);
		Map<String, List<WaterSample>> samples = getDailySamplesForMeters(
				waterMeters, from, to);
		WaterBean waterConsumptionBean = buildWaterBean(samples, QUERY_PERIOD_DAILY);
		response = Response.ok(gson.toJson(waterConsumptionBean));
		return response;
	}
	
	private Map<String, List<WaterSample>> getDailySamplesForMeters(
			List<WaterMeterSampler> waterMeters, String from, String to) {
		Map<String, List<WaterSample>> samples = new HashMap<String, List<WaterSample>>();

		DateTime fromDt = parseFromDate(from);
		DateTime toDt = parseToDate(to);
		
		while (!fromDt.isAfter(toDt)) {
			for (WaterMeterSampler sampler : waterMeters) {
				tryAddRange(samples, sampler, fromDt, fromDt.plusDays(1));
			}
			fromDt = fromDt.plusDays(1);
		}

		return samples;
	}
	
	@GET
	@Path("/{puid}/weekly")
	public Response getWeeklyWaterConsumption(
			@PathParam("puid") String puid,
			@QueryParam("from") String from,
			@DefaultValue("") @QueryParam("to") String to) {
		ResponseBuilder response = null;

		if (userService != null) {
			PlatformUser pu = userService.getPlatformUser(puid);
			if (pu != null)
				response = buildWeeklyWaterConsumptionResponseFrom(pu, from, to);
			else
				response = Response.status(Response.Status.NOT_FOUND);
		} else
			response = Response.serverError();
		
		return response.build();
	}

	private ResponseBuilder buildWeeklyWaterConsumptionResponseFrom(
			PlatformUser pu, String from, String to) {
		ResponseBuilder response;
		List<WaterMeterSampler> waterMeters = waterMetersForUser(pu);
		Map<String, List<WaterSample>> samples = getWeeklySamplesForMeters(
				waterMeters, from, to);
		WaterBean waterConsumptionBean = buildWaterBean(samples, QUERY_PERIOD_WEEKLY);
		response = Response.ok(gson.toJson(waterConsumptionBean));
		return response;
	}
	
	private Map<String, List<WaterSample>> getWeeklySamplesForMeters(
			List<WaterMeterSampler> waterMeters, String from, String to) {
		Map<String, List<WaterSample>> samples = new HashMap<String, List<WaterSample>>();

		DateTime fromDt = parseFromDate(from);
		DateTime toDt = parseToDate(to);
		
		while (!fromDt.isAfter(toDt)) {
			for (WaterMeterSampler sampler : waterMeters) {
				tryAddRange(samples, sampler, fromDt, fromDt.plusDays(7));
			}
			fromDt = fromDt.plusDays(7);
		}

		return samples;
	}

	@GET
	@Path("/{puid}/monthly")
	public Response getMonthlyWaterConsumption(
			@PathParam("puid") String puid,
			@QueryParam("from") String from,
			@DefaultValue("") @QueryParam("to") String to) {
		ResponseBuilder response = null;

		if (userService != null) {
			PlatformUser pu = userService.getPlatformUser(puid);
			if (pu != null)
				response = buildMonthlyWaterConsumptionResponseFrom(pu, from, to);
			else
				response = Response.status(Response.Status.NOT_FOUND);
		} else
			response = Response.serverError();

		return response.build();
	}

	private ResponseBuilder buildMonthlyWaterConsumptionResponseFrom(
			PlatformUser pu, String from, String to) {
		ResponseBuilder response;
		List<WaterMeterSampler> waterMeters = waterMetersForUser(pu);
		Map<String, List<WaterSample>> samples = getMonthlySamplesForMeters(
				waterMeters, from, to);
		WaterBean waterConsumptionBean = buildWaterBean(samples, QUERY_PERIOD_MONTHLY);
		response = Response.ok(gson.toJson(waterConsumptionBean));
		return response;
	}

	private Map<String, List<WaterSample>> getMonthlySamplesForMeters(
			List<WaterMeterSampler> waterMeters, String from, String to) {
		Map<String, List<WaterSample>> samples = new HashMap<String, List<WaterSample>>();
		DateTime fromDt = parseFromDate(from);
		DateTime toDt = parseToDate(to);
		
		while (!fromDt.isAfter(toDt)) {
			for (WaterMeterSampler sampler : waterMeters) {
				tryAddRange(samples, sampler, fromDt, fromDt.plusMonths(1));
			}
			fromDt = fromDt.plusMonths(1);
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
			// ignore sample
		}
	}

	private List<WaterMeterSampler> waterMetersForUser(PlatformUser pu) {
		List<WaterMeterSampler> meters = new ArrayList<>();
		User[] users = userService.getUsers(pu);
		for (User user : users) {
			if (user instanceof GatewayUser) {
				for (Device device : ((GatewayUser) user).getGateway()) {
					if (device instanceof WaterMeterSampler) {
						meters.add((WaterMeterSampler) device);
					}
				}
			}
		}
		return meters;
	}

	private Map<String, List<WaterSample>> getCurrentSamplesForMeters(
			List<WaterMeterSampler> waterMeters) {
		Map<String, List<WaterSample>> samples = new HashMap<>();
		for (WaterMeterSampler sampler : waterMeters) {
			tryAdd(samples, sampler);
		}
		return samples;
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
	
	private DateTime parseFromDate(String from) {
		return new Instant(Long.parseLong(from)).toDateTime();
	}

	private DateTime parseToDate(String to) {
		DateTime toDt;
		if (!to.isEmpty())
			toDt = new Instant(Long.parseLong(to)).toDateTime();
		else 
			toDt = DateTime.now();
		
		if (toDt.isAfter(DateTime.now()))
			toDt = DateTime.now();	
		return toDt;
	}

	private WaterBean buildWaterBean(Map<String, List<WaterSample>> samples,
			String queryPeriod) {
		return WaterBeanFactory.create(samples, queryPeriod);
	}

	protected void bindUserService(UserService us) {
		this.userService = us;
	}

	protected void unbindUserService(UserService us) {
		this.userService = null;
	}
}
