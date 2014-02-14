package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;
import java.util.UUID;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.OrderedProperties;

public class MockDataObject1 implements ElisDataObject {

	private UUID uuid;
	private int userid;
	private int foo;
	private String bar;
	
	public MockDataObject1() {
		uuid = null;
		userid = 0;
		foo = 0;
		bar = "bar";
	}
	
	public MockDataObject1(UUID uuid, int foo, String bar) {
		this.uuid = uuid;
		this.foo = foo;
		this.bar = bar;
		this.userid = 0;
	}
	
	public MockDataObject1(int foo, String bar) {
		this.uuid = null;
		this.foo = foo;
		this.bar = bar;
		this.userid = 0;
	}
	
	public MockDataObject1(UUID uuid, int userid, int foo, String bar) {
		this.uuid = uuid;
		this.userid = userid;
		this.foo = foo;
		this.bar = bar;
	}
	
	public MockDataObject1(int userid, int foo, String bar) {
		this.uuid = null;
		this.userid = userid;
		this.foo = foo;
		this.bar = bar;
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
		
		if (uuid !=  null) {
			props.put("uuid", uuid);
		}
		props.put("userid", userid);
		props.put("foo", foo);
		if (bar != null) {
			props.put("bar", bar);
		}
		
		return props;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		OrderedProperties props = new OrderedProperties();
		
		props.put("uuid", UUID.randomUUID());
		props.put("userid", 0);
		props.put("foo", 0);
		props.put("bar", "16");
		
		return props;
	}

	@Override
	public void populate(Properties props) {
		uuid = (UUID) props.get("uuid");
		userid = (int) props.get("userid");
		foo = (int) props.get("foo");
		bar = (String) props.get("bar");
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

}
