package com.taskew.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.taskew.gameobjects.Terrapin;
import com.taskew.gameworld.GameWorld;

/**
 * @author Tyler Askew
 * @version 1.0
 * This class is responsible for dealing with user input by recognizing
 * what type of input it is and then calling the associated methods
 * in order to alter the game.
 */
public class InputHandler implements InputProcessor {
	
	// represents the game world
	private GameWorld world;
	
	// Represents the terrapin
	private Terrapin terp;
	
	/**
	 * Constructor that receives a Terrapin object and then
	 * assigns it to the instance variable: terp.
	 * @param terp Terrapin object representing the terp character.
	 * @return none
	 */
	public InputHandler(GameWorld world) {
		this.world = world;
		this.terp = world.getTerp();
	}
	
	/**
	 * Once a user touches down on the screen, this method will be
	 * called and then the terp's onClick method will be called resulting
	 * in the terp jumping. Also accounts for the different states of
	 * the game. Returns true to show that the touch has
	 * been handled.
	 * @inheritDoc
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (world.isReady())
			world.start();
		
		if (world.isRunning())
			terp.onClick();
		
		return true;
	}
	
	/******** THESE METHODS WILL NOT BE USED *************/

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
