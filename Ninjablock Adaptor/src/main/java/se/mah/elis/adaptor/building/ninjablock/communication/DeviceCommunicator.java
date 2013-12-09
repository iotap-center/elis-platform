package se.mah.elis.adaptor.building.ninjablock.communication;

import java.util.List;

import org.apache.http.NameValuePair;

public class DeviceCommunicator {
	
	private Communicator com;
	
	public DeviceCommunicator(Communicator c)	{
	
		com = c;
	}
	
	/**
	 * Returns the list of devices associated with the authenticating user.
	 * @return result(String)
	 * @throws Exception
	 */
	public String getDevices () throws Exception{
		return com.httpGet("https://api.ninja.is/rest/v0/devices");
	}
	
	/**
	 * Fetch metadata about the specified device.
	 * @param guid
	 * @return result(String)
	 * @throws Exception
	 */
	public String getDevice(String guid) throws Exception {		
		return com.httpGet("https://api.ninja.is/rest/v0/device/" + guid);
	}

	/**
	 * Update a device, including sending a command.
	 * @param guid
	 * @param urlParam
	 * @return result(String)
	 * @throws Exception
	 */
	public String setDeviceValue(String guid,List<NameValuePair> urlParam) throws Exception {
		return com.httpPut("https://api.ninja.is/rest/v0/device/"+ guid, urlParam );
	}
}
