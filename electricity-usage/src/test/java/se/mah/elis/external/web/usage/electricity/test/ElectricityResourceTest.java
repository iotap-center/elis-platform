package se.mah.elis.external.web.usage.electricity.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

import se.mah.elis.external.web.usage.electricity.ElectricityResource;
import se.mah.elis.services.electricity.internal.DeviceSetRequest;
import se.mah.elis.services.electricity.internal.Usage;
import se.mah.elis.services.electricity.internal.UsageRequest;

public class ElectricityResourceTest extends JerseyTest {
	
	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(ElectricityResource.class);
	}
	
	//@Test
	public void ensureOkReply() {
		final String nothingHere = target("/usage/electricity").request().get(String.class);
		assert(nothingHere.equals("nothing here"));
	}
	
	//@Test
	public void getElectricityUsageTest() {
		final DeviceSetRequest deviceSet = new DeviceSetRequest();
		deviceSet.id = "test";
		
		final UsageRequest request = new UsageRequest();
		request.deviceSets.add(deviceSet);
		
		final Usage usageResp = target("/usage/electricity")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), Usage.class);
				
		assertEquals(usageResp.getTimestamp(), "test");
	}

}
