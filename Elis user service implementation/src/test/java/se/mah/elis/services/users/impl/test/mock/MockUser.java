package se.mah.elis.services.users.impl.test.mock;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MockUser implements User {

	private String stuff;
	private int whatever;
	
	public MockUser() {
		stuff = "";
		whatever = 0;
	}
	
	public MockUser(String stuff, int whatever) {
		this.stuff = stuff;
		this.whatever = whatever;
	}

	@Override
	public UserIdentifier getId() {
		// TODO Auto-generated method stub
		return new MockUserIdentifier();
	}

	@Override
	public void setId(UserIdentifier id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() throws UserInitalizationException {
		// TODO Auto-generated method stub

	}

	public String getStuff() {
		return stuff;
	}
	
	public int getWhatever() {
		return whatever;
	}
}
