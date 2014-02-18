package se.mah.elis.adaptor.utilityprovider.eon.test;

import static org.junit.Assert.*;

import javax.naming.AuthenticationException;
import javax.ws.rs.client.ResponseProcessingException;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.utilityprovider.eon.internal.EonHttpBridge;

public class ProductionSSLTest {

	private static final String BASEPATH = "/v0_2/api";
	private static final int PORT = 443;
	private static final String HOST = "https://smarthome.eon.se";
	private static final String USERNAME = "eon2hem@gmail.com";
	private static final String PASSWORD = "02DCBD";
	
	private EonHttpBridge bridge;
	
	@Before
	public void setup() {
		bridge = new EonHttpBridge(HOST, PORT, BASEPATH);
	}
	
	@Test
	public void testAuthenticate() {
		String token;
		try {
			token = bridge.authenticate(USERNAME, PASSWORD);
			assertTrue(token.contains(USERNAME));
		} catch (AuthenticationException e) {
			e.printStackTrace();
			fail("Authentication failed");
		} catch (ResponseProcessingException e) {
			e.printStackTrace();
			fail("Could not process response");
		}
	}
}
