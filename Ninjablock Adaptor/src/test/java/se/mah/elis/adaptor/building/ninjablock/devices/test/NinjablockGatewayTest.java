package se.mah.elis.adaptor.building.ninjablock.devices.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import se.mah.elis.adaptor.building.api.exceptions.GatewayCommunicationException;
import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;
import se.mah.elis.adaptor.building.ninjablock.NinjaBuiltInThermometer;
import se.mah.elis.adaptor.building.ninjablock.NinjablockGateway;

public class NinjablockGatewayTest {

	private NinjablockGateway gateway;
	private static final int TEST_GATEWAY_ID = 1234;
	private static final String TEST_GATEWAY_NAME = "Ninja Block";
	
	public NinjablockGatewayTest() throws StaticEntityException{
		// New gateway
		gateway = new NinjablockGateway();
		gateway.setId(TEST_GATEWAY_ID);

		// New Device
		NinjaBuiltInThermometer ninjaThermDevice = new NinjaBuiltInThermometer();
		
		// Add Device to gateway
		gateway.add(ninjaThermDevice);
		
	}
	
	
	
	@Test
	public void testConnect() throws StaticEntityException, GatewayCommunicationException {
			
		gateway.connect();
		assertEquals(TEST_GATEWAY_NAME, gateway.getName());
		assertEquals(TEST_GATEWAY_ID, gateway.getId());
		assertTrue(gateway.size() > 0);
		assertTrue(gateway.hasConnected());


	}
	
}
