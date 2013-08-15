package se.mah.elis.services.utilitiy.electricity;

import java.util.Date;

import se.mah.elis.services.utilitiy.electricity.data.PriceData;
import se.mah.elis.services.utilitiy.electricity.exceptions.OutOfDateRangeException;

/**
 * The ElectricityPriceSource interface provides a set of methods used to
 * acquire electricity pricing data.
 * 
 * @since 1.0
 * @author "Johan Holmberg, Malmö University"
 */
public interface ElectricityPriceSource {
	
	/**
	 * Provides current pricing information.
	 * 
	 * @return Current pricing information.
	 * @since 1.0
	 */
	PriceData getCurrentPrice();
	
	/**
	 * Provides historical pricing information for a given moment in time.
	 * 
	 * @param when The moment for which we're looking for pricing information.
	 * @return Pricing information.
	 * @throws OutOfDateRangeException if the date given is in the future or
	 * 		   simply older than the earliest pricing data available.
	 * @since 1.0
	 */
	PriceData getPriceAtAGivenMoment(Date when) throws OutOfDateRangeException;
}
