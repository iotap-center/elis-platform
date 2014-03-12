package se.mah.elis.external.water;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
import se.mah.elis.external.water.beans.WaterBean;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.UserService;

public class WaterServiceTest extends JerseyTest {

	private static final float SAMPLE_VOLUME = 1.1f;
	private static final String SAMPLER_NAME = "sampler";
	private static final Float HISTORIC_SAMPLE_VOLUME = 2.2f;
	private static UserService userService;
	private static PlatformUser platformUser;
	private static GatewayUser gatewayUser;
	private static LogService log;
	
	private Gson gson = new Gson();

	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new WaterService(userService, log));
		return config;
	}
	
	@BeforeClass
	public static void setupClass() throws SensorFailedException {
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
		WaterMeterSampler sampler = mock(WaterMeterSampler.class);
		Gateway gateway = mock(Gateway.class);

		List<Device> meters = new ArrayList<>();
		meters.add(sampler);
		
		when(gateway.iterator()).thenReturn(meters.iterator());
		when(gatewayUser.getGateway()).thenReturn(gateway);
		
		DateTime now = DateTime.now();
		WaterSample sample = mock(WaterSample.class);		
		when(sample.getSampleTimestamp()).thenReturn(now);
		when(sample.getVolume()).thenReturn(SAMPLE_VOLUME);
		when(sampler.getName()).thenReturn(SAMPLER_NAME);
		when(sampler.getSample()).thenReturn(sample);
		
		
		DateTime from = new DateTime(1392681600000l);
		DateTime to = new DateTime(1392685200000l);
		DateTime sampleTime = to;
		WaterSample historicSample = mock(WaterSample.class);
		when(historicSample.getSampleTimestamp()).thenReturn(sampleTime);
		when(historicSample.getVolume()).thenReturn(HISTORIC_SAMPLE_VOLUME);
		when(sampler.getSample(any(DateTime.class), any(DateTime.class))).thenReturn(historicSample);
	}	
	
	@Test
	public void testGetNowRequest() {
		WaterBean bean = makeRequest("/water/1/now");
		assertEquals("1", bean.puid);
		assertEquals("now", bean.period);
		assertEquals(SAMPLE_VOLUME, bean.summary.totalVolume, 0.001f);
		assertEquals(SAMPLER_NAME, bean.devices.get(0).deviceId);
	}
	
	@Test
	public void testGetDailyRequestOneHour() {
		final String waterData = target("/water/1/daily")
				.queryParam("from", 1392681600000l)
				.queryParam("to", 1392685200000l)
				.request()
				.get(String.class);
		WaterBean bean = gson.fromJson(waterData, WaterBean.class);
		//WaterBean bean = makeRequest("/water/1/daily/?from=1392681600000&to=1392685200000");
		assertEquals(1, bean.devices.get(0).data.size());
		assertEquals(HISTORIC_SAMPLE_VOLUME, bean.devices.get(0).data.get(0).volume, 0.01f);
	}

	private WaterBean makeRequest(String uri) {
		final String waterData = target(uri).request().get(String.class);
		WaterBean bean = gson.fromJson(waterData, WaterBean.class);
		return bean;
	}
}
