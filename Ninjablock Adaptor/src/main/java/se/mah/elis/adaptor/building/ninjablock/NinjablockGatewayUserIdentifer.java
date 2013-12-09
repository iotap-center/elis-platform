package se.mah.elis.adaptor.building.ninjablock;

import com.google.gson.Gson;

import se.mah.elis.adaptor.building.api.data.GatewayUserIdentifier;
import se.mah.elis.adaptor.building.ninjablock.beans.UserBean;
import se.mah.elis.adaptor.building.ninjablock.communication.Communicator;
import se.mah.elis.adaptor.building.ninjablock.communication.UserCommunicator;

public class NinjablockGatewayUserIdentifer implements GatewayUserIdentifier{
	
	private String response;
	private UserBean user;
	
	public NinjablockGatewayUserIdentifer (){
		Communicator com = new Communicator();
		UserCommunicator useCom = new UserCommunicator(com);
		try {
			response = useCom.getUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		user = gson.fromJson(response, UserBean.class);
	}
	
	public String getUserId () {
		return user.getId();
	}
	
	public String getUserName () {
		return user.getName();
	}
	
	public String getUserEmail () {
		return user.getEmail();
	}

}
