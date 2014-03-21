package se.mah.elis.adaptor.energy.eon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Response;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.energy.eon.internal.EonActionObject;
import se.mah.elis.adaptor.energy.eon.internal.EonActionStatus;
import se.mah.elis.adaptor.energy.eon.internal.EonHttpBridge;

public class EonHttpBridgeTest {
	
	private static final int LEVEL_HOUR = 0;
	// test config
	private static final String TEST_HOST = "http://ewpapi2.dev.appex.no";
	private static final int TEST_PORT = 80;
	private static final String TEST_BASEPATH = "/v0_2/api/";
	private static final String TEST_USER = "marcus.ljungblad@mah.se";
	private static final String TEST_PASS = "medeamah2012";
	private static final String TEST_GATEWAY = "134";
	private static final String TEST_DEVICEID = "ab62ec3d-f86d-46bc-905b-144ee0511a25";
			
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
	
	/**
	 * Test only required if post breaks. Must change path to 
	 * new requestb.in container (http://requestb.in) if test is needed.
	 */
	@Test
	@Ignore
	public void testRegularPost() {
		final String HOST = "http://requestb.in";
		final String PATH = "/ql0zycql";
		EonHttpBridge b = new EonHttpBridge(HOST, 80, "");
		Response response = b.post("token", PATH, 
				"{\"data\": 123}");
		System.out.println(response.getStatus());
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
		List<Device> devices = bridge.getDevices(token, TEST_GATEWAY);
		assertTrue(devices.size() > 0);
	}
	
	@Test
	public void testGetDeviceStatus() throws ResponseProcessingException, ParseException, AuthenticationException {
		String token = bridge.authenticate(TEST_USER, TEST_PASS);
		Map<String, Object> status = bridge.getDeviceStatus(token, TEST_GATEWAY, TEST_DEVICEID);
		assertEquals(TEST_DEVICEID, status.get("DeviceId"));
	}
	
//	@Test
//	public void testTurnOn() throws AuthenticationException {
//		String token = bridge.authenticate(TEST_USER, TEST_PASS);
//		System.out.println(token);
//		try {
//			String tulpanLampa = "d114d9c7-8374-4386-a0b6-1bbdc25c28f5";
//			EonActionObject reply = bridge.turnOn(token, TEST_GATEWAY, tulpanLampa);
//			assertEquals(EonActionStatus.ACTION_WAITING, reply.getStatus());
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("Failed to toggle device");
//		}
//	}
//	
//	@Test
//	public void testTurnOff() throws AuthenticationException {
//		String token = bridge.authenticate(TEST_USER, TEST_PASS);
//		try {
//			String tulpanLampa = "d114d9c7-8374-4386-a0b6-1bbdc25c28f5"; 
//			EonActionObject reply = bridge.turnOff(token, TEST_GATEWAY, tulpanLampa);
//			assertEquals(EonActionStatus.ACTION_WAITING, reply.getStatus());
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("Failed to toggle device");
//		}
//	}

	
	/**
	 * Default action status on non-existing action is ACTION_QUEUED although 
	 * the documentation says it should be ACTION_NOT_FOUND. Documentation fail. 
	 * 
	 * @throws AuthenticationException
	 */
	@Test
	public void testGetActionObject() throws AuthenticationException {
		String token = bridge.authenticate(TEST_USER, TEST_PASS);
		try {
			int ACTION_ID = 1234;
			EonActionObject obj = bridge.getActionObject(token, TEST_GATEWAY, ACTION_ID);
			assertEquals(EonActionStatus.ACTION_QUEUED, obj.getStatus());
			assertNotNull(obj.getId());
		} catch (Exception ignore) { 
			fail("No action object found"); 
		}
	}
	
	@Test
	public void testGetTemperature() throws AuthenticationException {
		String token = bridge.authenticate(TEST_USER, TEST_PASS);
		try {
			String thermometerDeviceId = "b6530784-14da-469b-8a46-36e8e2c0d684";
			float temperature = bridge.getTemperature(token, TEST_GATEWAY, thermometerDeviceId);
			assertTrue(temperature == -1f);
		} catch (Exception ignore) {
			fail("Temperature not retrieved");
		}
	}
	
	@Test
	public void testGetPowerMeterValueKWh() throws AuthenticationException {
		String token = bridge.authenticate(TEST_USER, TEST_PASS);
		try {
			double value = bridge.getPowerMeterKWh(token, TEST_GATEWAY, TEST_DEVICEID);
			assertTrue(value >= 0.0);
		} catch (Exception ignore) { fail("No power meter value received"); }
	}
	
	@Test
	public void testGetStatData() throws AuthenticationException {
		String token = bridge.authenticate(TEST_USER, TEST_PASS);
		String from = "2013-10-01 00:00";
		int level = LEVEL_HOUR; 
		try {
			List<Map<String, Object>> stats = bridge.getStatData(token, TEST_GATEWAY, TEST_DEVICEID, from, level);
			assertTrue(stats.size() == 24);
		} catch (Exception ignore) { fail(); }
	}
	
}