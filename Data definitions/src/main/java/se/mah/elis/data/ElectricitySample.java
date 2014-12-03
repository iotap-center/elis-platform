/**
 * 
 */
package se.mah.elis.data;

/**
 * The ElectricitySample interface encapsulates electricity usage data from
 * sensors with electricity-sampling capabilities. While SI units are mostly
 * used, support for watt hours is also available, since that unit is widely
 * used by electric utilities.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface ElectricitySample extends Sample {
	
	/**
	 * Gets the currently measured current in amperes.
	 * 
	 * @return The currently measured current in amperes, given as a double.
	 * @since 1.0
	 */
	double getCurrentCurrent();
	
	/**
	 * Gets the currently measured voltage in volts.
	 * 
	 * @return The currently measured voltage in volts, given as a double.
	 * @since 1.0
	 */
	double getCurrentVoltage();
	
	/**
	 * Gets the currently measured power consumption in watts.
	 * 
	 * @return The currently measured power consumption in watts, given as a
	 * 		   double.
	 * @since 1.0
	 */
	double getCurrentPower();
	
	/**
	 * Gets the highest measured current in the sample in amperes.
	 * 
	 * @return The highest measured current in amperes, given as a double.
	 * @since 1.0
	 */
	double getMaxCurrent();
	
	/**
	 * Gets the highest measured voltage in the sample in Volts.
	 * 
	 * @return The highest measured current in Volts, given as a double.
	 * @since 1.0
	 */
	double getMaxVoltage();
	
	/**
	 * Gets the highest measured power usage in the sample in watts.
	 * 
	 * @return The highest measured power usage in watts, given as a double.
	 * @since 1.0
	 */
	double getMaxPower();
	
	/**
	 * Gets the lowest measured current in the sample in amperes.
	 * 
	 * @return The lowest measured current in amperes, given as a double.
	 * @since 1.0
	 */
	double getMinCurrent();

	/**
	 * Gets the lowest measured voltage in the sample in volts.
	 * 
	 * @return The lowest measured current in volts, given as a double.
	 * @since 1.0
	 */
	
	double getMinVoltage();
	
	/**
	 * Gets the lowest measured power usage in the sample in watts.
	 * 
	 * @return The lowest measured power usage in watts, given as a double.
	 * @since 1.0
	 */
	double getMinPower();
	
	/**
	 * Gets the mean current in the sample in amperes.
	 * 
	 * @return The mean current in amperes, given as a double.
	 * @since 1.0
	 */
	double getMeanCurrent();
	
	/**
	 * Gets the mean voltage in the sample in volts.
	 * 
	 * @return The mean voltage in volts, given as a double.
	 * @since 1.0
	 */
	double getMeanVoltage();
	
	/**
	 * Gets the mean power usage in the sample in watts.
	 * 
	 * @return The mean power usage in watts, given as a double.
	 * @since 1.0
	 */
	double getMeanPower();
	
	/**
	 * Gets the total energy usage during the sample in joules.
	 * 
	 * @return The total energy usage in joules, given as a double.
	 * @since 1.0
	 */
	double getTotalEnergyUsageInJoules();
	
	/**
	 * Gets the total energy usage during the sample in watt hours (Wh).
	 * 
	 * @return The total energy usage in watt hours, given as a double.
	 * @since 1.0
	 */
	double getTotalEnergyUsageInWh();
}
