package com.taskew.gameworld;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.taskew.gameobjects.Collectable;
import com.taskew.gameobjects.Pencil;
import com.taskew.gameobjects.Terrapin;
import com.taskew.helpers.AssetLoader;
import com.taskew.helpers.InputHandler;
import com.taskew.screens.MainMenuScreen;

/**
 * @author Tyler Askew
 * @version 1.0
 * Draws all of the objects and other necessary components to visually create
 * the game in regards to the specified GameWorld object being passed to
 * the constructor. Every component that is drawn is actually contained
 * within the GameWorld class.
 */
public class GameRenderer {	

	// Contains the most current version of the game world
	private GameWorld world;

	// 2-dimensional camera
	private OrthographicCamera cam;
	
	// components for the game over menu
	private Table table;
	private ImageButton replayButton, menuButton;
	private Label gameoverLabel, scoreLabel;
	private Label.LabelStyle gameoverStyle, scoreStyle;

	// used to draw the textures and shapes
	private SpriteBatch batcher;
	private ShapeRenderer shapeRenderer;

	// midpoint of the game screen
	private float midScreen;
	
	// game objects
	private Terrapin terp;
	private Pencil pencil1, pencil2, pencil3, pencil4;
	private Collectable collectable;

	// game assets
	private TextureRegion terpImage;
	private TextureRegion pencilTipUp, pencilTipDown, pencilBody, fullPencil;
	private TextureRegion mSymbol, coffee, pizza, beer;
	
	// represents the entire game
	private Game game;

	/**
	 * Constructor that sets the world variable to the one
	 * being passed in so that this class is rendering the most
	 * current version of the world. As well as initializes the other
	 * instance variables.
	 * @param world GameWorld object that contains updates to the game world.
	 * @return none
	 */
	public GameRenderer(GameWorld world, Game game, 
			int gameHeight, int midScreen) {
		this.world = world;
		this.midScreen = midScreen;

		// setting up the camera
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, gameHeight);

