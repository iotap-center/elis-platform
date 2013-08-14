package se.mah.elis.adaptor.utilityprovider.api;

import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;

/**
 * This is a temporary interface for describing a utility provider such as E.On. 
 * It will eventually be superseeded by something more complete and thought-through. 
 * 
 * @author Marcus Ljungblad
 *
 */
public interface UtilityProvider {
	
	/**
	 * Retrieve a specific device set
	 * 
	 * @param user
	 * @param deviceSetId
	 * @return DeviceSet
	 */
	public DeviceSet getDeviceSet(String user, String deviceSetId);
}
