package se.mah.elis.external.water;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
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
import com.google.gson.GsonBuilder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.any;
import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.WaterMeterSampler;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.WaterSample;
import se.mah.elis.external.beans.EnvelopeBean;
import se.mah.elis.external.beans.helpers.DateTimeAdapter;
import se.mah.elis.external.water.beans.WaterBean;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.PlatformUserIdentifier;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.UserService;

public class WaterServiceTest extends JerseyTest {

	private static final String TEST_PUID = "00001111-2222-3333-4444-555566667777";
	private static final String TEST_DID = "10001111-2222-3333-4444-555566667777";
	private static final String TEST_DID2 = "11001111-2222-3333-4444-555566667777";
	private static final String TEST_DSID = "20001111-2222-3333-4444-555566667777";
	private static final String TEST_BAD_DID = "30001111-2222-3333-4444-555566667777";
	private static final String TEST_BAD_DSID = "40001111-2222-3333-4444-555566667777";
	private static final float SAMPLE_VOLUME = 1.1f;
	private static final String SAMPLER_NAME = "sampler";
	private static final Float HISTORIC_SAMPLE_VOLUME = 2.2f;
	private static UserService userService;
	private static Storage storage;
	private static PlatformUser platformUser;
	private static GatewayUser gatewayUser;
	private static LogService log;
	private static WaterSample sample;
	private static WaterSample historicSample;
	private static Gateway gateway;
	private static Device device;
	private static WaterMeterSampler meter1;
	private static WaterMeterSampler meter2;
	private static DeviceSet deviceset1;
	private static DeviceSet deviceset2;
	private List<Device> devices1 = new ArrayList<Device>();
	private List<Device> devices2 = new ArrayList<Device>();
	private static DateTime from = new DateTime(1392681600000l);
	private static DateTime to = new DateTime(1392685200000l);
	private static DateTime threeDays = new DateTime(1392940800000l);
	private static DateTime now = DateTime.now();
	private static DateTime sampleTime = to;
	
