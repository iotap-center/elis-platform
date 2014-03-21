package se.mah.elis.adaptor.energy.eon.internal.devices;

import java.util.Properties;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.data.OrderedProperties;

/**
 * Identifier for E.On devices. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 */
public class EonDeviceIdentifier implements DeviceIdentifier {
	private String id; 
	
	public EonDeviceIdentifier(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof EonDeviceIdentifier)
			return this.getId().equals( ((EonDeviceIdentifier)o).getId() );
		return false;
	}

	@Override
	public OrderedProperties getProperties() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("id", id);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("id", "32");
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		id = (String) props.getProperty("id");
	}
}
