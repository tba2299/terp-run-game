package com.taskew.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.taskew.gameworld.GameRenderer;
import com.taskew.gameworld.GameWorld;
import com.taskew.helpers.InputHandler;

/**
 * @author Tyler Askew
 * @version 1.0
 * Simple class that displays the game screen by
 * calling its render method. This class utilizes the
 * GameWorld and GameRenderer classes in order to display the screen
 * to the user.
 */
public class GameScreen implements Screen {
	
	// the game world, its renderer, and the entire game (Game object)
	private GameWorld world;
	private GameRenderer renderer;
	private Game game;
	
	// keeps track of how long the game has been running
	private float runtime;
	
	/**
	 * Default constructor that initializes the
	 * instance variables.
	 * @return none
	 */
	public GameScreen(Game game) {
		// just started so runtime is 0
		runtime = 0;
		
		// initializes the game
		this.game = game;
		
		// finding the screen height and width
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		
		// finding the size of the height of gameplay's screen relative
		// to the actual screen's height
		float gameWidth = 136;
		float gameHeight = screenHeight / (screenWidth / gameWidth);
		
		// calculate the midpoint of the gameplay's screen
		int midScreen = (int) (gameHeight / 2);
		
		// create the game world and its associated renderer
		world = new GameWorld(midScreen);
		renderer = new GameRenderer(world, game, (int) gameHeight, midScreen);
		
		// changes the game state to the READY state
		world.getReady();
		
		// setting the input processor to the InputHandler class
		Gdx.input.setInputProcessor(new InputHandler(world));
	}

	/**
	 * Makes calls to the update method of the GameWorld class
	 * and the render method of the GameRenderer class. This class'
	 * render method is constantly called and therefore these two
	 * methods inside it will be constantly called. (like a loop)
	 * @param delta A float containing the number of seconds that have passed
	 * since this method was last called.
	 * @return void
	 */
	@Override
	public void render(float delta) {
		runtime += delta;  // calculating total runtime of the game
		world.update(delta);  // updates the objects contained in the world
		renderer.render(runtime);  // refreshes world to display new objects
	}
	
	/**
	 * Returns the world that the game screen is displaying.
	 * @return GameWorld object that represents all of the objects
	 * in the game.
	 */
	public GameWorld getWorld() {
		return world;
	}
	
	/*********** THESE METHODS WILL NOT BE USED ************/

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
