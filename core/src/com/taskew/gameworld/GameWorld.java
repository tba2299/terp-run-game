package com.taskew.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.taskew.gameobjects.ScrollHandler;
import com.taskew.gameobjects.Terrapin;
import com.taskew.helpers.AssetLoader;
import com.taskew.helpers.InputHandler;

/**
 * @author Tyler Askew
 * @version 1.0
 * Contains all of the objects that are going to be presented to the
 * screen. This class DOES NOT draw anything to the screen, that is the
 * job of the GameRenderer class.
 */
public class GameWorld {
	
	// Creating a terrapin
	private Terrapin terp;
	
	// moves the objects along the screen
	private ScrollHandler scroller;
		
	// represents collision area of the ground
	private Rectangle ground;
	
	// contains the player's score
	private int score;
	
	// used to determine when the terp will normalize
	private int enlargedScoreCount;
	
	// represents the current state of the game and all possible game states
	private GameState currentState;
	private enum GameState {
		READY, RUNNING, PAUSED, GAMEOVER, HIGHSCORE
	}
	
	/**
	 * Default constructor that initializes all of the 
	 * instance variables in this class.
	 * @param midScreen Int containing the y-coordinate pointing to 
	 * the middle of the game's screen.
	 * @return none
	 */
	public GameWorld(int midScreen) {
		terp = new Terrapin(21, midScreen - 5, 15, 15);
		scroller = new ScrollHandler(this);
		ground = new Rectangle(0, midScreen + 100, 136, 10);
		score = 0;
		enlargedScoreCount = 0;
		currentState = GameState.READY;
	}
	
	/**
	 * This is the head update method responsible for determining
	 * the state that the game is in and then calling that state's
	 * corresponding update method. This update method controls the
	 * flow of the game states.
	 * @param delta Float containing the amount of seconds since the last
	 * time this method was called.
	 * @return void
	 */
	public void update(float delta) {
		switch (currentState) {
		case READY:
			updateReady(delta);
			break;
		case RUNNING:
			updateRunning(delta);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Calls the restart method to make sure everything
	 * is ready for correct game play.
	 * @param delta Float containing the seconds since the last time this
	 * method was called.
	 * @return void
	 */
	public void updateReady(float delta) {
		restart();
	}

	/**
	 * Updates all of the objects contained within the GameWorld so that
	 * these objects with their new attributes can be drawn to the game
	 * screen via the GameRenderer class.
	 * @param delta Float containing the number of seconds since the last
	 * time this update method was called.
	 * @return void
	 */
	public void updateRunning(float delta) {
		// puts a cap on the delta in case the game lags for a long time
		if (delta > .15f)
			delta = .15f;
		
		terp.update(delta);
		scroller.update(delta);
		
		// checks if terp is alive and if it has collided with an object
		if (scroller.collides(terp) && terp.isAlive()) {
			scroller.stop();
			terp.kill();
			AssetLoader.dead.play();
		}
		
		// checks if terp has hit the ground
		if (Intersector.overlaps(terp.getCollisionArea(), ground)) {
			scroller.stop();
			terp.kill();
			terp.decelerate();
			currentState = GameState.GAMEOVER;
						
			// changes the top five highscores if new score belongs in there
			if (score > AssetLoader.getHighScore(4)) {
				if (score < AssetLoader.getHighScore(3)) {
					AssetLoader.setHighScore(score, 4);
				} else if (score < AssetLoader.getHighScore(2)) {
					AssetLoader.setHighScore(AssetLoader.getHighScore(3), 4);
					AssetLoader.setHighScore(score, 3);
				} else if (score < AssetLoader.getHighScore(1)) {
					AssetLoader.setHighScore(AssetLoader.getHighScore(3), 4);
					AssetLoader.setHighScore(AssetLoader.getHighScore(2), 3);
					AssetLoader.setHighScore(score, 2);
				} else if (score < AssetLoader.getHighScore(0)) {
					AssetLoader.setHighScore(AssetLoader.getHighScore(3), 4);
					AssetLoader.setHighScore(AssetLoader.getHighScore(2), 3);
					AssetLoader.setHighScore(AssetLoader.getHighScore(1), 2);
					AssetLoader.setHighScore(score, 1);
				} else {
					AssetLoader.setHighScore(AssetLoader.getHighScore(3), 4);
					AssetLoader.setHighScore(AssetLoader.getHighScore(2), 3);
					AssetLoader.setHighScore(AssetLoader.getHighScore(1), 2);
					AssetLoader.setHighScore(AssetLoader.getHighScore(0), 1);
					AssetLoader.setHighScore(score, 0);
				}
				
				currentState = GameState.HIGHSCORE;
			}
		}
	}
	
	/**
	 * Returns the Terrapin object in the world.
	 * @return Terrapin object representing the terp character.
	 */
	public Terrapin getTerp() {
		return terp;
	}
	
	/**
	 * Returns the ScrollHandler object.
	 * @return ScrollHandler object that moves objects along the screen.
	 * @return
	 */
	public ScrollHandler getScroller() {
		return scroller;
	}
	
	/**
	 * Returns the player's score.
	 * @return Int representing the player's score.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Updates the score variable by adding the
	 * increment variable being passed in to it.
	 * @param increment Int containing the score amount to increment by.
	 * @return void
	 */
	public void addScore(int increment) {
		score += increment;
	}
	
	/**
	 * Returns the score count used to determine when the terp
	 * will go back to a normal size state.
	 * @return Int containing the score count during enlarged state of terp.
	 */
	public int getEnlargedScoreCount() {
		return enlargedScoreCount;
	}
	
	/**
	 * Sets the enlarged score count to the value being passed in.
	 * @param newCount Int containing the new enlarged score count.
	 */
	public void setEnlargedScoreCount(int newCount) {
		enlargedScoreCount = newCount;
	}
	
	/**
	 * Determines if the game is in the ready state. Returns true if it
	 * is and false otherwise.
	 * @return boolean
	 */
	public boolean isReady() {
		return currentState == GameState.READY;
	}
	
	/**
	 * Determines if the game is in the running state. Returns true if it
	 * is and false otherwise.
	 * @return boolean
	 */
	public boolean isRunning() {
		return currentState == GameState.RUNNING;
	}
	
	/**
	 * Determines if the game is in the game over state. Returns true if it
	 * is and false otherwise.
	 * @return boolean
	 */
	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}
	
	/**
	 * Determines if the game is in the high score state. Returns true if it
	 * is and false otherwise.
	 * @return boolean
	 */
	public boolean isHighScore() {
		return currentState == GameState.HIGHSCORE;
	}
	
	/**
	 * Determines if the game is in the paused state. Returns true if so and
	 * false otherwise.
	 * @return boolean
	 */
	public boolean isPaused() {
		return currentState == GameState.PAUSED;
	}
	
	/**
	 * Changes the game state to the READY state.
	 * @return void
	 */
	public void getReady() {
		currentState = GameState.READY;
	}
	
	/**
	 * Changes the game state to the running state.
	 * @return void
	 */
	public void start() {
		currentState = GameState.RUNNING;
	}
	
	/**
	 * Sets the current state to the paused state.
	 * @return void
	 */
	public void pause() {
		currentState = GameState.PAUSED;
	}
	
	/**
	 * Restarts all of the game's components and sets the
	 * current state to the ready state. Also resets the input processor.
	 * @return void
	 */
	public void restart() {
		Gdx.input.setInputProcessor(new InputHandler(this));
		score = 0;
		enlargedScoreCount = 0;
		terp.restart();
		scroller.restart();
		currentState = GameState.READY;
	}

}
