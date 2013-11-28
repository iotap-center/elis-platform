/**
 * 
 */
package se.mah.elis.impl.services.users;

import se.mah.elis.services.users.PlatformUserIdentifier;

/**
 * Implements the UserIdentifier interface.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class PlatformUserIdentifierImpl implements PlatformUserIdentifier {

	private int id;
	private String username;
	private String password;
	
	public PlatformUserIdentifierImpl() {
		username = "";
		password = "";
	}
	
	public PlatformUserIdentifierImpl(String username, String password)
			throws IllegalArgumentException {
		id = 0;
		setUsername(username);
		setPassword(password);
	}
	
	public PlatformUserIdentifierImpl(int id, String username, String password)
			throws IllegalArgumentException {
		setId(id);
		setUsername(username);
		setPassword(password);
	}
	
	public void setId(int id) throws IllegalArgumentException {
		if (id > 0) {
			this.id = id;
		} else {
			throw new IllegalArgumentException("Id can't be zero");
		}
	}
	
	public int getId() {
		return id;
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
		} else {
			throw new IllegalArgumentException("Password can't be empty");
		}
		
		if (!password.isEmpty()) {
			this.password = password;
		} else {
			throw new IllegalArgumentException("Password can't be empty");
		}
	}
	
	public boolean isEmpty() {
		return username.isEmpty() || password.isEmpty();
	}
	
	@Override
	public String toString() {
		return id + ": " + username + ", " + password;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass().getName().equals(this.getClass().getName())) {
			PlatformUserIdentifierImpl oid = (PlatformUserIdentifierImpl) o;
			return (id == oid.getId() || username.equals(oid.getUsername()));
		}
		
		return false;
	}
}