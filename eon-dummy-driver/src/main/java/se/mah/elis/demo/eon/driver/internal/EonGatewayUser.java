package se.mah.elis.demo.eon.driver.internal;

import se.mah.elis.adaptor.building.api.data.GatewayUserIdentifier;
import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;

public class EonGatewayUser implements GatewayUser {

	private Gateway gateway;
	private GatewayUserIdentifier gatewayUserIdentifier;
	
	public Gateway getGateway() {
		return this.gateway;
	}

	public GatewayUserIdentifier getId() {
		return gatewayUserIdentifier;
	}

	public void setGateway(Gateway arg0) {
		this.gateway = arg0;
	}

	public void setId(GatewayUserIdentifier arg0) {
		this.gatewayUserIdentifier = arg0;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof EonGatewayUser))
			return false;
		return getId().equals(o);
	}
	
}
