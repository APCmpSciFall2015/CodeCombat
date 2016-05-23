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
	/** color of sprite **/
	private Color color;
	/** size of sprite **/
	private Vector2 size;
	/** location of sprite **/	
	private Vector2 position;
	/** rate of change in location of sprite **/
	private Vector2 velocity;
	/** rate of change in rate of change of location of sprite **/
	private Vector2 acceleration;

	// Constructors
	// --------------------------------------------------

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
	public Sprite(Vector2 size, Vector2 position, Vector2 velocity, Vector2 acceleration, Color color,
			World world)
	{
		this.size = size.copy();
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
		if (getPosition().getX() - getSize().getX() / 2 < s.getPosition().getX() + s.getSize().getX())
			setPosition(getPosition().setX(s.getPosition().getX() - s.getSize().getX() / 2 + getSize().getX()));
		else if (getPosition().getX() + getSize().getX() / 2 > s.getPosition().getX() - s.getSize().getX() / 2)
			setPosition(getPosition().setX(s.getPosition().getX() + s.getSize().getX() / 2 + getSize().getX()));

		// slide on top and bottom of sprite
		if (getPosition().getY() - getSize().getY() / 2 < s.getPosition().getY() + s.getSize().getY())
			setPosition(getPosition().setY(s.getPosition().getY() - s.getSize().getY() / 2 + getSize().getY()));
		else if (getPosition().getY() + getSize().getY() / 2 > s.getPosition().getY() - s.getSize().getY() / 2)
			setPosition(getPosition().setY(s.getPosition().getY() + s.getSize().getY() / 2 + getSize().getY()));
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
		if (getPosition().getX() < s.getPosition().getX() + s.getSize().getX() / 2
				&& getPosition().getX() > s.getPosition().getX() - s.getSize().getX() / 2)
		{
			setVelocity(getVelocity().setY(-getVelocity().getY()));
		}
		// bounce in Y direction
		if (getPosition().getY() < s.getPosition().getY() + s.getSize().getX() / 2
				&& getPosition().getY() > s.getPosition().getY() - s.getSize().getX() / 2)
		{
			setVelocity(getVelocity().setX(-getVelocity().getX()));
		}
	}

	// Abstract methods
	// -----------------------------------------------------

	/**
	 * The copy method returns a deep copy of the sprite.
	 * @return copy of sprite
	 */
	public abstract Sprite copy();
	
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

	public Vector2 getSize()
	{
		return size;
	}

	public void setSize(Vector2 size)
	{
		this.size = size;
	}

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
