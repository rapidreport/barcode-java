package jp.co.systembase.barcode;

import static java.lang.Math.*;
import static jp.co.systembase.barcode.content.Scale.*;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.systembase.barcode.content.BarContent;
import jp.co.systembase.barcode.content.PointScale;
import jp.co.systembase.barcode.content.Scale;

public class Gs1_128 extends Barcode {

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

	public boolean conveniFormat = false;

	private static enum CodeType {
		A(START_A, SET_A) {
			@Override
			public int codePoint(String data) {
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
			public int codePoint(String data) {
				return data.charAt(0) - 0x20; // - 'SP'
			}
		},
		C(START_C, SET_C) {
			@Override
			public int codePoint(String data) {
				return Integer.parseInt(data.substring(0, 2));
			}
			@Override
			public String nextData(String data) {
				return data.substring(2);
			}
		};

		private int _startCodePoint;

		private int _codeSetPoint;

		public abstract int codePoint(String data);

		CodeType(int startCodePoint, int codeSetPoint) {
	        _startCodePoint = startCodePoint;
	        _codeSetPoint = codeSetPoint;
	    }

	    public String nextData(String data) {
	    	return data.substring(1);
	    }

	    public int getStartCodePoint() {
	    	return _startCodePoint;
	    }

	    public int getCodeSetPoint() {
	    	return _codeSetPoint;
	    }

		public static CodeType type(String data) {
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

	private static final String AI_START_PATTERN = "#\\{";
	private static final String AI_NUMBER_PATTERN = "[0-9]{2,4}";
	private static final String AI_END_PATTERN = "\\}";
	private static final String AI_PATTERN = AI_START_PATTERN + AI_NUMBER_PATTERN + AI_END_PATTERN;

	private static final String AI_CONVENIENCE = "91";

	private static final Map<String, Integer> FIXED_AI = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("00", 20); // ai number and data length
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
			put(AI_CONVENIENCE, 44);
		}
	};

	private class CodeMap {

		private String _ai;
		private String _data;

		public CodeMap(String ai, String data) {
			_ai = ai;
			_data = data;
		}

		public String getAi() {
			return _ai;
		}

		public String getData() {
			return _data;
		}
	}

	private Map<String, List<CodeMap>> codeMapCache = new LinkedHashMap<String, List<CodeMap>>();
	private static final int CACHE_MAX_SIZE = 10;

