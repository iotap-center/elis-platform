/**
 * 
 */
package se.mah.elis.adaptor.building.api.data;

import java.util.Date;

/**
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Sample {
	
	/**
	 * Gets the sample length measured in milliseconds.
	 * 
	 * @return The sample length in milliseconds.
	 * @since 1.0
	 */
	int getSampleLength();

	/**
	 * Gets the moment in time when this sample was taken. The timestamp is
	 * generated when the object is initialized, or in the case of saved data,
	 * at the moment the sample was stored.
	 * 
	 * @return The time when the sample was taken.
	 * @since 1.0
	 */
	Date getSampleTimestamp();
}
