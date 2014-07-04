package se.mah.elis.external.water;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.service.log.LogService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import se.mah.elis.services.storage.Storage;
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
	private static Storage storage;
	private static LogService log;
	
	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new WaterService(userService, storage, log));
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
