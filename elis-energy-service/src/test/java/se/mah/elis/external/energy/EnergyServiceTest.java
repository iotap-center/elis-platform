package se.mah.elis.external.energy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
import se.mah.elis.external.energy.beans.EnergyBean;
import se.mah.elis.external.energy.beans.EnergyDeviceBean;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

import com.google.gson.Gson;

public class EnergyServiceTest extends JerseyTest {

	private static final long TO_TIME = 1392685200000l;
	private static final long FROM_TIME = 1392681600000l;
	private static final String DEVICE = "device-uuid-";
	private static final Double DEVICE_1_KWH = 0.02d;
	private static UserService userService;
	private static PlatformUser platformUser;
	private static GatewayUser gatewayUser;
	private static Gateway gateway;
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
		
		platformUser = mock(PlatformUser.class);
		when(platformUser.getUserId()).thenReturn(UUID.fromString("00001111-2222-3333-4444-555566667777"));
		
		gateway = mock(Gateway.class);
		gatewayUser = mock(GatewayUser.class);
		when(gatewayUser.getGateway()).thenReturn(gateway);
		
		userService = mock(UserService.class);
		when(userService.getPlatformUser(any(UUID.class))).thenReturn(platformUser);
		when(userService.getUsers(any(PlatformUser.class))).thenReturn(new User[] { gatewayUser });
		when(userService.getUser(any(PlatformUser.class), any(UUID.class))).thenReturn(gatewayUser);
	}
	
	@Before
	public void setup() throws SensorFailedException {
		List<Device> devices = createMeters(4);
		gateway.addAll(devices);
		when(gateway.iterator()).thenReturn(devices.iterator());
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
		DateTime from = new DateTime(FROM_TIME);
//		DateTime to = new DateTime(1392685200000l);
		ElectricitySample deviceSample = mock(ElectricitySample.class);
		when(deviceSample.getSampleTimestamp()).thenReturn(from);
		when(deviceSample.getTotalEnergyUsageInWh()).thenReturn(DEVICE_1_KWH*1000);
		when(deviceSample.getCurrentPower()).thenReturn(DEVICE_1_KWH*1000);

		DeviceIdentifier identifier = mock(DeviceIdentifier.class);
		when(identifier.toString()).thenReturn(DEVICE + id);
		
		ElectricitySampler meter = mock(ElectricitySampler.class);
		when(meter.getId()).thenReturn(identifier);
		when(meter.getName()).thenReturn(DEVICE + id + "-name");
		when(meter.getSample()).thenReturn(deviceSample);
		List<ElectricitySample> samples = createSamples(24);
		when(meter.getSamples(any(DateTime.class), any(DateTime.class))).thenReturn(samples);
		return meter;
	}
	
	private List<ElectricitySample> createSamples(int size) {
		List<ElectricitySample> samples = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			samples.add(createSample(i));
		}
		return samples;
	}

	private ElectricitySample createSample(int i) {
		DateTime date = new DateTime(FROM_TIME).plusHours(i);
		ElectricitySample sample = mock(ElectricitySample.class);
		when(sample.getTotalEnergyUsageInWh()).thenReturn(DEVICE_1_KWH);
		when(sample.getSampleTimestamp()).thenReturn(date);
		return sample;
	}

	@Test
	public void testGetNowRequest() {
		EnergyBean bean = getNowRequest();
		assertEquals("00001111-2222-3333-4444-555566667777", bean.puid);
		assertEquals("now", bean.period);
		assertEquals(DEVICE + 0, getFirstDevice(bean.devices));
		assertEquals(DEVICE_1_KWH, bean.devices.get(0).data.get(0).watts, 0.01f);
		assertEquals(0, bean.devices.get(0).data.get(0).kwh, 0.01f);
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

	private EnergyBean getNowRequest() {
		final String energyNowData = target("/energy/00001111-2222-3333-4444-555566667777/now")
				.request().get(String.class);
		EnergyBean bean = gson.fromJson(energyNowData, EnergyBean.class);
		return bean;
	}
	
	@Test
	public void testGetHourlyStatsOneDevice() {
		final String energyHourlydata = target("/energy/00001111-2222-3333-4444-555566667777/hourly")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(TO_TIME))
				.request()
				.get(String.class);
		EnergyBean bean = gson.fromJson(energyHourlydata, EnergyBean.class);
		assertEquals("hourly", bean.period);
		assertEquals(4, bean.devices.size());
		assertEquals(24, bean.devices.get(0).data.size());
		assertNotNull(bean.summary);
		assertEquals(4*DEVICE_1_KWH/1000, bean.summary.kwh, 0.01d);
	}
}
