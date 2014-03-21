package se.mah.elis.adaptor.energy.eon.internal.user;

import java.util.Properties;

import se.mah.elis.services.users.factory.UserRecipe;

/**
 * Describes how to create E.On users. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 */
public class EonUserRecipe implements UserRecipe {

	@Override
	public String getUserType() {
		return EonGatewayUser.class.getSimpleName();
	}

	@Override
	public String getServiceName() {
		return "eon";
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("username", "256");
		props.put("password", "256");
		return props;
	}

}
