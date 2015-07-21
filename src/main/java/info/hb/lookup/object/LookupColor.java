package info.hb.lookup.object;

import info.hb.lookup.object.Lookup.NotFound;
import info.hb.lookup.object.common.ImageBinary;
import info.hb.lookup.object.common.ImageBinaryChannel;
import info.hb.lookup.object.common.ImageBinaryGrey;
import info.hb.lookup.object.common.RangeColor;
import info.hb.lookup.object.proc.NCC.WrongChannelType;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LookupColor {

	//
	// lookup
	//

	static boolean find(BufferedImage bi, int x, int y, BufferedImage icon, float m) {
		for (int yy = 0; yy < icon.getHeight(); yy++) {
			for (int xx = 0; xx < icon.getWidth(); xx++) {
				int rgb1 = icon.getRGB(xx, yy);
				int rgb2 = bi.getRGB(x + xx, y + yy);
				RangeColor r = new RangeColor(rgb1, m);
				if (!r.inRange(rgb2))
					return false;
			}
		}

		return true;
	}

	public static boolean find(ImageBinary image, int x, int y, ImageBinary template, double m) {
		m = m * 255;
		for (int yy = 0; yy < template.getHeight(); yy++) {
			for (int xx = 0; xx < template.getWidth(); xx++) {
				List<ImageBinaryChannel> ci = image.getChannels();
				List<ImageBinaryChannel> ct = template.getChannels();

				int ii = Math.min(ci.size(), ct.size());

				for (int i = 0; i < ii; i++) {
					ImageBinaryChannel cct = ct.get(i);
					ImageBinaryChannel cci = ci.get(i);

					if (!cct.type.equals(cci.type))
						throw new WrongChannelType();

					double rgb1 = cct.zeroMean.s(xx, yy);
					double rgb2 = cci.zeroMean.s(x + xx, y + yy);
					double min = rgb1 - m;
					double max = rgb1 + m;
					if (rgb2 < min || rgb2 > max)
						return false;
				}
			}
		}

		return true;
	}

	public static Point lookup(BufferedImage bi, BufferedImage icon) {
		return lookupUL(bi, icon, 0.10f);
	}

	public static Point lookupUL(BufferedImage image, BufferedImage template, float m) {
		return lookupUL(image, template, 0, 0, image.getWidth() - 1, image.getHeight() - 1, m);
	}

	public static Point lookupUL(BufferedImage image, BufferedImage template, int x1, int y1, int x2, int y2, float m) {
		for (int y = y1; y < y2 - template.getHeight(); y++) {
			for (int x = x1; x < x2 - template.getWidth(); x++) {
				if (find(image, x, y, template, m))
					return new Point(x, y);
			}
		}

		return null;
	}

	public static Point lookupUL(ImageBinaryGrey image, ImageBinaryGrey template, float m) {
		for (int y = 0; y < image.getHeight() - template.getHeight(); y++) {
			for (int x = 0; x < image.getWidth() - template.getWidth(); x++) {
				if (find(image, x, y, template, m))
					return new Point(x, y);
			}
		}

		return null;
	}

	public static List<Point> lookupAllUL(BufferedImage image, BufferedImage template, float m) {
		List<Point> list = new ArrayList<>();

		for (int y = 0; y < image.getHeight() - template.getHeight(); y++) {
			for (int x = 0; x < image.getWidth() - template.getWidth(); x++) {
				if (find(image, x, y, template, m))
					list.add(new Point(x, y));
			}
		}

		return list;
	}

	/**
	 * lookup center of image
	 *
	 * @param bi
	 * @param exit
	 * @param m
	 * @return
	 */
	static public Point lookup(BufferedImage bi, BufferedImage exit, float m) {
		return lookup(bi, exit, 0, 0, bi.getWidth() - 1, bi.getHeight() - 1, m);
	}

	static public Point lookup(BufferedImage bi, BufferedImage exit, int x1, int y1, int x2, int y2, float m) {
		Point pul = lookupUL(bi, exit, x1, y1, x2, y2, m);
		if (pul == null)
			throw new NotFound();

		int x = pul.x + exit.getWidth() / 2;
		int y = pul.y + exit.getHeight() / 2;
		return new Point(x, y);
	}

	static public Point lookupMeanImage(ImageBinaryGrey bi, ImageBinaryGrey i, int x1, int y1, int x2, int y2, float p) {
		Point pul = lookupUL(bi, i, p);
		if (pul == null)
			throw new NotFound();

		int x = pul.x + i.getWidth() / 2;
		int y = pul.y + i.getHeight() / 2;
		return new Point(x, y);
	}

	static public List<Point> lookupAll(BufferedImage bi, BufferedImage i) {
		return lookupAll(bi, i, 0.10f);
	}

	static public List<Point> lookupAll(BufferedImage bi, BufferedImage i, float p) {
		return lookupAll(bi, i, 0, 0, bi.getWidth(), bi.getHeight(), p);
	}

	static public List<Point> lookupAll(BufferedImage bi, BufferedImage i, int x1, int y1, int x2, int y2, float p) {
		List<Point> pul = lookupAllUL(bi, i, p);
		if (pul.size() == 0)
			throw new NotFound();

		for (Point pp : pul) {
			pp.x += i.getWidth() / 2;
			pp.y += i.getHeight() / 2;
		}

		return pul;
	}

}
