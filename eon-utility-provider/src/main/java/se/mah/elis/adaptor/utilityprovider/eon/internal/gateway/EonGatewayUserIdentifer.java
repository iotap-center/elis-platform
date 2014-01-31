package se.mah.elis.adaptor.utilityprovider.eon.internal.gateway;

import java.util.Properties;

import se.mah.elis.adaptor.device.api.data.GatewayUserIdentifier;
import se.mah.elis.data.OrderedProperties;

/**
 * Representation of an E.On user id
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
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

	@Override
	public Class identifies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void identifies(Class clazz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
