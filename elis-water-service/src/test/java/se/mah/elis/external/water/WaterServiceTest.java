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

public class WaterServiceTest extends JerseyTest {

	private static final String TEST_UUID = "00001111-2222-3333-4444-555566667777";
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
	public static void setupClass() {
		log = mock(LogService.class);
		
		platformUser = mock(PlatformUser.class);
		when(platformUser.getUserId()).thenReturn(UUID.fromString(TEST_UUID));
		
		gatewayUser = mock(GatewayUser.class);
		
		userService = mock(UserService.class);
		when(userService.getPlatformUser(any(UUID.class))).thenReturn(platformUser);
		when(userService.getUsers(any(PlatformUser.class))).thenReturn(new User[] { gatewayUser });
	}
	
	@Before
	public void setup() {
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
		try {
			when(sampler.getSample()).thenReturn(sample);
		} catch (SensorFailedException e) {}
		
		DateTime from = new DateTime(1392681600000l);
		DateTime to = new DateTime(1392685200000l);
		DateTime sampleTime = to;
		WaterSample historicSample = mock(WaterSample.class);
		when(historicSample.getSampleTimestamp()).thenReturn(sampleTime);
		when(historicSample.getVolume()).thenReturn(HISTORIC_SAMPLE_VOLUME);
		try {
			when(sampler.getSample(any(DateTime.class), any(DateTime.class))).thenReturn(historicSample);
		} catch (SensorFailedException e) {}
	}	
	
	@Test
	public void testGetNowRequest() {
		WaterBean bean = makeRequest("/water/" + TEST_UUID + "/now");
		assertEquals(TEST_UUID, bean.puid);
		assertEquals("now", bean.period);
		assertEquals(SAMPLE_VOLUME, bean.summary.totalVolume, 0.001f);
		assertEquals(SAMPLER_NAME, bean.devices.get(0).deviceId);
	}
	
	@Test
	public void testGetDailyRequestOneHour() {
		final String waterData = target("/water/" + TEST_UUID + "/daily")
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
		EnvelopeBean envelope = gson.fromJson(waterData, EnvelopeBean.class);
		WaterBean bean = gson.fromJson(gson.toJson(envelope.response), WaterBean.class);
		return bean;
	}
	
	@Test
	public void testBadUUID() {
		final Response response = target("/water/saasdf/now").request().get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testNoSuchUser() {
		when(userService.getPlatformUser(any(UUID.class))).thenReturn(null);
		final Response response = target("/water/00001111-2222-3333-4444-555566667778/now")
				.request().get();
		assertEquals(404, response.getStatus());
		when(userService.getPlatformUser(any(UUID.class))).thenReturn(platformUser);
	}
	
	@Test
	public void testFromDateIsInTheFutureDaily() {
		final Response response = target("/water/" + TEST_UUID + "/daily")
				.queryParam("from", futureDate())
				.request()
				.get();
		assertEquals(400, response.getStatus());
	}

	@Test
	public void testFromDateIsInTheFutureWeekly() {
		final Response response = target("/water/" + TEST_UUID + "/weekly")
				.queryParam("from", futureDate())
				.request()
				.get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testFromDateIsInTheFutureMonthly() {
		final Response response = target("/water/" + TEST_UUID + "/monthly")
				.queryParam("from", futureDate())
				.request()
				.get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testFromDateIsBadlyFormatted() {
		final Response response = target("/water/" + TEST_UUID + "/monthly")
				.queryParam("from", "mumbojumbo")
				.request()
				.get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testToDateIsBadlyFormatted() {
		final Response response = target("/water/" + TEST_UUID + "/monthly")
				.queryParam("from", 1392681600000l)
				.queryParam("to", "lajbans")
				.request()
				.get();
		assertEquals(400, response.getStatus());
	}

	private long futureDate() {
		return DateTime.now().plusDays(1).getMillis();
	}
}
