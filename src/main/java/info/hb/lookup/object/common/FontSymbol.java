package info.hb.lookup.object.common;

import java.awt.image.BufferedImage;

public class FontSymbol {

	public FontFamily fontFamily;
	public String fontSymbol;
	public ImageBinaryScale image;

	public FontSymbol(FontFamily ff, String fs, BufferedImage i) {
		this.fontFamily = ff;
		this.fontSymbol = fs;
		this.image = new ImageBinaryGreyScale(i);
	}

	public int getHeight() {
		return image.image.getHeight();
	}

	public int getWidth() {
		return image.image.getWidth();
	}

	@Override
	public String toString() {
		return fontSymbol;
	}

}
