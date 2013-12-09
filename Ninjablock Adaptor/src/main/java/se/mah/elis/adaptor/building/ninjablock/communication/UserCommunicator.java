package se.mah.elis.adaptor.building.ninjablock.communication;

public class UserCommunicator {
	
	private Communicator com;
	
	public UserCommunicator(Communicator c) {
		com = c;
	}
	
	/**
	 * Gets the user with the specific API-token K.
	 * @return result(String)
	 * @throws Exception
	 */
	public String getUser() throws Exception	{
		return com.httpGet("https://api.ninja.is/rest/v0/user");
	}
	
	/**
	 * Returns the 30 most recent entries in the authenticating user's activity stream.
	 * @return result(String)
	 * @throws Exception
	 */
	public String getUserStream() throws Exception {
		return com.httpGet("https://api.ninja.is/rest/v0/user/stream");
	}
	
	/**
	 * Returns user's pusher channel key.
	 * @return returns pusherChannel
	 * @throws Exception
	 */
	public String getUserPusherChannel() throws Exception {
		return com.httpGet("https://api.ninja.is/rest/v0/user/pusherchannel");
	}
	
	// TODO Implement PUT user/realtime?
}
