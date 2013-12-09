package se.mah.elis.adaptor.building.ninjablock;

import java.text.ParseException;

import se.mah.elis.adaptor.building.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.building.ninjablock.communication.Communicator;
import se.mah.elis.adaptor.building.ninjablock.communication.DeviceCommunicator;
import se.mah.elis.auxiliaries.data.TemperatureData;

public class NinjaBuiltInHumidity {


	public float getCurrentHumidity()  throws SensorFailedException {
		
		float currentHumidity = 0;
		String response = null;
		String guid = "4412BB000319_0101_0_30";
		Communicator com = new Communicator();
		DeviceCommunicator devCom = new DeviceCommunicator(com);
		
		try {
			response = devCom.getDevice(guid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		NinjablockParser parser = new NinjablockParser();
		try {
			currentHumidity = parser.parseHumidityValue(response);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return currentHumidity;
	}
	
}
