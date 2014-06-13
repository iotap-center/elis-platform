package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.services.storage.factory.DataObjectRecipe;

public class MockDataObject2Recipe implements DataObjectRecipe {

	public MockDataObject2Recipe() {
	}

	@Override
	public String getDataType() {
		return "MockDataObject2";
	}

	@Override
	public String getServiceName() {
		return "test";
	}

	@Override
	public Properties getProperties() {
		Properties p = new Properties();
		
		p.put("baz", 0f);
		
		return p;
	}

}
