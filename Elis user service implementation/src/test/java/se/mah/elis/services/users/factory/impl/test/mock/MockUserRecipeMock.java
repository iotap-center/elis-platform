package se.mah.elis.services.users.factory.impl.test.mock;

import java.util.Properties;

import se.mah.elis.services.users.factory.UserRecipe;

public class MockUserRecipeMock implements UserRecipe {

	public MockUserRecipeMock() {
	}

	@Override
	public String getUserType() {
		return "MockUser";
	}

	@Override
	public String getServiceName() {
		return "test";
	}

	@Override
	public Properties getProperties() {
		Properties p = new Properties();
		
		p.put("stuff", "string");
		p.put("whatever", "integer");
		
		return p;
	}

}
