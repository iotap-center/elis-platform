package se.mah.elis.authentication.oauth.impl.internal;

import static org.junit.Assert.*;

import java.nio.BufferOverflowException;

import org.junit.Test;

public class OAuthCodeTest {

	@Test
	public void codeShouldBeDead() throws InterruptedException {
		OAuthCode code = OAuthCode.create(1);
		Thread.sleep(10);
		assertTrue(code.isExpired());
	}
	
	@Test
	public void codeShouldBeAlive() {
		OAuthCode code = OAuthCode.create(10000);
		assertFalse(code.isExpired());
	}
	
	@Test
	public void createOAuthCode() {
		try {
			OAuthCode.create();
		} catch (BufferOverflowException boe) {
			fail("Byte buffer in code too small");
		}
	}
}
