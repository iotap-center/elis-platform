package se.mah.elis.impl.services.users.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;

@Component(name = "Elis User factory")
@Service(value = UserFactory.class)
public class UserFactoryImpl implements UserFactory {

	private Map<String, Map<String, UserProvider>> providers;
	
	public UserFactoryImpl() {
		providers = new HashMap<String, Map<String, UserProvider>>();
	}

	@Override
	public void registerProvider(UserProvider provider) {
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
		UserProvider provider = null;
		User user = null;
		Map<String, UserProvider> map =
				providers.get(userType);
		
		if (map == null) {
			throw new UserInitalizationException("No such user provider");
		}
		
		provider = map.get(serviceName);
		
		if (provider == null) {
			throw new UserInitalizationException("No such user provider");
		}
		
		user = provider.build(properties);
		
		return user;
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

}
