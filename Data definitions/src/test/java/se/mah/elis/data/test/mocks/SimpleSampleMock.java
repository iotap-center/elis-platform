package se.mah.elis.data.test.mocks;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.data.OrderedProperties;
import se.mah.elis.data.Sample;

public class SimpleSampleMock implements Sample {

	public SimpleSampleMock() {
		// TODO Auto-generated constructor stub
	}

	public long getSampleLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public DateTime getSampleTimestamp() {
		// TODO Auto-generated method stub
		return new DateTime();
	}

	public List<String> getTraversableMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUUID(UUID uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUniqueUserId(UUID userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UUID getUniqueUserId() {
		// TODO Auto-generated method stub
		return UUID.randomUUID();
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populate(Properties props) {
		// TODO Auto-generated method stub
		
	}

}
