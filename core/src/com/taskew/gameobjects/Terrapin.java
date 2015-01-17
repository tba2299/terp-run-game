package com.taskew.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.taskew.helpers.AssetLoader;

/**
 * @author Tyler Askew
 * @version 1.0
 * Contains all methods and variables that represent the terrapin's
 * actions and attributes.
 */
public class Terrapin {
	
	// Represents terp's position, speed, and change in speed.
	private Vector2 position, startingPosition, velocity, acceleration;
	
	// Terp's width and height
	private int width, height;
	
	// terp's rotation 
	private float rotation;
		
	// circle around the turtle used for collision detection
	private Circle collisionArea;
	
	// represents the life state of the terrapin
	private boolean isAlive;
	
	// represents the size states of the terrapin
	private SizeState currentState;
	private enum SizeState {
		NORMAL, ENLARGED
	}
			
	/**
	 * Constructor that creates a new Terrapin with the specified
	 * width and height at the specified position on the screen.
	 * Also initializes the other instance variables of the class.
	 * @param x Float containing the x-coordinate of the terp.
	 * @param y Float containing the y-coordinate of the terp.
	 * @param width Int specifying the width of the terp.
	 * @param height Int specifying the height of the terp.
	 * @return none
	 */
	public Terrapin(float x, float y, int width, int height) {
		this.width = width;
		this.height = height;
		startingPosition = new Vector2(x, y);
		position = new Vector2(startingPosition);
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(0, 460);
		rotation = 0;
		collisionArea = new Circle();
		isAlive = true;
		currentState = SizeState.NORMAL;
	}
	
	/**
	 * Updates the terrapin's velocity by adding the
	 * acceleration scaled by the amount of seconds since the last
	 * time this method was called. Then, adds this new velocity to
	 * the position to move the terrapin upwards. Also, this method
	 * sets a velocity cap at 200.
	 * @param delta Float containing the amount of seconds since the last
	 * time the method was called.
	 * @return void
	 */
	public void update(float delta) {
		velocity.add(acceleration.cpy().scl(delta));
		
		// this sets a terminal velocity
		if (velocity.y > 200)
			velocity.y = 200;
		
		// sets a boundary on the ceiling
		if (position.y < -8) {
			position.y = -8;
			velocity.y = 0;
		}
		
		// sets collision area with regards to size state
		switch (currentState) {
		case NORMAL:
			updateNormal(delta);
			break;
		case ENLARGED:
			updateEnlarged(delta);
			break;
		default:
			break;
		}
		
		// rotate counter-clockwise
		if (velocity.y < 0) {
			rotation -= 600 * delta;
			
			if (rotation < -20)
				rotation = -20;
		}
		
		// rotate clockwise
		if (velocity.y > 110 || !isAlive()) {
			rotation += 480 * delta;
			
			if (rotation > 90)
				rotation = 90;
		}
				
		position.add(velocity.cpy().scl(delta));
	}
	
	/**
	 * Sets the collision area for the normal sized terrapin.
	 * @param delta Float containing the seconds since the last
	 * time this method was called.
	 */
	private void updateNormal(float delta) {
		collisionArea.set(position.x + 7, position.y + 6, 5.0f);
	}
	
	/**
	 * Sets the collision area for the enlarged sized terrapin.
	 * @param delta Float containing the seconds since the last
	 * time this method was called.
	 */
	private void updateEnlarged(float delta) {
		collisionArea.set(position.x + 10, position.y + 6, 7.5f);
	}
	
	/**
	 * Resets all of the attributes of the terrapin
	 * back to their original values when the object was created.
	 * @return void
	 */
	public void restart() {
		position.x = startingPosition.x;
		position.y = startingPosition.y;
		velocity.x = 0;
		velocity.y = 0;
		acceleration.x = 0;
		acceleration.y = 460;
		rotation = 0;
		isAlive = true;
		
		// resets the size and state if necessary
		if (currentState == SizeState.ENLARGED)
			normalize();
	}
	
	/**
	 * The actions that are taken when a user presses on
	 * the screen. Basically, this is the jumping action for our
	 * terrapin.
	 * @return void
	 */
	public void onClick() {
		if (isAlive()) {
			velocity.y = -140;
			AssetLoader.flap.play();
		}
	}
	
	/**
	 * Returns the x-coordinate of the terrapin.
	 * @return Containing x-coordinate of terp.
	 */
	public float getX() {
		return position.x;
	}

	/**
	 * Returns the y-coordinate of the terrapin.
	 * @return Containing y-coordinate of terp.
	 */
	public float getY() {
		return position.y;
	}

	/**
	 * Returns the width of the terrapin.
	 * @return Float displaying the width of the terp.
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Returns the height of the terrapin.
	 * @return Float displaying the height of the terp.
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Returns the rotation of the terrapin.
	 * @return Float containing the rotation of the terp.
	 */
	public float getRotation() {
		return rotation;
	}
	
	/**
	 * Returns the collision circle of the terrapin.
	 * @return Circle representing the collision area of the terrapin.
	 */
	public Circle getCollisionArea() {
		return collisionArea;
	}
	
	/**
	 * Determines if the terp is alive or not.
	 * @return Boolean, returns true if alive and false if not.
	 */
	public boolean isAlive() {
		return isAlive;
	}
	
	/**
	 * Returns true if terp is enlarged and false otherwise.
	 * @return boolean
	 */
	public boolean isEnlarged() {
		return currentState == SizeState.ENLARGED;
	}
	
	/**
	 * Sets the terp to being dead and stops the terp's speed.
	 * @return void
	 */
	public void kill() {
		isAlive = false;
		velocity.y = 0;
	}
	
	/**
	 * Stops the terp from accelerating downwards and
	 * repositions the terp (if dead) at top of ground.
	 * @return void
	 */
	public void decelerate() {
		// repositions terp to the top of the ground
		if (velocity.y == 0)
			position.y -= 5;
		
		acceleration.y = 0;
	}
	
	/**
	 * Doubles the size of the terrapin and its size state is
	 * changed in order to call the correct update method.
	 * This method is used when the player hits the
	 * a collectable.
	 * @return void
	 */
	public void enlarge() {
		if (!isEnlarged()) {
			width = (int) (width * 1.5);
			height = (int) (height * 1.5);
			currentState = SizeState.ENLARGED;
		}
	}
	
	/**
	 * Changes the size and the size state back to the original.
	 * @return void
	 */
	public void normalize() {
		width = (int) (width / 1.5);
		height = (int) (height / 1.5);
		currentState = SizeState.NORMAL;
	}
	
}
