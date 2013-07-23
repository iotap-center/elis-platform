/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.exceptions.GatewayCommunicationException;

/**
 * The Gateway interface describes a gateway device.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Gateway extends DeviceSet {
	
	/**
	 * Gets the address of this gateway.
	 * 
	 * @return The gateway's address as a GatewayAddress.
	 * @since 1.0
	 */
	GatewayAddress getAddress();
	
	/**
	 * Sets the address of this gateway
	 * 
	 * @param address The gateway's address.
	 * @since 1.0
	 */
	void setAddress(GatewayAddress address);
	
	/**
	 * Gets the user associated with this gateway.
	 * 
	 * @return The associated user.
	 * @since 1.0
	 */
	GatewayUser getUser();
	
	/**
	 * Sets the user associated with this gateway.
	 * 
	 * @param user The user to associate with this gateway.
	 * @since 1.0
	 */
	void setUser(GatewayUser user);
	
	/**
	 * Establishes a communication route to the gateway.
	 * 
	 * @throws GatewayCommunicationException if communication failed.
	 * @since 1.0
	 */
	void connect() throws GatewayCommunicationException;
	
	/**
	 * Checks whether a gateway is online or not. If it isn't online, then we
	 * shouldn't try to read stuff from it or perform actuating actions on it.
	 * 
	 * @return True if the gateway is online, otherwise false.
	 * @since 1.0
	 */
	boolean isOnline();
}
