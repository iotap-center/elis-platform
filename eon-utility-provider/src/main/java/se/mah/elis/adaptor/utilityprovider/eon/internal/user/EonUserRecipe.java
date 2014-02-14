package se.mah.elis.adaptor.utilityprovider.eon.internal.user;

import java.util.Properties;

import se.mah.elis.services.users.factory.UserRecipe;

public class EonUserRecipe implements UserRecipe {

	@Override
	public String getUserType() {
		return "EonUser";
	}

	@Override
	public String getServiceName() {
		return "eon";
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("email", "string");
		props.put("password", "string");
		return props;
	}

}
