package info.hb.lookup.object.common;

/**
 * Multiply matrix pixel by pixel
 */
public class ImageMultiply extends SArray {

	public SArray m;
	public int xx;
	public int yy;

	public ImageMultiply() {
		//
	}

	public ImageMultiply(SArray s1, SArray s2) {
		this(s1, 0, 0, s2);
	}

	public ImageMultiply(SArray image, int xx, int yy, SArray template) {
		init(image, xx, yy, template);

		for (int x = 0; x < cx; x++) {
			for (int y = 0; y < cy; y++) {
				step(x, y);
			}
		}
	}

	public void init(SArray image, int xx, int yy, SArray template) {
		super.initBase(template);

		this.m = image;
		this.xx = xx;
		this.yy = yy;
	}

	public void step(int x, int y) {
		s(x, y, m.s(xx + x, yy + y) * base.s(x, y));
	}

}
