package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

/**
 * The sprite class represents an entity of a given World.
 * @author Robert, Dalton, Brian, Derek
 * @version 0.1
 * @see World
 */
public abstract class Sprite
{
	/** maximum  velocity of sprites **/
	public static final float MAX_VELOCITY = 20f;
	/** whether or not the sprite is alive **/
	private boolean alive = true;
	/** plane of existence for the sprite **/
	private World world;
	/** width of sprite **/
	private int width;
	/** height of sprite **/
	private int height;
	/** color of sprite **/
	private Color color;
	/** location of sprite **/
	private Vector2 position;
	/** rate of change in location of sprite **/
	private Vector2 velocity;
	/** rate of change in rate of change of location of sprite **/
	private Vector2 acceleration;

	// Constructors
	// --------------------------------------------------

	/**
	 * Sprite Copy Constructor
	 * @param s sprite to copy
	 */
	public Sprite(Sprite s)
	{
		this.width = s.width;
		this.height = s.height;
		this.position = s.position.copy();
		this.velocity = s.velocity.copy();
		this.acceleration = s.acceleration.copy();
		this.color = s.color;
		this.world = s.world;
	}

	/**
	 * 7-Argument Sprite Constructor
	 * @param width width
	 * @param height height
	 * @param position position vector
	 * @param velocity rate of change in position vector
	 * @param acceleration rate of change in rate of change of position / rate
	 *        of change in velocity / acceleration vector
	 * @param color color
	 * @param world plane of existence
	 */
	public Sprite(int width, int height, Vector2 position, Vector2 velocity, Vector2 acceleration, Color color,
			World world)
	{
		this.width = width;
		this.height = height;
		this.position = position.copy();
		this.velocity = velocity.copy();
		this.acceleration = acceleration.copy();
		this.color = color;
		this.world = world;
	}

	// Instance methods
	// ----------------------------------------------------
	
	/**
	 * The slide method sets the position of a colliding object such that its
	 * bounding box lies on the edge of the colliding object's bounding box.
	 * @param s colliding sprite
	 */
	public void slide(Sprite s)
	{
		// slide on left and right of sprite
		if (getPosition().getX() - getWidth() / 2 < s.getPosition().getX() + s.getWidth())
			setPosition(getPosition().setX(s.getPosition().getX() - s.getWidth() / 2 + getWidth()));
		else if (getPosition().getX() + getWidth() / 2 > s.getPosition().getX() - s.getWidth() / 2)
			setPosition(getPosition().setX(s.getPosition().getX() + s.getWidth() / 2 + getWidth()));

		// slide on top and bottom of sprite
		if (getPosition().getY() - getHeight() / 2 < s.getPosition().getY() + s.getHeight())
			setPosition(getPosition().setY(s.getPosition().getY() - s.getHeight() / 2 + getHeight()));
		else if (getPosition().getY() + getHeight() / 2 > s.getPosition().getY() - s.getHeight() / 2)
			setPosition(getPosition().setY(s.getPosition().getY() + s.getHeight() / 2 + getHeight()));
	}

	/**
	 * The exchange method sets the velocity of this sprite to the velocity of a
	 * colliding sprite.
	 * @param s colliding sprite
	 */
	public void exchange(Sprite s)
	{
		// this changes the velocity of this sprite... how will the other sprite get my velocity?
		setVelocity(s.getVelocity());
	}

	/**
	 * The bounce method rebounds the velocity of this sprite in the x and/or y
	 * direction from an obstacle.
	 * @param s colliding sprite
	 */
	public void bounce(Sprite s)
	{
		// bounce in X direction
		if (getPosition().getX() < s.getPosition().getX() + s.getWidth() / 2
				&& getPosition().getX() > s.getPosition().getX() - s.getWidth() / 2)
		{
			setVelocity(getVelocity().setY(-getVelocity().getY()));
		}
		// bounce in Y direction
		if (getPosition().getY() < s.getPosition().getY() + s.getWidth() / 2
				&& getPosition().getY() > s.getPosition().getY() - s.getWidth() / 2)
		{
			setVelocity(getVelocity().setX(-getVelocity().getX()));
		}
	}

	// Abstract methods
	// -----------------------------------------------------

	/**
	 * The paint method draws the sprite
	 * @param g Graphics to paint on
	 */
	public abstract void paint(Graphics g);

	/**
	 * The update method updates the state of the sprite.
	 */
	public abstract void update();

	/**
	 * The collide method defines collision behavior for the sprite upon collision with another sprite.
	 * @param s colliding sprite
	 */
	public abstract void collide(Sprite s);

	// Getters and setters
	// -----------------------------------------------------

	public Vector2 getPosition()
	{
		return position.copy();
	}

	public void setPosition(Vector2 position)
	{
		this.position = position.copy();
	}

	public Vector2 getVelocity()
	{
		return velocity.copy();
	}

	public void setVelocity(Vector2 velocity)
	{
		this.velocity = velocity.copy();
	}

	public Vector2 getAcceleration()
	{
		return acceleration.copy();
	}

	public void setAcceleration(Vector2 acceleration)
	{
		this.acceleration = acceleration.copy();
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	protected World getWorld()
	{
		return world;
	}

	protected void setWorld(World world)
	{
		this.world = world;
	}

	public boolean isAlive()
	{
		return alive;
	}

	public void setAlive(boolean alive)
	{
		this.alive = alive;
	}
}
