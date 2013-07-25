/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities.devices;

import se.mah.elis.adaptor.building.api.data.Address;
import se.mah.elis.adaptor.building.api.entities.User;
import se.mah.elis.adaptor.building.api.exceptions.GatewayCommunicationException;

/**
 * The Gateway interface describes a gateway device.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface Gateway {
	
	/**
	 * 
	 * @return
	 */
	Address getAddress();
	
	/**
	 * 
	 */
	void setAddress();
	
	/**
	 * 
	 * @return
	 */
	User getUser();
	
	/**
	 * 
	 */
	void setUser();
	
	/**
	 * 
	 * @throws GatewayCommunicationException
	 */
	void connect() throws GatewayCommunicationException;
}