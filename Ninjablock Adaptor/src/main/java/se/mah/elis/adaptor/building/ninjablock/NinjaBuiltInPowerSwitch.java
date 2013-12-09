package se.mah.elis.adaptor.building.ninjablock;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.building.api.exceptions.ActuatorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.building.ninjablock.communication.Communicator;
import se.mah.elis.adaptor.building.ninjablock.communication.DeviceCommunicator;
import se.mah.elis.adaptor.building.ninjablock.internal.NinjaDeviceIdentifier;

public class NinjaBuiltInPowerSwitch implements PowerSwitch{
	
	private NinjaDeviceIdentifier ninjaIdentifier;
	private String name;

	public NinjaBuiltInPowerSwitch() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public DeviceIdentifier getId() {
		return ninjaIdentifier;
	}

	@Override
	public void setId(DeviceIdentifier id) throws StaticEntityException {
		if (id == instanceOf(NinjaDeviceIdentifier.class)) {
			ninjaIdentifier = (NinjaDeviceIdentifier) id;			
		} else{
			throw new IllegalArgumentException("Enter a valid NinjaDeviceIdentifier.");
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) throws StaticEntityException {
		if(name != null && name.isEmpty())
			this.name = name;
		else{
			throw new IllegalArgumentException("Input is null or empty.");
		}
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDescription(String description) throws StaticEntityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Gateway getGateway() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGateway(Gateway gw) throws StaticEntityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DeviceSet[] getDeviceSets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void turnOn() throws ActuatorFailedException {
		String guid = "4412BB000319_0_0_1007"; // Ninja Eye Leds
		String blue = "FF00FF";
		
		List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
		urlParam.add(new BasicNameValuePair("DA", blue));
		
		Communicator com = new Communicator();
		DeviceCommunicator devCom = new DeviceCommunicator(com);
		
		try {
			devCom.setDeviceValue(guid, urlParam);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void turnOff() throws ActuatorFailedException {
		String guid = "4412BB000319_0_0_1007"; // Ninja Eye Leds
		String black = "000000";
		
		List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
		urlParam.add(new BasicNameValuePair("DA", black));
		
		Communicator com = new Communicator();
		DeviceCommunicator devCom = new DeviceCommunicator(com);
		
		try {
			devCom.setDeviceValue(guid, urlParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toggle() throws ActuatorFailedException {
		
		String response = null;
		String value = null;
		String guid = "4412BB000319_0_0_1007"; // Ninja Eye Leds
		
		Communicator com = new Communicator();
		DeviceCommunicator devCom = new DeviceCommunicator(com);
		
		try {
			response = devCom.getDevice(guid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		NinjablockParser parser = new NinjablockParser();
		try {
			value = parser.parseGetStringValue(response);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// If LED-hex is black, turnOn.
		if (value.equals("000000")){
			turnOn();
			}
		else{ 
			turnOff();
		}
	}

}
