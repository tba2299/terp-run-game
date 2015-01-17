package com.taskew.gameobjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Tyler Askew
 * @version 1.0
 * Represents a pencil in the game world. Used to create pencil
 * objects with random height attributes.
 */
public class Pencil extends Scrollable {

	// rectangles used to detect collisions
	private Rectangle verticalCollisionTop, verticalCollisionBottom;
	private Rectangle horizontalCollisionTop, horizontalCollisionMiddle,
						horizontalCollisionBottom;
	
	// circle used to detect collisions
	private Circle spinningCollisionBottom, spinningCollisionTop;

	// used to determine if the player has surpassed a pencil
	private boolean hasScored;

	// states that the pencils are in
	private PencilState currentState;
	private enum PencilState {
		VERTICAL, HORIZONTAL, SPINNING
	}

	/**
	 * Constructor used to create a new pencil object and initializes
	 * the random object being used to calculate the random gap heights.
	 * @param x Float containing the x-coordinate of the pencil.
	 * @param y Float containing the y-coordinate of the pencil.
	 * @param width Int representing the width of the pencil.
	 * @param height Int representing the height of the pencil.
	 * @param scrollSpeed Float containing the speed of the pencil.
	 * @return none
	 */
	public Pencil(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		verticalCollisionTop = new Rectangle();
		verticalCollisionBottom = new Rectangle();
		horizontalCollisionTop = new Rectangle();
		horizontalCollisionMiddle = new Rectangle();
		horizontalCollisionBottom = new Rectangle();
		spinningCollisionTop = new Circle();
		spinningCollisionBottom = new Circle();
		hasScored = false;
		currentState = PencilState.VERTICAL;
	}

	/**
	 * Calls the update methods corresponding to the pencil state.
	 * {@inheritDoc}
	 */
	@Override
	public void update(float delta) {
		super.update(delta);

		switch (currentState) {
		case VERTICAL:
			updateVertical(delta);
			break;
		case HORIZONTAL:
			updateHorizontal(delta);
			break;
		case SPINNING:
			updateSpinning(delta);
			break;
		default:
			break;
		}
	}

	/**
	 * Handles the collision areas of the vertical pencils.
	 * @param delta Float representing the amount of time since this 
	 * method was called last.
	 */
	public void updateVertical(float delta) {
		// checks to see if the pencil has scrolled off screen
		if (position.x + width < 0)
			finishedScroll = true;
		
		// setting the positions and dimensions of the collision areas
		verticalCollisionTop.set(position.x + 6, position.y - (shiftAmount * 2),
				width / 2, height + (shiftAmount * 2) + 10);
		verticalCollisionBottom.set(position.x + 6, position.y + height + 47,
				width / 2, height + position.y + 129);
	}
	
	/**
	 * Handles the collision areas of the horizontal pencils.
	 * @param delta Float representing the amount of time since this 
	 * method was called last.
	 */
	public void updateHorizontal(float delta) {
		// checks to see if the pencil has scrolled off screen
		if (horizontalCollisionMiddle.x + horizontalCollisionMiddle.width < 0)
			finishedScroll = true;
		
		horizontalCollisionMiddle.set(getLeftX() - (getHeight() / 2.0f) + 15, 
		getHeight() + (getHeight() / 2.0f) + (getWidth() / 3.0f) + 5 + getY(), 
		getHeight() - 8, getWidth() / 2.0f);
		horizontalCollisionTop.set(getLeftX() - (getHeight() / 2.0f) + 15, 
		getHeight() + (getHeight() / 2.0f) + (getWidth() / 3.0f) - 60 + getY(), 
		getHeight() - 8, getWidth() / 2.0f);
		horizontalCollisionBottom.set(getLeftX() - (getHeight() / 2.0f) + 15, 
				getHeight() + (getHeight() / 2.0f) + (getWidth() / 3.0f) + 70 +
				getY(), getHeight() - 8, getWidth() / 2.0f);
	}
	
	/**
	 * Handles the collision areas of the spinning pencils.
	 * @param delta Float representing the amount of time since this 
	 * method was called last.
	 */
	public void updateSpinning(float delta) {
		// checks to see if the pencil has scrolled off screen
		if ((spinningCollisionBottom.x - spinningCollisionBottom.radius) + 
			spinningCollisionBottom.radius * 2 < 0)
			finishedScroll = true;
		
		spinningCollisionBottom.set(getLeftX() + ((getWidth() * 3) / 2.0f), 
				getHeight() + (getHeight() / 2.0f), getHeight() / 2.0f);
		spinningCollisionTop.set(getLeftX() + ((getWidth() * 3) / 2.0f),
				(spinningCollisionBottom.y - spinningCollisionBottom.radius -
				150) + (getHeight() / 2.0f), getHeight() / 2.0f);
	}
	
	/**
	 * Sets the current state of the pencil to vertical.
	 * @return void
	 */
	public void setVertical() {
		currentState = PencilState.VERTICAL;
	}
	
	/**
	 * Sets the current state of the pencil to horizontal.
	 * @return void
	 */
	public void setHorizontal() {
		currentState = PencilState.HORIZONTAL;
	}
	
