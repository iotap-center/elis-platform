package se.mah.elis.adaptor.building.ninjablock.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import se.mah.elis.adaptor.building.ninjablock.beans.NinjaEyeBean;

import com.google.gson.Gson;

public class Communicator	{
 
	private String token = "vPV3i5wdyNef4Zmy3S4tAmI0msMhHvWiV8eReCE6iY";
 
	public String httpGet(String url) throws Exception {
		
		url = url + "?user_access_token=" + token;
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		
		HttpResponse response = client.execute(request);
		
		return responseToString(response);
	}
	
	public String httpPut(String url,List<NameValuePair> urlParam) throws Exception {
		
		url = url + "?user_access_token=" + token;
		List<NameValuePair> urlParameters = urlParam;
		
		HttpClient client = new DefaultHttpClient();
		HttpPut request = new HttpPut(url);
		
		request.setHeader("Accept", "application/json");
		
		request.setEntity(new UrlEncodedFormEntity(urlParameters));
		
		HttpResponse response = client.execute(request);
		
		return responseToString(response);
	}
	
	public String httpPost(String url,List<NameValuePair> urlParam) throws Exception {
		
		url = url + "?user_access_token=" + token;
		List<NameValuePair> urlParameters = urlParam;
		
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		
		request.setEntity(new UrlEncodedFormEntity(urlParameters));
		
		HttpResponse response = client.execute(request);
		
		return responseToString(response);
	}

	public String httpDelete(String url) throws Exception{
		
		url = url + "?user_access_token=" + token;
		
		HttpClient client = new DefaultHttpClient();
		HttpDelete request = new HttpDelete(url);
		
		HttpResponse response = client.execute(request);
		
		return responseToString(response);
	}
 
	/**
	 * Method for setting the color on Ninjablocks eye-LEDs.
	 * @param color (in hexadecimal)
	 * @throws Exception
	 */
	private void changeEyeColor(String color) throws Exception	{
		
		String ninjaBlockLED = "4412BB000319_0_0_1007";
		
		String url = "https://api.ninja.is/rest/v0/device/"+ninjaBlockLED+"?user_access_token="+token;

		HttpClient client = new DefaultHttpClient();
		HttpPut request = new HttpPut(url);
		
		request.addHeader("Content-Type","application/json");
		
		NinjaEyeBean ninjaBean = new NinjaEyeBean(color);
		Gson gson = new Gson();

		String json = gson.toJson(ninjaBean);

		StringEntity se = new StringEntity(json);		

		request.setEntity(se);
		
		HttpResponse response = client.execute(request);
		
		System.out.println(responseToString(response));
	}
 
	/**
	 * Method takes parameter HttpResponse and returns a String(JSON).
	 * @param response
	 * @return result(String)
	 * @throws Exception
	 */
	private String responseToString(HttpResponse response) throws Exception	{
		
		// Prints the Response Code
//		System.out.println("Response Code : " + 
//                response.getStatusLine().getStatusCode());
		
		// Save response in from server
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		
		// Append response to StringBuffer
		StringBuffer result = new StringBuffer();
		String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

		return result.toString();
	}
}