package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;

public class MockUserIdentifier implements UserIdentifier {

	Class clazz;
	
	public MockUserIdentifier() {
		clazz = se.mah.elis.services.users.User.class;
	}
	
	@Override
	public Properties getProperties() {
		Properties props = new Properties();

		props.put("id_number", 42);
		props.put("username", "batman");
		props.put("password", "");
		
		return props;
	}

	@Override
	public Properties getPropertiesTemplate() {
		Properties props = new OrderedProperties();
		
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
