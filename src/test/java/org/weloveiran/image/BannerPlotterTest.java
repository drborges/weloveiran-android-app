package org.weloveiran.image;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@PrepareForTest({ Bitmap.class })
@RunWith(RobolectricTestRunner.class)
public class BannerPlotterTest {

	private BannerPlotter plotter;
	private Bitmap photo;
	private Canvas canvas;
	
	@Before
	public void setup() {
		photo = mock(Bitmap.class);
		canvas = mock(Canvas.class);
		plotter = new BannerPlotter(photo, canvas);
	}
	
	@Test
	public void plotsBannerOnPictureAtSpecifyedPosition() {
		Bitmap banner = mock(Bitmap.class);
		plotter.plote(banner).at(20, 30);
		
		verify(canvas).drawBitmap(Mockito.eq(banner), Mockito.eq(20f), Mockito.eq(30f), Mockito.any(Paint.class));
	}

}
