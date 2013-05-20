package jp.co.systembase.barcode;

import static java.lang.Math.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ITF extends Barcode {

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

	private static final int DPI = 72;

	public List<Integer[]> encode(String data) {
		if (data == null || data.length() == 0) {
			return null;
		}

		validate(data);

		final String _data = _encode(data);

		List<Integer[]> ret = new ArrayList<Integer[]>();
		ret.add(START_PATTERN);
		for (int i = 0; i < _data.length(); i = i + 2) {
			final char c1 = _data.charAt(i);
			final char c2 = _data.charAt(i + 1);
			final Integer[] code1 = CODE_PATTERNS.get(c1);
			final Integer[] code2 = CODE_PATTERNS.get(c2);

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
			if (CODE_PATTERNS.get(c) == null) {
				throw new IllegalArgumentException("illegal data: " + data);
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
		final float marginX = pointToPixel(dpi, super.marginX);
		final float marginY = pointToPixel(dpi, super.marginY);

		final float shortBarWidth = mmToPixel(dpi, 1.016f);
		final float longBarWidth = mmToPixel(dpi, 1.016f * 2.5f);

		float width = marginX;
		List<Integer[]> codes = encode(data);
		for (Integer[] code: codes) {
			for (int c: code) {
				width += c == 0 ? shortBarWidth : longBarWidth;
			}
		}

		final float h = pointToPixel(dpi, r.height) - marginY * 2;
		final float barHeight = withText ? h * 0.7f : h;
		final float height = barHeight + marginY;

		final float w = pointToPixel(dpi, r.width) - marginX * 2;
		if (w <= 0 || h <= 0) {
			return null;
		}

		BarContent ret = new BarContent();
		float xPos = 0;
		float scale = w / width;
		for (Integer[] code: codes) {
			for (int i = 0; i < code.length; i++) {
				final int c = code[i];
				float barWidth = c == 0 ? shortBarWidth : longBarWidth;
				barWidth *= scale;
				if (i % 2 == 0) {
					BarContent bc = new BarContent();
					BarContent.Bar b = bc.new Bar(r.x + xPos + marginX, r.y + marginY, barWidth, barHeight);
					ret.add(b);
				}
				xPos += barWidth;
			}
		}

		if (withText) {
			final String _data = _encode(data);

			final float textHeight = h * 0.2f;
			final float textWidth = ((w * 0.9f) / _data.length()) * 2.0f;
			final float fs = max(min(textHeight, textWidth), 6.0f);
			final Font f = new Font("SansSerif", Font.PLAIN, round(fs));

			FontMetrics fm = g.getFontMetrics(f);
	        Rectangle _r = fm.getStringBounds(_data, g).getBounds();
	        final int _xPos = round(w - _r.width) / 2;
	        final int _x = r.x + _xPos;
			final int _y = round(r.y + height + fs);
			BarContent bc = new BarContent();
			BarContent.Text t = bc.new Text(_data, f, _x, _y);
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
		final BarContent c = createContent(g, r, dpi, data);
		if (c == null) {
			return;
		}

		g.setColor(Color.BLACK);
		final Graphics2D g2d = (Graphics2D)g;
		for (BarContent.Bar b: c.getBars()) {
			Rectangle2D.Float s = new Rectangle2D.Float(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			g2d.fill(s);
		}

		final BarContent.Text t = c.getText();
		if (t != null) {
			g.setFont(t.getFont());
			g.drawString(t.getCode(), t.getX(), t.getY());
		}
	}
}
