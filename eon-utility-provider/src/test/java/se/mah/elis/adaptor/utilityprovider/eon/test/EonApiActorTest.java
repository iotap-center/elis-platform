package se.mah.elis.adaptor.utilityprovider.eon.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.utilityprovider.eon.EonApiActor;

public class EonApiActorTest {

	private final String TEST_USER = "eon2hem@gmail.com";
	private final String TEST_PASS = "02DCBD";
	private final long EXPECTED_GATEWAY_ID = 60;
	
	private EonApiActor service;
	
	@Before
	public void setUp() {
		service = new EonApiActor();
	}
	
	@Test
	public void testSuccessfulAuthenticationAgainstEonTestGateway() {
		try {
			String key = service.getAuthenticationToken(TEST_USER, TEST_PASS);
			assertTrue(key.contains("eon2hem@gmail.com"));
		} catch (AuthenticationException | IOException e) {
			fail("Exception thrown in success case"); 
		}
	}
	
	@Test
	public void testFailureAuthenticationAgainstEonTestGateway() {
		try {
			service.getAuthenticationToken("failure-user", "incorrect-password");
			fail("token received");
		} catch (AuthenticationException e) {
			// success case
		} catch (IOException e) {
			fail("weird response");
		}
	}
	
	@Test 
	public void testGetGatewayIds() {
		try {
			service.initialise(TEST_USER, TEST_PASS);
			long panelId = service.getPanels();
			assertEquals(EXPECTED_GATEWAY_ID, panelId);
		} catch (Exception e) {
			fail("unexpected exception in success case");
		}
	}
	
	@Test
	public void testGetDeviceIdsFromGateway() {
		try {
			service.initialise(TEST_USER, TEST_PASS);
			List<String> deviceIds = service.getDevices(EXPECTED_GATEWAY_ID);
			assertFalse(deviceIds.isEmpty());
		} catch (Exception e) {
			fail("unexpected exception in success case");
		}
	}
	
	@Test 
	public void testGetDeviceStatus() {
		final int RANDOM_DEVICE = 10; 
		try {
			service.initialise(TEST_USER, TEST_PASS);
			String id = service.getDevices(EXPECTED_GATEWAY_ID).get(RANDOM_DEVICE);
			Map<String, Object> deviceStatus = service.getDeviceStatus(EXPECTED_GATEWAY_ID, id);
			assertTrue(deviceStatus.containsKey("DeviceId")); // should always be there
															  // according to EWP api spec
		} catch (Exception e) {
			fail("unexpected exception in success case");
		}
	}
	
	@Test
	public void testGetCurrentDeviceKwh() {
		final int RANDOM_DEVICE = 10; 
		try {
			service.initialise(TEST_USER, TEST_PASS);
			// I'm not sure these are sorted... but leaving for now
			String id = service.getDevices(EXPECTED_GATEWAY_ID).get(RANDOM_DEVICE);
			Map<String, Object> deviceStatus = service.getDeviceStatus(EXPECTED_GATEWAY_ID, id);
			assertTrue(deviceStatus.containsKey("CurrentKwh"));
			double currentKwh = (double) deviceStatus.get("CurrentKwh");
			assertTrue(currentKwh >= 0);
		} catch (Exception e) {
			fail("unexpected exception in success case");
		}
	}

}
