package se.mah.elis.external.control.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

@Component(name = "EndpointUtils", immediate = true)
@Service(value = EndpointUtils.class)
public class EndpointUtils {
	
	@Reference
	private UserService userService;
	
	public EndpointUtils() {}
	
	public EndpointUtils(UserService us) {
		userService = us;
	}

	public <T> List<T> getDevices(PlatformUser pu, Class type) {
		User[] users = userService.getUsers(pu);
		List<Device> devices = getDevices(users);
		List<T> meters = new ArrayList<T>();
		String info = "Seems to have found " + meters.size() + " meters:\n";
		
		for (Device device : devices) {
			if (type.isInstance(device)) {
				meters.add((T) device);
				info += device.getName() + "; " + device.getDataId() + "\n";
			}
		}
		
		return meters;
	}

	public <T> List<T> getDevices(DeviceSet set, Class type) {
		List<T> devices = new ArrayList<T>();
		
		for (Device device : set) {
			if (type.isInstance(device)) {
				devices.add((T) device);
			}
		}
		
		return devices;
	}

	private List<Device> getDevices(User[] users) {
		List<Device> devices = new ArrayList<Device>();
		for (User user : users) {
			if (user instanceof GatewayUser) {
				devices.addAll(getDevices((GatewayUser) user));
			}
		}
		return devices;
	}

	private List<Device> getDevices(GatewayUser user) {
		List<Device> devices = new ArrayList<Device>();
		for (Device device : user.getGateway()) {
			devices.add(device);
		}
		return devices;
	}
}
