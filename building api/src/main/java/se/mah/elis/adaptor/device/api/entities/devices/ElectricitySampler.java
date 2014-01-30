/**
 * 
 */
package se.mah.elis.adaptor.device.api.entities.devices;

import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;

/**
 * The ElectricitySampler interface describes a sensor with electricity
 * sampling capabilities.
 * 
 * @author "Johan Holmberg, Malm�� University"
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
