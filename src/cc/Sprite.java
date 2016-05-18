package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

public abstract class Sprite
{
	public static final float MAX_VELOCITY = 5f;
	
	private World world;
	private int width;
	private int height;
	private Color color;
	private Vector2 position;
	private Vector2 velocity; 
	private Vector2 acceleration;
	
	// Constructors
	// --------------------------------------------------
	
	public Sprite(World world)
	{
		this.width = 0;
		this.height = 0;
		this.position = new Vector2(0, 0);
		this.velocity = new Vector2(0, 0);
		this.acceleration = new Vector2(0, 0);
		this.world = world; // shallow copy
	}
	
	public Sprite(float x, float y, World world)
	{
		this.width = 0;
		this.height = 0;
		this.position = new Vector2(x, y);
		this.velocity = new Vector2(0, 0);
		this.acceleration = new Vector2(0, 0);
		this.world = world;
	}
	
	public Sprite(Vector2 position, Vector2 velocity, Vector2 acceleration, World world)
	{
		this.width = 0;
		this.height = 0;
		this.position = position.copy();
		this.velocity = velocity.copy();
		this.acceleration = acceleration.copy();
		this.color = Color.BLACK;
		this.world = world;
	}
	
	public Sprite(int width, int height, Vector2 position, Color color, World world)
	{
		this.width = width;
		this.height = height;
		this.position = position.copy();
		this.velocity = new Vector2(0, 0);
		this.acceleration = new Vector2(0, 0);
		this.color = color;
		this.world = world;
	}
	
	public Sprite(int width, int height, Vector2 position, Vector2 velocity, Color color, World world)
	{
		this.width = width;
		this.height = height;
		this.position = position.copy();
		this.velocity = velocity.copy();
		this.acceleration = new Vector2(0, 0);
		this.color = color;
		this.world = world;
	}
	
	public Sprite(int width, int height, Vector2 position, Vector2 velocity, Vector2 acceleration, Color color, World world)
	{
		this.width = width;
		this.height = height;
		this.position = position.copy();
		this.velocity = velocity.copy();
		this.acceleration = acceleration.copy();
		this.color = color;
		this.world = world;
	}
	
	// Abstract methods
	// -----------------------------------------------------
	
	public abstract void update();
	public abstract void paint(Graphics g);
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
	
	public  Vector2 getVelocity()
	{
		return velocity.copy();
	}

	public  void setVelocity(Vector2 velocity)
	{
		this.velocity = velocity.copy();
	}

	public  Vector2 getAcceleration()
	{
		return acceleration.copy();
	}

	public  void setAcceleration(Vector2 acceleration)
	{
		this.acceleration = acceleration.copy();
	}

	public  int getWidth()
	{
		return width;
	}

	public  void setWidth(int width)
	{
		this.width = width;
	}

	public  int getHeight()
	{
		return height;
	}

	public  void setHeight(int height)
	{
		this.height = height;
	}

	public  Color getColor()
	{
		return color;
	}

	public  void setColor(Color color)
	{
		this.color = color;
	}

	protected  World getWorld()
	{
		return world;
	}

	protected  void setWorld(World world)
	{
		this.world = world;
	}
}
