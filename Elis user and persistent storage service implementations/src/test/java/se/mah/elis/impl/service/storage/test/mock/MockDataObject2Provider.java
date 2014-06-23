package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.exceptions.DataInitalizationException;
import se.mah.elis.services.storage.factory.DataObjectProvider;
import se.mah.elis.services.storage.factory.DataObjectRecipe;

public class MockDataObject2Provider implements DataObjectProvider {

	public MockDataObject2Provider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ElisDataObject build(Properties properties)
			throws DataInitalizationException {
		ElisDataObject edo = new MockDataObject2();
		
		try {
			edo.populate(properties);
		} catch (Exception e) {
			throw new DataInitalizationException("baz must be a float");
		}
		
		return edo;
	}

	@Override
	public DataObjectRecipe getRecipe() {
		return new MockDataObject2Recipe();
	}

}
