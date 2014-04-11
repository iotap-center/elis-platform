package se.mah.elis.adaptor.fooprovider.internal;

import java.util.Iterator;

import se.mah.elis.adaptor.device.api.entities.devices.Device;

public class FooIterator implements Iterator<Device> {

	private FooGateway gateway;
	private boolean hasNext;
	
	public FooIterator(FooGateway gw) {
		gateway = gw;
		hasNext = true;
	}
	
	@Override
	public boolean hasNext() {
		hasNext = !hasNext;
		return !hasNext;
	}

	@Override
	public Device next() {
		return gateway.getMeter();
	}

	@Override
	public void remove() {
	}

}
