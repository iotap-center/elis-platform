/**
 * 
 */
package se.mah.elis.adaptor.device.api.entities.devices;

/**
 * ColoredLamp describes a coloured light source, such as a Philips Hue or
 * similar. If the device isn't able to display the colour provided, it will
 * try to display the closest possible approximation. 
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface ColoredLamp extends Actuator {
	
	/**
	 * Red
	 * 
	 * @since 1.0
	 */
	int RED = 0xFF0000;
	
	/**
	 * Green
	 * 
	 * @since 1.0
	 */
	int GREEN = 0x00FF00;
	
	/**
	 * Blue
	 * 
	 * @since 1.0
	 */
	int BLUE = 0x0000FF;
	
	/**
	 * White
	 * 
	 * @since 1.0
	 */
	int WHITE = 0xFFFFFF;
	
	/**
	 * Black
	 * 
	 * @since 1.0
	 */
	int BLACK = 0x000000;
	
	/**
	 * <p>Sets the colour of the light. The colour is defined by a 24 bit
	 * value, giving a colour space between 0 and 16777215, i.e.
	 * 255*255*255.</p>
	 * 
	 * <p>The predefined colour codes found in this interface are used in
	 * conjunction with this method.</p>
	 * 
	 * @param rgb The colour code.
	 * @since 1.0
	 */
	void setColor(int rgb);
	
	/**
	 * Sets the colour of the light. The colour is defined by three discrete
	 * eight-bit values, one for each channel (red, green, blue), much like
	 * colours are defined in the HTML world.
	 * 
	 * @param r The red channel.
	 * @param g The green channel.
	 * @param b The blue channel.
	 * @since 1.0
	 */
	void setColor(byte r, byte g, byte b);
	
	/**
	 * Gets the current colour value. The colour is defined by a 24 bit value,
	 * giving a colour space between 0 and 16777215, i.e.
	 * 255*255*255.
	 * 
	 * @return The colour code.
	 * @since 1.0
	 */
	int getColor();
}
