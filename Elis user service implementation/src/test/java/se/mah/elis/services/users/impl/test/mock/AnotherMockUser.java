package se.mah.elis.services.users.impl.test.mock;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.OrderedProperties;
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
	public OrderedProperties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populate(Properties props) {
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
