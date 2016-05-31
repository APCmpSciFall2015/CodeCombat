package app;

import java.awt.Dimension;
import java.io.File;
import java.io.Serializable;
import javax.swing.JFrame;

import world.World;

/**
 * The Main class is the host applet for the game.
 * @author Robert
 * @version 0.1
 */
public class Main implements Serializable
{
	public static final File CONFIG_FILE = new File("./res/config.txt");
	public static Config config = new Config(CONFIG_FILE);
	/** debug mode enabled? **/
	public static boolean debug = Boolean.parseBoolean(config.get("debug"));
	/** frame rate of applet set to 60 fps **/
	public static final long frameRate = Integer.parseInt(config.get("frameRate"));

	/** GameState enum */
	public static enum GameState
	{
		MENU, PLAY, PAUSED, OVER;
	}

	/** Serializable id **/
	private static final long serialVersionUID = 3206847208968227199L;
	/** JFrame to hold applet **/
	private static JFrame frame;

	// Initialization of JFrame
	public static void main(String[] args)
	{
		frame = new MainFrame("Code Combat", new Dimension(
					Integer.parseInt(config.get("worldWidth")),
					Integer.parseInt(config.get("worldHeight"))),
				new MainApplet());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
		    	System.out.println("save config");
		    	config.save(CONFIG_FILE);
		    }
		}));
	}
}
