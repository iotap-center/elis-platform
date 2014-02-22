package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;

public class MockPlatformUserIdentifier implements PlatformUserIdentifier {

	private int id;
	private String username;
	private String password;
	
	public MockPlatformUserIdentifier() {
		username = "";
		password = "";
	}
	
	public MockPlatformUserIdentifier(String username, String password) {
		id = 0;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public Class identifies() {
		return PlatformUser.class;
	}

	@Override
	public void identifies(Class clazz) {
		// TODO Auto-generated method stub

	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties p = new OrderedProperties();
		
		p.put("id", id);
		if (username != null) {
			p.put("username", username);
		}
		if (password != null) {
			p.put("password", password);
		}
		
		return p;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties p = new OrderedProperties();
		
		p.put("id", new Integer(0));
		p.put("username", "256");
		p.put("password", "256");
		
		return p;
	}

	@Override
	public void setId(int id) throws IllegalArgumentException {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
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
		return !(username.length() > 0 && password.length() > 0);
	}

}
