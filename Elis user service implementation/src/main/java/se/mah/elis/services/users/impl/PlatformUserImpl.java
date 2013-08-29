/**
 * 
 */
package se.mah.elis.services.users.impl;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.UserIdentifier;

/**
 * @author "Johan Holmberg, Malmö University"
 *
 */
public class PlatformUserImpl implements PlatformUser {
	
	private UserIdentifier id;

	/**
	 * 
	 */
	public PlatformUserImpl() {
		id = new PlatformUserIdentifier();
	}
	
	public PlatformUserImpl(UserIdentifier id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see se.mah.elis.services.users.AbstractUser#getId()
	 */
	@Override
	public UserIdentifier getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see se.mah.elis.services.users.AbstractUser#setId(se.mah.elis.services.users.UserIdentifier)
	 */
	@Override
	public void setId(UserIdentifier id) {
		if (id != null) {
			this.id = id;
		} else {
			this.id = new PlatformUserIdentifier();
		}
	}

	@Override
	public String toString() {
		return "PlatformUser " + ((PlatformUserIdentifier) id).getUsername();
	}
}
