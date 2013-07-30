package se.mah.elis.demo.eon.driver.internal;

import java.util.ArrayList;
import java.util.List;

import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.demo.eon.driver.BMSProviderService;

public class EonService implements BMSProviderService {

	private List<GatewayUser> users;
	private DeviceSet onlyset;
	
	public EonService() throws StaticEntityException {
		users = new ArrayList<GatewayUser>();
		
		ElectricitySampler device = new EonDevice();
		device.setId(new EonDeviceIdentifier(1));
		
		Gateway gateway = new EonGateway();
		gateway.add(device);
		
		GatewayUser user = new EonGatewayUser();
		user.setGateway(gateway);
		user.setId(new EonGatewayUserIdentifier("eon-testuser"));
		gateway.setUser(user);
		
		this.onlyset = gateway;
	}
	
	public DeviceSet getDeviceSet(String user, String deviceSetId) {
		return this.onlyset;
	}
	
}
