package com.taskew.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

/**
 * @author Tyler Askew
 * @version 1.0
 * Contains all of the images, sounds, animations, and other necessary
 * objects that will be used to create the visual display of the game.
 */
public class AssetLoader {

	// textures made up with the terp, pencils, collectables, and background
	public static Texture terpTexture;
	public static Texture pBodyTexture, pTipTexture;
	public static Texture pFullTexture;
	public static Texture bgTexture;
	public static Texture collectablesTexture;
	public static Texture highscoreBGTexture;
	
	// textures used for the menus and user input
	public static Texture buttonsTexture, menuBGTexture;
	
	// terp image
	public static TextureRegion terp;
	
	// contains the pencil imagery
	public static TextureRegion pencilBody, pencilTipUp, pencilTipDown;
	public static TextureRegion fullPencil;
	
	// contains the collectable images
	public static TextureRegion mSymbol, beer, pizza, coffee;
	
	// contains the background and sky images
	public static TextureRegion bgImage, skyImage;
	
	// contains the images for the menus
	public static Sprite playButtonUp, playButtonDown;
	public static Sprite highscoresButtonUp, highscoresButtonDown;
	public static Sprite menuButtonUp, menuButtonDown;
	public static Sprite highscoreBackground, menuBackground;
	public static Sprite replayButtonUp, replayButtonDown;
		
	// all of the sounds in the game
	public static Sound dead, flap, score, collect;
	
	// fonts being used in the game
	public static BitmapFont gameplayFont, gameplayShadow;
	public static BitmapFont menuFont, regularFont;
	
	// used to overlay a game over menu on the screen
	public static Stage gameoverStage;
	
	// storage of the high scores
	public static Preferences highScores;
	
