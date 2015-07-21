package info.hb.lookup.object;

import info.hb.lookup.object.common.FontSymbol;
import info.hb.lookup.object.common.FontSymbolLookup;
import info.hb.lookup.object.common.ImageBinary;
import info.hb.lookup.object.common.ImageBinaryGreyScale;
import info.hb.lookup.object.common.ImageBinaryScale;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * For big images you may want scale booth image and template image similarly. (lets say booth reduce by 2x, you will
 * still have same recognitionz quality but it goes twice faster on recognition step)
 */
public class OCRScale extends OCR {

	public double s = 0;
	public int defaultBlurKernel;

	/**
	 * @param scaleSize
	 *            ex:5
	 *
	 * @param blurKernel
	 *            ex:10
	 */
	public OCRScale(float s, int blurKernel, float threshold) {
		super(threshold);
		this.s = s;
		this.defaultBlurKernel = blurKernel;
	}

	@Override
	public String recognize(BufferedImage bi) {
		ImageBinaryScale i = new ImageBinaryGreyScale(bi);

		return recognize(i);
	}

	public String recognize(ImageBinaryScale i) {
		List<FontSymbol> list = getSymbols();

		return recognize(i, 0, 0, i.image.getWidth() - 1, i.image.getHeight() - 1, list);
	}

	@Override
	public String recognize(BufferedImage bi, String fontSet) {
		ImageBinaryScale i = new ImageBinaryGreyScale(bi);

		return recognize(i, fontSet);
	}

	public String recognize(ImageBinaryScale i, String fontSet) {
		List<FontSymbol> list = getSymbols(fontSet);

		return recognize(i, 0, 0, i.image.getWidth() - 1, i.image.getHeight() - 1, list);
	}

	public String recognize(ImageBinaryScale i, int x1, int y1, int x2, int y2) {
		List<FontSymbol> list = getSymbols();

		return recognize(i, x1, y1, x2, y2, list);
	}

	public String recognize(ImageBinaryScale i, int x1, int y1, int x2, int y2, String fontFamily) {
		List<FontSymbol> list = getSymbols(fontFamily);

		return recognize(i, x1, y1, x2, y2, list);
	}

	public String recognize(ImageBinaryScale i, int x1, int y1, int x2, int y2, List<FontSymbol> list) {
		for (FontSymbol s : list) {
			scale(i, s.image);
		}

		// before this point we operating on original image pixels. after it, we are operating on scaled coords

		x1 *= s;
		y1 *= s;
		x2 *= s;
		y2 *= s;

		List<FontSymbolLookup> all = new ArrayList<>();

		for (ImageBinary iScaleBin : i.scales) {
			// rounding can be 1 pixels off images end
			if (x2 >= iScaleBin.getWidth())
				x2 = iScaleBin.getWidth() - 1;
			if (y2 >= iScaleBin.getHeight())
				y2 = iScaleBin.getHeight() - 1;

			all.addAll(findAll(list, iScaleBin, x1, y1, x2, y2));
		}

		return recognize(all);
	}

	public void scale(ImageBinaryScale image, ImageBinaryScale template) {
		if (s == 0) {
			s = template.s;
		}

		if (s != template.s) {
			template.rescaleMosaic(s, defaultBlurKernel);
		}

		if (s != image.s) {
			image.rescale(s, defaultBlurKernel);
		}
	}

}
