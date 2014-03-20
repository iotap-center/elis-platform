package se.mah.elis.adaptor.water.mkb;

import java.util.Properties;

import se.mah.elis.services.users.factory.UserRecipe;

/**
 * Describes how to create MKB users. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
public class MkbUserRecipe implements UserRecipe {

	@Override
	public String getUserType() {
		return "MkbWaterUser";
	}

	@Override
	public String getServiceName() {
		return "mkb-water";
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("id", "string");
		return props;
	}

}
