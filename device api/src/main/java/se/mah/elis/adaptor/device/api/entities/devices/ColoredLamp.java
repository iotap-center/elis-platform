/**
 * 
 */
package se.mah.elis.adaptor.device.api.entities.devices;

import se.mah.elis.adaptor.device.api.exceptions.ActuatorFailedException;

/**
 * ColoredLamp describes a colored light source, such as a Philips Hue or
 * similar. If the device isn't able to display the color provided, it will
 * try to display the closest possible approximation. 
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
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
	 * <p>Sets the color of the light. The color is defined by a 24 bit
	 * value, giving a color space between 0 and 16777215, i.e.
	 * 255*255*255.</p>
	 * 
	 * <p>The predefined color codes found in this interface are used in
	 * conjunction with this method.</p>
	 * 
	 * @param rgb The color code.
	 * @throws ActuatorFailedException if the color couldn't be set.
	 * @since 1.0
	 */
	void setColor(int rgb) throws ActuatorFailedException;
	
	/**
	 * Sets the color of the light. The color is defined by three discrete
	 * eight-bit values, one for each channel (red, green, blue), much like
	 * colors are defined in the HTML world.
	 * 
	 * @param r The red channel.
	 * @param g The green channel.
	 * @param b The blue channel.
	 * @throws ActuatorFailedException if the color couldn't be set.
	 * @since 1.0
	 */
	void setColor(byte r, byte g, byte b) throws ActuatorFailedException;
	
	/**
	 * Gets the current color value. The color is defined by a 24 bit value,
	 * giving a color space between 0 and 16777215, i.e. 255*255*255.
	 * 
	 * @return The color code.
	 * @throws ActuatorFailedException if the color couldn't be read.
	 * @since 1.0
	 */
	int getColor() throws ActuatorFailedException;
}
