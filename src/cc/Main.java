package cc;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import lib.Vector2;

/**
 * The Main class is the host applet for the game.
 * @author Robert
 * @version 0.1
 */
public class Main extends Applet implements Runnable, ComponentListener
{
	public static final boolean DEBUG = true;

	/** plane of existence help by the applet **/
	private World world;

	// Applet parameters
	// -------------------------------------------------------------

	/** frame rate of applet set to 60 fps **/
	public static final long frameRate = 1000 / 60;
	/** width of applet **/
	private static int worldWidth = 800;
	/** height of applet **/
	private static int worldHeight = 600;
	/** color of background **/
	private Color backgroundColor = Color.GRAY;
	/** instance of self **/
	public static Main main;
	/** Thread to run on **/
	private Thread thread = new Thread(this);
	/** Graphics to double buffer with **/
	private Graphics gg;
	/** Image to double buffer with **/
	private BufferedImage bi;
	/** Serializable id **/
	private static final long serialVersionUID = 3206847208968227199L;

	/** GameState enum */
	public static enum GameState
	{
		MENU, PLAY, PAUSED;
	}

	/** GameState */
	private static GameState gameState = GameState.PLAY;
	
	private static JFrame frame;
	
	// Initialization of JFrame
	public static void main(String[] args)
	{
		frame = new JFrame("Code Combat");
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(worldWidth, worldHeight));
		frame.add(panel);
		
		Applet applet = new Main();
		applet.init();
		applet.start();
		
		panel.add(applet, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	//  Applet core
	// ------------------------------------
	
	@Override
	public void init()
	{
		// setup the window
		if (main == null)
		{
			main = this;
		}
		setSize(worldWidth, worldHeight);
		world = new World(main, new Vector2(worldWidth, worldHeight));
		setBackground(backgroundColor);
	}

	@Override
	public void start()
	{
		thread.start();
	}

	@Override
	public void stop()
	{
	}

	@Override
	public void destroy()
	{
	}

	/**
	 * The update method double buffers the contents of the screen while an
	 * update is occurring.
	 * @param g Graphics of screen
	 */
	@Override
	public void update(Graphics g)
	{
		bi = new BufferedImage((int) world.getSize().getX(), (int) world.getSize().getY(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();
		
		// paint world
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, (int) world.getSize().getX(), (int) world.getSize().getY());
		paint(g2);
		
		// paint the scaled image on the applet
		g.drawImage(bi, 0, 0, getWidth(), getHeight(),  null);
	}
	
	@Override
	public void paint(Graphics g)
	{
		world.paint(g);
	}

	@Override
	public void run()
	{
		while (gameState.equals(GameState.PLAY))
		{
			world.update();
			repaint();

			// sleep for rest of frame time
			try
			{
				Thread.sleep(frameRate);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	// Game Actions
	// -------------------------------------------------------

	/**
	 * The restart method resets the world and restarts the game.
	 */
	public void restart()
	{
		world = new World(main, new Vector2(worldWidth, worldHeight));
	}

	/**
	 * The pause method sets the gameState enum to PAUSED and opens the pause
	 * menu.
	 */
	public void pause()
	{
		gameState = GameState.PAUSED;
		// Open menu
	}

	/**
	 * The end method sets the gameState enum to MENU and returns the game to
	 * the main menu.
	 */
	public void end()
	{
		gameState = GameState.MENU;
		// Do menu stuff
	}

	// Getters and Setters
	// --------------------------------------------------------------------------------------------

	/**
	 * The getWidth method returns the width of the applet.
	 * @return width of applet
	 */
	public int getWidth()
	{
		return (int)  getSize().getWidth();//frame.getWidth();
	}

	/**
	 * The setWidth method sets the width of the applet.
	 * @param width width of applet
	 */
	public void setWidth(int width)
	{
		this.worldWidth = width;
		setSize(width, worldHeight);
	}

	/**
	 * The getHeight method returns the height of the applet.
	 * @return height of applet
	 */
	public int getHeight()
	{
		return (int) getSize().getHeight();//frame.getHeight();
	}

	/**
	 * The setHeight method sets the height of the applet.
	 * @param height height of applet
	 */
	public void setHeight(int height)
	{
		this.worldHeight = height;
		setSize(worldWidth, height);
	}

	/**
	 * The getGameState method returns the current game state.
	 * @return The current game state
	 */
	public static GameState getGameState()
	{
		return gameState;
	}

	/**
	 * The setGameState method sets gameState to the desired state
	 * @param gameState game state
	 */
	public static void setGameState(GameState gameState)
	{
		Main.gameState = gameState;
	}

	@Override
	public void componentHidden(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0)
	{
		
	}

	@Override
	public void componentShown(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
