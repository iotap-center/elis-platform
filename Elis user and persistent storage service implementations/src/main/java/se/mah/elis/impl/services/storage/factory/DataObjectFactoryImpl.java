package se.mah.elis.impl.services.storage.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.exceptions.DataInitalizationException;
import se.mah.elis.services.storage.factory.DataObjectFactory;
import se.mah.elis.services.storage.factory.DataObjectProvider;
import se.mah.elis.services.storage.factory.DataObjectRecipe;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;

@Component(name = "Elis Data object factory")
@Service(value = DataObjectFactory.class)
public class DataObjectFactoryImpl implements DataObjectFactory {
	
	@Reference
	private LogService log;

	private Map<String, Map<String, DataObjectProvider>> providers;
	
	public DataObjectFactoryImpl() {
		providers = new HashMap<String, Map<String, DataObjectProvider>>();
	}

	@Override
	public void registerProvider(DataObjectProvider provider) {
		log(LogService.LOG_INFO, "Registering " + provider.getClass().getSimpleName());
		Map<String, DataObjectProvider> map =
				providers.get(provider.getRecipe().getDataType());
		
		if (map == null) {
			map = new HashMap<String, DataObjectProvider>();
			
			map.put(provider.getRecipe().getServiceName(), provider);
			providers.put(provider.getRecipe().getDataType(), map);
		} else {
			DataObjectProvider p = map.get(provider.getRecipe().getServiceName());
			if (p == null) {
				map.put(provider.getRecipe().getServiceName(), provider);
			}
		}
	}

	@Override
	public void unregisterProvider(DataObjectProvider provider) {
		log(LogService.LOG_INFO, "Unregistering " + provider.getClass().getSimpleName());
		Map<String, DataObjectProvider> map =
				providers.get(provider.getRecipe().getDataType());
		
		if (map != null) {
			if (map.size() == 1) {
				providers.remove(provider.getRecipe().getDataType());
			} else {
				map.remove(provider.getRecipe().getServiceName());
			}
		}
	}

	@Override
	public ElisDataObject build(String dataType, String serviceName,
			Properties properties) throws DataInitalizationException {
		log(LogService.LOG_INFO, "Attempting to build a " + dataType + " using " + serviceName);
		
		DataObjectProvider provider = null;
		ElisDataObject edo = null;
		Map<String, DataObjectProvider> map =
				providers.get(dataType);
		
		if (map == null) {
			throw new DataInitalizationException("No such data object provider");
		}
		
		provider = map.get(serviceName);
		
		if (provider == null) {
			log(LogService.LOG_ERROR, "Didn't find the " + serviceName + " service");
			throw new DataInitalizationException("No such data object provider");
		}
		
		try {
			edo = provider.build(properties);
		} catch (Exception e) {
			log(LogService.LOG_ERROR, "Couldn't build a data object using the " + serviceName + " service.");
			throw new DataInitalizationException(serviceName, "Probably malformed properties");
		}
		
		return edo;
	}

	@Override
	public DataObjectRecipe[] getAvailableDataRecipes() {
		ArrayList<DataObjectRecipe> recipes = new ArrayList<DataObjectRecipe>();
		
		for (Map.Entry<String, Map<String, DataObjectProvider>> entry
				: providers.entrySet()) {
			Map<String, DataObjectProvider> map = entry.getValue();
			for (Map.Entry<String, DataObjectProvider> entry2: map.entrySet()) {
				recipes.add(entry2.getValue().getRecipe());
			}
		}
		
		return recipes.toArray(new DataObjectRecipe[0]);
	}

	@Override
	public DataObjectRecipe getRecipe(String dataType, String systemName) {
		DataObjectRecipe recipe = null;
		Map<String, DataObjectProvider> types = providers.get(dataType);
		
		if (types != null) {
			try {
				recipe = ((DataObjectProvider) types.get(systemName)).getRecipe();
			} catch (NullPointerException e) {}
		}

		return recipe;
	}
	
	protected void bindLog(LogService log) {
		this.log = log;
	}
	
	protected void unbindLog(LogService log) {
		this.log = null;
	}
	
	private void log(int level, String message, Throwable t) {
		if (log != null) {
			log.log(level, "StorageImpl: " + message, t);
		}
	}
	
	private void log(int level, String message) {
		if (log != null) {
			log.log(level, "StorageImpl: " + message);
		}
	}

}
