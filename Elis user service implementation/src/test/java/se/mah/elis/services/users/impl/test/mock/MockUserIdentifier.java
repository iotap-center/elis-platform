package se.mah.elis.services.users.impl.test.mock;

import java.util.Properties;

import se.mah.elis.services.users.UserIdentifier;

public class MockUserIdentifier implements UserIdentifier {

	public MockUserIdentifier() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "I'm a MockUserIndentifier";
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getPropertiesTemplate() {
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
}
