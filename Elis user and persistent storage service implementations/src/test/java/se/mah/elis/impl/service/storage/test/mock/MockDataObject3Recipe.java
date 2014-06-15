package se.mah.elis.impl.service.storage.test.mock;

import java.util.ArrayList;
import java.util.Properties;

import se.mah.elis.services.storage.factory.DataObjectRecipe;

public class MockDataObject3Recipe implements DataObjectRecipe {

	public MockDataObject3Recipe() {
	}

	@Override
	public String getDataType() {
		return "se.mah.elis.impl.service.storage.test.mock.MockDataObject3";
	}

	@Override
	public Properties getProperties() {
		Properties p = new Properties();
		
		p.put("baz", 0f);
		p.put("mdos", new ArrayList<MockDataObject2>());
		
		return p;
	}

}
