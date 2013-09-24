package se.mah.elis.services.users.factory.impl.test.mock;

import java.util.Properties;

import se.mah.elis.services.users.factory.UserRecipe;

public class MockUserRecipe implements UserRecipe {

	public MockUserRecipe() {
	}

	@Override
	public String getUserType() {
		return "GatewayUser";
	}

	@Override
	public String getServiceName() {
		return "Waynecorp";
	}

	@Override
	public Properties getProperties() {
		Properties p = new Properties();

		p.put("id", "string");
		p.put("serviceUserName", "string");
		p.put("servicePassword", "string");
		
		return p;
	}

}
