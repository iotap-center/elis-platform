package se.mah.elis.adaptor.building.ninjablock.devices.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.adaptor.building.ninjablock.beans.DataBean;
import se.mah.elis.adaptor.building.ninjablock.internal.NinjaDeviceIdentifier;

public class NinjaDeviceIdentifierTest {
	
	private NinjaDeviceIdentifier ninjaDevId;
	private DataBean bean;

	public NinjaDeviceIdentifierTest() {
	}
	
	@Before
	public void setUp() {
		
		// Mockup humidity data created:
		bean = new DataBean();
		bean.setNode("4412BB000319");
		bean.setDid(30);
		bean.setVid(0);
		bean.setGid("0101");
		
		ninjaDevId = new NinjaDeviceIdentifier(bean);
	}
	
	@Test
	public void testNinjaDeviceIdentifier() {
		String expected = "0101_0_30";
		assertEquals(expected,ninjaDevId.toSemiGUID());
	}
	
	@Test
	public void testConsumeBean() {
		ninjaDevId.consumeBean(bean);
		assertEquals(30, ninjaDevId.getDid());
		assertEquals(0, ninjaDevId.getVid());
		assertEquals("0101", ninjaDevId.getGid());
		assertEquals("4412BB000319", ninjaDevId.getNode());
	}
	
	@Test
	public void testConsumeBeanNegativeDid() {
		bean.setDid(-42);
		ninjaDevId.consumeBean(bean);
		assertEquals(0, ninjaDevId.getDid());
	}
	
	@Test
	public void testConsumeBeanNegativeVid() {
		bean.setVid(-42);
		ninjaDevId.consumeBean(bean);
		assertEquals(0, ninjaDevId.getVid());
	}
	
	@Test
	public void testConsumeBeanNullGid() {
		bean.setGid(null);
		ninjaDevId.consumeBean(bean);
		assertNotNull(ninjaDevId.getGid());
	}
	
	@Test
	public void testConsumeBeanNullNode() {
		bean.setNode(null);
		ninjaDevId.consumeBean(bean);
		assertNotNull(ninjaDevId.getNode());
	}
	
	@Test
	public void testConsumeBeanToGUID()	{
		assertEquals("4412BB000319_0101_0_30", ninjaDevId.toGUID());
	}


}
