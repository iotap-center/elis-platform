package se.mah.elis.adaptor.utilityprovider.eon.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.building.api.entities.devices.Device;
import se.mah.elis.adaptor.building.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;

/**
 * Used to parse E.On HTTP response messages 
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class EonParser {

	private static JSONParser parser = new JSONParser();
	
	public static String parseToken(String json) throws ParseException {
		JSONObject keyObject = (JSONObject) parser.parse(json);
		String token = (String) keyObject.get("ewp-auth-key");
		return token;
	}

	public static Map<String, Object> parseGateway(String json) throws ParseException {
		Map<String, Object> gateway = new HashMap<String, Object>();
		JSONArray gateways = (JSONArray) parser.parse(json);
		JSONObject jsonGateway = (JSONObject) gateways.get(0); // only supports one gateway
		gateway.put("EwpPanelId", jsonGateway.get("EwpPanelId"));
		gateway.put("Name", jsonGateway.get("Name"));
		return gateway;
	}

	// make this return List<Device> instead
	public static List<Device> parseDeviceList(String json) throws ParseException {
		List<Device> deviceList = new ArrayList<Device>();
		JSONArray devices = (JSONArray) parser.parse(json);
		for (Iterator<JSONObject> deviceIterator = devices.iterator(); deviceIterator.hasNext(); ) { 
			JSONObject obj = deviceIterator.next();
			Device device;
			try {
				device = EonDeviceFactory.createFrom(obj);
				deviceList.add(device);
			} catch (MethodNotSupportedException e) {
				continue;
			} catch (StaticEntityException e) {
				throw new ParseException(0);
			}
		}
		return deviceList;
	}

	public static Map<String, Object> parseDeviceStatus(String response) throws ParseException {
		Map<String, Object> deviceStatus = new HashMap<String, Object>();
		JSONObject obj = (JSONObject) ((JSONArray) parser.parse(response)).get(0);
		deviceStatus.put("CurrentKwh", obj.get("CurrentKwh"));
		deviceStatus.put("CurrentOn", obj.get("CurrentOn"));
		deviceStatus.put("CurrentPrice", obj.get("CurrentPrice"));
		deviceStatus.put("DeviceId", obj.get("DeviceId"));
		return deviceStatus;
	}

	public static Map<String, Object> parseActionObject(String response) throws ParseException {
		Map<String, Object> actionObject = new HashMap<String, Object>();
		JSONObject obj = (JSONObject) parser.parse(response);
		actionObject.put("Id", obj.get("Id"));
		actionObject.put("Message", obj.get("Message"));
		actionObject.put("StatusId", obj.get("StatusId"));
		return actionObject;
	}
}
