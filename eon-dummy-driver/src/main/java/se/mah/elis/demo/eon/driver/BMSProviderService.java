package se.mah.elis.demo.eon.driver;

import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;

public interface BMSProviderService {
	public DeviceSet getDeviceSet(String user, String deviceSetId);
}
