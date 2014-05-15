package se.mah.elis.impl.service.storage.test.mock;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class MockUser4 implements User {

	private UserIdentifier uid;
	private UUID uuid;
	private String stuff;
	private DateTime created = DateTime.now();
	private Collection<ElisDataObject> edos;
	
	public MockUser4() {
		uid = new MockUserIdentifier();
		uuid = UUID.fromString("0000dead-beef-3333-4444-555566667777");
		stuff = "";
		edos = new LinkedList<ElisDataObject>();
		
		uid.identifies(this.getClass());
	}
	
	public MockUser4(String stuff) {
		uid = new MockUserIdentifier();
		uuid = UUID.fromString("0000dead-beef-3333-4444-555566660000");
		this.stuff = stuff;
		edos = new LinkedList<ElisDataObject>();
		
		uid.identifies(this.getClass());
	}
	
	public MockUser4(UUID uuid, String stuff) {
		uid = new MockUserIdentifier();
		this.uuid = uuid;
		this.stuff = stuff;
		edos = new LinkedList<ElisDataObject>();
		
		uid.identifies(this.getClass());
	}

	@Override
	public UserIdentifier getIdentifier() {
		return uid;
	}

	@Override
	public void setIdentifier(UserIdentifier id) {
		uid = id;
		if (uid != null)
			uid.identifies(this.getClass());
	}

	@Override
	public void initialize() throws UserInitalizationException {
		// TODO Auto-generated method stub

	}

	public String getStuff() {
		return stuff;
	}
	
	public void setStuff(String stuff) {
		this.stuff = stuff;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		if (uuid != null) {
			props.put("uuid", uuid);
		}
		props.put("service_name", "MockUser1");
		props.putAll((new MockUserIdentifier()).getProperties());
		if (stuff != null) {
			props.put("stuff", stuff);
		}
		props.put("created", created);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", uuid);
		props.put("service_name", "9");
		props.putAll((new MockUserIdentifier()).getPropertiesTemplate());
		props.put("stuff", "32");
		props.put("created", created);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		uid = new MockUserIdentifier();
		uuid = (UUID) props.get("uuid");
		stuff = (String) props.get("stuff");
		created = (DateTime) props.get("created");
	}

	@Override
	public String getServiceName() {
		return "";
	}

	@Override
	public UUID getUserId() {
		return uuid;
	}

	@Override
	public void setUserId(UUID id) {
		uuid = id;
	}
	
	@Override
	public String toString() {
		return "MockUser4, uid: " + uid + ", UUID: " + uuid +
				", stuff: " + stuff;
	}
	
	public void setCreated(DateTime dt) {
		created = dt;
	}

	@Override
	public DateTime created() {
		return created;
	}
	
	public Collection<ElisDataObject> getCollection() {
		return edos;
	}
}
