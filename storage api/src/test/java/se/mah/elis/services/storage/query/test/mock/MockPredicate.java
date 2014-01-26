package se.mah.elis.services.storage.query.test.mock;

import se.mah.elis.services.storage.query.Predicate;
import se.mah.elis.services.storage.query.QueryTranslator;

public class MockPredicate implements Predicate {

	private int c;
	
	public MockPredicate(int cardinality) {
		c = cardinality;
	}
	
	@Override
	public String compile() {
		return "MockPredicate " + c;
	}

	@Override
	public void setTranslator(QueryTranslator translator) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return compile();
	}
}
