package cc;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import cc.Main.GameState;
import lib.Vector2;

public class MainApplet extends Applet implements Runnable, KeyListener
{
	private long ticks = 0;
	/** plane of existence help by the applet **/
	private World world;
	/** UI for overlay and other user interaction **/
	private UI ui;

	
	private boolean displayStatsOverlay = false;
	/** color of background **/
	private Color backgroundColor = Color.GRAY;
	/** instance of self **/
	public static MainApplet main;
	/** Thread to run on **/
	private Thread thread = new Thread(this);
	/** Graphics to double buffer with **/
	private Graphics gg;
	/** Image to double buffer with **/
	private BufferedImage bi;

	/** GameState */
	private GameState gameState = GameState.PLAY;

	// Applet core
	// ------------------------------------

	@Override
	public void init()
	{
		// setup the window
		setSize(Main.worldWidth, Main.worldHeight);
		setBackground(backgroundColor);
		addKeyListener(this);
		setFocusTraversalKeysEnabled(false);
		
		// initialize instance variables
		if (main == null)
			main = this;
		world = new World(this, new Vector2(Main.worldWidth, Main.worldHeight));
		ui = new UI(this);
	}

	@Override
	public void start()
	{
		thread.setDaemon(true);
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
		ticks++;
		bi = new BufferedImage((int) world.getSize().getX(), (int) world.getSize().getY(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = bi.createGraphics();

		// paint world
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, (int) world.getSize().getX(), (int) world.getSize().getY());
		paint(g2);

		// paint the scaled image on the applet
		g.drawImage(bi, 0, 0, getWidth(), getHeight(), null);
	}

	@Override
	public void paint(Graphics g)
	{
		world.paint(g);
		if (gameState.equals(GameState.PAUSED)) ui.drawPauseScreen(g);
		if(displayStatsOverlay) ui.drawFullStatsOverlay(g);
	}

	@Override
	public void run()
	{
		while (!gameState.equals(GameState.OVER))
		{
			if (gameState.equals(GameState.PLAY))
			{
				world.update();
			}	
			repaint();

			// sleep for rest of frame time
			try
			{
				Thread.sleep(Main.frameRate);
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
		world = new World(main, new Vector2(Main.worldWidth, Main.worldHeight));
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

	public void togglePause()
	{
		if (gameState.equals(GameState.PLAY))
			gameState = GameState.PAUSED;
		else if (gameState.equals(GameState.PAUSED))
			gameState = GameState.PLAY;
	}

	// Event Listeners
	// -------------------------------------------------------------------------------------

	@Override
	public void keyTyped(KeyEvent e)
	{
		if (e.getKeyChar() == 'p') togglePause();
		if (e.getKeyChar() == 'd') Main.DEBUG = !Main.DEBUG;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyChar() == '\t') displayStatsOverlay = true;
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyChar() == '\t') displayStatsOverlay = false;
	}

	// Getters and Setters
	// --------------------------------------------------------------------------------------------

	/**
	 * The getWidth method returns the width of the applet.
	 * @return width of applet
	 */
	public int getWidth()
	{
		return (int) getSize().getWidth();// frame.getWidth();
	}

	/**
	 * The setWidth method sets the width of the applet.
	 * @param width width of applet
	 */
	public void setWidth(int width)
	{
		setSize(width, (int) getSize().getHeight());
	}

	/**
	 * The getHeight method returns the height of the applet.
	 * @return height of applet
	 */
	public int getHeight()
	{
		return (int) getSize().getHeight();// frame.getHeight();
	}

	/**
	 * The setHeight method sets the height of the applet.
	 * @param height height of applet
	 */
	public void setHeight(int height)
	{
		setSize((int) getSize().getWidth(), height);
	}

	/**
	 * The getGameState method returns the current game state.
	 * @return The current game state
	 */
	public GameState getGameState()
	{
		return this.gameState;
	}

	/**
	 * The setGameState method sets gameState to the desired state
	 * @param gameState game state
	 */
	public void setGameState(GameState gameState)
	{
		this.gameState = gameState;
	}

	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	public long getTicks()
	{
		return ticks;
	}

	public void setTicks(long ticks)
	{
		this.ticks = ticks;
	}
}
