package se.mah.elis.adaptor.energy.eon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
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

import se.mah.elis.adaptor.energy.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.energy.eon.internal.gateway.EonGateway;
import se.mah.elis.adaptor.energy.eon.internal.user.EonGatewayUser;
import se.mah.elis.adaptor.energy.eon.internal.user.EonGatewayUserFactory;
import se.mah.elis.adaptor.energy.eon.internal.user.EonGatewayUserIdentifer;

public class EonGatewayUserFactoryTest {

	private static final String TEST_TOKEN = "sometoken";
	private static final String TEST_PASS = "testuser";
	private static final String TEST_USER = "testpass";
	private EonHttpBridge bridge;
	private EonGatewayUserFactory factory;

	@Before
	public void setUp() throws AuthenticationException,
			ResponseProcessingException, ParseException {
		bridge = mock(EonHttpBridge.class);
		when(bridge.authenticate(anyString(), anyString())).thenReturn(
				TEST_TOKEN);
		Map<String, Object> gatewayData = new HashMap<>();
		gatewayData.put("Name", "testGwName");
		gatewayData.put("EwpPanelId", "testGwPanelId");
		when(bridge.getGateway(anyString())).thenReturn(gatewayData );
		factory = new EonGatewayUserFactory();
	}

	@Test
	public void testCreateUser() {
		try {
			EonGatewayUser user = (EonGatewayUser) factory.getUser(TEST_USER,
					TEST_PASS, bridge);
			EonGatewayUserIdentifer userId = (EonGatewayUserIdentifer) user
					.getIdentifier();
			EonGateway gateway = (EonGateway) user.getGateway();
			assertEquals(gateway.getAuthenticationToken(), TEST_TOKEN);
			assertNotNull(gateway.getHttpBridge());
			assertEquals(TEST_USER, userId.getUsername());
			assertEquals(TEST_PASS, userId.getPassword());
		} catch (Exception e) {
			fail();
		}
	}
}
