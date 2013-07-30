package se.mah.elis.demo.eon.driver.internal;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;

public class EonDeviceIdentifier implements DeviceIdentifier {
	
	final private int id; 
	
	public EonDeviceIdentifier(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public boolean equals(Object o) {
		if (o instanceof EonDeviceIdentifier) {
			return this.getId() == ((EonDeviceIdentifier) o).getId();
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return String.format("%d", getId());
	}
}
