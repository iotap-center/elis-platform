package se.mah.elis.adaptor.utilityprovider.eon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Response;

import org.apache.http.HttpException;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;

public class EonHttpBridgeTest {
	
	// test config
	private static final String TEST_HOST = "http://ewpapi2.dev.appex.no";
	private static final int TEST_PORT = 80;
	private static final String TEST_BASEPATH = "/v0_2/api/";
	private static final String TEST_USER = "marcus.ljungblad@mah.se";
	//private static final String TEST_PASS = "02DCBD";
	private static final String TEST_PASS = "medeamah2012";
	private static final long EXPECTED_GATEWAY_ID = 60;
	private static final String TEST_GATEWAY = "134";
			
	private EonHttpBridge bridge; 
	
	@Before
	public void setUp() {
		bridge = new EonHttpBridge(TEST_HOST, TEST_PORT, TEST_BASEPATH);
	}
	
	@Test
	public void testRegularGet() {
		EonHttpBridge b = new EonHttpBridge("http://vecka.nu", 80, "");
		Response response = b.get("token", "/");
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testRegularPost() {
		EonHttpBridge b = new EonHttpBridge("http://requestb.in", 80, "");
		Response response = b.post("token", "/ql0zycql", 
				"{\"data\": 123}");
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testAuthenticate() {
		//EonHttpBridge b = new EonHttpBridge("http://requestb.in", 80, "/ql0zycql");
		String token;
		try {
			token = bridge.authenticate(TEST_USER, TEST_PASS);
			assertTrue(token.contains(TEST_USER));
		} catch (AuthenticationException e) {
			e.printStackTrace();
			fail("Failed to authenticate");
		} catch (ResponseProcessingException rpe) {
			rpe.printStackTrace();
			fail("Response not ok");
		}
	}
	
	@Test
	public void testGetGateway() throws AuthenticationException, ResponseProcessingException, ParseException {
		String token = bridge.authenticate(TEST_USER, TEST_PASS);
		Map<String, Object> gatewayMap = bridge.getGateway(token);
		assertTrue(gatewayMap.containsKey("EwpPanelId"));
		assertTrue(gatewayMap.containsKey("Name"));
	}
	
	@Test
	public void testGetDeviceList() throws ResponseProcessingException, ParseException, AuthenticationException {
		String token = bridge.authenticate(TEST_USER, TEST_PASS);
		List<Map<String, Object>> devices = bridge.getDevices(token, TEST_GATEWAY);
		assertTrue(devices.get(0).containsKey("Id"));
	}
	
	@Test
	public void testGetDeviceStatus() throws ResponseProcessingException, ParseException, AuthenticationException {
		String token = bridge.authenticate(TEST_USER, TEST_PASS);
		String deviceId = "ab62ec3d-f86d-46bc-905b-144ee0511a25";
		Map<String, Object> status = bridge.getDeviceStatus(token, TEST_GATEWAY, deviceId);
		assertEquals(deviceId, status.get("DeviceId"));
	}
}
