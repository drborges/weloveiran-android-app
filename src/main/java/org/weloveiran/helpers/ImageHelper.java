package org.weloveiran.helpers;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class ImageHelper {

	private static final String TAG = "we-love-iran";
	
	private ImageHelper() {}
	
	public static Bitmap resolvePhotoOrientation(Activity activity, Uri uri, Bitmap photo)
			throws IOException {
		
		Bitmap resolvedImage = photo;
		String realPath = getRealPathFromURI(activity, uri);
		Log.d(TAG, realPath);
		ExifInterface exif = new ExifInterface(realPath);
		
		if (exif != null) {
			int orientation = Integer.parseInt(exif.getAttribute(ExifInterface.TAG_ORIENTATION));
			Log.d(TAG, Integer.toString(orientation));
			
			if (orientation == 3) {
				resolvedImage = rotate(photo, 180);
			} else {
				// orientation == 6 || orientation == 8
				resolvedImage = rotate(photo, 90);
			}
		}
		return resolvedImage;
	}
	
	private static String getRealPathFromURI(Activity activity, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
		if (cursor == null) {
			return contentUri.getPath();
		}

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	private static Bitmap rotate(Bitmap photo, int amount) {
		Matrix matrix = new Matrix();
		matrix.postRotate(amount);

		return Bitmap.createBitmap(photo, 0, 0, photo.getWidth(),
				photo.getHeight(), matrix, true);
	}
}
