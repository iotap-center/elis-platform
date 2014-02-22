package se.mah.elis.services.storage.query.test.mock;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.UserIdentifier;

public class MockPlatformUser implements PlatformUser {

	@Override
	public UserIdentifier getIdentifier() {
		return new MockUserIdentifier();
	}

	@Override
	public void setIdentifier(UserIdentifier id) {
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
	public void populate(Properties props) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFirstName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLastName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getLastName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEmail(String address) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	}

}
