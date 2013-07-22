package se.mah.elis.demo.electricityuse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;


public class ElectricityUseService{

	private final String USER_AGENT = "Mozilla/5.0 - Jocke";
	private String key; 
	
	public ElectricityUseService (String User,String Password){
		try {
			this.key = getAuthKey(User,Password);
		} catch (Exception e) {
			System.out.println("Auth Key could not be generated");
			e.printStackTrace();
			
		}
	}
	
	// Method for getting a "key" for authentication from E-on API.
	private String getAuthKey(String User, String Password) throws Exception {
		
		// Auth-key
		String returnAuthKey = null;

		String TargetUrl = "https://smarthome.eon.se/v0_2/api/Auth";
 
		//Data sent to E-on Api
		String param = "{\"Username\": \""+User+"\", \"Password\": \""+Password+"\"}";

		
		//Try: Create Connection 
		HttpURLConnection connection;
		try {
			URL url = new URL(TargetUrl);
			connection = (HttpURLConnection) url.openConnection();
			
			//set the output to true, indicating you are outputting(uploading).
			connection.setDoOutput(true);
			
			// optional default is POST
			connection.setRequestMethod("POST");
			
			// do NOT specify this length in the header by using conn.setRequestProperty("Content-Length", length);
			connection.setFixedLengthStreamingMode(param.getBytes().length);
			
			//Request headers
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			
			//send the POST out
			PrintWriter out = new PrintWriter(connection.getOutputStream());
			out.print(param);
			out.close();

			// Gets the response from the API
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
				 
			// Separate the Auth-Key from Api-response
			String Json_obj = response.toString();
			String [] parts = Json_obj.split(":");
			String authKey = parts[1].replace("}","");
			returnAuthKey = authKey.substring(1,authKey.length()-1);
			
		} catch (Exception e) {
			System.out.println("Connection failed!");
			e.printStackTrace();
		}
		
		return returnAuthKey;

	}

	// Method for getting all Panels
	public long getPanels() throws Exception{
		
		
		String TargetUrl = "https://smarthome.eon.se/v0_2/api/Panel/GetPanels";
		URL url = new URL(TargetUrl);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		//set the output to true
		connection.setDoOutput(true);
				
		// optional default is GET
		connection.setRequestMethod("GET");
		
		//add request headers
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("ewp-auth-key", this.key);

		//Reads and saves Api-response.
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		


		// Return panels
		return getPanelIdFromJson(response.toString());
		
	}
	
	// Method for parsing Api-response from Json to long
	private Long getPanelIdFromJson(String jsonPanelResponse) {
		long returnValue;
		
		JSONParser parser = new JSONParser();
		try {
			JSONArray obj = (JSONArray)parser.parse(jsonPanelResponse);
			
			// TO-DO: loop the array to get more then one panel  
			JSONObject entry1 = (JSONObject)obj.get(0);
			returnValue = (Long) entry1.get("EwpPanelId");
			
		} catch (Exception e) {
			e.printStackTrace();
			return (long) -1;
		}
		
		return returnValue;
		
	}
	
	// Method for getting all devices from a specific panel-Id (panelIds)
	public String getDevices(Long panelIds) throws Exception{
		
		String returnValue;
		
		String TargetUrl = "https://smarthome.eon.se/v0_2/api/Device/GetDevices?EwpPanelId="+panelIds;
		URL url = new URL(TargetUrl);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setDoOutput(true);
				
		connection.setRequestMethod("GET");
		
		//add request header
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("ewp-auth-key", this.key);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		returnValue = response.toString();
		
		return getDeviceIdFromJson(returnValue);
	}
	
	// Method for parsing GetDevices response
	public String getDeviceIdFromJson(String jsonGetDevicesResponse) {
		
		String returnValue = "";
//		String returnDeviceName  = "";
		
		JSONParser parser = new JSONParser();
		try {
			JSONArray obj = (JSONArray)parser.parse(jsonGetDevicesResponse);
			
			for (int i = 0; i < obj.size(); i++){
				JSONObject entry = (JSONObject)obj.get(i);
//					returnDeviceName = (String)entry.get("Name");
//					returnValue = "\n"+(String)entry.get("Id")+" "+returnDeviceName.replace("%20"," ");
			     	returnValue = (String)entry.get("Id");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "Could not parse DeviceResponse!";
		}
		System.out.println("Working with value: "+returnValue);
		return returnValue;
	}
	
	// Method for getting the status of the device using the "PanelId" and "DeviceId"
	public String getDeviceStatus(Long panelId, String deviceId) throws Exception{
		
		//Data sent to E-on Api
		String param = "[{\"DeviceId\": \""+deviceId+"\"}]"; 
		
		String TargetUrl = "https://smarthome.eon.se/v0_2/api/Device/GetDeviceStatus?EwpPanelId="+panelId;
		URL url = new URL(TargetUrl);

		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setDoOutput(true);
				
		connection.setRequestMethod("POST");
		
		//add request header
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("ewp-auth-key", this.key);
		
		
		PrintWriter out = new PrintWriter(connection.getOutputStream());
		out.print(param);
		out.close();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
				
		return getCurrentKwhFromJson(response.toString());
		
	}
	
	// Method for parsing GetDevice response and extracting only CurrentKwh
	public String getCurrentKwhFromJson(String jsonGetDevicesResponse) {
		
		String CurrentKwh ="";
		
		JSONParser parser = new JSONParser();
		try {
			JSONArray obj = (JSONArray)parser.parse(jsonGetDevicesResponse);
			
			for (int i = 0; i < obj.size(); i++){
				JSONObject entry1 = (JSONObject)obj.get(i);
				CurrentKwh= JSONValue.toJSONString(entry1.get("CurrentKwh"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "CurrentKwh could not be displayed";
		}
		
		return CurrentKwh;
		
	}
	
	
}
