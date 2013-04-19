package jp.co.systembase.barcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ean8 extends Ean {

	private static final int GUARDS[] = {0, 2, 20, 22, 40, 42};
	private static final int CHARPOS[] = {7, 14, 21, 28, 39, 46, 53, 60};

	public Byte[] encode(String data){
		if (data == null || data.length() == 0){
			return new Byte[0];
		}
		List<Byte> _data = pack(data);
		if (_data.size() == 7){
			_data.add(this.calcCheckDigit(_data));
		}
		if (_data.size() != 8){
			throw new IllegalArgumentException("illegal data: " + data);
		}
		return this._encode(_data);
	}

	private Byte[] _encode(List<Byte> data){
		List<Byte> cs = new ArrayList<Byte>();
		this.addStartCodes(cs);
		for(int i = 0;i <= 3;i++){
			this.addCodes(cs, data.get(i));
		}
		this.addCenterCodes(cs);
		for(int i = 4;i <= 7;i++){
			this.addCodes(cs, data.get(i));
		}
		this.addStopCodes(cs);
		return cs.toArray(new Byte[0]);
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
		float fs = 0.0f;
		if (this.withText){
			_h *= 0.7f;
			fs = h * 0.2f;
			fs = Math.min(fs, ((w * 0.9f) / data.length()) * 2.0f);
			fs = Math.max(fs, 6.0f);
		}
		if (w <= 0 || h <= 0){
			return;
		}
		List<Byte> _data = this.pack(data);
		if (_data.size() == 7){
			_data.add(this.calcCheckDigit(_data));
		}
		if (_data.size() != 8){
			throw new IllegalArgumentException("illegal data: " + data);
		}
		Byte cs[] = this._encode(_data);
		float mw = w / (8 * 7 + 11);
		boolean draw = true;
		float x = this.marginX;
		g.setColor(Color.BLACK);
		for(int i = 0;i < cs.length;i++){
			float dw = cs[i] * mw;
			if (draw){
				float __h = _h;
				if (Arrays.binarySearch(GUARDS, i) >= 0){
					__h += fs / 2;
				}
				g.fillRect(
						(int)(r.x + x), (int)(r.y + this.marginY),
						(int)(dw * barWidth), (int)__h);
			}
			draw = !draw;
			x += dw;
		}
		if (this.withText){
			Font f = new Font("SansSerif", Font.PLAIN, (int)fs);
			g.setFont(f);
			for(int i = 0;i < 8;i++){
				g.drawString(_data.get(i).toString(),
						(int)(r.x + CHARPOS[i] * mw + marginX - fs / 4),
						(int)(r.y + _h + marginY + fs));
			}
		}
	}

}
