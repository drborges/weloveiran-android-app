package org.weloveiran.ui;

import org.weloveiran.R;
import org.weloveiran.ui.listeners.OnClickCapturePhotoButton;
import org.weloveiran.ui.listeners.OnClickChoosePhotoButton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;

public class HomeActivity extends Activity {

	public static final int CAPTURE_IMAGE_REQUEST_CODE = 1;
	public static final int SELECT_IMAGE_REQUEST_CODE = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.ui_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.big_title);

		findViewById(R.id.btnTakePhoto).setOnClickListener(
				new OnClickCapturePhotoButton(this, CAPTURE_IMAGE_REQUEST_CODE));
		findViewById(R.id.btnChoosePhoto).setOnClickListener(
				new OnClickChoosePhotoButton(this, SELECT_IMAGE_REQUEST_CODE));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case CAPTURE_IMAGE_REQUEST_CODE:
				startActivityChooseBanner((Uri) getIntent().getExtras().get(MediaStore.EXTRA_OUTPUT));
				break;
			case SELECT_IMAGE_REQUEST_CODE:
				startActivityChooseBanner(Uri.parse(data.getDataString()));
				break;
			default:
				break;
			}
		}
	}

	private void startActivityChooseBanner(Uri photoUri) {
		Intent intent = new Intent(HomeActivity.this, ChooseBannerActivity.class);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivity(intent);
	}
}