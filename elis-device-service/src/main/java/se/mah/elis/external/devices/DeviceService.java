package se.mah.elis.external.devices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.external.devices.beans.DeviceBean;
import se.mah.elis.external.devices.beans.DeviceSetBean;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

@Path("/users/{id}")
@Produces("application/json")
@Component(name = "ElisDeviceService", immediate = true)
@Service(value = DeviceService.class)
public class DeviceService {

	private Gson gson;
	
	@Reference
	private UserService userService;
	
	public DeviceService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}
	
	@GET
	@Path("/devices")
	public Response getDeviceList(@PathParam("id") String id) {
		ResponseBuilder response = null;
		
		if (userService != null) {
			PlatformUser pu = userService.getPlatformUser(id);
			if (pu != null) {
				response = buildDeviceListResponseFrom(pu);
			} else {
				response = Response.status(Response.Status.NOT_FOUND);
			}
		} else {
			response = Response.serverError();
		}
		
		return response.build();
	}

	private ResponseBuilder buildDeviceListResponseFrom(PlatformUser pu) {
		ResponseBuilder response;
		User[] users = userService.getUsers(pu);
		DeviceSetBean deviceset = new DeviceSetBean();
		deviceset.devices = getAllDevicesFor(users);
		response = Response.ok(gson.toJson(deviceset));
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
	
	private List<DeviceBean> convertDevicesToBeans(Gateway gateway) {
		List<DeviceBean> devices = new ArrayList<>();
		for (Device device : gateway) {
			DeviceBean bean = new DeviceBean();
			bean.id = device.getId().toString();
			bean.description = device.getDescription();
			bean.name = device.getName();
			devices.add(bean);
		}
		return devices;
	}

	protected void bindUserService(UserService us) {
		this.userService = us;
		System.out.println("DeviceService bound to UserService");
	}
	
	protected void unbindUserService(UserService us) {
		this.userService = null;
	}
}