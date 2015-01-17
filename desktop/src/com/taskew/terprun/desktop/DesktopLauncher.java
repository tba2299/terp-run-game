package com.taskew.terprun.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.taskew.terprun.TerpRunClient;

/**
 * @author Tyler Askew
 * @version 1.0
 * Simple launcher class that's only purpose is to start a
 * desktop emulation of the game.
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = 
        		new LwjglApplicationConfiguration();
        config.title = "Terp Run";
        config.width = 272;
        config.height = 408;
        new LwjglApplication(new TerpRunClient(), config);
	}
}