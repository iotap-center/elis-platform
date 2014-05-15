package se.mah.elis.impl.service.storage.test.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.OrderedProperties;

public class MockDataObject3 implements ElisDataObject {

	private UUID id;
	private UUID ownerid;
	private float baz;
	private Collection<MockDataObject2> mdos;
	private DateTime created = DateTime.now();
	
	public MockDataObject3() {
		id = null;
		ownerid = null;
		baz = 0;
		mdos = new ArrayList<MockDataObject2>();
	}
	
	public MockDataObject3(UUID id, UUID ownerid, float baz) {
		this.id = id;
		this.ownerid = ownerid;
		this.baz = baz;
		mdos = new ArrayList<MockDataObject2>();
	}
	
	public MockDataObject3(UUID ownerid, float baz) {
		this.id = null;
		this.ownerid = ownerid;
		this.baz = baz;
		mdos = new ArrayList<MockDataObject2>();
	}
	
	public MockDataObject3(float baz) {
		this.id = null;
		this.ownerid = null;
		this.baz = baz;
		mdos = new ArrayList<MockDataObject2>();
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
		props.put("created", created);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();

		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("baz", 0.0);
		props.put("created", created);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		id = (UUID) props.get("dataid");
		ownerid = (UUID) props.get("ownerid");
		baz = (float) props.get("baz");
		created = (DateTime) props.get("created");
	}

	public void setBaz(float baz) {
		this.baz = baz;
	}
	
	public float getBaz() {
		return baz;
	}
	
	public Collection<MockDataObject2> getCollection() {
		return mdos;
	}
	
	public void setCreated(DateTime dt) {
		created = dt;
	}

	@Override
	public DateTime created() {
		return created;
	}
}
