/**
 * 
 */
package se.mah.elis.adaptor.device.api.data;

/**
 * The BatteryStatus interface is used by BatteryDrivenDevice objects to report
 * their current charge levels, whether they're depleted and whether they're
 * currently being charged.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface BatteryStatus {
	
	/**
	 * Use this method to find out the current charge level. The charge will be
	 * given as a float value ranging between 0 and 1, where 0 is depleted and
	 * 1 is fully charged.
	 * 
	 * @return The current battery level.
	 * @since 1.0
	 */
	float getRelativeChargeLevel();
	
	/**
	 * Use this method to find out whether the battery power source is depleted
	 * or not. Note that a battery can in some case be depleted while the
	 * device itself is still online, e.g. in a wired fire alarm using a
	 * battery backup.
	 * 
	 * @return True if the battery is depleted, otherwise false.
	 * @since 1.0
	 */
	boolean isDepleted();
	
	/**
	 * Use this method to find out whether the battery is currently charging or
	 * not. This information could be used to either detect a faulty battery or
	 * a power outage.
	 * 
	 * @return True if the battery is currently charging, otherwise false.
	 * @since 1.0
	 */
	boolean isCharging();
	
	/**
	 * Use this method to find out whether a battery needs replacement or not.
	 * 
	 * @return True if the battery needs replacement, otherwise false.
	 * @since 1.0
	 */
	boolean replacementNeeded();
}
