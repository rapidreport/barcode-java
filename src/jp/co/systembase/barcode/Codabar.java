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

	public Byte[] _encode(List<Integer> codePoints){
		List<Byte> ret = new ArrayList<Byte>();
		for(int p: codePoints){
			this.addCodes(ret, p);
		}
		return ret.toArray(new Byte[0]);
	}

	public List<Integer> getCodePoints(String data){
		List<Integer> ret = new ArrayList<Integer>();
		for(int i = 0;i < data.length();i++){
			int p = CHARS.indexOf(data.toUpperCase().charAt(i));
			if (p >= 0){
				if (i == 0 || i == data.length() - 1){
					if (p < START_STOP_POINT){
						throw new IllegalArgumentException("(codabar)スタート/ストップ文字が含まれていません: " + data);
					}
				}else{
					if (p >= START_STOP_POINT){
						throw new IllegalArgumentException("(codabar)不正なデータです: " + data);
					}
				}
			}else{
				throw new IllegalArgumentException("(codabar)不正なデータです: " + data);
			}
			ret.add(p);
		}
        if (ret.size() < 2){
        	throw new IllegalArgumentException("(codabar)不正なデータです: " + data);
        }
        return ret;
	}

	public int calcCheckDigit(List<Integer> ps){
		int s = 0;
		for(int p: ps){
			s += p;
		}
        return (16 - (s % 16)) % 16;
	}

	public void addCheckDigit(List<Integer> codePoints, int cd){
		codePoints.add(codePoints.size() - 1, cd);
	}

	public String addCheckDigit(String txt, int cd){
		String ret = txt.substring(0, txt.length() - 1);
		ret += CHARS.charAt(cd);
		ret += txt.substring(txt.length() - 1);
		return ret;
	}

	public String trimStartStopText(String txt){
		return txt.substring(1, txt.length() - 2);
	}

	private void addCodes(List<Byte> l, int p){
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
		List<Integer> ps = this.getCodePoints(data);
		String txt = data;
		if (this.generateCheckSum){
			int cd = this.calcCheckDigit(ps);
			this.addCheckDigit(ps, cd);
			if (this.withCheckSumText){
				txt = this.addCheckDigit(txt, cd);
			}
		}
		if (!this.withStartStopText){
			txt = this.trimStartStopText(txt);
		}
		Byte cs[] = this._encode(ps);
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
            Font f = this.getFont(this.getFontSize(txt, w, h));
			g.setFont(f);
			g.drawString(txt,
					(int)(r.x + (r.width - f.getSize() * txt.length() / 2) / 2),
					(int)(r.y + _h + this.marginY + f.getSize()));
        }
	}

}
