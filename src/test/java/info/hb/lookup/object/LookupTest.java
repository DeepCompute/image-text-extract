package info.hb.lookup.object;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

public class LookupTest {

	public static void main(String[] args) {
		BufferedImage image = Capture.load(OCRTest.class, "cyclopst1.png");
		BufferedImage template = Capture.load(OCRTest.class, "cyclopst3.png");

		List<Point> pp = LookupColor.lookupAll(image, template, 0.20f);

		for (Point p : pp) {
			System.out.println(p);
		}
	}

}
