package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import java.util.Properties;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.data.OrderedProperties;

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
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("id", this.id);
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		props.put("id", "256");
		return props;
	}

	@Override
	public void populate(Properties props) {
		this.id = (String) props.get("id");
	}
}