	private GsonBuilder gsonBuilder = new GsonBuilder()
		.registerTypeAdapter(DateTime.class, new DateTimeAdapter());
	private Gson gson = gsonBuilder.create();

	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new WaterService(userService, storage, log));
		return config;
	}
	
	@BeforeClass
	public static void setupClass() {
		log = mock(LogService.class);
		
		platformUser = mock(PlatformUser.class);
		when(platformUser.getUserId()).thenReturn(UUID.fromString(TEST_PUID));
		
		sample = mock(WaterSample.class);		
		when(sample.getSampleTimestamp()).thenReturn(now);
		when(sample.getVolume()).thenReturn(SAMPLE_VOLUME);
		
		historicSample = mock(WaterSample.class);
		when(historicSample.getSampleTimestamp()).thenReturn(sampleTime);
		when(historicSample.getVolume()).thenReturn(HISTORIC_SAMPLE_VOLUME);
		
		device = mock(Device.class);
		DeviceIdentifier devId = mock(DeviceIdentifier.class);
		when(devId.toString()).thenReturn("ID: device");
		when(device.getId()).thenReturn(devId);
		when(device.getName()).thenReturn("Name: device");
		when(device.getDescription()).thenReturn("Description: device");
		when(device.getDataId()).thenReturn(UUID.fromString(TEST_BAD_DID));
		when(device.getOwnerId()).thenReturn(UUID.fromString(TEST_PUID));
		
		meter1 = mock(WaterMeterSampler.class);
		DeviceIdentifier meterId = mock(DeviceIdentifier.class);
		when(meterId.toString()).thenReturn("ID: meter");
		when(meter1.getId()).thenReturn(meterId);
		when(meter1.getName()).thenReturn(SAMPLER_NAME);
		when(meter1.getDescription()).thenReturn("Description: meter");
		when(meter1.getDataId()).thenReturn(UUID.fromString(TEST_DID));
		when(meter1.getOwnerId()).thenReturn(UUID.fromString(TEST_PUID));
		
		meter2 = mock(WaterMeterSampler.class);
		DeviceIdentifier meterId2 = mock(DeviceIdentifier.class);
		when(meterId.toString()).thenReturn("ID: meter");
		when(meter2.getId()).thenReturn(meterId2);
		when(meter2.getName()).thenReturn(SAMPLER_NAME + "2");
		when(meter2.getDescription()).thenReturn("Description: meter2");
		when(meter2.getDataId()).thenReturn(UUID.fromString(TEST_DID2));
		when(meter2.getOwnerId()).thenReturn(UUID.fromString(TEST_PUID));
		
		deviceset1 = mock(DeviceSet.class);
		when(deviceset1.getDataId()).thenReturn(UUID.fromString(TEST_DSID));
		when(deviceset1.getOwnerId()).thenReturn(UUID.fromString(TEST_PUID));
		
		deviceset2 = mock(DeviceSet.class);
		when(deviceset2.getDataId()).thenReturn(UUID.fromString(TEST_BAD_DSID));
		when(deviceset2.getOwnerId()).thenReturn(UUID.fromString(TEST_PUID));

		gateway = mock(Gateway.class);
		
		gatewayUser = mock(GatewayUser.class);
		when(gatewayUser.getGateway()).thenReturn(gateway);
		
		userService = mock(UserService.class);
		when(userService.getPlatformUser(any(UUID.class))).thenReturn(platformUser);
		when(userService.getUsers(any(PlatformUser.class))).thenReturn(new User[] { gatewayUser });
		
		storage = mock(Storage.class);
		try {
			when(storage.readData(UUID.fromString(TEST_PUID))).thenThrow(new StorageException());
			when(storage.readData(UUID.fromString(TEST_DID))).thenReturn(meter1);
			when(storage.readData(UUID.fromString(TEST_BAD_DID))).thenReturn(device);
			when(storage.readData(UUID.fromString(TEST_DSID))).thenReturn(deviceset1);
			when(storage.readData(UUID.fromString(TEST_BAD_DSID))).thenReturn(deviceset2);
			when(meter1.getSample()).thenReturn(sample);
			when(meter1.getSample(any(DateTime.class), any(DateTime.class))).thenReturn(historicSample);
			when(meter2.getSample()).thenReturn(sample);
			when(meter2.getSample(any(DateTime.class), any(DateTime.class))).thenReturn(historicSample);
		} catch (StorageException | SensorFailedException e) {}
	}
	
	@Before
	public void setup() {
		devices1 = new ArrayList<Device>();
		devices2 = new ArrayList<Device>();

		devices1.add(device);
		devices1.add(meter1);
		devices1.add(meter2);

		devices2.add(device);
		
		when(deviceset1.iterator()).thenReturn(devices1.iterator());
		when(deviceset2.iterator()).thenReturn(devices2.iterator());
		when(gateway.iterator()).thenReturn(devices1.iterator());
	}
	
	@Test
	public void testGetNowRequestWithUser() {
		final String waterData = target("/water/" + TEST_PUID + "/now").request().get(String.class);
		EnvelopeBean envelope = gson.fromJson(waterData, EnvelopeBean.class);
		WaterBean bean = gson.fromJson(gson.toJson(envelope.response), WaterBean.class);
		assertEquals(200, envelope.code);
		assertEquals(TEST_PUID, bean.user);
		assertEquals(1, bean.samples.size());
		assertEquals(2 * SAMPLE_VOLUME, bean.summary.totalVolume, 0.001f);
		assertEquals(2 * SAMPLE_VOLUME, bean.samples.get(0).volume, 0.001f);
		assertEquals("now", bean.period.periodicity);
		assertTrue(bean.period.when.isBeforeNow());
		assertNull(bean.period.from);
		assertNull(bean.period.to);
		assertEquals("now", bean.period.periodicity);
	}
	
	@Test
	public void testGetNowRequestWithWaterMeter() {
		WaterBean bean = makeRequest("/water/" + TEST_DID + "/now");
		assertEquals(TEST_DID, bean.device);
		assertEquals(1, bean.samples.size());
		assertEquals(SAMPLE_VOLUME, bean.summary.totalVolume, 0.001f);
		assertEquals(SAMPLE_VOLUME, bean.samples.get(0).volume, 0.001f);
		assertEquals("now", bean.period.periodicity);
		assertTrue(bean.period.when.isBeforeNow());
		assertNull(bean.period.from);
		assertNull(bean.period.to);
		assertEquals("now", bean.period.periodicity);
	}
	
	@Test
	public void testGetNowRequestWithNonWaterMeterDevice() {
		Response response = target("/water/" + TEST_BAD_DID + "/now").request().get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testGetNowRequestWithDeviceSet() {
		WaterBean bean = makeRequest("/water/" + TEST_DSID + "/now");
		assertEquals(TEST_DSID, bean.deviceset);
		assertEquals(1, bean.samples.size());
		assertEquals(2 * SAMPLE_VOLUME, bean.summary.totalVolume, 0.001f);
		assertEquals(2 * SAMPLE_VOLUME, bean.samples.get(0).volume, 0.001f);
		assertEquals("now", bean.period.periodicity);
		assertTrue(bean.period.when.isBeforeNow());
		assertNull(bean.period.from);
		assertNull(bean.period.to);
		assertEquals("now", bean.period.periodicity);
	}
	
	@Test
	public void testGetNowRequestWithDeviceSetWithoutWaterMeter() {
		Response response = target("/water/" + TEST_BAD_DSID + "/now").request().get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testGetDailyRequestOneHour() {
		final String waterData = target("/water/" + TEST_PUID + "/daily")
				.queryParam("from", 1392681600000l)
				.queryParam("to", 1392685200000l)
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(waterData, EnvelopeBean.class);
		WaterBean bean = gson.fromJson(gson.toJson(envelope.response), WaterBean.class);
		assertEquals(1, bean.samples.size());
		assertEquals(2 * HISTORIC_SAMPLE_VOLUME, bean.samples.get(0).volume, 0.01f);
		assertNull(bean.period.when);
		assertEquals(from, bean.period.from);
		assertEquals(to, bean.period.to);
		assertEquals("daily", bean.period.periodicity);
	}
	
	@Test
	public void testGetDailyRequestOneHourWithWaterMeter() {
		final String waterData = target("/water/" + TEST_DID + "/daily")
				.queryParam("from", 1392681600000l)
				.queryParam("to", 1392685200000l)
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(waterData, EnvelopeBean.class);
		WaterBean bean = gson.fromJson(gson.toJson(envelope.response), WaterBean.class);
		assertEquals(1, bean.samples.size());
		assertEquals(HISTORIC_SAMPLE_VOLUME, bean.samples.get(0).volume, 0.01f);
		assertEquals("daily", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(from, bean.period.from);
		assertEquals(to, bean.period.to);
	}
	
	@Test
	public void testGetDailyRequestOneHourWithNonWaterMeterDevice() {
		Response response = target("/water/" + TEST_BAD_DID + "/now").request().get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testGetDailyRequestOneHourWithDeviceSet() {
		final String waterData = target("/water/" + TEST_DSID + "/daily")
				.queryParam("from", 1392681600000l)
				.queryParam("to", 1392685200000l)
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(waterData, EnvelopeBean.class);
		WaterBean bean = gson.fromJson(gson.toJson(envelope.response), WaterBean.class);
		assertEquals(1, bean.samples.size());
		assertEquals(2 * HISTORIC_SAMPLE_VOLUME, bean.samples.get(0).volume, 0.01f);
		assertEquals(2 * HISTORIC_SAMPLE_VOLUME, bean.summary.totalVolume, 0.01f);
		assertEquals("daily", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(from, bean.period.from);
		assertEquals(to, bean.period.to);
	}
	
	@Test
	public void testGetDailyRequestOneHourWithDeviceSetWithoutWaterMeter() {
		Response response = target("/water/" + TEST_BAD_DID + "/now").request().get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testGetDailyRequestThreeDays() {
		final String waterData = target("/water/" + TEST_PUID + "/daily")
				.queryParam("from", from.getMillis())
				.queryParam("to", threeDays.getMillis())
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(waterData, EnvelopeBean.class);
		WaterBean bean = gson.fromJson(gson.toJson(envelope.response), WaterBean.class);
		assertEquals(3, bean.samples.size());
		assertEquals(2 * HISTORIC_SAMPLE_VOLUME, bean.samples.get(0).volume, 0.01f);
		assertEquals(6 * HISTORIC_SAMPLE_VOLUME, bean.summary.totalVolume, 0.01f);
		assertEquals("daily", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(from, bean.period.from);
		assertEquals(threeDays, bean.period.to);
	}
	
	@Test
	public void testGetDailyRequestThreeDaysOneDevice() {
		final String waterData = target("/water/" + TEST_DID + "/daily")
				.queryParam("from", from.getMillis())
				.queryParam("to", threeDays.getMillis())
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(waterData, EnvelopeBean.class);
		WaterBean bean = gson.fromJson(gson.toJson(envelope.response), WaterBean.class);
		assertEquals(3, bean.samples.size());
		assertEquals(1 * HISTORIC_SAMPLE_VOLUME, bean.samples.get(0).volume, 0.01f);
		assertEquals(3 * HISTORIC_SAMPLE_VOLUME, bean.summary.totalVolume, 0.01f);
		assertEquals("daily", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(from, bean.period.from);
		assertEquals(threeDays, bean.period.to);
	}
	
	@Test
	public void testBadUUID() {
		final Response response = target("/water/saasdf/now").request().get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testNoSuchId() {
		when(userService.getPlatformUser(any(UUID.class))).thenReturn(null);
		final Response response = target("/water/00001111-2222-3333-4444-555566667778/now")
				.request().get();
		assertEquals(404, response.getStatus());
		when(userService.getPlatformUser(any(UUID.class))).thenReturn(platformUser);
	}
	
	@Test
	public void testFromDateIsInTheFutureDaily() {
		final Response response = target("/water/" + TEST_PUID + "/daily")
				.queryParam("from", futureDate())
				.request()
				.get();
		assertEquals(400, response.getStatus());
	}

	@Test
	public void testFromDateIsInTheFutureWeekly() {
		final Response response = target("/water/" + TEST_PUID + "/weekly")
				.queryParam("from", futureDate())
				.request()
				.get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testFromDateIsInTheFutureMonthly() {
		final Response response = target("/water/" + TEST_PUID + "/monthly")
				.queryParam("from", futureDate())
				.request()
				.get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testFromDateIsBadlyFormatted() {
		final Response response = target("/water/" + TEST_PUID + "/monthly")
				.queryParam("from", "mumbojumbo")
				.request()
				.get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testToDateIsBadlyFormatted() {
		final Response response = target("/water/" + TEST_PUID + "/monthly")
				.queryParam("from", 1392681600000l)
				.queryParam("to", "lajbans")
				.request()
				.get();
		assertEquals(400, response.getStatus());
	}

	private WaterBean makeRequest(String uri) {
		final String waterData = target(uri).request().get(String.class);
		EnvelopeBean envelope = gson.fromJson(waterData, EnvelopeBean.class);
		WaterBean bean = gson.fromJson(gson.toJson(envelope.response), WaterBean.class);
		return bean;
	}

	private long futureDate() {
		return DateTime.now().plusDays(1).getMillis();
	}
}
