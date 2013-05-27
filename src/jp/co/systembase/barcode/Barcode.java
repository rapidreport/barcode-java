package jp.co.systembase.barcode;

import static java.lang.Math.*;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Barcode {

	public static float barWidth = 1.0f;

    public float marginX = 2.0f;
    public float marginY = 2.0f;

    public boolean withText = true;

    public static int fontSize(float width, float heigth, String data) {
    	float _width = ((width * 0.9f) / data.length()) * 2.0f;
    	float _heigth = heigth * 0.2f;
		int fs = round(max(min(_width, _heigth), 6.0f));
		return fs;
    }

    public static int centerAlign(Font font, Graphics g, float width, String data) {
    	FontMetrics fm = g.getFontMetrics(font);
        Rectangle r = fm.getStringBounds(data, g).getBounds();
        int x = (round(width) - r.width) / 2;
        return x;
    }
}
