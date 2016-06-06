package world;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import app.Main;
import lib.Parser;
import lib.Vector2;


/**
 * The Obstacle class is responsible for rendering immovable and collidable objects in the world.
 * @author Robert
 * @version 0.1
 */
public class Obstacle extends Sprite
{
	
	/** The possible sizes the obstacle can be*/
	public static final List<List<Float>> SIZES = Parser.parse2DImmutableFloatArray(Main.CONFIG.get("obstacleSizes"));
	
	/**
	 * Obstacle Copy Constructor.
	 *
	 * @param o the o
	 */
	public Obstacle(Obstacle o)
	{
		super(o);
	}

	/**
	 * Instantiates a new obstacle with a random size.
	 *
	 * @param world the world
	 */
	public Obstacle(World world)
	{
		// @formatter:off
		super(new Vector2(SIZES.get((int) (Math.random() * SIZES.size())).toArray(new Float[2])),
				new Vector2(0, 0),
				new Vector2(1, (float) (Math.random() * Math.PI * 2), true),
				new Vector2(0, 0),
				Color.BLACK,
				world
				);
		setPosition(new Vector2(
				(float) (Math.random() * (world.getSize().getX() - getSize().getX()) + getSize().getX() / 2),
				(float) (Math.random() * (world.getSize().getY() - getSize().getY()) + getSize().getY() / 2)
				));
		// @formatter:on
	}

	/**
	 * 5-Argument Obstacle Constructor.
	 *
	 * @param size the size
	 * @param position position
	 * @param color color
	 * @param world plane of existence
	 */
	public Obstacle(Vector2 size, Vector2 position, Color color, World world)
	{
		super(size, position, new Vector2(0, 0), new Vector2(0, 0), color, world);
	}

	// Overridden methods
	// --------------------------------------------

	/* (non-Javadoc)
	 * @see world.Sprite#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		// @formatter:off
		// paint rectangle for obstacle
		g.setColor(getColor());
		g.fillRect(
				(int) (getPosition().getX() - getSize().getX() / 2),
				(int) (getPosition().getY() - getSize().getY() / 2),
				(int) getSize().getX(), (int) getSize().getY());
		// @formatter:on		
	}

	/* (non-Javadoc)
	 * @see world.Sprite#update()
	 */
	@Override
	public void update()
	{
	}

	/* (non-Javadoc)
	 * @see world.Sprite#collide(world.Sprite)
	 */
	@Override
	public void collide(Sprite s)
	{
		if(s == null) return;
		if(s instanceof Obstacle) slide(s);
	}

	/* (non-Javadoc)
	 * @see world.Sprite#copy()
	 */
	@Override
	public Sprite copy()
	{
		return new Obstacle(this);
	}
}
