package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.auxiliaries.data.TemperatureData;

/**
 * The Thermostat interface describes a physical thermostat. A thermostat is
 * simultaneously an actuator and thermometer, thus it extends both of these
 * interfaces.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Thermostat extends Actuator, Thermometer {
	
	/**
	 * Use this method to set the temperature that the thermostat should strive
	 * to reach and maintain.
	 * 
	 * @param goal A Temperature object.
	 * @throws ActuatorFailedException if the thermostat couldn't be set.
	 * @since 1.0
	 */
	void setDesiredTemperature(TemperatureData goal) throws ActuatorFailedException;
}
