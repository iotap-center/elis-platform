package se.mah.elis.services.users.impl.test.mock;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MockUser implements User {

	private int id;
	private String serviceUserName;
	private String servicePassword;
	
	public MockUser() {
		id = 0;
		serviceUserName = servicePassword = "";
	}
	
	public MockUser(int id, String serviceUserName, String servicePassword) {
		this.id = id;
		this.serviceUserName = serviceUserName;
		this.servicePassword = servicePassword;
	}

	@Override
	public UserIdentifier getIdentifier() {
		return new MockUserIdentifier("" + id);
	}

	@Override
	public void setIdentifier(UserIdentifier id) {
		
	}

	@Override
	public void initialize() throws UserInitalizationException {
		// TODO Auto-generated method stub

	}

	public String getServiceUserName() {
		return serviceUserName;
	}
	
	public String getServicePassword() {
		return servicePassword;
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
