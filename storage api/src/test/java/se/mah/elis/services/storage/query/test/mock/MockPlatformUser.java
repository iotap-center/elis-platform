package se.mah.elis.services.storage.query.test.mock;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.PlatformUser;

public class MockPlatformUser implements PlatformUser {

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

	@Override
	public DateTime created() {
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

	@Override
	public void setUsername(String username) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		return "id_number: 42";
	}

}
