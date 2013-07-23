/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities;

import se.mah.elis.adaptor.building.api.data.GatewayUserIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;

/**
 * The GatewayUser interface describes a user of the local building system,
 * e.g. a home owner or a system administrator.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface GatewayUser {
	
	/**
	 * Get this user's identifier.
	 * 
	 * @return The identifier.
	 * @since 1.0
	 */
	GatewayUserIdentifier getId();
	
	/**
	 * Set this user's identifier.
	 * 
	 * @param id The identifier.
	 * @since 1.0
	 */
	void setId(GatewayUserIdentifier id);
	
	/**
	 * Get the gateway associated with this user.
	 * 
	 * @return The gateway,
	 * @since 1.0
	 */
	Gateway getGateway();
	
	/**
	 * Set the gateway associated with this user.
	 * 
	 * @param gateway The gateway.
	 * @since 1.0
	 */
	void setGateway(Gateway gateway);
}
