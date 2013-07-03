package jp.co.systembase.barcode;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Gs1_128 extends Code128 {

	public boolean conveniFormat = false;

	public Gs1_128(){
		this.parseFnc1 = true;
	}

	@Override
	public void render(Graphics g, Rectangle r, String data) {
		if (data == null || data.length() == 0){
			return;
		}
		float w = r.width - this.marginX * 2;
		float h = r.height - this.marginY * 2;
		float _h = h;
		if (this.withText){
			if (this.conveniFormat){
				_h *= 0.5f;
			}else{
				_h *= 0.7f;
			}
			
		}
		if (w <= 0 || h <= 0){
			return;
		}
		String _data = data;
		this.validate(_data);
		if (this.conveniFormat){
			_data = this.preprocessConveniData(_data);
		}
		this.renderBars(
				g, 
				this.getCodePoints(this.trimData(_data), ECodeType.C),
				r.x + this.marginX,
				r.y + this.marginY,
				w, 
				_h);
		if (this.withText){
			if (this.conveniFormat){
				String t = this.conveniDisplayFormat(_data);
				String t1 = t.substring(0, 33);
				String t2 = t.substring(33);
				Font f = this.getFont(t1, w, h);
				g.setFont(f);
				g.drawString(t1,
						(int)(r.x + this.marginX),
						(int)(r.y + _h + this.marginY + f.getSize()));
				g.drawString(t2,
						(int)(r.x + this.marginX),
						(int)(r.y + _h + this.marginY + f.getSize() * 2));
			}else{
				String t = this.displayFormat(_data);
				Font f = this.getFont(t, w, h);
				g.setFont(f);
				g.drawString(t,
						(int)(r.x + (r.width - f.getSize() * t.length() / 2) / 2),
						(int)(r.y + _h + this.marginY + f.getSize()));
			}
			
		}
	}

	public String preprocessConveniData(String data){
		String _data = data;
		if (!_data.startsWith("(91)")){
			throw new IllegalArgumentException("(gs1_128)データの先頭が'(91)'でなければいけません: " + data);
		}
		if (_data.length() == 45){
			_data = data + this.calcConveniCheckDigit(_data);
		}else if (_data.length() != 46){
			throw new IllegalArgumentException("(gs1_128)データの'(91)'以降が41桁(チェックディジットを含めるなら42桁)でなければいけません: " + data);
		}
		return _data;
	}

	public String trimData(String data){
		String ret = data;
		if (!ret.startsWith("{1}")){
			ret = "{1}" + ret;
		}
		ret = ret.replaceAll("\\(", "");
		ret = ret.replaceAll("\\)", "");
		return ret;
	}

	public String displayFormat(String data){
		String ret = data;
		ret = ret.replaceAll("\\{1\\}", "");
		return ret;
	}

	public String conveniDisplayFormat(String data){
		String ret = data;
		ret = ret.replaceAll("\\{1\\}", "");
		return ret.substring(0, 10) + "-" +
				ret.substring(10, 38) + "-" +
				ret.substring(38, 39) + "-" +
				ret.substring(39, 45) + "-" +
				ret.substring(45, 46);
	}

	private String calcConveniCheckDigit(String data){
		String _data = data;
		_data = _data.replaceAll("\\(", "");
		_data = _data.replaceAll("\\)", "");
		int s = 0;
		for(int i = 0;i < _data.length();i++){
			int t = data.charAt(_data.length() - i - 1) - '0';
			if (i % 2 == 0){
				s += t * 3;
			}else{
				s += t;
			}
		}
		return String.valueOf((10 - (s % 10)) % 10);
	}

}
