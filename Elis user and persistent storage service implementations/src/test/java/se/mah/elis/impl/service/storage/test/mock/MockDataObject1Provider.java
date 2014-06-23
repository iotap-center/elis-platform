package se.mah.elis.impl.service.storage.test.mock;

import java.util.Properties;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.exceptions.DataInitalizationException;
import se.mah.elis.impl.service.storage.test.mock.MockDataObject1;
import se.mah.elis.services.storage.factory.DataObjectProvider;
import se.mah.elis.services.storage.factory.DataObjectRecipe;

public class MockDataObject1Provider implements DataObjectProvider {

	public MockDataObject1Provider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ElisDataObject build(Properties properties)
			throws DataInitalizationException {
		ElisDataObject mdo = new MockDataObject1();
		
		mdo.populate(properties);
		
		return mdo;
	}

	@Override
	public DataObjectRecipe getRecipe() {
		return new MockDataObject1Recipe();
	}

}
