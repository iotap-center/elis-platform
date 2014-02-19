package se.mah.elis.adaptor.device.api.entities.devices;

/**
 * 
 * This interface describes a water meter that has the capability of registering
 * an ever increasing amount of water consumption in some unit. 
 * 
 * @author Marcus Ljungblad
 * @since 1.1
 *
 */
public interface WaterMeter extends Sensor {

	/**
	 * Retrieve the total amount of water consumed since the device started 
	 * measuring. 
	 * 
	 * @return the total amount of water consumed in unit
	 * @since 1.1
	 */
	float getWaterConsumption();
}
