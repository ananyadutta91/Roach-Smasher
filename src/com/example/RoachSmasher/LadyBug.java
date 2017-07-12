package com.example.RoachSmasher;

import android.graphics.Canvas;

import com.example.RoachSmasher.Assets;
import com.example.RoachSmasher.LadyBug.LadyBugState;

public class LadyBug {
	
	// States of a Bug
		enum LadyBugState {
		  Dead,	 
		  ComingBackToLife,		
		  Alive, 			// in the game
		  DrawDead,			// draw dead body on screen
		};
		
		LadyBugState state1;			// current state of bug	
		int x,y;				// location on screen (in screen coordinates)
		double speed;			// speed of bug (in pixels per second)
		// All times are in seconds
		float timeToBirth;		// # seconds till birth
		float startBirthTimer;	// starting timestamp when decide to be born
		float deathTime;		// time of death
		float animateTimer;		// used to move and animate the bug
		
		// Bug starts not alive
		public LadyBug () {
			state1 = LadyBugState.Dead;
		}
		
		// Bug birth processing
		public void birth (Canvas canvas) {
			// Bring a bug to life?
			if (state1 == LadyBugState.Dead) {
				// Set it to coming alive
				state1 = LadyBugState.ComingBackToLife;
				// Set a random number of seconds before it comes to life
				//timeToBirth = (float)Math.random () * 5;
				timeToBirth = (float)(7 + (int)(Math.random()*9));
				// Note the current time
				startBirthTimer = System.nanoTime() / 1000000000f;
			}
			// Check if bug is alive yet
			else if (state1 == LadyBugState.ComingBackToLife) {
				float curTime = System.nanoTime() / 1000000000f;
				// Has birth timer expired?
					if (curTime - startBirthTimer >= timeToBirth) {
					// If so, then bring bug to life
					state1 = LadyBugState.Alive;
					// Set bug starting location at top of screen
					x = (int)(Math.random() * canvas.getWidth());
					// Keep entire bug on screen
					if (x < Assets.ladyroach.getWidth()/2)
						x = Assets.ladyroach.getWidth()/2;
					else if (x > canvas.getWidth() - Assets.ladyroach.getWidth()/2)
						x = canvas.getWidth() - Assets.ladyroach.getWidth()/2;
					y = 0;
					// Set speed of this bug
					speed = canvas.getHeight() / 4; // no faster than 1/4 a screen per second
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
			if (state1 == LadyBugState.Alive) {
				// Get elapsed time since last call here
				float curTime = System.nanoTime() / 1000000000f;
				float elapsedTime = curTime - animateTimer;
				animateTimer = curTime;
				// Compute the amount of pixels to move (vertically down the screen)
				if(curTime-Assets.notifyCallTime < elapsedTime){
					y += (speed/2 * (elapsedTime-(Assets.notifyCallTime-Assets.waitCallTime)));
				}else{
			       y += (speed/2 * elapsedTime);
			     }
				//y += (speed * elapsedTime);
				// Draw bug on screen
				canvas.drawBitmap(Assets.ladyroach, x,  y, null);
				//canvas.drawBitmap(Assets.roach2, x,  y, null);
				// Has it reached the bottom of the screen?
				if (y >= canvas.getHeight()) {
					// Kill the bug
					//state1 = LadyBugState.Dead;
					// Subtract 1 life
					//Assets.livesLeft--;
				}
			}
		}
		
		// Process touch to see if kills bug - return true if bug killed
		public boolean touched_ladybug (Canvas canvas, int touchx, int touchy) {
			boolean touched_ladybug = false;
			// Make sure this bug is alive
			if (state1 == LadyBugState.Alive) {
				// Compute distance between touch and center of bug
				float dis = (float)(Math.sqrt ((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
				// Is this close enough for a kill?
				if (dis <= Assets.ladyroach.getWidth()*0.75f) {
					state1 = LadyBugState.DrawDead;	// need to draw dead body on screen for a while
					touched_ladybug = true;		
					// Record time of death
					deathTime = System.nanoTime() / 1000000000f;
					Assets.score++;
				}
			}	
			return (touched_ladybug);
		}
		
		// Draw dead bug body on screen, if needed
		public void drawDead (Canvas canvas) {
			if (state1 == LadyBugState.DrawDead) {
				canvas.drawBitmap(Assets.roach3, x,  y, null);			
				// Get time since death
				float curTime = System.nanoTime() / 1000000000f;
				float timeSinceDeath = curTime - deathTime;
				// Drawn dead body long enough (4 seconds) ?
				if (timeSinceDeath > 4)
					state1 = LadyBugState.Dead;
			}
		}

}
