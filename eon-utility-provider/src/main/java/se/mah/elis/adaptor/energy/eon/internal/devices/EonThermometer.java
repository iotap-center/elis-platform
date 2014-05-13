package se.mah.elis.adaptor.energy.eon.internal.devices;

import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.device.api.data.DeviceIdentifier;
import se.mah.elis.adaptor.device.api.entities.devices.DeviceSet;
import se.mah.elis.adaptor.device.api.entities.devices.Gateway;
import se.mah.elis.adaptor.device.api.entities.devices.Thermometer;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.adaptor.energy.eon.internal.gateway.EonGateway;
import se.mah.elis.data.OrderedProperties;
import se.mah.elis.data.TemperatureData;
import se.mah.elis.exceptions.StaticEntityException;

/**
 * A virtual representation of the E.On Thermometer
 * 
 * @author Joakim Lithell
 * @author "Johan Holmberg, Malmö University"
 * @version 1.0.0
 * @since 1.0
 *
 */

public class EonThermometer extends EonDevice implements Thermometer {

	private static final long serialVersionUID = 4511570832087418074L;

	@Override
	public TemperatureData getCurrentTemperature() throws SensorFailedException {
		float currentTemperature = 0;

		try {
			currentTemperature = httpBridge.getTemperature(
					this.gateway.getAuthenticationToken(), getGatewayAddress(),
					getId().toString());
		} catch (ParseException e) {
			throw new SensorFailedException();
		}

		TemperatureData temperatureData =
				new TemperatureDataImpl(currentTemperature);

		return temperatureData;
	}

	private String getGatewayAddress() {
		return getGateway().getAddress().toString();
	}

	@Override
	public void populate(Properties props) {
		super.populate(props);
	}
}
