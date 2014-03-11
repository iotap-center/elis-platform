package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.services.users.factory.UserRecipe;

public class MockUser1Recipe implements UserRecipe {

	public MockUser1Recipe() {
	}

	@Override
	public String getUserType() {
		return "MockUser1";
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
