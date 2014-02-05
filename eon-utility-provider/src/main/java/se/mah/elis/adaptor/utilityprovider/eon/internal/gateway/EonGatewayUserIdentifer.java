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
	private Class clazz;
	
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
		return clazz;
	}

	@Override
	public void identifies(Class clazz) {
		this.clazz = clazz;
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("username", this.username);
		props.put("password", this.password);
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("username", "256");
		props.put("password", "256");
		return props;
	}

	@Override
	public void populate(Properties props) {
		this.username = (String) props.get("username");
		if (props.contains("password"))
			this.password = (String) props.get("password");
	}
	
}
