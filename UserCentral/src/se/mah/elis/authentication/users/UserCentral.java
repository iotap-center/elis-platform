package se.mah.elis.authentication.users;


public interface UserCentral {
	public User addUser(User user);
	public void removeUser(String user);
	public User getUser(String userName);
	public void linkUserToSystem(User user, String systemId);
	public void unlinkUserToSystem(User user, String systemId);
}
