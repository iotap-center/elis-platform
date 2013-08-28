/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities;

import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.services.users.User;

/**
 * The GatewayUser interface describes a user of the local building system,
 * e.g. a home owner or a system administrator.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface GatewayUser extends User {
	
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
