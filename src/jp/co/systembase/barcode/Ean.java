package jp.co.systembase.barcode;

import java.util.ArrayList;
import java.util.List;

public class Ean extends Barcode {

	protected static final byte CODE_PATTERNS[][] =
	{{3, 2, 1, 1},
	 {2, 2, 2, 1},
	 {2, 1, 2, 2},
	 {1, 4, 1, 1},
	 {1, 1, 3, 2},
	 {1, 2, 3, 1},
	 {1, 1, 1, 4},
	 {1, 3, 1, 2},
	 {1, 2, 1, 3},
	 {3, 1, 1, 2}};

	protected static final byte START_PATTERN[] = {1, 1, 1};
	protected static final byte STOP_PATTERN[] = {1, 1, 1};
	protected static final byte CENTER_PATTERN[] = {1, 1, 1, 1, 1};

	protected byte calcCheckDigit(String data){
      return this.calcCheckDigit(this.pack(data));
	}

	protected byte calcCheckDigit(List<Byte> data){
		int s = 0;
		for(int i = 0;i < data.size();i++){
			if (i % 2 == 0){
				s += data.get(data.size() - i - 1) * 3;
			}else{
				s += data.get(data.size() - i - 1);
			}
		}
		return (byte)((10 - (s % 10)) % 10);
	}

	protected List<Byte> pack(String data){
		if (data == null){
			throw new IllegalArgumentException("illegal data");
		}
		List<Byte> ret = new ArrayList<Byte>();
		for(int i = 0;i < data.length();i++){
			char c = data.charAt(i);
			if (c < '0' || c > '9'){
				throw new IllegalArgumentException("illegal data: " + data);
			}
			ret.add((byte)(c - '0'));
		}
		return ret;
	}

	protected void addCodes(List<Byte> l, int p){
		l.add(CODE_PATTERNS[p][0]);
		l.add(CODE_PATTERNS[p][1]);
		l.add(CODE_PATTERNS[p][2]);
		l.add(CODE_PATTERNS[p][3]);
	}

	protected void addCodesEven(List<Byte> l, int p){
		l.add(CODE_PATTERNS[p][3]);
		l.add(CODE_PATTERNS[p][2]);
		l.add(CODE_PATTERNS[p][1]);
		l.add(CODE_PATTERNS[p][0]);
	}

	protected void addStartCodes(List<Byte> l){
		l.add(START_PATTERN[0]);
		l.add(START_PATTERN[1]);
		l.add(START_PATTERN[2]);
	}

	protected void addCenterCodes(List<Byte> l){
		l.add(CENTER_PATTERN[0]);
		l.add(CENTER_PATTERN[1]);
		l.add(CENTER_PATTERN[2]);
		l.add(CENTER_PATTERN[3]);
		l.add(CENTER_PATTERN[4]);
	}

	protected void addStopCodes(List<Byte> l){
		l.add(STOP_PATTERN[0]);
		l.add(STOP_PATTERN[1]);
		l.add(STOP_PATTERN[2]);
	}

}
