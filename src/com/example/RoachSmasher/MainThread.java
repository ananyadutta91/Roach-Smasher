package com.example.RoachSmasher;

import java.util.Random;

import com.example.RoachSmasher.Bug.BugState;
import com.example.RoachSmasher.LadyBug.LadyBugState;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainThread extends Thread {
	private SurfaceHolder holder;
	private Handler handler;		// required for running code in the UI thread
	private boolean isRunning = false;
	Context context;
	Paint paint;
	int touchx, touchy;	// x,y of touch event
	int touch_count=0;
	boolean touched;	// true if touch happened
	
	private static final Object lock = new Object(); 

	public MainThread (SurfaceHolder surfaceHolder, Context context) {
	   holder = surfaceHolder;
	   this.context = context;
	   handler = new Handler();
	   touched = false;
	}
	
	public void setRunning(boolean b) {
		isRunning = b;	// no need to synchronize this since this is the only line of code to writes this variable
	}
	
	// Set the touch event x,y location and flag indicating a touch has happened
	public void setXY (int x, int y) {
		synchronized (lock) {
			touchx = x;
			touchy = y;
			this.touched = true;
		}
	}
	
	//make wait when paused
	public void makeWait () {
		Assets.pauseClicked =1;
		synchronized(lock) {
	        while (Assets.pauseClicked == 1) {
	            try {
	            	Assets.waitCallTime = System.nanoTime() / 1000000000f;
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	}
	//make notify if play
	public void makeNotify () {
			synchronized(lock) {	
		        lock.notifyAll();
		        Assets.notifyCallTime = System.nanoTime() / 1000000000f;
		        Log.i("notified", "notified");
	   }
    }
	
	//For Background Music
	 private void playMediaAudio(int resId,boolean loop){
	    	if(Assets.mp != null){
	    		Assets.mp.release();
	    	}
	    	Assets.mp=MediaPlayer.create(context, resId);
	    	Assets.mp.start();
	    	Assets.mp.setLooping(loop);
	    
				if (Assets.mp != null)
					Assets.mp.setVolume(1, 1);				
			
	    }
	 
	@Override
	public void run() {
		while (isRunning) {
			// Lock the canvas before drawing
			Canvas canvas = holder.lockCanvas();	
			if (canvas != null) {
				// Perform drawing operations on the canvas
				render(canvas);		
				// After drawing, unlock the canvas and display it
				holder.unlockCanvasAndPost (canvas);	
			}
		}
	}
	
	// Loads graphics, etc. used in game
	private void loadData (Canvas canvas) {
		Bitmap bmp;
		int newWidth, newHeight;
		float scaleFactor;
		
		// Create a paint object for drawing vector graphics
		paint = new Paint();
		
		// Load score bar
		bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.scorebar);
		// Compute size of bitmap needed (suppose want height = 10% of screen height)
		newHeight = (int)(canvas.getHeight() * 0.1f);
		// Scale it to a new size
		Assets.scorebar = Bitmap.createScaledBitmap (bmp, canvas.getWidth(), newHeight, false);
		// Delete the original
		bmp = null;	
		
		
	//load pause button
			bmp = null;		
			bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.pause);
			newHeight = (int)(canvas.getHeight() * 0.08f);
			// Scale it to a new size
			Assets.pause = Bitmap.createScaledBitmap (bmp, canvas.getWidth()/10, newHeight, false);
		
				
		// Load food bar
		bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.food);
		// Compute size of bitmap needed (suppose want height = 10% of screen height)
		newHeight = (int)(canvas.getHeight() * 0.1f);
		// Scale it to a new size
		Assets.foodbar = Bitmap.createScaledBitmap (bmp, canvas.getWidth(), newHeight, false);
		// Delete the original
		bmp = null;		
		
		// Load roach1
		bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.roach1);
		// Compute size of bitmap needed (suppose want width = 20% of screen width)
		newWidth = (int)(canvas.getWidth() * 0.2f);
		// What was the scaling factor to get to this?
		scaleFactor = (float)newWidth / bmp.getWidth();
		// Compute the new height
		newHeight = (int)(bmp.getHeight() * scaleFactor);
		// Scale it to a new size
		Assets.roach1 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
		// Delete the original
		bmp = null;
		
		// Load roach2
		bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.roach2);
		// Compute size of bitmap needed (suppose want width = 20% of screen width)
		newWidth = (int)(canvas.getWidth() * 0.2f);
		// What was the scaling factor to get to this?
		scaleFactor = (float)newWidth / bmp.getWidth();
		// Compute the new height
		newHeight = (int)(bmp.getHeight() * scaleFactor);
		// Scale it to a new size
		Assets.roach2 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
		// Delete the original
		bmp = null;
		

		// Load lady bug
		bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ladyroach);
		// Compute size of bitmap needed (suppose want width = 20% of screen width)
		newWidth = (int)(canvas.getWidth() * 0.2f);
		// What was the scaling factor to get to this?
		scaleFactor = (float)newWidth / bmp.getWidth();
		// Compute the new height
		newHeight = (int)(bmp.getHeight() * scaleFactor);
		// Scale it to a new size
		Assets.ladyroach = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
		// Delete the original
		bmp = null;
		
		// Load super bug
				bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.superroach);
				// Compute size of bitmap needed (suppose want width = 20% of screen width)
				newWidth = (int)(canvas.getWidth() * 0.2f);
				// What was the scaling factor to get to this?
				scaleFactor = (float)newWidth / bmp.getWidth();
				// Compute the new height
				newHeight = (int)(bmp.getHeight() * scaleFactor);
				// Scale it to a new size
				Assets.superroach = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
				// Delete the original
				bmp = null;
				
		
		// Load the leaf
		
		bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.leaf);
		// Compute size of bitmap needed (suppose want width = 20% of screen width)
		newWidth = (int)(canvas.getWidth() * 0.5f);
		// What was the scaling factor to get to this?
		scaleFactor = (float)newWidth / bmp.getWidth();
		// Compute the new height
		newHeight = (int)(bmp.getHeight() * scaleFactor);
		// Scale it to a new size
		Assets.leaf = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
		// Delete the original
		bmp = null;
		// ...
		
		// Load roach3 (dead bug)
		bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.dead);
		// Compute size of bitmap needed (suppose want width = 20% of screen width)
		newWidth = (int)(canvas.getWidth() * 0.2f);
		// What was the scaling factor to get to this?
		scaleFactor = (float)newWidth / bmp.getWidth();
		// Compute the new height
		newHeight = (int)(bmp.getHeight() * scaleFactor);
		// Scale it to a new size
		Assets.roach3 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
		// Delete the original
		bmp = null;
		
		// Create a bug
		Assets.bug = new Bug();
		Assets.bug1 = new Bug();
		Assets.bug2 = new Bug();
		//Assets.bug3 = new Bug();
		Assets.lady_bug = new LadyBug();
		Assets.super_bug = new SuperBug();
		
		// Define Leaf object
		Assets.leaf1= new Leaf();
		Assets.leaf2= new Leaf();
		
		//Initialize the score variable
		Assets.score=0;
	}

	// Load specific background screen
	private void loadBackground (Canvas canvas, int resId) {
		// Load background
		Bitmap bmp = BitmapFactory.decodeResource (context.getResources(), resId);
		// Scale it to fill entire canvas
		Assets.background = Bitmap.createScaledBitmap (bmp, canvas.getWidth(), canvas.getHeight(), false);
		// Delete the original
		bmp = null;						
	}

	
	private void render (Canvas canvas) {
		int i, x, y;
		
		switch (Assets.state) {
			case GettingReady:
				// Load a special "getting ready screen"
			    loadBackground (canvas, R.drawable.title2);
				// Load data and other graphics needed by game
				loadData(canvas);
				// Draw the background screen
				canvas.drawBitmap (Assets.background, 0, 0, null);
				// Play a sound effect
				Assets.soundPool.play(Assets.sound_getready, 1, 1, 1, 0, 1);
				// Start a timer
				Assets.gameTimer = System.nanoTime() / 1000000000f;
				// Go to next state
				Assets.state = Assets.GameState.Starting;
				break;
			case Starting:
				// Draw the background screen
				canvas.drawBitmap (Assets.background, 0, 0, null);
				// Has 3 seconds elapsed?
				float currentTime = System.nanoTime() / 1000000000f;
				if (currentTime - Assets.gameTimer >= 3) {
					// Load game play background
					loadBackground (canvas, R.drawable.background);
					// Goto next state
					
					playMediaAudio(R.raw.background,true);
					Assets.state = Assets.GameState.Running;
				}
				break;
			case Running:
				
				// Draw the background screen
				canvas.drawBitmap (Assets.background, 0, 0, null);
				
				// Draw the score bar at top of screen
				canvas.drawBitmap(Assets.scorebar,canvas.getWidth()-Assets.scorebar.getWidth(),0, null);               
				// Draw the score on the score bar
				// canvas.drawText(....)
				paint.setColor(Color.WHITE); // Text Color
	            //paint.setStrokeWidth(12); // Text Size
				paint.setTextSize(20);
	            canvas.drawText( Assets.current_score, 10, 20, paint);
	            
				// Draw the foodbar at bottom of screen
				canvas.drawBitmap (Assets.foodbar, 0, canvas.getHeight()-Assets.foodbar.getHeight(), null);	
				//canvas.getWidth()/2-Assets.pause.getWidth()/2,0,

				// Draw the pause button
				canvas.drawBitmap(Assets.pause,(canvas.getWidth()/2)-(Assets.pause.getWidth()/2),
						(canvas.getHeight()/98+(Assets.scorebar.getHeight()-Assets.pause.getHeight())/2), null);
				
				// Draw one circle for each life at top right corner of screen
				// Let circle radius be 5% of width of screen
				int radius = (int)(canvas.getWidth() * 0.05f);
				int spacing = 8; // spacing in between circles
				x = canvas.getWidth() - radius - spacing;	// coordinates for rightmost circle to draw
				y = radius + spacing;
				for (i=0; i<Assets.livesLeft; i++) {
					paint.setColor(Color.GREEN);
					paint.setStyle(Style.FILL);
					canvas.drawCircle(x, y, radius, paint);
					paint.setColor(Color.BLACK);
					paint.setStyle(Style.STROKE);
					canvas.drawCircle(x, y, radius, paint);
					// Reposition to draw the next circle to the left
					x -= (radius*2 + spacing);
				}
				
				
				// Process a touch for  bug
				
				if (touched) {
					// Set touch flag to false since we are processing this touch now
					touched = false;
					//touchx >150 && touchx <200 && touchy > 25 && touchy < 75
					
					if(touchx >= Assets.scorebar.getWidth()/2- Assets.pause.getWidth() && 
							   touchx <= Assets.scorebar.getWidth()/2+ Assets.pause.getWidth() && 
							   touchy > Assets.pause.getHeight()/2 && 
							   touchy < Assets.pause.getHeight()*3){
						if(Assets.pauseClicked == 0){
							
						    makeWait();
						    Log.i("paused", "paused");
						}
					}
					
					
					
					// See if this touch killed a bug
					boolean bugKilled = Assets.bug.touched(canvas, touchx, touchy);
					boolean bugKilled1 = Assets.bug.touched(canvas, touchx, touchy);
					boolean bugKilled2 = Assets.bug.touched(canvas, touchx, touchy);
					//boolean bugKilled3 = Assets.bug.touched(canvas, touchx, touchy);
					
					
					
				    boolean ladybugKilled = Assets.lady_bug.touched_ladybug(canvas, touchx, touchy);
				    boolean superbugKilled = Assets.super_bug.touched_superbug(canvas, touchx, touchy);
					
					boolean leafKilled1=Assets.leaf1.touched(canvas, touchx, touchy);
					boolean leafKilled2=Assets.leaf2.touched(canvas, touchx, touchy);
					
					int sound;
					
					//if (bugKilled ||bugKilled1 ||bugKilled2||bugKilled3){
						if (bugKilled ||bugKilled1||bugKilled2){

					    sound=1;
						Random rand = new Random();
						sound=rand.nextInt((3 - 1) + 1) + 1;
							if(sound==1)Assets.soundPool.play(Assets.sound_squish1, 1, 1, 1, 0, 1);
						    if(sound==2)Assets.soundPool.play(Assets.sound_squish2, 1, 1, 1, 0, 1);
						    if(sound==3)Assets.soundPool.play(Assets.sound_squish3, 1, 1, 1, 0, 1);
						    
						    Assets.score++;
						    Assets.current_score = "Score: " + Assets.score;
					}
					
					if(leafKilled1 || leafKilled2)
						{
							//Assets.soundPool.play(Assets.sound_squish, 1, 1, 1, 0, 1);
							Assets.livesLeft++;
							
					}
					
					else if (superbugKilled){
						Assets.soundPool.play(Assets.sound_superbug, 1, 1, 1, 0, 1);
					    Assets.score=Assets.score +10;
						Assets.current_score = "Score: " + Assets.score;
					}
					else if(ladybugKilled){
						Assets.soundPool.play(Assets.sound_ladybug, 1, 1, 1, 0, 1);
						Assets.state = Assets.GameState.GameEnding;
					}
					
					
					else
					{
						Assets.soundPool.play(Assets.sound_thump, 1, 1, 1, 0, 1);
					}
					
				}
				
					
				

				// Draw dead bugs on screen
				Assets.bug.drawDead(canvas);
				Assets.bug1.drawDead(canvas);
				Assets.bug2.drawDead(canvas);
				//Assets.bug3.drawDead(canvas);*/
				Assets.lady_bug.drawDead(canvas);
			    Assets.super_bug.drawDead(canvas);
				
				// Move bugs on screen
				Assets.bug.move(canvas);
				Assets.bug1.move(canvas);
				Assets.bug2.move(canvas);
				//Assets.bug3.move(canvas);*/
				Assets.lady_bug.move(canvas);
				Assets.super_bug.move(canvas);
				
				// Bring a dead bug to life?
				Assets.bug.birth(canvas);
				Assets.bug1.birth(canvas);
				Assets.bug2.birth(canvas);
				//Assets.bug3.birth(canvas);*/
				Assets.lady_bug.birth(canvas);
				Assets.super_bug.birth(canvas);
				
				
				if(Assets.livesLeft==1)
				{
					//Log.i("ProjectLogging","Here....");
					Assets.leaf1.drawDead(canvas);
					Assets.leaf1.birth(canvas);
					Assets.leaf1.move(canvas);
				}
				
				if(Assets.livesLeft==2)
				{
					//Log.i("ProjectLogging","Here....");
					Assets.leaf2.drawDead(canvas);
					Assets.leaf2.birth(canvas);
					Assets.leaf2.move(canvas);
				}

				
				// Are no lives left?
					else if (Assets.livesLeft == 0)
					{
					// Goto next state
					Assets.state = Assets.GameState.GameEnding;
					}
				break;
				

			case GameEnding:
				// Show a game over message
				handler.post(new Runnable() {
					public void run() {	
						Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show();  
											}
				});
				// Goto next state
				Assets.state = Assets.GameState.GameOver;
				break;
			case GameOver:
	
				// Fill the entire canvas' bitmap with 'black'
				canvas.drawColor(Color.BLACK);
				
				


				//Get the current score from preferences
				SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
		    	int highscore=sharedPreferences.getInt("prefs_highscore", 0);
		    	
		    	//Check for the new high score has been reached or not
		    	if(Assets.score>highscore)
		    	{
		    		//save the new high score
		    		SharedPreferences.Editor editor=sharedPreferences.edit();
		    		editor.putInt("prefs_highscore", Assets.score);
		    		editor.commit();
		    		
		    		// Show a new high score message
					handler.post(new Runnable() {
						public void run() {	
							Toast.makeText(context, "New High Score has been reached", Toast.LENGTH_LONG).show();  	
						}
					});
		    		
		    	
					
		    		
		    		
		    	}
		    	
		    	
		    	Assets.mp.setVolume(0, 0);
				isRunning=false;
				break;
				
		}	
	}
}

