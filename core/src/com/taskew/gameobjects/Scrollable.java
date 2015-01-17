package com.taskew.gameobjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Tyler Askew
 * @version 1.0
 * This class describes a scrolling object in the game screen. It
 * defines the object's scrolling speed, width, height, and starting 
 * position as well as several methods for updating the object.
 */
public class Scrollable {

	// Variables
	protected Vector2 position, velocity;
	protected int width, height;
	protected float startingY, shiftAmount;
	protected int rotation;
	protected boolean finishedScroll, isShifting, shiftSwitch;
	
	// used to calculate random heights
	protected Random random;
	
	/**
	 * Constructor that creates a new Scrollable object with
	 * the specified parameters.
	 * @param x Float containing the x-coordinate position of the object.
	 * @param y Float containing the y-coordinate position of the object.
	 * @param width Int holding the width of the object.
	 * @param height Int holding the height of the object.
	 * @param scrollSpeed Float containing the speed of the scrolling object.
	 * @return none
	 */
	public Scrollable(float x, float y, 
			int width, int height, float scrollSpeed) {
		position = new Vector2(x, y);
		velocity = new Vector2(scrollSpeed, 0);
		startingY = position.y;
		random = new Random();
		this.width = width;
		this.height = height;
		finishedScroll = false;
		isShifting = false;
		shiftSwitch = false;
		shiftAmount = 0;
		rotation = 0;
	}
	
	/**
	 * Updates the position of the scrolling object by adding the
	 * product of the scrolling speed and the number of seconds this method
	 * was last called to the previous position.
	 * @param delta Float containing the number of seconds since the last
	 * time this method was called.
	 * @return void
	 */
	public void update(float delta) {
		position.add(velocity.cpy().scl(delta));
		
		// shifts the objects up and down by the shiftAmount
		if (isShifting) {
			if (!shiftSwitch) {
				if (Math.floor(position.y - startingY) <= shiftAmount)
					position.y += (shiftAmount * delta);
				else
					shiftSwitch = true;
			} else {
				if (Math.floor(position.y) != Math.floor(startingY))
					position.y -= (shiftAmount * delta);
				else
					shiftSwitch = false;
			}
		}
	}
	
	/**
	 * Resets an object by setting an object that has scrolled off the screen
	 * to a new x-coordinate position so that it gets put back into the
	 * gameplay. Then, sets the finishedScroll variable back to false since it
	 * is now visible once again.
	 * @param newX Float containing the new position of the scrolling object.
	 * @return void
	 */
	public void reset(float newX) {
		position.x = newX;
		finishedScroll = false;
	}
	
	/**
	 * Restarts anything that might have been changed during the
	 * game by changing it back to its original state.
	 * @param scrollSpeed Float containing original scroll speed.
	 */
	public void restart(float scrollSpeed) {
		velocity.x = scrollSpeed;
		position.y = startingY;
		isShifting = false;
		shiftSwitch = false;
		shiftAmount = 0;
	}
	
	/**
	 * Stops the scrolling objects from scrolling across the screen.
	 * @return void
	 */
	public void stop() {
		velocity.x = 0;
	}
	
	/**
	 * Returns the x-coordinate of the left side of the object.
	 * @return Float containing x-coordinate of the left side of the object.
	 */
	public float getLeftX() {
		return position.x;
	}
	
	/**
	 * Returns the x-coordinate of the right side of the object.
	 * @return Float containing x-coordinate of the right side of the object.
	 */
	public float getRightX() {
		return position.x + width;
	}
	
	/**
	 * Returns the y-coordinate of the object.
	 * @return Float containing y-coordinate of the object.
	 */
	public float getY() {
		return position.y;
	}
	
	/**
	 * Returns the width of the object.
	 * @return Int containing the width of the object.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the height of the object.
	 * @return Int containing the height of the object.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the rotation of the object.
	 * @return Int containing the rotation of the object.
	 */
	public int getRotation() {
		return rotation;
	}
	
	/**
	 * Rotates the object to a different starting position.
	 * @param r Int containing the degrees to rotate.
	 */
	public void setStartingRotation(int r) {
		rotation = r;
	}
	
	/**
	 * Rotates the object by whatever degree is given.
	 * @param r Int containing the new rotation addition.
	 */
	public void rotate(int r) {
		rotation += r;
	}
	
	/**
	 * Returns true if object has scrolled across the screen or
	 * false if object is still scrolling across the screen.
	 * @return Boolean containing true/false depending on object's position.
	 */
	public boolean isFinishedScrolling() {
		return finishedScroll;
	}
	
	/**
	 * Returns true if object is shifting up and down. Returns false if
	 * object is not.
	 * @return boolean
	 */
	public boolean isShifting() {
		return isShifting;
	}
	
	/**
	 * Returns the amount of pixels that the object is being shifted.
	 * @return A float containing the shift amount for that object.
	 */
	public float getShiftAmount() {
		return shiftAmount;
	}
	
	/**
	 * Returns the current scrolling speed.
	 * @return Float containing the current scrolling speed.
	 */
	public float getScrollingSpeed() {
		return velocity.x;
	}
	
	/**
	 * Modifies the scroll speed of the object by the amount
	 * specified through the parameter.
	 * @param s Float containing addition to the scroll speed.
	 */
	public void modifyScrollSpeed(float s) {
		// puts a cap on the scrolling speed
		if (velocity.x > -89)
			velocity.x -= s;
	}
	
	/**
	 * Sets the isShifting variable to true and then adds the parameter
	 * amount to the shifting amount of the object.
	 * @param s Float containing the new amount to increase shifting.
	 */
	public void shift(float s) {
		if (!isShifting)
			isShifting = true;
		
		// puts a cap on the amount of shifting
		if (shiftAmount < 100) 
			shiftAmount += s;
	}
	
}
