package se.mah.elis.services.users.impl.test.mock;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class AnotherMockUser implements User {

	public AnotherMockUser() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public UserIdentifier getId() {
		// TODO Auto-generated method stub
		return new AnotherMockUserIdentifier();
	}

	@Override
	public void setId(UserIdentifier id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() throws UserInitalizationException {
		// TODO Auto-generated method stub

	}

}
