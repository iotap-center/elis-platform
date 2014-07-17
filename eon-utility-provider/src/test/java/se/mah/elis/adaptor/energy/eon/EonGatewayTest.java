package se.mah.elis.adaptor.energy.eon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.naming.AuthenticationException;
import javax.ws.rs.client.ResponseProcessingException;

import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import se.mah.elis.adaptor.device.api.data.GatewayAddress;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.exceptions.GatewayCommunicationException;
import se.mah.elis.adaptor.device.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.energy.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.energy.eon.internal.gateway.EonGateway;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.services.storage.Storage;

public class EonGatewayTest {

	private static final UUID USER_ID = UUID.fromString("00001111-2222-3333-4444-555566667777");
	private static final String PASSWORD = "02DCD0"; // "medeamah2012";
	private static final String USERNAME = "hems7@eon.se"; //"marcus.ljungblad@mah.se";
	private static final String TEST_GATEWAY_NAME = "testGateway";
	private static final String TEST_TOKEN = "sometoken";
	private static final String TEST_GATEWAY_ADDRESS = "59"; // "134";
	private static final String TEST_HOST = "http://smarthome.eon.se"; //"http://ewpapi2.dev.appex.no";
	private static final String TEST_BASEPATH = "/v0_2/api/";
	private static final int TEST_PORT = 80;
	private EonHttpBridge bridge;
	private EonGateway gateway;
	private Storage storage;

	@Before
	public void setUp() throws AuthenticationException,
			ResponseProcessingException, ParseException {
		storage = mock(Storage.class);
		when(storage.objectExists(any(UUID.class))).thenReturn(true);
		
		bridge = mock(EonHttpBridge.class);
		when(bridge.authenticate(anyString(), anyString())).thenReturn(
				TEST_TOKEN);
		when(bridge.getGateway(TEST_TOKEN)).thenReturn(fakeGatewayData());
		when(bridge.getDevices(TEST_TOKEN, TEST_GATEWAY_ADDRESS, null))
		.thenReturn(fakeDeviceList());
//		when(bridge.getDevices(any(String.class), any(String.class), any(UUID.class)))
//		.thenReturn(fakeDeviceList());

		GatewayAddress address = mock(GatewayAddress.class);
		when(address.toString()).thenReturn(TEST_GATEWAY_ADDRESS);

		gateway = new EonGateway(storage);
		gateway.setAuthenticationToken(TEST_TOKEN);
		gateway.setHttpBridge(bridge);
		gateway.setAddress(address);
	}

	private List<Device> fakeDeviceList() {
		List<Device> list = new ArrayList<Device>();
		Device device = mock(Device.class);
		list.add(device);
		return list;
	}

	private Map<String, Object> fakeGatewayData() {
		Map<String, Object> fakeGatewayData = new HashMap<String, Object>();
		fakeGatewayData.put("Name", TEST_GATEWAY_NAME);
		fakeGatewayData.put("EwpPanelId", TEST_GATEWAY_ADDRESS);
		return fakeGatewayData;
	}
	
	@Test
	public void testConnect() {
		try {
			gateway.connect();
			assertEquals(TEST_GATEWAY_NAME, gateway.getName());
			assertEquals(TEST_GATEWAY_ADDRESS, gateway.getAddress().toString());
			assertTrue(gateway.hasConnected());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test 
	public void testEnsureGatewayHasDevices() {
		try {
			gateway.connect();
			assertTrue(gateway.size() > 0);
		} catch (GatewayCommunicationException e) {
			fail("Could not connect");
		}
	}
	
	@Test
	public void testDeviceIsInStorage() {
		try {
			gateway.connect();
			verify(storage, Mockito.times(1)).objectExists(any(UUID.class));
		} catch (GatewayCommunicationException e) {
			fail("Could not connect");
		}
	}

	@Test
	public void testConnectWithRealUser() {
		try {
			EonHttpBridge bridge = new EonHttpBridge(TEST_HOST, TEST_PORT, TEST_BASEPATH);
			String token = bridge.authenticate(USERNAME, PASSWORD);
			EonGateway gw = new EonGateway(storage);
			gw.setHttpBridge(bridge);
			gw.setAuthenticationToken(token);
			gw.connect();
		} catch (Exception e) {
			fail("Could not connect to gateway with real user");
		}
	}
	
	@Test 
	public void testValidToken() {
		EonHttpBridge bridge = new EonHttpBridge(TEST_HOST, TEST_PORT, TEST_BASEPATH);
		try {
			String token = bridge.authenticate(USERNAME, PASSWORD);
			// example: 635282347637201905,marcus.ljungblad@mah.se,medeamah2012
			assertTrue(token.endsWith("," + USERNAME + "," + PASSWORD));
		} catch (AuthenticationException | ResponseProcessingException e) {
			fail("exception thrown");
		}
	}
	
	@Test
	public void testConnectShouldThrowGatewayCommunicationExceptionOnNoData()
			throws ResponseProcessingException, ParseException,
			AuthenticationException, MethodNotSupportedException {
		Map<String, Object> value = new HashMap<>();
		when(bridge.getGateway(anyString())).thenReturn(value);
		try {
			gateway.connect();
			fail("Gateway should have thrown an exception and didn't");
		} catch (GatewayCommunicationException e) {
		}
	}

}
