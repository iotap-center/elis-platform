package se.mah.elis.services.users.impl.test.mock;

import se.mah.elis.services.users.UserIdentifier;

public class MockUserIdentifier implements UserIdentifier {

	private String id;
	
	public MockUserIdentifier(String id) {
		this.id = id;
	}

	public String toString() {
		return id;
	}
}
