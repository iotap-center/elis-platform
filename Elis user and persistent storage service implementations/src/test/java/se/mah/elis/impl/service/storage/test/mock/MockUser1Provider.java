package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;

public class MockUser1Provider implements UserProvider {

	public MockUser1Provider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public User build(Properties properties) throws UserInitalizationException {
		MockUser1 user = new MockUser1();
		
		user.populate(properties);
		
		return user;
	}

	@Override
	public UserRecipe getRecipe() {
		return new MockUser1Recipe();
	}

}
