package com.taskew.gameobjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.taskew.gameworld.GameWorld;
import com.taskew.helpers.AssetLoader;

/**
 * @author Tyler Askew
 * @version 1.0
 * Creates all of the scrolling objects in the game. Each is initialized
 * and placed at certain coordinates on the screen. Also controls
 * the different speeds and gap lengths of the pencils.
 */
public class ScrollHandler {

	// represents the game world
	private GameWorld gameWorld;
	
	// creates the 3 pencil objects
	private Pencil pencil1, pencil2, pencil3, pencil4;
	
	// creates the different collectables
	private Collectable collectable;
	
	// represents the gaps between each pencil
	public static final int NORM_PENCIL_GAP = 45;
	
	// represents the scroll speeds of the objects
	public static final int NORM_SCROLL_SPEED = -59;
	private boolean speedIncreased, isShifting;
		
	// random number generator to change pencil states
	private Random random;
	private int randomNum;

	
	/**
	 * Initializes the pencils within the game and receives the game
	 * world where these pencils reside.
	 * @param gameWorld GameWorld object representing the game world.
	 * @return none
	 */
	public ScrollHandler(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		random = new Random();
		randomNum = 0;
		speedIncreased = false;
		isShifting = false;
		
		pencil1 = new Pencil(210, 0, 22, 60, NORM_SCROLL_SPEED);
		pencil2 = new Pencil(pencil1.getRightX() + NORM_PENCIL_GAP, 
				0, 22, 70, NORM_SCROLL_SPEED);
		pencil3 = new Pencil(pencil2.getRightX() + NORM_PENCIL_GAP, 
				0, 22, 80, NORM_SCROLL_SPEED);
		pencil4 = new Pencil(pencil3.getRightX() + NORM_PENCIL_GAP,
				0, 22, 60, NORM_SCROLL_SPEED);
		collectable = new Collectable(pencil1.getRightX() + 
				(NORM_PENCIL_GAP / 2), 0, 10, 10, NORM_SCROLL_SPEED);
	}
	
	/**
	 * Updates all of the pencil objects to keep them moving
	 * across the screen. Also checks the pencils have gone off screen and
	 * if they have they are reset to be placed back on screen.
	 * @param delta Float containing number of seconds since the last time
	 * this method was called.
	 * @return void
	 */
	public void update(float delta) {		
		pencil1.update(delta);
		pencil2.update(delta);
		pencil3.update(delta);
		pencil4.update(delta);
		collectable.update(delta);
		
		// updates these objects' states and positions
		updatePencils();
       	updateCollectables();
       	
       	// modifies the game based on collectables and changes in the game
       	modifyGame();
	}
	
	/**
	 * Calls all of the pencil's and collectable's restart methods in 
	 * order to fully reset them to their original form.
	 * @return void
	 */
	public void restart() {
		speedIncreased = false;
		isShifting = false;
		pencil1.restart(210, NORM_SCROLL_SPEED);
		pencil2.restart(pencil1.getRightX() + NORM_PENCIL_GAP, 
				NORM_SCROLL_SPEED);
		pencil3.restart(pencil2.getRightX() + NORM_PENCIL_GAP,
				NORM_SCROLL_SPEED);
		pencil4.restart(pencil3.getRightX() + NORM_PENCIL_GAP, 
				NORM_SCROLL_SPEED);
		collectable.restart(pencil1.getRightX() + (NORM_PENCIL_GAP / 2.0f), 
				NORM_SCROLL_SPEED);
	}
	
	/**
	 * Calls each object's stop method in order to halt the
	 * game screen from scrolling.
	 * @return void
	 */
	public void stop() {
		pencil1.stop();
		pencil2.stop();
		pencil3.stop();
		pencil4.stop();
		collectable.stop();
	}
	
