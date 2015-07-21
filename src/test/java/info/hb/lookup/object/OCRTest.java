package info.hb.lookup.object;

import info.hb.lookup.object.common.ImageBinaryGrey;

import java.io.File;

public class OCRTest {

	static public void main(String[] args) {
		OCR l = new OCR(0.70f);

		// will go to com/github/axet/lookup/fonts folder and load all font
		// familys (here is only font_1 family in this library)
		l.loadFontsDirectory(OCRTest.class, new File("fonts"));

		// example how to load only one family
		// "com/github/axet/lookup/fonts/font_1"
		l.loadFont(OCRTest.class, new File("fonts", "font_1"));

		String str = "";

		// recognize using all familys set
		str = l.recognize(Capture.load(OCRTest.class, "test3.png"));
		System.out.println(str);

		// recognize using only one family set
		str = l.recognize(Capture.load(OCRTest.class, "test3.png"), "font_1");
		System.out.println(str);

		// recognize using only one family set and rectangle
		ImageBinaryGrey i = new ImageBinaryGrey(Capture.load(OCRTest.class, "full.png"));
		str = l.recognize(i, 1285, 654, 1343, 677, "font_1");
		System.out.println(str);
	}

}
