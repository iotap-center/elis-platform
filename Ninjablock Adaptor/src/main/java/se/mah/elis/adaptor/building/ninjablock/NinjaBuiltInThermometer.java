package se.mah.elis.adaptor.building.ninjablock;

import java.text.ParseException;

import se.mah.elis.adaptor.building.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.building.api.entities.devices.Gateway;
import se.mah.elis.adaptor.building.api.entities.devices.Thermometer;
import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.building.ninjablock.TemperatureDataImpl;
import se.mah.elis.adaptor.building.ninjablock.communication.Communicator;
import se.mah.elis.adaptor.building.ninjablock.communication.DeviceCommunicator;
import se.mah.elis.adaptor.building.ninjablock.internal.NinjaDeviceIdentifier;
import se.mah.elis.data.TemperatureData;
import static org.hamcrest.CoreMatchers.instanceOf;


public class NinjaBuiltInThermometer implements Thermometer {
	
	private NinjaDeviceIdentifier ninjaIdentifier;
	private String name;
	
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
	public TemperatureData getCurrentTemperature()  throws SensorFailedException {
		
		float currentTemperature = 0;
		String response = null;
		String guid = "4412BB000319_0101_0_31";
		Communicator com = new Communicator();
		DeviceCommunicator devCom = new DeviceCommunicator(com);
		
		try {
			response = devCom.getDevice(guid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		NinjablockParser parser = new NinjablockParser();
		try {
			currentTemperature = parser.parseTemperatureValue(response);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		TemperatureData temperatureData = new TemperatureDataImpl(
				currentTemperature);

		return temperatureData;
	}
	

}