	/**
	 * Determines if the terp collides with any object or if the
	 * terp has surpassed one of the objects and adds to the player's score.
	 * @param terp Terrapin object representing the terp.
	 * @return Boolean returning true if the terp has collided with
	 * something and false otherwise.
	 */
	public boolean collides(Terrapin terp) {
		// determining if the player has scored a point
		if (!pencil1.hasScored() &&
			pencil1.getLeftX() + (pencil1.getWidth() / 2) <
			terp.getX() + terp.getWidth()) {
			gameWorld.addScore(1);
			pencil1.setScored(true);
			AssetLoader.score.play();
		} else if (!pencil2.hasScored() &&
					pencil2.getLeftX() + (pencil2.getWidth() / 2) <
					terp.getX() + terp.getWidth()) {
			gameWorld.addScore(1);
			pencil2.setScored(true);
			AssetLoader.score.play();
		} else if (!pencil3.hasScored() &&
					pencil3.getLeftX() + (pencil3.getWidth() / 2) <
					terp.getX() + terp.getWidth()) {
			gameWorld.addScore(1);
			pencil3.setScored(true);
			AssetLoader.score.play();
		} else if (!pencil4.hasScored() &&
					pencil4.getLeftX() + (pencil4.getWidth() / 2) <
					terp.getX() + terp.getWidth()) {
			gameWorld.addScore(1);
			pencil4.setScored(true);
			AssetLoader.score.play();
		}
		
		
		if (collectable.collected(terp) && !collectable.isCollected() &&
			gameWorld.getScore() > 4 && 
			!(collectable.collectableCollidesPencil(pencil1) ||
					collectable.collectableCollidesPencil(pencil2) ||
					collectable.collectableCollidesPencil(pencil3) ||
					collectable.collectableCollidesPencil(pencil4))) {
			// determines which action to do corresponding to the collectable
			if (collectable.isM())
				gameWorld.addScore(5);
			else if (collectable.isBeer())
				isShifting = true;
			else if (collectable.isCoffee())
				speedIncreased = true;
			else if (collectable.isPizza()) {
				gameWorld.getTerp().enlarge();
				gameWorld.setEnlargedScoreCount(gameWorld.getScore());
			}
				
			// plays collect sound and marks collectable as being collected
			collectable.setCollected(true);
			AssetLoader.collect.play();
		}
		
		return (pencil1.collides(terp) || pencil2.collides(terp) ||
				pencil3.collides(terp) || pencil4.collides(terp));
	}
	
