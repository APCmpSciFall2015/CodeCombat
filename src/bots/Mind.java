package bots;

import app.Main;


import java.util.ArrayList;
import java.util.Random;

import lib.Vector2;
import world.Circle;
import world.CircleStats;
import world.Sprite;

public abstract class Mind
{
	public static final int MAX_NAME_CHARS = Integer.parseInt(Main.CONFIG.get("maxMindNameChars"));
	private static Random random = new Random();
	
	private Circle circle;
	private float variance;
	private float mean;

	// Constructors
	// ------------------------------------------

	public Mind(Mind m)
	{
		this.circle = (Circle) m.circle.copy();
		this.variance = m.variance;
		this.mean = m.mean;
	}

	public Mind(Circle c, float variance, float mean)
	{
		this.circle = c;
		this.variance = variance;
		this.mean = mean;
	}

	// Instance Methods
	// ----------------------------------------

	public final Vector2 noise()
	{
		return new Vector2((float) random.nextGaussian() * variance + mean, (float) random.nextGaussian() * variance + mean);
	}

	public final void shoot()
	{
		if(circle.isAlive()) circle.shoot();
	}

	public final ArrayList<Sprite> requestInView()
	{
		ArrayList<Sprite> inView = circle.requestInView();
		for (Sprite s : inView)
		{
			s.getPosition().add(noise());
		}
		return inView;
	}

	public final void turn(float deltaTheta)
	{
		if (circle.isAlive())
		{
			circle.turn(deltaTheta);
		}
	}

	// Abstract Methods
	// --------------------------------------------

	public abstract Mind copy();

	public abstract void think();

	// Getters and Setters
	// ---------------------------------------------

	public final int getRespawnTimer()
	{
		return circle.getRespawnTimer();
	}
	
	public final int getShootTimer()
	{
		return circle.getShootTimer();
	}
	
	public final boolean isAlive()
	{
		return circle.isAlive();
	}

	public final boolean isShielded()
	{
		return circle.isShielded();
	}
	
	public final Vector2 getPosition()
	{
		return circle.getPosition().add(noise());
	}

	public final Vector2 getEyePosition()
	{
		Vector2 position = getPosition();
		return new Vector2((position.getX() + getVelocity().normalize().getX() * circle.getSize().getX() / 2),
				(position.getY() + getVelocity().normalize().getY() * circle.getSize().getY() / 2));
	}
	
	public final Vector2 getVelocity()
	{
		return circle.getVelocity().add(noise());
	}

	public final CircleStats getStats()
	{
		return new CircleStats(circle);
	}
}
