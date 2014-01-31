package se.mah.elis.services.users.impl.test.mock;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MockUser implements User {

	public static final String MOCK_UUID = "067e6162-3b6f-4ae2-a171-2470b63dff00";
	private String stuff;
	private int whatever;
	private int id;
	private UUID uuid;
	
	public MockUser() {
		stuff = "";
		whatever = 0;
		uuid = UUID.fromString(MockUser.MOCK_UUID);
	}
	
	public MockUser(String stuff, int whatever) {
		this.stuff = stuff;
		this.whatever = whatever;
	}

	@Override
	public UserIdentifier getIdentifier() {
		// TODO Auto-generated method stub
		return new MockUserIdentifier();
	}

	@Override
	public void setIdentifier(UserIdentifier id) {
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
}
