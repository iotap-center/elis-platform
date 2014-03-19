package se.mah.elis.services.users.impl.test.mock;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;

public class MockUserIdentifier implements UserIdentifier {

	private Class clazz;
	private int idNumber;
	private String username;
	private String password;
	
	public MockUserIdentifier() {
		clazz = MockUser.class;
		
		idNumber = 1;
		username = "Batman";
		password = "Robin";
	}
	
	public MockUserIdentifier(int idNumber, String username, String password) {
		clazz = MockUser.class;
		
		this.idNumber = idNumber;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();

		props.put("id_number", idNumber);
		props.put("username", username);
		props.put("password", password);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("id_number", new Integer(0));
		props.put("username", "32");
		props.put("password", "32");
		
		return props;
	}

	@Override
	public Class identifies() {
		return clazz;
	}

	@Override
	public void identifies(Class clazz) {
		this.clazz = clazz;
	}

	@Override
	public void populate(Properties props) {
		idNumber = (int) props.get("id_number");
		username = (String) props.get("username");
		password = (String) props.get("password");
	}

	@Override
	public String toString() {
		return "I'm a MockUserIndentifier";
	}

}