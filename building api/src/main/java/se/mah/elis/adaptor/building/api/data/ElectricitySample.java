/**
 * 
 */
package se.mah.elis.adaptor.building.api.data;

/**
 * The ElectricitySample interface encapsulates electricity usage data from
 * sensors with electricity-sampling capabilities. While SI units are mostly
 * used, support for Watt hours is also available, since that unit is widely
 * used by electric utilities.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface ElectricitySample extends Sample {
	
	/**
	 * Gets the currently measured current in Ampères.
	 * 
	 * @return The currently measured current in Ampères, given as a float.
	 * @since 1.0
	 */
	float getCurrentCurrent();
	
	/**
	 * Gets the currently measured voltage in Volts.
	 * 
	 * @return The currently measured voltage in Volts, given as a float.
	 * @since 1.0
	 */
	float getCurrentVoltage();
	
	/**
	 * Gets the currently measured power consumption in Watts.
	 * 
	 * @return The currently measured power consumption in Watts, given as a
	 * 		   float.
	 * @since 1.0
	 */
	float getCurrentPower();
	
	/**
	 * Gets the highest measured current in the sample in Ampères.
	 * 
	 * @return The highest measured current in Ampères, given as a float.
	 * @since 1.0
	 */
	float getTopCurrent();
	
	/**
	 * Gets the highest measured voltage in the sample in Volts.
	 * 
	 * @return The highest measured current in Volts, given as a float.
	 * @since 1.0
	 */
	float getTopVoltage();
	
	/**
	 * Gets the highest measured power usage in the sample in Watts.
	 * 
	 * @return The highest measured power usage in Watts, given as a float.
	 * @since 1.0
	 */
	float getTopPower();
	
	/**
	 * Gets the lowest measured current in the sample in Ampères.
	 * 
	 * @return The lowest measured current in Ampères, given as a float.
	 * @since 1.0
	 */
	float getMinCurrent();

	/**
	 * Gets the lowest measured voltage in the sample in Volts.
	 * 
	 * @return The lowest measured current in Volts, given as a float.
	 * @since 1.0
	 */
	
	float getMinVoltage();
	
	/**
	 * Gets the lowest measured power usage in the sample in Watts.
	 * 
	 * @return The lowest measured power usage in Watts, given as a float.
	 * @since 1.0
	 */
	float getMinPower();
	
	/**
	 * Gets the mean current in the sample in Ampères.
	 * 
	 * @return The mean current in Ampères, given as a float.
	 * @since 1.0
	 */
	float getMeanCurrent();
	
	/**
	 * Gets the mean voltage in the sample in Volts.
	 * 
	 * @return The mean voltage in Volts, given as a float.
	 * @since 1.0
	 */
	float getMeanVoltage();
	
	/**
	 * Gets the mean power usage in the sample in Watts.
	 * 
	 * @return The mean power usage in Watts, given as a float.
	 * @since 1.0
	 */
	float getMeanPower();
	
	/**
	 * Gets the total energy usage during the sample in Joules.
	 * 
	 * @return The total energy usage in Joules, given as a float.
	 * @since 1.0
	 */
	float getTotalEnergyUsageInJoules();
	
	/**
	 * Gets the total energy usage during the sample in Watt hours (Wh).
	 * 
	 * @return The total energy usage in Watt hours, given as a float.
	 * @since 1.0
	 */
	float getTotalEnergyUsageInWh();
}
