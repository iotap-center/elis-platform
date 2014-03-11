package se.mah.elis.external.water;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.external.energy.EnergyService;
import se.mah.elis.external.energy.beans.EnergyBean;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

import com.google.gson.Gson;

public class EnergyServiceTest extends JerseyTest {

	private static final String DEVICE_1 = "device1";
	private static final Double DEVICE_1_KWH = 20.0d;
	private static UserService userService;
	private static PlatformUser platformUser;
	private static GatewayUser gatewayUser;
	private static LogService log;
	
	private Gson gson = new Gson();

	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new EnergyService(userService, log));
		return config;
	}
	
	@BeforeClass
	public static void setup() throws SensorFailedException {
		log = mock(LogService.class);
		
		PlatformUserIdentifier uid = mock(PlatformUserIdentifier.class);
		when(uid.getId()).thenReturn(1); // same as in tests
		
		platformUser = mock(PlatformUser.class);
		when(platformUser.getIdentifier()).thenReturn(uid);
		
		gatewayUser = mock(GatewayUser.class);
		Gateway gateway = mock(Gateway.class);

		List<Device> devices = new ArrayList<>();
		ElectricitySampler meter = createSampleMeter();
		devices.add(meter);
		
		when(gateway.iterator()).thenReturn(devices.iterator());
		when(gatewayUser.getGateway()).thenReturn(gateway);
		
		userService = mock(UserService.class);
		when(userService.getPlatformUser(anyString())).thenReturn(platformUser);
		when(userService.getUsers(any(PlatformUser.class))).thenReturn(new User[] { gatewayUser });
		
	}

	private static ElectricitySampler createSampleMeter() throws SensorFailedException {
		ElectricitySample device1sample = mock(ElectricitySample.class);
		when(device1sample.getSampleTimestamp()).thenReturn(DateTime.now());
		when(device1sample.getTotalEnergyUsageInWh()).thenReturn(DEVICE_1_KWH);
		
		ElectricitySampler meter = mock(ElectricitySampler.class);
		when(meter.getName()).thenReturn(DEVICE_1);
		when(meter.getSample()).thenReturn(device1sample);
		return meter;
	}
	
	@Test
	public void testGetNowRequest() {
		final String energyNowData = target("/energy/1/now").request().get(String.class);
		EnergyBean bean = gson.fromJson(energyNowData, EnergyBean.class);
		assertEquals("1", bean.puid);
		assertEquals("now", bean.period);
		assertEquals(DEVICE_1, bean.devices.get(0).deviceId);
		assertEquals(DEVICE_1_KWH, bean.devices.get(0).data.get(0).kwh, 0.01f);
	}
}
