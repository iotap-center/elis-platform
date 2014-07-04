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

/**
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 1.0
 */
public class PlatformUserImpl
		implements PlatformUser, Comparable<PlatformUserImpl> {

	public static final Pattern VALID_EMAIL = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);

	private String username;
	private String password;
	private UUID uuid;
	private String firstName;
	private String lastName;
	private String email;
	private DateTime created = DateTime.now();

	/**
	 * Create a brand new, empty platform user.
	 */
	public PlatformUserImpl() {
		uuid = null;
		username = "";
		password = null;
		firstName = "";
		lastName = "";
		email = "";
	}
	
	/**
	 * Create a platform user with a username and a password.
	 * 
	 * @param username The username to use.
	 * @param password The password to use.
	 */
	public PlatformUserImpl(String username, String password) {
		uuid = null;
		this.username = username;
		this.password = password;
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
		uuid = null;
		username = "";
		password = null;
		firstName = "";
		lastName = "";
		email = "";
		populate(p);
	}

	@Override
	public String toString() {
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
			return this.getUserId() != null &&
				   this.getUserId().equals(((PlatformUser) o).getUserId());
		}
		
		return false;
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
		p.put("username", username);
		if (password != null) {
			p.put("password", password);
		} else {
			p.put("password", "");
		}
		p.put("first_name", firstName);
		p.put("last_name", lastName);
		p.put("email", email);
		p.put("created", created);
		
		return p;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties p = new OrderedProperties();
		
		p.put("username", "256");
		p.put("password", "256");
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
		if (!(props.containsKey("created") &&
					props.get("created") instanceof DateTime)) {
			throw new IllegalArgumentException();
		}
		// Let's see if the username is OK
		String un = (String) props.get("username");
		if (un == null || un.isEmpty()) {
			// Apparently not. Let's bail out.
			throw new IllegalArgumentException();
		}
		
		// OK, all properties are fine. Let's start with taking care of the
		// identifier and add that to the object.
		username = un;
		password = null;
		
		if (props.containsKey("password") && props.get("password") != null &&
				!props.getProperty("password").isEmpty()) {
			password = (String) props.get("password");
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
	public int compareTo(PlatformUserImpl pu) {
		if (pu != null) {
			return pu.getUsername().compareTo(username);
		}
		
		return 0;
	}
}
