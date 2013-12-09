package se.mah.elis.adaptor.building.ninjablock;
import java.text.ParseException;

import se.mah.elis.adaptor.building.ninjablock.beans.DeviceBean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NinjablockParser {

	private static Gson gson = new GsonBuilder().create();
	private static DeviceBean deviceTemp;
	
	public static float parseTemperatureValue(String response) throws ParseException {
	
		deviceTemp = gson.fromJson(response, DeviceBean.class);		
		return deviceTemp.getData().getLastData().getValueAsFloat();
	}
	
	public static float parseHumidityValue(String response) throws ParseException {
		
		deviceTemp = gson.fromJson(response, DeviceBean.class);		
		return deviceTemp.getData().getLastData().getValueAsFloat();
	}
	
	public static String parseGetStringValue(String response) throws ParseException {
		
		DeviceBean deviceValue = gson.fromJson(response, DeviceBean.class);		
		return deviceValue.getData().getLastData().getStringValue();
	}
	
	public static String parsePusherChannelKey(String response) throws ParseException {
		
		DeviceBean devicePusherChannelKey = gson.fromJson(response, DeviceBean.class);
		return devicePusherChannelKey.getData().getPusherChannelValue();
	}

}

