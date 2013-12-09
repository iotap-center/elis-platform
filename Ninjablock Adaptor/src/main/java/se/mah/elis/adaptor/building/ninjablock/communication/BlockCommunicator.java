package se.mah.elis.adaptor.building.ninjablock.communication;

public class BlockCommunicator {

	private Communicator com;
	
	public BlockCommunicator(Communicator c) {
		com = c;
	}
	
	/**
	 * Returns all the blocks paired to an account.
	 * @return json response(String)
	 * @throws Exception
	 */
	public String getBlocks() throws Exception {
		return com.httpGet("https://api.ninja.is/rest/v0/block");
	}
	
	/**
	 * Fetch a specific block's details.
	 * @param nodeid
	 * @return json response(String)
	 * @throws Exception
	 */
	public String getBlock(String nodeid) throws Exception {
		return com.httpGet("https://api.ninja.is/rest/v0/block/" + nodeid);
	}
	
}