package se.mah.elis.adaptor.building.ninjablock.devices.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.mah.elis.adaptor.building.ninjablock.NinjablockGatewayUserIdentifer;

public class NinjablockGatewayUserIdentiferTest {

	private NinjablockGatewayUserIdentifer ninjaGwIdent;
	
	@Test
	public void testGetId () {
		ninjaGwIdent = new NinjablockGatewayUserIdentifer();
		assertEquals("2124d350-74ed-11e2-bfe5-22000a9d2c4c",ninjaGwIdent.getUserId());
	}
	
	@Test
	public void testGetName() {
		ninjaGwIdent = new NinjablockGatewayUserIdentifer();
		assertEquals("Marcus Ljungblad",ninjaGwIdent.getUserName());
	}
	
	@Test
	public void testGetEmail() {
		ninjaGwIdent = new NinjablockGatewayUserIdentifer();
		assertEquals("marcus.ljungblad@mah.se",ninjaGwIdent.getUserEmail());
	}

}
