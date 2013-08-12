package se.mah.elis.auxiliaries.test.mocks;

import java.util.Date;
import java.util.List;

import se.mah.elis.adaptor.building.api.data.Sample;

public class SimpleSampleMock implements Sample {

	public SimpleSampleMock() {
		// TODO Auto-generated constructor stub
	}

	public int getSampleLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Date getSampleTimestamp() {
		// TODO Auto-generated method stub
		return new Date();
	}

	public List<String> getTraversableMethods() {
		// TODO Auto-generated method stub
		return null;
	}

}
