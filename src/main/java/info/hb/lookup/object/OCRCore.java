package info.hb.lookup.object;

import info.hb.lookup.object.common.FontFamily;
import info.hb.lookup.object.common.FontSymbol;
import info.hb.lookup.object.common.FontSymbolLookup;
import info.hb.lookup.object.common.GPoint;
import info.hb.lookup.object.common.ImageBinary;
import info.hb.lookup.object.common.ImageBinaryGrey;
import info.hb.lookup.object.common.LessCompare;
import info.hb.lookup.object.proc.CannyEdgeDetector;
import info.hb.lookup.object.proc.NCC;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OCRCore {

	static public class BiggerFirst implements Comparator<FontSymbolLookup> {

		public int maxSize;
		public int maxSize2;

		public BiggerFirst(List<FontSymbolLookup> list) {
			maxSize = 0;

			for (FontSymbolLookup l : list) {
				maxSize = Math.max(maxSize, l.size());
			}

			maxSize2 = maxSize / 2;
		}

		@Override
		public int compare(FontSymbolLookup arg0, FontSymbolLookup arg1) {
			int r = LessCompare.compareBigFirst(arg0.size(), arg1.size(), maxSize2);

			// better quality goes first
			if (r == 0)
				r = LessCompare.compareBigFirst(arg0.g, arg1.g);

			// bigger items goes first
			if (r == 0)
				r = LessCompare.compareBigFirst(arg0.size(), arg1.size());

			return r;
		}

	}

	static public class Left2Right implements Comparator<FontSymbolLookup> {

		@Override
		public int compare(FontSymbolLookup arg0, FontSymbolLookup arg1) {
			int r = 0;

			if (r == 0) {
				if (!arg0.yCross(arg1))
					r = LessCompare.compareSmallFirst(arg0.y, arg1.y);
			}

			if (r == 0)
				r = LessCompare.compareSmallFirst(arg0.x, arg1.x);

			if (r == 0)
				r = LessCompare.compareSmallFirst(arg0.y, arg1.y);

			return r;
		}

	}

	public Map<String, FontFamily> fontFamily = new HashMap<>();

	public CannyEdgeDetector detector = new CannyEdgeDetector();

	// 1.0f == exact match, -1.0f - completely different images
	public float threshold = 0.70f;

	public OCRCore(float threshold) {
		this.threshold = threshold;

		detector.setLowThreshold(3f);
		detector.setHighThreshold(3f);
		detector.setGaussianKernelWidth(2);
		detector.setGaussianKernelRadius(1f);
	}

	public List<FontSymbol> getSymbols() {
		List<FontSymbol> list = new ArrayList<>();

		for (FontFamily f : fontFamily.values()) {
			list.addAll(f);
		}

		return list;
	}

	public List<FontSymbol> getSymbols(String fontFamily) {
		return this.fontFamily.get(fontFamily);
	}

	public List<FontSymbolLookup> findAll(List<FontSymbol> list, ImageBinaryGrey bi) {
		return findAll(list, bi, 0, 0, bi.getWidth(), bi.getHeight());
	}

	public List<FontSymbolLookup> findAll(List<FontSymbol> list, ImageBinary bi, int x1, int y1, int x2, int y2) {
		List<FontSymbolLookup> l = new ArrayList<>();

		for (FontSymbol fs : list) {
			for (ImageBinary imageScaleBin : fs.image.scales) {
				List<GPoint> ll = NCC.lookupAll(bi, x1, y1, x2, y2, imageScaleBin, threshold);
				for (GPoint p : ll)
					l.add(new FontSymbolLookup(fs, p.x, p.y, p.g));
			}
		}

		return l;
	}

}