	/**
	 * Initializes all of the assets that will be used within
	 * the game. (e.g.  sounds, images, animations, etc.)
	 * @return void
	 */
	public static void load() {
		// initializing the terp texture
		terpTexture = new Texture(Gdx.files.internal("images/testudo.png"));
		terpTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		terp = new TextureRegion(terpTexture, 0, 0, 48, 48);
		terp.flip(false, true);
		
		// initializing the pencil body structures
		pBodyTexture = new Texture(Gdx.files.internal
				("images/pencil_body.png"));
		pBodyTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		pencilBody = new TextureRegion(pBodyTexture, 0, 0, 92, 16);
		pencilBody.flip(false, true);
		
		// initializing the pencil tip structures
		pTipTexture = new Texture(Gdx.files.internal
				("images/pencil_tip.png"));
		pTipTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		pencilTipUp = new TextureRegion(pTipTexture, 0, 0, 128, 128);
		pencilTipDown = new TextureRegion(pencilTipUp);
		pencilTipDown.flip(false, true);
		
		// initializing the full pencil
		pFullTexture = new Texture(Gdx.files.internal
				("images/full_pencil.png"));
		pFullTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		fullPencil = new TextureRegion(pFullTexture, 0, 0, 512, 512);
		fullPencil.flip(false, true);
		
		// initializing the background and sky visuals
		bgTexture = new Texture(Gdx.files.internal("images/McKeldin.png"));
		bgTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		bgImage = new TextureRegion(bgTexture, 0, 0, 500, 376);
		bgImage.flip(false, true);
		skyImage = new TextureRegion(bgTexture, 0, 0, 500, 25);
		skyImage.flip(false, true);
		
		// initializing the collectable images
		collectablesTexture = 
				new Texture(Gdx.files.internal("images/collectables.png"));
		collectablesTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		mSymbol = new TextureRegion(collectablesTexture, 232, 2, 48, 48);
		mSymbol.flip(false, true);
		beer = new TextureRegion(collectablesTexture, 132, 2, 98, 108);
		beer.flip(false, true);
		pizza = new TextureRegion(collectablesTexture, 232, 52, 32, 32);
		pizza.flip(false, true);
		coffee = new TextureRegion(collectablesTexture, 2, 2, 122, 128);
		coffee.flip(false, true);
		
		// initializing the sound files
		dead = Gdx.audio.newSound(Gdx.files.internal("sounds/dead.wav"));
		flap = Gdx.audio.newSound(Gdx.files.internal("sounds/flap.wav"));
		score = Gdx.audio.newSound(Gdx.files.internal("sounds/score.wav"));
		collect = Gdx.audio.newSound(Gdx.files.internal("sounds/collect.wav"));
		
		// initializing main menu images
		buttonsTexture = 
				new Texture(Gdx.files.internal("buttons/buttons.png"));
		buttonsTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		playButtonDown = 
				new Sprite(new TextureRegion(buttonsTexture, 810, 2, 200, 50));
		playButtonUp = 
				new Sprite(new TextureRegion(buttonsTexture, 1012, 2, 200, 50));
		highscoresButtonDown = 
				new Sprite(new TextureRegion(buttonsTexture, 2, 2, 200, 50));
		highscoresButtonUp =
				new Sprite(new TextureRegion(buttonsTexture, 204, 2, 200, 50));
		menuButtonDown = 
				new Sprite(new TextureRegion(buttonsTexture, 406, 2, 200, 50));
		menuButtonUp =
				new Sprite(new TextureRegion(buttonsTexture, 608, 2, 200, 50));
		replayButtonDown = 
				new Sprite(new TextureRegion(buttonsTexture, 1618, 2, 200, 50));
		replayButtonUp =
				new Sprite(new TextureRegion(buttonsTexture, 1820, 2, 200, 50));
		
		menuBGTexture = new Texture(Gdx.files.internal("images/menuBG.png"));
		menuBGTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		menuBackground = new Sprite(menuBGTexture);
		highscoreBGTexture = 
				new Texture(Gdx.files.internal("images/highscoreBackground.png"));
		highscoreBackground = new Sprite(highscoreBGTexture);
		
		// initializing the fonts
		gameplayFont = new BitmapFont(Gdx.files.internal("fonts/in_game_text.fnt"));
		gameplayFont.setScale(.25f, -.25f);
		gameplayShadow = new BitmapFont(Gdx.files.internal("fonts/in_game_shadow.fnt"));
		gameplayShadow.setScale(.25f, -.25f);
		menuFont = new BitmapFont(Gdx.files.internal("fonts/main_menu_font.fnt"));
		regularFont = new BitmapFont(Gdx.files.internal("fonts/regular_font.fnt"));
		regularFont.setScale(.65f);
		
		// initializing the variable or retrieving past high scores
		highScores = Gdx.app.getPreferences("Terp Run");
		if (!highScores.contains("highScore"))
			highScores.putInteger("highScore", 0);
		
		// initializing the stage
		gameoverStage = new Stage();
	}
	
	/**
	 * Disposing of all textures to free up resources.
	 * @return void
	 */
	public static void dispose() {
		terpTexture.dispose();
		pBodyTexture.dispose();
		pTipTexture.dispose();
		pFullTexture.dispose();
		collectablesTexture.dispose();
		bgTexture.dispose();
		dead.dispose();
		flap.dispose();
		score.dispose();
		collect.dispose();
		gameplayFont.dispose();
		gameplayShadow.dispose();
		menuFont.dispose();
		regularFont.dispose();
		buttonsTexture.dispose();
		highscoreBGTexture.dispose();
		menuBGTexture.dispose();
		gameoverStage.dispose();
	}
	
	/**
	 * Retrieves the high score of the game.
	 * @param i Int containing the number corresponding to one of the top ten
	 * highscores.
	 * @return Int containing the highest score.
	 */
	public static int getHighScore(int i) {
		return highScores.getInteger("highScore" + i);
	}
	
	/**
	 * Sets the highest score of the game to a specified
	 * integer.
	 * @param score Int containing the new high score.
	 * @param i Int containing the rank of the new highscore.
	 * @return void
	 */
	public static void setHighScore(int score, int i) {
		highScores.putInteger("highScore" + i, score);
		highScores.flush();
	}
	
}
