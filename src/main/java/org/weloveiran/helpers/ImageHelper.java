package org.weloveiran.helpers;

import java.io.IOException;

import org.weloveiran.image.Resizer;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageHelper {

	private ImageHelper() {
	}

	public static Bitmap resolvePhotoOrientation(Activity activity, Uri uri, Bitmap photo) throws IOException {

		Bitmap resolvedImage = photo;
		String realPath = getRealPathFromURI(activity, uri);
		ExifInterface exif = new ExifInterface(realPath);

		if (exif != null) {
			int orientation = Integer.parseInt(exif.getAttribute(ExifInterface.TAG_ORIENTATION));

			if (orientation == 3) {
				resolvedImage = rotate(photo, 180);
			} else if (orientation == 1) {
				resolvedImage = rotate(photo, 90);
			} else if (orientation == 6) {
				resolvedImage = rotate(photo, 90);
			} else if (orientation == 8) {
				resolvedImage = rotate(photo, 270);
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

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public static Bitmap rotate(Bitmap photo, int amount) {
		try {
			Matrix matrix = new Matrix();
			matrix.postRotate(amount);
			return Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
		} catch (Throwable e) {
			e.printStackTrace();
			return photo;
		}
	}

	public static Bitmap fitInsideFrame(Bitmap bitmap, int vw, int vh) {
		Resizer r = new Resizer(bitmap.getWidth(), bitmap.getHeight()).newRecipient(vw, vh);
		Bitmap result = Bitmap.createScaledBitmap(bitmap, (int) r.getNewWidth(), (int) r.getNewHeight(), false);
		return result;
	}
}
