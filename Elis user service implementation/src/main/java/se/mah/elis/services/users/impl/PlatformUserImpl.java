/**
 * 
 */
package se.mah.elis.services.users.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.UserIdentifier;

/**
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class PlatformUserImpl
implements PlatformUser, Comparable<PlatformUserImpl> {

	public static final Pattern VALID_EMAIL = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
	
	private UserIdentifier id;
	private String firstName;
	private String lastName;
	private String email;

	/**
	 * 
	 */
	public PlatformUserImpl() {
		id = new PlatformUserIdentifier();
		firstName = "";
		lastName = "";
		email = "";
	}
	
	public PlatformUserImpl(UserIdentifier id) {
		this.id = id;
		firstName = "";
		lastName = "";
		email = "";
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
		return "PlatformUser " + ((PlatformUserIdentifier) id).getUsername() +
				" (" + ((PlatformUserIdentifier) id).getId() + ")";
	}

	@Override
	public void setFirstName(String name) {
		if (name == null) {
			firstName = "";
		} else {
			firstName = name;
		}
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setLastName(String name) {
		if (name == null) {
			lastName = "";
		} else {
			lastName = name;
		}
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setEmail(String address) {
		if (address == null || !validateAddress(address)) {
			email = "";
		} else {
			email = address;
		}
	}

	@Override
	public String getEmail() {
		return email;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass().getName().equals(this.getClass().getName())) {
			return this.id.equals(((PlatformUserImpl) o).getId());
		}
		
		return false;
	}

	@Override
	public int compareTo(PlatformUserImpl pu) {
		PlatformUserIdentifier puId = (PlatformUserIdentifier) pu.getId();
		PlatformUserIdentifier thisId = (PlatformUserIdentifier) id;
		
		return thisId.getId() - puId.getId();
	}
	
	private boolean validateAddress(String address) {
		Matcher matcher = VALID_EMAIL.matcher(address);
		
		return matcher.find();
	}
}