	/**
	 * Updates all of the pencils' states and positions if it
	 * is done scrolling across the screen.
	 */
	private void updatePencils() {
		if (pencil1.isFinishedScrolling()) {			
			randomNum = random.nextInt(100) + 1;

			// randomly changes pencil states
			if (gameWorld.getScore() > 4) {
				if (randomNum >= 1 && randomNum <= 20)
					pencil1.setVertical();
				else if (randomNum >= 30 && randomNum <= 50)
					pencil1.setHorizontal();
				else if (randomNum >= 60 && randomNum <= 80)
					pencil1.spin();
			}
			                    	
            // sets new position depending on the different pencil states
            if (pencil4.isVertical())
            	pencil1.reset(pencil4.getRightX() + NORM_PENCIL_GAP);
            else if (pencil4.isHorizontal())
            	pencil1.reset(pencil4.getHorizontalCollisionMiddle().x +
            			pencil4.getHorizontalCollisionMiddle().width +
            			NORM_PENCIL_GAP);
            else if (pencil4.isSpinning())
            	pencil1.reset((pencil4.getSpinningCollisionBottom().x -
            			pencil4.getSpinningCollisionBottom().radius) + 
            			(pencil4.getSpinningCollisionBottom().radius * 2) +
            			NORM_PENCIL_GAP);
        } else if (pencil2.isFinishedScrolling()) {  
        	randomNum = random.nextInt(100) + 1;

        	// randomly changes pencil states
			if (gameWorld.getScore() > 4) {
				if (randomNum >= 1 && randomNum <= 20)
					pencil2.setVertical();
				else if (randomNum >= 30 && randomNum <= 50)
					pencil2.setHorizontal();
				else if (randomNum >= 60 && randomNum <= 80)
						pencil2.spin();
			}
                    	
            // sets new position depending on the different pencil states
        	if (pencil1.isVertical())
        		pencil2.reset(pencil1.getRightX() + NORM_PENCIL_GAP);
        	else if (pencil1.isHorizontal())
        		pencil2.reset(pencil1.getHorizontalCollisionMiddle().x +
        				pencil1.getHorizontalCollisionMiddle().width +
        				NORM_PENCIL_GAP);
        	else if (pencil1.isSpinning())
        		pencil2.reset((pencil1.getSpinningCollisionBottom().x -
        				pencil1.getSpinningCollisionBottom().radius) + 
        				(pencil1.getSpinningCollisionBottom().radius * 2) +
        				NORM_PENCIL_GAP);
        } else if (pencil3.isFinishedScrolling()) { 
        	randomNum = random.nextInt(100) + 1;

        	// randomly changes pencil states
			if (gameWorld.getScore() > 4) {
				if (randomNum >= 1 && randomNum <= 20)
					pencil3.setVertical();
				else if (randomNum >= 30 && randomNum <= 50)
					pencil3.setHorizontal();
				else if (randomNum >= 60 && randomNum <= 80)
						pencil3.spin();
			}

            // sets new position depending on the different pencil states
        	if (pencil2.isVertical())
        		pencil3.reset(pencil2.getRightX() + NORM_PENCIL_GAP);
        	else if (pencil2.isHorizontal())
        		pencil3.reset(pencil2.getHorizontalCollisionMiddle().x +
        				pencil2.getHorizontalCollisionMiddle().width +
        				NORM_PENCIL_GAP);
        	else if (pencil2.isSpinning())
        		pencil3.reset((pencil2.getSpinningCollisionBottom().x -
        				pencil2.getSpinningCollisionBottom().radius) + 
        				(pencil2.getSpinningCollisionBottom().radius * 2) +
        				NORM_PENCIL_GAP);
        } else if (pencil4.isFinishedScrolling()) {
        	randomNum = random.nextInt(100) + 1;

        	// randomly changes pencil states
			if (gameWorld.getScore() > 4) {
				if (randomNum >= 1 && randomNum <= 20)
					pencil4.setVertical();
				else if (randomNum >= 30 && randomNum <= 50)
					pencil4.setHorizontal();
				else if (randomNum >= 60 && randomNum <= 80)
						pencil4.spin();
			}
        	
            // sets new position depending on the different pencil states
        	if (pencil3.isVertical())
        		pencil4.reset(pencil3.getRightX() + NORM_PENCIL_GAP);
        	else if (pencil3.isHorizontal())
        		pencil4.reset(pencil3.getHorizontalCollisionMiddle().x +
        				pencil3.getHorizontalCollisionMiddle().width +
        				NORM_PENCIL_GAP);
        	else if (pencil3.isSpinning())
        		pencil4.reset((pencil3.getSpinningCollisionBottom().x -
        				pencil3.getSpinningCollisionBottom().radius) + 
        				(pencil3.getSpinningCollisionBottom().radius * 2) +
        				NORM_PENCIL_GAP);
        }
	}
	
	/**
	 * Helper method used to randomly assign a new collectable
	 * to the screen and randomize its position.
	 */
	private void updateCollectables() {
        if (collectable.isFinishedScrolling() &&
        		gameWorld.getScore() > 4) {
        	randomNum = random.nextInt(4) + 1;

        	// randomly changes the type of collectable
        	if (randomNum == 1)
        		collectable.setM();
        	else if (randomNum == 2)
        		collectable.setBeer();
        	else if (randomNum == 3)
        		collectable.setPizza();
        	else if (randomNum == 4)
        		collectable.setCoffee();

        	// determines where to randomly place the collectable
        	if (randomNum == 1 && pencil1.getLeftX() >= 210)
        		collectable.reset(pencil1.getRightX() + 
        				(NORM_PENCIL_GAP / 2.0f));
        	else if (randomNum == 2 && pencil2.getLeftX() >= 210)
        		collectable.reset(pencil2.getRightX() +
        				(NORM_PENCIL_GAP / 2.0f));
        	else if (randomNum == 3 && pencil3.getLeftX() >= 210)
        		collectable.reset(pencil3.getRightX() +
        				(NORM_PENCIL_GAP / 2.0f));
        	else if (randomNum == 4 && pencil4.getLeftX() >= 210)
        		collectable.reset(pencil4.getRightX() +
        				(NORM_PENCIL_GAP / 2.0f));
        }
	}
	
