package cc;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Main extends Applet implements Runnable
{
	// Applet parameters
	// -------------------------------------------------------------

	/** frame rate of applet set to 60 fps **/
	public static final long frameRate = 1000 / 60;
	/** scale of time as pertinent to frameRate **/
	public static final double timeScale = 1.0 / 12.0;
	/** width of applet **/
	private int width = 800;
	/** height of applet **/
	private int height = 600;
	/** color of background **/
	private Color backgroundColor = Color.BLACK;
	/** instance of self **/
	public static Main main;
	/** Thread to run on **/
	private Thread thread = new Thread(this);
	/** Graphics to double buffer with **/
	private Graphics gg;
	/** Image to double buffer with **/
	private Image ii;
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
	}

	@Override
	public void run()
	{
		while (true)
		{
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
}
