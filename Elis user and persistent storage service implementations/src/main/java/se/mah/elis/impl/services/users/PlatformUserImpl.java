/**
 * 
 */
package se.mah.elis.impl.services.users;

import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

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
	private UUID uuid;
	private String firstName;
	private String lastName;
	private String email;
	private DateTime created = DateTime.now();

	/**
	 * Create a brand new, empty platform user.
	 */
	public PlatformUserImpl() {
		id = new PlatformUserIdentifierImpl();
		uuid = null;
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
		uuid = null;
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
		id = null;
		uuid = null;
		firstName = "";
		lastName = "";
		email = "";
		populate(p);
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
		String username = null;
		
		if (id != null) {
			username = ((PlatformUserIdentifier) id).getUsername();
		}
		
		return "PlatformUser " + username + " (" + getUserId() + ")";
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
			return this.id.equals(((PlatformUserImpl) o).getIdentifier()) ||
					this.getUserId() != null &&
					this.getUserId() == ((PlatformUser) o).getUserId();
		}
		
		return false;
	}

	@Override
	public int compareTo(PlatformUserImpl pu) {
		PlatformUserIdentifierImpl puId = (PlatformUserIdentifierImpl) pu.getIdentifier();
		PlatformUserIdentifierImpl thisId = (PlatformUserIdentifierImpl) id;
		
		return puId.getUsername().compareTo(thisId.getUsername());
	}
	
	private boolean validateAddress(String address) {
		if (address == null) {
			return false;
		}
		
		Matcher matcher = VALID_EMAIL.matcher(address);
		
		return matcher.find();
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties p = new OrderedProperties();
		
		if (uuid != null) {
			p.put("uuid", uuid);
		}
		p.putAll(id.getProperties());
		p.put("first_name", firstName);
		p.put("last_name", lastName);
		p.put("email", email);
		p.put("created", created);
		
		return p;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties p = id.getPropertiesTemplate();
		
		p.put("uuid", UUID.randomUUID());
		p.put("first_name", "32");
		p.put("last_name", "32");
		p.put("email", "256");
		p.put("created", created);
		
		return p;
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		// The "created" field isn't necessary for new objects
		if (props.containsKey("uuid") &&
				!(props.containsKey("created") &&
						props.get("created") instanceof DateTime)) {
			throw new IllegalArgumentException();
		}
		if (props.containsKey("identifier") &&
			props.get("identifier") instanceof PlatformUserIdentifier &&
			!(props.containsKey("created") &&
					props.get("created") instanceof DateTime)) {
			throw new IllegalArgumentException();
		}
		// The identifier part can be of two kinds: either a full Identifier
		// object or a flattened version.
		if (!((props.containsKey("identifier") &&
			   props.get("identifier") instanceof PlatformUserIdentifierImpl) ||
			(props.containsKey("username") &&
			   props.get("username") instanceof String))) {
			// Apparently not. Let's bail out.
			throw new IllegalArgumentException();
		}
		
		// OK, all properties are fine. Let's start with taking care of the
		// identifier and add that to the object.
		if (props.containsKey("identifier")) {
			id = (UserIdentifier) props.get("identifier");
		} else {
			String username = (String) props.get("username");
			String password = null;
			
			if (props.containsKey("password")) {
				password = (String) props.get("password");
			}
			
			id = new PlatformUserIdentifierImpl(username, password);
		}
		
		// Set the rest of the object's properties.
		if (props.containsKey("uuid")) {
			setUserId((UUID) props.get("uuid"));
		}
		setFirstName((String) props.get("first_name"));
		setLastName((String) props.get("last_name"));
		if (validateAddress(props.getProperty("email"))) {
			setEmail((String) props.get("email"));
		}
		
		if (props.containsKey("created")) {
			created = (DateTime) props.get("created");
		}
	}

	@Override
	public String getServiceName() {
		return "se.mah.elis.services.users.PlatformUser";
	}

	@Override
	public DateTime created() {
		return created;
	}

	@Override
	public UUID getUserId() {
		return uuid;
	}

	@Override
	public void setUserId(UUID id) {
		uuid = id;
	}
}
