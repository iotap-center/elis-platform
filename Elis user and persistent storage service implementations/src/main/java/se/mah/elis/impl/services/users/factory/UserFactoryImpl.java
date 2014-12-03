package se.mah.elis.impl.services.users.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.log.LogService;

import se.mah.elis.impl.services.users.PlatformUserImpl;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;

@Component(name = "Elis User factory")
@Service(value = UserFactory.class)
public class UserFactoryImpl implements UserFactory {
	
	@Reference
	private LogService log;

	private Map<String, Map<String, UserProvider>> providers;
	
	public UserFactoryImpl() {
		providers = new HashMap<String, Map<String, UserProvider>>();
	}

	@Override
	public void registerProvider(UserProvider provider) {
		log(LogService.LOG_INFO, "Registering " + provider.getClass().getSimpleName());
		Map<String, UserProvider> map =
				providers.get(provider.getRecipe().getUserType());
		
		if (map == null) {
			map = new HashMap<String, UserProvider>();
			
			map.put(provider.getRecipe().getServiceName(), provider);
			providers.put(provider.getRecipe().getUserType(), map);
		} else {
			UserProvider p = map.get(provider.getRecipe().getServiceName());
			if (p == null) {
				map.put(provider.getRecipe().getServiceName(), provider);
			}
		}
	}

	@Override
	public void unregisterProvider(UserProvider provider) {
		log(LogService.LOG_INFO, "Unregistering " + provider.getClass().getSimpleName());
		Map<String, UserProvider> map =
				providers.get(provider.getRecipe().getUserType());
		
		if (map != null) {
			if (map.size() == 1) {
				providers.remove(provider.getRecipe().getUserType());
			} else {
				map.remove(provider.getRecipe().getServiceName());
			}
		}
	}

	@Override
	public User build(String userType, String serviceName, Properties properties)
			throws UserInitalizationException {
		log(LogService.LOG_INFO, "Attempting to build a " + userType + " using " + serviceName);
		
		UserProvider provider = null;
		User user = null;
		Map<String, UserProvider> map =
				providers.get(userType);
		
		if (map == null) {
			throw new UserInitalizationException("No such user provider");
		}
		
		provider = map.get(serviceName);
		
		if (provider == null) {
			log(LogService.LOG_ERROR, "Didn't find the " + serviceName + " service");
			throw new UserInitalizationException("No such user provider");
		}
		
		user = provider.build(properties);
		
		return user;
	}

	@Override
	public PlatformUser build(Properties properties)
			throws UserInitalizationException {
		PlatformUser pu = new PlatformUserImpl();
		
		try {
			pu.populate(properties);
		} catch (IllegalArgumentException e) {
			throw new UserInitalizationException();
		}
		
		return pu;
	}

	@Override
	public UserRecipe[] getAvailableUserRecipes() {
		ArrayList<UserRecipe> recipes = new ArrayList<UserRecipe>();
		
		for (Map.Entry<String, Map<String, UserProvider>> entry
				: providers.entrySet()) {
			Map<String, UserProvider> map = entry.getValue();
			for (Map.Entry<String, UserProvider> entry2: map.entrySet()) {
				recipes.add(entry2.getValue().getRecipe());
			}
		}
		
		return recipes.toArray(new UserRecipe[0]);
	}

	@Override
	public UserRecipe getRecipe(String userType, String systemName) {
		UserRecipe recipe = null;
		Map<String, UserProvider> types = providers.get(userType);
		
		if (types != null) {
			try {
				recipe = ((UserProvider) types.get(systemName)).getRecipe();
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
