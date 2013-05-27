package jp.co.systembase.barcode.content;

public class PointScale extends Scale {

	public PointScale(float marginX, float marginY, float w, float h, int dpi) {
		super(marginX, marginY, w, h, dpi);
	}
	public PointScale(float marginX, float marginY, float w, float h) {
		super(marginX, marginY, w, h, DPI);
	}

	@Override
	public float pixelMarginX() {
		return pointToPixel(getDpi(), getMarginX());
	}

	@Override
	public float pixelMarginY() {
		return pointToPixel(getDpi(), getMarginY());
	}

	@Override
	public float pixelWidth() {
		return pointToPixel(getDpi(), getWidth()) - (pixelMarginX() * 2);
	}

	@Override
	public float pixelHeight() {
		return pointToPixel(getDpi(), getHeigth()) - (pixelMarginY() * 2);
	}
}
