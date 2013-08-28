package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;

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
}
