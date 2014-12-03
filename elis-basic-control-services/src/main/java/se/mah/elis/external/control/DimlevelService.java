package se.mah.elis.external.control;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import se.mah.elis.adaptor.device.api.entities.devices.ColoredLamp;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Dimmer;
import se.mah.elis.adaptor.device.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;
import se.mah.elis.data.ElisDataObject;
import se.mah.elis.exceptions.UnsupportedFunctionalityException;
import se.mah.elis.external.beans.helpers.ElisResponseBuilder;
import se.mah.elis.external.control.beans.DimlevelBean;
import se.mah.elis.external.control.beans.PowerstateBean;
import se.mah.elis.external.control.internal.EndpointUtils;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.UserService;

/**
 * 
 * This service provides a HTTP interface to read and alter the dim level of
 * dimmer devices.
 * 
 * @author Johan Holmberg
 * @since 1.0
 * @version 1.0
 * 
 */
@Path("/dimlevel")
@Produces("application/json")
@Component(name = "Elis Dimlevel Service", immediate = true)
@Service(value = DimlevelService.class)
public class DimlevelService {
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
	 * Creates an instance of DimlevelService.
	 * 
	 * @since 1.0
	 */
	public DimlevelService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	/**
	 * Creates an instance of DimlevelService. This method is mainly used for
	 * testing purposes.
	 * 
	 * @param us A user service implementation
	 * @param storage A storage implementation
	 * @param utils An EndpointUtils implementation
	 * @since 1.0
	 */
	public DimlevelService(UserService us, Storage storage, EndpointUtils utils) {
		this();
		userService = us;
		this.storage = storage;
		this.utils = utils;
	}

	/**
	 * Returns the dim level of a device as a HTTP response. The response is
	 * JSON encoded.
	 * 
	 * @param id The id of a device with dimming capabilities.
	 * @return A HTTP response object.
	 * @since 1.0
	 */
	@GET
	@Path("/{id}")
	public Response getDimlevel(@PathParam("id") String id) {
		Response response = null;
		UUID uuid = null;
		Dimmer dimmer = null;
		
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
					dimmer = (Dimmer) storage.readData(uuid);
					DimlevelBean bean = new DimlevelBean();
					bean.device = uuid;
					bean.dimlevel = (int) (100 * dimmer.getDimLevel());
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
	 * Sets the dim level of a dimmer, then returns the current status of the
	 * device. The response is JSON encoded.
	 * 
	 * @param id The id of a device with dimming capabilities.
	 * @param bean A bean with information regarding the preferred bean level.
	 * 		  The service accepts any number in the range of 0 <= x <= 100.
	 * @return A HTTP response object.
	 * @since 1.0
	 */
	@PUT
	@Path("/{id}")
	public Response setDimlevel(@PathParam("id") String id,
			DimlevelBean bean) {
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
			
			// We only deal with dim levels in the 0 - 100 span. Everything else is
			// considered very, very bad.
			if (bean.dimlevel < 0 || bean.dimlevel > 100) {
				// Not good enough. Throw it away.
				// We'll throw a 405 response back. Suits 'em right.
				log(LogService.LOG_INFO, "Tried to perform a dim level operation  on a non-dimmer object: " + uuid);
				response = ElisResponseBuilder.buildMethodNotAllowedResponse();
				
			// Ok. That was fine. Let's move on.
			// First, let's see if the requested object exists.
			} else if (storage.objectExists(uuid)) {
				try {
					data = storage.readData(uuid);
				} catch (StorageException e) {
					try {
						user = (PlatformUser) storage.readUser(uuid);
					} catch (StorageException | ClassCastException e1) {}
				}
				
				try {
					if (data instanceof Dimmer) {
						// The requested resource is a power switch.
						Dimmer dimmer = (Dimmer) data;
						
						dimmer.setDimLevel(bean.dimlevel / 100);
						
						bean.device = uuid;
						bean.dimlevel = (int) (dimmer.getDimLevel() * 100);
						response = ElisResponseBuilder.buildOKResponse(bean);
					} else if (data instanceof DeviceSet) {
						// The requested resource is a device set.
						DeviceSet set = (DeviceSet) data;
						Dimmer dimmer = null;
						int count = 0;
						
						for (Device device : set) {
							if (device instanceof Dimmer) {
								dimmer = (Dimmer) device;
								dimmer.setDimLevel(bean.dimlevel / 100);
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
						List<Dimmer> dimmers = utils.getDevices(user, Dimmer.class);
						
						if (dimmers.size() == 0) {
							// As per the documentation: If a user doesn't
							// have any devices that are power switches,
							// let us fail.
							throw new UnsupportedFunctionalityException();
						}
						
						for (Dimmer dimmer : dimmers) {
							dimmer.setDimLevel(bean.dimlevel / 100);
						}
						bean.user = uuid;
						response = ElisResponseBuilder.buildOKResponse(bean);
					} else {
						throw new UnsupportedFunctionalityException();
					}
				} catch (ClassCastException | UnsupportedFunctionalityException e) {
					// So the requested object wasn't even a dimmer.
					// We'll throw a 405 response back. Suits 'em right.
					log(LogService.LOG_INFO, "Tried to perform a dim level operation  on a non-dimmer object: " + uuid);
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
