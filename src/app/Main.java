package app;

import java.awt.Dimension;
import java.io.File;
import java.io.Serializable;
import javax.swing.JFrame;
import lib.Config;

/**
 * The Main class is the host applet for the game.
 * @author Robert
 * @version 0.1
 */
public class Main implements Serializable
{
	/** Config file **/
	public static final File CONFIG_FILE = new File("./res/config.txt");
	
	/** Config data **/
	public static final Config CONFIG = new Config(CONFIG_FILE);
	
	/** Game settings file **/
	public static final File GAME_SETTINGS_FILE = new File("./res/game_settings.txt");
	
	/** Game settings **/
	public static final Config GAME_SETTINGS = new Config(GAME_SETTINGS_FILE);
	
	/** Serializable id **/
	private static final long serialVersionUID = 3206847208968227199L;
	
	/** frame rate of applet set to 60 fps **/
	public static final long FRAME_RATE = Integer.parseInt(CONFIG.get("frameRate"));
	
	/** world width **/
	public static final int WORLD_WIDTH = Integer.parseInt(CONFIG.get("worldWidth"));	
	
	/** world height **/
	public static final int WORLD_HEIGHT = Integer.parseInt(CONFIG.get("worldHeight"));

	/** JFrame to hold applet **/
	// @formatter:off
	private static final JFrame FRAME = new MainFrame("Code Combat", 
											new Dimension(WORLD_WIDTH, WORLD_HEIGHT),
											new MainApplet());;
	// @formatter:on
	
	/** GameState enum */
	public static enum GameState
	{
		MENU, PLAY, PAUSED, OVER;
	}
											
	// Alterable values
	// ---------------------------------------
											
	/** debug mode enabled? **/
	public static boolean debug = Boolean.parseBoolean(CONFIG.get("debug"));

	// Core init
	// -------------------------------------------------
	
	// Initialization of Application
	public static void main(String[] args)
	{
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
		{
			public void run()
			{
				CONFIG.save(CONFIG_FILE);
				GAME_SETTINGS.save(GAME_SETTINGS_FILE);
			}
		}));
	}
}
