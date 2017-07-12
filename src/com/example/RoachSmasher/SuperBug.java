package com.example.RoachSmasher;


import android.graphics.Canvas;
import com.example.RoachSmasher.Assets;
import com.example.RoachSmasher.SuperBug.SuperBugState;


public class SuperBug {
	
	// States of a Bug
	enum SuperBugState {
	   Dead,	 
	  ComingBackToLife,		
	  Alive, 			// in the game
	  DrawDead,			// draw dead body on screen
	};
	
	SuperBugState state;			// current state of bug	
	int x,y;				// location on screen (in screen coordinates)
	double speed;			// speed of bug (in pixels per second)
	// All times are in seconds
	float timeToBirth;		// # seconds till birth
	float startBirthTimer;	// starting timestamp when decide to be born
	float deathTime;		// time of death
	float animateTimer;		// used to move and animate the bug
	
	// Bug starts not alive
	public SuperBug () {
		state = SuperBugState.Dead;
	}
	
	// Bug birth processing
	public void birth (Canvas canvas) {
		// Bring a bug to life?
		if (state == SuperBugState.Dead) {
			// Set it to coming alive
			state = SuperBugState.ComingBackToLife;
			// Set a random number of seconds before it comes to life
			//timeToBirth = (float)Math.random () * 5;
			timeToBirth = 20;
			// Note the current time
			startBirthTimer = System.nanoTime() / 1000000000f;
		}
		// Check if bug is alive yet
		else if (state == SuperBugState.ComingBackToLife) {
			float curTime = System.nanoTime() / 1000000000f;
			// Has birth timer expired?
			if (curTime - startBirthTimer >= timeToBirth) {
				// If so, then bring bug to life
				state = SuperBugState.Alive;
				// Set bug starting location at top of screen
				x = (int)(Math.random() * canvas.getWidth());
				// Keep entire bug on screen
				if (x < Assets.superroach.getWidth()/2)
					x = Assets.superroach.getWidth()/2;
				else if (x > canvas.getWidth() - Assets.superroach.getWidth()/2)
					x = canvas.getWidth() - Assets.superroach.getWidth()/2;
				y = 0;
				// Set speed of this bug
				speed = canvas.getHeight() /4; // no faster than 1/4 a screen per second
				// subtract a random amount off of this so some bugs are a little slower
				// ADD CODE HERE
				// Record timestamp of this bug being born
				animateTimer = curTime;
			}
		}	
	}
	
	// Bug movement processing
	public void move (Canvas canvas) {
		// Make sure this bug is alive
		if (state == SuperBugState.Alive) {
			// Get elapsed time since last call here
			float curTime = System.nanoTime() / 1000000000f;
			float elapsedTime = curTime - animateTimer;
			animateTimer = curTime;
			// Compute the amount of pixels to move (vertically down the screen)
			//y += (speed * elapsedTime);
			if(curTime-Assets.notifyCallTime < elapsedTime){
				y += (speed/2 * (elapsedTime-(Assets.notifyCallTime-Assets.waitCallTime)));
			}else{
		       y += (speed/2 * elapsedTime);
			}
			
			// Draw bug on screen
			canvas.drawBitmap(Assets.superroach, x,  y, null);
			// Has it reached the bottom of the screen?
			if (y >= canvas.getHeight()) {
				// Kill the bug
				state = SuperBugState.Dead;
				// Subtract 1 life
				//Assets.livesLeft--;
			}
		}
	}
	
	// Process touch to see if kills bug - return true if bug killed
	public boolean touched_superbug (Canvas canvas, int touchx, int touchy) {
		boolean touched = false;
		// Make sure this bug is alive
		if (state == SuperBugState.Alive) {
			
			
			// Compute distance between touch and center of bug
			float dis = (float)(Math.sqrt ((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
			// Is this close enough for a kill?
			if (dis <= Assets.superroach.getWidth()*0.75f) {
				
				Assets.touch_count++;
				if (Assets.touch_count<4)
				{
					return false;
				}
				
				else
				{
				state = SuperBugState.DrawDead;	// need to draw dead body on screen for a while
				touched = true;		
				// Record time of death
				deathTime = System.nanoTime() / 1000000000f;
				Assets.touch_count=0;
				}
				
			}
		}	
		return (touched);
	}
	
	// Draw dead bug body on screen, if needed
	public void drawDead (Canvas canvas) {
		if (state == SuperBugState.DrawDead) {
			canvas.drawBitmap(Assets.roach3, x,  y, null);			
			// Get time since death
			float curTime = System.nanoTime() / 1000000000f;
			float timeSinceDeath = curTime - deathTime;
			// Drawn dead body long enough (4 seconds) ?
			if (timeSinceDeath > 4)
				state = SuperBugState.Dead;
		}
	}

}
