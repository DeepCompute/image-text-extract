package info.hb.lookup.object.proc;

import info.hb.lookup.object.Lookup.NotFound;
import info.hb.lookup.object.common.GFirst;
import info.hb.lookup.object.common.GPoint;
import info.hb.lookup.object.common.ImageBinary;
import info.hb.lookup.object.common.ImageBinaryChannel;
import info.hb.lookup.object.common.ImageBinaryRGB;
import info.hb.lookup.object.common.ImageMultiplySum;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * http://www.fmwconcepts.com/imagemagick/similar/index.php
 *
 * 1) mean && stddev
 *
 * 2) image1(x,y) - mean1 && image2(x,y) - mean2
 *
 * 3) [3] = (image1(x,y) - mean)(x,y) * (image2(x,y) - mean)(x,y)
 *
 * 4) [4] = mean([3])
 *
 * 5) [4] / (stddev1 * stddev2)
 *
 * Normalized cross correlation algorithm
 */
public class NCC {

	public static class WrongChannelType extends RuntimeException {

		private static final long serialVersionUID = 2487329823855153614L;

	}

	static public GPoint lookup(BufferedImage i, BufferedImage t, float m) {
		List<GPoint> list = lookupAll(i, t, m);

		if (list.size() == 0)
			throw new NotFound();

		Collections.sort(list, new GFirst());

		return list.get(0);
	}

	static public List<GPoint> lookupAll(BufferedImage i, BufferedImage t, float m) {
		ImageBinaryRGB imageBinary = new ImageBinaryRGB(i);
		ImageBinaryRGB templateBinary = new ImageBinaryRGB(t);

		return lookupAll(imageBinary, templateBinary, m);
	}

	static public GPoint lookup(ImageBinary image, ImageBinary template, float m) {
		List<GPoint> list = lookupAll(image, template, m);

		if (list.size() == 0)
			throw new NotFound();

		Collections.sort(list, new GFirst());

		return list.get(0);
	}

	static public List<GPoint> lookupAll(ImageBinary image, ImageBinary template, float m) {
		return lookupAll(image, 0, 0, image.getWidth() - 1, image.getHeight() - 1, template, m);
	}

	static public GPoint lookup(ImageBinary image, int x1, int y1, int x2, int y2, ImageBinary template, float m) {
		List<GPoint> list = lookupAll(image, x1, y1, x2, y2, template, m);

		if (list.size() == 0)
			throw new NotFound();

		Collections.sort(list, new GFirst());

		return list.get(0);
	}

	static public List<GPoint> lookupAll(ImageBinary image, int x1, int y1, int x2, int y2, ImageBinary template,
			float m) {
		List<GPoint> list = new ArrayList<>();

		for (int x = x1; x <= x2 - template.getWidth() + 1; x++) {
			for (int y = y1; y <= y2 - template.getHeight() + 1; y++) {
				GPoint g = lookup(image, template, x, y, m);
				if (g != null)
					list.add(g);
			}
		}

		return list;
	}

	static double numerator(ImageBinaryChannel image, ImageBinaryChannel template, int xx, int yy) {
		ImageMultiplySum m = new ImageMultiplySum(image.zeroMean, xx, yy, template.zeroMean);
		return m.sum;
	}

	static double denominator(ImageBinaryChannel image, ImageBinaryChannel template, int xx, int yy) {
		double di = image.dev2n(xx, yy, xx + template.getWidth() - 1, yy + template.getHeight() - 1);
		double dt = template.dev2n();
		return Math.sqrt(di * dt);
	}

	static public GPoint lookup(ImageBinary image, ImageBinary template, int x, int y, float m) {
		List<ImageBinaryChannel> ci = image.getChannels();
		List<ImageBinaryChannel> ct = template.getChannels();

		int ii = Math.min(ci.size(), ct.size());

		double g = Double.MAX_VALUE;

		for (int i = 0; i < ii; i++) {
			ImageBinaryChannel cct = ct.get(i);
			ImageBinaryChannel cci = ci.get(i);

			if (!cct.type.equals(cci.type))
				throw new WrongChannelType();

			double gg = gamma(cci, cct, x, y);

			if (gg < m)
				return null;

			g = Math.min(g, gg);
		}

		return new GPoint(x, y, g);
	}

	static public double gamma(ImageBinary image, ImageBinary template, int x, int y) {
		List<ImageBinaryChannel> ci = image.getChannels();
		List<ImageBinaryChannel> ct = template.getChannels();

		int ii = Math.min(ci.size(), ct.size());

		double g = 0;

		for (int i = 0; i < ii; i++) {
			ImageBinaryChannel cct = ct.get(i);
			ImageBinaryChannel cci = ci.get(i);

			if (!cct.type.equals(cci.type))
				throw new WrongChannelType();

			g += gamma(cci, cct, x, y);
		}

		g /= ii;

		return g;
	}

	static public double gammaMin(ImageBinary image, ImageBinary template, int x, int y) {
		List<ImageBinaryChannel> ci = image.getChannels();
		List<ImageBinaryChannel> ct = template.getChannels();

		int ii = Math.min(ci.size(), ct.size());

		double g = Double.MAX_VALUE;

		for (int i = 0; i < ii; i++) {
			ImageBinaryChannel cct = ct.get(i);
			ImageBinaryChannel cci = ci.get(i);

			if (!cct.type.equals(cci.type))
				throw new WrongChannelType();

			g = Math.min(g, gamma(cci, cct, x, y));
		}

		return g;
	}

	static public double gamma(ImageBinaryChannel image, ImageBinaryChannel template, int xx, int yy) {
		double d = denominator(image, template, xx, yy);

		if (d == 0)
			return -1;

		double n = numerator(image, template, xx, yy);

		return (n / d);
	}

}
