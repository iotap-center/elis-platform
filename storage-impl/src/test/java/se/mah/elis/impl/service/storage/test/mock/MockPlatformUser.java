package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.UserIdentifier;

public class MockPlatformUser implements PlatformUser {

	private PlatformUserIdentifier id;
	private String firstName;
	private String lastName;
	private String email;
	private DateTime created = DateTime.now();

	/**
	 * Create a brand new, empty platform user.
	 */
	public MockPlatformUser() {
		id = new MockPlatformUserIdentifier();
		firstName = "";
		lastName = "";
		email = "";
	}
	
	/**
	 * Create a platform user with a given user identifier.
	 * 
	 * @param id The user identifier.
	 */
	public MockPlatformUser(PlatformUserIdentifier id) {
		this.id = id;
		firstName = "";
		lastName = "";
		email = "";
	}
	
	@Override
	public UserIdentifier getIdentifier() {	
		return id;
	}

	@Override
	public void setIdentifier(UserIdentifier id) {
		this.id = (PlatformUserIdentifier) id;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		if (id != null) {
			props.putAll(id.getProperties());
		}
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
		PlatformUserIdentifier puid = new MockPlatformUserIdentifier();
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

}