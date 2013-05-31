package jp.co.systembase.barcode.content;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class BarContent {

	private static final BarContent CONTENT = new BarContent();

	private List<Bar> _bars = new ArrayList<Bar>();
	private List<Text> _text = new ArrayList<Text>();

	public static Bar newBar(float x, float y, float width, float height) {
		return CONTENT.new Bar(x, y, width, height);
	}

	public static Bar newBar(Rectangle2D.Float r) {
		return CONTENT.new Bar(r);
	}

	public static Text newText(String code, Font font, int x, int y) {
		return CONTENT.new Text(code, font, x, y);
	}

	public List<Bar> getBars() {
		return _bars;
	}

	public List<Text> getText() {
		return _text;
	}

	public void setBars(List<Bar> bars) {
		_bars = bars;
	}

	public void setText(List<Text> text) {
		_text = text;
	}

	public void add(Bar bar) {
		_bars.add(bar);
	}

	public void add(Text text) {
		_text.add(text);
	}

	public void draw(Graphics g) {
		drawBars(g);
		drawText(g);
	}

	public void drawBars(Graphics g) {
		if (getBars() == null || getBars().isEmpty()) {
			return;
		}
		g.setColor(Color.BLACK);
		Graphics2D g2d = (Graphics2D)g;
		for (Bar b: getBars()) {
			Rectangle2D.Float s = new Rectangle2D.Float(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			g2d.fill(s);
		}
	}

	public void drawText(Graphics g) {
		if (getBars() == null || getBars().isEmpty()) {
			return;
		}
		if (getText() == null || getText().isEmpty()) {
			return;
		}
		g.setColor(Color.BLACK);
		for (Text t: getText()) {
			g.setFont(t.getFont());
			g.drawString(t.getCode(), t.getX(), t.getY());
		}
	}

	public class Bar {

		private float _x;
		private float _y;
		private float _width;
		private float _height;

		private Bar(float x, float y, float width, float height) {
			_x = x;
			_y = y;
			_width = width;
			_height = height;
		}

		private Bar(Rectangle2D.Float r) {
			_x = r.x;
			_y = r.y;
			_width = r.width;
			_height = r.height;
		}

		public float getX() {
			return _x;
		}

		public float getY() {
			return _y;
		}

		public float getWidth() {
			return _width;
		}

		public float getHeight() {
			return _height;
		}

		public void setX(float x) {
			_x = x;
		}

		public void setY(float y) {
			_y = y;
		}

		public void setWidth(float width) {
			_width = width;
		}

		public void setHeight(float height) {
			_height = height;
		}
	}

	public class Text {

		private String _code;
		private Font _font;
		private int _x;
		private int _y;

		private Text(String code, Font font, int x, int y) {
			_code = code;
			_font = font;
			_x = x;
			_y = y;
		}

		public String getCode() {
			return _code;
		}

		public Font getFont() {
			return _font;
		}

		public int getX() {
			return _x;
		}

		public int getY() {
			return _y;
		}

		public void setCode(String code) {
			_code = code;
		}

		public void setFont(Font font) {
			_font = font;
		}

		public void setX(int x) {
			_x = x;
		}

		public void setY(int y) {
			_y = y;
		}
	}
}
