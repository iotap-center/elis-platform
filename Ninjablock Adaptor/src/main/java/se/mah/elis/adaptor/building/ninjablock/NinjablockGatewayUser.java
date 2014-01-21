package se.mah.elis.adaptor.building.ninjablock;

import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.exceptions.GatewayCommunicationException;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * Representation of a gateway user for Ninjablock
 * @author Joakim Lithell
 * @version 1.0.0
 * @since 1.0
 */

public class NinjablockGatewayUser implements GatewayUser {

	private Gateway gateway;
	private NinjablockGatewayUserIdentifer gatewayUserIdentifier;
	private int id;
	
	@Override
	public int getIdNumber() {
		return id;
	}

	@Override
	public void setIdNumber(int id) {
		this.id = id;
	}


	@Override
	public UserIdentifier getIdentifier() {
		return gatewayUserIdentifier;
	}


	@Override
	public void setIdentifier(UserIdentifier userIdentifier) {
		gatewayUserIdentifier = (NinjablockGatewayUserIdentifer) userIdentifier;
	}

	@Override
	public Gateway getGateway() {
		return gateway;
	}

	@Override
	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
		
	}

	@Override
	public void initialize() throws UserInitalizationException {
		if (!gateway.hasConnected()) {
			try {
				gateway.connect();
			} catch (GatewayCommunicationException e) {
				throw new UserInitalizationException();
			}
		}
	}



}
