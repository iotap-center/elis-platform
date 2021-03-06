package se.mah.elis.adaptor.device.api.entities.devices;

import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.TemperatureData;

/**
 * The Thermometer interface describes a sensor that can sample the current
 * temperature of its immediate surroundings. This capability may be
 * implemented by a wide array of device types, such as thermostats, PIR
 * sensors and others.
 * 
 * @author "Johan Holmberg, Malm�� University"
 * @since 1.0
 */
public interface Thermometer extends Sensor {
	
	/**
	 * Use this method to get the current temperature registered by the
	 * thermometer.
	 * 
	 * @return Returns the current temperature as Temperature object.
	 * @throws SensorFailedException if the temperature sampling failed.
	 * @since 1.0
	 */
	TemperatureData getCurrentTemperature() throws SensorFailedException;

}
