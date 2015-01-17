package com.taskew.terprun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.taskew.helpers.AssetLoader;
import com.taskew.screens.MainMenuScreen;

/**
 * @author Tyler Askew
 * @version 1.0
 * Base class for the entire game. Loads all of the textures the game
 * will use and sets the games screen. Also disposes of the game and
 * its resources once the user closes the game.
 */
public class TerpRunClient extends Game {

	/**
	 * Starts the game by creating the game screen and loading all
	 * of the game's assets.
	 * @return void
	 */
	@Override
	public void create() {		
		// loading all assets for the game
		AssetLoader.load();
		
		// sets the screen to display the main menu upon start up
		setScreen(new MainMenuScreen(getGame()));
	}

	/**
	 * Disposing of all assets and other components of the game.
	 * @return void
	 */
	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}
	
	/**
	 * Returns the game object in order to switch screens.
	 * @return Game object used to switch screens.
	 */
	public TerpRunClient getGame() {
		return this;
	}
	
}
