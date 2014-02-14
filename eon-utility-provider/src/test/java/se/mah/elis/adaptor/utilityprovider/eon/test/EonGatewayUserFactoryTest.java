package se.mah.elis.adaptor.utilityprovider.eon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.naming.AuthenticationException;
import javax.ws.rs.client.ResponseProcessingException;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;
import se.mah.elis.adaptor.utilityprovider.eon.internal.gateway.EonGateway;
import se.mah.elis.adaptor.utilityprovider.eon.internal.user.EonGatewayUser;
import se.mah.elis.adaptor.utilityprovider.eon.internal.user.EonGatewayUserFactory;
import se.mah.elis.adaptor.utilityprovider.eon.internal.user.EonGatewayUserIdentifer;

public class EonGatewayUserFactoryTest {

	private static final String TEST_TOKEN = "sometoken";
	private static final String TEST_PASS = null;
	private static final String TEST_USER = null;
	private EonHttpBridge bridge;
	private EonGatewayUserFactory factory;

	@Before
	public void setUp() throws AuthenticationException,
			ResponseProcessingException {
		bridge = mock(EonHttpBridge.class);
		when(bridge.authenticate(anyString(), anyString())).thenReturn(
				TEST_TOKEN);
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
