package se.mah.elis.external.water;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.external.energy.EnergyService;
import se.mah.elis.external.energy.beans.EnergyBean;
import se.mah.elis.external.energy.beans.EnergyDeviceBean;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

import com.google.gson.Gson;

public class EnergyServiceTest extends JerseyTest {

	private static final String DEVICE = "device-uuid-";
	private static final Double DEVICE_1_WH = 20.0d;
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
	public static void setupSuite() {
		log = mock(LogService.class);
		
		PlatformUserIdentifier uid = mock(PlatformUserIdentifier.class);
		when(uid.getId()).thenReturn(1); // same as in tests
		
		platformUser = mock(PlatformUser.class);
		when(platformUser.getIdentifier()).thenReturn(uid);

		gatewayUser = mock(GatewayUser.class);	
		userService = mock(UserService.class);
		when(userService.getPlatformUser(anyString())).thenReturn(platformUser);
		when(userService.getUsers(any(PlatformUser.class))).thenReturn(new User[] { gatewayUser });
		
	}
	
	@Before
	public void setup() throws SensorFailedException {
		List<Device> devices = createMeters(4);
		Gateway gateway = mock(Gateway.class);
		gateway.addAll(devices);
		when(gateway.iterator()).thenReturn(devices.iterator());
		when(gatewayUser.getGateway()).thenReturn(gateway);
	}

	private List<Device> createMeters(int numberOfMeters) throws SensorFailedException {
		List<Device> meters = new ArrayList<>();
		for (int i = 0; i < numberOfMeters; i++) {
			ElectricitySampler meter = createSampleMeter(i);
			meters.add(meter);
		}
		return meters;
	}

	private ElectricitySampler createSampleMeter(int id) throws SensorFailedException {
		ElectricitySample deviceSample = mock(ElectricitySample.class);
		when(deviceSample.getSampleTimestamp()).thenReturn(DateTime.now());
		when(deviceSample.getTotalEnergyUsageInWh()).thenReturn(DEVICE_1_WH);

		DeviceIdentifier identifier = mock(DeviceIdentifier.class);
		when(identifier.toString()).thenReturn(DEVICE + id);
		
		ElectricitySampler meter = mock(ElectricitySampler.class);
		when(meter.getId()).thenReturn(identifier);
		when(meter.getName()).thenReturn(DEVICE + id + "-name");
		when(meter.getSample()).thenReturn(deviceSample);
		return meter;
	}
	
	@Test
	public void testGetNowRequest() {
		EnergyBean bean = getNowRequest();
		assertEquals("1", bean.puid);
		assertEquals("now", bean.period);
		assertEquals(DEVICE + 0, getFirstDevice(bean.devices));
		assertEquals(DEVICE_1_WH/1000, bean.devices.get(0).data.get(0).kwh, 0.01f);
	}

	
	private String getFirstDevice(List<EnergyDeviceBean> devices) {
		List<String> names = new ArrayList<>();
		
		for (EnergyDeviceBean bean : devices) 
			names.add(bean.deviceId);
		
		Collections.sort(names);
		
		return names.get(0);
	}

	@Test
	public void testGetNowRequestMultipleDevices() {
		EnergyBean bean = getNowRequest();
		assertEquals(4, bean.devices.size());
	}
	
	@Test
	public void testGetNowSummary() {
		EnergyBean bean = getNowRequest();
		assertEquals(4*DEVICE_1_WH/1000, bean.summary.kwh, 0.01d);
	}

	private EnergyBean getNowRequest() {
		final String energyNowData = target("/energy/1/now").request().get(String.class);
		EnergyBean bean = gson.fromJson(energyNowData, EnergyBean.class);
		return bean;
	}
}
