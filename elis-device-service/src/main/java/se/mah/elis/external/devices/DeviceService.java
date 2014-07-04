package se.mah.elis.external.devices;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.data.ElisDataObject;
import se.mah.elis.external.beans.helpers.ElisResponseBuilder;
import se.mah.elis.external.devices.beans.DeviceBean;
import se.mah.elis.external.devices.beans.DeviceSetBean;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * This service provides a HTTP interface to retrieve information about all
 * devices that a platform user is connected with.
 * 
 * @author Marcus Ljungblad
 * @author Johan Holmberg
 * @since 1.0
 * @version 1.0
 * 
 */
@Path("/devices")
@Produces("application/json")
@Component(name = "ElisDeviceService", immediate = true)
@Service(value = DeviceService.class)
public class DeviceService {

	private Gson gson;

	@Reference
	private UserService userService;
	
	@Reference
	private Storage storage;

	@Reference
	private LogService log;

	public DeviceService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	public DeviceService(UserService us, Storage storage, LogService log) {
		this();
		userService = us;
		this.storage = storage;
	}

	/**
	 * Returns a list of devices as a HTTP response. The response is JSON
	 * encoded.
	 * 
	 * @since 1.0
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	public Response getDeviceList(@PathParam("id") String id) {
		Response response = null;
		UUID uuid = null;

		logThis("Request: /devices/" + id + "/");
		
		// First of all, count on things being bad.
		response = ElisResponseBuilder.buildBadRequestResponse();

		try {
			uuid = UUID.fromString(id);
		} catch (Exception e) {
			logWarning("Bad UUID");
		}

		if (userService != null && storage != null && uuid != null) {
			PlatformUser pu = null;
			ElisDataObject edo = null;
			
			try {
				edo = storage.readData(uuid);
			} catch (StorageException e) {
				pu = userService.getPlatformUser(uuid);
			}
			
			// If either edo or pu are set, then we've found something worth
			// having a look at.
			if (pu != null) {
				response = buildDeviceListResponseFrom(pu);
			} else if (edo != null) {
				// Any data object that isn't a Device or a DeviceSet are out
				// of scope for what we're trying to do here.
				if (edo instanceof Device) {
					response = buildDeviceResponseFor((Device) edo);
				} else if (edo instanceof DeviceSet) {
					response = buildDeviceResponseFor((DeviceSet) edo);
				}
			} else {
				response = ElisResponseBuilder.buildNotFoundResponse();
				logWarning("Could not find user: " + id);
			}
		} else if (response == null) {
			response = ElisResponseBuilder.buildInternalServerErrorResponse();
			logError("User service not available");
		}

		return response;
	}

	private Response buildDeviceListResponseFrom(PlatformUser pu) {
		Response response;
		User[] users = userService.getUsers(pu);
		DeviceSetBean deviceset = new DeviceSetBean();
		deviceset.puid = pu.getUserId().toString();
		deviceset.devices = getAllDevicesFor(users);
		response = ElisResponseBuilder.buildOKResponse(deviceset);
		return response;
	}

	private Response buildDeviceResponseFor(Device device) {
		Response response;
		DeviceBean bean = new DeviceBean();
		bean.id = device.getDataId().toString();
		bean.description = device.getDescription();
		bean.name = device.getName();
		response = ElisResponseBuilder.buildOKResponse(bean);
		return response;
	}

	private Response buildDeviceResponseFor(DeviceSet set) {
		Response response;
		DeviceSetBean bean = new DeviceSetBean();
		bean.puid = set.getOwnerId().toString();
		bean.devices = convertDevicesToBeans(set);
		response = ElisResponseBuilder.buildOKResponse(bean);
		return response;
	}

	private List<DeviceBean> getAllDevicesFor(User[] users) {
		List<DeviceBean> devices = new ArrayList<>();
		for (User user : users) {
			if (user instanceof GatewayUser) {
				GatewayUser gatewayUser = (GatewayUser) user;
				Gateway gateway = gatewayUser.getGateway();
				devices.addAll(convertDevicesToBeans(gateway));
			}
		}
		return devices;
	}

	private List<DeviceBean> convertDevicesToBeans(DeviceSet set) {
		List<DeviceBean> devices = new ArrayList<>();
		for (Device device : set) {
			DeviceBean bean = new DeviceBean();
			bean.id = device.getDataId().toString();
			bean.description = device.getDescription();
			bean.name = device.getName();
			devices.add(bean);
		}
		return devices;
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

	private void logWarning(String msg) {
		logThis(LogService.LOG_WARNING, msg);
	}

	private void logError(String msg) {
		logThis(LogService.LOG_ERROR, msg);
	}

	private void logThis(String msg) {
		logThis(LogService.LOG_INFO, msg);
	}

	private void logThis(int logError, String msg) {
		if (log != null)
			log.log(logError, msg);
	}
}
