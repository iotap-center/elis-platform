package se.mah.elis.authentication.users.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.mah.elis.authentication.users.User;
import se.mah.elis.authentication.users.UserCentral;

public class UserCentralImpl implements UserCentral {

	private Map<String, User> userCentral;
	private Map<User, List<String>> userToSystemMap;
	
	public UserCentralImpl() {
		// FIXME users should be stored in persistent storage
		this.userCentral = new HashMap<String, User>();
		this.userToSystemMap = new HashMap<User, List<String>>();
	}	
	
	@Override
	public User addUser(User user) {
		if (user == null || user.userName.isEmpty()) 
			return null;
		
		this.userCentral.put(user.userName, user);
				
		return user;
	}

	@Override
	public void removeUser(String userName) {
		if (this.userCentral.containsKey(userName))
			this.userCentral.remove(userName);
	}
	
	public User getUser(String userName) { 
		return this.userCentral.get(userName);
	}

	@Override
	public void linkUserToSystem(User user, String systemId) {
		List<String> userLinks = this.userToSystemMap.get(user);
		
		if (userLinks == null) {
			userLinks = new ArrayList<String>();
		}
		
		if (!systemId.isEmpty()) {
			userLinks.add(systemId);
		}
		
		this.userToSystemMap.put(user, userLinks);
	}
	
	@Override
	public void unlinkUserToSystem(User user, String systemId) {
		List<String> userLinks = this.userToSystemMap.get(user);
		
		if (userLinks != null && !systemId.isEmpty()) {
			userLinks.remove(systemId);
		}
		
		this.userToSystemMap.put(user, userLinks);
	}

}
