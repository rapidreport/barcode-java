package jp.co.systembase.barcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Codabar extends Barcode {

	private static final byte CODE_PATTERNS[][] =
	{{0, 0, 0, 0, 0, 1, 1},
	 {0, 0, 0, 0, 1, 1, 0},
	 {0, 0, 0, 1, 0, 0, 1},
	 {1, 1, 0, 0, 0, 0, 0},
	 {0, 0, 1, 0, 0, 1, 0},
	 {1, 0, 0, 0, 0, 1, 0},
	 {0, 1, 0, 0, 0, 0, 1},
	 {0, 1, 0, 0, 1, 0, 0},
	 {0, 1, 1, 0, 0, 0, 0},
	 {1, 0, 0, 1, 0, 0, 0},
	 {0, 0, 0, 1, 1, 0, 0},
	 {0, 0, 1, 1, 0, 0, 0},
	 {1, 0, 0, 0, 1, 0, 1},
	 {1, 0, 1, 0, 0, 0, 1},
	 {1, 0, 1, 0, 1, 0, 0},
	 {0, 0, 1, 0, 1, 0, 1},
	 {0, 0, 1, 1, 0, 1, 0},
	 {0, 1, 0, 1, 0, 0, 1},
	 {0, 0, 0, 1, 0, 1, 1},
	 {0, 0, 0, 1, 1, 1, 0}};

	private static final String CHARS = "0123456789-$:/.+ABCD";
	private static final int START_STOP_POINT = 16;

	public boolean generateCheckSum = false;
	public boolean withCheckSumText = false;
	public boolean withStartStopText = false;

	public Byte[] encode(String data){
		if (data == null || data.length() == 0){
			return null;
		}
		List<Integer> ps = this.getCodePoints(data);
		List<Integer> _ps;
		if (generateCheckSum){
			_ps = new ArrayList<Integer>();
			_ps.addAll(ps.subList(0,  ps.size() - 1));
			_ps.add(this.calcCheckDigit(ps));
			_ps.add(ps.get(ps.size() - 1));
		}else{
			_ps = ps;
		}
		return _encode(_ps);
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
	}

	private List<Integer> getCodePoints(String data){
		List<Integer> ret = new ArrayList<Integer>();
		for(int i = 0;i < data.length();i++){
			int p = CHARS.indexOf(data.toUpperCase().charAt(i));
			if (p >= 0){
				if (i == 0 || i == data.length() - 1){
					if (p < START_STOP_POINT){
						throw new IllegalArgumentException("illegal data: " + data);
					}
				}else{
					if (p >= START_STOP_POINT){
						throw new IllegalArgumentException("illegal data: " + data);
					}
				}
			}else{
				throw new IllegalArgumentException("illegal data: " + data);
			}
			ret.add(p);
		}
        if (ret.size() < 2){
        	throw new IllegalArgumentException("illegal data: " + data);
        }
        return ret;
	}

	private int calcCheckDigit(List<Integer> ps){
		int s = 0;
		for(int p: ps){
			s += p;
		}
        return (16 - (s % 16)) % 16;
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
		List<Integer> ps = this.getCodePoints(data);
		List<Integer> _ps;
		String _data = data;
		if (this.generateCheckSum){
			int cd = this.calcCheckDigit(ps);
			_ps = new ArrayList<Integer>();
			_ps.addAll(ps.subList(0, ps.size() - 1));
			_ps.add(cd);
			_ps.add(ps.get(ps.size() - 1));
			if (this.withCheckSumText){
				_data = data.substring(0, data.length() - 1);
				_data += CHARS.charAt(cd);
				_data += data.substring(data.length() - 1);
			}
		}else{
			_ps = ps;
		}
		if (!this.withStartStopText){
			_data = _data.substring(1, _data.length() - 1);
		}
		Byte cs[] = this._encode(_ps);
		float mw;
		{
			int l = 0;
			for(int c: cs){
				l += c + 1;
			}
            mw = w / l;
		}
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
