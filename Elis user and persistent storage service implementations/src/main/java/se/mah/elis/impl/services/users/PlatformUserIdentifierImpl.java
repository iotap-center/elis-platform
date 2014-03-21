/**
 * 
 */
package se.mah.elis.impl.services.users;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;

/**
 * Implements the UserIdentifier interface.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public class PlatformUserIdentifierImpl implements PlatformUserIdentifier {

	private String username;
	private String password;
	
	public PlatformUserIdentifierImpl() {
		username = "";
		password = "";
	}
	
	public PlatformUserIdentifierImpl(String username, String password)
			throws IllegalArgumentException {
		setUsername(username);
		setPassword(password);
	}

	public void setUsername(String username) throws IllegalArgumentException {
		if (username != null) {
			username = username.trim();
		} else {
			throw new IllegalArgumentException("User name can't be empty");
		}
		
		if (!username.isEmpty()) {
			this.username = username;
		} else {
			throw new IllegalArgumentException("User name can't be empty");
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password != null) {
			password = password.trim();
			
			if (!password.isEmpty()) {
				this.password = password;
			} else {
				throw new IllegalArgumentException("Password can't be empty");
			}
		} else {
			this.password = null;
		}
	}
	
	public boolean isEmpty() {
		return username.isEmpty() || password.isEmpty();
	}
	
	@Override
	public String toString() {
		return "PlatformUserIdentifier: " + username;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass().getName().equals(this.getClass().getName())) {
			PlatformUserIdentifierImpl oid = (PlatformUserIdentifierImpl) o;
			return username.equals(oid.getUsername());
		}
		
		return false;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties p = new OrderedProperties();
		
		p.put("username", username);
		if (password != null) {
			p.put("password", password);
		} else {
			p.put("password", "");
		}
		
		return p;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties p = new OrderedProperties();
		
		p.put("username", "256");
		p.put("password", "256");
		
		return p;
	}

	@Override
	public Class<PlatformUser> identifies() {
		return se.mah.elis.services.users.PlatformUser.class;
	}

	@Override
	public void identifies(Class clazz) {
		// Don't do anything at all. This is not applicable for the
		// PlatformUserIdentifier.
	}

	@Override
	public void populate(Properties props) {
		if (props.containsKey("username")) {
			setUsername(props.getProperty("username"));
		}
		if (props.containsKey("password")) {
			setPassword(props.getProperty("password"));
		}
	}
}
