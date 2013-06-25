package jp.co.systembase.barcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Itf extends Barcode {

	private static final Map<Character, Byte[]> CODE_PATTERNS = new HashMap<Character, Byte[]>() {
		private static final long serialVersionUID = -368362659972102121L;
		{
			put('0', new Byte[]{0, 0, 1, 1, 0});
			put('1', new Byte[]{1, 0, 0, 0, 1});
			put('2', new Byte[]{0, 1, 0, 0, 1});
			put('3', new Byte[]{1, 1, 0, 0, 0});
			put('4', new Byte[]{0, 0, 1, 0, 1});
			put('5', new Byte[]{1, 0, 1, 0, 0});
			put('6', new Byte[]{0, 1, 1, 0, 0});
			put('7', new Byte[]{0, 0, 0, 1, 1});
			put('8', new Byte[]{1, 0, 0, 1, 0});
			put('9', new Byte[]{0, 1, 0, 1, 0});
		}
	};

	private static final Byte[] START_PATTERN = {0, 0, 0, 0};
	private static final Byte[] STOP_PATTERN = {1, 0, 0};

	public boolean generateCheckSum = false;
	public boolean withCheckSumText = false;

	public List<Byte> encode(String data){
		if (data == null || data.length() == 0){
			return null;
		}
		validate(data);
		return _encode(_regularizeData(data));
	}

	private String _regularizeData(String data){
		String ret = data;
		if (this.generateCheckSum){
			ret += this.calcCheckDigit(data);
		}
		if (ret.length() % 2 == 1){
			ret = "0" + ret;
		}
		return ret;
	}

	private List<Byte> _encode(String data){
		List<Byte> ret = new ArrayList<Byte>();
		addCodes(ret, START_PATTERN);
		for(int i = 0;i < data.length(); i+= 2){
			Byte[] c1 = CODE_PATTERNS.get(data.charAt(i));
			Byte[] c2 = CODE_PATTERNS.get(data.charAt(i + 1));
			for(int j = 0;j < c1.length;j++){
				ret.add(c1[j]);
				ret.add(c2[j]);
			}
		}
		addCodes(ret, STOP_PATTERN);
		return ret;
	}

	private void addCodes(List<Byte> l, Byte[] c){
		for(Byte _c: c){
			l.add(_c);
		}
	}

	private void validate(String data) {
		for (char c: data.toCharArray()) {
			if (!CODE_PATTERNS.containsKey(c)) {
				throw new IllegalArgumentException("illegal char: " + c + " of data: " + data);
			}
		}
	}

	private int calcCheckDigit(String data) {
		int sum = 0;
		for (int i = data.length() - 1; i >= 0; i--) {
			int n = data.charAt(i) - 0x30; // - '0'
			sum += i % 2 != 0 ? n: n * 3;
		}

		int checkNum = 10;
		int cd = checkNum - (sum % checkNum);
		if (cd == checkNum) {
			cd = 0;
		}

		return cd;
	}

	public void render(Graphics g, int x, int y, int w, int h, String data){
		this.render(g, new Rectangle(x, y, w, h), data);
	}

	public void render(Graphics g, Rectangle r, String data){
		if (data == null || data.length() == 0){
			return;
		}
		float w = r.width - this.marginX * 2;
		float h = r.height - this.marginY * 2;
		float _h = h;
		if (this.withText){
			_h *= 0.7f;
		}
		if (w <= 0 || h <= 0){
			return;
		}
		String _data = this._regularizeData(data);
		List<Byte> cs = this._encode(_data);
		float uw = w / (_data.length() * 7 + 8);
		float x = this.marginX;
		boolean draw = true;
		g.setColor(Color.BLACK);
		for(Byte c: cs){
			float bw = uw * (c + 1);
			if (draw){
				g.fillRect((int)(r.x + x), (int)(r.y + this.marginY), (int)bw, (int)_h);
			}
			x += bw;
			draw = !draw;
		}
		if (this.withText){
			if (this.generateCheckSum && !this.withCheckSumText){
				_data = _data.substring(0, _data.length() - 1);
			}
			float fs = h * 0.2f;
			fs = Math.min(fs, ((w * 0.9f) / data.length()) * 2.0f);
			fs = Math.max(fs, 6.0f);
			Font f = new Font("SansSerif", Font.PLAIN, (int)fs);
			g.setFont(f);
			g.drawString(_data,
					(int)(r.x + (r.width - fs * _data.length() / 2) / 2),
					(int)(r.y + _h + this.marginY + fs));
		}		
	}

}
