package se.mah.elis.adaptor.water.mkb;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;

public class MkbUserIdentifier implements UserIdentifier {

	private String meterId;
	private Class clazz;

	public MkbUserIdentifier(String meterId) {
		this.meterId = meterId;
	}
	
	public String getMeterId() {
		return meterId;
	}
	
	@Override
	public String toString() {
		return this.meterId;
	}
	
	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("meterId", meterId);
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("meterId", "32");
		return props;
	}

	@Override
	public void populate(Properties props) {
		this.meterId = (String) props.getProperty("meterId");
	}

	@Override
	public Class identifies() {
		return clazz;
	}

	@Override
	public void identifies(Class clazz) {
		this.clazz = clazz;
	}

}
