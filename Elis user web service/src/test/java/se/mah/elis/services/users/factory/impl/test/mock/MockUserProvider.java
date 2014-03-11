package se.mah.elis.services.users.factory.impl.test.mock;

import java.util.Properties;
import java.util.UUID;

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
		UUID id = UUID.randomUUID();
		if (properties.containsKey("id")) {
			id = (UUID) properties.get("id");
		}
		System.out.println("hello");
		String username = properties.getProperty("serviceUserName");
		String password = properties.getProperty("servicePassword");
		
		return new MockUser(id, username, password);
	}

	@Override
	public UserRecipe getRecipe() {
		return new MockUserRecipe();
	}

}
