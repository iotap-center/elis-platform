package se.mah.elis.adaptor.energy.eon.internal.gateway;

import se.mah.elis.adaptor.device.api.data.GatewayAddress;

/**
 * Representation of a gateway address for E.On 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class EonGatewayAddress implements GatewayAddress {

	private String id; 
	
	public EonGatewayAddress(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof EonGatewayAddress)
			return this.getId().equals( ((EonGatewayAddress)o).getId() );
		return false;
	}
}
