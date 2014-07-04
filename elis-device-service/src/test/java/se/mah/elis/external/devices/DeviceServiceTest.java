package se.mah.elis.external.devices;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.service.log.LogService;

import com.google.gson.Gson;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.external.beans.EnvelopeBean;
import se.mah.elis.external.devices.beans.DeviceBean;
import se.mah.elis.external.devices.beans.DeviceSetBean;
import se.mah.elis.services.storage.Storage;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

public class DeviceServiceTest extends JerseyTest {

	private static final String PUID = "00001111-2222-3333-4444-555566667777";
	private static final String DID = "10001111-2222-3333-4444-555566667777";
	private static final String DSID = "20001111-2222-3333-4444-555566667777";
	private static final String DEVICENAME = "deviceName";
	private static final String DEVICEDESCRIPTION = "deviceDescription";
	private static UserService userService;
	private static Storage storage;
	private static PlatformUser platformUser;
	private static GatewayUser gatewayUser;
	private static Device device;
	private static DeviceSet deviceset;
	private static List<Device> devices = new ArrayList<Device>();
	private Gson gson;

	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new DeviceService(userService, storage, mock(LogService.class)));
		return config;
	}
	
	@BeforeClass
	public static void setupClass() {
		userService = mock(UserService.class);
		platformUser = mock(PlatformUser.class);
		gatewayUser = mock(GatewayUser.class);
		storage = mock(Storage.class);
		device = mock(Device.class);
		deviceset = mock(DeviceSet.class);
		when(platformUser.getUserId()).thenReturn(UUID.fromString(PUID));
		when(device.getDataId()).thenReturn(UUID.fromString(DID));
	
		when(userService.getPlatformUser(any(UUID.class))).thenReturn(platformUser);
		when(userService.getUsers(any(PlatformUser.class))).thenReturn(new User[] { gatewayUser });
		
		try {
			when(storage.readData(UUID.fromString(DID))).thenReturn(device);
			when(storage.readData(UUID.fromString(DSID))).thenReturn(deviceset);
			when(storage.readData(UUID.fromString(PUID))).thenThrow(new StorageException());
			when(storage.readUser(UUID.fromString(DID))).thenThrow(new StorageException());
			when(storage.readUser(UUID.fromString(DSID))).thenThrow(new StorageException());
			when(storage.readUser(UUID.fromString(PUID))).thenReturn(platformUser);
		} catch (StorageException e) {}
		
		when(device.getDataId()).thenReturn(UUID.fromString(DID));
		when(device.getName()).thenReturn(DEVICENAME);
		when(device.getDescription()).thenReturn(DEVICEDESCRIPTION);
		
		devices.add(device);
		
		when(deviceset.iterator()).thenReturn(devices.iterator());
		when(deviceset.getOwnerId()).thenReturn(UUID.fromString(PUID));
	}
	
	@Before
	public void setup() {
		gson = new Gson();
		Gateway gw = mock(Gateway.class);
		gw.addAll(devices);
		when(gw.iterator()).thenReturn(devices.iterator());
		when(gatewayUser.getGateway()).thenReturn(gw);
	}
	
	@Test
	public void testGetDevices() {
		final String deviceResponse = target("/devices/" + PUID + "/").request().get(String.class);
		EnvelopeBean envelope = gson.fromJson(deviceResponse, EnvelopeBean.class);
		DeviceSetBean bean = gson.fromJson(gson.toJson(envelope.response), DeviceSetBean.class);
		assertEquals(PUID, bean.puid);
		assertEquals(1, bean.devices.size());
		assertEquals(DEVICENAME, bean.devices.get(0).name);
	}
	
	@Test
	public void testBadRequest() {
		final Response response = target("/devices/" + "WRONG!" + "/").request().get();
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testGetDevicesWithADevice() {
		final String deviceResponse = target("/devices/" + DID + "/").request().get(String.class);
		EnvelopeBean envelope = gson.fromJson(deviceResponse, EnvelopeBean.class);
		DeviceBean bean = gson.fromJson(gson.toJson(envelope.response), DeviceBean.class);
		assertEquals(DID, bean.id);
		assertEquals(DEVICEDESCRIPTION, bean.description);
		assertEquals(DEVICENAME, bean.name);
	}
	
	@Test
	public void testGetDevicesWithADeviceset() {
		final String deviceResponse = target("/devices/" + DSID + "/").request().get(String.class);
		EnvelopeBean envelope = gson.fromJson(deviceResponse, EnvelopeBean.class);
		DeviceSetBean bean = gson.fromJson(gson.toJson(envelope.response), DeviceSetBean.class);
		assertEquals(PUID, bean.puid);
		assertEquals(1, bean.devices.size());
		assertEquals(DEVICENAME, bean.devices.get(0).name);
	}
}
