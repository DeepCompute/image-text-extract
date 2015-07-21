package info.hb.lookup.object;

import info.hb.lookup.object.common.SArray;

public class SArrayTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		int[] s = new int[] {

		5, 2, 5, 2,

		3, 6, 3, 6,

		5, 2, 5, 2,

		3, 6, 3, 6
		//
		};

		SArray a = new SArray();
		a.cx = 4;
		a.cy = 4;
		a.s = new double[a.cx * a.cy];

		for (int y = 0; y < a.cy; y++) {
			for (int x = 0; x < a.cx; x++) {
				a.s(x, y, a.s(x, y) + a.s(x - 1, y) + a.s(x, y - 1) - a.s(x - 1, y - 1));
			}
		}

		boolean t = a.sigma() == 64;
	}

}
