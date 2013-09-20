package se.mah.elis.services.users.impl.test.mock;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class AnotherMockUser implements User {
	
	private int id;

	public AnotherMockUser() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public UserIdentifier getIdentifier() {
		// TODO Auto-generated method stub
		return new AnotherMockUserIdentifier();
	}

	@Override
	public void setIdentifier(UserIdentifier id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() throws UserInitalizationException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getIdNumber() {
		return id;
	}

	@Override
	public void setIdNumber(int id) {
		this.id = id;
	}

}
