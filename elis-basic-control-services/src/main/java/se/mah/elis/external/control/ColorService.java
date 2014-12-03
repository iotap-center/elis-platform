package se.mah.elis.external.control;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.devices.ColoredLamp;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;
import se.mah.elis.data.ElisDataObject;
import se.mah.elis.exceptions.UnsupportedFunctionalityException;
import se.mah.elis.external.beans.helpers.ElisResponseBuilder;
import se.mah.elis.external.control.beans.ColorBean;
import se.mah.elis.external.control.internal.EndpointUtils;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.UserService;

/**
 * 
 * This service provides a HTTP interface to read and alter the color settings
 * of color lamp devices.
 * 
 * @author Johan Holmberg
 * @since 1.0
 * @version 1.0
 */
@Path("/color")
@Produces("application/json")
@Component(name = "Elis Color Service", immediate = true)
@Service(value = ColorService.class)
public class ColorService {

	@Reference
	private UserService userService;
	
	@Reference
	private Storage storage;

	@Reference
	private LogService log;
	
	@Reference
	private EndpointUtils utils;

	/**
	 * Creates an instance of ColorService.
	 * 
	 * @since 1.0
	 */
	public ColorService() {}

	/**
	 * Creates an instance of ColorService. This method is mainly used for
	 * testing purposes.
	 * 
	 * @param us A user service implementation
	 * @param storage A storage implementation
	 * @param utils An EndpointUtils implementation
	 * @since 1.0
	 */
	public ColorService(UserService us, Storage storage, EndpointUtils utils) {
		this();
		userService = us;
		this.storage = storage;
		this.utils = utils;
	}

	/**
	 * Returns the color setting of a device as a HTTP response. The response
	 * is JSON encoded.
	 * 
	 * @param id The id of a device with dimming capabilities.
	 * @return A HTTP response object.
	 * @since 1.0
	 */
	@GET
	@Path("/{id}")
	public Response getColor(@PathParam("id") String id) {
		Response response = null;
		UUID uuid = null;
		ColoredLamp lamp = null;
		
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
					lamp = (ColoredLamp) storage.readData(uuid);
					ColorBean bean = new ColorBean();
					bean.device = uuid;
					bean.color = toJsonHex(lamp.getColor());
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
	 * Sets the color setting of a color lamp, then returns the current status
	 * of the device. The response is JSON encoded.
	 * 
	 * @param id The id of a device with dimming capabilities.
	 * @param bean A bean containing information on what color to display. The
	 * 		  color is given as a hexadecimal number encoded as a string, akin
	 * 		  the way colors are represented in CSS. However, the only allowed
	 * 		  formats are "#RRGGBB" or "#rrggbb".
	 * @return A HTTP response object.
	 * @since 1.0
	 */
	@PUT
	@Path("/{id}")
	public Response setColor(@PathParam("id") String id,
			ColorBean bean) {
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
					if (data instanceof ColoredLamp) {
						// The requested resource is a power switch.
						ColoredLamp lamp = (ColoredLamp) data;
						lamp.setColor(fromJsonHex(bean.color));
						
						bean.device = uuid;
						bean.color = toJsonHex(lamp.getColor());
						response = ElisResponseBuilder.buildOKResponse(bean);
					} else if (data instanceof DeviceSet) {
						// The requested resource is a device set.
						DeviceSet set = (DeviceSet) data;
						ColoredLamp lamp = null;
						int count = 0;
						
						for (Device device : set) {
							if (device instanceof ColoredLamp) {
								lamp = (ColoredLamp) device;
								lamp.setColor(fromJsonHex(bean.color));
								++count;
							}
						}
						
						if (count == 0) {
							throw new UnsupportedFunctionalityException();
						}
						bean.color = bean.color.toUpperCase();
						bean.deviceset = uuid;
						response = ElisResponseBuilder.buildOKResponse(bean);
					} else if (user != null) {
						// The requested resource is a platform user.
						List<ColoredLamp> lamps = utils.getDevices(user, ColoredLamp.class);
						
						if (lamps.size() == 0) {
							// As per the documentation: If a user doesn't
							// have any devices that are power switches,
							// let us fail.
							throw new UnsupportedFunctionalityException();
						}
						
						for (ColoredLamp lamp : lamps) {
							lamp.setColor(fromJsonHex(bean.color));
						}
						bean.color = bean.color.toUpperCase();
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
	
	private String toJsonHex(int value) {
		return "#" + String.format("%06X", value & 0xFFFFFF);
	}
	
	private int fromJsonHex(String value) {
		return Integer.parseInt(value.substring(1), 16);
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
