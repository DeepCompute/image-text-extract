package info.hb.lookup.object.proc;

import info.hb.lookup.object.Lookup.NotFound;
import info.hb.lookup.object.common.FeatureK;
import info.hb.lookup.object.common.GFirst;
import info.hb.lookup.object.common.GPoint;
import info.hb.lookup.object.common.ImageBinary;
import info.hb.lookup.object.common.ImageBinaryChannel;
import info.hb.lookup.object.common.ImageBinaryChannelFeature;
import info.hb.lookup.object.common.ImageBinaryFeature;
import info.hb.lookup.object.common.ImageBinaryRGB;
import info.hb.lookup.object.common.ImageBinaryRGBFeature;
import info.hb.lookup.object.common.RectK;
import info.hb.lookup.object.proc.NCC.WrongChannelType;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * http://isas.uka.de/Material/AltePublikationen/briechle_spie2001.pdf
 *
 * NOT WORKING (check NCC.java)
 *
 * Fast Normalized cross correlation algorithm
 */
public class FNCC {

	static public List<GPoint> lookupAll(BufferedImage i, BufferedImage t, double threshold, float m) {
		ImageBinaryRGB imageBinary = new ImageBinaryRGB(i);
		ImageBinaryRGBFeature templateBinary = new ImageBinaryRGBFeature(t, threshold);

		return lookupAll(imageBinary, templateBinary, m);
	}

	static public GPoint lookup(ImageBinary image, ImageBinaryFeature template, float m) {
		List<GPoint> list = lookupAll(image, template, m);

		if (list.size() == 0)
			throw new NotFound();

		Collections.sort(list, new GFirst());

		return list.get(0);
	}

	static public List<GPoint> lookupAll(ImageBinary image, ImageBinaryFeature template, float m) {
		return lookupAll(image, 0, 0, image.getWidth() - 1, image.getHeight() - 1, template, m);
	}

	static public List<GPoint> lookupAll(ImageBinary image, int x1, int y1, int x2, int y2,
			ImageBinaryFeature template, float m) {
		List<GPoint> list = new ArrayList<>();

		for (int x = x1; x <= x2 - template.getWidth() + 1; x++) {
			for (int y = y1; y <= y2 - template.getHeight() + 1; y++) {
				List<ImageBinaryChannel> ci = image.getChannels();
				List<ImageBinaryChannelFeature> ct = template.getFeatureChannels();

				int ii = Math.min(ci.size(), ct.size());

				boolean b = true;
				double gg = Double.MAX_VALUE;

				for (int i = 0; i < ii; i++) {
					ImageBinaryChannelFeature cct = ct.get(i);
					ImageBinaryChannel cci = ci.get(i);

					if (!cct.type.equals(cci.type))
						throw new WrongChannelType();

					double g = gamma(cci, cct, x, y);

					gg = Math.min(gg, g);

					if (g < m) {
						b = false;
					}
				}

				if (b) {
					list.add(new GPoint(x, y, gg));
				}
			}
		}

		return list;
	}

	static double denominator(ImageBinaryChannel image, ImageBinaryChannel template, int xx, int yy) {
		double di = image.dev2n(xx, yy, xx + template.getWidth() - 1, yy + template.getHeight() - 1);
		double dt = template.dev2n();
		return Math.sqrt(di * dt);
	}

	static double numerator(ImageBinaryChannel image, ImageBinaryChannelFeature template, int xx, int yy) {
		double n = 0;

		for (FeatureK f : template.k) {
			for (RectK k : f.list) {
				double ii = image.integral.sigma(xx + k.x1, yy + k.y1, xx + k.x2, yy + k.y2);
				double mt = k.k;
				n += ii * mt;
			}
		}

		return n;
	}

	static public double gamma(ImageBinaryChannel image, ImageBinaryChannelFeature template, int xx, int yy) {
		double d = denominator(image, template, xx, yy);

		if (d == 0)
			return -1;

		double n = numerator(image, template, xx, yy);

		return (n / d);
	}

}
