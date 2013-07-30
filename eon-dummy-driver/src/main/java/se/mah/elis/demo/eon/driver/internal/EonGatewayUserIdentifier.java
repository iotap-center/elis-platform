package se.mah.elis.demo.eon.driver.internal;

import se.mah.elis.adaptor.building.api.data.GatewayUserIdentifier;

public class EonGatewayUserIdentifier implements GatewayUserIdentifier {

	private String username;
	
	public EonGatewayUserIdentifier(String username) {
		setUsername(username);
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof EonGatewayUserIdentifier))
			return false;
		return this.username.equals(o);
	}
}
