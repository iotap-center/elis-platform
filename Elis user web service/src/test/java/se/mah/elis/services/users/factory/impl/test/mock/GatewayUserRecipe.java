package se.mah.elis.services.users.factory.impl.test.mock;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.services.users.factory.UserRecipe;

public class GatewayUserRecipe implements UserRecipe {

	public GatewayUserRecipe() {
	}

	@Override
	public String getUserType() {
		return "GatewayUser";
	}

	@Override
	public String getServiceName() {
		return "uwstest";
	}

	@Override
	public Properties getProperties() {
		Properties p = new Properties();

		p.put("id", UUID.randomUUID());
		p.put("serviceUserName", "string");
		p.put("servicePassword", "string");
		
		return p;
	}

}
