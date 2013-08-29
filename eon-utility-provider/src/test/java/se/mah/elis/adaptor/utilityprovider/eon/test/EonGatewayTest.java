package se.mah.elis.adaptor.utilityprovider.eon.test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.ws.rs.client.ResponseProcessingException;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.data.GatewayAddress;
import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;

public class EonGatewayTest {
	
	private static final String TEST_GATEWAY_NAME = "testGateway";
	private static final String TEST_TOKEN = "sometoken";
	private static final String TEST_GATEWAY_ADDRESS = "someaddress";
	private EonHttpBridge bridge;
	private EonGateway gateway;

	@Before
	public void setUp() throws AuthenticationException, ResponseProcessingException, ParseException {
		bridge = mock(EonHttpBridge.class);
		when(bridge.authenticate(anyString(), anyString())).thenReturn(TEST_TOKEN);
		when(bridge.getGateway(TEST_TOKEN)).thenReturn(fakeGatewayData());
		
		GatewayAddress address = mock(GatewayAddress.class);
		when(address.toString()).thenReturn(TEST_GATEWAY_ADDRESS);
		
		gateway = new EonGateway();
		gateway.setAuthenticationToken(TEST_TOKEN);
		gateway.setHttpBridge(bridge);
		gateway.setAddress(address);
	}
	
	private Map<String, Object> fakeGatewayData() {
		Map<String, Object> fakeGatewayData = new HashMap<String, Object>();
		fakeGatewayData.put("Name", TEST_GATEWAY_NAME);
		fakeGatewayData.put("EwpPanelId", "1234");
		return fakeGatewayData;
	}

	@Test 
	public void testConnect() {
		try {
			gateway.connect();
			assertTrue(gateway.hasConnected());
			assertEquals(TEST_GATEWAY_NAME, gateway.getName());
		} catch (Exception e) {
			fail();
		}
	}
}
