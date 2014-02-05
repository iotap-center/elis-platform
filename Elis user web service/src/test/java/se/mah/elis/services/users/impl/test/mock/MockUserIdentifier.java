package se.mah.elis.services.users.impl.test.mock;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;

public class MockUserIdentifier implements UserIdentifier {

	private String id;
	
	public MockUserIdentifier(String id) {
		this.id = id;
	}

	public String toString() {
		return id;
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
	public void populate(Properties props) {
		// TODO Auto-generated method stub
		
	}
}
