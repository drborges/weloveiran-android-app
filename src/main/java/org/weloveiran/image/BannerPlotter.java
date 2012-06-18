package org.weloveiran.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BannerPlotter implements BannerSetter, Plotter {

	private Bitmap photo;
	private Bitmap banner;
	private final Canvas canvas;
	
	public BannerPlotter(Bitmap photo) {
		this(photo, new Canvas());
	}

	public BannerPlotter(Bitmap photo, Canvas canvas) {
		this.photo = photo;
		this.canvas = canvas;
		canvas.setBitmap(photo);
	}
	
	public BannerSetter plote(Bitmap banner) {
		this.banner = banner;
		return (BannerSetter) this;
	}

	public Bitmap at(float left, float top) {
		canvas.drawBitmap(banner, left, top, new Paint());
		return photo;
	}
}
