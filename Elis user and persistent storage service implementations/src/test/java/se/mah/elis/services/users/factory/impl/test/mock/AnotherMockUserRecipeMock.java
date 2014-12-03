package se.mah.elis.services.users.factory.impl.test.mock;

import java.util.Properties;

import se.mah.elis.services.users.factory.UserRecipe;

public class AnotherMockUserRecipeMock implements UserRecipe {

	public AnotherMockUserRecipeMock() {
	}

	@Override
	public String getUserType() {
		return "AnotherMockUser";
	}

	@Override
	public String getServiceName() {
		return "test";
	}

	@Override
	public Properties getProperties() {
		Properties p = new Properties();
		
		return p;
	}

}
