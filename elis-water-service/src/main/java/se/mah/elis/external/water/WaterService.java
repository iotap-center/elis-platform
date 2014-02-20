package se.mah.elis.external.water;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

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

@Path("/water/{puid}")
@Produces("application/json")
@Component(name = "ElisWaterService", immediate = true)
@Service(value = WaterService.class)
public class WaterService {

	private static final String QUERY_PERIOD_NOW = "now";

	private Gson gson;
	
	@Reference
	private UserService userService;
	
	public WaterService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}
	
	@GET
	@Path("/now")
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

	private ResponseBuilder buildCurrentWaterConsumptionResponseFrom(PlatformUser pu) {
		ResponseBuilder response;
		List<WaterMeterSampler> waterMeters = waterMetersForUser(pu);
		Map<String, WaterSample> samples = getCurrentSamplesForMeters(waterMeters);
		WaterBean waterConsumptionBean = buildWaterBean(samples, QUERY_PERIOD_NOW);
		String json = gson.toJson(waterConsumptionBean);
		System.out.println(json);
		response = Response.ok(json);
		return response;
	}
	
	private List<WaterMeterSampler> waterMetersForUser(PlatformUser pu) {
		List<WaterMeterSampler> meters = new ArrayList<>();
		User[] users = userService.getUsers(pu);
		for (User user : users) {
			if (user instanceof GatewayUser) {
				for (Device device : ((GatewayUser) user).getGateway()) {
					if (device instanceof WaterMeterSampler) {
						System.out.println("adding meter: " + device.getName());
						meters.add((WaterMeterSampler) device);
					}
				}
			}
		}
		return meters;
	}

	private Map<String, WaterSample> getCurrentSamplesForMeters(List<WaterMeterSampler> waterMeters) {
		Map<String, WaterSample> samples = new HashMap<>();
		for (WaterMeterSampler sampler : waterMeters) {
			tryAdd(samples, sampler);
		}
		return samples;
	}
	
	private void tryAdd(Map<String, WaterSample> samples, WaterMeterSampler sampler) {
		try {
			WaterSample sample = sampler.getSample();
			System.out.println("added sample: " + sample.getSampleTimestamp().toString());
			samples.put(sampler.getName(), sample);
		} catch (SensorFailedException sfe) {
			// ignore this sample
		}
	}

	private WaterBean buildWaterBean(Map<String, WaterSample> samples, String queryPeriod) {
		return WaterBeanFactory.create(samples, queryPeriod);
	}
	
	protected void bindUserService(UserService us) {
		this.userService = us;
	}
	
	protected void unbindUserService(UserService us) {
		this.userService = null;
	}
}
