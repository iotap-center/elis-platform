package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;

public class EmptyMockUserIdentifier implements UserIdentifier {

	Class clazz;
	
	public EmptyMockUserIdentifier() {
		clazz = se.mah.elis.services.users.User.class;
	}

	
	public EmptyMockUserIdentifier(Class clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();

		props.put("id_number", 0);
		props.put("username", "");
		props.put("password", "");
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("id_number", new Integer(0));
		props.put("username", "32");
		props.put("password", "32");
		
		return props;
	}

	@Override
	public Class identifies() {
		return clazz;
	}

	@Override
	public void identifies(Class clazz) {
		this.clazz = clazz;
	}

}
