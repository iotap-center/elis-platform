package se.mah.elis.external.control;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.PowerSwitch;
import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;
import se.mah.elis.external.beans.EnvelopeBean;
import se.mah.elis.external.beans.helpers.DateTimeAdapter;
import se.mah.elis.external.beans.helpers.GsonProvider;
import se.mah.elis.external.control.beans.PowerstateBean;
import se.mah.elis.external.control.internal.EndpointUtils;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

import com.google.gson.Gson;

public class PowerstateServiceTest extends JerseyTest {

	private static final UUID PUID = UUID.fromString("00001111-2222-3333-4444-555566667777");
	private static final UUID BAD_PUID = UUID.fromString("01001111-2222-3333-4444-555566667777");
	private static final UUID DID = UUID.fromString("10001111-2222-3333-4444-555566667777");
	private static final UUID BAD_DID = UUID.fromString("11001111-2222-3333-4444-555566667777");
	private static final UUID OFFLINE_DID = UUID.fromString("12001111-2222-3333-4444-555566667777");
	private static final UUID DSID = UUID.fromString("20001111-2222-3333-4444-555566667777");
	private static final UUID BAD_DSID = UUID.fromString("21001111-2222-3333-4444-555566667777");
	private static final UUID BOGUS_ID = UUID.fromString("deadbeef-2222-3333-4444-555566667777");
	private static final String DEVICENAME = "deviceName";
	private static final String DEVICEDESCRIPTION = "deviceDescription";
	private static UserService userService;
	private static EndpointUtils utils;
	private static Storage storage;
	private static LogService log;
	private static PlatformUser platformUser;
	private static PlatformUser badPlatformUser;
	private static GatewayUser gatewayUser;
	private static Device badDevice;
	private static PowerSwitch device;
	private static PowerSwitch offlineDevice;
	private static DeviceSet deviceset;
	private static DeviceSet badDeviceset;
	private static List<Device> devices = new ArrayList<Device>();
	private static List<Device> badDevices = new ArrayList<Device>();
	private Gson gson = new Gson();

	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new PowerstateService(userService, storage, log, utils));
		config.register(new GsonProvider<HashSet>());
		return config;
	}
	
	@BeforeClass
	public static void setupClass() {
		userService = mock(UserService.class);
		utils = new EndpointUtils(userService);
		storage = mock(Storage.class);
		log = mock(LogService.class);
		platformUser = mock(PlatformUser.class);
		badPlatformUser = mock(PlatformUser.class);
		gatewayUser = mock(GatewayUser.class);
		device = mock(PowerSwitch.class);
		badDevice = mock(Device.class);
		offlineDevice = mock(PowerSwitch.class);
		deviceset = mock(DeviceSet.class);
		badDeviceset = mock(DeviceSet.class);
		when(platformUser.getUserId()).thenReturn(PUID);
		when(badPlatformUser.getUserId()).thenReturn(BAD_PUID);
		when(device.getDataId()).thenReturn(DID);
		when(badDevice.getDataId()).thenReturn(BAD_DID);
		when(offlineDevice.getDataId()).thenReturn(OFFLINE_DID);
	
		when(userService.getPlatformUser(PUID)).thenReturn(platformUser);
		when(userService.getPlatformUser(BAD_PUID)).thenReturn(badPlatformUser);
		when(userService.getUsers(any(PlatformUser.class))).thenReturn(new User[] { gatewayUser });
		
		try {
			when(storage.readData(DID)).thenReturn(device);
			when(storage.readData(BAD_DID)).thenReturn(badDevice);
			when(storage.readData(OFFLINE_DID)).thenReturn(offlineDevice);
			when(storage.readData(DSID)).thenReturn(deviceset);
			when(storage.readData(BAD_DSID)).thenReturn(badDeviceset);
			when(storage.readData(PUID)).thenThrow(new StorageException());
			when(storage.readData(BOGUS_ID)).thenThrow(new StorageException());
			when(storage.readUser(DID)).thenThrow(new StorageException());
			when(storage.readUser(DSID)).thenThrow(new StorageException());
			when(storage.readUser(PUID)).thenReturn(platformUser);
			when(storage.readUser(BAD_PUID)).thenReturn(badPlatformUser);
		} catch (StorageException e) {}

		when(storage.objectExists(DID)).thenReturn(true);
		when(storage.objectExists(BAD_DID)).thenReturn(true);
		when(storage.objectExists(OFFLINE_DID)).thenReturn(true);
		when(storage.objectExists(DSID)).thenReturn(true);
		when(storage.objectExists(BAD_DSID)).thenReturn(true);
		when(storage.objectExists(PUID)).thenReturn(true);
		when(storage.objectExists(BAD_PUID)).thenReturn(true);
		when(storage.objectExists(BOGUS_ID)).thenReturn(false);
		
		when(device.getDataId()).thenReturn(DID);
		when(device.getName()).thenReturn(DEVICENAME);
		when(device.getDescription()).thenReturn(DEVICEDESCRIPTION);
		try {
			when(device.isTurnedOn()).thenReturn(true);
		} catch (ActuatorFailedException e) {}
		
		when(badDevice.getDataId()).thenReturn(BAD_DID);
		when(badDevice.getName()).thenReturn("Bad " + DEVICENAME);
		when(badDevice.getDescription()).thenReturn("Bad " + DEVICEDESCRIPTION);
		
		when(offlineDevice.getDataId()).thenReturn(OFFLINE_DID);
		when(offlineDevice.getName()).thenReturn(DEVICENAME);
		when(offlineDevice.getDescription()).thenReturn(DEVICEDESCRIPTION);
		try {
			when(offlineDevice.isTurnedOn()).thenThrow(new ActuatorFailedException());
		} catch (ActuatorFailedException e) {}
		
		devices.add(device);
		devices.add(badDevice);
		
		badDevices.add(badDevice);
		
		when(deviceset.iterator()).thenReturn(devices.iterator());
		when(deviceset.getOwnerId()).thenReturn(PUID);
		
		when(badDeviceset.iterator()).thenReturn(badDevices.iterator());
		when(badDeviceset.getOwnerId()).thenReturn(PUID);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		Gateway gw = mock(Gateway.class);
		gw.addAll(devices);
		when(gw.iterator()).thenReturn(devices.iterator());
		when(gatewayUser.getGateway()).thenReturn(gw);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testBadRequest() {
		final Response response = target("/powerstate/" + "WRONG!").request().get();
		assertEquals(400, response.getStatus());
	}

	@Test
	public void testGetPowerstate() {
		final String deviceResponse = target("/powerstate/" + DID).request().get(String.class);
		EnvelopeBean envelope = gson.fromJson(deviceResponse, EnvelopeBean.class);
		PowerstateBean bean = gson.fromJson(gson.toJson(envelope.response), PowerstateBean.class);
		assertEquals(DID, bean.device);
		assertTrue(bean.powerstate);
	}

	@Test
	public void testGetPowerstateDeviceOffline() {
		final Response response = target("/powerstate/" + OFFLINE_DID).request().get();
		assertEquals(503, response.getStatus());
	}
	
	@Test
	public void testGetPowerstateNotADevice() {
		final Response response = target("/powerstate/" + PUID).request().get();
		assertEquals(405, response.getStatus());
	}
	
	@Test
	public void testGetPowerstateNotAnApplicableDevice() {
		final Response response = target("/powerstate/" + BAD_DID).request().get();
		assertEquals(405, response.getStatus());
	}
	
	@Test
	public void testGetPowerstateNoSuchObject() {
		final Response response = target("/powerstate/" + BOGUS_ID).request().get();
		assertEquals(404, response.getStatus());
	}

	@Test
	public void testSetPowerstateOperationFailed() {
		final Entity<String> requestBody = Entity.entity("{\"powerstate\": true}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/powerstate/" + OFFLINE_DID).request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(503, response.getStatus());
	}

	@Test
	public void testSetPowerstateApplicableDevice() {
		final Entity<String> requestBody = Entity.entity("{\"powerstate\": true}", MediaType.APPLICATION_JSON_TYPE);
		final String response = target("/powerstate/" + DID).request(MediaType.APPLICATION_JSON_TYPE).put(requestBody, String.class);
		
		EnvelopeBean envelope = gson.fromJson(response, EnvelopeBean.class);
		PowerstateBean bean = gson.fromJson(gson.toJson(envelope.response), PowerstateBean.class);
		assertEquals(DID, bean.device);
		assertTrue(bean.powerstate);
	}

	@Test
	public void testSetPowerstateNonApplicableDevice() {
		final Entity<String> requestBody = Entity.entity("{\"powerstate\": true}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/powerstate/" + BAD_DID).request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(405, response.getStatus());
	}

	@Test
	public void testSetPowerstateUserWithOKDevice() {
		final Entity<String> requestBody = Entity.entity("{\"powerstate\": true}", MediaType.APPLICATION_JSON_TYPE);
		final String response = target("/powerstate/" + PUID).request(MediaType.APPLICATION_JSON_TYPE).put(requestBody, String.class);
		
		EnvelopeBean envelope = gson.fromJson(response, EnvelopeBean.class);
		PowerstateBean bean = gson.fromJson(gson.toJson(envelope.response), PowerstateBean.class);
		assertEquals(PUID, bean.user);
		assertTrue(bean.powerstate);
	}

	@Test
	public void testSetPowerstateUserWithNoOKDevices() {
		final Entity<String> requestBody = Entity.entity("{\"powerstate\": true}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/powerstate/" + BAD_PUID).request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(405, response.getStatus());
	}

	@Test
	public void testSetPowerstateDeviceSetWithOKDevice() {
		final Entity<String> requestBody = Entity.entity("{\"powerstate\": true}", MediaType.APPLICATION_JSON_TYPE);
		final String response = target("/powerstate/" + DSID).request(MediaType.APPLICATION_JSON_TYPE).put(requestBody, String.class);
		
		EnvelopeBean envelope = gson.fromJson(response, EnvelopeBean.class);
		PowerstateBean bean = gson.fromJson(gson.toJson(envelope.response), PowerstateBean.class);
		assertEquals(DSID, bean.deviceset);
		assertTrue(bean.powerstate);
	}

	@Test
	public void testSetPowerstateDevicesetWithNoOKDevices() {
		final Entity<String> requestBody = Entity.entity("{\"powerstate\": true}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/powerstate/" + BAD_DSID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(405, response.getStatus());
	}

}
