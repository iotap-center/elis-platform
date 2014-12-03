package se.mah.elis.external.energy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.MainPowerMeter;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.data.ElisDataObject;
import se.mah.elis.external.beans.helpers.ElisResponseBuilder;
import se.mah.elis.external.energy.beans.EnergyBean;
import se.mah.elis.external.energy.beans.EnergyBeanFactory;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
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
	private Storage storage;
	
	@Reference
	private LogService log;

	private Gson gson;

	public EnergyService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}
	
	public EnergyService(UserService userService, Storage storage, LogService log) {
		this();
		this.storage = storage;
		this.userService = userService;
		this.log = log;
	}

	@GET
	@Path("/{id}/now")
	public Response getCurrentEnergyConsumption(@PathParam("id") String id) {
		Response response = null;
		PlatformUser pu = null;
		ElisDataObject edo = null;
		UUID uuid = null;
		
		logRequest("now", id);
		
		// Count on things being bad
		response = ElisResponseBuilder.buildBadRequestResponse();
		
		try {
			uuid = UUID.fromString(id);
		} catch (Exception e) {
			logWarning("Bad UUID");
		}
		
		logInfo("Hello there!");
		
		if (userService != null && storage != null && uuid != null) {
			try {
				edo = storage.readData(uuid);
			} catch (StorageException e) {
				pu = userService.getPlatformUser(uuid);
			}
			try {
				if (pu != null) {
					response = buildCurrentEnergyConsumptionResponse("now", pu);
				} else if (edo != null) {
					if (edo instanceof ElectricitySampler) {
						response = buildCurrentEnergyConsumptionResponse("now", (ElectricitySampler) edo);
					} else if (edo instanceof DeviceSet) {
						response = buildCurrentEnergyConsumptionResponse("now", (DeviceSet) edo);
					}
				} else {
					response = ElisResponseBuilder.buildNotFoundResponse();
					logWarning("Could not find user: " + uuid.toString());
				}
			} catch (SensorFailedException e) {}
		} else if (response == null) {
			response = ElisResponseBuilder.buildInternalServerErrorResponse();
			logError("User service not available");
		}

		return response;
	}

	private Response buildCurrentEnergyConsumptionResponse(
			String period, PlatformUser pu) throws SensorFailedException {
		List<ElectricitySampler> meters = getMeters(pu);
		meters = filterOutSupersededMeters(meters);
		Map<ElectricitySampler, List<ElectricitySample>> samples = fetchSamples(meters);
		EnergyBean bean = EnergyBeanFactory.create(samples, period, pu);
		return ElisResponseBuilder.buildOKResponse(bean);
	}

	private Response buildCurrentEnergyConsumptionResponse(
			String period, ElectricitySampler sampler) throws SensorFailedException {
		List<ElectricitySampler> meters = Arrays.asList(sampler);
		meters = filterOutSupersededMeters(meters);
		Map<ElectricitySampler, List<ElectricitySample>> samples = fetchSamples(meters);
		EnergyBean bean = EnergyBeanFactory.create(samples, period, sampler);
		return ElisResponseBuilder.buildOKResponse(bean);
	}

	private Response buildCurrentEnergyConsumptionResponse(
			String period, DeviceSet set) throws SensorFailedException {
		Response response = null;
		List<ElectricitySampler> meters = getMetersInSet(set);
		meters = filterOutSupersededMeters(meters);
		
		if (meters.size() > 0) {
			Map<ElectricitySampler, List<ElectricitySample>> samples = fetchSamples(meters);
			EnergyBean bean = EnergyBeanFactory.create(samples, period, set);
			response = ElisResponseBuilder.buildOKResponse(bean);
		} else {
			response = ElisResponseBuilder.buildBadRequestResponse();
		}
		
		return response;
	}
	
	@GET
	@Path("/{id}/hourly")
	public Response getHourlyEnergyConsumption(@PathParam("id") String id,
			@QueryParam("from") String from,
			@QueryParam("to") String to) {
		logRequest("hourly", id, from, to);
		
		return getHistoricEnergyConsumtion(id, from, to, "hourly");
	}
	
	@GET
	@Path("/{id}/daily")
	public Response getDailyEnergyConsumption(@PathParam("id") String id,
			@QueryParam("from") String from,
			@QueryParam("to") String to) {
		logRequest("daily", id, from, to);
		
		return getHistoricEnergyConsumtion(id, from, to, "daily");
	}
	
	@GET
	@Path("/{id}/weekly")
	public Response getWeeklyEnergyConsumption(@PathParam("id") String id,
			@QueryParam("from") String from,
			@QueryParam("to") String to) {
		logRequest("weekly", id, from, to);
		
		return getHistoricEnergyConsumtion(id, from, to, "weekly");
	}
	
	@GET
	@Path("/{id}/monthly")
	public Response getMonthlyEnergyConsumption(@PathParam("id") String id,
			@QueryParam("from") String from,
			@QueryParam("to") String to) {
		logRequest("monthly", id, from, to);
		
		return getHistoricEnergyConsumtion(id, from, to, "monthly");
	}
	
	private Response getHistoricEnergyConsumtion(String id, String from,
			String to, String periodicity) {
		Response response = null;
		PlatformUser pu = null;
		ElisDataObject edo = null;
		UUID uuid = null;
		DateTime startDate = null;
		DateTime endDate = null;
		
		// Count on things being bad
		response = ElisResponseBuilder.buildBadRequestResponse();
		
		if (to == null || to.length() == 0) {
			to = "0";
		}
		
		try {
			// Try to parse the platform user id.
			uuid = UUID.fromString(id);
		} catch (Exception e) {
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
			logWarning("Bad dates");
			return response;
		}
		
		if (userService != null && storage != null && uuid != null) {
			try {
				edo = storage.readData(uuid);
			} catch (StorageException e) {
				pu = userService.getPlatformUser(uuid);
			}
			try {
				if (pu != null) {
					response = buildPeriodicEnergyConsumptionResponse(periodicity,
							startDate, endDate, pu);
				} else if (edo != null) {
					if (edo instanceof ElectricitySampler) {
						response = buildPeriodicEnergyConsumptionResponse(periodicity,
								startDate, endDate, (ElectricitySampler) edo);
					} else if (edo instanceof DeviceSet) {
						response = buildPeriodicEnergyConsumptionResponse(periodicity,
								startDate, endDate, (DeviceSet) edo);
					}
				} else {
					response = ElisResponseBuilder.buildNotFoundResponse();
					logWarning("Could not find user: " + uuid.toString());
				}
			} catch (SensorFailedException e) {
				logWarning("Couldn't read some samples.");
			}
		} else if (response == null) {
			logError("User service not available");
		}
		
		return response;
	}

	private Response buildPeriodicEnergyConsumptionResponse(
			String period, DateTime startDate, DateTime endDate, PlatformUser pu)
					throws SensorFailedException {
		List<ElectricitySampler> meters = getMeters(pu);
		meters = filterOutSupersededMeters(meters);
		Map<ElectricitySampler, List<ElectricitySample>> samples =
				fetchSamples(meters, startDate, endDate);

		logDebug("Building periodic response with " + meters.size() + " meters, starting at " + startDate + " and ending at " + endDate);
		
		EnergyBean bean = EnergyBeanFactory.create(samples, period, startDate, endDate, pu);
		
		return ElisResponseBuilder.buildOKResponse(bean);
	}

	private Response buildPeriodicEnergyConsumptionResponse(
			String period, DateTime startDate, DateTime endDate,
			ElectricitySampler meter)
					throws SensorFailedException {
		List<ElectricitySampler> meters = Arrays.asList(meter);
		Map<ElectricitySampler, List<ElectricitySample>> samples =
				fetchSamples(meters, startDate, endDate);
		
		logDebug("Building periodic response with " + meters.size() + " meters, starting at " + startDate + " and ending at " + endDate);
		
		EnergyBean bean = EnergyBeanFactory.create(samples, period, startDate, endDate, meter);
		
		return ElisResponseBuilder.buildOKResponse(bean);
	}

	private Response buildPeriodicEnergyConsumptionResponse(
			String period, DateTime startDate, DateTime endDate, DeviceSet set)
					throws SensorFailedException {
		List<ElectricitySampler> meters = getMetersInSet(set);
		meters = filterOutSupersededMeters(meters);
		Map<ElectricitySampler, List<ElectricitySample>> samples =
				fetchSamples(meters, startDate, endDate);
		
		logDebug("Building periodic response with " + meters.size() + " meters, starting at " + startDate + " and ending at " + endDate);
		
		EnergyBean bean = EnergyBeanFactory.create(samples, period, startDate, endDate, set);
		
		return ElisResponseBuilder.buildOKResponse(bean);
	}

	private List<ElectricitySampler> getMeters(PlatformUser pu) {
		User[] users = userService.getUsers(pu);
		List<ElectricitySampler> devices = getDevices(users);
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

	private List<ElectricitySampler> getMeters(GatewayUser user) {
		List<ElectricitySampler> meters = new ArrayList<>();
		for (Device device : user.getGateway()) {
			if (device instanceof ElectricitySampler)
				meters.add((ElectricitySampler) device);
		}
		return meters;
	}

	private List<ElectricitySampler> getMetersInSet(DeviceSet set) {
		List<ElectricitySampler> meters = new ArrayList<ElectricitySampler>();
		
		for (Device device : set) {
			if (device instanceof ElectricitySampler) {
				meters.add((ElectricitySampler) device);
			}
		}
		
		return meters;
	}
	
	private List<ElectricitySampler> filterOutSupersededMeters(List<ElectricitySampler> meters) {
		List<MainPowerMeter> mainMeters = new ArrayList<MainPowerMeter>();
		
		for (Device device : meters) {
			// We'd better take all the main power meters into a special list,
			// so that we can remove any devices being present in them later on
			if (device instanceof MainPowerMeter) {
				mainMeters.add((MainPowerMeter) device);
			}
		}
		
		// Now, remove meters which are in fact superseded by main power meters
		if (mainMeters.size() > 0) {
			MainPowerMeter[] mms = mainMeters.toArray(new MainPowerMeter[0]);
			for (int i = 0; i < mms.length; i++) {
				Device[] devices = ((MainPowerMeter) mms[i]).toArray(new Device[0]);
				for (int j = 0; j < devices.length; j++) {
					meters.remove(devices[j]);
				}
			}
		}
		
		return meters;
	}

	private List<ElectricitySampler> getDevices(User[] users) {
		List<ElectricitySampler> meters = new ArrayList<>();
		for (User user : users) {
			if (user instanceof GatewayUser) {
				logDebug("Found a GatewayUser");
				meters.addAll(getMeters((GatewayUser) user));
			}
		}
		logDebug("Found " + meters.size() + " devices");
		return meters;
	}
	
	/**
	 * Fetches current samples from a group of meters.
	 * 
	 * @param meters The meters to fetch samples from
	 * @return A list of samples
	 * @throws SensorFailedException If the samples couldn't be fetched.
	 * @since 1.0
	 */
	private Map<ElectricitySampler, List<ElectricitySample>> fetchSamples(
			List<ElectricitySampler> meters) throws SensorFailedException {
		Map<ElectricitySampler, List<ElectricitySample>> samples =
				new HashMap<ElectricitySampler, List<ElectricitySample>>();
		
		for (ElectricitySampler meter : meters) {
			samples.put(meter, Arrays.asList(meter.getSample()));
		}
		
		return samples;
	}
	
	/**
	 * <p>Fetches samples from a group of meters from a moment of time up until
	 * a later moment in time.</p>
	 * 
	 * <p>If all of the samples fail to be read, the method will throw an
	 * exception. If at least one sample is read, the read samples will be
	 * returned.</p>
	 * 
	 * @param meters The meters to fetch samples from
	 * @param from The instant to start fetching samples from
	 * @param to The instant up until which we'll sample.
	 * @return A list of samples
	 * @throws SensorFailedException If the samples couldn't be fetched.
	 * @since 1.0
	 */
	private Map<ElectricitySampler, List<ElectricitySample>> fetchSamples(
			List<ElectricitySampler> meters, DateTime from, DateTime to)
					throws SensorFailedException {
		Map<ElectricitySampler, List<ElectricitySample>> sampleList =
				new HashMap<ElectricitySampler, List<ElectricitySample>>();
		boolean throwException = false;
		
		for (ElectricitySampler meter : meters) {
			// First, fetch all samples
			try {
				List<ElectricitySample> samples = meter.getSamples(from, to);
				
				// Now, check all samples for sanity
				List<ElectricitySample> killEmAll =
						new ArrayList<ElectricitySample>();
				if (samples != null) {
					for (ElectricitySample sample : samples) {
						if (sample.getSampleTimestamp().isBefore(from) ||
							sample.getSampleTimestamp().isAfter(to)) {
							killEmAll.add(sample);
						}
					}
				}
				
				// OK, some might have been bad. Remove them.
				for (ElectricitySample sample : killEmAll) {
					samples.remove(sample);
				}
				
				// If no fitting samples were found, let's throw an exception.
				if (samples.size() == 0) {
					throw new SensorFailedException();
				}
				
				// Then, add them to our list
				sampleList.put(meter, samples);
			} catch (SensorFailedException e) {
				logInfo("Can't read from " + meter + ", " + e.getMessage());
				throwException = true;
			}
		}
		
		// If all of the samplings failed, then let's throw an exception.
		if (throwException && sampleList.size() == 0) {
			throw new SensorFailedException();
		}
		
		return sampleList;
	}
	
	// OSGi-related stuff below
	
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
	
	protected void bindStorage(Storage storage) {
		this.storage = storage;
	}
	
	protected void unbindStorage(Storage storage) {
		this.storage = null;
	}

	protected void unbindLog(LogService log) {
		this.log = null;
	}

	protected void bindLog(LogService log) {
		this.log = log;
	}
	
}
