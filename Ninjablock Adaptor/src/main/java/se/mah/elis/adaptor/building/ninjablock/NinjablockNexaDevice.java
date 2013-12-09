package se.mah.elis.adaptor.building.ninjablock;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import se.mah.elis.adaptor.building.ninjablock.communication.Communicator;
import se.mah.elis.adaptor.building.ninjablock.communication.DeviceCommunicator;

public class NinjablockNexaDevice {

	private String GUID; 
	
	public NinjablockNexaDevice(String GUID) {
		this.GUID = GUID;
	}
	
	public void turnOn(String data){
		updateDeviceData(GUID, data);
	}

	public void turnOff(String data){
		updateDeviceData(GUID, data);
	}
	
	public void updateDeviceData(String Guid, String dataInput){
		String guid = Guid; 
		String data = dataInput;
		
		List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
		urlParam.add(new BasicNameValuePair("DA", data));
		
		Communicator com = new Communicator();
		DeviceCommunicator devCom = new DeviceCommunicator(com);
		
		try {
			devCom.setDeviceValue(guid, urlParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
