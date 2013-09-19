package se.mah.elis.services.users.factory.impl.test.mock;

import java.util.Properties;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;
import se.mah.elis.services.users.impl.test.mock.MockUser;

public class MockUserProvider implements UserProvider {

	public MockUserProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public User build(Properties properties) throws UserInitalizationException {
		return new MockUser((String) properties.getProperty("stuff"),
				Integer.parseInt(properties.getProperty("whatever")));
	}

	@Override
	public UserRecipe getRecipe() {
		return new MockUserRecipeMock();
	}

}
