package org.weloveiran.ui;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.weloveiran.R;
import org.weloveiran.ui.listeners.OnClickCapturePhotoButton;
import org.weloveiran.ui.listeners.OnClickChoosePhotoButton;

import android.os.Bundle;
import android.view.View;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {

	private HomeActivity activity;
	
	private Bundle savedInstanceState;
	private View btnChoosePhotoView;
	private View btnTakePhotoView;
	
	@Before
	public void setup() {
		savedInstanceState = mock(Bundle.class);
		activity = spy(new HomeActivity() {
			@Override
			public void setContentView(int layoutResID) {
			}
		});
		setupHomeButtons();
	}
	
	@Test
	public void setTakePhotoButtonClickListener() {
		activity.onCreate(savedInstanceState);
		verify(btnTakePhotoView).setOnClickListener(Mockito.any(OnClickCapturePhotoButton.class));
	}
	
	@Test
	public void setChoosePhotoButtonClickListener() {
		activity.onCreate(savedInstanceState);
		verify(btnChoosePhotoView).setOnClickListener(Mockito.any(OnClickChoosePhotoButton.class));
	}

	@Test
	public void setContentViewToBeUiMain() throws Exception {
		activity.onCreate(savedInstanceState);
		verify(activity).setContentView(R.layout.ui_main);
	}

	private void setupHomeButtons() {
		btnTakePhotoView = mock(View.class);
		btnChoosePhotoView = mock(View.class);
		
		doReturn(btnTakePhotoView).when(activity).findViewById(R.id.btnTakePhoto);
		doReturn(btnChoosePhotoView).when(activity).findViewById(R.id.btnChoosePhoto);
	}
	
}