	/**
	 * Sets the current state of the pencil to spinning.
	 * @return void
	 */
	public void spin() {
		currentState = PencilState.SPINNING;
	}

	/**
	 * Resets the pencil object once it has finished scrolling across
	 * the screen and randomizes the height.
	 * @param newX Float containing the new x-coordinate.
	 * @return void
	 */
	@Override
	public void reset(float newX) {
		super.reset(newX);
		hasScored = false;
		
		// randomizes the height according to current state
		if (isVertical())
			height = random.nextInt(90) + 15;
		else if (isHorizontal())
			height = random.nextInt(70) + 35;
		else if (isSpinning())
			height = random.nextInt(20) + 85;
	}

	/**
	 * Resets the pencils back to their original placements,
	 * scrolling speeds, states, and collision areas.
	 * @param f Float containing original placement of the pencil.
	 * @param scrollSpeed Float containing the original speed of the pencil.
	 * @return void
	 */
	public void restart(float f, float scrollSpeed) {
		super.restart(scrollSpeed);
		setVertical();
		
		// resetting the collisions
		verticalCollisionTop = new Rectangle();
		verticalCollisionBottom = new Rectangle();
		horizontalCollisionTop = new Rectangle();
		horizontalCollisionMiddle = new Rectangle();
		horizontalCollisionBottom = new Rectangle();
		spinningCollisionTop = new Circle();
		spinningCollisionBottom = new Circle();
		
		reset(f);
	}

	/**
	 * Determines if the terp has collides with a pencil
	 * object.
	 * @param terp Terrapin object representing the terp character.
	 * @return Boolean representing whether or not the terp has
	 * collided with a pencil.
	 */
	public boolean collides(Terrapin terp) {		
		// checks if there could be a possible collision
		return (Intersector.overlaps(terp.getCollisionArea(), 
				verticalCollisionTop) ||
				Intersector.overlaps(terp.getCollisionArea(), 
				verticalCollisionBottom) ||
				Intersector.overlaps(terp.getCollisionArea(),
				spinningCollisionBottom) ||
				Intersector.overlaps(terp.getCollisionArea(),
				horizontalCollisionMiddle) ||
				Intersector.overlaps(terp.getCollisionArea(),
				spinningCollisionTop) ||
				Intersector.overlaps(terp.getCollisionArea(),
				horizontalCollisionTop) ||
				Intersector.overlaps(terp.getCollisionArea(),
				horizontalCollisionBottom));
	}

	/**
	 * Returns the rectangle that represents the collision area for
	 * the vertical pencil at the top of the screen.
	 * @return Rectangle object representing the collision area of the top
	 * pencil body.
	 */
	public Rectangle getVerticalCollisionTop() {
		return verticalCollisionTop;
	}

	/**
	 * Returns the rectangle that represents the collision area for
	 * the vertical pencil at the bottom of the screen.
	 * @return Rectangle object representing the collision area of the bottom
	 * pencil body.
	 */
	public Rectangle getVerticalCollisionBottom() {
		return verticalCollisionBottom;
	}

	/**
	 * Returns true if the pencil has been scored on and false if not.
	 * @return boolean
	 */
	public boolean hasScored() {
		return hasScored;
	}

	/**
	 * Used to set the score state of the pencil to whatever
	 * the caller chooses.
	 * @param b Boolean representing the score state of the pencil.
	 * @return void
	 */
	public void setScored(boolean b) {
		hasScored = b;
	}
	
	/**
	 * Returns true if the pencil is vertical and false if not.
	 * @return boolean
	 */
	public boolean isVertical() {
		return currentState == PencilState.VERTICAL;
	}
	
	/**
	 * Returns true if the pencil is horizontal and false if not.
	 * @return boolean
	 */
	public boolean isHorizontal() {
		return currentState == PencilState.HORIZONTAL;
	}
	
	/**
	 * Returns true if the pencil is spinning and false if not.
	 * @return boolean
	 */
	public boolean isSpinning() {
		return currentState == PencilState.SPINNING;
	}
	
	/**
	 * Returns the collision area for the top spinning pencils.
	 * @return Circle object representing the collision area
	 * for spinning pencil.
	 */
	public Circle getSpinningCollisionTop() {
		return spinningCollisionTop;
	}
	
	/**
	 * Returns the collision area used for the bottom spinning pencils.
	 * @return Circle object representing collision area for spinning pencil.
	 */
	public Circle getSpinningCollisionBottom() {
		return spinningCollisionBottom;
	}
	
	/**
	 * Returns the top collision area used for horizontal pencils.
	 * @return Rectangle object representing collision area for horizontal
	 * pencils.
	 */
	public Rectangle getHorizontalCollisionTop() {
		return horizontalCollisionTop;
	}
	
	/**
	 * Returns the middle collision area used for horizontal pencils.
	 * @return Rectangle object representing collision area for horizontal
	 * pencils.
	 */
	public Rectangle getHorizontalCollisionMiddle() {
		return horizontalCollisionMiddle;
	}
	
	/**
	 * Returns the bottom collision area used for horizontal pencils.
	 * @return Rectangle object representing collision area for horizontal
	 * pencils.
	 */
	public Rectangle getHorizontalCollisionBottom() {
		return horizontalCollisionBottom;
	}

}
