package cc;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 * The Main class is the host applet for the game.
 * @author Robert
 * @version 0.1
 */
public class Main extends Applet implements Runnable
{
	public static final boolean DEBUG = false;
	
	/** plane of existence help by the applet **/
	private World world;
	
	// Applet parameters
	// -------------------------------------------------------------

	/** frame rate of applet set to 60 fps **/
	public static final long frameRate = 1000 / 60;
	/** width of applet **/
	private int width = 800;
	/** height of applet **/
	private int height = 600;
	/** color of background **/
	private Color backgroundColor = Color.GRAY;
	/** instance of self **/
	public static Main main;
	/** Thread to run on **/
	private Thread thread = new Thread(this);
	/** Graphics to double buffer with **/
	private Graphics gg;
	/** Image to double buffer with **/
	private Image ii;
	/** Serializable id **/
	private static final long serialVersionUID = 3206847208968227199L;

	@Override
	public void init()
	{
		// setup the window
		if (main == null)
		{
			main = this;
		}
		setSize(width, height);
		setBackground(backgroundColor);
		// setup game
		world = new World(main);
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
		if (ii == null)
		{
			ii = createImage(this.getWidth(), this.getHeight());
			gg = ii.getGraphics();
		}

		gg.setColor(getBackground());
		gg.fillRect(0, 0, this.getWidth(), this.getHeight());

		gg.setColor(getForeground());
		paint(gg);

		g.drawImage(ii, 0, 0, this);
	}

	@Override
	public void paint(Graphics g)
	{
		world.paint(g);
	}

	@Override
	public void run()
	{
		while (true)
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

	/**
	 * The getWidth method returns the width of the applet.
	 * @return width of applet
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * The setWidth method sets the width of the applet.
	 * @param width width of applet
	 */
	public void setWidth(int width)
	{
		this.width = width;
		setSize(width, height);
	}

	/**
	 * The getHeight method returns the height of the applet.
	 * @return height of applet
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * The setHeight method sets the height of the applet.
	 * @param height height of applet
	 */
	public void setHeight(int height)
	{
		this.height = height;
		setSize(width, height);
	}
}
