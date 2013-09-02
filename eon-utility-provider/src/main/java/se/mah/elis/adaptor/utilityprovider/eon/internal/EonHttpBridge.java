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
	private static final String EWP_PANEL_ID = "EwpPanelId";
	private static final String AUTH_TOKEN_KEY = "ewp-auth-key";
	private static final String AUTH_ENDPOINT = "/Auth";
	private static final String PANELS_ENDPOINT = "/Panel/GetPanels";
	private static final String DEVICELIST_ENDPOINT = "/Device/GetDevices";
	private static final String DEVICESTATUS_ENDPOINT = "/Device/GetDeviceStatus";
	private static final String SWITCHPSS_ENDPOINT = "/Device/SwitchPSS";
	private static final String ACTIONSTATUS_ENDPOINT = "/Panel/GetActionStatus";

	// SSL config
	private static final String TRUSTSTORE_FILE = "./eon_truststore_client";
	private static final String TRUSTSTORE_SECRET = "eon-truststore-secret";
	private static final String KEYSTORE_FILE = "./eon_keystore_client";
	private static final String KEYSTORE_SECRET = "eon-keystore-secret";

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

	/**
	 * HTTP call to get an E.On token
	 * 
	 * @param username
	 * @param password
	 * @return token as String
	 * @throws AuthenticationException
	 * @throws ResponseProcessingException
	 */
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

	/**
	 * HTTP call to get the gateway of an E.On user
	 * 
	 * Note: if there are multiple gateways attached to the user, only the first
	 * is retrieved. Which gateway the first one is, is highly arbitrary.
	 * 
	 * @param token
	 * @return a gateway representation Map<String, Object>
	 * @throws ResponseProcessingException
	 * @throws ParseException
	 */
	public Map<String, Object> getGateway(String token)
			throws ResponseProcessingException, ParseException {
		Response response = get(token, PANELS_ENDPOINT);
		verifyResponse(response);
		Map<String, Object> gatewayMap = EonParser.parseGateway(response
				.readEntity(String.class));
		return gatewayMap;
	}

	/**
	 * HTTP call to get a list of devices attached to an E.On gateway
	 * 
	 * @param token
	 * @param gatewayId
	 * @return a list of device representations List<Map<String, Object>>
	 * @throws ResponseProcessingException
	 * @throws ParseException
	 */
	public List<Map<String, Object>> getDevices(String token, String gatewayId)
			throws ResponseProcessingException, ParseException {
		Response response = get(token, DEVICELIST_ENDPOINT, EWP_PANEL_ID,
				gatewayId);
		verifyResponse(response);
		return EonParser.parseDeviceList(response.readEntity(String.class));
	}

	/**
	 * HTTP call to get a status object of an E.On device
	 * 
	 * @param token
	 * @param gatewayId
	 * @param deviceId
	 * @return
	 * @throws ResponseProcessingException
	 * @throws ParseException
	 */
	public Map<String, Object> getDeviceStatus(String token, String gatewayId,
			String deviceId) throws ResponseProcessingException, ParseException {
		JSONArray deviceList = new JSONArray();
		JSONObject device = new JSONObject();
		device.put("DeviceId", deviceId);
		deviceList.add(device);
		Response response = post(token, DEVICESTATUS_ENDPOINT,
				deviceList.toJSONString(), EWP_PANEL_ID, gatewayId);
		verifyResponse(response);
		return EonParser.parseDeviceStatus(response.readEntity(String.class));
	}

	/**
	 * HTTP call to get an action object. These are typically created by the
	 * E.On server when state-changing methods are called.
	 * 
	 * @param token
	 * @param gatewayId
	 * @param actionId
	 * @throws ParseException
	 */
	public Map<String, Object> getActionObject(String token, String gatewayId,
			int actionId) throws ParseException {
		WebTarget target = createTarget(ACTIONSTATUS_ENDPOINT);
		target = target.queryParam(EWP_PANEL_ID, gatewayId)
				.queryParam("ActionId", actionId);
		Response response = doGet(token, target);
		verifyResponse(response);
		return EonParser.parseActionObject(response.readEntity(String.class));
	}

	/**
	 * HTTP call to toggle an E.On device
	 * 
	 * @param token
	 * @param gatewayId
	 * @param deviceId
	 * @throws ResponseProcessingException
	 */
	public void switchPSS(String token, String gatewayId, String deviceId)
			throws ResponseProcessingException {
		WebTarget target = createTarget(SWITCHPSS_ENDPOINT);
		target = target.queryParam(EWP_PANEL_ID, gatewayId)
				.queryParam("DeviceId", deviceId).queryParam("TurnOn", "1");
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
	public Response get(String token, String path, String paramKey,
			String paramValue) {
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
	public Response post(String token, String path, String body,
			String paramKey, String paramValue) {
		WebTarget target = createTarget(path).queryParam(paramKey, paramValue);
		return doPost(token, body, target);
	}

	private Response doPost(String token, String body, WebTarget postTarget) {
		Invocation.Builder invocationBuilder = builder(postTarget, token);
		Response response = invocationBuilder.post(Entity.entity(body,
				MediaType.APPLICATION_JSON));
		return response;
	}

	private Invocation.Builder builder(WebTarget target, String token) {
		Invocation.Builder builder = target
				.request(MediaType.APPLICATION_JSON_TYPE);
		if (!token.isEmpty())
			builder.header(AUTH_TOKEN_KEY, token);
		return builder;
	}

	private WebTarget createTarget(String path) {
		WebTarget target = client.target(this.host + ":" + this.port + "/"
				+ this.basepath);
		return target.path(path);
	}

	private void verifyResponse(Response response) {
		if (response.getStatus() != 200)
			throw new ResponseProcessingException(response, "Response "
					+ response.getStatus());
	}

	private Client createClient() {
		Client client = ClientBuilder.newClient();
		return client;
	}

	/*
	 * This is an attempt to make the client SSL compatible TODO: fix keystore
	 * file not available
	 */
	private Client createSslClient() {
		SslConfigurator sslConfig = SslConfigurator.newInstance()
				.trustStoreFile(TRUSTSTORE_FILE)
				.trustStorePassword(TRUSTSTORE_SECRET)
				.keyStoreFile(KEYSTORE_FILE).keyPassword(KEYSTORE_SECRET);

		SSLContext sslContext = sslConfig.createSSLContext();
		Client client = ClientBuilder.newBuilder().sslContext(sslContext)
				.build();

		return client;
	}
}
