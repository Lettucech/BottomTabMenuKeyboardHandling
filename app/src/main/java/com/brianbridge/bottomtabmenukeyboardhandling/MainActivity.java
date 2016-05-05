package com.brianbridge.bottomtabmenukeyboardhandling;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private View activityRootView;
	private ViewTreeObserver.OnGlobalLayoutListener keyboardShowUpListener;
	private int systemUiHeight;
	private LinearLayout container;
	private EditText editText;
	private View tabDummy;
	private boolean keyboardIsUp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		container = (LinearLayout) findViewById(R.id.container);
		tabDummy = findViewById(R.id.tabDummy);
		editText = (EditText) findViewById(R.id.editText);

		Rect r = new Rect();
		View rootView = getWindow().getDecorView();
		rootView.getWindowVisibleDisplayFrame(r);
		int screenHeight = rootView.getRootView().getHeight();
		systemUiHeight = screenHeight - r.bottom;
		if (systemUiHeight < 0) {
			systemUiHeight = 0;
		}

		activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);

		keyboardShowUpListener = new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Log.d(TAG, "keyboardShowUpListener");

				Rect r = new Rect();
				View rootView = getWindow().getDecorView();
				rootView.getWindowVisibleDisplayFrame(r);
				int screenHeight = rootView.getRootView().getHeight();

				int heightDifference = screenHeight - r.bottom - systemUiHeight;

				if (heightDifference > 0 && !keyboardIsUp) {
					Log.d(TAG, "keyboard up");
					keyboardIsUp = true;
				} else if (heightDifference == 0 && keyboardIsUp) {
					Log.d(TAG, "keyboard down");
					keyboardIsUp = false;
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							tabDummy.setVisibility(View.VISIBLE);
						}
					}, 100);
				}
			}
		};

		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(keyboardShowUpListener);

		editText.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Log.d(TAG, "onTouch");
					tabDummy.setVisibility(View.GONE);
				}
				return false;
			}
		});
	}
}
