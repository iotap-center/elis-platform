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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}
}
