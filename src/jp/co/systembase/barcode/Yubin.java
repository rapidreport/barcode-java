package jp.co.systembase.barcode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Yubin extends Barcode {

	private static final Byte[][] CODE_PATTERNS =
		{{1, 4, 4},
		{1, 1, 4},
		{1, 3, 2},
		{3, 1, 2},
		{1, 2, 3},
		{1, 4, 1},
		{3, 2, 1},
		{2, 1, 3},
		{2, 3, 1},
		{4, 1, 1},
		{4, 1, 4},
		{3, 2, 4},
		{3, 4, 2},
		{2, 3, 4},
		{4, 3, 2},
		{2, 4, 3},
		{4, 2, 3},
		{4, 4, 1},
		{1, 1, 1}};

	private static final Map<Character, Byte[]> CODE_POINTS = new HashMap<Character, Byte[]>() {
		private static final long serialVersionUID = -2375616882222293711L;
		{
			put('0', new Byte[]{0});
			put('1', new Byte[]{1});
			put('2', new Byte[]{2});
			put('3', new Byte[]{3});
			put('4', new Byte[]{4});
			put('5', new Byte[]{5});
			put('6', new Byte[]{6});
			put('7', new Byte[]{7});
			put('8', new Byte[]{8});
			put('9', new Byte[]{9});
			put('-', new Byte[]{10});
			put('A', new Byte[]{11, 0});
			put('B', new Byte[]{11, 1});
			put('C', new Byte[]{11, 2});
			put('D', new Byte[]{11, 3});
			put('E', new Byte[]{11, 4});
			put('F', new Byte[]{11, 5});
			put('G', new Byte[]{11, 6});
			put('H', new Byte[]{11, 7});
			put('I', new Byte[]{11, 8});
			put('J', new Byte[]{11, 9});
			put('K', new Byte[]{12, 0});
			put('L', new Byte[]{12, 1});
			put('M', new Byte[]{12, 2});
			put('N', new Byte[]{12, 3});
			put('O', new Byte[]{12, 4});
			put('P', new Byte[]{12, 5});
			put('Q', new Byte[]{12, 6});
			put('R', new Byte[]{12, 7});
			put('S', new Byte[]{12, 8});
			put('T', new Byte[]{12, 9});
			put('U', new Byte[]{13, 0});
			put('V', new Byte[]{13, 1});
			put('W', new Byte[]{13, 2});
			put('X', new Byte[]{13, 3});
			put('Y', new Byte[]{13, 4});
			put('Z', new Byte[]{13, 5});
		}
	};

	private static final Byte[] START_PATTERN = {1, 3};
	private static final Byte[] STOP_PATTERN = {3, 1};

	private static final int MAX_SIZE = 20;

	public List<Byte> encode(String data){
		if (data == null || data.length() == 0) {
			return null;
		}
		validate(data);
		List<Byte> ret = new ArrayList<Byte>();
		int sum_p = 0;
		int l = 0;
		ret.add(START_PATTERN[0]);
		ret.add(START_PATTERN[1]);
		for (char c: data.toCharArray()) {
			Byte[] cp = CODE_POINTS.get(c);
			if (l + cp.length > MAX_SIZE){
				break;
			}
			for(Byte p: cp){
				ret.add(CODE_PATTERNS[p][0]);
				ret.add(CODE_PATTERNS[p][1]);
				ret.add(CODE_PATTERNS[p][2]);
				sum_p += p;
			}
			l += cp.length;
		}
		while(l < MAX_SIZE){
			ret.add(CODE_PATTERNS[14][0]);
			ret.add(CODE_PATTERNS[14][1]);
			ret.add(CODE_PATTERNS[14][2]);
			sum_p += 14;
			l += 1;
		}
		{
			int cd = calcCheckDigit(sum_p);
			ret.add(CODE_PATTERNS[cd][0]);
			ret.add(CODE_PATTERNS[cd][1]);
			ret.add(CODE_PATTERNS[cd][2]);
		}
		ret.add(STOP_PATTERN[0]);
		ret.add(STOP_PATTERN[1]);
		return ret;
	}

	private void validate(String data) {
		for (char c: data.toCharArray()) {
			if (!CODE_POINTS.containsKey(c)) {
				throw new IllegalArgumentException("illegal char: " + c + " of data: " + data);
			}
		}
	}

	private int calcCheckDigit(int p){
		int checkNum = 19;
		int pos = checkNum - (p % checkNum);
		if (pos == checkNum) {
			pos = 0;
		}
		return pos;
	}

	public void render(Graphics g, int x, int y, int w, int h, String data){
		this.render(g, new Rectangle(x, y, w, h), data);
	}

	public void render(Graphics g, Rectangle r, String data){
		if (data == null || data.length() == 0){
			return;
		}
		float w = r.width - this.marginX * 2;
		if (w <= 0){
			return;
		}
		List<Byte> codes = this.encode(data);
		float uw = w / (codes.size() * 2);
		float x = r.x + this.marginX;
		float y = r.y + r.height / 2;
		g.setColor(Color.BLACK);
		for(Byte c: codes){
			float by = 0;
			float bh = 0;
			switch(c){
			case 1:
				by = y - uw * 3;
				bh = uw * 6;
				break;
			case 2:
				by = y - uw * 3;
				bh = uw * 4;
				break;
			case 3:
				by = y - uw;
				bh = uw * 4;
				break;
			case 4:
				by = y - uw;
				bh = uw * 2;
				break;
			}
			g.fillRect((int)x, (int)by, (int)uw, (int)bh);
			x += uw * 2;
		}
	}

}
