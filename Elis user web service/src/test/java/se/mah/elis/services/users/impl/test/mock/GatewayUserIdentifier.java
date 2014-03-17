package se.mah.elis.services.users.impl.test.mock;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;

public class GatewayUserIdentifier implements UserIdentifier {

	private String id;
	
	public GatewayUserIdentifier(String id) {
		this.id = id;
	}

	public String toString() {
		return id;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("id", id);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("id", "32");
		
		return props;
	}

	@Override
	public Class identifies() {
		return GatewayUser.class;
	}

	@Override
	public void identifies(Class clazz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void populate(Properties props) {
		id =  props.getProperty("id");
	}
}
