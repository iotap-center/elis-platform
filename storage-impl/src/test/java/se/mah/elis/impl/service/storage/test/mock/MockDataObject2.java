package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.OrderedProperties;

public class MockDataObject2 implements ElisDataObject {

	private UUID id;
	private UUID ownerid;
	private float baz;
	
	public MockDataObject2() {
		id = null;
		ownerid = null;
		baz = 0;
	}
	
	public MockDataObject2(UUID id, UUID ownerid, float baz) {
		this.id = id;
		this.ownerid = ownerid;
		this.baz = baz;
	}
	
	public MockDataObject2(UUID ownerid, float baz) {
		this.id = null;
		this.ownerid = ownerid;
		this.baz = baz;
	}
	
	public MockDataObject2(float baz) {
		this.id = null;
		this.ownerid = null;
		this.baz = baz;
	}

	@Override
	public UUID getDataId() {
		return id;
	}

	@Override
	public void setDataId(UUID uuid) {
		this.id = uuid;
	}

	@Override
	public void setOwnerId(UUID userId) {
		this.ownerid = userId;
	}

	@Override
	public UUID getOwnerId() {
		return ownerid;
	}

	@Override
	public Properties getProperties() {
		Properties props = new OrderedProperties();
		
		if (id != null) {
			props.put("dataid", id);
		}
		if (ownerid != null) {
			props.put("ownerid", ownerid);
		}
		props.put("baz", baz);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();

		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("baz", 0.0);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		id = (UUID) props.get("dataid");
		ownerid = (UUID) props.get("ownerid");
		baz = (float) props.get("baz");
	}

	public void setBaz(float baz) {
		this.baz = baz;
	}
	
	public float getBaz() {
		return baz;
	}
}
