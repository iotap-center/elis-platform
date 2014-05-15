package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.services.users.factory.UserRecipe;

public class MockUser4Recipe implements UserRecipe {

	public MockUser4Recipe() {
	}

	@Override
	public String getUserType() {
		return "MockUser4";
	}

	@Override
	public String getServiceName() {
		return "test";
	}

	@Override
	public Properties getProperties() {
		Properties p = new Properties();
		
		p.put("stuff", "string");
		
		return p;
	}

}
