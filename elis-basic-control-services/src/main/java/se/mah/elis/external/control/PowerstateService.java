package se.mah.elis.external.control;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;
import se.mah.elis.data.ElisDataObject;
import se.mah.elis.exceptions.UnsupportedFunctionalityException;
import se.mah.elis.external.beans.helpers.ElisResponseBuilder;
import se.mah.elis.external.control.beans.PowerstateBean;
import se.mah.elis.external.control.internal.EndpointUtils;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.UserService;

/**
 * 
 * This service provides a HTTP interface to read and alter the state of
 * power switch devices.
 * 
 * @author Johan Holmberg
 * @since 1.0
 * @version 1.0
 * 
 */
@Path("/powerstate")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
@Component(name = "Elis Powerstate Service", immediate = true)
@Service(value = PowerstateService.class)
public class PowerstateService {

	@Reference
	private UserService userService;
	
	@Reference
	private Storage storage;

	@Reference
	private LogService log;
	
	@Reference
	private EndpointUtils utils;

	private Gson gson;

	/**
	 * Creates an instance of PowerstateService.
	 * 
	 * @since 1.0
	 */
	public PowerstateService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	/**
	 * Creates an instance of PowerstateService. This method is mainly used for
	 * testing purposes.
	 * 
	 * @param us A user service implementation
	 * @param storage A storage implementation
	 * @param log A log service implementation.
	 * @param utils An EndpointUtils implementation
	 * @since 1.0
	 */
	public PowerstateService(UserService us, Storage storage,
			LogService log, EndpointUtils utils) {
		this();
		userService = us;
		this.storage = storage;
		this.log = log;
		this.utils = utils;
	}

