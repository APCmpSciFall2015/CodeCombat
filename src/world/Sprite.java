package world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import app.Main;
import lib.Vector2;

/**
 * The sprite class represents an entity of a given World.
 * @author Robert, Dalton, Brian, Derek
 * @version 0.1
 * @see World
 */
public abstract class Sprite
{
	
	/**  maximum velocity of sprites *. */
	public static final float MAX_VELOCITY = Float.parseFloat(Main.CONFIG.get("spriteMaxVelocity"));
	
	/**  running id count of sprites *. */
	protected static int idCount = 0;

	// instance variables
	// -------------------------------------------

	/**  id of sprite *. */
	private int id;
	
	/**  whether or not the sprite exists. */
	private boolean existence;
	
	/**  whether or not the Circle is alive. */
	private boolean alive;
	
	/**  plane of existence for the sprite. */
	private World world;
	
	/**  color of sprite. */
	private Color color;
	
	/**  size of sprite. */
	private Vector2 size;
	
	/**  location of sprite. */
	private Vector2 position;
	
	/**  rate of change in location of sprite. */
	private Vector2 velocity;
	
	/**  rate of change in rate of change of location of sprite. */
	private Vector2 acceleration;

	// Constructors
	// --------------------------------------------------

	/**
	 * Sprite Copy Constructor.
	 *
	 * @param s Sprite to copy
	 */
	protected Sprite(Sprite s)
	{
		this.id = s.id;
		this.size = s.size.copy();
		this.position = s.position.copy();
		this.velocity = s.velocity.copy();
		this.acceleration = s.acceleration.copy();
		this.color = s.getColor();
		this.world = s.getWorld();
		this.existence = s.existence;
		this.alive = s.alive;
		this.existence = s.existence;
	}

	/**
	 * 6-Argument Sprite Constructor.
	 *
	 * @param size the size
	 * @param position position vector
	 * @param velocity rate of change in position vector
	 * @param acceleration rate of change in rate of change of position / rate
	 *        of change in velocity / acceleration vector
	 * @param color color
	 * @param world plane of existence
	 */
	public Sprite(Vector2 size, Vector2 position, Vector2 velocity, Vector2 acceleration, Color color, World world)
	{
		this.id = idCount++;
		this.size = size.copy();
		this.position = position.copy();
		this.velocity = velocity.copy();
		this.acceleration = acceleration.copy();
		this.color = color;
		this.world = world;
		this.existence = true;
		this.alive = true;
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
		if(s == null) return;
		int direction = 0;

		// @formatter:off
		// calculate the distances required to move this sprite outside of
		// the other sprite s
		float[] dists = new float[] {
				s.getPosition().getX() + s.getSize().getX() / 2 - (getPosition().getX() - getSize().getX() / 2), // right side sprite
				getPosition().getX() + getSize().getX() / 2 - (s.getPosition().getX() - s.getSize().getX() / 2), // left side sprite
				s.getPosition().getY() + s.getSize().getY() / 2 - (getPosition().getY() - getSize().getY() / 2), // top side sprite
				getPosition().getY() + getSize().getY() / 2 - (s.getPosition().getY() - s.getSize().getY() / 2)  // bottom side sprite
				};
		// @formatter:on

		// find the smallest distance
		float minValue = dists[0];
		for (int i = 1; i < dists.length; i++)
		{
			if (dists[i] < minValue)
			{
				direction = i;
				minValue = dists[i];
			}
		}

		// Set position outside of colliding sprite on the proper side
		switch (direction)
		{
		// right side sprite
		case 0:
			setPosition(getPosition().setX(s.getPosition().getX() + s.getSize().getX() / 2 + getSize().getX() / 2));
			break;
		// left other sprite
		case 1:
			setPosition(getPosition().setX(s.getPosition().getX() - s.getSize().getX() / 2 - getSize().getX() / 2));
			break;
		// top other sprite
		case 2:
			setPosition(getPosition().setY(s.getPosition().getY() + s.getSize().getY() / 2 + getSize().getY() / 2));
			break;
		// bottom other sprite
		case 3:
			setPosition(getPosition().setY(s.getPosition().getY() - s.getSize().getY() / 2 - getSize().getY() / 2));
			break;
		}

		// if (Main.DEBUG)
		// {
		// System.out.println("collision");
		// System.out.println(direction);
		// System.out.println(
		// s.getPosition().getX() + s.getSize().getX() / 2 -
		// (getPosition().getX() - getSize().getX() / 2));
		// System.out.println(
		// getPosition().getX() + getSize().getX() / 2 - (s.getPosition().getX()
		// - s.getSize().getX() / 2));
		// System.out.println(
		// s.getPosition().getY() + s.getSize().getY() / 2 -
		// (getPosition().getY() - getSize().getY() / 2));
		// System.out.println(
		// getPosition().getY() + getSize().getY() / 2 - (s.getPosition().getY()
		// - s.getSize().getY() / 2));
		// }

	}

	/**
	 * The exchange method sets the velocity of this sprite to the velocity of a
	 * colliding sprite.
	 * @param s colliding sprite
	 */
	public void exchange(Sprite s)
	{
		if(s == null) return;
		// this changes the velocity of this sprite... how will the other sprite
		// get my velocity?
		setVelocity(s.getVelocity());
	}

