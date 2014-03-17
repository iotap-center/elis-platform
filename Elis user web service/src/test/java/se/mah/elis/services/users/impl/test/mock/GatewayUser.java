package se.mah.elis.services.users.impl.test.mock;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class GatewayUser implements User {

	private UUID id;
	private String serviceUserName;
	private String servicePassword;
	private DateTime created = DateTime.now();
	
	public GatewayUser() {
		id = UUID.randomUUID();
		serviceUserName = servicePassword = "";
	}
	
	public GatewayUser(UUID id, String serviceUserName, String servicePassword) {
		this.id = id;
		this.serviceUserName = serviceUserName;
		this.servicePassword = servicePassword;
	}

	@Override
	public UserIdentifier getIdentifier() {
		return new GatewayUserIdentifier("" + id);
	}

	@Override
	public void setIdentifier(UserIdentifier id) {
		
	}

	@Override
	public void initialize() throws UserInitalizationException {
		// TODO Auto-generated method stub

	}

	public String getServiceUserName() {
		return serviceUserName;
	}
	
	public String getServicePassword() {
		return servicePassword;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties properties = new OrderedProperties();
		
		properties.put("uuid", UUID.randomUUID());
		properties.put("created", created);
		properties.put("service_name", "7");
		properties.put("serviceUserName", "32");
		properties.put("servicePassword", "32");
		
		return properties;
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		id = (UUID) props.get("uuid");
		created = (DateTime) props.get("created");
		if (props.containsKey("serviceUserName")) {
			serviceUserName = props.getProperty("serviceUserName");
		}
		if (props.containsKey("servicePassword")) {
			servicePassword = props.getProperty("servicePassword");
		}
	}

	@Override
	public String getServiceName() {
		return "uwstest";
	}

	@Override
	public UUID getUserId() {
		return id;
	}

	@Override
	public void setUserId(UUID id) {
		this.id = id;
	}

	@Override
	public DateTime created() {
		return created;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties properties = new OrderedProperties();

		properties.put("uuid", id);
		properties.put("created", created);
		properties.put("service_name", "uwstest");
		properties.put("serviceUserName", serviceUserName);
		properties.put("servicePassword", servicePassword);
		
		return properties;
	}
}
