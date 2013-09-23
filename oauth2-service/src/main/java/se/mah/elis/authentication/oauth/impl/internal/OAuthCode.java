package se.mah.elis.authentication.oauth.impl.internal;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * Representation of a authorization code or access token and 
 * helpers to create one. 
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 *
 */
public class OAuthCode {

	private static final String SALT = "elis-monkey-make-me-salt";
	
	// fields
	private String code;
	private long timeToLive; // millis
	private long creationTime; 
	
	private OAuthCode() { } // force use of factory
	
	/**
	 * Check if the code has expired or not.
	 *  
	 * @return true if code is expired
	 */
	public boolean isExpired() {
		return (System.currentTimeMillis() - timeToLive) > getCreationTime();
	}

	/**
	 * Get at string representation of the code
	 * 
	 * @return code as string
	 */
	public String getCode() {
		return code;
	}

	private void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Returns the time to live in millis
	 * 
	 * @return time in millis
	 */
	public long getTimeToLive() {
		return timeToLive;
	}

	private void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}
	
	/**
	 * Returns the time of code creation 
	 * 
	 * @return time in millis
	 */
	public long getCreationTime() {
		return creationTime;
	}

	private void setCreationTime(long time) {
		this.creationTime = time;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof OAuthCode))
			return false;
		return this.getCode().equals(((OAuthCode) other).getCode());
	}
	
	@Override
	public int hashCode() {
		return getCode().hashCode();
	}
	
	@Override
	public String toString() {
		return getCode();
	}
	
	/**
	 * Create a code using any time to live. 
	 * 
	 * @param ttl in milliseconds
	 * @return an instance of a code
	 * @since 1.0
	 */
	public static OAuthCode create(long ttl) {
		OAuthCode code = new OAuthCode();
		code.setTimeToLive(ttl);
		code.setCreationTime(System.currentTimeMillis());
		code.setCode(createCode());
		return code;
	}
	
	/**
	 * Create a code using the default time to live = 10 minutes. 
	 * 
	 * @return an instance of a code
	 * @since 1.0
	 */
	public static OAuthCode create() {
		long tenMinutes = 1000*60*10;
		return create(tenMinutes);
	}
	
	private static String createCode() {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(createRandomBytes());
			byte[] code = md.digest();
			for (byte b : code) {
				sb.append(Integer.toHexString((int) (b & 0xff)));
			}
		} catch (NoSuchAlgorithmException e) { }
		return sb.toString();
	}

	private static byte[] createRandomBytes() {
		int lengthOfLong = 8;
		ByteBuffer buffer = ByteBuffer.allocate(lengthOfLong + SALT.getBytes().length); 
		buffer.putLong(System.currentTimeMillis());
		buffer.put(SALT.getBytes());
		return buffer.array();
	}

}
