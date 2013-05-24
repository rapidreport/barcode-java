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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gs1128 extends Barcode {

	private static final byte CODE_PATTERNS[][] =
		{{2, 1, 2, 2, 2, 2},
		 {2, 2, 2, 1, 2, 2},
		 {2, 2, 2, 2, 2, 1},
		 {1, 2, 1, 2, 2, 3},
		 {1, 2, 1, 3, 2, 2},
		 {1, 3, 1, 2, 2, 2},
		 {1, 2, 2, 2, 1, 3},
		 {1, 2, 2, 3, 1, 2},
		 {1, 3, 2, 2, 1, 2},
		 {2, 2, 1, 2, 1, 3},
		 {2, 2, 1, 3, 1, 2},
		 {2, 3, 1, 2, 1, 2},
		 {1, 1, 2, 2, 3, 2},
		 {1, 2, 2, 1, 3, 2},
		 {1, 2, 2, 2, 3, 1},
		 {1, 1, 3, 2, 2, 2},
		 {1, 2, 3, 1, 2, 2},
		 {1, 2, 3, 2, 2, 1},
		 {2, 2, 3, 2, 1, 1},
		 {2, 2, 1, 1, 3, 2},
		 {2, 2, 1, 2, 3, 1},
		 {2, 1, 3, 2, 1, 2},
		 {2, 2, 3, 1, 1, 2},
		 {3, 1, 2, 1, 3, 1},
		 {3, 1, 1, 2, 2, 2},
		 {3, 2, 1, 1, 2, 2},
		 {3, 2, 1, 2, 2, 1},
		 {3, 1, 2, 2, 1, 2},
		 {3, 2, 2, 1, 1, 2},
		 {3, 2, 2, 2, 1, 1},
		 {2, 1, 2, 1, 2, 3},
		 {2, 1, 2, 3, 2, 1},
		 {2, 3, 2, 1, 2, 1},
		 {1, 1, 1, 3, 2, 3},
		 {1, 3, 1, 1, 2, 3},
		 {1, 3, 1, 3, 2, 1},
		 {1, 1, 2, 3, 1, 3},
		 {1, 3, 2, 1, 1, 3},
		 {1, 3, 2, 3, 1, 1},
		 {2, 1, 1, 3, 1, 3},
		 {2, 3, 1, 1, 1, 3},
		 {2, 3, 1, 3, 1, 1},
		 {1, 1, 2, 1, 3, 3},
		 {1, 1, 2, 3, 3, 1},
		 {1, 3, 2, 1, 3, 1},
		 {1, 1, 3, 1, 2, 3},
		 {1, 1, 3, 3, 2, 1},
		 {1, 3, 3, 1, 2, 1},
		 {3, 1, 3, 1, 2, 1},
		 {2, 1, 1, 3, 3, 1},
		 {2, 3, 1, 1, 3, 1},
		 {2, 1, 3, 1, 1, 3},
		 {2, 1, 3, 3, 1, 1},
		 {2, 1, 3, 1, 3, 1},
		 {3, 1, 1, 1, 2, 3},
		 {3, 1, 1, 3, 2, 1},
		 {3, 3, 1, 1, 2, 1},
		 {3, 1, 2, 1, 1, 3},
		 {3, 1, 2, 3, 1, 1},
		 {3, 3, 2, 1, 1, 1},
		 {3, 1, 4, 1, 1, 1},
		 {2, 2, 1, 4, 1, 1},
		 {4, 3, 1, 1, 1, 1},
		 {1, 1, 1, 2, 2, 4},
		 {1, 1, 1, 4, 2, 2},
		 {1, 2, 1, 1, 2, 4},
		 {1, 2, 1, 4, 2, 1},
		 {1, 4, 1, 1, 2, 2},
		 {1, 4, 1, 2, 2, 1},
		 {1, 1, 2, 2, 1, 4},
		 {1, 1, 2, 4, 1, 2},
		 {1, 2, 2, 1, 1, 4},
		 {1, 2, 2, 4, 1, 1},
		 {1, 4, 2, 1, 1, 2},
		 {1, 4, 2, 2, 1, 1},
		 {2, 4, 1, 2, 1, 1},
		 {2, 2, 1, 1, 1, 4},
		 {4, 1, 3, 1, 1, 1},
		 {2, 4, 1, 1, 1, 2},
		 {1, 3, 4, 1, 1, 1},
		 {1, 1, 1, 2, 4, 2},
		 {1, 2, 1, 1, 4, 2},
		 {1, 2, 1, 2, 4, 1},
		 {1, 1, 4, 2, 1, 2},
		 {1, 2, 4, 1, 1, 2},
		 {1, 2, 4, 2, 1, 1},
		 {4, 1, 1, 2, 1, 2},
		 {4, 2, 1, 1, 1, 2},
		 {4, 2, 1, 2, 1, 1},
		 {2, 1, 2, 1, 4, 1},
		 {2, 1, 4, 1, 2, 1},
		 {4, 1, 2, 1, 2, 1},
		 {1, 1, 1, 1, 4, 3},
		 {1, 1, 1, 3, 4, 1},
		 {1, 3, 1, 1, 4, 1},
		 {1, 1, 4, 1, 1, 3},
		 {1, 1, 4, 3, 1, 1},
		 {4, 1, 1, 1, 1, 3},
		 {4, 1, 1, 3, 1, 1},
		 {1, 1, 3, 1, 4, 1},
		 {1, 1, 4, 1, 3, 1},
		 {3, 1, 1, 1, 4, 1},
		 {4, 1, 1, 1, 3, 1},
		 {2, 1, 1, 4, 1, 2},
		 {2, 1, 1, 2, 1, 4},
		 {2, 1, 1, 2, 3, 2}};

	private static final byte[] STOP_PATTERN = {2, 3, 3, 1, 1, 1, 2};
	private static final int SET_C = 99;
	private static final int SET_B = 100;
	private static final int SET_A = 101;
	private static final int FNC1 = 102;
	private static final int START_A = 103;
	private static final int START_B = 104;
	private static final int START_C = 105;

	private static final String CHARS = "!\"%&'()*+,-./0123456789:;<=>?ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";

	private static enum CodeType {
		A(START_A, SET_A) {
			@Override
			public int getCodePoint(String data) {
				int c = data.charAt(0);
				if (c <= 0x1f){ // <= 'US'
					return c + 0x40; // + '@'
				}else{
					return c - 0x20; // - 'SP'
				}
			}
		},
		B(START_B, SET_B) {
			@Override
			public int getCodePoint(String data) {
				return data.charAt(0) - 0x20; // - 'SP'
			}
		},
		C(START_C, SET_C) {
			@Override
			public int getCodePoint(String data) {
				return Integer.parseInt(data.substring(0, 2));
			}
			@Override
			public String getNextData(String data) {
				return data.substring(2);
			}
		};

		private int _startCodePoint;

		private int _codeSetPoint;

		public abstract int getCodePoint(String data);

		CodeType(int startCodePoint, int codeSetPoint) {
	        _startCodePoint = startCodePoint;
	        _codeSetPoint = codeSetPoint;
	    }

	    public String getNextData(String data) {
	    	return data.substring(1);
	    }

	    public int getStartCodePoint() {
	    	return _startCodePoint;
	    }

	    public int getCodeSetPoint() {
	    	return _codeSetPoint;
	    }

		public static CodeType getCodeType(String data) {
			if (data.length() >= 2){
				if ('0' <= data.charAt(0) && data.charAt(0) <= '9' &&
					'0' <= data.charAt(1) && data.charAt(1) <= '9') {
					return C;
				}
			}
			if (data.charAt(0) <= 0x1f){ // <= US
				return A;
			}
			return B;
	    }
	}

	private static final int DPI = 72;

	private static final String AI_START_PATTERN = "#\\{";
	private static final String AI_NUMBER_PATTERN = "[0-9]{2,4}";
	private static final String AI_END_PATTERN = "\\}";
	private static final String AI_PATTERN = AI_START_PATTERN + AI_NUMBER_PATTERN + AI_END_PATTERN;

	private static final Map<String, Integer> FIXED_AI = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("00", 20);
			put("01", 16);
			put("02", 16);
			put("03", 16);
			put("04", 18);
			put("11", 8);
			put("12", 8);
			put("13", 8);
			put("14", 8);
			put("15", 8);
			put("16", 8);
			put("17", 8);
			put("18", 8);
			put("19", 8);
			put("20", 4);
			put("31", 10);
			put("32", 10);
			put("33", 10);
			put("34", 10);
			put("35", 10);
			put("36", 10);
			put("41", 16);
		}
	};

	private Map<String, Map<String, String>> encodeCache = new LinkedHashMap<String, Map<String,String>>();
	private static final int CACHE_MAX_SIZE = 10;

	public List<byte[]> encode(String data) {
		if (data == null || data.length() == 0) {
			return null;
		}

		validate(data);

		List<Integer> points = new ArrayList<Integer>();
		Map<String, String> codes = createCodeMap(data);
		CodeType type = CodeType.C;
		points.add(type.getStartCodePoint());
		points.add(FNC1);
		for (String key : codes.keySet()) {
			String _data = key + codes.get(key);
			while (_data.length() > 0) {
				CodeType _type = CodeType.getCodeType(_data);
				if (type != _type) {
					points.add(_type.getCodeSetPoint());
					type = _type;
				}
				points.add(_type.getCodePoint(_data));
				_data = _type.getNextData(_data);
			}
			String ai_2 = key.substring(0, 2);
			if (!FIXED_AI.containsKey(ai_2)) {
				points.add(FNC1);
			}
		}
		if (points.get(points.size() - 1) == FNC1) {
			points.remove(points.size() - 1);
		}
		points.add(calcCheckDigit(points));

		List<byte[]> ret = new ArrayList<byte[]>();
		for (int point: points) {
			ret.add(CODE_PATTERNS[point]);
		}
		ret.add(STOP_PATTERN);

		return ret;
	}

	protected String _encode(String data) {
		Map<String, String> codes = createCodeMap(data);
		StringBuilder sb = new StringBuilder("");
		for (String key: codes.keySet()) {
			sb.append("(" + key + ")" + codes.get(key));
		}
		return sb.toString();
	}

	private void validate(String data) {
		final String _data = data.replaceAll(AI_PATTERN, "");
		for (char c: _data.toCharArray()) {
			if (CHARS.indexOf(c) < 0) {
				throw new IllegalArgumentException("illegal data: " + data);
			}
		}
		if (!data.startsWith("#{") || data.endsWith("}")) {
			throw new IllegalArgumentException("illegal data: " + data);
		}
		Map<String, String> codes = createCodeMap(data);
		for (String key: codes.keySet()) {
			final String ai_2 = key.substring(0, 2);
			if (FIXED_AI.containsKey(ai_2)) {
				if (FIXED_AI.get(ai_2) != (key.length() + codes.get(key).length())) {
					throw new IllegalArgumentException("illegal ai length: (" + key + ")");
				}
			}
		}
	}

	protected Map<String, String> createCodeMap(String data) {
		if (encodeCache.containsKey(data)) {
			return encodeCache.get(data);
		}
		Map<String, String> ret = new LinkedHashMap<String, String>();
		final String codes[] = data.split(AI_PATTERN);
		final Pattern p = Pattern.compile(AI_START_PATTERN + "(" + AI_NUMBER_PATTERN + ")" + AI_END_PATTERN);
		final Matcher m = p.matcher(data);
		for (int i = 1; i < codes.length; i++) {
			m.find();
			ret.put(m.group(1), codes[i]);
		}
		if (encodeCache.size() == CACHE_MAX_SIZE) {
			String key = "";
			for (String _key: encodeCache.keySet()) {
				key = _key;
				break;
			}
			encodeCache.remove(key);
		}
		encodeCache.put(data, ret);
		return ret;
	}

	private int calcCheckDigit(List<Integer> points) {
		int sum = points.get(0);
		for(int i = 1; i < points.size(); i++) {
			sum += i * points.get(i);
		}
		final int checkNum = 103;
		return sum % checkNum;
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

		final float barWidth = mmToPixel(dpi, 0.191f);

		float width = 0;
		List<byte[]> codes = encode(data);
		for (byte[] code: codes) {
			for (byte c: code) {
				width += barWidth * c;
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
		float scale = (w - marginX) / width;
		for (byte[] code: codes) {
			for (int i = 0; i < code.length; i++) {
				final int c = code[i];
				float _barWidth = barWidth * c * scale;
				if (i % 2 == 0) {
					BarContent bc = new BarContent();
					BarContent.Bar b = bc.new Bar(r.x + xPos + marginX, r.y + marginY, _barWidth, barHeight);
					ret.add(b);
				}
				xPos += _barWidth;
			}
		}

		if (withText) {
			String _data = _encode(data);

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
