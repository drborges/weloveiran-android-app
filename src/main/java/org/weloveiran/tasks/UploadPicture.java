package org.weloveiran.tasks;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;

public class UploadPicture extends AsyncTask<Object, Void, Integer> {

	private ProgressDialog progress;
	private AlertDialog.Builder alertBuilder;

	@Override
	protected Integer doInBackground(Object... params) {
		try {
			progress = (ProgressDialog) params[0];
			alertBuilder = (AlertDialog.Builder) params[1];
			Bitmap photo = (Bitmap) params[2];

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost("http://dev.weloveiran.org/upload");

			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			photo.compress(CompressFormat.JPEG, 100, bos);
			byte[] data = bos.toByteArray();

			entity.addPart("photo", new ByteArrayBody(data, "weloveiran.jpg"));

			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost, localContext);

			return response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			Log.e(e.getClass().getName(), e.getMessage(), e);
			return -1;
		}
	}

	@Override
	protected void onProgressUpdate(Void... unsued) {
	}

	@Override
	protected void onPostExecute(Integer response) {
		if (progress.isShowing())
			progress.dismiss();

		if (response == HttpURLConnection.HTTP_OK) {
			alertBuilder.setMessage("Picture posted!");
		} else {
			alertBuilder.setMessage("Post failed!");
		}
		
		AlertDialog alert = alertBuilder.create();
		alert.show();
	}
}
