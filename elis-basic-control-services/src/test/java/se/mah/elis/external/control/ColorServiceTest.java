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
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.ColoredLamp;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Dimmer;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;
import se.mah.elis.external.beans.EnvelopeBean;
import se.mah.elis.external.control.beans.ColorBean;
import se.mah.elis.external.control.beans.DimlevelBean;
import se.mah.elis.external.control.internal.EndpointUtils;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

import com.google.gson.Gson;

public class ColorServiceTest extends JerseyTest {

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
	private static final int COLOR = 42;
	private static final String COLOR_IN_HEX = "#00002A";
	private static UserService userService;
	private static EndpointUtils utils;
	private static Storage storage;
	private static PlatformUser platformUser;
	private static PlatformUser badPlatformUser;
	private static GatewayUser gatewayUser;
	private static Device badDevice;
	private static ColoredLamp device;
	private static ColoredLamp offlineDevice;
	private static DeviceSet deviceset;
	private static DeviceSet badDeviceset;
	private static List<Device> devices = new ArrayList<Device>();
	private static List<Device> badDevices = new ArrayList<Device>();
	private Gson gson;

	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new ColorService(userService, storage, utils));
		return config;
	}

	@BeforeClass
	public static void setupClass() {
		userService = mock(UserService.class);
		utils = new EndpointUtils(userService);
		platformUser = mock(PlatformUser.class);
		badPlatformUser = mock(PlatformUser.class);
		gatewayUser = mock(GatewayUser.class);
		storage = mock(Storage.class);
		device = mock(ColoredLamp.class);
		badDevice = mock(Device.class);
		offlineDevice = mock(ColoredLamp.class);
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
		
		when(device.getDataId()).thenReturn(DID);
		when(device.getName()).thenReturn(DEVICENAME);
		when(device.getDescription()).thenReturn(DEVICEDESCRIPTION);

		when(storage.objectExists(DID)).thenReturn(true);
		when(storage.objectExists(BAD_DID)).thenReturn(true);
		when(storage.objectExists(OFFLINE_DID)).thenReturn(true);
		when(storage.objectExists(DSID)).thenReturn(true);
		when(storage.objectExists(BAD_DSID)).thenReturn(true);
		when(storage.objectExists(PUID)).thenReturn(true);
		when(storage.objectExists(BAD_PUID)).thenReturn(true);
		when(storage.objectExists(BOGUS_ID)).thenReturn(false);
		
		when(badDevice.getDataId()).thenReturn(BAD_DID);
		when(badDevice.getName()).thenReturn("Bad " + DEVICENAME);
		when(badDevice.getDescription()).thenReturn("Bad " + DEVICEDESCRIPTION);
		
		when(offlineDevice.getDataId()).thenReturn(OFFLINE_DID);
		when(offlineDevice.getName()).thenReturn("Offline " + DEVICENAME);
		when(offlineDevice.getDescription()).thenReturn("Offline " + DEVICEDESCRIPTION);
		try {
			when(offlineDevice.getColor()).thenThrow(new ActuatorFailedException());
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
		try {
			when(device.getColor()).thenReturn(42);
		} catch (ActuatorFailedException e1) {}
		gson = new Gson();
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
		final Response response = target("/color/" + "WRONG!" + "/").request().get();
		assertEquals(400, response.getStatus());
	}

	@Test
	public void testGetColor() {
		final String deviceResponse = target("/color/" + DID + "/").request().get(String.class);
		EnvelopeBean envelope = gson.fromJson(deviceResponse, EnvelopeBean.class);
		ColorBean bean = gson.fromJson(gson.toJson(envelope.response), ColorBean.class);
		assertEquals(DID, bean.device);
		assertEquals(COLOR_IN_HEX, bean.color);
	}

	@Test
	public void testGetColorDeviceOffline() {
		final Response response = target("/color/" + OFFLINE_DID + "/").request().get();
		assertEquals(503, response.getStatus());
	}

	@Test
	public void testGetColorNotAnApplicableDevice() {
		final Response response = target("/color/" + BAD_DID + "/").request().get();
		assertEquals(405, response.getStatus());
	}

	@Test
	public void testGetColorNoSuchObject() {
		final Response response = target("/color/" + BOGUS_ID + "/").request().get();
		assertEquals(404, response.getStatus());
	}

	@Test
	public void testSetColor() {
		try {
			when(device.getColor()).thenReturn(255);
		} catch (ActuatorFailedException e1) {}
		
		final Entity<String> requestBody = Entity.entity("{\"color\": \"#0000ff\"}", MediaType.APPLICATION_JSON_TYPE);
		final String response = target("/color/" + DID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody, String.class);
		
		EnvelopeBean envelope = gson.fromJson(response, EnvelopeBean.class);
		ColorBean bean = gson.fromJson(gson.toJson(envelope.response), ColorBean.class);
		assertEquals(DID, bean.device);
		assertEquals("#0000FF", bean.color);
	}

	@Test
	@Ignore
	public void testSetColorNoValue() {
		// TODO: Find a way to hijack the 500 process.
		final Entity<String> requestBody = Entity.entity("{\"color\": }", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/color/" + DID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(405, response.getStatus());
	}

	@Test
	@Ignore
	public void testSetColorEmptyValue() {
		// TODO: Find a way to hijack the 500 process.
		final Entity<String> requestBody = Entity.entity("{\"color\": \"\"}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/color/" + DID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(405, response.getStatus());
	}

	@Test
	@Ignore
	public void testSetColorBadColorValue() {
		// TODO: Find a way to hijack the 500 process.
		final Entity<String> requestBody = Entity.entity("{\"color\": \"#badcat\"}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/color/" + DID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(405, response.getStatus());
	}

	@Test
	@Ignore
	public void testSetColorValueOutOfRange() {
		// TODO: Find a way to hijack the 500 process.
		final Entity<String> requestBody = Entity.entity("{\"color\": \"#deadbeef\"}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/color/" + DID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(405, response.getStatus());
	}

	@Test
	public void testSetColorOperationFailed() {
		final Entity<String> requestBody = Entity.entity("{\"color\": \"#0000ff\"}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/color/" + OFFLINE_DID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(503, response.getStatus());
	}

	@Test
	public void testSetColorNonApplicableDevice() {
		final Entity<String> requestBody = Entity.entity("{\"color\": \"#0000ff\"}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/color/" + BAD_DID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(405, response.getStatus());
	}

	@Test
	public void testSetColorUserWithOKDevice() {
		final Entity<String> requestBody = Entity.entity("{\"color\": \"#0000ff\"}", MediaType.APPLICATION_JSON_TYPE);
		final String response = target("/color/" + PUID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody, String.class);
		
		EnvelopeBean envelope = gson.fromJson(response, EnvelopeBean.class);
		ColorBean bean = gson.fromJson(gson.toJson(envelope.response), ColorBean.class);
		assertEquals(PUID, bean.user);
		assertEquals("#0000FF", bean.color);
	}

	@Test
	public void testSetColorUserWithNoOKDevices() {
		final Entity<String> requestBody = Entity.entity("{\"color\": \"#0000ff\"}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/color/" + BAD_PUID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(405, response.getStatus());
	}

	@Test
	public void testSetColorDevicesetWithOKDevice() {
		final Entity<String> requestBody = Entity.entity("{\"color\": \"#0000ff\"}", MediaType.APPLICATION_JSON_TYPE);
		final String response = target("/color/" + DSID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody, String.class);
		
		EnvelopeBean envelope = gson.fromJson(response, EnvelopeBean.class);
		ColorBean bean = gson.fromJson(gson.toJson(envelope.response), ColorBean.class);
		assertEquals(DSID, bean.deviceset);
		assertEquals("#0000FF", bean.color);
	}

	@Test
	public void testSetColorDevicesetWithNoOKDevices() {
		final Entity<String> requestBody = Entity.entity("{\"color\": \"#0000ff\"}", MediaType.APPLICATION_JSON_TYPE);
		final Response response = target("/color/" + BAD_DSID + "/").request(MediaType.APPLICATION_JSON_TYPE).put(requestBody);
		
		assertEquals(405, response.getStatus());
	}

}
