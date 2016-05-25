package cc;

import java.awt.Color;
import java.awt.Graphics;

import cc.Main.GameState;
import lib.Vector2;

/**
 * The Circle class represents a battle bot sprite in its generic form.
 * @author Robert, Brian, Dalton, Derek
 * @version 0.1
 * @see Sprite
 */
public class Circle extends Sprite implements Comparable<Circle>
{
	/** maximum rate of change in the direction of velocity **/
	public static final float MAX_TURNING_ANGLE = (float) Math.PI / 60f;
	/** time to respawn **/
	public static final int RESPAWN_TIME = 5 * 60;
	/** time to between shots **/
	public static final int RELOAD_TIME = 1 * 60;

	/** circle accuracy **/
	private float accuracy = 1;
	/** circle shots fired **/
	private int shotsFired = 0;
	/** circle deaths **/
	private int deaths = 0;
	/** circle kills **/
	private int kills = 0;
	/** timer for shooting **/
	private int shootTimer = RELOAD_TIME;
	/** timer for respawns **/
	private int respawnTimer = RESPAWN_TIME;
	

	// Constructors
	// --------------------------------------------------------------------

	/**
	 * Circle copy constructor
	 * @param x x pos
	 * @param y y pos
	 * @param world plane of existence
	 */
	public Circle(Circle c)
	{
		super(c);
		this.shootTimer = c.shootTimer;
		this.respawnTimer = c.respawnTimer;
	}

	/**
	 * 3-Argument Circle constructor
	 * @param x x pos
	 * @param y y pos
	 * @param world plane of existence
	 */
	public Circle(float x, float y, World world)
	{
		// @formatter:off
		super(
				new Vector2(40, 40), // size
				new Vector2(x, y), new Vector2(1, 0, true), new Vector2(0, 0), // pos, vel, acc
				new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)), // random color
				world);
		// @formatter:on
	}

	/**
	 * 4-Argument Circle constructor
	 * @param x x pos
	 * @param y y pos
	 * @param color color
	 * @param world plane of existence
	 */
	public Circle(float x, float y, Color color, World world)
	{
		super(new Vector2(40, 40), new Vector2(x, y), new Vector2(1, 0, true), new Vector2(0, 0), color, world);
	}

	// Overridden methods
	// -----------------------------------------------------------------------

	@Override
	public final void paint(Graphics g)
	{
		if (isAlive())
		{
			// @formatter:off
			g.setColor(getColor());
			// paint circle
			g.fillOval(
					(int) (getPosition().getX() - getSize().getX() / 2),
					(int) (getPosition().getY() - getSize().getX() / 2),
					(int) getSize().getX(), (int) getSize().getY());
	
			// paint direction visualizer (color declared is inverted)
			g.setColor(new Color(255 - getColor().getRed(), 255 - getColor().getGreen(), 255 - getColor().getBlue()));
			g.fillOval(
					(int) (getPosition().getX() + getVelocity().normalize().getX() * getSize().getX() / 2) - 5,
					(int) (getPosition().getY() + getVelocity().normalize().getY() * getSize().getY() / 2) - 5,
					(int) (getSize().getX() / 4), (int) (getSize().getY() / 4));
			// @formatter:on
	
				if (Main.DEBUG)
					super.paint(g);
		}
	}

	@Override
	public void update()
	{
		if (isAlive())
		{
			super.update();
			// update position
			setPosition(getPosition().add(getVelocity()));

			// test change in velocity and shooting
			setVelocity(new Vector2(2f, (float) (getVelocity().angle() + Math.PI / 500), true));
			shoot();
		}
		else
		{
			respawn();
		}
		calcStats();
		updateCounters();
	}

	@Override
	public final void collide(Sprite s)
	{
		// die when hit by projectile (not own projectile)
		if (s instanceof Projectile && !((Projectile) s).isOwner(this))
		{
			kill();
		}
		// slide on all other objects
		else if (s instanceof Circle)
		{
			if (((Circle) s).isAlive())
				slide(s);
		}
		else if (!(s instanceof Projectile))
			slide(s);
	}

	@Override
	public Sprite copy()
	{
		return new Circle(this);
	}

	@Override
	public String toString()
	{
		return super.toString() + " [" + kills + ", " + deaths + ", " + String.format("%.2f", accuracy) + "]";
	}
	
	@Override
	public int compareTo(Circle c)
	{
		return kills - c.kills - deaths + c.deaths;
	}
	
	// instance methods
	// ----------------------------------------------------------------------

	public void kill()
	{
		respawnTimer = RESPAWN_TIME;
		deaths++;
		setAlive(false);
	}
	
	private final void respawn()
	{
		if (respawnTimer == 0)
		{
			getWorld().spawn(this);
			respawnTimer = RESPAWN_TIME;
		}
	}
	
	private void calcStats()
	{
		accuracy = (float) kills / shotsFired;
	}

	private final void updateCounters()
	{
		if (shootTimer > 0)
			shootTimer--;
		if (respawnTimer > 0)
			respawnTimer--;
	}

	/**
	 * The shoot method generates a projectile in an attempt to destroy other
	 * circles.
	 */
	protected final void shoot()
	{
		if (shootTimer == 0)
		{
			// generate a new projectile in front of the circle traveling faster
			// in the same direction
			getWorld().generateObject(new Projectile(
					(int) (getPosition().getX() + getVelocity().normalize().getX() * getSize().getX() / 2),
					(int) (getPosition().getY() + getVelocity().normalize().getY() * getSize().getY() / 2),
					getVelocity(), this, getWorld()));
			shootTimer = RELOAD_TIME;
			shotsFired++;
		}
	}

	// Getters and Setters
	// -------------------------------------------------------

	public int getDeaths()
	{
		return deaths;
	}

	public void setDeaths(int deaths)
	{
		this.deaths = deaths;
	}

	public int getKills()
	{
		return kills;
	}

	public void setKills(int kills)
	{
		this.kills = kills;
	}

	public float getAccuracy()
	{
		return accuracy;
	}

	public void setAccuracy(float accuracy)
	{
		this.accuracy = accuracy;
	}

	public int getShotsFired()
	{
		return shotsFired;
	}

	public void setShotsFired(int shotsFired)
	{
		this.shotsFired = shotsFired;
	}
}
