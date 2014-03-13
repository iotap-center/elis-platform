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

import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.exceptions.MethodNotSupportedException;
import se.mah.elis.exceptions.StaticEntityException;

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

	public static float parseTemperatureValue(String response) throws ParseException {
		JSONObject temperatureObject = (JSONObject) parser.parse(response);
		Number tempValue = (Number) temperatureObject.get("Temperature");
		return tempValue.floatValue();
	}
	
	public static double parsePowerMeterValue(String response) throws ParseException{
		// retrieves the first json object in [{ 'CurrentKwh': value }]
		JSONObject powerMeterObject = (JSONObject) ((JSONArray) parser.parse(response)).get(0);
		double powerMeterValue = ((Number) powerMeterObject.get("CurrentKwh")).doubleValue();
		return powerMeterValue;
	}

	public static List<Map<String, Object>> parseSummaryStats(String response) throws ParseException {
		List<Map<String, Object>> summaries = new ArrayList<Map<String,Object>>(); 
		JSONArray responses = (JSONArray) parser.parse(response);
		for (Iterator<JSONObject> iterator = responses.iterator(); iterator.hasNext(); ) {
			JSONObject obj = iterator.next();
			
			Map<String, Object> summary = new HashMap<>();
			summary.put("DeviceId", obj.get("DeviceId"));
			
			summary.put("AverageConsumptionLastYear", 	number(obj.get("AverageConsumptionLastYear")));
			summary.put("AverageConsumptionLastMonth", 	number(obj.get("AverageConsumptionLastMonth")));
			summary.put("AverageConsumptionLastWeek", 	number(obj.get("AverageConsumptionLastWeek")));
			summary.put("AverageConsumptionThisYear", 	number(obj.get("AverageConsumptionThisYear")));
			summary.put("AverageConsumptionThisMonth", 	number(obj.get("AverageConsumptionThisMonth")));
			summary.put("AverageConsumptionThisWeek", 	number(obj.get("AverageConsumptionThisWeek")));
			summary.put("AverageConsumptionThisDay", 	number(obj.get("AverageConsumptionThisDay")));
			
			summary.put("SumConsumptionLastYear", 	number(obj.get("SumConsumptionLastYear")));
			summary.put("SumConsumptionLastMonth", 	number(obj.get("SumConsumptionLastMonth")));
			summary.put("SumConsumptionLastWeek", 	number(obj.get("SumConsumptionLastWeek")));
			summary.put("SumConsumptionThisYear", 	number(obj.get("SumConsumptionThisYear")));
			summary.put("SumConsumptionThisMonth", 	number(obj.get("SumConsumptionThisMonth")));
			summary.put("SumConsumptionThisWeek", 	number(obj.get("SumConsumptionThisWeek")));
			summary.put("SumConsumptionThisDay", 	number(obj.get("SumConsumptionThisDay")));
			
			summary.put("SumCostLastYear", 	number(obj.get("SumCostLastYear")));
			summary.put("SumCostLastMonth", number(obj.get("SumCostLastMonth")));
			summary.put("SumCostLastWeek", 	number(obj.get("SumCostLastWeek")));
			summary.put("SumCostThisYear", 	number(obj.get("SumCostThisYear")));
			summary.put("SumCostThisMonth", number(obj.get("SumCostThisMonth")));
			summary.put("SumCostThisWeek", 	number(obj.get("SumCostThisWeek")));
			summary.put("SumCostThisDay", 	number(obj.get("SumCostThisDay")));
			
			summaries.add(summary);
		}
		return summaries;
	}

	public static List<Map<String, Object>> parseStatData(String response) throws ParseException{
		List<Map<String, Object>> stats = new ArrayList<Map<String,Object>>();
		
		JSONArray responseArray = (JSONArray) parser.parse(response);
		for (Iterator<JSONObject> iterator = responseArray.iterator(); iterator.hasNext();) {
			JSONObject responseStat = iterator.next();
			Map<String, Object> stat = new HashMap<>();
			stat.put("Key", responseStat.get("Key"));
			stat.put("Value", responseStat.get("Value"));
			stat.put("ValueCost", responseStat.get("ValueCost"));
			stats.add(stat);
		}
		
		return stats;
	}
	
	private static double number(Object value) {
		return ((Number) value).doubleValue();
	}
}
