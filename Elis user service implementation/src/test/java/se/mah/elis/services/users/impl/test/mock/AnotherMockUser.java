package se.mah.elis.services.users.impl.test.mock;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class AnotherMockUser implements User {
	
	public static final String MOCK_UUID = "54947df8-0e9e-4471-a2f9-9af509fb5889";
	private int id;
	private UUID uuid;

	public AnotherMockUser() {
		uuid = UUID.fromString(MOCK_UUID);
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
		return uuid;
	}

	@Override
	public void setUserId(UUID id) {
		this.uuid = id;
	}

	@Override
	public DateTime created() {
		// TODO Auto-generated method stub
		return null;
	}

}
