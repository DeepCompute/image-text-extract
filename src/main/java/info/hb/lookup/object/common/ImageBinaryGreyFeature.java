package info.hb.lookup.object.common;

import info.hb.lookup.object.common.ImageBinaryChannel.ChannelType;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * Container for ImageBinary and Feature class for each channel (one gray channel here)
 */
public class ImageBinaryGreyFeature implements ImageBinaryFeature {

	public RGBImage image;
	public ImageBinaryChannelFeature grey;

	public List<ImageBinaryChannelFeature> list;

	public ImageBinaryGreyFeature(BufferedImage img, double threshold) {
		init(img);

		FeatureSet lr = new FeatureSetAuto(grey, threshold);
		grey.init(lr);

	}

	public void init(BufferedImage img) {
		image = new RGBImage();
		grey = new ImageBinaryChannelFeature(ChannelType.GREY);

		list = Arrays.asList(new ImageBinaryChannelFeature[] { grey });

		this.image.init(img);
		this.grey.initBase(this.image.r);

		for (int x = 0; x < this.image.cx; x++) {
			for (int y = 0; y < this.image.cy; y++) {
				this.image.step(x, y);
				this.grey.step(x, y);
			}
		}

		grey.zeroMean = new ImageZeroMean();
		grey.zeroMean.init(grey.integral);

		for (int x = 0; x < this.image.cx; x++) {
			for (int y = 0; y < this.image.cy; y++) {
				grey.zeroMean.step(x, y);
			}
		}

		grey.zeroMeanIntegral = new IntegralImage();
		grey.zeroMeanIntegral.initBase(grey.zeroMean);

		for (int x = 0; x < this.image.cx; x++) {
			for (int y = 0; y < this.image.cy; y++) {
				grey.zeroMeanIntegral.step(x, y);
			}
		}
	}

	@Override
	public int getWidth() {
		return image.cx;
	}

	@Override
	public int getHeight() {
		return image.cy;
	}

	@Override
	public int size() {
		return image.cx * image.cy;
	}

	@Override
	public BufferedImage getImage() {
		return image.buf;
	}

	@Override
	public List<ImageBinaryChannelFeature> getFeatureChannels() {
		return list;
	}

}
