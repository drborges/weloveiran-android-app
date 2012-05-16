package org.weloveiran.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.weloveiran.R;
import org.weloveiran.image.BannerPlotter;
import org.weloveiran.tasks.UploadPicture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class ChooseBannerActivity extends Activity {

	private static final String TAG = "we-love-iran";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.ui_choose_banner);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		Uri photoUri = (Uri) getIntent().getExtras().get(
				MediaStore.EXTRA_OUTPUT);
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

	private Bitmap ploteBanner(Uri photoUri) {
		try {

			setContentView(R.layout.ui_choose_banner);
			ContentResolver cr = getContentResolver();
			InputStream in = cr.openInputStream(photoUri);

			//Bitmap photo = resolvePhotoOrientation(photoUri, BitmapFactory.decodeStream(in)).copy(Bitmap.Config.RGB_565, true);
			//Bitmap banner = BitmapFactory.decodeResource(getResources(), R.drawable.pink_round).copy(Bitmap.Config.RGB_565, true);

			Bitmap photo = resolvePhotoOrientation(photoUri, getBitmap(photoUri));
			Bitmap banner = BitmapFactory.decodeResource(getResources(), R.drawable.pink_round).copy(Bitmap.Config.RGB_565, true);
			
			return new BannerPlotter(photo).plote(banner).at(200, 30);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Bitmap resolvePhotoOrientation(Uri uri, Bitmap photo)
			throws IOException {
		
		Bitmap resolvedImage = photo;
		String realPath = getRealPathFromURI(uri);
		Log.d(TAG, realPath);
		ExifInterface exif = new ExifInterface(realPath);
		
		if (exif != null) {
			int orientation = Integer.parseInt(exif.getAttribute(ExifInterface.TAG_ORIENTATION));
			Log.d(TAG, Integer.toString(orientation));
			
			switch (orientation) {
			case 3:
				resolvedImage = rotate(photo, 180);
				break;
			case 6:
			case 8:
				resolvedImage = rotate(photo, 90);
				break;
			}
		}
		return resolvedImage;
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		if (cursor == null) {
			return contentUri.getPath();
		}

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private Bitmap rotate(Bitmap photo, int amount) {
		Matrix matrix = new Matrix();
		matrix.postRotate(amount);

		return Bitmap.createBitmap(photo, 0, 0, photo.getWidth(),
				photo.getHeight(), matrix, true);
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
	
	private Bitmap getBitmap(Uri uri) {

	    InputStream in = null;
	    try {
	        final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
	        in = getContentResolver().openInputStream(uri);

	        // Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(in, null, o);
	        in.close();



	        int scale = 1;
	        while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
	            scale++;
	        }
	        Log.d(TAG, "scale = " + scale + ", orig-width: " + o.outWidth       + ", orig-height: " + o.outHeight);

	        Bitmap b = null;
	        in = getContentResolver().openInputStream(uri);
	        if (scale > 1) {
	            scale--;
	            // scale to max possible inSampleSize that still yields an image
	            // larger than target
	            o = new BitmapFactory.Options();
	            o.inSampleSize = scale;
	            b = BitmapFactory.decodeStream(in, null, o);

	            // resize to desired dimensions
	            int height = b.getHeight();
	            int width = b.getWidth();
	            Log.d(TAG, "1th scale operation dimenions - width: " + width    + ", height: " + height);

	            double y = Math.sqrt(IMAGE_MAX_SIZE
	                    / (((double) width) / height));
	            double x = (y / height) * width;

	            Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,     (int) y, true);
	            b.recycle();
	            b = scaledBitmap;

	            System.gc();
	        } else {
	            b = BitmapFactory.decodeStream(in);
	        }
	        in.close();

	        Log.d(TAG, "bitmap size - width: " +b.getWidth() + ", height: " + b.getHeight());
	        return b;
	    } catch (IOException e) {
	        Log.e(TAG, e.getMessage(),e);
	        return null;
	    }
	}
}
