package se.mah.elis.adaptor.utilityprovider.eon.internal.devices;

import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;

/**
 * E.On constants that are used to describe devices. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0.0
 */
public class EonDevice {

	// constants
	public final static int TYPE_POWERSWITCH_METER = 49;
	public final static int TYPE_TERMOMETER = 101;
	public final static int TYPE_POWERMETER = 51;
	public final static int TYPE_THERMOSTAT = 96;

	// fields
	protected EonHttpBridge httpBridge;

	/**
	 * Attach a HTTP bridge to the device - mock bridges can be used during
	 * testing.
	 * 
	 * @param bridge
	 */
	public void setHttpBridge(EonHttpBridge bridge) {
		httpBridge = bridge;
	}

}
