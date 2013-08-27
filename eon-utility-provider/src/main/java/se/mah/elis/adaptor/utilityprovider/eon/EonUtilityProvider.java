package se.mah.elis.adaptor.utilityprovider.eon;

import java.util.HashMap;
import java.util.Map;

import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.exceptions.GatewayCommunicationException;
import se.mah.elis.adaptor.utilityprovider.api.UtilityProvider;
import se.mah.elis.adaptor.utilityprovider.eon.internal.devices.EonElectricitySampler;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;

public class EonUtilityProvider implements UtilityProvider {

	private Map<String, Gateway> userGatewayMap;
	
	public EonUtilityProvider() {
		userGatewayMap = new HashMap<String, Gateway>();
	}
	
	@Override
	public DeviceSet getDeviceSet(String user, String deviceSetId) {
		if (!userGatewayMap.containsKey(user))
			createUserGateway(user);
		
		// since we have no other devicesets at this time, return gateway
		return userGatewayMap.get(user);
	}

	private void createUserGateway(String user) {
		Gateway eonGateway = new EonGateway();
		
		try {
			eonGateway.connect(); // pre-populates gateway
		} catch (GatewayCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		userGatewayMap.put(user, eonGateway);
	}

}
