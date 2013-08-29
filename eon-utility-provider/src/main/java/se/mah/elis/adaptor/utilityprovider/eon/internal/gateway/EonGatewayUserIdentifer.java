package se.mah.elis.adaptor.utilityprovider.eon.internal.gateway;

import se.mah.elis.adaptor.building.api.data.GatewayUserIdentifier;

public class EonGatewayUserIdentifer implements GatewayUserIdentifier {

	private String username; 
	private String password;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
