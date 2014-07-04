package se.mah.elis.services.storage.query.test.mock;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MockUser implements User {

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
	public UUID getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUserId(UUID id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() throws UserInitalizationException {
		// TODO Auto-generated method stub

	}

	@Override
	public DateTime created() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return "id_number: 42";
	}

}