	/**
	 * Returns the power state of a device as a HTTP response. The response is
	 * JSON encoded.
	 * 
	 * @param id The id of a device with power switch capabilities.
	 * @return A HTTP response object.
	 * @since 1.0
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPowerstate(@PathParam("id") String id) {
		Response response = null;
		UUID uuid = null;
		PowerSwitch ps = null;
		
		logRequest("", id);
		
		// Count on things being bad
		response = ElisResponseBuilder.buildBadRequestResponse();
		
		try {
			uuid = UUID.fromString(id);
		} catch (Exception e) {
			log(LogService.LOG_WARNING, "Bad UUID");
		}
		
		// OK, let's move on. First, we'll just continue if all the stuff we
		// need is in place. If not, we'll simply respond with a 500 response,
		// as defined above.
		if (userService != null && storage != null && uuid != null) {
			// First, let's see if the requested object exists.
			if (storage.objectExists(uuid)) {
				try {
					ps = (PowerSwitch) storage.readData(uuid);
					PowerstateBean bean = new PowerstateBean();
					bean.device = uuid;
					bean.powerstate = ps.isTurnedOn();
					response = ElisResponseBuilder.buildOKResponse(bean);
				} catch (StorageException | ClassCastException e) {
					// So the requested object wasn't even a power switch.
					// We'll throw a 405 response back. Suits 'em right.
					log(LogService.LOG_INFO, "Tried to perform a getPowerstate on a non-power switch object: " + uuid);
					response = ElisResponseBuilder.buildMethodNotAllowedResponse();
				} catch (ActuatorFailedException e) {
					// Well, this sucks. We couldn't read the device status.
					// Let's just respond with a 503.
					log(LogService.LOG_WARNING, "Couldn't read device status from: " + uuid);
					response = ElisResponseBuilder.buildServiceUnavailableResponse();
				}
			} else {
				// Apparently, no such object exist. Respond with a 404.
				response = ElisResponseBuilder.buildNotFoundResponse();
			}
		}
		
		return response;
	}

	/**
	 * Sets the state of a power switch, then returns the current status of the
	 * device. The response is JSON encoded.
	 * 
	 * @param id The id of a device with power switch capabilities.
	 * @param bean A bean containing the desired power state.
	 * @return A HTTP response object.
	 * @since 1.0
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPowerstate(@PathParam("id") String id,
			PowerstateBean bean) {
		Response response = null;
		UUID uuid = null;
		ElisDataObject data = null;
		PlatformUser user = null;
		
		logRequest("", id);
		
		// Count on things being bad
		response = ElisResponseBuilder.buildBadRequestResponse();
		
		try {
			uuid = UUID.fromString(id);
		} catch (Exception e) {
			log(LogService.LOG_WARNING, "Bad UUID");
		}
		
		// OK, let's move on. First, we'll just continue if all the stuff we
		// need is in place. If not, we'll simply respond with a 500 response,
		// as defined above.
		if (userService != null && storage != null && uuid != null) {
			// First, let's see if the requested object exists.
			if (storage.objectExists(uuid)) {
				try {
					data = storage.readData(uuid);
				} catch (StorageException e) {
					try {
						user = (PlatformUser) storage.readUser(uuid);
					} catch (StorageException | ClassCastException e1) {}
				}
				
				try {
					if (data instanceof PowerSwitch) {
						// The requested resource is a power switch.
						PowerSwitch ps = (PowerSwitch) data;
						if (bean.powerstate) {
							ps.turnOn();
						} else {
							ps.turnOff();
						}
						
						bean.device = uuid;
						bean.powerstate = ps.isTurnedOn();
						response = ElisResponseBuilder.buildOKResponse(bean);
					} else if (data instanceof DeviceSet) {
						// The requested resource is a device set.
						DeviceSet set = (DeviceSet) data;
						PowerSwitch ps = null;
						int count = 0;
						
						for (Device device : set) {
							if (device instanceof PowerSwitch) {
								ps = (PowerSwitch) device;
								if (bean.powerstate) {
									ps.turnOn();
								} else {
									ps.turnOff();
								}
								++count;
							}
						}
						
						if (count == 0) {
							throw new UnsupportedFunctionalityException();
						}
						bean.deviceset = uuid;
						response = ElisResponseBuilder.buildOKResponse(bean);
					} else if (user != null) {
						// The requested resource is a platform user.
						List<PowerSwitch> switches = utils.getDevices(user, PowerSwitch.class);
						
						if (switches.size() == 0) {
							// As per the documentation: If a user doesn't
							// have any devices that are power switches,
							// let us fail.
							throw new UnsupportedFunctionalityException();
						}
						
						for (PowerSwitch ps : switches) {
							if (bean.powerstate) {
								ps.turnOn();
							} else {
								ps.turnOff();
							}
						}
						bean.user = uuid;
						response = ElisResponseBuilder.buildOKResponse(bean);
					} else {
						throw new UnsupportedFunctionalityException();
					}
				} catch (ClassCastException | UnsupportedFunctionalityException e) {
					// So the requested object wasn't even a power switch.
					// We'll throw a 405 response back. Suits 'em right.
					log(LogService.LOG_INFO, "Tried to perform a getPowerstate on a non-power switch object: " + uuid);
					response = ElisResponseBuilder.buildMethodNotAllowedResponse();
				} catch (ActuatorFailedException e) {
					// Well, this sucks. We couldn't read the device status.
					// Let's just respond with a 503.
					log(LogService.LOG_WARNING, "Couldn't read device status from: " + uuid);
					response = ElisResponseBuilder.buildServiceUnavailableResponse();
				}
			} else {
				// Apparently, no such object exist. Respond with a 404.
				response = ElisResponseBuilder.buildNotFoundResponse();
			}
		}
		
		return response;
	}

	protected void bindUserService(UserService us) {
		this.userService = us;
	}

	protected void unbindUserService(UserService us) {
		this.userService = null;
	}

	protected void bindStorage(Storage storage) {
		this.storage = storage;
	}

	protected void unbindStorage(Storage storage) {
		this.storage = null;
	}

	protected void bindLog(LogService log) {
		this.log = log;
	}

	protected void unbindLog(LogService log) {
		this.log = null;
	}
	
	private void logRequest(String endpoint, String puid, String from, String to) {
		log(LogService.LOG_INFO, "Request: /energy/" + puid + "/" + endpoint
				+ "?from=" + from + "&to=" + to);
	}
	
	private void logRequest(String endpoint, String id) {
		log(LogService.LOG_INFO, "Request: /energy/" + id + "/" + endpoint);
	}

	private void log(int logLevel, String msg) {
		if (log != null) {
			log.log(logLevel, msg);
		}
	}
}
