package com.example.RoachSmasher;

import java.util.Random;

import android.graphics.Canvas;

import com.example.RoachSmasher.Assets;
import com.example.RoachSmasher.Bug.BugState;

public class Bug {

	// States of a Bug
	enum BugState {
	   Dead,	 
	  ComingBackToLife,		
	  Alive, 			// in the game
	  DrawDead,			// draw dead body on screen
	};
	
	BugState state;			// current state of bug	
	int x,y;				// location on screen (in screen coordinates)
	double speed;			// speed of bug (in pixels per second)
	// All times are in seconds
	float timeToBirth;		// # seconds till birth
	float startBirthTimer;	// starting timestamp when decide to be born
	float deathTime;		// time of death
	float animateTimer;		// used to move and animate the bug
	
	// Bug starts not alive
	public Bug () {
		state = BugState.Dead;
	}
	
	Random rd=new Random();
	
	// Bug birth processing
	public void birth (Canvas canvas) {
		// Bring a bug to life?
		if (state == BugState.Dead) {
			// Set it to coming alive
			state = BugState.ComingBackToLife;
			// Set a random number of seconds before it comes to life
			timeToBirth = (float)Math.random () * 5;
			// Note the current time
			startBirthTimer = System.nanoTime() / 1000000000f;
		}
		// Check if bug is alive yet
		else if (state == BugState.ComingBackToLife) {
			float curTime = System.nanoTime() / 1000000000f;
			// Has birth timer expired?
			if (curTime - startBirthTimer >= timeToBirth) {
				// If so, then bring bug to life
				state = BugState.Alive;
				// Set bug starting location at top of screen
				x = (int)(Math.random() * canvas.getWidth());
				// Keep entire bug on screen
				if (x < Assets.roach1.getWidth()/2)
					x = Assets.roach1.getWidth()/2;
				else if (x > canvas.getWidth() - Assets.roach1.getWidth()/2)
					x = canvas.getWidth() - Assets.roach1.getWidth()/2;
				y = 0;
				// Set speed of this bug
				speed = canvas.getHeight() /4;
				
				/*int temp=rd.nextInt(2);
                if(temp==0)
                {
                speed = canvas.getHeight() /5; 
                }
                else if(temp==1)
                {
                    
                }
                else if(temp==2)
                {
                    speed = canvas.getHeight() /3;
                }*/
				//speed = canvas.getHeight() /4; // no faster than 1/4 a screen per second
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
		if (state == BugState.Alive) {
			// Get elapsed time since last call here
			float curTime = System.nanoTime() / 1000000000f;
			float elapsedTime = curTime - animateTimer;
			animateTimer = curTime;
		/*	// Compute the amount of pixels to move (vertically down the screen)
			y += (speed * elapsedTime);*/
			// Compute the amount of pixels to move (vertically down the screen)
			if(curTime-Assets.notifyCallTime < elapsedTime){
				y += (speed * (elapsedTime-(Assets.notifyCallTime-Assets.waitCallTime)));
			}else{
		       y += (speed * elapsedTime);
		     }
		//	boolean f=true;
			if(y%2==0)
			{
				canvas.drawBitmap(Assets.roach1, x,  y, null);	
				
			}
			else 
			{
				canvas.drawBitmap(Assets.roach2, x,  y, null);	
				
			}
			
			
			// Draw bug on screen
			
			// Has it reached the bottom of the screen?
			if (y >= canvas.getHeight()) {
				// Kill the bug
				state = BugState.Dead;
				// Subtract 1 life
				Assets.livesLeft--;
			}
		}
	}
	
	// Process touch to see if kills bug - return true if bug killed
	public boolean touched (Canvas canvas, int touchx, int touchy) {
		boolean touched = false;
		// Make sure this bug is alive
		if (state == BugState.Alive) {
			// Compute distance between touch and center of bug
			float dis = (float)(Math.sqrt ((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
			// Is this close enough for a kill?
			if (dis <= Assets.roach1.getWidth()*0.75f) {
				state = BugState.DrawDead;	// need to draw dead body on screen for a while
				touched = true;		
				// Record time of death
				deathTime = System.nanoTime() / 1000000000f;
				
			}
		}	
		return (touched);
	}
	
	
	
	// Draw dead bug body on screen, if needed
	public void drawDead (Canvas canvas) {
		if (state == BugState.DrawDead) {
			canvas.drawBitmap(Assets.roach3, x,  y, null);			
			// Get time since death
			float curTime = System.nanoTime() / 1000000000f;
			float timeSinceDeath = curTime - deathTime;
			// Drawn dead body long enough (4 seconds) ?
			if (timeSinceDeath > 4)
				state = BugState.Dead;
		}
	}
	
}
