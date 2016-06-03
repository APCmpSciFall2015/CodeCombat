package app;

import java.awt.Dimension;
import java.io.File;
import java.io.Serializable;
import lib.Parser;
import javax.swing.JFrame;

/**
 * The Main class is the host applet for the game.
 * @author Robert
 * @version 0.1
 */
public class Main implements Serializable
{
	public static final File CONFIG_FILE = new File("./res/config.txt");
	public static final Config CONFIG = new Config(new File("./res/config.txt"));
	/** Game settings **/
	public static final File GAME_SETTINGS_FILE = new File("./res/config.txt");
	private static final Config GAME_SETTINGS = new Config(new File("./res/game_settings.txt"));
	
	/** Serializable id **/
	private static final long serialVersionUID = 3206847208968227199L;
	/** JFrame to hold applet **/
	private static JFrame frame;
	/** debug mode enabled? **/	
	public static boolean debug = Boolean.parseBoolean(CONFIG.get("debug"));
	/** frame rate of applet set to 60 fps **/
	public static final long FRAME_RATE = Integer.parseInt(CONFIG.get("frameRate"));
	/** world width **/
	public static final int worldWidth = Integer.parseInt(CONFIG.get("worldWidth"));
	/** world height **/
	public static final int worldHeight = Integer.parseInt(CONFIG.get("worldHeight"));
	/** GameState enum */
	public static enum GameState
	{
		MENU, PLAY, PAUSED, OVER;
	}

	// Initialization of JFrame
	public static void main(String[] args)
	{
		frame = new MainFrame("Code Combat", 
					new Dimension(worldWidth, worldHeight),
					new MainApplet());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
		    	CONFIG.save(CONFIG_FILE);
		    	GAME_SETTINGS.save(GAME_SETTINGS_FILE);
		    }
		}));
	}
}

