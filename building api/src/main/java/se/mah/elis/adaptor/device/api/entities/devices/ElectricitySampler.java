/**
 * 
 */
package se.mah.elis.adaptor.device.api.entities.devices;

import java.util.List;

import org.joda.time.DateTime;

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
	 * Gets a list of energy usage samples between the from and to date.
	 * 
	 * @param from
	 * @param to
	 * @return 
	 * @throws SensorFailedException
	 */
	List<ElectricitySample> getSamples(DateTime from, DateTime to)
			throws SensorFailedException;
	
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
