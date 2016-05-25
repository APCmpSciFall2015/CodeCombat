package cc;

import java.awt.Dimension;
import java.io.Serializable;
import javax.swing.JFrame;

/**
 * The Main class is the host applet for the game.
 * @author Robert
 * @version 0.1
 */
public class Main implements Serializable
{
	/** debug mode enabled? **/
	public static boolean DEBUG = false;
	/** frame rate of applet set to 60 fps **/
	public static final long frameRate = 1000 / 60;

	/** GameState enum */
	public static enum GameState
	{
		MENU, PLAY, PAUSED, OVER;
	}

	/** Serializable id **/
	private static final long serialVersionUID = 3206847208968227199L;
	/** width of applet **/
	public static int worldWidth = 800;
	/** height of applet **/
	public static int worldHeight = 600;
	/** JFrame to hold applet **/
	private static JFrame frame;

	// Initialization of JFrame
	public static void main(String[] args)
	{
		frame = new MainFrame("Code Combat", new Dimension(worldWidth, worldHeight), new MainApplet());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
