package jp.co.systembase.barcode;

import static java.lang.Math.*;
import static jp.co.systembase.barcode.content.Scale.*;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.barcode.content.BarContent;
import jp.co.systembase.barcode.content.PointScale;
import jp.co.systembase.barcode.content.Scale;

public class Itf extends Barcode {

	private static final Map<Character, Integer[]> CODE_PATTERNS = new HashMap<Character, Integer[]>() {
		private static final long serialVersionUID = 1L;
		{
			put('0', new Integer[]{0, 0, 1, 1, 0});
			put('1', new Integer[]{1, 0, 0, 0, 1});
			put('2', new Integer[]{0, 1, 0, 0, 1});
			put('3', new Integer[]{1, 1, 0, 0, 0});
			put('4', new Integer[]{0, 0, 1, 0, 1});
			put('5', new Integer[]{1, 0, 1, 0, 0});
			put('6', new Integer[]{0, 1, 1, 0, 0});
			put('7', new Integer[]{0, 0, 0, 1, 1});
			put('8', new Integer[]{1, 0, 0, 1, 0});
			put('9', new Integer[]{0, 1, 0, 1, 0});
		}
	};

	private static final Integer[] START_PATTERN = {0, 0, 0, 0};
	private static final Integer[] STOP_PATTERN = {1, 0, 0};

	public List<Integer[]> encode(String data) {
		if (data == null || data.length() == 0) {
			return null;
		}

		validate(data);

		String _data = _encode(data);

		List<Integer[]> ret = new ArrayList<Integer[]>();
		ret.add(START_PATTERN);
		for (int i = 0; i < _data.length(); i = i + 2) {
			char c1 = _data.charAt(i);
			char c2 = _data.charAt(i + 1);
			Integer[] code1 = CODE_PATTERNS.get(c1);
			Integer[] code2 = CODE_PATTERNS.get(c2);

			if (code1.length != code2.length) {
				throw new IllegalArgumentException("illegal pattern length: " + c1 + " != " + c2);
			}

			List<Integer> code = new ArrayList<Integer>();
			for (int t = 0; t < code1.length; t++) {
				code.add(code1[t]);
				code.add(code2[t]);
			}
			ret.add(code.toArray(new Integer[0]));
		}
		ret.add(STOP_PATTERN);

		return ret;
	}

	private void validate(String data) {
		for (char c: data.toCharArray()) {
			if (!CODE_PATTERNS.containsKey(c)) {
				throw new IllegalArgumentException("illegal char: " + c + " of data: " + data);
			}
		}
	}

	protected String _encode(String data) {
		StringBuilder sb = new StringBuilder(data);
		if (sb.length() % 2 != 0) {
			sb.insert(0, "0");
		}
		return sb.toString();
	}

	public BarContent createContent(Graphics g, int x, int y, int w, int h, String data) {
		return createContent(g, x, y, w, h, DPI, data);
	}

	public BarContent createContent(Graphics g, int x, int y, int w, int h, int dpi, String data) {
		return createContent(g, new Rectangle(x, y, w, h), dpi, data);
	}

	public BarContent createContent(Graphics g, Rectangle r, String data) {
		return createContent(g, r, DPI, data);
	}

	public BarContent createContent(Graphics g, Rectangle r, int dpi, String data) {
		float shortBarWidth = mmToPixel(dpi, 1.016f);
		float longBarWidth = mmToPixel(dpi, 1.016f * 2.5f);

		float width = 0;
		List<Integer[]> codes = encode(data);
		for (Integer[] code: codes) {
			for (int c: code) {
				width += c == 0 ? shortBarWidth : longBarWidth;
			}
		}

		Scale scale = new PointScale(marginX, marginY, r.width, r.height, dpi);
		float h = scale.pixelHeight();
		float barHeight = withText ? h * 0.7f : h;
		float height = barHeight + scale.pixelMarginY();

		float w = scale.pixelWidth();
		if (w <= 0 || h <= 0) {
			return null;
		}

		BarContent ret = new BarContent();
		float xPos = 0;
		float _scale = (w - scale.pixelMarginX()) / width;
		for (Integer[] code: codes) {
			for (int i = 0; i < code.length; i++) {
				int c = code[i];
				float barWidth = c == 0 ? shortBarWidth : longBarWidth;
				barWidth *= _scale;
				if (i % 2 == 0) {
					float x = r.x + xPos + scale.pixelMarginX();
					float y = r.y + scale.pixelMarginY();
					BarContent.Bar b = BarContent.newBar(x, y, barWidth, barHeight);
					ret.add(b);
				}
				xPos += barWidth;
			}
		}

		if (withText) {
			String _data = _encode(data);

			int fs = fontSize(w, h, _data);
			Font f = new Font("SansSerif", Font.PLAIN, fs);
	        int x = r.x + centerAlign(f, g, w, _data);
			int y = r.y + round(height) + fs;

			BarContent.Text t = BarContent.newText(_data, f, x, y);
			ret.setText(t);
		}

		return ret;
	}

	public void render(Graphics g, int x, int y, int w, int h, String data) {
		render(g, x, y, w, h, DPI, data);
	}

	public void render(Graphics g, int x, int y, int w, int h, int dpi, String data) {
		render(g, new Rectangle(x, y, w, h), dpi, data);
	}

	public void render(Graphics g, Rectangle r, String data) {
		render(g, r, DPI, data);
	}

	public void render(Graphics g, Rectangle r, int dpi, String data) {
		BarContent c = createContent(g, r, dpi, data);
		c.draw(g);
	}
}
