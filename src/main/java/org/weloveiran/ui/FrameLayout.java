package org.weloveiran.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class FrameLayout extends LinearLayout {

	private OnMeasure onMeasure;

	interface OnMeasure {
		public void measure();
	}

	public FrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
		super.onSizeChanged(xNew, yNew, xOld, yOld);

		if (onMeasure != null && getWidth() != 0 && getHeight() != 0) {
			onMeasure.measure();
			onMeasure = null;
		}
	}

	public void setOnMeasure(OnMeasure onMeasure) {
		this.onMeasure = onMeasure;
	}
}