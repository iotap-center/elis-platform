/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.data.ElectricitySample;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;

/**
 * The ElectricitySampler interface describes a sensor with electricity
 * sampling capabilities.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface ElectricitySampler extends Sampler {

	/**
	 * Gets a sample of the current energy usage.
	 * 
	 * @return An ElectricitySample object containing current electricity
	 * 		   usage data.
	 * @throws SensorFailedException if the sample couldn't be fetched.
	 * @since 1.0
	 */
	ElectricitySample getSample() throws SensorFailedException;
	
	/**
	 * Sample the current energy usage for a given amount of time.
	 * 
	 * @param millis The length of the sample in milliseconds.
	 * @return An ElectricitySample object containing current electricity
	 * 		   usage data.
	 * @throws SensorFailedException if the sample couldn't be fetched.
	 * @since 1.0
	 */
	ElectricitySample sample(int millis) throws SensorFailedException;
}
