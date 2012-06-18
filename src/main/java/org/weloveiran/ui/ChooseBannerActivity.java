package org.weloveiran.ui;

import org.weloveiran.R;
import org.weloveiran.adapters.ImageAdapter;
import org.weloveiran.helpers.ImageHelper;
import org.weloveiran.image.BannerPlotter;
import org.weloveiran.image.Resizer;
import org.weloveiran.tasks.UploadPicture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blahti.example.drag.DragController;
import com.blahti.example.drag.DragLayer;

public class ChooseBannerActivity extends Activity implements View.OnTouchListener {

	private DragController mDragController;
	private DragLayer mDragLayer;
	private Bitmap photo;
	private Bitmap banner;
	private ImageView bannerView;
	private Resizer r;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDragController = new DragController(this);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.ui_choose_banner);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.small_title);
		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("Preview");
		
		ImageView rotate = (ImageView) findViewById(R.id.rotate);
		rotate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				photo = ImageHelper.rotate(photo, 90);
				showPicture(photo);	
			}
		});
		
		bannerView = (ImageView) findViewById(R.id.banner);
		
		Button btnAcceptPhoto = (Button) findViewById(R.id.btnAcceptPhoto);
		btnAcceptPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Bitmap plotted = new BannerPlotter(photo).plote(banner).at(
						(float) (bannerView.getLeft() * (1 / r.getPercentual())),
						(float) (bannerView.getTop() * (1 / r.getPercentual())));
				uploadPicture(plotted);
				//showPicture(plotted);
			}
		});

		setupViews();
		prepareBannerSelectionGallery();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		try {
			photo = getPhoto();
			Uri photoUri = (Uri) getIntent().getExtras().get(MediaStore.EXTRA_OUTPUT);
			photo = ImageHelper.resolvePhotoOrientation(this, photoUri, photo);
			photo = ImageHelper.fitInsideFrame(photo, 800, 600);
		} catch (Throwable e) {
			e.printStackTrace();
			Toast.makeText(this, "Photo too large to be loaded. Choose some smaller! :)", Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if (photo != null) {
			photo.recycle();
		}
		if (banner != null) {
			banner.recycle();
		}
		super.onStop();
	}

	public boolean onTouch(View v, MotionEvent ev) {
		boolean handledHere = false;
		final int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			handledHere = startDrag(v);
		}
		return handledHere;
	}

	public boolean startDrag(View v) {
		Object dragInfo = v;
		mDragController.startDrag(v, mDragLayer, dragInfo, DragController.DRAG_ACTION_MOVE);
		return true;
	}

	private void setupViews() {
		DragController dragController = mDragController;
		mDragLayer = (DragLayer) findViewById(R.id.photo_banner);
		mDragLayer.setDragController(dragController);
		dragController.addDropTarget(mDragLayer);
		ImageView banner = (ImageView) findViewById(R.id.banner);
		banner.setOnTouchListener(this);
	}

	private void prepareBannerSelectionGallery() {
		Gallery g = (Gallery) findViewById(R.id.banners_gallery);
		g.setAdapter(new ImageAdapter(this));
		g.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				int bannerId = 0;
				switch (position) {
					case 0: bannerId = R.drawable.blue_banner; break;
					case 1: bannerId = R.drawable.orange_banner; break;
					case 2: bannerId = R.drawable.pink_banner; break;
					case 3: bannerId = R.drawable.purple_banner; break;
					case 4: bannerId = R.drawable.pink_round; break;
				}
				banner = BitmapFactory.decodeResource(getResources(), bannerId);
				showPicture(photo);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void uploadPicture(Bitmap photo) {
		ProgressDialog progress = ProgressDialog.show(ChooseBannerActivity.this, "Uploading", "Please wait...", true);
		progress.setCancelable(true);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ChooseBannerActivity.this.finish();
			}
		});
		new UploadPicture().execute(progress, alertBuilder, photo);
	}
	
	private Bitmap getPhoto() throws Exception {
		Uri photoUri = (Uri) getIntent().getExtras().get(MediaStore.EXTRA_OUTPUT);
		Bitmap result = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
		return result;
	}
	
	private void showPicture(Bitmap bitmap) {
		
		DragLayer photo = (DragLayer) findViewById(R.id.photo_banner);
		RelativeLayout frame = (RelativeLayout) findViewById(R.id.frame);
		
		r = new Resizer(bitmap.getWidth(), bitmap.getHeight()).newRecipient(frame.getWidth(), frame.getHeight());

		photo.setBackgroundDrawable(new BitmapDrawable(bitmap));
		photo.setLayoutParams(new RelativeLayout.LayoutParams((int) r.getNewWidth(), (int) r.getNewHeight()));
		//photo.getLayoutParams().width = (int) r.getNewWidth();
		//photo.getLayoutParams().height = (int) r.getNewHeight();
		//photo.invalidate();

		if (banner != null) {
			int bannerHeight = (int) (banner.getHeight() * r.getPercentual());
			int bannerWidth = (int) (banner.getWidth() * r.getPercentual());
			bannerView.getLayoutParams().width = bannerWidth;
			bannerView.getLayoutParams().height = bannerHeight;
			bannerView.setBackgroundDrawable(new BitmapDrawable(banner));
		}
	}
}
