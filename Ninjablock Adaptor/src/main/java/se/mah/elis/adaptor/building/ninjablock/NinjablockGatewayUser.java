package se.mah.elis.adaptor.building.ninjablock;

import org.osgi.service.useradmin.User;

import com.google.gson.Gson;

import se.mah.elis.adaptor.building.api.entities.GatewayUser;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.ninjablock.beans.UserBean;
import se.mah.elis.adaptor.building.ninjablock.communication.Communicator;
import se.mah.elis.adaptor.building.ninjablock.communication.UserCommunicator;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

public class NinjablockGatewayUser implements GatewayUser {

	private Gateway gateway;
	private NinjablockGatewayUserIdentifer gatewayUserIdentifier;
	private int id;
	private String response;
	
	@Override
	public int getIdNumber() {
		return id;
	}

	@Override
	public void setIdNumber(int id) {
		this.id = id;
	}

	@Override
	public void initialize() throws UserInitalizationException {
	
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



}
