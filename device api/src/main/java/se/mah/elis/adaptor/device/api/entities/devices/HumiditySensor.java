package se.mah.elis.adaptor.device.api.entities.devices;

import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;

/**
 * The HumiditySensor interface describes a humidity sensor.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface HumiditySensor extends Sensor {
	
	/**
	 * <p>Gets the current humidity level.</p>
	 * 
	 * <p>Humidity is traditionally graded using a percentual value. As there
	 * isn't a very good way of describing this in Java, the humidity level is
	 * represented by a float (with double precision) number between
	 * 0 and 1.</p> 
	 * 
	 * @return A float between 0 and 1.
	 * @throws SensorFailedException if the humidity level couldn't be fetched.
	 * @since 1.0
	 */
	double getHumidity() throws SensorFailedException;
}
