/**
 * 
 */
package se.mah.elis.impl.services.users;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.mah.elis.data.OrderedProperties;
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
	public OrderedProperties getProperties() {
		OrderedProperties p = new OrderedProperties();
		
		p.put("identifier", id);
		p.put("first_name", firstName);
		p.put("last_name", lastName);
		p.put("email", email);
		
		return p;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties p = id.getPropertiesTemplate();
		
		p.put("first_name", "32");
		p.put("last_name", "32");
		p.put("email", "256");
		
		return p;
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		// Check the properties. Are they OK?
		if (!props.containsKey("first_name") ||
				!(props.get("first_name") instanceof String) ||
				((String) props.get("first_name")).length() < 1 ||
				!props.containsKey("last_name") ||
				!(props.get("last_name") instanceof String) ||
				((String) props.get("last_name")).length() < 1 ||
				!props.containsKey("email") ||
				!(props.get("email") instanceof String) ||
				!validateAddress((String) props.get("email"))) {
			// Apparently not. Let's bail out.
			throw new IllegalArgumentException();
		}
		// The identifier part can be of two kinds: either a full Identifier
		// object or a flattened version.
		if (!((props.containsKey("identifier") &&
			   props.get("identifier") instanceof PlatformUserIdentifierImpl) ||
			(((props.containsKey("id")) &&
			   props.get("id") instanceof Integer) ||
			   !props.containsKey("id")) &&
			   props.containsKey("username") &&
			   props.get("username") instanceof String)) {
			// Apparently not. Let's bail out.
			throw new IllegalArgumentException();
		}
//		// The password part is tricky. We'll deal with that separately for
//		// the sake of readability.
//		if ((props.containsKey("password") &&
//				!(props.get("password") instanceof String &&
//				((String) props.get("password")).length() > 0))) {
//			// If there is a password, and it isn't valid, then let's bail out.
//			throw new IllegalArgumentException();
//		}
			
		
		// OK, all properties are fine. Let's start with taking care of the
		// identifier and add that to the object.
		if (props.containsKey("identifier")) {
			id = (UserIdentifier) props.get("identifier");
		} else {
			int idNumber = 0;
			String username = (String) props.get("username");
			String password = null;
			
			if (props.containsKey("id")) {
				idNumber = (int) props.get("id");
				if (idNumber < 0)
					idNumber = 0;
			}
			
			if (props.containsKey("password")) {
				password = (String) props.get("password");
			}
			
			id = new PlatformUserIdentifierImpl(idNumber, username, password);
		}
		
		// Set the rest of the object's properties.
		firstName = (String) props.get("first_name");
		lastName = (String) props.get("last_name");
		email = (String) props.get("email");
	}

	@Override
	public String getServiceName() {
		return "se.mah.elis.services.users.PlatformUser";
	}
}
