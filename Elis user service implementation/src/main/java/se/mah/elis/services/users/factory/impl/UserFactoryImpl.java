package se.mah.elis.services.users.factory.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;
import se.mah.elis.services.users.factory.UserRecipe;

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
		// TODO Auto-generated method stub
		return null;
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

}
