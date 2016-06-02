package world;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import app.Main;
import bots.Mind;
import lib.Vector2;

/**
 * The Circle class represents a battle bot sprite in its generic form.
 * @author Robert, Brian, Dalton, Derek
 * @version 0.1
 * @see Sprite
 */
public class Circle extends Sprite implements Comparable<Circle>
{
	/** Speed of Circles **/
	public static final float SPEED = Float.parseFloat(Main.CONFIG.get("circleSpeed"));
	/** Field of View of Circle **/
	public static final float FOV = Float.parseFloat(Main.CONFIG.get("circleFOV"));
	/** maximum rate of change in the direction of velocity **/
	public static final float MAX_TURNING_ANGLE = Float.parseFloat(Main.CONFIG.get("circleMaxTurningAngle"));
	/** time to respawn **/
	public static final int RESPAWN_TIME = Integer.parseInt(Main.CONFIG.get("circleRespawnTime"));
	/** time to between shots **/
	public static final int RELOAD_TIME = Integer.parseInt(Main.CONFIG.get("circleReloadTime"));
	/** radius of circle **/
	public static final int RADIUS = Integer.parseInt(Main.CONFIG.get("circleRadius"));

	// Circle stats
	// ------------------------------------------

	/** circle accuracy during existence **/
	private float totalAccuracy = 0;
	/** circle accuracy during life **/
	private float accuracy = 1;
	/** circle shots fired during existence **/
	private int totalShotsFired = 0;
	/** circle shots fired during life **/
	private int shotsFired = 0;
	/** circle hits during existence **/
	private int totalHits = 0;
	/** circle hits during life **/
	private int hits = 0;
	/** circle deaths **/
	private int deaths = 0;
	/** circle kills during existence **/
	private int totalKills = 0;
	/** circle kills during life **/
	private int kills = 0;
	/** circle ticks alive **/
	private int ticksAlive = 0;
	/** shields acquired during existence **/
	private int totalShieldsAcquired = 0;
	/** shields acquired during life **/
	private int shieldsAcquired = 0;
	/** obstacle collisions during existence **/
	private int totalObstacleCollisions = 0;
	/** obstacle collisions during life **/
	private int obstacleCollisions = 0;
	/** wall collisions during existence **/
	private int totalWallCollisions = 0;
	/** wall collisions during life **/
	private int wallCollisions;

