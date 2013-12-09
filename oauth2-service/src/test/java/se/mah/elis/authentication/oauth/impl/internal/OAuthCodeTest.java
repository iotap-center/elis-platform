package se.mah.elis.authentication.oauth.impl.internal;

import static org.junit.Assert.*;

import java.nio.BufferOverflowException;

import org.junit.Test;

import se.mah.elis.authentication.oauth.OAuthCode;

public class OAuthCodeTest {

	@Test
	public void codeShouldBeDead() throws InterruptedException {
		OAuthCode code = OAuthCodeImpl.create(1);
		Thread.sleep(10);
		assertTrue(code.isExpired());
	}
	
	@Test
	public void codeShouldBeAlive() {
		OAuthCode code = OAuthCodeImpl.create(10000);
		assertFalse(code.isExpired());
	}
	
	@Test
	public void createOAuthCode() {
		try {
			OAuthCodeImpl.create();
		} catch (BufferOverflowException boe) {
			fail("Byte buffer in code too small");
		}
	}
}
