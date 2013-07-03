package jp.co.systembase.barcode;

import java.awt.Font;

public class Barcode {

	public static float barWidth = 1.0f;

	public float marginX = 2.0f;
	public float marginY = 2.0f;

	public boolean withText = true;

	public float fontSize(String txt, float w, float h) {
		float fs = h * 0.2f;
		fs = Math.min(fs, ((w * 0.9f) / txt.length()) * 2.0f);
		fs = Math.max(fs, 6.0f);
		return fs;
	}

	public Font getFont(String txt, float w, float h){
		return new Font("SansSerif", Font.PLAIN, (int)fontSize(txt, w, h));
	}

}
