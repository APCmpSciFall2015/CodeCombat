package cc;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
		//setup the menu
		//Menu menu = new Menu();
		//frame.setJMenuBar(menu.createMenuBar());
		
		JMenuBar menuBar;
		JMenu menu;
		JCheckBoxMenuItem cbMenuItem;
		
		menuBar = new JMenuBar();
		
		menu = new JMenu("Game Controls");
		menuBar.add(menu);
		
		cbMenuItem = new JCheckBoxMenuItem("Pause");
		//cbMenuItem.addActionListener(this);
		menu.add(cbMenuItem);
		
		cbMenuItem = new JCheckBoxMenuItem("Debug");
		//cbMenuItem.addActionListener(this);
		menu.add(cbMenuItem);
		frame.setJMenuBar(menuBar);
	}

}
