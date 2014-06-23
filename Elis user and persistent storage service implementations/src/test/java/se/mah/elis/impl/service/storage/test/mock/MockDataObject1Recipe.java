package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.services.storage.factory.DataObjectRecipe;

public class MockDataObject1Recipe implements DataObjectRecipe {

	public MockDataObject1Recipe() {
	}

	@Override
	public String getDataType() {
		return "se.mah.elis.impl.service.storage.test.mock.MockDataObject1";
	}

	@Override
	public Properties getProperties() {
		Properties p = new Properties();
		
		p.put("foo", 0);
		p.put("bar", 16);
		
		return p;
	}

}