	/**
	 * Modifies the game if a player has hit the beer or coffee
	 * collectable. Also, decreases the amount of speed and shifting
	 * gradually, if these have been altered by the player.
	 */
	private void modifyGame() {
		/* 
		 * If the player hits a coffee collectable, 
		 * the speed of the objects increase.
		 */
        if (speedIncreased) {
        	pencil1.modifyScrollSpeed(10);
        	pencil2.modifyScrollSpeed(10);
        	pencil3.modifyScrollSpeed(10);
        	pencil4.modifyScrollSpeed(10);
        	collectable.modifyScrollSpeed(10);
        	speedIncreased = false;
        }
                
        /* 
         * If the player hits a beer collectable,
         * the objects in the game start shifting vertically.
         */
        if (isShifting) {
        	pencil1.shift(5);
        	pencil2.shift(10);
        	pencil3.shift(5);
        	pencil4.shift(10);
        	collectable.shift(7);
        	isShifting = false;
        }
        
        /*
         * Gradually decreases the scrolling speed if its been altered.
         * Only one of the altered objects needs to be compared to the normal
         * scroll speed because they all get changed equally at the same time.
         */
        if (pencil1.getScrollingSpeed() < NORM_SCROLL_SPEED) {
        	pencil1.modifyScrollSpeed(-.01f);
        	pencil2.modifyScrollSpeed(-.01f);
        	pencil3.modifyScrollSpeed(-.01f);
        	pencil4.modifyScrollSpeed(-.01f);
        	collectable.modifyScrollSpeed(-.01f);
        }
        
        /*
         * Gradually decreases the shifting amount if its been altered.
         */
        if (pencil1.getShiftAmount() > 0)
        	pencil1.shift(-.01f);
        
        if (pencil2.getShiftAmount() > 0)
        	pencil2.shift(-.01f);

        if (pencil3.getShiftAmount() > 0)
        	pencil3.shift(-.01f);

        if (pencil4.getShiftAmount() > 0)
        	pencil4.shift(-.01f);

        if (collectable.getShiftAmount() > 0)
        	collectable.shift(-.01f);
        
        /*
         * Changes the terp's size back to normal once the player
         * has scored 5 additional points in the enlarged state.
         */
        if (gameWorld.getTerp().isEnlarged() && 
        		(gameWorld.getScore() - gameWorld.getEnlargedScoreCount() == 5))
        	gameWorld.getTerp().normalize();
	}
	
	/**
	 * Returns the first pencil.
	 * @return Pencil object representing the first pencil.
	 */
	public Pencil getPencil1() {
		return pencil1;
	}
	
	/**
	 * Returns the second pencil.
	 * @return Pencil object representing the second pencil.
	 */
	public Pencil getPencil2() {
		return pencil2;
	}
	
	/**
	 * Returns the third pencil.
	 * @return Pencil object representing the third pencil.
	 */
	public Pencil getPencil3() {
		return pencil3;
	}
	
	/**
	 * Returns the fourth pencil.
	 * @return Pencil object representing the fourth pencil.
	 */
	public Pencil getPencil4() {
		return pencil4;
	}
	
	/**
	 * Returns the current collectable in the game.
	 * @return Collectable object represents one of the collectable types.
	 */
	public Collectable getCollectable() {
		return collectable;
	}
	
}
