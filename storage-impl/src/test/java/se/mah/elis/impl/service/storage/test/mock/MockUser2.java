package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MockUser2 implements User {

	private UserIdentifier uid;
	private UUID uuid;
	private String stuff;
	private int id;
	
	public MockUser2() {
		uid = new MockUserIdentifier();
		uuid = UUID.fromString("0000deadbeef33334444555566667777");
		stuff = "";
		
		uid.identifies(this.getClass());
	}
	
	public MockUser2(String stuff) {
		uid = new MockUserIdentifier();
		uuid = UUID.fromString("0000deadbeef33334444555566667777");
		this.stuff = stuff;
		
		uid.identifies(this.getClass());
	}
	
	public MockUser2(UUID uuid, String stuff) {
		uid = new MockUserIdentifier();
		uuid = uuid;
		this.stuff = stuff;
		
		uid.identifies(this.getClass());
	}

	@Override
	public UserIdentifier getIdentifier() {
		return uid;
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
		Properties props = new Properties();
		
		props.put("identifier", new MockUserIdentifier());
		props.put("id", id);
		props.put("stuff", stuff);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("identifier", new MockUserIdentifier());
		props.put("id", 1);
		props.put("stuff", "32");
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		uid = new MockUserIdentifier();
		uuid = (UUID) props.get("uuid");
		stuff = (String) props.get("stuff");
		id = (int) props.get("id");
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
