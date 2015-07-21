package info.hb.lookup.object;

import info.hb.lookup.object.common.GPoint;
import info.hb.lookup.object.common.ImageBinaryGrey;
import info.hb.lookup.object.common.ImageBinaryRGB;
import info.hb.lookup.object.proc.NCC;

import java.awt.image.BufferedImage;
import java.util.List;

public class NCCTest {

	public static void main(String[] args) {
		BufferedImage image = Capture.load(OCRTest.class, "cyclopst1.png");
		BufferedImage template = Capture.load(OCRTest.class, "cyclopst3.png");

		// rgb image lookup
		{
			List<GPoint> pp = NCC.lookupAll(new ImageBinaryRGB(image), new ImageBinaryRGB(template), 0.9f);

			for (GPoint p : pp) {
				System.out.println(p);
			}
		}

		// grey image lookup
		{
			List<GPoint> pp = NCC.lookupAll(new ImageBinaryGrey(image), new ImageBinaryGrey(template), 0.9f);

			for (GPoint p : pp) {
				System.out.println(p);
			}
		}
	}

}
