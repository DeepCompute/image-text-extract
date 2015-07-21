package info.hb.lookup.object.common;

import info.hb.lookup.object.common.ImageBinaryChannel.ChannelType;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * Container for ImageBinary class for each channel (one gray channel here)
 */
public class ImageBinaryGrey implements ImageBinary {

	public GrayImage gi;
	public ImageBinaryChannel gray;

	public List<ImageBinaryChannel> list;

	public ImageBinaryGrey(BufferedImage img) {
		gi = new GrayImage();
		gray = new ImageBinaryChannel(ChannelType.GREY);

		list = Arrays.asList(new ImageBinaryChannel[] { gray });

		this.gi.init(img);
		this.gray.initBase(gi);

		for (int x = 0; x < this.gi.cx; x++) {
			for (int y = 0; y < this.gi.cy; y++) {
				this.gi.step(x, y);
				this.gray.step(x, y);
			}
		}

		gray.zeroMean = new ImageZeroMean(gray.integral);
	}

	@Override
	public int getWidth() {
		return gi.cx;
	}

	@Override
	public int getHeight() {
		return gi.cy;
	}

	@Override
	public int size() {
		return gi.cx * gi.cy;
	}

	@Override
	public BufferedImage getImage() {
		return gi.buf;
	}

	@Override
	public List<ImageBinaryChannel> getChannels() {
		return list;
	}

}
