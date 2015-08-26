package jp.co.systembase.barcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Code128 extends Barcode {

	private static byte CODE_PATTERNS[][] =
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
	private static final int TO_C = 99;
	private static final int TO_B  = 100;
	private static final int TO_A = 101;
	private static final int FNC_1 = 102;
	private static final int START_A = 103;
	private static final int START_B = 104;
	private static final int START_C = 105;

	public enum ECodeType{
		NO_CHANGE,
		A,
		B,
		C
	}
	
	public boolean parseFnc1 = false;

	public Byte[] encode(List<Integer> codePoints){
		List<Byte> cs = new ArrayList<Byte>();
		for(int p: codePoints){
			this.addCodes(cs, p);
		}
		this.addCodes(cs, this.calcCheckDigit(codePoints));
		this.addStopCodes(cs);
		return cs.toArray(new Byte[0]);
	}

	public void validate(String data){
		for(int i = 0;i < data.length();i++){
			char c = data.charAt(i);
			if (c > 0x7f){
				throw new IllegalArgumentException("(code128)不正なデータです: " + data);
			}
		}
	}

	public List<Integer> getCodePoints(String data){
		return this.getCodePoints(data, this.getStartCodeType(data));
	}
	
	public List<Integer> getCodePoints(String data, ECodeType startCodeType){
		List<Integer> ret = new ArrayList<Integer>();
		String _data = data;
		ECodeType codeType = startCodeType;
		switch(codeType){
		case A:
			ret.add(START_A);
			break;
		case B:
			ret.add(START_B);
			break;
		case C:
			ret.add(START_C);
			break;
		default:
		}
		while(_data.length() > 0){
			if (this.parseFnc1 && _data.startsWith("{1}")){
				ret.add(FNC_1);
				_data = _data.substring(3);
			}else{
				switch(this.getNextCodeType(_data, codeType)){
				case A:
					ret.add(TO_A);
					codeType = ECodeType.A;
					break;
				case B:
					ret.add(TO_B);
					codeType = ECodeType.B;
					break;
				case C:
					ret.add(TO_C);
					codeType = ECodeType.C;
					break;
				default:
				}
				switch(codeType){
				case A:
					ret.add(this.getCodePointA(_data));
					_data = _data.substring(1);
					break;
				case B:
					ret.add(this.getCodePointB(_data));
					_data = _data.substring(1);
					break;
				case C:
					ret.add(this.getCodePointC(_data));
					_data = _data.substring(2);
					break;
				default:
				}
			}
		}
		return ret;
	}

	private void addCodes(List<Byte> l, int p){
		l.add(CODE_PATTERNS[p][0]);
		l.add(CODE_PATTERNS[p][1]);
		l.add(CODE_PATTERNS[p][2]);
		l.add(CODE_PATTERNS[p][3]);
		l.add(CODE_PATTERNS[p][4]);
		l.add(CODE_PATTERNS[p][5]);
	}

	private void addStopCodes(List<Byte> l){
		l.add(STOP_PATTERN[0]);
		l.add(STOP_PATTERN[1]);
		l.add(STOP_PATTERN[2]);
		l.add(STOP_PATTERN[3]);
		l.add(STOP_PATTERN[4]);
		l.add(STOP_PATTERN[5]);
		l.add(STOP_PATTERN[6]);
	}

	private ECodeType getStartCodeType(String data){
		if (data.length() >= 2){
			if (data.charAt(0) >= '0' && data.charAt(0) <= '9' &&
				data.charAt(1) >= '0' && data.charAt(1) <= '9'){
				return ECodeType.C;
			}
		}
		if (data.charAt(0) <= 0x1f){
			return ECodeType.A;
		}
		return ECodeType.B;
	}

	private ECodeType getNextCodeType(String data, ECodeType codeType){
		if (codeType != ECodeType.C){
			if (data.length() >= 4){
				if (data.charAt(0) >= '0' && data.charAt(0) <= '9' &&
					data.charAt(1) >= '0' && data.charAt(1) <= '9' &&
					data.charAt(2) >= '0' && data.charAt(2) <= '9' &&
					data.charAt(3) >= '0' && data.charAt(3) <= '9'){
					return ECodeType.C;
				}
			}
		}
		if (codeType != ECodeType.A){
			if (data.charAt(0) <= 0x1f){
				return ECodeType.A;
			}
		}
		if (codeType != ECodeType.B){
			if (data.charAt(0) >= 0x60){
				return ECodeType.B;
			}
		}
		if (codeType == ECodeType.C){
			if (data.length() < 2 ||
				(data.charAt(0) < '0' || data.charAt(0) > '9') ||
				(data.charAt(1) < '0' || data.charAt(1) > '9')){
				return ECodeType.B;
			}
		}
		return ECodeType.NO_CHANGE;
	}

	private int getCodePointA(String data){
		int c = data.charAt(0);
		if (c <= 0x1f){
			return c + 0x40;
		}else{
			return c - 0x20;
		}
	}

	private int getCodePointB(String data){
		return data.charAt(0) - 0x20;
	}

	private int getCodePointC(String data){
		return Integer.parseInt(data.substring(0, 2));
	}

	private byte calcCheckDigit(List<Integer> ps){
		int t = ps.get(0);
		for(int i = 1;i < ps.size();i++){
			t += i * ps.get(i);
		}
		return (byte)(t % 103);
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
		this.validate(data);
		this.renderBars(
				g, 
				this.getCodePoints(data),
				r.x + this.marginX, 
				r.y + this.marginY,
				w, 
				_h);
		if (this.withText){
			Font f = this.getFont(this.getFontSize(data, w, h));
			g.setFont(f);
			g.drawString(data,
					(int)(r.x + (r.width - f.getSize() * data.length() / 2) / 2),
					(int)(r.y + _h + this.marginY + f.getSize()));
		}
	}

	protected void renderBars(
			Graphics g, 
			List<Integer> codePoints,
			float x,
			float y,
			float w,
			float h){
		float mw = w / ((codePoints.size() + 1) * 11 + 13);
		boolean draw = true;
		float _x = 0;
		g.setColor(Color.BLACK);
		for(byte c: this.encode(codePoints)){
			float dw = c * mw;
			if (draw){
				g.fillRect((int)(x + _x), (int)y, (int)(dw * barWidth), (int)h);
			}
			draw = !draw;
			x += dw;
		}
	}

}
