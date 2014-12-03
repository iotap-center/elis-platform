package se.mah.elis.data;

/**
 * The WaterSample interface
 * 
 * @author Marcus Ljungblad
 * @since 1.1
 *
 */
public interface WaterSample extends Sample {

	/**
	 * Retrieve the total amount of water consumed since the device started 
	 * measuring in litres. 
	 * 
	 * @return the total amount of water consumed in unit
	 * @since 1.1
	 */
	float getVolume();
	
}
