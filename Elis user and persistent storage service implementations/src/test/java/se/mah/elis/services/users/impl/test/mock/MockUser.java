package se.mah.elis.services.users.impl.test.mock;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MockUser implements User {

	public static final UUID MOCK_UUID =
			UUID.fromString("00001111-2222-dead-beef-555566667777");
	
	private UUID uuid;
	private int idNumber;
	private String username;
	private String password;
	private String stuff;
	private int whatever;
	private DateTime created = DateTime.now();
	
	public MockUser() {
		uuid = MOCK_UUID;
		idNumber = 1;
		username = "Batman";
		password = "Robin";
		stuff = "";
		whatever = 0;
	}
	
	public MockUser(String stuff, int whatever) {
		this.uuid = MOCK_UUID;
		idNumber = 1;
		username = "Batman";
		password = "Robin";
		this.stuff = stuff;
		this.whatever = whatever;
	}
	
	public MockUser(UUID uuid, String stuff, int whatever) {
		this.uuid = uuid;
		idNumber = 1;
		username = "Batman";
		password = "Robin";
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
		created = (DateTime)props.get("created");
		idNumber = (int) props.get("id_number");
		username = (String) props.get("username");
		password = (String) props.get("password");
		if (props.containsKey("stuff")) {
			stuff = (String) props.get("stuff");
		}
		if (props.containsKey("whatever")) {
			whatever = (int) props.get("whatever");
		}
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
		return "MockUser, id: " + idNumber + ", username: " + username +
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
}
