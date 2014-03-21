package se.mah.elis.adaptor.water.mkb;

import java.util.Properties;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.users.UserIdentifier;

/**
 * User identifier for MKB users. Based on the meter id. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
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
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
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
