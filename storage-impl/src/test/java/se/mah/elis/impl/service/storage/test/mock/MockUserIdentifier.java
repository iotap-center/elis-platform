package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;

public class MockUserIdentifier implements UserIdentifier {

	private Class clazz;
	private int idNumber;
	private String username;
	private String password;
	
	public MockUserIdentifier() {
		clazz = se.mah.elis.services.users.User.class;
		
		idNumber = 42;
		username = "Batman";
		password = "Robin";
	}
	
	public MockUserIdentifier(int idNumber, String username, String password) {
		clazz = se.mah.elis.services.users.User.class;
		
		this.idNumber = idNumber;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public Properties getProperties() {
		Properties props = new OrderedProperties();

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

}
