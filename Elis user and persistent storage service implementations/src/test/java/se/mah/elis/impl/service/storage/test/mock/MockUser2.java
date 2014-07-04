package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MockUser2 implements User {

	private UUID uuid;
	private int idNumber;
	private String username;
	private String password;
	private String stuff;
	private DateTime created = DateTime.now();
	
	public MockUser2() {
		uuid = UUID.fromString("0000dead-beef-3333-4444-555566667777");
		idNumber = 1;
		username = "Batman";
		password = "Robin";
		stuff = "";
	}
	
	public MockUser2(String stuff) {
		uuid = UUID.fromString("0000dead-beef-3333-4444-555566667777");
		idNumber = 1;
		username = "Batman";
		password = "Robin";
		this.stuff = stuff;
	}
	
	public MockUser2(UUID uuid, String stuff) {
		this.uuid = uuid;
		idNumber = 1;
		username = "Batman";
		password = "Robin";
		this.stuff = stuff;
	}

	@Override
	public void initialize() throws UserInitalizationException {
		// TODO Auto-generated method stub

	}

	public String getStuff() {
		return stuff;
	}
	
	public void setStuff(String stuff) {
		this.stuff = stuff;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		if (uuid != null) {
			props.put("uuid", uuid);
		}
		props.put("service_name", "test");
		props.put("id_number", idNumber);
		props.put("username", username);
		props.put("password", password);
		if (stuff != null) {
			props.put("stuff", stuff);
		}
		props.put("created", created);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", uuid);
		props.put("service_name", "9");
		props.put("id_number", new Integer(0));
		props.put("username", "32");
		props.put("password", "32");
		props.put("stuff", "32");
		props.put("created", created);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		uuid = (UUID) props.get("uuid");
		idNumber = (int) props.get("id_number");
		username = (String) props.get("username");
		password = (String) props.get("password");
		stuff = (String) props.get("stuff");
		created = (DateTime) props.get("created");
	}

	@Override
	public String getServiceName() {
		return "test";
	}

	@Override
	public UUID getUserId() {
		return uuid;
	}

	@Override
	public void setUserId(UUID id) {
		uuid = id;
	}
	
	@Override
	public String toString() {
		return "MockUser2, id: " + idNumber + ", username: " + username +
				", password: " + password + ", UUID: " + uuid +
				", stuff: " + stuff;
	}
	
	public void setCreated(DateTime dt) {
		created = dt;
	}

	@Override
	public DateTime created() {
		return created;
	}
}
