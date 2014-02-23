package se.mah.elis.services.storage.query.test.mock;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;

public class MockUserIdentifier implements UserIdentifier {

	Class clazz;
	
	public MockUserIdentifier() {
		clazz = se.mah.elis.services.users.User.class;
	}
	
	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("id_number", 42);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("id_number", new Integer(0));
		
		return props;
	}

	@Override
	public Class identifies() {
		return clazz;
	}

	@Override
	public void identifies(Class clazz) {
		this.clazz = clazz;
	}

	@Override
	public String toString() {
		String output = this.getClass().getSimpleName() +
						": id_number: 42";
		
		return output;
	}

	@Override
	public void populate(Properties props) {
		// TODO Auto-generated method stub
		
	}
}
