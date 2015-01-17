package com.taskew.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.taskew.gameworld.GameWorld;
import com.taskew.helpers.AssetLoader;

/**
 * @author Tyler Askew
 * @version 1.0
 * Simple menu class that create a main menu for the game.
 * Organizes the components into a table form and lets the user
 * access the various screens of the game.
 */
public class MainMenuScreen implements Screen {
	
	// represents the stage object
	private Stage stage;
	
	// creates a table layout for the menu screen
	private Table table, highscoresTable;
	
	// creates the titles and their styles for the menu screen
	private Label mainTitle, highscoresTitle;
	private Label.LabelStyle titleStyle, highscoreStyle;
	
	// contains the top ten highscores
	private ArrayList<Label> highscores;
	
	// the image buttons to play, get highscores, and share
	private ImageButton playButton, highscoresButton, menuButton;
	
	// represents the entire game
	private Game game;
			
	/**
	 * Constructor that initializes all of the components that
	 * make up the menu screen.
	 * @return none
	 */
	public MainMenuScreen(Game game) {
		stage = new Stage();
		table = new Table();
		highscoresTable = new Table();
		table.background(new SpriteDrawable(AssetLoader.menuBackground));
		
		// creating all of the buttons
		playButton = new ImageButton(new SpriteDrawable(AssetLoader.playButtonUp),
				new SpriteDrawable(AssetLoader.playButtonDown));
		highscoresButton = new ImageButton(new SpriteDrawable(AssetLoader.highscoresButtonUp),
				new SpriteDrawable(AssetLoader.highscoresButtonDown));
		menuButton = new ImageButton(new SpriteDrawable(AssetLoader.menuButtonUp),
				new SpriteDrawable(AssetLoader.menuButtonDown));
		
		// creating the titles 
		titleStyle = new Label.LabelStyle();
		titleStyle.font = AssetLoader.menuFont;
		mainTitle = new Label("TERP RUN", titleStyle);
		highscoresTitle = new Label("HIGHSCORES", titleStyle);
		
		// creates the display for the highscores
		highscoreStyle = new Label.LabelStyle();
		highscoreStyle.font = AssetLoader.regularFont;
		highscoresTable.background(new SpriteDrawable
				(AssetLoader.highscoreBackground));
		highscores = new ArrayList<Label>(5);
		
		this.game = game;
	}

	/**
	 * Creates the background of the menu screen and draws the
	 * stage where all of the components of the menu are kept.
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	/**
	 * Places all of the menu screens components onto the stage in
	 * an organized table form. Also adds the listeners to the buttons
	 * to account for user input.
	 */
	@Override
	public void show() {
		// add the title to the table
		AssetLoader.menuFont.setScale(.4f);
		table.add(mainTitle).padBottom(100).center().row();	
		
		// adds the buttons to the table
		table.add(playButton).padBottom(10).row();
		table.add(highscoresButton).padBottom(10).row();
		
		// adds table to the stage and spans it to the entire stage
		table.setFillParent(true);
		stage.addActor(table);
		
		// sets the input processor on the stage
		Gdx.input.setInputProcessor(stage);
		
		// opens a new game screen after play is clicked
		playButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game));
			}	
		});
		
		// displays the top ten highscores of the game
		highscoresButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// clear current table components
				table.clear();
				
				// add the highscores title
				AssetLoader.menuFont.setScale(.35f);
				table.add(highscoresTitle).padBottom(40).center().top().row();
				
				// getting all of the highscores and setting their style
				for (int i = 0; i < 5; i++)
					highscores.add(new Label(String.valueOf
							(AssetLoader.getHighScore(i)), highscoreStyle));
				
				// adding the highscores to the highscoresTable
				for (int i = 0; i < 5; i++)
					highscoresTable.add(highscores.get(i)).padBottom(10).row();
				
				// add highscores table to main table
				table.add(highscoresTable).padBottom(20).row();
				
				// add main menu button to the table
				table.add(menuButton).row();
			}
		});
		
		// refreshed the screen to show the initial main menu
		menuButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				highscoresTable.clear();
				table.clear();
				show();
			}
		});
	}
	
	/**
	 * Disposes of the resources that this screen uses once
	 * it has been closed.
	 */
	@Override
	public void dispose() {
		stage.dispose();
	}
	
	/**
	 * Just calls the dispose method.
	 */
	@Override
	public void hide() {
		dispose();
	}
	
	
	/********* THESE METHODS DO NOT NEED IMPLEMENTATION ************/

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
