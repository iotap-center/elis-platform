package se.mah.elis.adaptor.energy.eon.internal.devices;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.parser.ParseException;

import se.mah.elis.adaptor.device.api.entities.devices.ElectricitySampler;
import se.mah.elis.adaptor.device.api.exceptions.SensorFailedException;
import se.mah.elis.data.ElectricitySample;

/**
 * A virtual representation of the E.On power meter. Powerswitches which is
 * capable of metering also inherits from this class.
 * 
 * @author Joakim Lithell
 * @author Marcus Ljungblad
 * @author "Johan Holmberg, Malmö University"
 * @version 1.2.0
 * @since 1.0
 */

public class EonPowerMeter extends EonDevice implements ElectricitySampler {

	private static final int HOURLY = 0;
	private static DateTimeFormatter fmt = DateTimeFormat
			.forPattern("yyyy-MM-dd");

	@Override
	public ElectricitySample getSample() throws SensorFailedException {
		double value;

		try {
			value = httpBridge.getPowerMeterKWh(
					this.gateway.getAuthenticationToken(), getGatewayAddress(),
					getId().toString());
		} catch (ParseException e) {
			throw new SensorFailedException();
		}

		ElectricitySample electricitySample = null;
		electricitySample = new ElectricitySampleImpl(value);

		return electricitySample;
	}

	@Override
	public List<ElectricitySample> getSamples(DateTime from, DateTime to)
			throws SensorFailedException {
		List<ElectricitySample> samples = new ArrayList<ElectricitySample>();
		List<ElectricitySampleImpl> someSamples = new ArrayList<ElectricitySampleImpl>();
		List<Map<String, Object>> data = null;
		boolean forcedInclusion = false;
		
		// First of all, we need to understand that any calls to E.On's
		// end point getStats will retrieve 24 data points when using the hourly
		// resolution. This needs to be taken into account while doing our
		// magic.

		DateTime start = null;
		
		// The if condition checks if milliseconds after midnight is zero,
		// i.e. if the instant is in fact midnight
		if ((from.get(DateTimeFieldType.millisOfDay()) == 0)) {
			// As per the definition, the from and to parameters are INCLUSIVE,
			// meaning that we need to fetch the latest sample from the
			// previous day.
			start = from.minus(1);
			forcedInclusion = true;
		} else {
			start = from;
		}

		// Fetch all samples
		while (start.isBefore(to)) {
			try {
				data = httpBridge.getStatData(
						gateway.getAuthenticationToken(), getGatewayAddress(),
						getId().toString(), formatDate(start), formatDate(to),
						HOURLY);
				someSamples.addAll(convertToSamples(data, start));
			} catch (ParseException e) {
				throw new SensorFailedException();
			}

			start = start.plusDays(1);

			if (start.equals(to)) {
				break;
			}
		}
		
		// If we had to change the "from" timestamp, all samples will be offset
		// by 1 ms, and all need to be corrected.
		if (forcedInclusion) {
			for (ElectricitySampleImpl sample : someSamples) {
				sample.setSamplingTime(sample.getSampleTimestamp().plus(1));
			}
		}
		
		// Trim unwanted samples from the list
		Iterator iter = someSamples.iterator();
		ElectricitySampleImpl sample = null;
		while(iter.hasNext()) {
			sample = (ElectricitySampleImpl) iter.next();
			if (sample.getSampleTimestamp().isBefore(from) ||
					sample.getSampleTimestamp().isAfter(to)) {
				iter.remove();
			}
		}
		
		// Convert to the right kind of collection
		samples.addAll(someSamples);
		
		return samples;
	}

	private List<ElectricitySampleImpl> convertToSamples(
			List<Map<String, Object>> data, DateTime from) {
		List<ElectricitySampleImpl> samples = new ArrayList<>();

		for (Map<String, Object> rawsample : data) {
			double value = number(rawsample.get("Value"));
			DateTime sampleTime = calculateSampleTime(from,
					(String) rawsample.get("Key"));
			ElectricitySampleImpl sample = new ElectricitySampleImpl(value,
					sampleTime);
			sample.setSampleLength(DateTimeConstants.MILLIS_PER_HOUR);
			samples.add(sample);
		}

		return samples;
	}

	private DateTime calculateSampleTime(DateTime from, String offset) {
		int HOUR_START = 11;
		int HOUR_STOP = 13;
		int offsetInHours = Integer.parseInt(offset.substring(HOUR_START,
				HOUR_STOP));
		DateTime sampleTime = from.plusHours(offsetInHours);
		return sampleTime;
	}

	private double number(Object object) {
		double value = ((Number) object).doubleValue();
		return value;
	}

	public String formatDate(DateTime from) {
		return fmt.print(from);
	}

	@Override
	public ElectricitySample sample(int millis) throws SensorFailedException {
		// TODO Sample the current energy usage for a given amount of time.
		return null;
	}

	protected String getGatewayAddress() {
		return getGateway().getAddress().toString();
	}

}
