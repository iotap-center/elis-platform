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
		uuid = UUID.fromString("00001111-2222-dead-beef-555566667777");
		stuff = "";
		whatever = 0;
		
		uid.identifies(this.getClass());
	}
	
	public MockUser1(String stuff, int whatever) {
		this.uid = new MockUserIdentifier();
		this.uuid = UUID.fromString("00001111-2222-dead-beef-555566667777");
		this.stuff = stuff;
		this.whatever = whatever;
		
		uid.identifies(this.getClass());
	}
	
	public MockUser1(UUID uuid, String stuff, int whatever) {
		this.uid = new MockUserIdentifier();
		this.uuid = uuid;
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
	
	public void setStuff(String stuff) {
		this.stuff = stuff;
	}
	
	public void setWhatever(int whatever) {
		this.whatever = whatever;
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
		OrderedProperties props = new OrderedProperties();
		
		if (uuid != null) {
			props.put("uuid", uuid);
		}
		props.put("service_name", "MockUser1");
		if (uid != null) {
			props.putAll(uid.getProperties());
		}
		props.put("id", id);
		if (stuff != null) {
			props.put("stuff", stuff);
		}
		props.put("whatever", whatever);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();

		props.put("uuid", uuid);
		props.put("service_name", "9");
		props.putAll((new MockUserIdentifier()).getPropertiesTemplate());
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
		this.uuid = id;
	}
	
	@Override
	public String toString() {
		return "MockUser1, uid: " + uid + ", UUID: " + uuid.toString() +
				", stuff: " + stuff + ", whatever: " + whatever;
	}
}
