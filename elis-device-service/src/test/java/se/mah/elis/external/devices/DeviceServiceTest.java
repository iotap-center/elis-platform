package se.mah.elis.external.devices;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Device;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.external.beans.EnvelopeBean;
import se.mah.elis.external.devices.beans.DeviceBean;
import se.mah.elis.external.devices.beans.DeviceSetBean;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.UserService;

public class DeviceServiceTest extends JerseyTest {

	private static final String PUID = "00001111-2222-3333-4444-555566667777";
	private static final String DEVICENAME = "deviceName";
	private static final String DEVICEDESCRIPTION = "deviceDescription";
	private static UserService userService;
	private static PlatformUser platformUser;
	private static GatewayUser gatewayUser;
	private Gson gson;

	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(new DeviceService(userService, mock(LogService.class)));
		return config;
	}
	
	@BeforeClass
	public static void setupClass() {
		userService = mock(UserService.class);
		platformUser = mock(PlatformUser.class);
		when(platformUser.getUserId()).thenReturn(UUID.fromString(PUID));

		gatewayUser = mock(GatewayUser.class);	
		userService = mock(UserService.class);
		when(userService.getPlatformUser(any(UUID.class))).thenReturn(platformUser);
		when(userService.getUsers(any(PlatformUser.class))).thenReturn(new User[] { gatewayUser });
	}
	
	@Before
	public void setup() {
		gson = new Gson();
		List<Device> devices = createDevices();
		Gateway gw = mock(Gateway.class);
		gw.addAll(devices);
		when(gw.iterator()).thenReturn(devices.iterator());
		when(gatewayUser.getGateway()).thenReturn(gw);
	}

	private List<Device> createDevices() {
		List<Device> devices = new ArrayList<Device>();
		
		Device device = mock(Device.class);
		DeviceIdentifier devId = mock(DeviceIdentifier.class);
		when(devId.toString()).thenReturn("ID:" + DEVICENAME);
		when(device.getId()).thenReturn(devId);
		when(device.getName()).thenReturn(DEVICENAME);
		when(device.getDescription()).thenReturn(DEVICEDESCRIPTION);
		devices.add(device);
		
		return devices;
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
	
}
