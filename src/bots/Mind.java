package bots;

import java.util.ArrayList;
import java.util.Random;

import cc.Circle;
import cc.Sprite;
import lib.Vector2;

public abstract class Mind
{
	private Circle circle;
	private Random random;

	// Constructors
	// ------------------------------------------

	public Mind(Mind m)
	{
		this.circle = (Circle) m.circle.copy();
		this.random = m.random;
	}

	public Mind(Circle c, Random r)
	{
		this.circle = c;
		this.random = r;
	}

	// Instance Methods
	// ----------------------------------------

	public final Vector2 noise()
	{
		//return new Vector2(0, 0);
		return new Vector2((float) random.nextGaussian(), (float) random.nextGaussian());
	}

	public final void shoot()
	{
		circle.shoot();
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
		circle.turn(deltaTheta);
	}

	// Abstract Methods
	// --------------------------------------------

	public abstract Mind copy();

	public abstract void think();

	// Getters and Setters
	// ---------------------------------------------

	public Vector2 getPosition()
	{
		return circle.getPosition().add(noise());
	}

	public Vector2 getVelocity()
	{
		return circle.getVelocity().add(noise());
	}
}
