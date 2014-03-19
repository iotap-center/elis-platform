package se.mah.elis.adaptor.water.mkb;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.exceptions.GatewayCommunicationException;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.exceptions.StaticEntityException;
import se.mah.elis.services.users.UserIdentifier;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * 
 * MKB Gateway user. When initialised also adds the meter registered with the user 
 * account. 
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
public class MkbGatwayUser implements GatewayUser {

	private UUID uuid;
	private UserIdentifier userIdentifier;
	private Gateway gateway;

	@Override
	public UUID getUserId() {
		return this.uuid;
	}

	@Override
	public void setUserId(UUID id) {
		this.uuid = id;
	}

	@Override
	public void initialize() throws UserInitalizationException {
		if (!gateway.hasConnected()) {
			try {
				addWaterMeter();
				gateway.connect();
			} catch (GatewayCommunicationException | StaticEntityException e) {
				throw new UserInitalizationException();
			}
		}
	}

	private void addWaterMeter() throws StaticEntityException {
		MkbWaterMeter meter = new MkbWaterMeter();
		meter.setGateway(gateway);
		meter.setName(userIdentifier.toString());
		meter.setId(new MkbWaterMeterDeviceIdentifier(userIdentifier.toString()));
		gateway.add(meter);
	}

	@Override
	public UserIdentifier getIdentifier() {
		return this.userIdentifier;
	}

	@Override
	public void setIdentifier(UserIdentifier id) {
		this.userIdentifier = id;
	}

	@Override
	public OrderedProperties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderedProperties getPropertiesTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populate(Properties props) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getServiceName() {
		return "mkb-water-gateway-user";
	}

	@Override
	public Gateway getGateway() {
		return this.gateway;
	}

	@Override
	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public DateTime created() {
		// TODO Auto-generated method stub
		return null;
	}

}
