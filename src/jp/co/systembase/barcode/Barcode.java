package jp.co.systembase.barcode;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Barcode {

	public static float barWidth = 1.0f;

    public float marginX = 2.0f;
    public float marginY = 2.0f;

    public boolean withText = true;

    public static float pointToPixel(int dpi, float point) {
		return dpi * (point / 72.0f);
	}

	public static float mmToPixel(int dpi, float mm) {
		return dpi * (mm / 25.4f);
	}

	public class BarContent {

		private List<Bar> _bars = new ArrayList<Bar>();
		private Text _text;

		public List<Bar> getBars() {
			return _bars;
		}

		public Text getText() {
			return _text;
		}

		public void seBars(List<Bar> bars) {
			_bars = bars;
		}

		public void setText(Text text) {
			_text = text;
		}

		public void add(Bar bar) {
			_bars.add(bar);
		}

		public class Bar {

			private float _x = 0.0f;
			private float _y = 0.0f;
			private float _width = 0.0f;
			private float _height = 0.0f;

			public Bar(float x, float y, float width, float height) {
				_x = x;
				_y = y;
				_width = width;
				_height = height;
			}

			public Bar(Rectangle2D.Float r) {
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

			public Text(String code, Font font, int x, int y) {
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
}
