package info.hb.lookup.object.common;

import info.hb.lookup.object.Capture;
import info.hb.lookup.object.Lookup;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class ImageBinaryScale {

	// original image
	public ImageBinary image;

	/**
	 * for template we need several scales.
	 *
	 * due to gliches druing scale or big original images we may get disproportional scaled image. big or small objects
	 * may be +1 pixed wider or toller.
	 *
	 * scales: normal scale, scale +1x, scale +1y, scale +1x+1y
	 */
	public List<ImageBinary> scales = new ArrayList<>();

	// scale 0.5f for 50%
	public double s = 0;
	// blur kernel size
	public int k = 0;

	public void rescale(int s, int k) {
		rescale(project(s), k);
	}

	public double project(int s) {
		double m = Math.min(image.getWidth(), image.getHeight());
		double q = m / s;

		q = Math.ceil(q);

		q = 1 / q;

		return q;
	}

	/**
	 * create one resacle image
	 *
	 * @param s
	 * @param k
	 */
	public void rescale(double s, int k) {
		scales.clear();

		this.s = s;
		this.k = k;

		scales.add(rescale(Lookup.scale(image.getImage(), s, k)));
	}

	/**
	 * create compensated images, 0,0 +1,0 0,+1 +1,+1
	 *
	 * @param s
	 * @param k
	 */
	public void rescaleMosaic(double s, int k) {
		scales.clear();

		this.s = s;
		this.k = k;

		scales.add(rescaleCrop(Lookup.scale(image.getImage(), s, k)));
		// scales.add(rescaleCrop(Lookup.scale(image.getImage(), s, k, +1, 0)));
		// scales.add(rescaleCrop(Lookup.scale(image.getImage(), s, k, 0, +1)));
		scales.add(rescaleCrop(Lookup.scale(image.getImage(), s, k, +1, +1)));

	}

	ImageBinary rescaleCrop(BufferedImage i) {
		if (i.getHeight() < 3 || i.getWidth() < 3)
			return rescale(i);
		else
			return rescale(Capture.crop(i, 1));
	}

	abstract public ImageBinary rescale(BufferedImage i);

}