		// initializing batcher
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);

		// initializing the shape renderer
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		// initializes the game
		this.game = game;

		// initialize all of the game components 
		initGameObjects();
		initAssets();
		initGameOverMenu();
	}

	/**
	 * Draws the visual display of the game onto the screen
	 * including backgrounds, grass, pencils, terrapins, and other components.
	 * @param runtime Float containing how long the game has been running.
	 * @return void
	 */
	public void render(float runtime) {		
		// Fill the entire screen with black to prevent potential flickering
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// drawing the background color
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(127 / 255.0f, 205 / 255.0f, 255 / 255.0f, 1);
		shapeRenderer.rect(0, 0, 136, midScreen * 2);
		shapeRenderer.end();

		// starts the SpriteBatch
		batcher.begin();
		
		// draws the background
		drawBackground();
		
		// enabling transparency
		batcher.enableBlending();
		
		// draws all of the pencils in the game
		drawAllPencils();
		
		// draws all collectables in the game
		drawCollectables();
		
		// draws the terp on the screen
		batcher.draw(terpImage, terp.getX(), terp.getY(),
				terp.getWidth() / 2.0f, terp.getHeight() / 2.0f,
				terp.getWidth(), terp.getHeight(), 
				1, 1, terp.getRotation());
		
		// draws the call-to-action text before the game starts
		if (world.isReady()) {
			AssetLoader.gameplayShadow.draw(batcher, "Tap Screen", (136 / 2)
					- (50), 76);
			AssetLoader.gameplayFont.draw(batcher, "Tap Screen", (136 / 2)
					- (50 - 1), 75);
		} else {

			if (world.isHighScore() || world.isGameOver())
				drawGameOverMenu();
			
			// Draw shadow and then the text to display score
			String score = String.valueOf(world.getScore());
			AssetLoader.gameplayShadow.draw(batcher, score, 
					(136 / 2) - (3 * score.length()), 12);
			AssetLoader.gameplayFont.draw(batcher, score, 
					(136 / 2) - (3 * score.length() - 1), 11);
		}

		// ends the SpriteBatch
		batcher.end();
		
		// drawing the dirt
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(147 / 255.0f, 80 / 255.0f, 27 / 255.0f, 1);
		shapeRenderer.rect(0, midScreen + 100, 136, 52);
		shapeRenderer.end();
	}

	/**
	 * Helper method that initializes all of the
	 * objects used in the game.
	 * @return void
	 */
	private void initGameObjects() {
		terp = world.getTerp();
		pencil1 = world.getScroller().getPencil1();
		pencil2 = world.getScroller().getPencil2();
		pencil3 = world.getScroller().getPencil3();
		pencil4 = world.getScroller().getPencil4();
		collectable = world.getScroller().getCollectable();
	}

	/**
	 * Helper method that initializes all of the game's
	 * assets.
	 * @return void
	 */
	private void initAssets() {
		terpImage = AssetLoader.terp;
		pencilTipUp = AssetLoader.pencilTipUp;
		pencilTipDown = AssetLoader.pencilTipDown;
		pencilBody = AssetLoader.pencilBody;
		fullPencil = AssetLoader.fullPencil;
		mSymbol = AssetLoader.mSymbol;
		coffee = AssetLoader.coffee;
		pizza = AssetLoader.pizza;
		beer = AssetLoader.beer;
	}
	
	/**
	 * Helper method that initializes all components for the
	 * game over menu.
	 */
	private void initGameOverMenu() {
		// initializing the table
		table = new Table();
		table.setFillParent(true);
		
		// initializing the buttons for the game over screen
		replayButton = 
				new ImageButton(new SpriteDrawable(AssetLoader.replayButtonUp), 
						new SpriteDrawable(AssetLoader.replayButtonDown));
		menuButton = 
				new ImageButton(new SpriteDrawable(AssetLoader.menuButtonUp),
						new SpriteDrawable(AssetLoader.menuButtonDown));
		
		// restarts the game and reassigns input processor
		replayButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				world.restart();
			}
		});
		
		// changes screen back to main menu
		menuButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MainMenuScreen(game));
				table.clear();
			}
		});
		
		// creating the style for the labels
		gameoverStyle = new Label.LabelStyle();
		gameoverStyle.font = AssetLoader.menuFont;
		scoreStyle = new Label.LabelStyle();
		scoreStyle.font = AssetLoader.regularFont;
		
		// initializing the labels for the menu
		gameoverLabel = new Label("GAME OVER", gameoverStyle);
		scoreLabel = new Label("", scoreStyle);
				
		// add all components to the table
		table.add(gameoverLabel).padBottom(25).top().row();
		table.add(scoreLabel).padBottom(15).row();
		table.add(replayButton).padBottom(10).row();
		table.add(menuButton).row();
		
		// adds the table to the stage
		AssetLoader.gameoverStage.addActor(table);
	}

	/**
	 * Helper method that draws the tips of the pencils for the
	 * vertical and horizontal pencils.
	 * @return void
	 */
	private void drawPencilTips() {
		if (pencil1.isVertical()) {
			batcher.draw(pencilTipUp, pencil1.getLeftX() - 5,
					pencil1.getY() + pencil1.getHeight() - 1, 32, 32);
			batcher.draw(pencilTipDown, pencil1.getLeftX() - 5,
					pencil1.getY() + pencil1.getHeight() + 25, 32, 32);
		}
		
		if (pencil2.isVertical()) {
			batcher.draw(pencilTipUp, pencil2.getLeftX() - 5,
					pencil2.getY() + pencil2.getHeight() - 1, 32, 32);
			batcher.draw(pencilTipDown, pencil2.getLeftX() - 5,
					pencil2.getY() + pencil2.getHeight() + 25, 32, 32);
		}
		
		if (pencil3.isVertical()) {
			batcher.draw(pencilTipUp, pencil3.getLeftX() - 5,
					pencil3.getY() + pencil3.getHeight() - 1, 32, 32);
			batcher.draw(pencilTipDown, pencil3.getLeftX() - 5,
					pencil3.getY() + pencil3.getHeight() + 25, 32, 32);
		}
		
		if (pencil4.isVertical()) {
			batcher.draw(pencilTipUp, pencil4.getLeftX() - 5,
					pencil4.getY() + pencil4.getHeight() - 1, 32, 32);
			batcher.draw(pencilTipDown, pencil4.getLeftX() - 5,
					pencil4.getY() + pencil4.getHeight() + 25, 32, 32);
		}
	}

	/**
	 * Helper method that draws the bodies of the vertical and
	 * horizontal pencils.
	 * @return void
	 */
	private void drawPencilBodies() {
		if (pencil1.isVertical()) {
			batcher.draw(pencilBody, pencil1.getLeftX(), 
					pencil1.getY() - (pencil1.getShiftAmount() * 2), 
					pencil1.getWidth() + 1,
					pencil1.getHeight() + (pencil1.getShiftAmount() * 2));
			batcher.draw(pencilBody, pencil1.getLeftX(), 
					pencil1.getY() + pencil1.getHeight() + 57,
					pencil1.getWidth() + 1, midScreen + 66 - 
					pencil1.getHeight());
		}
		
		if (pencil2.isVertical()) {
			batcher.draw(pencilBody, pencil2.getLeftX(), 
					pencil2.getY() - (pencil2.getShiftAmount() * 2),
					pencil2.getWidth() + 1,
					pencil2.getHeight() + (pencil2.getShiftAmount() * 2));
			batcher.draw(pencilBody, pencil2.getLeftX(),
					pencil2.getY() + pencil2.getHeight() + 57,
					pencil2.getWidth() + 1, midScreen + 66 -
					(pencil2.getHeight()));
		}
		
		if (pencil3.isVertical()) {
			batcher.draw(pencilBody, pencil3.getLeftX(), 
					pencil3.getY() - (pencil3.getShiftAmount() * 2),
					pencil3.getWidth() + 1,
					pencil3.getHeight() + (pencil3.getShiftAmount() * 2));
			batcher.draw(pencilBody, pencil3.getLeftX(), 
					pencil3.getY() + pencil3.getHeight() + 57,
					pencil3.getWidth() + 1, midScreen + 66 - 
					(pencil3.getHeight()));
		}
		
		if (pencil4.isVertical()) {
			batcher.draw(pencilBody, pencil4.getLeftX(), 
					pencil4.getY() - (pencil4.getShiftAmount() * 2),
					pencil4.getWidth() + 1,
					pencil4.getHeight() + (pencil4.getShiftAmount() * 2));
			batcher.draw(pencilBody, pencil4.getLeftX(), 
					pencil4.getY() + pencil4.getHeight() + 57,
					pencil4.getWidth() + 1, midScreen + 66 - 
					(pencil4.getHeight()));
		}
	}
	
	/**
	 * Draws the horizontal pencils on the screen.
	 * @return void
	 */
	private void drawHorizontalPencils() {
		if (pencil1.isHorizontal()) {
			pencil1.setStartingRotation(90);
			batcher.draw(fullPencil, pencil1.getLeftX(), 
					pencil1.getHeight() + pencil1.getY(),
					pencil1.getWidth() / 2.0f, pencil1.getHeight() / 2.0f,
					pencil1.getWidth() * 3, pencil1.getHeight(), 
					1, 1, pencil1.getRotation());
			batcher.draw(fullPencil, pencil1.getLeftX(), pencil1.getHeight() -
					65 + pencil1.getY(), pencil1.getWidth() / 2.0f, 
					pencil1.getHeight() / 2.0f,
					pencil1.getWidth() * 3, pencil1.getHeight(), 
					1, 1, pencil1.getRotation());
			batcher.draw(fullPencil, pencil1.getLeftX(), pencil1.getHeight() +
					65 + pencil1.getY(), pencil1.getWidth() / 2.0f, 
					pencil1.getHeight() / 2.0f,
					pencil1.getWidth() * 3, pencil1.getHeight(), 
					1, 1, pencil1.getRotation());
		} 
		
		if (pencil2.isHorizontal()) {
			pencil2.setStartingRotation(90);
			batcher.draw(fullPencil, pencil2.getLeftX(), 
					pencil2.getHeight() + pencil2.getY(),
					pencil2.getWidth() / 2.0f, pencil2.getHeight() / 2.0f,
					pencil2.getWidth() * 3, pencil2.getHeight(), 
					1, 1, pencil2.getRotation());
			batcher.draw(fullPencil, pencil2.getLeftX(), pencil2.getHeight() -
					65 + pencil2.getY(), pencil2.getWidth() / 2.0f, 
					pencil2.getHeight() / 2.0f,
					pencil2.getWidth() * 3, pencil2.getHeight(), 
					1, 1, pencil2.getRotation());
			batcher.draw(fullPencil, pencil2.getLeftX(), pencil2.getHeight() +
					65 + pencil2.getY(), pencil2.getWidth() / 2.0f, 
					pencil2.getHeight() / 2.0f,
					pencil2.getWidth() * 3, pencil2.getHeight(), 
					1, 1, pencil2.getRotation());
		}
		
		if (pencil3.isHorizontal()) {
			pencil3.setStartingRotation(90);
			batcher.draw(fullPencil, pencil3.getLeftX(), 
					pencil3.getHeight() + pencil3.getY(),
					pencil3.getWidth() / 2.0f, pencil3.getHeight() / 2.0f,
					pencil3.getWidth() * 3, pencil3.getHeight(), 
					1, 1, pencil3.getRotation());
			batcher.draw(fullPencil, pencil3.getLeftX(), pencil3.getHeight() -
					65 + pencil3.getY(), pencil3.getWidth() / 2.0f, 
					pencil3.getHeight() / 2.0f,
					pencil3.getWidth() * 3, pencil3.getHeight(), 
					1, 1, pencil3.getRotation());
			batcher.draw(fullPencil, pencil3.getLeftX(), pencil3.getHeight() +
					65 + pencil3.getY(), pencil3.getWidth() / 2.0f, 
					pencil3.getHeight() / 2.0f,
					pencil3.getWidth() * 3, pencil3.getHeight(), 
					1, 1, pencil3.getRotation());
		}
		
		if (pencil4.isHorizontal()) {
			pencil4.setStartingRotation(90);
			batcher.draw(fullPencil, pencil4.getLeftX(), 
					pencil4.getHeight() + pencil4.getY(),
					pencil4.getWidth() / 2.0f, pencil4.getHeight() / 2.0f,
					pencil4.getWidth() * 3, pencil4.getHeight(), 
					1, 1, pencil4.getRotation());
			batcher.draw(fullPencil, pencil4.getLeftX(), pencil4.getHeight() -
					65 + pencil4.getY(), pencil4.getWidth() / 2.0f,
					pencil4.getHeight() / 2.0f,
					pencil4.getWidth() * 3, pencil4.getHeight(), 
					1, 1, pencil4.getRotation());
			batcher.draw(fullPencil, pencil4.getLeftX(), pencil4.getHeight() +
					65 + pencil4.getY(), pencil4.getWidth() / 2.0f, 
					pencil4.getHeight() / 2.0f,
					pencil4.getWidth() * 3, pencil4.getHeight(), 
					1, 1, pencil4.getRotation());
		}
	}
	
	/**
	 * Draws the full pencil images used when the pencils are
	 * spinning.
	 * @return void
	 */
	private void drawSpinningPencils() {
		if (pencil1.isSpinning()) {
			pencil1.rotate(10);
			batcher.draw(fullPencil, pencil1.getLeftX(), 
					pencil1.getHeight(),
					(pencil1.getWidth() * 3) / 2.0f, 
					pencil1.getHeight() / 2.0f,
					pencil1.getWidth() * 3, pencil1.getHeight(), 
					1, 1, pencil1.getRotation());
			batcher.draw(fullPencil, pencil1.getLeftX(),
					pencil1.getSpinningCollisionBottom().y - 150 - 
					pencil1.getSpinningCollisionBottom().radius,
					(pencil1.getWidth() * 3) / 2.0f, 
					pencil1.getHeight() / 2.0f,
					pencil1.getWidth() * 3, pencil1.getHeight(), 
					1, 1, pencil1.getRotation() * -1);
		}
		
		if (pencil2.isSpinning()) {
			pencil2.rotate(10);
			batcher.draw(fullPencil, pencil2.getLeftX(), 
					pencil2.getHeight(),
					(pencil2.getWidth() * 3) / 2.0f, 
					pencil2.getHeight() / 2.0f,
					pencil2.getWidth() * 3, pencil2.getHeight(), 
					1, 1, pencil2.getRotation());
			batcher.draw(fullPencil, pencil2.getLeftX(),
					pencil2.getSpinningCollisionBottom().y - 150 - 
					pencil2.getSpinningCollisionBottom().radius,
					(pencil2.getWidth() * 3) / 2.0f, 
					pencil2.getHeight() / 2.0f,
					pencil2.getWidth() * 3, pencil2.getHeight(), 
					1, 1, pencil2.getRotation() * -1);
		}
		
		if (pencil3.isSpinning()) {
			pencil3.rotate(10);
			batcher.draw(fullPencil, pencil3.getLeftX(),
					pencil3.getHeight(),
					(pencil3.getWidth() * 3) / 2.0f,
					pencil3.getHeight() / 2.0f,
					pencil3.getWidth() * 3, pencil3.getHeight(), 
					1, 1, pencil3.getRotation());
			batcher.draw(fullPencil, pencil3.getLeftX(),
					pencil3.getSpinningCollisionBottom().y - 150 - 
					pencil3.getSpinningCollisionBottom().radius,
					(pencil3.getWidth() * 3) / 2.0f, 
					pencil3.getHeight() / 2.0f,
					pencil3.getWidth() * 3, pencil3.getHeight(), 
					1, 1, pencil3.getRotation() * -1);
		}
		
		if (pencil4.isSpinning()) {
			pencil4.rotate(10);
			batcher.draw(fullPencil, pencil4.getLeftX(), 
					pencil4.getHeight(),
					(pencil4.getWidth() * 3) / 2.0f, 
					pencil4.getHeight() / 2.0f,
					pencil4.getWidth() * 3, pencil4.getHeight(), 
					1, 1, pencil4.getRotation());
			batcher.draw(fullPencil, pencil4.getLeftX(),
					pencil4.getSpinningCollisionBottom().y - 150 - 
					pencil4.getSpinningCollisionBottom().radius,
					(pencil4.getWidth() * 3) / 2.0f, 
					pencil4.getHeight() / 2.0f,
					pencil4.getWidth() * 3, pencil4.getHeight(), 
					1, 1, pencil4.getRotation() * -1);
		}
	}
	
	/**
	 * Helper method that draws all of the pencils in the game by
	 * calling other methods in this class.
	 */
	private void drawAllPencils() {
		// draws the vertical pencils
		if (pencil1.isVertical() || pencil2.isVertical() ||
				pencil3.isVertical() || pencil4.isVertical()) {
			drawPencilBodies();
			drawPencilTips();
		}
		
		// draws the horizontal pencils
		if (pencil1.isHorizontal() || pencil2.isHorizontal() ||
				pencil3.isHorizontal() || pencil4.isHorizontal())
			drawHorizontalPencils();
		
		// draws the spinning pencils
		if (pencil1.isSpinning() || pencil2.isSpinning() || 
				pencil3.isSpinning() || pencil4.isSpinning())
			drawSpinningPencils();
	}
	
	/**
	 * Helper method that draws the collectables
	 * to the game screen.
	 */
	private void drawCollectables() {
		if (world.getScore() > 4 && !collectable.isCollected()) {
			collectable.rotate(1);

			// if collectable doesn't overlap a pencil, then it gets drawn
			if (!(collectable.collectableCollidesPencil(pencil1) ||
					collectable.collectableCollidesPencil(pencil2) ||
					collectable.collectableCollidesPencil(pencil3) ||
					collectable.collectableCollidesPencil(pencil4))) {
				if (collectable.isM())
					batcher.draw(mSymbol, collectable.getLeftX(),
							collectable.getHeight() + collectable.getY(), 
							5, 5, 10, 10, 
							1, 1, collectable.getRotation());
				else if (collectable.isBeer())
					batcher.draw(beer, collectable.getLeftX(),
							collectable.getHeight() + collectable.getY(),
							5, 5, 10, 10, 
							1, 1, collectable.getRotation());
				else if (collectable.isCoffee())
					batcher.draw(coffee, collectable.getLeftX(),
							collectable.getHeight() + collectable.getY(), 
							5, 5, 10, 10, 
							1, 1, collectable.getRotation());
				else if (collectable.isPizza())
					batcher.draw(pizza, collectable.getLeftX(),
							collectable.getHeight() + collectable.getY(), 
							5, 5, 10, 10, 
							1, 1, collectable.getRotation());
			}
		}
	}
	
	/**
	 * Draws the background image of the game.
	 */
	private void drawBackground() {
		// draws the image of McKeldin library
		batcher.draw(AssetLoader.bgImage, -83, midScreen - 30, 300, 185);

		// draws the sky
		batcher.disableBlending();
		batcher.draw(AssetLoader.skyImage, 0, 0, 300, midScreen - 20);
	}
	
	/**
	 * Draws the game over menu on the screen.
	 */
	private void drawGameOverMenu() {
		if (world.isHighScore()) {
			gameoverLabel.setText("NEW HIGHSCORE");
			gameoverStyle.font.setScale(.25f);
		} else {
			gameoverLabel.setText("GAME OVER");
			gameoverStyle.font.setScale(.4f);
		}
		
		Gdx.input.setInputProcessor(AssetLoader.gameoverStage);

		// draws semi-transparent black background
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, .7f);
		shapeRenderer.rect(0, 0, 
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.end();

		// setting label to display the most current score
		scoreLabel.setText("Score: " + world.getScore());

		AssetLoader.gameoverStage.draw();
	}

}
