package se.mah.elis.adaptor.mockenergyprovider.internal.user;

import java.util.Properties;

import se.mah.elis.services.users.factory.UserRecipe;

/**
 * Describes how to create E.On users. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 */
public class FooUserRecipe implements UserRecipe {

	@Override
	public String getUserType() {
		return FooGatewayUser.class.getSimpleName();
	}

	@Override
	public String getServiceName() {
		return "mock-energy-data-provider";
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();

		return props;
	}

}
