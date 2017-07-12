package com.example.RoachSmasher;

import com.example.RoachSmasher.R;
import com.example.RoachSmasher.PrefsActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class TitleActivity extends Activity implements OnClickListener{
	
    ImageView imageView1;
    ImageButton button1, button2;

	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		/*setContentView(new MyView(this));*/
		
		imageView1 =(ImageView) findViewById (R.id.imageView1);
 		button1 = (ImageButton) findViewById (R.id.button1);
  		button2 = (ImageButton) findViewById (R.id.button2);
 		
 		button1.setOnClickListener(this); // play game button
 		button2.setOnClickListener(this); // high score button
 		
 		
 	    // setting the title screen 
 		imageView1.setImageResource(R.drawable.title);
 		
	}
	
	
	  public void onClick(View v){

		if (v ==button1){
			
			// On click switch to main (game) activity
			startActivity(new Intent(TitleActivity.this, MainActivity.class));
		}
		
		else if (v ==button2){
			
			startActivity (new Intent(this, PrefsActivity.class));
		}
		
		else
			
		{
			button1.setEnabled(true);
			button2.setEnabled(true);
			
			
		
		}
	
	}
}
	
