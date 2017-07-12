package com.example.RoachSmasher;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	MainView v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Disable the title
		requestWindowFeature (Window.FEATURE_NO_TITLE);
		// Make full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Start the view
		v = new MainView(this);
		setContentView(v);
	}
	
	@Override
	protected void onPause () {
		super.onPause();
		v.pause();
		if (isFinishing())
		    if (Assets.mp != null) {
		    	Assets.mp.release();
		    	Assets.mp = null;
		    }
	}

	@Override
	protected void onResume () {
		super.onResume();
		v.resume();
	}
}

