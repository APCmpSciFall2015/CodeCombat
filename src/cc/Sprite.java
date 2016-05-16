package cc;

import java.awt.Graphics;

import lib.Vector2;

public abstract class Sprite
{
	public static final float MAX_VELOCITY = 5f;
	
	private World world;
	private Vector2 position;
	private Vector2 velocity; 
	private Vector2 acceleration;
	
	// Constructors
	// --------------------------------------------------
	
	public Sprite(World world)
	{
		this.position = new Vector2(0, 0);
		this.velocity = new Vector2(0, 0);
		this.acceleration = new Vector2(0, 0);
		this.world = world; // shallow copy
	}
	
	public Sprite(float x, float y, World world)
	{
		this.position = new Vector2(x, y);
		this.velocity = new Vector2(0, 0);
		this.acceleration = new Vector2(0, 0);
		this.world = world; // shallow copy
	}
	
	public Sprite(Vector2 position, Vector2 velocity, Vector2 acceleration, World world)
	{
		this.position = position.copy();
		this.velocity = velocity.copy();
		this.acceleration = acceleration.copy();
		this.world = world; // shallow copy
	}
	
	// Abstract methods
	// -----------------------------------------------------
	
	public abstract void update();
	public abstract void paint(Graphics g);
	
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


}
