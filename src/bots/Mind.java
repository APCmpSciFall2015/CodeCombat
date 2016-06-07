package bots;

import app.Main;


import java.util.ArrayList;
import java.util.Random;

import lib.Vector2;
import world.Circle;
import world.CircleStats;
import world.Sprite;

/**
 * The Mind Class provides a basis for other Bot AIs to be built on.
 * @author Robert
 * @version 0.1
 */
public abstract class Mind
{
	
	/** The maximum number of characters a mind name can be*/
	public static final int MAX_NAME_CHARS = Integer.parseInt(Main.CONFIG.get("maxMindNameChars"));
	
	/** The random number used in calculations. */
	private static Random random = new Random();
	
	/** The circle the AI is running. */
	private Circle circle;
	
	/** The noise variance used when the AI is receiving data */
	private float variance;
	
	/** The noise mean used when the AI is receiving data*/
	private float mean;

	// Constructors
	// ------------------------------------------

	/**
	 * Instantiates a new mind using a copy constructor.
	 *
	 * @param m the mind to copy from
	 */
	public Mind(Mind m)
	{
		this.circle = (Circle) m.circle.copy();
		this.variance = m.variance;
		this.mean = m.mean;
	}

	/**
	 * Instantiates a new mind.
	 *
	 * @param c the circle to run the AI on
	 * @param variance the noise variance
	 * @param mean the noise mean
	 */
	public Mind(Circle c, float variance, float mean)
	{
		this.circle = c;
		this.variance = variance;
		this.mean = mean;
	}

	// Instance Methods
	// ----------------------------------------

	/**
	 * Calculates noise.
	 *
	 * @return the noise calculations
	 */
	public final Vector2 noise()
	{
		return new Vector2((float) random.nextGaussian() * variance + mean, (float) random.nextGaussian() * variance + mean);
	}

	/**
	 * Shoots a projectile out of the robot.
	 */
	public final void shoot()
	{
		if(circle.isAlive()) circle.shoot();
	}
	
	/**
	 * Request in view.
	 *
	 * @return all items in the Bot's FOV
	 */
	public final ArrayList<Sprite> requestInView()
	{
		ArrayList<Sprite> inView = circle.requestInView();
		for (Sprite s : inView)
		{
			s.getPosition().add(noise());
		}
		return inView;
	}

	/**
	 * Turns the circle.
	 *
	 * @param deltaTheta the angle to turn
	 */
	public final void turn(float deltaTheta)
	{
		if (circle.isAlive())
		{
			circle.turn(deltaTheta);
		}
	}

	// Abstract Methods
	// --------------------------------------------

	/**
	 * Copies the AI to use in a copy constructor.
	 *
	 * @return the copied AI
	 */
	public abstract Mind copy();

	/**
	 * Used for calculations in the AI.
	 */
	public abstract void think();

	// Getters and Setters
	// ---------------------------------------------

	/**
	 * Returns a noisy deep copy of the circle.
	 * @return Circle copy
	 */
	public final Circle getCircle()
	{
		Circle c = (Circle) c.copy();
		c.setPosition(c.getPosition().add(noise()));
		c.setVelocity(c.getVelocity().add(noise()));
		return c;
	}
	
	/**
	 * Gets the time for the bot to respawn.
	 *
	 * @return the time for the bot to respawn
	 */
	public final int getRespawnTimer()
	{
		return circle.getRespawnTimer();
	}
	
	/**
	 * Gets the time for the bot to reload.
	 *
	 * @return the time for the bot to reload
	 */
	public final int getShootTimer()
	{
		return circle.getShootTimer();
	}
	
	/**
	 * Checks if the bot is alive.
	 *
	 * @return true, if it is alive
	 */
	public final boolean isAlive()
	{
		return circle.isAlive();
	}

	/**
	 * Checks if the bot is shielded.
	 *
	 * @return true, if it is shielded
	 */
	public final boolean isShielded()
	{
		return circle.isShielded();
	}
	
	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public final Vector2 getPosition()
	{
		return circle.getPosition().add(noise());
	}

	/**
	 * Gets the eye position.
	 *
	 * @return the eye position
	 */
	public final Vector2 calcEyePosition(Vector2 position, Vector2 velocity)
	{
		return new Vector2((position.getX() + velocity.normalize().getX() * circle.getSize().getX() / 2),
				(position.getY() + velocity.normalize().getY() * circle.getSize().getY() / 2));
	}
	
	/**
	 * Gets the velocity.
	 *
	 * @return the velocity
	 */
	public final Vector2 getVelocity()
	{
		return circle.getVelocity().add(noise());
	}

	/**
	 * Gets the stats of the bot.
	 *
	 * @return the stats of the bot
	 */
	public final CircleStats getStats()
	{
		return new CircleStats(circle);
	}
}
