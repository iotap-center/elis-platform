/**
 * 
 */
package se.mah.elis.auxiliaries.data;

/**
 * The Temperature interface is used to store temperature data. The interface
 * exposes functionality to retrieve the data in any of the Kelvin, Celsius, 
 * Fahrenheit, or Rankine scales.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface TemperatureData {
	
	/**
	 * Gets the temperature according to the Kelvin scale.
	 * 
	 * @return The temperature as a float value according to the Kelvin scale.
	 * @since 1.0
	 */
	float getKelvin();
	
	/**
	 * Gets the temperature according to the Celsius scale.
	 * 
	 * @return The temperature as a float value according to the Celsius scale.
	 * @since 1.0
	 */
	float getCelsius();
	
	/**
	 * Gets the temperature according to the Fahrenheit scale
	 * 
	 * @return The temperature as a float value according to the Fahrenheit
	 * 		   scale.
	 * @since 1.0
	 */
	float getFahrenheit();
	
	/**
	 * Gets the temperature according to the Rankine scale.
	 * 
	 * @return The temperature as a float value according to the Rankine scale.
	 * @since 1.0
	 */
	float getRankine();
}
