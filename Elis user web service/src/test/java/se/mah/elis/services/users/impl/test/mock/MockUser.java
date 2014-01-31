package se.mah.elis.services.users.impl.test.mock;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.OrderedProperties;
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

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUserId(UUID id) {
		// TODO Auto-generated method stub
		
	}
}
