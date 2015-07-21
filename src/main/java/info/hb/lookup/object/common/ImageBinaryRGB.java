package info.hb.lookup.object.common;

import info.hb.lookup.object.common.ImageBinaryChannel.ChannelType;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * Container for ImageBinary class for each channel (rgb channels here)
 */
public class ImageBinaryRGB implements ImageBinary {

	public RGBImage image;
	public ImageBinaryChannel r;
	public ImageBinaryChannel g;
	public ImageBinaryChannel b;

	public List<ImageBinaryChannel> list;

	public ImageBinaryRGB(BufferedImage img) {
		image = new RGBImage();
		r = new ImageBinaryChannel(ChannelType.RED);
		g = new ImageBinaryChannel(ChannelType.GREEN);
		b = new ImageBinaryChannel(ChannelType.BLUE);

		list = Arrays.asList(new ImageBinaryChannel[] { r, g, b });

		this.image.init(img);
		this.r.initBase(this.image.r);
		this.g.initBase(this.image.g);
		this.b.initBase(this.image.b);

		for (int x = 0; x < this.image.cx; x++) {
			for (int y = 0; y < this.image.cy; y++) {
				this.image.step(x, y);
				this.r.step(x, y);
				this.g.step(x, y);
				this.b.step(x, y);
			}
		}

		r.zeroMean = new ImageZeroMean();
		g.zeroMean = new ImageZeroMean();
		b.zeroMean = new ImageZeroMean();
		r.zeroMean.init(r.integral);
		g.zeroMean.init(g.integral);
		b.zeroMean.init(b.integral);

		for (int x = 0; x < this.image.cx; x++) {
			for (int y = 0; y < this.image.cy; y++) {
				r.zeroMean.step(x, y);
				g.zeroMean.step(x, y);
				b.zeroMean.step(x, y);
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
	public List<ImageBinaryChannel> getChannels() {
		return list;
	}

}
