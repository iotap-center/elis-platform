/**
 * 
 */
package se.mah.elis.services.users.impl;

import se.mah.elis.services.users.UserIdentifier;

/**
 * Implements the UserIdentifier interface.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class PlatformUserIdentifier implements UserIdentifier {

	private int id;
	private String username;
	private String password;
	
	public PlatformUserIdentifier() {
		username = "username";
		password = "password";
	}
	
	public PlatformUserIdentifier(String username, String password)
			throws IllegalArgumentException {
		id = 0;
		setUsername(username);
		setPassword(password);
	}
	
	public PlatformUserIdentifier(int id, String username, String password)
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
		if (username.length() > 0) {
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
		if (password.length() > 0) {
			this.password = password;
		} else {
			throw new IllegalArgumentException("Password can't be empty");
		}
	}
	
	@Override
	public String toString() {
		return id + ": " + username + ", " + password;
	}
}
