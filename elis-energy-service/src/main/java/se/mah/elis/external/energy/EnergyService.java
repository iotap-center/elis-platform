package se.mah.elis.external.energy;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.external.energy.beans.EnergyBean;
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
	@Path("{puid}/now")
	public Response getCurrentEnergyConsumption(@PathParam("puid") String puid) {
		ResponseBuilder response = null;
		
		if (userService != null) {
			PlatformUser pu = userService.getPlatformUser(puid);
			if (pu != null)
				response = buildCurrentEnergyConsumptionResponse("now", pu);
			else
				response = Response.status(Response.Status.NOT_FOUND);	
		} else
			response = Response.serverError();

		return response.build();
	}

	private ResponseBuilder buildCurrentEnergyConsumptionResponse(
			String period, PlatformUser pu) {
		List<Device> availableEnergyMeters = getMeters(pu);
		EnergyBean bean = EnergyBeanFactory.create(availableEnergyMeters, period, pu);
		return Response.ok(gson.toJson(bean));
	}

	private List<Device> getMeters(PlatformUser pu) {
		User[] users = userService.getUsers(pu);
		List<Device> meters = getDevices(users);
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
			if (user instanceof GatewayUser) 
				meters.addAll(getMeters((GatewayUser) user));
		}
		return meters;
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