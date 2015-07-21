package info.hb.tesseract.example;

import java.io.File;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class TesseractDemo {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Tesseract instance = Tesseract.getInstance();

		try {
			System.out.println(instance.doOCR(new File("image/test1.jpg")));
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}

	}

}
