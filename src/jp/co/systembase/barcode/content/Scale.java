package jp.co.systembase.barcode.content;

public abstract class Scale {

	public static int DPI = 72;

	private float _marginX;
	private float _marginY;
	private float _width;
	private float _heigth;
	private int _dpi;

	public Scale(float marginX, float marginY, float width, float heigth, int dpi) {
		_marginX = marginX;
		_marginY = marginY;
		_width = width;
		_heigth = heigth;
		_dpi = dpi;
	}

	public Scale(float marginX, float marginY, float width, float heigth) {
		this(marginX, marginY, width, heigth, DPI);
	}

	public static float pointToPixel(int dpi, float point) {
		return dpi * (point / 72.0f);
	}

	public static float mmToPixel(int dpi, float mm) {
		return dpi * (mm / 25.4f);
	}

	public float getMarginX() {
		return _marginX;
	}

	public float getMarginY() {
		return _marginY;
	}

	public float getWidth() {
		return _width;
	}

	public float getHeigth() {
		return _heigth;
	}

	public int getDpi() {
		return _dpi;
	}

	public void setMarginX(float marginX) {
		_marginX = marginX;
	}

	public void setMarginY(float marginY) {
		_marginY = marginY;
	}

	public void setWidth(float width) {
		_width = width;
	}

	public void setHeight(float heigth) {
		_heigth = heigth;
	}

	public void setDpi(int dpi) {
		_dpi = dpi;
	}

	abstract public float pixelMarginX();

	abstract public float pixelMarginY();

	abstract public float pixelWidth();

	abstract public float pixelHeight();
}
