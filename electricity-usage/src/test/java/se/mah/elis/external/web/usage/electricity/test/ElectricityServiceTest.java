package se.mah.elis.external.web.usage.electricity.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.osgi.util.tracker.ServiceTracker;

import se.mah.elis.adaptor.building.api.entities.devices.DeviceSet;
import se.mah.elis.demo.eon.driver.BMSProviderService;
import se.mah.elis.services.electricity.ElectricityService;
import se.mah.elis.services.electricity.internal.DeviceSetRequest;
import se.mah.elis.services.electricity.internal.ElectricityServiceImpl;
import se.mah.elis.services.electricity.internal.Usage;
import se.mah.elis.services.electricity.internal.UsageRequest;

public class ElectricityServiceTest {

	private ElectricityService service;
	
	private class EonService implements BMSProviderService {
		public DeviceSet getDeviceSet(String user, String deviceSetId) {
			return null;
		}
	}
	
	@Before
	public void setUp() {
		ServiceTracker tracker = mock(ServiceTracker.class);
		when(tracker.getService()).thenReturn(null);
		service = new ElectricityServiceImpl(tracker);
	}
	
	@Test
	public void getUsageUsingDeviceSetIdTest() {
		UsageRequest ur = new UsageRequest();
		DeviceSetRequest req = new DeviceSetRequest();
		req.id = "1";
		ur.deviceSets.add(req);
		
		Usage usage;
		try {
			usage = service.getDeviceSetUsage(ur);
			assert(usage.getTimestamp().equals("fixed-timestamp"));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
