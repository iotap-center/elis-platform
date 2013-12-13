package se.mah.elis.adaptor.building.ninjablock.beans;

import com.google.gson.annotations.SerializedName;

public class GatewayDataBean {

	@SerializedName("4412BB000319")
	private GatewayDetails gateway;
	
	public GatewayDetails getGatewayDetails() {
		return gateway;
	}
	
}