	public List<byte[]> encode(String data) {
		if (data == null || data.length() == 0) {
			return null;
		}

		validate(data);

		List<CodeMap> codes = createCodeMap(data);
		List<Integer> points = new ArrayList<Integer>();
		CodeType type = CodeType.C;
		points.add(type.getStartCodePoint());
		points.add(FNC1);
		for (CodeMap map : codes) {
			String _data = map.getAi() + map.getData();
			while (_data.length() > 0) {
				CodeType _type = CodeType.type(_data);
				if (type != _type) {
					points.add(_type.getCodeSetPoint());
					type = _type;
				}
				points.add(_type.codePoint(_data));
				_data = _type.nextData(_data);
			}
			String ai_2 = map.getAi().substring(0, 2);
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
		List<CodeMap> codes = createCodeMap(data);
		StringBuilder sb = new StringBuilder("");
		for (CodeMap map: codes) {
			sb.append("(" + map.getAi() + ")" + map.getData());
		}
		if (conveniFormat) {
			sb.insert(10, "-");
			sb.insert(33, " ");
			sb.insert(40, "-");
			sb.insert(42, "-");
			sb.insert(49, "-");
		}
		return sb.toString();
	}

	private void validate(String data) {
		String _data = data.replaceAll(AI_PATTERN, "");
		for (char c: _data.toCharArray()) {
			if (CHARS.indexOf(c) < 0) {
				throw new IllegalArgumentException("illegal char: " + c + " of data: " + data);
			}
		}

		if (!data.startsWith("#{")) {
			throw new IllegalArgumentException("illegal data: " + data + ", must start with \"#{\"");
		}
		if (data.endsWith("}")) {
			throw new IllegalArgumentException("illegal data: " + data + ", don't end with \"}\"");
		}

		List<CodeMap> codes = createCodeMap(data);
		for (CodeMap map: codes) {
			String ai_2 = map.getAi().substring(0, 2);
			if (conveniFormat && !AI_CONVENIENCE.equals(ai_2)) {
				throw new IllegalArgumentException("illegal ai: (" + map.getAi() + ")");
			}
			if (FIXED_AI.containsKey(ai_2)) {
				if (FIXED_AI.get(ai_2) != (map.getAi().length() + map.getData().length())) {
					throw new IllegalArgumentException("illegal ai data length: (" + map.getAi() + ")");
				}
			}
		}
	}

	protected List<CodeMap> createCodeMap(String data) {
		if (codeMapCache.containsKey(data)) {
			return codeMapCache.get(data);
		}

		List<CodeMap> ret = new ArrayList<CodeMap>();
		String codes[] = data.split(AI_PATTERN);
		Pattern p = Pattern.compile(AI_START_PATTERN + "(" + AI_NUMBER_PATTERN + ")" + AI_END_PATTERN);
		Matcher m = p.matcher(data);
		for (int i = 1; i < codes.length; i++) {
			m.find();
			CodeMap map = new CodeMap(m.group(1), codes[i]);
			ret.add(map);
		}

		if (codeMapCache.size() == CACHE_MAX_SIZE) {
			String key = "";
			for (String _key: codeMapCache.keySet()) {
				key = _key;
				break;
			}
			codeMapCache.remove(key);
		}
		codeMapCache.put(data, ret);
		return ret;
	}

	private int calcCheckDigit(List<Integer> points) {
		int sum = points.get(0);
		for(int i = 1; i < points.size(); i++) {
			sum += i * points.get(i);
		}
		int checkNum = 103;
		return sum % checkNum;
	}

	public BarContent createContent(Graphics g, int x, int y, int w, int h, String data) {
		return createContent(g, new Rectangle(x, y, w, h), data);
	}

	public BarContent createContent(Graphics g, int x, int y, int w, int h, int dpi, String data) {
		return createContent(g, new Rectangle(x, y, w, h), dpi, data);
	}

	public BarContent createContent(Graphics g, Rectangle r, String data) {
		return createContent(g, r, DPI, data);
	}

	public BarContent createContent(Graphics g, Rectangle r, int dpi, String data) {
		float barWidth = mmToPixel(dpi, 0.191f);
		if (conveniFormat) {
			switch (dpi) {
				case 300:
				case 600:
					barWidth = mmToPixel(dpi, 0.169f);
					break;
				case 400:
					barWidth = mmToPixel(dpi, 0.190f);
					break;
				case 480:
					barWidth = mmToPixel(dpi, 0.158f);
					break;
				default:
					break;
			}
		}

		float width = 0;
		List<byte[]> codes = encode(data);
		for (byte[] code: codes) {
			for (byte c: code) {
				width += barWidth * c;
			}
		}

		Scale scale = new PointScale(marginX, marginY, r.width, r.height, dpi);
		float h = scale.pixelHeight();
		float barHeight = h;
		if (withText) {
			if (conveniFormat) {
				barHeight = h * 0.5f;
			} else {
				barHeight = h * 0.7f;
			}
		}

		float w = scale.pixelWidth();
		if (w <= 0 || h <= 0) {
			return null;
		}

		BarContent ret = new BarContent();
		float xPos = 0;
		float _scale = w / width;
		for (byte[] code: codes) {
			for (int i = 0; i < code.length; i++) {
				int c = code[i];
				float _barWidth = barWidth * c * _scale;
				if (i % 2 == 0) {
					float x = r.x + xPos + scale.pixelMarginX();
					float y = r.y + scale.pixelMarginY();
					BarContent.Bar b = new BarContent.Bar(x, y, _barWidth, barHeight);
					ret.add(b);
				}
				xPos += _barWidth;
			}
		}

		if (withText) {
			String _data = _encode(data);
			String[] t = _data.split(" ");
			String baseText = t[0];

			int fs = fontSize(w, h, baseText);
			Font f = new Font("SansSerif", Font.PLAIN, fs);
			int x = r.x + centerAlign(f, g, w, baseText) + round(scale.pixelMarginX());
			int y = r.y + round(barHeight) + fs + round(scale.pixelMarginY());

			for (int i = 0; i < t.length; i++) {
				int _y = y + (fs * i);
				BarContent.Text _t = new BarContent.Text(t[i], f, x, _y);
				ret.add(_t);
			}
		}

		return ret;
	}

	public void render(Graphics g, int x, int y, int w, int h, String data) {
		render(g, new Rectangle(x, y, w, h), data);
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
