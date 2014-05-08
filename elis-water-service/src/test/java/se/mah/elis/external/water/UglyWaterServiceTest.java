package se.mah.elis.external.water;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

import com.google.gson.Gson;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.any;
import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.WaterMeterSampler;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.WaterSample;
import se.mah.elis.external.beans.EnvelopeBean;
import se.mah.elis.external.water.beans.WaterBean;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.UserService;

/**
 * Why, oh why, would you want a separate test class for a single test case?
 * Well, JerseyTest doesn't seem to like being reset. That is, this is an ugly
 * hack. Yuck.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public class UglyWaterServiceTest extends JerseyTest {

	private static final String TEST_UUID = "00001111-2222-3333-4444-555566667777";
	private static UserService userService;
	private static LogService log;
	
	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new WaterService(userService, log));
		return config;
	}
	
	@BeforeClass
	public static void setupClass() {
		log = mock(LogService.class);
		userService = null;
	}
	
	@Before
	public void setup() {}
	
	@Test
	public void testNoUserServiceAvailable() {
		final Response response = target("/water/" + TEST_UUID + "/now").request().get();
		assertEquals(500, response.getStatus());
	}
}
