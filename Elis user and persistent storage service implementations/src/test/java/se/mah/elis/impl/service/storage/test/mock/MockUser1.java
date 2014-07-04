package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MockUser1 implements User {

	private UUID uuid;
	private int idNumber;
	private String username;
	private String password;
	private String stuff;
	private int whatever;
	private DateTime created = DateTime.now();
	
	public MockUser1() {
		uuid = UUID.fromString("00001111-2222-dead-beef-555566667777");
		idNumber = 1;
		username = "Batman";
		password = "Robin";
		stuff = "";
		whatever = 0;
	}
	
	public MockUser1(String stuff, int whatever) {
		this.uuid = UUID.fromString("00001111-2222-dead-beef-555566667777");
		idNumber = 1;
		username = "Batman";
		password = "Robin";
		this.stuff = stuff;
		this.whatever = whatever;
	}
	
	public MockUser1(UUID uuid, String stuff, int whatever) {
		idNumber = 1;
		username = "Batman";
		password = "Robin";
		this.uuid = uuid;
		this.stuff = stuff;
		this.whatever = whatever;
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
		props.put("whatever", whatever);
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
		props.put("whatever", 1);
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
		whatever = (int) props.get("whatever");
		created = (DateTime)props.get("created");
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
		this.uuid = id;
	}
	
	@Override
	public String toString() {
		return "MockUser1, id: " + idNumber + ", username: " + username +
				", password: " + password + ", UUID: " + uuid +
				", stuff: " + stuff + ", whatever: " + whatever;
	}
	
	public void setCreated(DateTime dt) {
		created = dt;
	}

	@Override
	public DateTime created() {
		return created;
	}
	
	public void setId(int id) {
		idNumber = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
