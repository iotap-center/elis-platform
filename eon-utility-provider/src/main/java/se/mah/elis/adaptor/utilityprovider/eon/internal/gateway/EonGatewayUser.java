package se.mah.elis.adaptor.utilityprovider.eon.internal.gateway;

import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.exceptions.GatewayCommunicationException;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * Representation of a gateway user for E.On 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class EonGatewayUser implements GatewayUser {

	private Gateway gateway;
	private EonGatewayUserIdentifer gatewayUserIdentifier;
	private int id;

	/**
	 * Will try to initialise the gateway if that has not been done before. 
	 * 
	 * @throws UserInitalizationException if initialisation of gateway fails
	 */
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

	@Override
	public UserIdentifier getIdentifier() {
		return gatewayUserIdentifier;
	}

	@Override
	public void setIdentifier(UserIdentifier userIdentifier) {
		gatewayUserIdentifier = (EonGatewayUserIdentifer) userIdentifier;
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
	public int getIdNumber() {
		return id;
	}

	@Override
	public void setIdNumber(int id) {
		this.id = id;
	}

}
