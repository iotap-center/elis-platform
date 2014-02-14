package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.OrderedProperties;

public class MockDataObject2 implements ElisDataObject {

	private UUID uuid;
	private int userid;
	private float baz;
	
	public MockDataObject2() {
		uuid = null;
		userid = 0;
		baz = 0;
	}
	
	public MockDataObject2(UUID uuid, int userid, float baz) {
		this.uuid = uuid;
		this.userid = userid;
		this.baz = baz;
	}
	
	public MockDataObject2(int userid, float baz) {
		this.uuid = null;
		this.userid = userid;
		this.baz = baz;
	}
	
	public MockDataObject2(float baz) {
		this.uuid = null;
		this.userid = 0;
		this.baz = baz;
	}
	
	@Override
	public long getDataId() {
		return 0;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void setUniqueUserId(int userId) {
		this.userid = userId;
	}

	@Override
	public int getUniqueUserId() {
		return userid;
	}

	@Override
	public Properties getProperties() {
		Properties props = new OrderedProperties();
		
		if (uuid != null) {
			props.put("uuid", uuid);
		}
		props.put("userid", userid);
		props.put("baz", baz);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();

		props.put("uuid", UUID.randomUUID());
		props.put("userid", 0);
		props.put("baz", 0.0);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		uuid = (UUID) props.get("uuid");
		userid = (int) props.get("userid");
		baz = (float) props.get("baz");
	}

	public void setBaz(float baz) {
		this.baz = baz;
	}
	
	public float getBaz() {
		return baz;
	}
}
