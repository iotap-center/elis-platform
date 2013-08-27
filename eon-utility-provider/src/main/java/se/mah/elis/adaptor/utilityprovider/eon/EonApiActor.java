package se.mah.elis.adaptor.utilityprovider.eon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EonApiActor {

	private final String USER_AGENT = "Mozilla/5.0 - Elis Platform";
	private final String BASE_URL = "https://smarthome.eon.se/v0_2/api";
	private String authenticationToken;

	public void initialise(String user, String password)
			throws AuthenticationException, IOException {
		try {
			this.authenticationToken = getAuthenticationToken(user, password);
		} catch (MalformedURLException e) {
			throw new IOException("Malformed URL");
		}
	}

	// Method for getting a "key" for authentication from E-on API.
	public String getAuthenticationToken(String User, String Password)
			throws MalformedURLException, IOException, AuthenticationException {
		String resourcePath = "/Auth";

		// Data sent to E-on Api
		String param = "{\"Username\": \"" + User + "\", \"Password\": \""
				+ Password + "\"}";

		// Try: Create Connection
		HttpURLConnection connection;

		URL url = new URL(BASE_URL + resourcePath);
		connection = (HttpURLConnection) url.openConnection();

		// set the output to true, indicating you are outputting(uploading).
		connection.setDoOutput(true);

		// optional default is POST
		connection.setRequestMethod("POST");

		// do NOT specify this length in the header by using
		// conn.setRequestProperty("Content-Length", length);
		connection.setFixedLengthStreamingMode(param.getBytes().length);

		// Request headers
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("User-Agent", USER_AGENT);

		// send the POST out
		PrintWriter out = new PrintWriter(connection.getOutputStream());
		out.print(param);
		out.close();

		// Separate the Auth-Key from Api-response
		String authKey = null;
		try {
			// Gets the response from the API
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			String Json_obj = response.toString();
			String[] parts = Json_obj.split(":");
			authKey = parts[1].replace("}", "");
			authKey = authKey.substring(1, authKey.length() - 1);
		} catch (Exception e) { // FIXME This is totally broken!!
			throw new AuthenticationException();
		}

		return authKey;

	}

	// Method for getting all Panels
	public long getPanels() throws Exception {

		String TargetUrl = "https://smarthome.eon.se/v0_2/api/Panel/GetPanels";
		URL url = new URL(TargetUrl);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// set the output to true
		connection.setDoOutput(true);

		// optional default is GET
		connection.setRequestMethod("GET");

		// add request headers
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("ewp-auth-key", this.authenticationToken);

		// Reads and saves Api-response.
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
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
			JSONArray obj = (JSONArray) parser.parse(jsonPanelResponse);

			// TO-DO: loop the array to get more then one panel
			JSONObject entry1 = (JSONObject) obj.get(0);
			returnValue = (Long) entry1.get("EwpPanelId");

		} catch (Exception e) {
			e.printStackTrace();
			return (long) -1;
		}

		return returnValue;

	}

	// Method for getting all devices from a specific panel-Id (panelIds)
	public List<String> getDevices(long panelIds) throws Exception {
		String TargetUrl = "https://smarthome.eon.se/v0_2/api/Device/GetDevices?EwpPanelId="
				+ panelIds;
		URL url = new URL(TargetUrl);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setDoOutput(true);

		connection.setRequestMethod("GET");

		// add request header
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("ewp-auth-key", this.authenticationToken);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return getDeviceIdsFromJson(response.toString());
	}

	// Method for parsing GetDevices response
	public List<String> getDeviceIdsFromJson(String jsonGetDevicesResponse) {
		List<String> deviceIds = new ArrayList<String>();
		JSONParser parser = new JSONParser();
		try {
			JSONArray obj = (JSONArray) parser.parse(jsonGetDevicesResponse);

			for (int i = 0; i < obj.size(); i++) {
				JSONObject entry = (JSONObject) obj.get(i);
				deviceIds.add((String) entry.get("Id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return deviceIds;
	}

	// Method for getting the status of the device using the "PanelId" and
	// "DeviceId"
	public Map<String, Object> getDeviceStatus(Long panelId, String deviceId)
			throws Exception {

		// Data sent to E-on Api
		String param = "[{\"DeviceId\": \"" + deviceId + "\"}]";

		String TargetUrl = "https://smarthome.eon.se/v0_2/api/Device/GetDeviceStatus?EwpPanelId="
				+ panelId;
		URL url = new URL(TargetUrl);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setDoOutput(true);
		connection.setRequestMethod("POST");

		// add request header
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("ewp-auth-key", this.authenticationToken);

		PrintWriter out = new PrintWriter(connection.getOutputStream());
		out.print(param);
		out.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return createDeviceStatusMap(response.toString());

	}

	private Map<String, Object> createDeviceStatusMap(String response) {
		Map<String, Object> statusMap = new HashMap<String, Object>();
		JSONParser parser = new JSONParser();
		try {
			JSONArray statusObject = (JSONArray) parser.parse(response);
			JSONObject entry = (JSONObject) statusObject.get(0);
			statusMap.put("DeviceId",
					JSONValue.toJSONString(entry.get("DeviceId")));
			statusMap.put("CurrentKwh", Double.parseDouble(
					JSONValue.toJSONString(entry.get("CurrentKwh"))));
			statusMap.put("CurrentPrice", Double.parseDouble(
					JSONValue.toJSONString(entry.get("CurrentPrice"))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return statusMap;
	}

	// Method for parsing GetDevice response and extracting only CurrentKwh
	public String getCurrentKwhFromJson(String jsonGetDevicesResponse) {

		String CurrentKwh = "";

		JSONParser parser = new JSONParser();
		try {
			JSONArray obj = (JSONArray) parser.parse(jsonGetDevicesResponse);

			for (int i = 0; i < obj.size(); i++) {
				JSONObject entry1 = (JSONObject) obj.get(i);
				CurrentKwh = JSONValue.toJSONString(entry1.get("CurrentKwh"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "CurrentKwh could not be displayed";
		}

		return CurrentKwh;

	}

}
