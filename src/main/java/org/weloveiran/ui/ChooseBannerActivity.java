package org.weloveiran.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.weloveiran.R;
import org.weloveiran.adapters.ImageAdapter;
import org.weloveiran.helpers.ImageHelper;
import org.weloveiran.image.BannerPlotter;
import org.weloveiran.tasks.UploadPicture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class ChooseBannerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.ui_choose_banner);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		prepareBannerSelectionGallery();
		
		Uri photoUri = (Uri) getIntent().getExtras().get(MediaStore.EXTRA_OUTPUT);
		final Bitmap photoFinal = ploteBanner(photoUri);

		Button btnAcceptPhoto = (Button) findViewById(R.id.btnAcceptPhoto);
		btnAcceptPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadPicture(photoFinal);
			}
		});

		showPicture(photoFinal);
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Toast.makeText(this, "Longpress: " + info.position, Toast.LENGTH_SHORT).show();
        return true;
    }

	private void prepareBannerSelectionGallery() {
		Gallery g = (Gallery) findViewById(R.id.banners_gallery);
        // Set the adapter to our custom adapter (below)
        g.setAdapter(new ImageAdapter(this));
        
        // Set a item click listener, and just Toast the clicked position
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(ChooseBannerActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
	}

	private Bitmap ploteBanner(Uri photoUri) {
		try {
			ContentResolver cr = getContentResolver();
			InputStream in = cr.openInputStream(photoUri);

			Bitmap photo = ImageHelper.resolvePhotoOrientation(this, photoUri, BitmapFactory.decodeStream(in)).copy(Bitmap.Config.RGB_565, true);
			Bitmap banner = BitmapFactory.decodeResource(getResources(), R.drawable.pink_round).copy(Bitmap.Config.RGB_565, true);

			return new BannerPlotter(photo).plote(banner).at(200, 30);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void uploadPicture(Bitmap photo) {
		ProgressDialog progress = ProgressDialog.show(
				ChooseBannerActivity.this, "Uploading", "Please wait...", true);
		progress.setCancelable(true);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		new UploadPicture().execute(progress, alertBuilder, photo);
	}

	private void showPicture(Bitmap result) {
		ImageView resultView = (ImageView) findViewById(R.id.photo_banner);
		resultView.setImageBitmap(result);
	}
}
