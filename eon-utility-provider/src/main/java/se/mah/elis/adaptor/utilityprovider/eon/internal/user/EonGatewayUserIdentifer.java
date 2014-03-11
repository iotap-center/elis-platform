package se.mah.elis.adaptor.utilityprovider.eon.internal.user;

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
	private Class identifies = EonGatewayUser.class;
	
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
	public String toString() {
		return "(EonUser) " + username;
	}

	@Override
	public Class identifies() {
		return identifies;
	}

	@Override
	public void identifies(Class clazz) {
		identifies = clazz;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("username", username);
		props.put("password", password);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("username", 256);
		props.put("password", 256);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		username = (String) props.get("username");
		password = (String) props.get("password");
	}
	
}
