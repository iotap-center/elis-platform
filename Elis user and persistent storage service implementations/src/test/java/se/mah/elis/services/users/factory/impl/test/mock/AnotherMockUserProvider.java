package se.mah.elis.services.users.factory.impl.test.mock;

import java.util.Properties;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;
import se.mah.elis.services.users.impl.test.mock.AnotherMockUser;

public class AnotherMockUserProvider implements UserProvider {

	public AnotherMockUserProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public User build(Properties properties) throws UserInitalizationException {
		return new AnotherMockUser();
	}

	@Override
	public UserRecipe getRecipe() {
		return new AnotherMockUserRecipeMock();
	}

}
