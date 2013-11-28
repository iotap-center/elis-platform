package se.mah.elis.data;

import se.mah.elis.services.storage.data.ElisDataObject;

/**
 * The PriceData interface encapsulates pricing data. It maintains three types
 * of data: the pricing information itself (as a decimal value), the tax rate
 * and the currency code for the currency in which the price is given.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface PriceData extends ElisDataObject {
	
	/**
	 * Returns the total price, tax portion included.
	 * 
	 * @return The total price.
	 * @since 1.0
	 */
	double getPrice();
	
	/**
	 * Returns the price, tax portion excluded.
	 * 
	 * @return The total price.
	 * @since 1.0
	 */
	double getPriceWithoutTax();
	
	/**
	 * Returns the tax portion of the total price.
	 * 
	 * @return The tax portion.
	 * @since 1.0
	 */
	double getTaxPortion();
	
	/**
	 * The current tax rate, as a decimal value.
	 * 
	 * @return The current tax rate.
	 * @since 1.0
	 */
	double getTaxRate();
	
	/**
	 * The currency code.
	 * 
	 * @return The currency code.
	 * @since 1.0
	 */
	String getCurrency();
}
