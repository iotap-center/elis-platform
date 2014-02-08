package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MockUser1 implements User {

	private UserIdentifier uid;
	private UUID uuid;
	private String stuff;
	private int whatever;
	private int id;
	
	public MockUser1() {
		uid = new MockUserIdentifier();
		uuid = UUID.fromString("000011112222deadbeef555566667777");
		stuff = "";
		whatever = 0;
		
		uid.identifies(this.getClass());
	}
	
	public MockUser1(String stuff, int whatever) {
		uid = new MockUserIdentifier();
		uuid = UUID.fromString("000011112222deadbeef555566667777");
		this.stuff = stuff;
		this.whatever = whatever;
		
		uid.identifies(this.getClass());
	}
	
	public MockUser1(UUID uuid, String stuff, int whatever) {
		uid = new MockUserIdentifier();
		uuid = uuid;
		this.stuff = stuff;
		this.whatever = whatever;
		
		uid.identifies(this.getClass());
	}

	@Override
	public UserIdentifier getIdentifier() {
		return uid;
	}

	@Override
	public void setIdentifier(UserIdentifier id) {
		uid = id;
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
		Properties props = new Properties();
		
		props.put("identifier", new MockUserIdentifier());
		props.put("id", id);
		props.put("stuff", stuff);
		props.put("whatever", whatever);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("identifier", new MockUserIdentifier());
		props.put("id", 1);
		props.put("stuff", "32");
		props.put("whatever", 1);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		uid = new MockUserIdentifier();
		uuid = (UUID) props.get("uuid");
		stuff = (String) props.get("stuff");
		whatever = (int) props.get("whatever");
		id = (int) props.get("id");
	}

	@Override
	public String getServiceName() {
		return "MockUser1";
	}

	@Override
	public UUID getUserId() {
		return uuid;
	}

	@Override
	public void setUserId(UUID id) {
		this.uuid = uuid;
	}
}