	/**
	 * The bounce method rebounds the velocity of this sprite in the x and/or y
	 * direction from an obstacle.
	 * @param s colliding sprite
	 */
	public void bounce(Sprite s)
	{
		if(s == null) return;
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

	/**
	 * The paint method draws the sprite.
	 *
	 * @param g Graphics to paint on
	 */
	public void paint(Graphics g)
	{
		if (Main.debug)
		{
			// draw AABB
			g.setColor(new Color(255 - getColor().getRed(), 255 - getColor().getGreen(), 255 - getColor().getBlue()));
			g.drawRect((int) (getPosition().getX() - getSize().getX() / 2),
					(int) (getPosition().getY() - getSize().getY() / 2), (int) getSize().getX(),
					(int) getSize().getY());

			// draw text data
			Font font = new Font("Serif", Font.PLAIN, 12);
			g.setColor(this.color);
			g.setFont(font);
			g.drawString("" + this, (int) (getPosition().getX() - getSize().getX() / 1.5),
					(int) (getPosition().getY() - getSize().getY() / 1.5));
		}
	}

	/**
	 * Slides a sprite down a wall.
	 *
	 * @return true, if the sprite slid
	 */
	public boolean slideWalls()
	{
		boolean slid = false;
		// slide on left and right walls
		if (getPosition().getX() - getSize().getX() / 2 < 0)
		{
			setPosition(getPosition().setX(getSize().getX() / 2));
			slid = true;
		}
		else if (getPosition().getX() + getSize().getX() / 2 > getWorld().getSize().getX())
		{
			setPosition(getPosition().setX(getWorld().getSize().getX() - getSize().getX() / 2));
			slid = true;
		}
		
		// slide on top and bottom walls
		if (getPosition().getY() - getSize().getX() / 2 < 0)
		{
			setPosition(getPosition().setY(getSize().getX() / 2));
			slid = true;
		}
		else if (getPosition().getY() + getSize().getX() / 2 > getWorld().getSize().getY())
		{
			setPosition(getPosition().setY(getWorld().getSize().getY() - getSize().getY() / 2));
			slid = true;
		}
		return slid;
	}

	// Abstract methods
	// -----------------------------------------------------

	/**
	 * The copy method returns a deep copy of the sprite.
	 * @return copy of sprite
	 */
	public abstract Sprite copy();

	
	/**
	 * The update method updates the condition of the sprite.
	 */
	public abstract void update();
	
	/**
	 * The collide method defines collision behavior for the sprite upon
	 * collision with another sprite.
	 * @param s colliding sprite
	 */
	public abstract void collide(Sprite s);

	// Overridden methods
	// ----------------------------------------------------

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return (this.getClass() + ": " + id).substring(12);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o)
	{
		return o instanceof Sprite && this.id == ((Sprite) o).id;
	}

	// Getters and setters
	// -----------------------------------------------------

	/**
	 * Gets the size of the sprite.
	 *
	 * @return the size
	 */
	public Vector2 getSize()
	{
		return size;
	}

	/**
	 * Sets the size of the sprite.
	 *
	 * @param size the new size
	 */
	public void setSize(Vector2 size)
	{
		this.size = size;
	}

	/**
	 * Gets the position of the sprite.
	 *
	 * @return the position
	 */
	public Vector2 getPosition()
	{
		return position.copy();
	}

	/**
	 * Sets the position of the sprite.
	 *
	 * @param position the new position
	 */
	public void setPosition(Vector2 position)
	{
		this.position = position.copy();
	}

	/**
	 * Gets the velocity of the sprite.
	 *
	 * @return the velocity
	 */
	public Vector2 getVelocity()
	{
		return velocity.copy();
	}

	/**
	 * Sets the velocity of the sprite.
	 *
	 * @param velocity the new velocity
	 */
	public void setVelocity(Vector2 velocity)
	{
		this.velocity = velocity.copy();
	}

	/**
	 * Gets the acceleration of the sprite.
	 *
	 * @return the acceleration
	 */
	public Vector2 getAcceleration()
	{
		return acceleration.copy();
	}

	/**
	 * Sets the acceleration of the sprite.
	 *
	 * @param acceleration the new acceleration
	 */
	public void setAcceleration(Vector2 acceleration)
	{
		this.acceleration = acceleration.copy();
	}

	/**
	 * Gets the color of the sprite.
	 *
	 * @return the color
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Sets the color of the sprite.
	 *
	 * @param color the new color
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Gets the world the sprite is in.
	 *
	 * @return the world
	 */
	protected World getWorld()
	{
		return world;
	}

	/**
	 * Sets the world the sprite is in.
	 *
	 * @param world the new world
	 */
	protected void setWorld(World world)
	{
		this.world = world;
	}

	/**
	 * Gets whether the sprite exists in the world.
	 *
	 * @return true, if it exists
	 */
	public boolean getExistence()
	{
		return existence;
	}

	/**
	 * Sets whether the sprite exists in the world.
	 *
	 * @param existence true, if it exists
	 */
	public void setExistence(boolean existence)
	{
		this.existence = existence;
	}

	/**
	 * Gets the id of the sprite.
	 *
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Sets the id of the sprite.
	 *
	 * @param id the new id
	 */
	protected void setId(int id)
	{
		this.id = id;
	}

	/**
	 * Checks if the sprite is alive.
	 *
	 * @return true, if it is alive
	 */
	public boolean isAlive()
	{
		return alive;
	}

	/**
	 * Sets if the sprite is alive.
	 *
	 * @param alive true, if it is alive
	 */
	public void setAlive(boolean alive)
	{
		this.alive = alive;
	}
}
