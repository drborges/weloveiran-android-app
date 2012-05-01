package org.weloveiran.ui;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.weloveiran.R;
import org.weloveiran.image.BannerPlotter;
import org.weloveiran.ui.listeners.OnClickCapturePhotoButton;
import org.weloveiran.ui.listeners.OnClickChoosePhotoButton;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

public class HomeActivity extends Activity {

	public static final int CAPTURE_IMAGE_REQUEST_CODE = 1;
	public static final int SELECT_IMAGE_REQUEST_CODE = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.ui_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		findViewById(R.id.btnTakePhoto).setOnClickListener(new OnClickCapturePhotoButton(this, CAPTURE_IMAGE_REQUEST_CODE));
		findViewById(R.id.btnChoosePhoto).setOnClickListener(new OnClickChoosePhotoButton(this, SELECT_IMAGE_REQUEST_CODE));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK) {
			handleResult(requestCode, data);
		}
	}
	
	private void handleResult(int requestCode, Intent data) {
		switch (requestCode) {
		case CAPTURE_IMAGE_REQUEST_CODE:
			Log.d("Captured Image Path", data.getData().getPath());
			break;
		case SELECT_IMAGE_REQUEST_CODE:
			try {
				setContentView(R.layout.photo_banner);
				Uri photoUri = Uri.parse(data.getDataString());
				ContentResolver cr = getContentResolver();
				InputStream in;
					in = cr.openInputStream(photoUri);
				Bitmap photo = BitmapFactory.decodeStream(in).copy(Bitmap.Config.ARGB_8888, true);
				//Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.image).copy(Bitmap.Config.ARGB_8888, true);
				Bitmap banner = BitmapFactory.decodeResource(getResources(), R.drawable.pink_round).copy(Bitmap.Config.ARGB_8888, true);
				
				ImageView resultView = (ImageView) findViewById(R.id.photo_banner);
				Bitmap result = new BannerPlotter(photo).plote(banner).at(200, 30);
				resultView.setImageBitmap(result);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			break;
			
		default:
			break;
		}
	}
}