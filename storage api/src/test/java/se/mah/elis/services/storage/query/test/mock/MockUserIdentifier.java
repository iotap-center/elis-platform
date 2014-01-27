package se.mah.elis.services.storage.query.test.mock;

import java.util.Properties;

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
		
		return props;
	}

	@Override
	public Properties getPropertiesTemplate() {
		Properties props = new Properties();
		
		props.put("id_number", new Integer(0));
		
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
