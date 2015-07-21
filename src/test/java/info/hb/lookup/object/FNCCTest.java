package info.hb.lookup.object;

import info.hb.lookup.object.common.GPoint;
import info.hb.lookup.object.common.ImageBinaryGrey;
import info.hb.lookup.object.common.ImageBinaryRGB;
import info.hb.lookup.object.common.ImageBinaryRGBFeature;
import info.hb.lookup.object.proc.FNCC;

import java.awt.image.BufferedImage;
import java.util.List;

public class FNCCTest {

	public static void main(String[] args) {
		BufferedImage image = Capture.load(OCRTest.class, "cyclopst1.png");
		BufferedImage template = Capture.load(OCRTest.class, "cyclopst3.png");

		// rgb image lookup
		{
			List<GPoint> pp = FNCC
					.lookupAll(new ImageBinaryRGB(image), new ImageBinaryRGBFeature(template, 5000), 0.9f);

			for (GPoint p : pp) {
				System.out.println(p);
			}
		}

		// grey image lookup
		{
			List<GPoint> pp = FNCC.lookupAll(new ImageBinaryGrey(image), new ImageBinaryRGBFeature(template, 5000),
					0.9f);

			for (GPoint p : pp) {
				System.out.println(p);
			}
		}
	}

}
