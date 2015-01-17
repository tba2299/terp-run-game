package com.taskew.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class Collectable extends Scrollable {
	
	// used to detect collisions
	private Circle collisionArea;
	
	// used to set collision area radius
	private int initialHeight;
	
	// used to determine if player has collected the item
	private boolean isCollected;
	
	// types of collectables
	private CollectableType type;
	private enum CollectableType {
		M, COFFEE, BEER, PIZZA
	}

	/**
	 * Constructor that initializes all attributes of
	 * a collectable and its collision area.
	 * @param x Float containing x-coordinate.
	 * @param y Float containing y-coordinate.
	 * @param width Int containing the width.
	 * @param height Int containing the height.
	 * @param scrollSpeed Float containing the scrolling speed.
	 */
	public Collectable(float x, float y, int width, int height,
			float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		collisionArea = new Circle();
		initialHeight = height;
		isCollected = false;
	}
	
	/**
	 * Updates the collision area for the collectable and calculates
	 * if it has finished scrolling across the screen.
	 */
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if ((collisionArea.x - collisionArea.radius) + 
				(collisionArea.radius * 2) < 0)
			finishedScroll = true;
		
		collisionArea.set(getLeftX() + (getWidth() / 2.0f),
				getHeight() + (initialHeight / 2.0f), initialHeight / 2.0f);
	}
	
	/**
	 * Resets the x-coordinate of the collectable and randomizes its
	 * height which is actually used for its y-coordinate.
	 */
	@Override
	public void reset(float newX) {
		super.reset(newX);
		isCollected = false;
		height = random.nextInt(90) + 15;
	}
	
	/**
	 * Restarts the collectable by resetting its position,
	 * scroll speed, and its collision area.
	 * @param f Float containing the new x-coordinate.
	 * @param scrollSpeed Float containing the scroll speed.
	 */
	public void restart(float f, float scrollSpeed) {
		super.restart(scrollSpeed);
		
		// resets the collision area
		collisionArea = new Circle();
		
		reset(f);
	}
	
	/**
	 * Determines if the terrapin has collected a collectable.
	 * @param terp Terrapin object representing the terp character.
	 * @return boolean
	 */
	public boolean collected(Terrapin terp) {
		return Intersector.overlaps(terp.getCollisionArea(), 
			   collisionArea);
	}
	
	/**
	 * Checks to see if the collectable is being repositioned
	 * to where it may collide with a pencil. This is used
	 * to determine if the collectable should be drawn or not.
	 * @param p Pencil object representing the pencil.
	 * @return boolean
	 */
	public boolean collectableCollidesPencil(Pencil p) {
		return (Intersector.overlaps(getCollisionArea(), 
				p.getVerticalCollisionTop()) ||
				Intersector.overlaps(getCollisionArea(), 
				p.getVerticalCollisionBottom()) ||
				Intersector.overlaps(getCollisionArea(),
				p.getHorizontalCollisionTop()) ||
				Intersector.overlaps(getCollisionArea(), 
				p.getHorizontalCollisionMiddle()) ||
				Intersector.overlaps(getCollisionArea(),
				p.getHorizontalCollisionBottom()) ||
				Intersector.overlaps(getCollisionArea(),
				p.getSpinningCollisionTop()) ||
				Intersector.overlaps(getCollisionArea(),
				p.getSpinningCollisionBottom()));
	}
	
	/**
	 * Returns true if the collectable has been collected and
	 * false otherwise.
	 * @return boolean
	 */
	public boolean isCollected() {
		return isCollected;
	}

	/**
	 * Sets the isCollected boolean to true or false depending
	 * on if the item has been collected or not.
	 * @param b Boolean representing if item has been collected or not.
	 */
	public void setCollected(boolean b) {
		isCollected = b;
	}
	
	/**
	 * Sets the collectable type to the 'M' collectable.
	 */
	public void setM() {
		type = CollectableType.M;
	}
	
	/**
	 * Sets the collectable type to the beer collectable.
	 */
	public void setBeer() {
		type = CollectableType.BEER;
	}
	
	/**
	 * Sets the collectable type to the coffee collectable.
	 */
	public void setCoffee() {
		type = CollectableType.COFFEE;
	}
	
	/**
	 * Sets the collectable type to the pizza collectable.
	 */
	public void setPizza() {
		type = CollectableType.PIZZA;
	}
	
	/**
	 * Determines if the collectable is the 'M' collectable.
	 * @return boolean
	 */
	public boolean isM() {
		return type == CollectableType.M;
	}
	
	/**
	 * Determines if the collectable is the beer collectable.
	 * @return boolean
	 */
	public boolean isBeer() {
		return type == CollectableType.BEER;
	}
	
	/**
	 * Determines if the collectable is the coffee collectable.
	 * @return boolean
	 */
	public boolean isCoffee() {
		return type == CollectableType.COFFEE;
	}
	
	/**
	 * Determines if the collectable is the pizza collectable.
	 * @return boolean
	 */
	public boolean isPizza() {
		return type == CollectableType.PIZZA;
	}
	
	/**
	 * Returns the collision area for the collectable.
	 * @return Circle object representing the collision area.
	 */
	public Circle getCollisionArea() {
		return collisionArea;
	}
	
}
