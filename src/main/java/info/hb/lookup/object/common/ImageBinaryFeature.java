package info.hb.lookup.object.common;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Inteface for ImageBinaryGrayFeature and ImageBinaryRGBFeature classes
 */
public interface ImageBinaryFeature {

	public List<ImageBinaryChannelFeature> getFeatureChannels();

	public int getWidth();

	public int getHeight();

	public int size();

	public BufferedImage getImage();

}
