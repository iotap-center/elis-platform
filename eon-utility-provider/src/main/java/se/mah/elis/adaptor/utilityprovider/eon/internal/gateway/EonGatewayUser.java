package se.mah.elis.adaptor.utilityprovider.eon.internal.gateway;

import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class EonGatewayUser implements GatewayUser {

	private Gateway gateway;
	private EonGatewayUserIdentifer gatewayUserIdentifier;

	@Override
	public void initialize() throws UserInitalizationException {
		// empty for now
	}

	@Override
	public UserIdentifier getId() {
		return gatewayUserIdentifier;
	}

	@Override
	public void setId(UserIdentifier userIdentifier) {
		gatewayUserIdentifier = (EonGatewayUserIdentifer) userIdentifier;
	}

	@Override
	public Gateway getGateway() {
		return gateway;
	}

	@Override
	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

}
