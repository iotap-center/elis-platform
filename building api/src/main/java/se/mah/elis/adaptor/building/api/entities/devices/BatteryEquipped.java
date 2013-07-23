/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.data.BatteryStatus;

/**
 * The BatteryEquipped interface is used by electronics that are equipped with
 * batteries. A BatteryEquipped object needn't be powered by a battery alone,
 * but can use it as a backup, should the main power supply go out. 
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface BatteryEquipped {
	
	/**
	 * Use this method to get the current battery status.
	 * 
	 * @return A BatteryStatus object.
	 * @since 1.0
	 */
	BatteryStatus getBatteryStatus();
}
