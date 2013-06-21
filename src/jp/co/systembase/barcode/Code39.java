package jp.co.systembase.barcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Code39 extends Barcode {

	private static final byte CODE_PATTERNS[][] =
	{{0, 0, 0, 1, 1, 0, 1, 0, 0},
	 {1, 0, 0, 1, 0, 0, 0, 0, 1},
	 {0, 0, 1, 1, 0, 0, 0, 0, 1},
	 {1, 0, 1, 1, 0, 0, 0, 0, 0},
	 {0, 0, 0, 1, 1, 0, 0, 0, 1},
	 {1, 0, 0, 1, 1, 0, 0, 0, 0},
	 {0, 0, 1, 1, 1, 0, 0, 0, 0},
	 {0, 0, 0, 1, 0, 0, 1, 0, 1},
	 {1, 0, 0, 1, 0, 0, 1, 0, 0},
	 {0, 0, 1, 1, 0, 0, 1, 0, 0},
	 {1, 0, 0, 0, 0, 1, 0, 0, 1},
	 {0, 0, 1, 0, 0, 1, 0, 0, 1},
	 {1, 0, 1, 0, 0, 1, 0, 0, 0},
	 {0, 0, 0, 0, 1, 1, 0, 0, 1},
	 {1, 0, 0, 0, 1, 1, 0, 0, 0},
	 {0, 0, 1, 0, 1, 1, 0, 0, 0},
	 {0, 0, 0, 0, 0, 1, 1, 0, 1},
	 {1, 0, 0, 0, 0, 1, 1, 0, 0},
	 {0, 0, 1, 0, 0, 1, 1, 0, 0},
	 {0, 0, 0, 0, 1, 1, 1, 0, 0},
	 {1, 0, 0, 0, 0, 0, 0, 1, 1},
	 {0, 0, 1, 0, 0, 0, 0, 1, 1},
	 {1, 0, 1, 0, 0, 0, 0, 1, 0},
	 {0, 0, 0, 0, 1, 0, 0, 1, 1},
	 {1, 0, 0, 0, 1, 0, 0, 1, 0},
	 {0, 0, 1, 0, 1, 0, 0, 1, 0},
	 {0, 0, 0, 0, 0, 0, 1, 1, 1},
	 {1, 0, 0, 0, 0, 0, 1, 1, 0},
	 {0, 0, 1, 0, 0, 0, 1, 1, 0},
	 {0, 0, 0, 0, 1, 0, 1, 1, 0},
	 {1, 1, 0, 0, 0, 0, 0, 0, 1},
	 {0, 1, 1, 0, 0, 0, 0, 0, 1},
	 {1, 1, 1, 0, 0, 0, 0, 0, 0},
	 {0, 1, 0, 0, 1, 0, 0, 0, 1},
	 {1, 1, 0, 0, 1, 0, 0, 0, 0},
	 {0, 1, 1, 0, 1, 0, 0, 0, 0},
	 {0, 1, 0, 0, 0, 0, 1, 0, 1},
	 {1, 1, 0, 0, 0, 0, 1, 0, 0},
	 {0, 1, 1, 0, 0, 0, 1, 0, 0},
	 {0, 1, 0, 1, 0, 1, 0, 0, 0},
	 {0, 1, 0, 1, 0, 0, 0, 1, 0},
	 {0, 1, 0, 0, 0, 1, 0, 1, 0},
	 {0, 0, 0, 1, 0, 1, 0, 1, 0},
	 {0, 1, 0, 0, 1, 0, 1, 0, 0}};

	private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*";
	private static final int START_STOP_POINT = 43;

	public boolean generateCheckSum = false;
	public boolean withCheckSumText = false;

	public Byte[] encode(String data){
		if (data == null || data.length() == 0){
			return null;
		}
		List<Integer> ps = new ArrayList<Integer>();
		ps.add(START_STOP_POINT);
		List<Integer> _ps = this.getCodePoints(data);
		ps.addAll(_ps);
		if (this.generateCheckSum){
			ps.add(this.calcCheckDigit(_ps));
		}
		ps.add(START_STOP_POINT);
		return _encode(ps);
	}

	protected Byte[] _encode(List<Integer> ps){
		List<Byte> ret = new ArrayList<Byte>();
		for(int p: ps){
			this.addCodes(ret, p);
		}
		return ret.toArray(new Byte[0]);
	}

	protected void addCodes(List<Byte> l, int p){
		if (l.size() > 0){
			l.add((byte)0);
		}
		l.add(CODE_PATTERNS[p][0]);
		l.add(CODE_PATTERNS[p][1]);
		l.add(CODE_PATTERNS[p][2]);
		l.add(CODE_PATTERNS[p][3]);
		l.add(CODE_PATTERNS[p][4]);
		l.add(CODE_PATTERNS[p][5]);
		l.add(CODE_PATTERNS[p][6]);
		l.add(CODE_PATTERNS[p][7]);
		l.add(CODE_PATTERNS[p][8]);
	}

	private List<Integer> getCodePoints(String data){
		List<Integer> ret = new ArrayList<Integer>();
		for(int i = 0;i < data.length();i++){
			char c = data.charAt(i);
			int p = CHARS.indexOf(c);
			if (p >= 0){
				ret.add(p);
			}else{
				throw new IllegalArgumentException("illegal data: " + data);
			}
		}
		return ret;
	}

	private int calcCheckDigit(List<Integer> ps){
		int s = 0;
		for(int p: ps){
			s += p;
		}
		return s % 43;
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
		List<Integer> ps = new ArrayList<Integer>();
		String _data = data;
		{
			ps.add(START_STOP_POINT);
			List<Integer> _ps = this.getCodePoints(data);
			ps.addAll(_ps);
			if (this.generateCheckSum){
				int cd = this.calcCheckDigit(_ps);
				ps.add(cd);
				if (this.withCheckSumText){
				_data += CHARS.charAt(cd);
				}
			}
			ps.add(START_STOP_POINT);
			_data = "*" + _data + "*";
		}
		Byte cs[] = this._encode(ps);
		float mw = w / (ps.size() * 13 - 1);
		boolean draw = true;
		float x = this.marginX;
		g.setColor(Color.BLACK);
		for(int i = 0;i < cs.length;i++){
			float dw = (cs[i] + 1) * mw;
			if (draw){
				g.fillRect(
						(int)(r.x + x), (int)(r.y + this.marginY),
						(int)(dw * barWidth), (int)_h);
			}
			draw = !draw;
			x += dw;
		}
		if (this.withText){
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
