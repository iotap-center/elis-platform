package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.services.users.factory.UserRecipe;

public class MockUser2Recipe implements UserRecipe {

	public MockUser2Recipe() {
	}

	@Override
	public String getUserType() {
		return "MockUser2";
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
