package se.mah.elis.adaptor.utilityprovider.eon.internal;

import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.SslConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * HTTP bridge to communicate with the E.On HTTP API using JSON.
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class EonHttpBridge {
	
	// E.On key names
	private static final String AUTH_TOKEN_KEY = "ewp-auth-key";
	private static final String AUTH_ENDPOINT = "/Auth";
	private static final String PANELS_ENDPOINT = "/Panel/GetPanels";
	private static final String DEVICELIST_ENDPOINT = "/Device/GetDevices";
	private static final String DEVICESTATUS_ENDPOINT = "/Device/GetDeviceStatus";
	
	// SSL config
	private static final String TRUSTSTORE_FILE = "./eon_truststore_client";
	private static final String TRUSTSTORE_SECRET = "eon-truststore-secret";
	private static final String KEYSTORE_FILE = "./eon_keystore_client";
	private static final String KEYSTORE_SECRET = "eon-keystore-secret";
	private static final String SWITCHPSS_ENDPOINT = null;
	
	// internal config
	private String host;
	private String basepath;
	private int port;

	// helpers
	private Client client;
	
	public EonHttpBridge(String host, int port, String basepath) {
		this.host = host;
		this.port = port;
		this.basepath = basepath;
		this.client = createClient();
	}
	
	public String authenticate(String username, String password) 
			throws AuthenticationException, ResponseProcessingException {
		String token = "";
		JSONObject body = new JSONObject();
		body.put("Username", username);
		body.put("Password", password);
		
		Response response = post(token, AUTH_ENDPOINT, body.toJSONString());
		verifyResponse(response);
		
		try {
			token = EonParser.parseToken(response.readEntity(String.class));
		} catch (ParseException pe) {
			throw new AuthenticationException("Could not extract token");
		}
		
		return token;
	}
	
	public Map<String, Object> getGateway(String token) 
			throws ResponseProcessingException, ParseException {
		Response response = get(token, PANELS_ENDPOINT);
		verifyResponse(response);
		Map<String, Object> gatewayMap = EonParser.parseGateway(
				response.readEntity(String.class));
		return gatewayMap;
	}

	public List<Map<String, Object>> getDevices(String token, String gatewayId) 
			throws ResponseProcessingException, ParseException {
		Response response = get(token, DEVICELIST_ENDPOINT, "EwpPanelId", gatewayId);
		verifyResponse(response);
		return EonParser.parseDeviceList(response.readEntity(String.class));
	}

	public Map<String, Object> getDeviceStatus(String token, String gatewayId, String deviceId) 
			throws ResponseProcessingException, ParseException {
		JSONArray deviceList = new JSONArray();
		JSONObject device = new JSONObject();
		device.put("DeviceId", deviceId);
		deviceList.add(device);
		Response response = post(token, DEVICESTATUS_ENDPOINT, 
				deviceList.toJSONString(), "EwpPanelId", gatewayId);
		verifyResponse(response);
		return EonParser.parseDeviceStatus(response.readEntity(String.class));
	}

	public void switchPSS(String token, String gatewayId, String deviceId) 
		throws ResponseProcessingException {
		WebTarget target = createTarget(SWITCHPSS_ENDPOINT);
		target = target.queryParam("EwpPanelId", gatewayId)
				.queryParam("DeviceId", deviceId)
				.queryParam("TurnOn", "1");
		Response response = doGet(token, target);
		verifyResponse(response);
	}

	/*
	 * HTTP client implementation follows 
	 */
	/**
	 * Make a get request using one parameter
	 * 
	 * @param token
	 * @param path
	 * @param paramKey
	 * @param paramValue
	 * @return Response
	 */
	public Response get(String token, String path, String paramKey, String paramValue) {
		WebTarget target = createTarget(path).queryParam(paramKey, paramValue);
		return doGet(token, target);
	}
	
	/**
	 * Make a get request without parameters
	 * 
	 * @param token
	 * @param path
	 * @return Response
	 */
	public Response get(String token, String path) {
		WebTarget getTarget = createTarget(path);
		return doGet(token, getTarget);
	}

	private Response doGet(String token, WebTarget getTarget) {
		Invocation.Builder invocationBuilder = builder(getTarget, token);
		return invocationBuilder.get();
	}
	
	/**
	 * Make a post request without parameters
	 * 
	 * @param token
	 * @param path
	 * @param body
	 * @return Response
	 */
	public Response post(String token, String path, String body) {
		WebTarget postTarget = createTarget(path);
		Response response = doPost(token, body, postTarget);
		return response;
	}

	/**
	 * Make a post request with parameters
	 * 
	 * @param token
	 * @param path
	 * @param body
	 * @param paramKey
	 * @param paramValue
	 * @return Response
	 */
	public Response post(String token, String path,
			String body, String paramKey, String paramValue) {
		WebTarget target = createTarget(path).queryParam(paramKey, paramValue);
		return doPost(token, body, target);
	}
	
	private Response doPost(String token, String body, WebTarget postTarget) {
		Invocation.Builder invocationBuilder = builder(postTarget, token);
		Response response = invocationBuilder.post(
				Entity.entity(body, MediaType.APPLICATION_JSON));
		return response;
	}
	
	private Invocation.Builder builder(WebTarget target, String token) {
		Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
		if (!token.isEmpty())
			builder.header(AUTH_TOKEN_KEY, token);
		return builder;
	}
	
	private WebTarget createTarget(String path) {
		WebTarget target = client.target(this.host + ":" + this.port + "/" + this.basepath);
		return target.path(path);
	}

	private void verifyResponse(Response response) {
		if (response.getStatus() != 200) 
			throw new ResponseProcessingException(response, "Response " + response.getStatus());
	}
	
	private Client createClient() {
		Client client = ClientBuilder.newClient();
		return client;
	}
	
	private Client createSslClient() {
		SslConfigurator sslConfig = SslConfigurator.newInstance()
				.trustStoreFile(TRUSTSTORE_FILE)
				.trustStorePassword(TRUSTSTORE_SECRET)
				.keyStoreFile(KEYSTORE_FILE)
				.keyPassword(KEYSTORE_SECRET);
		
		SSLContext sslContext = sslConfig.createSSLContext();
		Client client = ClientBuilder.newBuilder()
				.sslContext(sslContext)
				.build();
		
		return client;
	}
}
