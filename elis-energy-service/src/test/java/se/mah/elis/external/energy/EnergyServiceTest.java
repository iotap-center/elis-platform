package se.mah.elis.external.energy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.MainPowerMeter;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;
import se.mah.elis.external.beans.EnvelopeBean;
import se.mah.elis.external.beans.helpers.DateTimeAdapter;
import se.mah.elis.external.energy.beans.EnergyBean;
import se.mah.elis.external.energy.beans.EnergyDataBean;
import se.mah.elis.external.energy.beans.EnergyDeviceBean;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EnergyServiceTest extends JerseyTest {

	private static final String PLATFORM_USER = "00001111-2222-3333-4444-555566667777";
	private static final String PLATFORM_USER_WITH_MAIN_METER = "01001111-2222-3333-4444-555566667777";
	private static final String METER = "10001111-2222-3333-4444-555566667777";
	private static final String METER2 = "11001111-2222-3333-4444-555566667777";
	private static final String MAIN_METER1 = "12001111-2222-3333-4444-555566667777";
	private static final String MAIN_METER2 = "13001111-2222-3333-4444-555566667777";
	private static final String MAIN_METER3 = "14001111-2222-3333-4444-555566667777";
	private static final String BAD_DEVICE = "20001111-2222-3333-4444-555566667777";
	private static final String DEVICESET = "30001111-2222-3333-4444-555566667777";
	private static final String DEVICESET_WITH_MAIN_METER = "31001111-2222-3333-4444-555566667777";
	private static final String BAD_DEVICESET = "40001111-2222-3333-4444-555566667777";
	private static final long TWENTYFOUR_HOURS = 1392768000000l; // T + 24 h
	private static final long ALMOST_TWENTYFOUR_HOURS = 1392767999000l; // T + 24 h - 1 s
	private static final long TEN_DAYS = 1393545600000l; // T + 24 h - 1 s
	private static final long ONE_HOUR = 1392685200000l; // T + 1 h
	private static final long FROM_TIME = 1392681600000l; // T
	private static final String DEVICE = "device-uuid-";
	private static final Double DEVICE_KWH = 0.02d;
	private static final Double DEVICE_VOLTAGE = 42d;
	private static final Double MAIN_DEVICE_KWH = 0.03d;
	private static final Double MAIN_DEVICE_VOLTAGE = 13d;
	private static final int MODE_HOURS = 0;
	private static final int MODE_DAYS = 1;
	private static UserService userService;
	private static Storage storage;
	private static PlatformUser platformUser;
	private static PlatformUser platformUser2;
	private static GatewayUser gatewayUser;
	private static GatewayUser gatewayUser2;
	private static Gateway gateway;
	private static Gateway gateway2;
	private static ElectricitySample sample;
	private static ElectricitySample historicSample;
	private static ElectricitySample sample2;
	private static ElectricitySample historicSample2;
	private static Device device;
	private static ElectricitySampler meter1;
	private static ElectricitySampler meter2;
	private static MainPowerMeter mainMeter1;
	private static MainPowerMeter mainMeter2;
	private static MainPowerMeter mainMeter3;
	private static DeviceSet deviceset1;
	private static DeviceSet deviceset2;
	private static DeviceSet deviceset3;
	private List<Device> devices1 = new ArrayList<Device>();
	private List<Device> devices2 = new ArrayList<Device>();
	private List<Device> devices3 = new ArrayList<Device>();
	private List<Device> devices4 = new ArrayList<Device>();
	private List<Device> devices5 = new ArrayList<Device>();
	private static List<ElectricitySample> oneSample = new ArrayList<ElectricitySample>();
	private static List<ElectricitySample> tenLongSamples = new ArrayList<ElectricitySample>();
	private static List<ElectricitySample> twentyFourSamples = new ArrayList<ElectricitySample>();
	private static LogService log;
	private static DateTime from = new DateTime(FROM_TIME);
	private static DateTime to = new DateTime(TWENTYFOUR_HOURS);
	private static DateTime oneHour = new DateTime(ONE_HOUR);
	private static DateTime tenDays = new DateTime(TEN_DAYS);
	private static DateTime now = DateTime.now();
	private static DateTime sampleTime = to;
	
	private GsonBuilder gsonBuilder = new GsonBuilder()
		.registerTypeAdapter(DateTime.class, new DateTimeAdapter());
	private Gson gson = gsonBuilder.create();

	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		
		config.register(new EnergyService(userService, storage, log));
		
		return config;
	}
	
	@BeforeClass
	public static void setupSuite() {
		log = mock(LogService.class);
		
		platformUser = mock(PlatformUser.class);
		when(platformUser.getUserId()).thenReturn(UUID.fromString(PLATFORM_USER));
		
		platformUser2 = mock(PlatformUser.class);
		when(platformUser2.getUserId()).thenReturn(UUID.fromString(PLATFORM_USER_WITH_MAIN_METER));
		
		gateway = mock(Gateway.class);
		gatewayUser = mock(GatewayUser.class);
		when(gatewayUser.getGateway()).thenReturn(gateway);

		gateway2 = mock(Gateway.class);
		gatewayUser2 = mock(GatewayUser.class);
		when(gatewayUser2.getGateway()).thenReturn(gateway2);
		
		sample = mock(ElectricitySample.class);		
		when(sample.getSampleTimestamp()).thenReturn(now);
		when(sample.getCurrentPower()).thenReturn(DEVICE_KWH);
		when(sample.getCurrentVoltage()).thenReturn(DEVICE_VOLTAGE);
		
		historicSample = mock(ElectricitySample.class);
		when(historicSample.getSampleTimestamp()).thenReturn(sampleTime);
		when(historicSample.getCurrentPower()).thenReturn(DEVICE_KWH);
		when(historicSample.getCurrentVoltage()).thenReturn(DEVICE_VOLTAGE);
		
		sample2 = mock(ElectricitySample.class);		
		when(sample2.getSampleTimestamp()).thenReturn(now);
		when(sample2.getCurrentPower()).thenReturn(MAIN_DEVICE_KWH);
		when(sample2.getCurrentVoltage()).thenReturn(MAIN_DEVICE_VOLTAGE);
		
		historicSample2 = mock(ElectricitySample.class);
		when(historicSample2.getSampleTimestamp()).thenReturn(sampleTime);
		when(historicSample2.getCurrentPower()).thenReturn(MAIN_DEVICE_KWH);
		when(historicSample2.getCurrentVoltage()).thenReturn(MAIN_DEVICE_VOLTAGE);
		
		device = mock(Device.class);
		when(device.getName()).thenReturn(DEVICE + "0");
		when(device.getDescription()).thenReturn("Description: device");
		when(device.getDataId()).thenReturn(UUID.fromString(BAD_DEVICE));
		when(device.getOwnerId()).thenReturn(UUID.fromString(PLATFORM_USER));
		
		meter1 = mock(ElectricitySampler.class);
		when(meter1.getName()).thenReturn(DEVICE + "1");
		when(meter1.getDescription()).thenReturn("Description: meter1");
		when(meter1.getDataId()).thenReturn(UUID.fromString(METER));
		when(meter1.getOwnerId()).thenReturn(UUID.fromString(PLATFORM_USER));
		
		meter2 = mock(ElectricitySampler.class);
		when(meter2.getName()).thenReturn(DEVICE + "2");
		when(meter2.getDescription()).thenReturn("Description: meter2");
		when(meter2.getDataId()).thenReturn(UUID.fromString(METER2));
		when(meter2.getOwnerId()).thenReturn(UUID.fromString(PLATFORM_USER));
		
		mainMeter1 = mock (MainPowerMeter.class);
		when(mainMeter1.getName()).thenReturn(DEVICE + "3");
		when(mainMeter1.getDescription()).thenReturn("Description: meter3");
		when(mainMeter1.getDataId()).thenReturn(UUID.fromString(MAIN_METER1));
		when(mainMeter1.getOwnerId()).thenReturn(UUID.fromString(PLATFORM_USER_WITH_MAIN_METER));
		
		mainMeter2 = mock (MainPowerMeter.class);
		when(mainMeter2.getName()).thenReturn(DEVICE + "4");
		when(mainMeter2.getDescription()).thenReturn("Description: meter4");
		when(mainMeter2.getDataId()).thenReturn(UUID.fromString(MAIN_METER2));
		when(mainMeter2.getOwnerId()).thenReturn(UUID.fromString(PLATFORM_USER_WITH_MAIN_METER));
		
		mainMeter3 = mock (MainPowerMeter.class);
		when(mainMeter3.getName()).thenReturn(DEVICE + "5");
		when(mainMeter3.getDescription()).thenReturn("Description: meter5");
		when(mainMeter3.getDataId()).thenReturn(UUID.fromString(MAIN_METER3));
		when(mainMeter3.getOwnerId()).thenReturn(UUID.fromString(PLATFORM_USER_WITH_MAIN_METER));
		
		deviceset1 = mock(DeviceSet.class);
		when(deviceset1.getDataId()).thenReturn(UUID.fromString(DEVICESET));
		when(deviceset1.getOwnerId()).thenReturn(UUID.fromString(PLATFORM_USER));
		
		deviceset2 = mock(DeviceSet.class);
		when(deviceset2.getDataId()).thenReturn(UUID.fromString(BAD_DEVICESET));
		when(deviceset2.getOwnerId()).thenReturn(UUID.fromString(PLATFORM_USER));
		
		deviceset3 = mock(DeviceSet.class);
		when(deviceset3.getDataId()).thenReturn(UUID.fromString(DEVICESET_WITH_MAIN_METER));
		when(deviceset3.getOwnerId()).thenReturn(UUID.fromString(PLATFORM_USER_WITH_MAIN_METER));
		
		storage = mock(Storage.class);
		try {
			when(storage.readData(UUID.fromString(PLATFORM_USER))).thenThrow(new StorageException());
			when(storage.readData(UUID.fromString(PLATFORM_USER_WITH_MAIN_METER))).thenThrow(new StorageException());
			when(storage.readData(UUID.fromString(METER))).thenReturn(meter1);
			when(storage.readData(UUID.fromString(METER2))).thenReturn(meter2);
			when(storage.readData(UUID.fromString(MAIN_METER1))).thenReturn(mainMeter1);
			when(storage.readData(UUID.fromString(MAIN_METER2))).thenReturn(mainMeter2);
			when(storage.readData(UUID.fromString(MAIN_METER3))).thenReturn(mainMeter3);
			when(storage.readData(UUID.fromString(BAD_DEVICE))).thenReturn(device);
			when(storage.readData(UUID.fromString(DEVICESET))).thenReturn(deviceset1);
			when(storage.readData(UUID.fromString(DEVICESET_WITH_MAIN_METER))).thenReturn(deviceset3);
			when(storage.readData(UUID.fromString(BAD_DEVICESET))).thenReturn(deviceset2);
			when(meter1.getSample()).thenReturn(sample);
			when(meter2.getSample()).thenReturn(sample);
			when(mainMeter1.getSample()).thenReturn(sample2);
			when(mainMeter2.getSample()).thenReturn(sample2);
			when(mainMeter3.getSample()).thenReturn(sample2);
		} catch (StorageException | SensorFailedException e) {}
		
		userService = mock(UserService.class);
		when(userService.getPlatformUser(UUID.fromString(PLATFORM_USER))).thenReturn(platformUser);
		when(userService.getPlatformUser(UUID.fromString(PLATFORM_USER_WITH_MAIN_METER))).thenReturn(platformUser2);
		when(userService.getUsers(platformUser)).thenReturn(new User[] { gatewayUser });
		when(userService.getUser(platformUser, UUID.fromString(PLATFORM_USER))).thenReturn(gatewayUser);
		when(userService.getUsers(platformUser2)).thenReturn(new User[] { gatewayUser2 });
		when(userService.getUser(platformUser2, UUID.fromString(PLATFORM_USER_WITH_MAIN_METER))).thenReturn(gatewayUser2);
		
		
	}
	
	@Before
	public void setup() throws SensorFailedException {
		setup(4, 24);
		oneSample = createSamples(1, MODE_HOURS);
		tenLongSamples = createSamples(10, MODE_DAYS);
		twentyFourSamples = createSamples(24, MODE_HOURS);
		
		// Always reset meter2
		
		meter2 = mock(ElectricitySampler.class);
		when(meter2.getName()).thenReturn(DEVICE + "2");
		when(meter2.getDescription()).thenReturn("Description: meter2");
		when(meter2.getDataId()).thenReturn(UUID.fromString(METER2));
		when(meter2.getOwnerId()).thenReturn(UUID.fromString(PLATFORM_USER));
		when(meter2.getSample()).thenReturn(sample);
		
		devices1 = new ArrayList<Device>();
		devices2 = new ArrayList<Device>();
		devices3 = new ArrayList<Device>();
		devices4 = new ArrayList<Device>();
		devices5 = new ArrayList<Device>();

		devices1.add(device);
		devices1.add(meter1);
		devices1.add(meter2);

		devices2.add(device);

		devices3.add(meter1);
		devices3.add(meter2);
		
		devices4.add(meter1);
		devices4.add(device);
		
		devices5.add(mainMeter2);
		devices5.add(meter1);
		devices5.add(meter2);
		
		when(deviceset1.iterator()).thenReturn(devices1.iterator());
		when(deviceset2.iterator()).thenReturn(devices2.iterator());
		when(deviceset3.iterator()).thenReturn(devices5.iterator());
		when(mainMeter1.iterator()).thenReturn(devices3.iterator());
		when(mainMeter2.iterator()).thenReturn(devices4.iterator());
		when(mainMeter3.iterator()).thenReturn(devices2.iterator());
		when(gateway.iterator()).thenReturn(devices1.iterator());
		when(gateway2.iterator()).thenReturn(devices5.iterator());

		when(mainMeter1.toArray()).thenReturn(devices3.toArray(new Device[0]));
		when(mainMeter2.toArray()).thenReturn(devices4.toArray(new Device[0]));
		when(mainMeter3.toArray()).thenReturn(devices2.toArray(new Device[0]));

		when(mainMeter1.toArray(any(Device[].class))).thenReturn(devices3.toArray(new Device[0]));
		when(mainMeter2.toArray(any(Device[].class))).thenReturn(devices4.toArray(new Device[0]));
		when(mainMeter3.toArray(any(Device[].class))).thenReturn(devices2.toArray(new Device[0]));

		when(meter1.getSamples(from, oneHour)).thenReturn(oneSample);
		when(meter2.getSamples(from, oneHour)).thenReturn(oneSample);
		when(mainMeter1.getSamples(from, oneHour)).thenReturn(oneSample);
		when(mainMeter2.getSamples(from, oneHour)).thenReturn(oneSample);
		when(mainMeter3.getSamples(from, oneHour)).thenReturn(oneSample);

		when(meter1.getSamples(from, to)).thenReturn(twentyFourSamples);
		when(meter2.getSamples(from, to)).thenReturn(twentyFourSamples);
		when(mainMeter1.getSamples(from, to)).thenReturn(twentyFourSamples);
		when(mainMeter2.getSamples(from, to)).thenReturn(twentyFourSamples);
		when(mainMeter3.getSamples(from, to)).thenReturn(twentyFourSamples);

		when(meter1.getSamples(from, tenDays)).thenReturn(tenLongSamples);
		when(meter2.getSamples(from, tenDays)).thenReturn(tenLongSamples);
		when(mainMeter1.getSamples(from, tenDays)).thenReturn(tenLongSamples);
		when(mainMeter2.getSamples(from, tenDays)).thenReturn(tenLongSamples);
		when(mainMeter3.getSamples(from, tenDays)).thenReturn(tenLongSamples);
	}
	
	private void setup(int numberOfMeters, int samples) {
		List<Device> devices = createMeters(numberOfMeters, samples);
		gateway.clear();
		gateway.addAll(devices);
		when(gateway.iterator()).thenReturn(devices.iterator());

		try {
			when(meter1.getSamples(from, new DateTime(ALMOST_TWENTYFOUR_HOURS))).thenReturn(twentyFourSamples);
			when(meter2.getSamples(from, new DateTime(ALMOST_TWENTYFOUR_HOURS))).thenReturn(twentyFourSamples);
			when(mainMeter1.getSamples(from, new DateTime(ALMOST_TWENTYFOUR_HOURS))).thenReturn(twentyFourSamples);
			when(mainMeter2.getSamples(from, new DateTime(ALMOST_TWENTYFOUR_HOURS))).thenReturn(twentyFourSamples);
			when(mainMeter3.getSamples(from, new DateTime(ALMOST_TWENTYFOUR_HOURS))).thenReturn(twentyFourSamples);
		} catch (SensorFailedException e) {}
		
	}
	
	private void setupForDailyTests() throws SensorFailedException {
		when(meter1.getSamples(from, new DateTime(ALMOST_TWENTYFOUR_HOURS))).thenReturn(null);
		when(meter2.getSamples(from, new DateTime(ALMOST_TWENTYFOUR_HOURS))).thenReturn(null);
		when(mainMeter1.getSamples(from, new DateTime(ALMOST_TWENTYFOUR_HOURS))).thenReturn(null);
		when(mainMeter2.getSamples(from, new DateTime(ALMOST_TWENTYFOUR_HOURS))).thenReturn(null);
		when(mainMeter3.getSamples(from, new DateTime(ALMOST_TWENTYFOUR_HOURS))).thenReturn(null);

		when(meter1.getSamples(from, new DateTime(TWENTYFOUR_HOURS))).thenReturn(tenLongSamples);
		when(meter2.getSamples(from, new DateTime(TWENTYFOUR_HOURS))).thenReturn(tenLongSamples);
		when(mainMeter1.getSamples(from, new DateTime(TWENTYFOUR_HOURS))).thenReturn(tenLongSamples);
		when(mainMeter2.getSamples(from, new DateTime(TWENTYFOUR_HOURS))).thenReturn(tenLongSamples);
		when(mainMeter3.getSamples(from, new DateTime(TWENTYFOUR_HOURS))).thenReturn(tenLongSamples);
	}

	private List<Device> createMeters(int numberOfMeters, int samples) {
		List<Device> meters = new ArrayList<>();
		
		for (int i = 0; i < numberOfMeters; i++) {
			ElectricitySampler meter = createSampleMeter(i, samples);
			meters.add(meter);
		}
		
		return meters;
	}

	private ElectricitySampler createSampleMeter(int id, int size) {
		DateTime from = new DateTime(FROM_TIME);
		
		ElectricitySample deviceSample = mock(ElectricitySample.class);
		when(deviceSample.getSampleTimestamp()).thenReturn(from);
		when(deviceSample.getTotalEnergyUsageInWh()).thenReturn(DEVICE_KWH*1000);
		when(deviceSample.getCurrentPower()).thenReturn(DEVICE_KWH*1000);
		
		ElectricitySampler meter = mock(ElectricitySampler.class);
		when(meter.getName()).thenReturn(DEVICE + id + "-name");
		try {
			when(meter.getSample()).thenReturn(deviceSample);
			List<ElectricitySample> samples = createSamples(size, MODE_HOURS);
			when(meter.getSamples(any(DateTime.class), any(DateTime.class))).thenReturn(samples);
		} catch (SensorFailedException e) {}
		
		return meter;
	}
	
	private List<ElectricitySample> createSamples(int size, int mode) {
		List<ElectricitySample> samples = new ArrayList<>();
		
		for (int i = 0; i < size; i++) {
			samples.add(createSample(i, mode));
		}
		
		return samples;
	}

	private ElectricitySample createSample(int i, int mode) {
		DateTime date = null;
		ElectricitySample sample = mock(ElectricitySample.class);
		
		switch (mode) {
		case MODE_HOURS:
			date = new DateTime(FROM_TIME).plusHours(1 + i);
			break;
		case MODE_DAYS:
			date = new DateTime(FROM_TIME).plusDays(1 + i);
			break;
		}
		
		when(sample.getTotalEnergyUsageInWh()).thenReturn(DEVICE_KWH);
		when(sample.getSampleTimestamp()).thenReturn(date);
		
		return sample;
	}

	
	private String getFirstDevice(List<EnergyDeviceBean> devices) {
		List<String> names = new ArrayList<>();
		
		for (EnergyDeviceBean bean : devices) 
			names.add(bean.deviceId);
		
		Collections.sort(names);
		
		return names.get(0);
	}

	private EnvelopeBean getNowRequest(String uuid) {
		final String energyNowData = target("/energy/" + uuid + "/now")
				.request().get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyNowData, EnvelopeBean.class);
		
		envelope.response = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		return envelope;
	}
	
