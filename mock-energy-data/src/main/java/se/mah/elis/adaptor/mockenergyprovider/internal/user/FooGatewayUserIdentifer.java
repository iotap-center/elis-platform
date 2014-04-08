package se.mah.elis.adaptor.mockenergyprovider.internal.user;

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
public class FooGatewayUserIdentifer implements GatewayUserIdentifier {

	private Class identifies = FooGatewayUser.class;
	
	@Override
	public String toString() {
		return "(FooUser)";
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
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		return props;
	}

	@Override
	public void populate(Properties props) {
	}
	
}
