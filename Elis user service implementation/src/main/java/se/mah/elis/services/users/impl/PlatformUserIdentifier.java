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
	
	public PlatformUserIdentifier() {
		id = 0;
	}
	
	public PlatformUserIdentifier(int id) throws IllegalArgumentException {
		if (id >= 0) {
			this.id = id;
		} else {
			this.id = 0;
			throw new IllegalArgumentException("User id can't be negative");
		}
	}

	public void setId(int id) throws IllegalArgumentException {
		if (id >= 0) {
			this.id = id;
		} else {
			throw new IllegalArgumentException("User id can't be negative");
		}
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return Integer.toString(id);
	}
}