//	@Test
//	public void testFail() {
//		fail("skriv tester med weekly och monthly");
//	}

	@Test
	public void testGetNowRequestWithUser() {
		EnvelopeBean envelope = getNowRequest(PLATFORM_USER);
		EnergyBean bean = (EnergyBean) envelope.response;
		
		// Validate
		assertEquals(PLATFORM_USER, bean.user);
		assertEquals("now", bean.period.periodicity);
		assertTrue(bean.period.when.isBefore(DateTime.now()));
		assertNull(bean.period.from);
		assertNull(bean.period.to);
		assertEquals(2 * DEVICE_VOLTAGE, bean.samples.get(0).watts, 0.01f);
//		assertEquals(0, bean.samples.get(0).kwh, 0); // This would fail, due to the way Mockito works
		assertEquals(1, bean.samples.size());
	}

	@Test
	public void testGetNowRequestWithMeter() {
		EnvelopeBean envelope = getNowRequest(METER);
		EnergyBean bean = (EnergyBean) envelope.response;
		
		// Validate
		assertEquals(METER, bean.device);
		assertEquals("now", bean.period.periodicity);
		assertTrue(bean.period.when.isBefore(DateTime.now()));
		assertNull(bean.period.from);
		assertNull(bean.period.to);
		assertEquals(DEVICE_VOLTAGE, bean.samples.get(0).watts, 0.01f);
//		assertEquals(0, bean.samples.get(0).kwh, 0); // This would fail, due to the way Mockito works
		assertEquals(1, bean.samples.size());
	}
	
	@Test
	public void testGetNowRequestWithNonMeterDevice() {
		Response response = target("/energy/" + BAD_DEVICE + "/now").request().get();
		assertEquals(400, response.getStatus());
	}

	@Test
	public void testGetNowRequestWithDeviceSet() {
		EnvelopeBean envelope = getNowRequest(DEVICESET);
		EnergyBean bean = (EnergyBean) envelope.response;
		
		// Validate
		assertEquals(DEVICESET, bean.deviceset);
		assertEquals("now", bean.period.periodicity);
		assertTrue(bean.period.when.isBefore(DateTime.now()));
		assertNull(bean.period.from);
		assertNull(bean.period.to);
		assertEquals(2 * DEVICE_VOLTAGE, bean.samples.get(0).watts, 0.01f);
//		assertEquals(0, bean.samples.get(0).kwh, 0); // This would fail, due to the way Mockito works
		assertEquals(1, bean.samples.size());
	}

	@Test
	public void testGetNowRequestWithMainPowerMeter() {
		EnvelopeBean envelope = getNowRequest(MAIN_METER1);
		EnergyBean bean = (EnergyBean) envelope.response;
		
		// Validate
		assertEquals(MAIN_METER1, bean.device);
		assertEquals("now", bean.period.periodicity);
		assertTrue(bean.period.when.isBefore(DateTime.now()));
		assertNull(bean.period.from);
		assertNull(bean.period.to);
		assertEquals(MAIN_DEVICE_VOLTAGE, bean.samples.get(0).watts, 0.01f);
//		assertEquals(0, bean.samples.get(0).kwh, 0); // This would fail, due to the way Mockito works
		assertEquals(1, bean.samples.size());
	}

	@Test
	public void testGetNowRequestWithDeviceSetContainingAMainPowerMeter() {
		EnvelopeBean envelope = getNowRequest(DEVICESET_WITH_MAIN_METER);
		EnergyBean bean = (EnergyBean) envelope.response;
		
		// Validate
		assertEquals(DEVICESET_WITH_MAIN_METER, bean.deviceset);
		assertEquals("now", bean.period.periodicity);
		assertTrue(bean.period.when.isBefore(DateTime.now()));
		assertNull(bean.period.from);
		assertNull(bean.period.to);
		assertEquals(DEVICE_VOLTAGE + MAIN_DEVICE_VOLTAGE, bean.samples.get(0).watts, 0.01f);
//		assertEquals(0, bean.samples.get(0).kwh, 0); // This would fail, due to the way Mockito works
		assertEquals(1, bean.samples.size());
	}

	@Test
	public void testGetNowRequestWithDeviceSetContainingAMainPowerMeterNotContainingOtherMeters() {
		EnvelopeBean envelope = getNowRequest(MAIN_METER3);
		EnergyBean bean = (EnergyBean) envelope.response;
		
		// Validate
		assertEquals(MAIN_METER3, bean.device);
		assertEquals("now", bean.period.periodicity);
		assertTrue(bean.period.when.isBefore(DateTime.now()));
		assertNull(bean.period.from);
		assertNull(bean.period.to);
		assertEquals(MAIN_DEVICE_VOLTAGE, bean.samples.get(0).watts, 0.01f);
//		assertEquals(0, bean.samples.get(0).kwh, 0); // This would fail, due to the way Mockito works
		assertEquals(1, bean.samples.size());
	}

	@Test
	public void testGetNowRequestWithUserHavingAMainPowerMeter() {
		EnvelopeBean envelope = getNowRequest(PLATFORM_USER_WITH_MAIN_METER);
		EnergyBean bean = (EnergyBean) envelope.response;
		
		// Validate
		assertEquals(PLATFORM_USER_WITH_MAIN_METER, bean.user);
		assertEquals("now", bean.period.periodicity);
		assertTrue(bean.period.when.isBefore(DateTime.now()));
		assertNull(bean.period.from);
		assertNull(bean.period.to);
		assertEquals(DEVICE_VOLTAGE + MAIN_DEVICE_VOLTAGE, bean.samples.get(0).watts, 0.01f);
//		assertEquals(0, bean.samples.get(0).kwh, 0); // This would fail, due to the way Mockito works
		assertEquals(1, bean.samples.size());
	}
	
	@Test
	public void testGetNowRequestWithDeviceSetWithoutMeters() {
		Response response = target("/energy/" + BAD_DEVICESET + "/now").request().get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testGetHourlyStatsUserTwentyFourHours() {
		setup(1, 25);
		
		final String energyHourlydata = target("/energy/" + PLATFORM_USER + "/hourly")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(TWENTYFOUR_HOURS))
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyHourlydata, EnvelopeBean.class);
		EnergyBean bean = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		// Validate
		assertEquals("hourly", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(new DateTime(FROM_TIME), bean.period.from);
		assertEquals(new DateTime(TWENTYFOUR_HOURS), bean.period.to);
		assertEquals(24, bean.samples.size());
		assertEquals(ONE_HOUR, ((EnergyDataBean) bean.samples.get(0)).timestamp);
		assertEquals(ONE_HOUR + 3600000, ((EnergyDataBean) bean.samples.get(1)).timestamp);
		assertEquals(TWENTYFOUR_HOURS, ((EnergyDataBean) bean.samples.get(23)).timestamp);
		assertNotNull(bean.summary);
		assertEquals(24*DEVICE_KWH/1000, bean.summary.kwh, 0.000001d);
	}
	
	@Test
	public void testGetHourlyStatsUserTwentyFourHoursOneFaultyMeter() {
		setup(1, 25);
		try {
			when(meter2.getSamples(from, new DateTime(TWENTYFOUR_HOURS))).thenThrow(new SensorFailedException());
		} catch (SensorFailedException e) {}
		
		final String energyHourlydata = target("/energy/" + PLATFORM_USER + "/hourly")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(TWENTYFOUR_HOURS))
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyHourlydata, EnvelopeBean.class);
		EnergyBean bean = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		// Validate
		assertEquals("hourly", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(new DateTime(FROM_TIME), bean.period.from);
		assertEquals(new DateTime(TWENTYFOUR_HOURS), bean.period.to);
		assertEquals(24, bean.samples.size());
		assertEquals(ONE_HOUR, ((EnergyDataBean) bean.samples.get(0)).timestamp);
		assertEquals(ONE_HOUR + 3600000, ((EnergyDataBean) bean.samples.get(1)).timestamp);
		assertEquals(TWENTYFOUR_HOURS, ((EnergyDataBean) bean.samples.get(23)).timestamp);
		assertNotNull(bean.summary);
		assertEquals(24*DEVICE_KWH/1000, bean.summary.kwh, 0.000001d);
	}
	
	@Test
	public void testGetHourlyStatsOneDeviceTwentyFourHours() {
		setup(1, 25);
		
		final String energyHourlydata = target("/energy/" + METER + "/hourly")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(TWENTYFOUR_HOURS))
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyHourlydata, EnvelopeBean.class);
		EnergyBean bean = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		// Validate
		assertEquals("hourly", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(new DateTime(FROM_TIME), bean.period.from);
		assertEquals(new DateTime(TWENTYFOUR_HOURS), bean.period.to);
		assertEquals(24, bean.samples.size());
		assertEquals(ONE_HOUR, ((EnergyDataBean) bean.samples.get(0)).timestamp);
		assertEquals(ONE_HOUR + 3600000, ((EnergyDataBean) bean.samples.get(1)).timestamp);
		assertEquals(TWENTYFOUR_HOURS, ((EnergyDataBean) bean.samples.get(23)).timestamp);
		assertNotNull(bean.summary);
		assertEquals(24*DEVICE_KWH/1000, bean.summary.kwh, 0.000001d);
	}
	
	@Test
	public void testGetHourlyStatsOneDeviceOneHour() {
		setup(1, 2);
		
		final String energyHourlydata = target("/energy/" + METER + "/hourly")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(ONE_HOUR))
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyHourlydata, EnvelopeBean.class);
		EnergyBean bean = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		// Validate
		assertEquals("hourly", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(new DateTime(FROM_TIME), bean.period.from);
		assertEquals(new DateTime(ONE_HOUR), bean.period.to);
		assertEquals(1, bean.samples.size());
		assertEquals(ONE_HOUR, bean.samples.get(0).timestamp);
		assertNotNull(bean.summary);
		assertEquals(1 * DEVICE_KWH / 1000, bean.summary.kwh, 0.000001d);
	}
	
	@Test
	public void testGetHourlyStatsOneDeviceAlmostTwentyFourHours() {
		setup(1, 24);
		
		final String energyHourlydata = target("/energy/" + METER + "/hourly")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(ALMOST_TWENTYFOUR_HOURS))
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyHourlydata, EnvelopeBean.class);
		EnergyBean bean = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		// Validate
		assertEquals("hourly", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(new DateTime(FROM_TIME), bean.period.from);
		assertEquals(new DateTime(TWENTYFOUR_HOURS - 1000), bean.period.to);
		assertEquals(23, bean.samples.size());
		assertEquals(ONE_HOUR, ((EnergyDataBean) bean.samples.get(0)).timestamp);
		assertEquals(ONE_HOUR + 3600000, ((EnergyDataBean) bean.samples.get(1)).timestamp);
		assertEquals(TWENTYFOUR_HOURS - 3600000, ((EnergyDataBean) bean.samples.get(22)).timestamp);
		assertNotNull(bean.summary);
		assertEquals(23*DEVICE_KWH/1000, bean.summary.kwh, 0.000001d);
	}
	
	@Test
	public void testGetHourlyStatsDeviceSetOneHour() {
		setup(1, 2);
		
		final String energyHourlydata = target("/energy/" + DEVICESET + "/hourly")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(ONE_HOUR))
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyHourlydata, EnvelopeBean.class);
		EnergyBean bean = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		// Validate
		assertEquals("hourly", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(new DateTime(FROM_TIME), bean.period.from);
		assertEquals(new DateTime(ONE_HOUR), bean.period.to);
		assertEquals(1, bean.samples.size());
		assertEquals(ONE_HOUR, bean.samples.get(0).timestamp);
		assertNotNull(bean.summary);
		assertEquals(2*DEVICE_KWH/1000, bean.summary.kwh, 0.000001d);
	}
	
	@Test
	public void testGetHourlyStatsUserOneHour() {
		setup(1, 2);
		
		final String energyHourlydata = target("/energy/" + PLATFORM_USER + "/hourly")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(ONE_HOUR))
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyHourlydata, EnvelopeBean.class);
		EnergyBean bean = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		// Validate
		assertEquals("hourly", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(new DateTime(FROM_TIME), bean.period.from);
		assertEquals(new DateTime(ONE_HOUR), bean.period.to);
		assertEquals(1, bean.samples.size());
		assertEquals(ONE_HOUR, bean.samples.get(0).timestamp);
		assertNotNull(bean.summary);
		assertEquals(DEVICE_KWH/1000, bean.summary.kwh, 0.000001d);
	}
	
	@Test
	public void testGetHourlyStatsUserWithMainMeterOneHour() {
		setup(1, 2);
		
		final String energyHourlydata = target("/energy/" + PLATFORM_USER_WITH_MAIN_METER + "/hourly")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(ONE_HOUR))
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyHourlydata, EnvelopeBean.class);
		EnergyBean bean = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		// Validate
		assertEquals("hourly", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(new DateTime(FROM_TIME), bean.period.from);
		assertEquals(new DateTime(ONE_HOUR), bean.period.to);
		assertEquals(1, bean.samples.size());
		assertEquals(ONE_HOUR, bean.samples.get(0).timestamp);
		assertNotNull(bean.summary);
		assertEquals(2 * DEVICE_KWH/1000, bean.summary.kwh, 0.000001d);
	}
	
	@Test
	public void testGetDailyStatsOneDeviceOneDay() {
		try {
			setupForDailyTests();
		} catch (SensorFailedException e) {}
		
		final String energyHourlydata = target("/energy/" + METER + "/daily")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(TWENTYFOUR_HOURS))
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyHourlydata, EnvelopeBean.class);
		EnergyBean bean = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		// Validate
		assertEquals("daily", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(new DateTime(FROM_TIME), bean.period.from);
		assertEquals(new DateTime(TWENTYFOUR_HOURS), bean.period.to);
		assertEquals(1, bean.samples.size());
		assertEquals(TWENTYFOUR_HOURS, bean.samples.get(0).timestamp);
		assertNotNull(bean.summary);
		assertEquals(1 * DEVICE_KWH / 1000, bean.summary.kwh, 0.000001d);
	}
	
	@Test
	public void testGetDailyStatsOneDeviceTenDays() {
		try {
			setupForDailyTests();
		} catch (SensorFailedException e) {}
		
		final String energyHourlydata = target("/energy/" + METER + "/daily")
				.queryParam("from", Long.toString(FROM_TIME))
				.queryParam("to", Long.toString(TEN_DAYS))
				.request()
				.get(String.class);
		EnvelopeBean envelope = gson.fromJson(energyHourlydata, EnvelopeBean.class);
		EnergyBean bean = gson.fromJson(gson.toJson(envelope.response), EnergyBean.class);
		
		// Validate
		assertEquals("daily", bean.period.periodicity);
		assertNull(bean.period.when);
		assertEquals(new DateTime(FROM_TIME), bean.period.from);
		assertEquals(new DateTime(TEN_DAYS), bean.period.to);
		assertEquals(10, bean.samples.size());
		assertEquals(TWENTYFOUR_HOURS, ((EnergyDataBean) bean.samples.get(0)).timestamp);
		assertEquals(TWENTYFOUR_HOURS + 24 * 3600000, ((EnergyDataBean) bean.samples.get(1)).timestamp);
		assertEquals(TEN_DAYS, ((EnergyDataBean) bean.samples.get(9)).timestamp);
		assertNotNull(bean.summary);
		assertEquals(10*DEVICE_KWH/1000, bean.summary.kwh, 0.000001d);
	}
	
	@Test
	public void testGetDailyStatsOneDeviceAlmostTwentyFourHours() {
		try {
			setupForDailyTests();
		} catch (SensorFailedException e) {}
		
		Response response = target("/energy/" + METER + "/daily").request().get();
		assertEquals(400, response.getStatus());
	}
}
