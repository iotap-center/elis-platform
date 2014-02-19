package se.mah.elis.adaptor.device.api.entities.devices;

import org.joda.time.DateTime;

import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.WaterSample;

/**
 * 
 * This interface describes a water meter that has the capability of registering
 * an ever increasing amount of water consumption in some unit. 
 * 
 * @author Marcus Ljungblad
 * @since 1.1
 *
 */
public interface WaterMeterSampler extends Sampler {
	
	/**
	 * Get the latest measured sample
	 * 
	 * @return the most recently recorded sample
	 * @throws SensorFailedException
	 * @since 1.1
	 */
	WaterSample getSample() throws SensorFailedException;
	
	/**
	 * Get recorded values between two dates specified as DateTime objects
	 * 
	 * @param DateTime object specifying the from date and time
	 * @param DateTime object specifying the to date and time 
	 * @return a list of samples from the given time range
	 * @throws SensorFailedException
	 * @since 1.1
	 */
	WaterSample getSample(DateTime from, DateTime to) throws SensorFailedException;
}
