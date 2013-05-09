package jp.co.systembase.barcode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YubinCustomer extends Barcode {

	private static final String[] CODE_CHARS =
		{"144",	// 0
		 "114",	// 1
		 "132",	// 2
		 "312",	// 3
		 "123",	// 4
		 "141",	// 5
		 "321",	// 6
		 "213",	// 7
		 "231",	// 8
		 "411",	// 9
		 "414",	// -
		 "324",	// CC1
		 "342",	// CC2
		 "234",	// CC3
		 "432",	// CC4
		 "243",	// CC5
		 "423",	// CC6
		 "441",	// CC7
		 "111"};// CC8

	private static final Map<Character, String[]> CODE_PATTERNS = new HashMap<Character, String[]>() {
		private static final long serialVersionUID = 1L;
		{
			put('1', new String[]{CODE_CHARS[1], ""});
			put('2', new String[]{CODE_CHARS[2], ""});
			put('3', new String[]{CODE_CHARS[3], ""});
			put('4', new String[]{CODE_CHARS[4], ""});
			put('5', new String[]{CODE_CHARS[5], ""});
			put('6', new String[]{CODE_CHARS[6], ""});
			put('7', new String[]{CODE_CHARS[7], ""});
			put('8', new String[]{CODE_CHARS[8], ""});
			put('9', new String[]{CODE_CHARS[9], ""});
			put('0', new String[]{CODE_CHARS[0], ""});
			put('-', new String[]{CODE_CHARS[10], ""});
			put('A', new String[]{CODE_CHARS[11], CODE_CHARS[0]});
			put('B', new String[]{CODE_CHARS[11], CODE_CHARS[1]});
			put('C', new String[]{CODE_CHARS[11], CODE_CHARS[2]});
			put('D', new String[]{CODE_CHARS[11], CODE_CHARS[3]});
			put('E', new String[]{CODE_CHARS[11], CODE_CHARS[4]});
			put('F', new String[]{CODE_CHARS[11], CODE_CHARS[5]});
			put('G', new String[]{CODE_CHARS[11], CODE_CHARS[6]});
			put('H', new String[]{CODE_CHARS[11], CODE_CHARS[7]});
			put('I', new String[]{CODE_CHARS[11], CODE_CHARS[8]});
			put('J', new String[]{CODE_CHARS[11], CODE_CHARS[9]});
			put('K', new String[]{CODE_CHARS[12], CODE_CHARS[0]});
			put('L', new String[]{CODE_CHARS[12], CODE_CHARS[1]});
			put('M', new String[]{CODE_CHARS[12], CODE_CHARS[2]});
			put('N', new String[]{CODE_CHARS[12], CODE_CHARS[3]});
			put('O', new String[]{CODE_CHARS[12], CODE_CHARS[4]});
			put('P', new String[]{CODE_CHARS[12], CODE_CHARS[5]});
			put('Q', new String[]{CODE_CHARS[12], CODE_CHARS[6]});
			put('R', new String[]{CODE_CHARS[12], CODE_CHARS[7]});
			put('S', new String[]{CODE_CHARS[12], CODE_CHARS[8]});
			put('T', new String[]{CODE_CHARS[12], CODE_CHARS[9]});
			put('U', new String[]{CODE_CHARS[13], CODE_CHARS[0]});
			put('V', new String[]{CODE_CHARS[13], CODE_CHARS[1]});
			put('W', new String[]{CODE_CHARS[13], CODE_CHARS[2]});
			put('X', new String[]{CODE_CHARS[13], CODE_CHARS[3]});
			put('Y', new String[]{CODE_CHARS[13], CODE_CHARS[4]});
			put('Z', new String[]{CODE_CHARS[13], CODE_CHARS[5]});
		}
	};

	private static final String START_PATTERN = "13";
	private static final String STOP_PATTERN = "31";

	private static final int CODE_MAX_SIZE = 20;

	private static final Map<String, Integer> CHECK_DIGIT_PATTERNS = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			for (int i = 0; i < CODE_CHARS.length; i++) {
				put(CODE_CHARS[i], i);
			}
		}
	};

	private static final int DPI = 72;

	public List<String> encode(String data){
		if (data == null || data.length() == 0) {
			return null;
		}

		validate(data);

		List<String> codes = new ArrayList<String>();
		for (char c: data.toCharArray()) {
			String[] s = CODE_PATTERNS.get(c);
			codes.add(s[0]);
			if (!s[1].equals("")) {
				codes.add(s[1]);
			}
		}

		for (int i = codes.size(); i <= CODE_MAX_SIZE; i++) {
			codes.add(CODE_CHARS[14]); // CC4
		}

		List<String> ret = codes.subList(0, CODE_MAX_SIZE);
		ret.add(calcCheckDigit(ret));
		ret.add(0, START_PATTERN);
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

	private String calcCheckDigit(List<String> codes){
		int sum = 0;
		for (String s: codes) {
			sum += CHECK_DIGIT_PATTERNS.get(s);
		}

		final int checkNum = 19;
		int pos = checkNum - (sum % checkNum);
		if (pos == checkNum) {
			pos = 0;
		}

		return CODE_CHARS[pos];
	}

	public void render(Graphics g, int x, int y, int w, int h, float point, String data) {
		render(g, x, y, w, h, point, DPI, data);
	}

	public void render(Graphics g, int x, int y, int w, int h, float point, int dpi, String data) {
		render(g, new Rectangle(x, y, w, h), point, dpi, data);
	}

	public void render(Graphics g, Rectangle r, float point, String data) {
		render(g, r, point, DPI, data);
	}

	public void render(Graphics g, Rectangle r, float point, int dpi, String data) {
		if (point < 8.0f || 11.5f < point) {
			throw new IllegalArgumentException("illegal point: " + point);
		}

		final float longBarHeight = mmToPixel(dpi, 3.6f * point / 10.0f);
		final float timingBarHeight = mmToPixel(dpi, 1.2f * point / 10.0f);
		final float semilongBarHeight = longBarHeight / 2.0f + timingBarHeight / 2.0f;
		final float barWidth = mmToPixel(dpi, 0.6f * point / 10.0f);
		final float barSpace = mmToPixel(dpi, 0.6f * point / 10.0f);

		float xPos = r.x + marginX * 2;
		final float yTop = r.y + marginY * 2;
		final float xMax = r.x + r.width - marginX * 2;
		final float yMax = r.y + r.height - marginY * 2;

		final Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		for (String code: encode(data)) {
			for (char c: code.toCharArray()) {
				float yPos = yTop;
				float barHeight = 0.0f;
				switch (c) {
					case '1':
						barHeight = longBarHeight;
						break;
					case '2':
						barHeight = semilongBarHeight;
						break;
					case '3':
						yPos = yTop + longBarHeight - semilongBarHeight;
						barHeight = semilongBarHeight;
						break;
					case '4':
						yPos = yTop + longBarHeight - semilongBarHeight;
						barHeight = timingBarHeight;
						break;
					default:
						throw new IllegalArgumentException("illegal switch case: " + c);
				}

				final float x = xPos + marginX;
				final float y = yPos + marginY;
				if (x > xMax || y > yMax) {
					continue;
				}

				if (y + barHeight > yMax) {
					barHeight = yMax - y;
				}

				Rectangle2D.Float s = new Rectangle2D.Float(x, y, barWidth, barHeight);
				g2d.fill(s);
				xPos = xPos + barWidth + barSpace;
			}
		}
	}

	private float mmToPixel(int dpi, float mm) {
		return dpi * (mm / 25.4f);
	}
}
