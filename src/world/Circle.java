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
	
	/**  Speed of Circles */
	public static final float SPEED = Float.parseFloat(Main.CONFIG.get("circleSpeed"));
	
	/**  Field of View angle of Circle */
	public static final float FOV = Float.parseFloat(Main.CONFIG.get("circleFOV"));
	
	/**  maximum rate of change in the direction of velocity */
	public static final float MAX_TURNING_ANGLE = Float.parseFloat(Main.CONFIG.get("circleMaxTurningAngle"));
	
	/**  time to respawn */
	public static final int RESPAWN_TIME = Integer.parseInt(Main.CONFIG.get("circleRespawnTime"));
	
	/**  time to between shots */
	public static final int RELOAD_TIME = Integer.parseInt(Main.CONFIG.get("circleReloadTime"));
	
	/**  radius of circle */
	public static final int RADIUS = Integer.parseInt(Main.CONFIG.get("circleRadius"));

	// Circle stats
	// ------------------------------------------

	/**  circle accuracy during existence */
	private float totalAccuracy = 0;
	
	/**  circle accuracy during life */
	private float accuracy = 1;
	
	/**  circle shots fired during existence */
	private int totalShotsFired = 0;
	
	/**  circle shots fired during life */
	private int shotsFired = 0;
	
	/**  circle hits during existence */
	private int totalHits = 0;
	
	/**  circle hits during life */
	private int hits = 0;
	
	/**  circle deaths */
	private int deaths = 0;
	
	/**  circle kills during existence */
	private int totalKills = 0;
	
	/**  circle kills during life */
	private int kills = 0;
	
	/**  circle ticks alive */
	private int ticksAlive = 0;
	
	/**  shields acquired during existence */
	private int totalShieldsAcquired = 0;
	
	/**  shields acquired during life */
	private int shieldsAcquired = 0;
	
	/**  obstacle collisions during existence */
	private int totalObstacleCollisions = 0;
	
	/**  obstacle collisions during life */
	private int obstacleCollisions = 0;
	
	/**  wall collisions during existence */
	private int totalWallCollisions = 0;
	
	/**  wall collisions during life */
	private int wallCollisions;
	
	/**  mine collisions during life (possible if have shield) */
	private int mineCollisions = 0;
	
	/**  mine collisions during existence */
	private int totalMineCollisions = 0;
	
	/**  projectile collisions during life (possible if have shield) */
	private int projectileCollisions = 0;
	
	/**  projectile collisions during existence */
	private int totalProjectileCollisions = 0;

	/**  timer for shooting */
	private int shootTimer = RELOAD_TIME;
	
	/**  timer for respawns */
	private int respawnTimer = RESPAWN_TIME;
	
	/**  location of Circle's eyes */
	private Vector2 eyePosition;
	
	/**  has a shield */
	private boolean shielded = false;
	
	/**  Mind to control circle bot */
	private Mind mind = null;

	// Constructors
	// --------------------------------------------------------------------

	/**
	 * Circle copy constructor.
	 *
	 * @param c the c
	 */
	public Circle(Circle c)
	{
		super(c);
		this.totalAccuracy = c.totalAccuracy;
		this.accuracy = c.accuracy;
		this.totalShotsFired = c.totalShotsFired;
		this.shotsFired = c.shotsFired;
		this.totalHits = c.totalHits;
		this.hits = c.hits;
		this.deaths = c.deaths;
		this.totalKills = c.totalKills;
		this.kills = c.kills;
		this.ticksAlive = c.ticksAlive;
		this.totalShieldsAcquired = c.totalShieldsAcquired;
		this.shieldsAcquired = c.shieldsAcquired;
		this.totalObstacleCollisions = c.totalObstacleCollisions;
		this.obstacleCollisions = c.obstacleCollisions;
		this.totalWallCollisions = c.totalWallCollisions;
		this.wallCollisions = c.wallCollisions;
		this.totalMineCollisions = c.totalMineCollisions;
		this.mineCollisions = c.mineCollisions;
		this.totalProjectileCollisions = c.totalProjectileCollisions;
		this.projectileCollisions = c.projectileCollisions;
		this.shootTimer = c.shootTimer;
		this.respawnTimer = c.respawnTimer;
		this.shielded = c.shielded;
		this.eyePosition = c.getEyePosition();
		// mind not copied
	}

	/**
	 * 1-Argument Circle Constructor. Instantiates a new circle and places it randomly in the world.
	 *
	 * @param world the world to place it in
	 */
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

	/**
	 * Instantiates a new circle.
	 *
	 * @param position the position of the circle
	 * @param direction the direction the circle is facing in
	 * @param color the color of the circle
	 * @param world the world to place it in
	 */
	public Circle(Vector2 position, Vector2 direction, Color color, World world)
	{
		super(new Vector2(RADIUS / 2, RADIUS / 2), position, direction.normalize().mult(SPEED), new Vector2(0, 0),
				color, world);
		this.eyePosition = new Vector2((getPosition().getX() + getVelocity().normalize().getX() * getSize().getX() / 2),
				(getPosition().getY() + getVelocity().normalize().getY() * getSize().getY() / 2));
	}

	// Overridden methods
	// -----------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see world.Sprite#paint(java.awt.Graphics)
	 */
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

	/* (non-Javadoc)
	 * @see world.Sprite#update()
	 */
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

		if(mind != null) mind.think();

		calcStats();
		updateCounters();
		shielded = false;
	}

	/* (non-Javadoc)
	 * @see world.Sprite#collide(world.Sprite)
	 */
	@Override
	public final void collide(Sprite s)
	{
		if(s == null) return;
		// die when hit by projectile (not own projectile)
		if (s instanceof Projectile && !((Projectile) s).isOwner(this))
		{
			projectileCollisions++;
			totalProjectileCollisions++;
			kill();
		}
		else if (s instanceof Mine)
		{
			mineCollisions++;
			totalMineCollisions++;
			kill();
		}
		// slide on all other objects
		else if (s instanceof Circle)
		{
			if (((Circle) s).isAlive())
				slide(s);
		}
		else if (s instanceof Shield)
		{
			shielded = true;
		}
		else if (!(s instanceof Projectile || s instanceof Shield))
		{
			slide(s);
		}
	}

	/* (non-Javadoc)
	 * @see world.Sprite#copy()
	 */
	@Override
	public Sprite copy()
	{
		return new Circle(this);
	}



	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Circle c)
	{
		// @formatter:off
		if (c.totalKills > totalKills) return 1; else if (c.totalKills < totalKills) return -1;
		if (c.deaths < deaths) return 1; else if (c.deaths > deaths) return -1;
		if (c.totalAccuracy > accuracy) return 1; else if (c.totalAccuracy < totalAccuracy) return -1;
		return 0;
		// @formatter:on
	}

	// instance methods
	// ----------------------------------------------------------------------

	/**
	 * Kills the circle.
	 */
	public void kill()
	{
		respawnTimer = RESPAWN_TIME;
		deaths++;
		setAlive(false);
	}

	/**
	 * Respawns the circle.
	 */
	private final void respawn()
	{
		if (respawnTimer <= 0)
		{
			accuracy = 1;
			shotsFired = 0;
			hits = 0;
			kills = 0;
			ticksAlive = 0;
			shieldsAcquired = 0;
			obstacleCollisions = 0;
			wallCollisions = 0;
			getWorld().respawn(this);
			respawnTimer = RESPAWN_TIME;
		}
	}

	/**
	 * Calcculates the stats of the circles.
	 */
	private void calcStats()
	{
		if (shotsFired > 0)
			accuracy = (float) hits / shotsFired;
		if (totalShotsFired > 0)
			totalAccuracy = (float) totalHits / totalShotsFired;
	}

	/**
	 * Updates the counters.
	 */
	private final void updateCounters()
	{
		// @formatter:off
		if (shootTimer > 0) shootTimer--;
		if (respawnTimer > 0) respawnTimer--;
		if (isAlive()) ticksAlive++;
		// @formatter:on
	}

	/**
	 * Requests items in view.
	 *
	 * @return a List containing's the object in the Bot's view
	 */
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

	/**
	 * responsible for turning the robot.
	 *
	 * @param deltaTheta the angle to turn
	 */
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
			getWorld().generateObject(new Projectile(new Vector2(
					(int) (getPosition().getX() + getVelocity().normalize().getX() * getSize().getX() / 2),
					(int) (getPosition().getY() + getVelocity().normalize().getY() * getSize().getY() / 2)),
					getVelocity(), this, getWorld()));
			shootTimer = RELOAD_TIME;
			shotsFired++;
			totalShotsFired++;
		}
	}

	// Getters and Setters
	// -------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see world.Sprite#setPosition(lib.Vector2)
	 */
	@Override
	public void setPosition(Vector2 position)
	{
		super.setPosition(position);
		this.eyePosition = new Vector2((position.getX() + getVelocity().normalize().getX() * getSize().getX() / 2),
				(position.getY() + getVelocity().normalize().getY() * getSize().getY() / 2));
	}

	/**
	 * Gets the total accuracy.
	 *
	 * @return the total accuracy
	 */
	public float getTotalAccuracy()
	{
		return totalAccuracy;
	}

	/**
	 * Sets the total accuracy.
	 *
	 * @param totalAccuracy the new total accuracy
	 */
	public void setTotalAccuracy(float totalAccuracy)
	{
		this.totalAccuracy = totalAccuracy;
	}

	/**
	 * Gets the accuracy.
	 *
	 * @return the accuracy
	 */
	public float getAccuracy()
	{
		return accuracy;
	}

	/**
	 * Sets the accuracy.
	 *
	 * @param accuracy the new accuracy
	 */
	public void setAccuracy(float accuracy)
	{
		this.accuracy = accuracy;
	}

	/**
	 * Gets the total shots fired.
	 *
	 * @return the total shots fired
	 */
	public int getTotalShotsFired()
	{
		return totalShotsFired;
	}

	/**
	 * Sets the total shots fired.
	 *
	 * @param totalShotsFired the new total shots fired
	 */
	public void setTotalShotsFired(int totalShotsFired)
	{
		this.totalShotsFired = totalShotsFired;
	}

	/**
	 * Gets the shots fired.
	 *
	 * @return the shots fired
	 */
	public int getShotsFired()
	{
		return shotsFired;
	}

	/**
	 * Sets the shots fired.
	 *
	 * @param shotsFired the new shots fired
	 */
	public void setShotsFired(int shotsFired)
	{
		this.shotsFired = shotsFired;
	}

	/**
	 * Gets the total hits.
	 *
	 * @return the total hits
	 */
	public int getTotalHits()
	{
		return totalHits;
	}

	/**
	 * Sets the total hits.
	 *
	 * @param totalHits the new total hits
	 */
	public void setTotalHits(int totalHits)
	{
		this.totalHits = totalHits;
	}

	/**
	 * Gets the hits.
	 *
	 * @return the hits
	 */
	public int getHits()
	{
		return hits;
	}

	/**
	 * Sets the hits.
	 *
	 * @param hits the new hits
	 */
	public void setHits(int hits)
	{
		this.hits = hits;
	}

	/**
	 * Gets the deaths.
	 *
	 * @return the deaths
	 */
	public int getDeaths()
	{
		return deaths;
	}

	/**
	 * Sets the deaths.
	 *
	 * @param deaths the new deaths
	 */
	public void setDeaths(int deaths)
	{
		this.deaths = deaths;
	}

	/**
	 * Gets the total kills.
	 *
	 * @return the total kills
	 */
	public int getTotalKills()
	{
		return totalKills;
	}

	/**
	 * Sets the total kills.
	 *
	 * @param totalKills the new total kills
	 */
	public void setTotalKills(int totalKills)
	{
		this.totalKills = totalKills;
	}

	/**
	 * Gets the kills.
	 *
	 * @return the kills
	 */
	public int getKills()
	{
		return kills;
	}

	/**
	 * Sets the kills.
	 *
	 * @param kills the new kills
	 */
	public void setKills(int kills)
	{
		this.kills = kills;
	}

	/**
	 * Gets the ticks alive.
	 *
	 * @return the ticks alive
	 */
	public int getTicksAlive()
	{
		return ticksAlive;
	}

	/**
	 * Sets the ticks alive.
	 *
	 * @param ticksAlive the new ticks alive
	 */
	public void setTicksAlive(int ticksAlive)
	{
		this.ticksAlive = ticksAlive;
	}

	/**
	 * Gets the total shields acquired.
	 *
	 * @return the total shields acquired
	 */
	public int getTotalShieldsAcquired()
	{
		return totalShieldsAcquired;
	}

	/**
	 * Sets the total shields acquired.
	 *
	 * @param totalShieldsAcquired the new total shields acquired
	 */
	public void setTotalShieldsAcquired(int totalShieldsAcquired)
	{
		this.totalShieldsAcquired = totalShieldsAcquired;
	}

	/**
	 * Gets the shields acquired.
	 *
	 * @return the shields acquired
	 */
	public int getShieldsAcquired()
	{
		return shieldsAcquired;
	}

	/**
	 * Sets the shields acquired.
	 *
	 * @param shieldsAcquired the new shields acquired
	 */
	public void setShieldsAcquired(int shieldsAcquired)
	{
		this.shieldsAcquired = shieldsAcquired;
	}

	/**
	 * Gets the total obstacle collisions.
	 *
	 * @return the total obstacle collisions
	 */
	public int getTotalObstacleCollisions()
	{
		return totalObstacleCollisions;
	}

	/**
	 * Sets the total obstacle collisions.
	 *
	 * @param totalObstacleCollisions the new total obstacle collisions
	 */
	public void setTotalObstacleCollisions(int totalObstacleCollisions)
	{
		this.totalObstacleCollisions = totalObstacleCollisions;
	}

	/**
	 * Gets the obstacle collisions.
	 *
	 * @return the obstacle collisions
	 */
	public int getObstacleCollisions()
	{
		return obstacleCollisions;
	}

	/**
	 * Sets the obstacle collisions.
	 *
	 * @param obstaclesCollisions the new obstacle collisions
	 */
	public void setObstacleCollisions(int obstaclesCollisions)
	{
		this.obstacleCollisions = obstaclesCollisions;
	}

	/**
	 * Gets the total wall collisions.
	 *
	 * @return the total wall collisions
	 */
	public int getTotalWallCollisions()
	{
		return totalWallCollisions;
	}

	/**
	 * Sets the total wall collisions.
	 *
	 * @param totalWallCollisions the new total wall collisions
	 */
	public void setTotalWallCollisions(int totalWallCollisions)
	{
		this.totalWallCollisions = totalWallCollisions;
	}

	/**
	 * Gets the wall collisions.
	 *
	 * @return the wall collisions
	 */
	public int getWallCollisions()
	{
		return wallCollisions;
	}

	/**
	 * Sets the wall collisions.
	 *
	 * @param wallCollisions the new wall collisions
	 */
	public void setWallCollisions(int wallCollisions)
	{
		this.wallCollisions = wallCollisions;
	}

	/**
	 * Gets the shoot timer.
	 *
	 * @return the shoot timer
	 */
	public int getShootTimer()
	{
		return shootTimer;
	}

	/**
	 * Sets the amount of time to reload.
	 *
	 * @param shootTimer the amount of time to reload
	 */
	public void setShootTimer(int shootTimer)
	{
		this.shootTimer = shootTimer;
	}

	/**
	 * Gets the respawn timer.
	 *
	 * @return the respawn timer
	 */
	public int getRespawnTimer()
	{
		return respawnTimer;
	}

	/**
	 * Sets the respawn timer.
	 *
	 * @param respawnTimer the new respawn timer
	 */
	public void setRespawnTimer(int respawnTimer)
	{
		this.respawnTimer = respawnTimer;
	}

	/**
	 * Gets the AI controlling the robot.
	 *
	 * @return the AI controlling the robot
	 */
	public Mind getMind()
	{
		// intentional shallow copy minds copy circles
		return mind;
	}

	/**
	 * Sets the AI containing the robot.
	 *
	 * @param mind the new AI
	 */
	public void setMind(Mind mind)
	{
		// intentional shallow copy minds copy circles
		this.mind = mind;
	}

	/**
	 * Gets the eye position.
	 *
	 * @return the eye position
	 */
	public Vector2 getEyePosition()
	{
		return eyePosition.copy();
	}

	/**
	 * Gets the mine collisions.
	 *
	 * @return the mine collisions
	 */
	public int getMineCollisions()
	{
		return mineCollisions;
	}

	/**
	 * Sets the mine collisions.
	 *
	 * @param mineCollisions the new mine collisions
	 */
	public void setMineCollisions(int mineCollisions)
	{
		this.mineCollisions = mineCollisions;
	}

	/**
	 * Gets the total mine collisions.
	 *
	 * @return the total mine collisions
	 */
	public int getTotalMineCollisions()
	{
		return totalMineCollisions;
	}

	/**
	 * Sets the total mine collisions.
	 *
	 * @param totalMineCollisions the new total mine collisions
	 */
	public void setTotalMineCollisions(int totalMineCollisions)
	{
		this.totalMineCollisions = totalMineCollisions;
	}
	

	/**
	 * Checks if is shielded.
	 *
	 * @return true, if it is shielded
	 */
	public boolean isShielded()
	{
		return shielded;
	}
	

	/**
	 * Sets the shielded.
	 *
	 * @param hasShield if it is shielded
	 */
	public void setShielded(boolean hasShield)
	{
		this.shielded = hasShield;
	}

	/**
	 * Gets the projectile collisions.
	 *
	 * @return the projectile collisions
	 */
	public int getProjectileCollisions()
	{
		return projectileCollisions;
	}

	/**
	 * Sets the projectile collisions.
	 *
	 * @param projectileCollisions the new projectile collisions
	 */
	public void setProjectileCollisions(int projectileCollisions)
	{
		this.projectileCollisions = projectileCollisions;
	}

	/**
	 * Gets the total projectile collisions.
	 *
	 * @return the total projectile collisions
	 */
	public int getTotalProjectileCollisions()
	{
		return totalProjectileCollisions;
	}

	/**
	 * Sets the total projectile collisions.
	 *
	 * @param totalProjectileCollisions the new total projectile collisions
	 */
	public void setTotalProjectileCollisions(int totalProjectileCollisions)
	{
		this.totalProjectileCollisions = totalProjectileCollisions;
	}

}
