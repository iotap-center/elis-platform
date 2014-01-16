/**
 * 
 */
package se.mah.elis.impl.services.users;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.UserIdentifier;

/**
 * @author "Johan Holmberg, Malm\u00f6 University"
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
	 * Create a brand new, empty platform user.
	 */
	public PlatformUserImpl() {
		id = new PlatformUserIdentifierImpl();
		firstName = "";
		lastName = "";
		email = "";
	}
	
	/**
	 * Create a platform user with a given user identifier.
	 * 
	 * @param id The user identifier.
	 */
	public PlatformUserImpl(PlatformUserIdentifier id) {
		this.id = id;
		firstName = "";
		lastName = "";
		email = "";
	}
	
	/**
	 * Create a platform user from the info stored in the persistent storage.
	 * 
	 * @param p The user information stored in the database.
	 */
	public PlatformUserImpl(Properties p) {
		PlatformUserIdentifier id = new PlatformUserIdentifierImpl();
		id.setId((Integer) p.get("id"));
		id.setUsername(p.getProperty("username"));
		// We won't set the password form the storage
		// id.setPassword((String) p.get("password"));
		this.id = id;
		this.firstName = p.getProperty("first_name");
		this.lastName = p.getProperty("last_name");
		
	}

	/* (non-Javadoc)
	 * @see se.mah.elis.services.users.AbstractUser#getId()
	 */
	@Override
	public UserIdentifier getIdentifier() {
		return id;
	}

	/* (non-Javadoc)
	 * @see se.mah.elis.services.users.AbstractUser#setId(se.mah.elis.services.users.UserIdentifier)
	 */
	@Override
	public void setIdentifier(UserIdentifier id) {
		if (id != null) {
			this.id = id;
		} else {
			this.id = new PlatformUserIdentifierImpl();
		}
	}

	@Override
	public String toString() {
		return "PlatformUser " + ((PlatformUserIdentifierImpl) id).getUsername() +
				" (" + ((PlatformUserIdentifierImpl) id).getId() + ")";
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
			return this.id.equals(((PlatformUserImpl) o).getIdentifier());
		}
		
		return false;
	}

	@Override
	public int compareTo(PlatformUserImpl pu) {
		PlatformUserIdentifierImpl puId = (PlatformUserIdentifierImpl) pu.getIdentifier();
		PlatformUserIdentifierImpl thisId = (PlatformUserIdentifierImpl) id;
		int result = 0;
		
		if (!puId.getUsername().equals(thisId.getUsername()) &&
				thisId.getId() != puId.getId()) {
			if (thisId.getId() > puId.getId()) {
				result = 1;
			} else  {
				result = -1;
			}
		}
		
		return result;
	}
	
	private boolean validateAddress(String address) {
		Matcher matcher = VALID_EMAIL.matcher(address);
		
		return matcher.find();
	}

	@Override
	public Properties getProperties() {
		Properties p = new Properties();
		Properties ip = id.getProperties();
		
		p.put("identifier", ip);
		p.put("first_name", firstName);
		p.put("last_name", lastName);
		p.put("email", email);
		
		return p;
	}

	@Override
	public Properties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populate(Properties props) {
		// TODO Auto-generated method stub
		
	}
}
