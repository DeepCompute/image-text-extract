package info.hb.lookup.object.common;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Inteface for ImageBinaryGray and ImageBinaryRGB classes
 */
public interface ImageBinary {

	public List<ImageBinaryChannel> getChannels();

	public int getWidth();

	public int getHeight();

	public int size();

	public BufferedImage getImage();

}
