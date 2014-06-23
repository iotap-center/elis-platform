package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.OrderedProperties;

public class MockDataObject1 implements ElisDataObject {

	private UUID id;
	private UUID ownerid;
	private int foo;
	private String bar;
	private DateTime created = DateTime.now();
	
	public MockDataObject1() {
		id = null;
		ownerid = null;
		foo = 0;
		bar = "bar";
	}
	
	public MockDataObject1(UUID dataid, int foo, String bar) {
		this.id = dataid;
		this.foo = foo;
		this.bar = bar;
		this.ownerid = null;
	}
	
	public MockDataObject1(int foo, String bar) {
		this.id = null;
		this.foo = foo;
		this.bar = bar;
		this.ownerid = null;
	}
	
	public MockDataObject1(UUID id, UUID ownerid, int foo, String bar) {
		this.id = id;
		this.ownerid = ownerid;
		this.foo = foo;
		this.bar = bar;
	}

	@Override
	public UUID getDataId() {
		return id;
	}

	@Override
	public void setDataId(UUID dataid) {
		this.id = dataid;
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
		
		if (id !=  null) {
			props.put("dataid", id);
		}
		if (ownerid != null) {
			props.put("ownerid", ownerid);
		}
		props.put("foo", foo);
		if (bar != null) {
			props.put("bar", bar);
		}
		props.put("created", created);
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("dataid", UUID.randomUUID());
		props.put("ownerid", UUID.randomUUID());
		props.put("foo", 0);
		props.put("bar", "16");
		props.put("created", created);
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		id = (UUID) props.get("dataid");
		ownerid = (UUID) props.get("ownerid");
		foo = (int) props.get("foo");
		bar = (String) props.get("bar");
		created = (DateTime) props.get("created");
	}
	
	public void setFoo(int foo) {
		this.foo = foo;
	}
	
	public int getFoo() {
		return foo;
	}
	
	public void setBar(String bar) {
		this.bar = bar;
	}
	
	public String getBar() {
		return bar;
	}
	
	public void setCreated(DateTime dt) {
		created = dt;
	}

	@Override
	public DateTime created() {
		return created;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MockDataObject1) {
			MockDataObject1 mdo = (MockDataObject1) o;
			if (id != null && mdo.getDataId() != null) {
				return id.equals(mdo.getDataId());
			}
		}
		
		return false;
	}

}
