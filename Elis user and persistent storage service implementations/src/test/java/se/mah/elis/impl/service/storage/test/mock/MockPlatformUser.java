package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.PlatformUser;

public class MockPlatformUser implements PlatformUser {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private UUID uuid;
	private DateTime created = DateTime.now();

	/**
	 * Create a brand new, empty platform user.
	 */
	public MockPlatformUser() {
		uuid = null;
		username = "";
		password = null;
		firstName = "";
		lastName = "";
		email = "";
	}

	/**
	 * Create a brand new, empty platform user.
	 */
	public MockPlatformUser(String username, String password) {
		uuid = null;
		this.username = username;
		this.password = password;
		firstName = "";
		lastName = "";
		email = "";
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		if (uuid != null) {
			props.put("uuid", uuid);
		}
		props.put("username", username);
		props.put("password", password);
		props.put("first_name", firstName);
		props.put("last_name", lastName);
		props.put("email", email);
		props.put("created", created);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
	}

	@Override
	public String getServiceName() {
		return "se.mah.elis.service.user.PlatformUser";
	}

	@Override
	public void setFirstName(String name) {
		firstName = name;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setLastName(String name) {
		lastName = name;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setEmail(String address) {
		email = address;
	}

	@Override
	public String getEmail() {
		return email;
	}
	
	public void setCreated(DateTime dt) {
		created = dt;
	}

	@Override
	public DateTime created() {
		return created;
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
	public void setUsername(String username) throws IllegalArgumentException {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