	/** timer for shooting **/
	private int shootTimer = RELOAD_TIME;
	/** timer for respawns **/
	private int respawnTimer = RESPAWN_TIME;
	/** location of Circle's eyes **/
	private Vector2 eyePosition;
	/** Mind to control circle bot **/
	private Mind mind = null;

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
		this.mind = c.mind;
		this.eyePosition = c.getEyePosition();
	}

	public Circle(World world)
	{
		super(new Vector2(RADIUS * 2, RADIUS * 2),
				new Vector2((float) ((Math.random() * (world.getSize().getX()) - (2 * RADIUS))) + RADIUS,
						(float) (((Math.random() * (world.getSize().getY()) - (2 * RADIUS))) + RADIUS)),
				new Vector2(SPEED, (float) (Math.random() * Math.PI * 2), true), new Vector2(0, 0),
				new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)),
				world);
		this.eyePosition = new Vector2((getPosition().getX() + getVelocity().normalize().getX() * getSize().getX() / 2),
				(getPosition().getY() + getVelocity().normalize().getY() * getSize().getY() / 2));
	}

	public Circle(Vector2 position, Vector2 direction, Color color, World world)
	{
		super(new Vector2(RADIUS / 2, RADIUS / 2), position, direction.normalize().mult(SPEED), new Vector2(0, 0),
				color, world);
		this.eyePosition = new Vector2((getPosition().getX() + getVelocity().normalize().getX() * getSize().getX() / 2),
				(getPosition().getY() + getVelocity().normalize().getY() * getSize().getY() / 2));
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
	
			// paint FOV visualizer
			Vector2 left = new Vector2(getWorld().getSize().mag(), getVelocity().angle() + FOV / 2, true);
			Vector2 right = new Vector2(getWorld().getSize().mag(), getVelocity().angle() - FOV / 2, true);
			g.drawLine((int) eyePosition.getX(), (int) eyePosition.getY(),
					(int) (eyePosition.getX() + left.getX()),
					(int) (eyePosition.getY() + left.getY()));
			g.drawLine((int) eyePosition.getX(), (int) eyePosition.getY(),
					(int) (eyePosition.getX() + right.getX()),
					(int) (eyePosition.getY() + right.getY()));
			
			// paint direction visualizer (color declared is inverted)
			g.setColor(new Color(255 - getColor().getRed(), 255 - getColor().getGreen(), 255 - getColor().getBlue()));
			g.fillOval(
					(int) eyePosition.getX() - RADIUS / 4,
					(int) eyePosition.getY() - RADIUS / 4,
					(int) (getSize().getX() / 4), (int) (getSize().getY() / 4));
			// @formatter:on
			super.paint(g);
		}
	}

	@Override
	public void update()
	{
		if(isAlive())
		{
			slideWalls();
			setPosition(getPosition().add(getVelocity()));
			if(mind == null) 
			{
				turn ((float) -Math.PI / 500); shoot();
			}
		}else

	{
		respawn();
	}

	if(mind!=null)mind.think();

	calcStats();

	updateCounters();

	}

	@Override
	public final void collide(Sprite s)
	{
		if(s == null) return;
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
		else if (!(s instanceof Projectile || s instanceof Shield))
		{
			slide(s);
		}
	}

	@Override
	public Sprite copy()
	{
		return new Circle(this);
	}



	@Override
	public int compareTo(Circle c)
	{
		// @formatter:off
		if (c.kills > kills) return 1; else if (c.kills < kills) return -1;
		if (c.deaths < deaths) return 1; else if (c.deaths > deaths) return -1;
		if (c.accuracy > accuracy) return 1; else if (c.accuracy < accuracy) return -1;
		return 0;
		// @formatter:on
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
			accuracy = 1;
			shotsFired = 0;
			hits = 0;
			totalKills = 0;
			kills = 0;
			ticksAlive = 0;
			shieldsAcquired = 0;
			obstacleCollisions = 0;
			wallCollisions = 0;
			getWorld().respawn(this);
			respawnTimer = RESPAWN_TIME;
		}
	}

	private void calcStats()
	{
		if (shotsFired > 0)
			accuracy = (float) hits / shotsFired;
		if (totalShotsFired > 0)
			totalAccuracy = (float) totalHits / totalShotsFired;
	}

	private final void updateCounters()
	{
		// @formatter:off
		if (shootTimer > 0) shootTimer--;
		if (respawnTimer > 0) respawnTimer--;
		if (isAlive()) ticksAlive++;
		// @formatter:on
	}

	public final ArrayList<Sprite> requestInView()
	{
		// @formatter:off
		ArrayList<Sprite> inView = new ArrayList<Sprite>();
		if(isAlive())
		{
			// get sprites in FOV from location of eyes
			inView = getWorld().requestInView(
						new Vector2(
							getPosition().getX() + getVelocity().normalize().getX() * getSize().getX() / 2,
							getPosition().getY() + getVelocity().normalize().getY() * getSize().getY() / 2),
						getVelocity(), FOV);
			// remove self
			for (int i = inView.size() - 1; i > 0; i--)
			{
				if(inView.get(i).getId() == getId()) inView.remove(i); 
			}			
		}
		return inView;
		// @formatter:on
	}

	public final void turn(float deltaTheta)
	{
		// do nothing if input not a number
		if(Float.isNaN(deltaTheta)) return;
		// truncate to interval [-MAX_TURNING_ANGLE, MAX_TURNING_ANGLE]
		if (deltaTheta < -MAX_TURNING_ANGLE)
			deltaTheta = -MAX_TURNING_ANGLE;
		else if (deltaTheta > MAX_TURNING_ANGLE)
			deltaTheta = MAX_TURNING_ANGLE;

		// adjust angle of velocity vector (aka turn)
		setVelocity(new Vector2(SPEED, getVelocity().angle() + deltaTheta, true));
	}

	/**
	 * The shoot method generates a projectile in an attempt to destroy other
	 * circles.
	 */
	public final void shoot()
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
			totalShotsFired++;
		}
	}

	// Getters and Setters
	// -------------------------------------------------------

	@Override
	public void setPosition(Vector2 position)
	{
		super.setPosition(position);
		this.eyePosition = new Vector2((position.getX() + getVelocity().normalize().getX() * getSize().getX() / 2),
				(position.getY() + getVelocity().normalize().getY() * getSize().getY() / 2));
	}

	public float getTotalAccuracy()
	{
		return totalAccuracy;
	}

	public void setTotalAccuracy(float totalAccuracy)
	{
		this.totalAccuracy = totalAccuracy;
	}

	public float getAccuracy()
	{
		return accuracy;
	}

	public void setAccuracy(float accuracy)
	{
		this.accuracy = accuracy;
	}

	public int getTotalShotsFired()
	{
		return totalShotsFired;
	}

	public void setTotalShotsFired(int totalShotsFired)
	{
		this.totalShotsFired = totalShotsFired;
	}

	public int getShotsFired()
	{
		return shotsFired;
	}

	public void setShotsFired(int shotsFired)
	{
		this.shotsFired = shotsFired;
	}

	public int getTotalHits()
	{
		return totalHits;
	}

	public void setTotalHits(int totalHits)
	{
		this.totalHits = totalHits;
	}

	public int getHits()
	{
		return hits;
	}

	public void setHits(int hits)
	{
		this.hits = hits;
	}

	public int getDeaths()
	{
		return deaths;
	}

	public void setDeaths(int deaths)
	{
		this.deaths = deaths;
	}

	public int getTotalKills()
	{
		return totalKills;
	}

	public void setTotalKills(int totalKills)
	{
		this.totalKills = totalKills;
	}

	public int getKills()
	{
		return kills;
	}

	public void setKills(int kills)
	{
		this.kills = kills;
	}

	public int getTicksAlive()
	{
		return ticksAlive;
	}

	public void setTicksAlive(int ticksAlive)
	{
		this.ticksAlive = ticksAlive;
	}

	public int getTotalShieldsAcquired()
	{
		return totalShieldsAcquired;
	}

	public void setTotalShieldsAcquired(int totalShieldsAcquired)
	{
		this.totalShieldsAcquired = totalShieldsAcquired;
	}

	public int getShieldsAcquired()
	{
		return shieldsAcquired;
	}

	public void setShieldsAcquired(int shieldsAcquired)
	{
		this.shieldsAcquired = shieldsAcquired;
	}

	public int getTotalObstacleCollisions()
	{
		return totalObstacleCollisions;
	}

	public void setTotalObstacleCollisions(int totalObstacleCollisions)
	{
		this.totalObstacleCollisions = totalObstacleCollisions;
	}

	public int getObstacleCollisions()
	{
		return obstacleCollisions;
	}

	public void setObstacleCollisions(int obstaclesCollisions)
	{
		this.obstacleCollisions = obstaclesCollisions;
	}

	public int getTotalWallCollisions()
	{
		return totalWallCollisions;
	}

	public void setTotalWallCollisions(int totalWallCollisions)
	{
		this.totalWallCollisions = totalWallCollisions;
	}

	public int getWallCollisions()
	{
		return wallCollisions;
	}

	public void setWallCollisions(int wallCollisions)
	{
		this.wallCollisions = wallCollisions;
	}

	public int getShootTimer()
	{
		return shootTimer;
	}

	public void setShootTimer(int shootTimer)
	{
		this.shootTimer = shootTimer;
	}

	public int getRespawnTimer()
	{
		return respawnTimer;
	}

	public void setRespawnTimer(int respawnTimer)
	{
		this.respawnTimer = respawnTimer;
	}

	public Mind getMind()
	{
		// intentional shallow copy minds copy circles
		return mind;
	}

	public void setMind(Mind mind)
	{
		// intentional shallow copy minds copy circles
		this.mind = mind;
	}

	public Vector2 getEyePosition()
	{
		return eyePosition.copy();
	}

}
