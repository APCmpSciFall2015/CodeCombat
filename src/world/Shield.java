package world;

import java.awt.Color;
import java.awt.Graphics;

import app.Main;
import lib.Vector2;

/**
 * The Shield class provides a layer of protection for Circles in game.
 * @author Brian, Robbie, Josh
 * @version 0.1
 */
public class Shield extends Sprite
{
	
	/**  time for duck to respawn *. */
	public static final int RESPAWN_TIME = Integer.parseInt(Main.CONFIG.get("shieldRespawnTime"));
	
	/**  radius of duck *. */
	public static final int RADIUS = Integer.parseInt(Main.CONFIG.get("shieldRadius"));
	
	/** speed of duck */
	public static final float SPEED = Integer.parseInt(Main.CONFIG.get("shieldSpeed"));
	
	/** path radius of duck */
	public static final int PATH_RADIUS = Integer.parseInt(Main.CONFIG.get("shieldPathRadius"));
	
	// Instance variables
	// ----------------------------------------
	
	/**  whether or not the shield has found its mama duck *. */
	private boolean unbound;
	
	/**  shield's mama duck **/
	private Sprite owner;
	
	/**  time for duck's respawn *. */
	private int respawnTimer = RESPAWN_TIME;

	// Constructors 
	// -----------------------------------------
	
	/**
	 * Shield copy constructor.
	 *
	 * @param s duck to copy
	 */
	public Shield(Shield s)
	{
		super(s);
		this.unbound = s.unbound;
		this.owner = s.getOwner(); // deep copy
	}

	/**
	 * 1-Argument Circle constructor.
	 *
	 * @param world plane of existence
	 */
	public Shield(World world)
	{
		// @formatter:off
		super(
				new Vector2(RADIUS, RADIUS), // size
				new Vector2((int) (Math.random() * world.getSize().getX()), (int) (Math.random() * world.getSize().getY())), // pos
				new Vector2(1, (float) (Math.random() * Math.PI * 2), true), // vel
				new Vector2(0, 0), // acc
				Color.CYAN, // random color
				world);
		unbound = true;
		// @formatter:on
	}

	// Overridden methods
	// -----------------------------------------
	
	/* (non-Javadoc)
	 * @see world.Sprite#update()
	 */
	@Override
	public void update()
	{
		if (isAlive())
		{
			// look for mama duck
			if (unbound)
			{
				// update position (move in circle)
				setAcceleration(new Vector2(SPEED * SPEED  / PATH_RADIUS, (float) (getVelocity().angle() + Math.PI / 2), true));
				Vector2 previousVelocity = getVelocity();
				setVelocity(getVelocity().add(getAcceleration()).normalize().mult(SPEED));
				setPosition(getPosition().add(getVelocity().add(previousVelocity).div(2)));
			}
		}
		else
		{
			respawnTimer--;
			respawn();
		}
	}

	/* (non-Javadoc)
	 * @see world.Sprite#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g)
	{	
		if (isAlive())
		{
			super.paint(g);
			// paint the sad little duck looking for its mama
			
			// @formatter:off
			g.setColor(getColor());
			g.drawOval(
					(int) (getPosition().getX() - getSize().getX() / 2),
					(int) (getPosition().getY() - getSize().getX() / 2),
					(int) getSize().getX(), (int) getSize().getY());
			// @formatter:on
		}
	}

	/* (non-Javadoc)
	 * @see world.Sprite#copy()
	 */
	@Override
	public Sprite copy()
	{
		return new Shield(this);
	}

	/* (non-Javadoc)
	 * @see world.Sprite#collide(world.Sprite)
	 */
	@Override
	public void collide(Sprite s)
	{
		if(s == null) return;
		// follow master circle
		if (!unbound && s.getId() == owner.getId())
		{
			setPosition(s.getPosition());
		}
		// attach to master circle (like a little ducky)
		else if (unbound && s instanceof Circle)
		{
			setOwner(s);
			((Circle) owner).setShielded(true);
			((Circle) owner).setShieldsAcquired(((Circle) owner).getShieldsAcquired() + 1);
			((Circle) owner).setTotalShieldsAcquired(((Circle) owner).getTotalShieldsAcquired() + 1);
			unbound = false;
			setSize(new Vector2(s.getSize().getX() + 2 * RADIUS, s.getSize().getY() + 2 * RADIUS));
			setPosition(s.getPosition());
		}
		else if (unbound && (s instanceof Projectile || s instanceof Mine))
		{
			setAlive(false);
		}
		
		// run in circle (also like a little ducky)
		if (!unbound)
		{
			if (s instanceof Projectile && !((Projectile) s).isOwner(owner))
			{
				setAlive(false);
				((Circle) owner).setShielded(false);
				((Circle) owner).setProjectileCollisions(((Circle) owner).getProjectileCollisions() + 1);
				((Circle) owner).setTotalProjectileCollisions(((Circle) owner).getProjectileCollisions() + 1);
				setSize(new Vector2(RADIUS, RADIUS));
				unbound = true;
			}
			else if (s instanceof Mine)
			{
				setAlive(false);
				setSize(new Vector2(RADIUS, RADIUS));
				((Circle) owner).setShielded(false);
				((Circle) owner).setMineCollisions(((Circle) owner).getMineCollisions() + 1);
				unbound = true;
			}
		}
	}
	
	// Functional methods 
	// --------------------------------------

	/**
	 * Respawns the shield.
	 */
	private final void respawn()
	{
		if (respawnTimer <= 0)
		{
			getWorld().respawn(this);
			respawnTimer = RESPAWN_TIME;
		}
	}

	/**
	 * The isOwner method tells whether the given circle is the owner of the
	 * shield.
	 *
	 * @param c Circle to check
	 * @return is owner circle?
	 */
	public boolean isOwner(Sprite c)
	{
		return owner != null && owner.getId() == c.getId();
	}
	
	// Getters and Setters
	// -------------------------------------------------

	/**
	 * Checks if the shield is part of a sprite.
	 *
	 * @return true, if is unbound
	 */
	public boolean isUnbound()
	{
		return unbound;
	}

	/**
	 * Sets whether the shield is not part of a sprite.
	 *
	 * @param unbound true if not bound to a sprite
	 */
	public void setUnbound(boolean unbound)
	{
		this.unbound = unbound;
	}

	/**
	 * Gets the owner of the shield.
	 *
	 * @return the owner
	 */
	public Sprite getOwner()
	{
		if (owner != null)
			return owner.copy();
		return null;
	}

	/**
	 * Sets the owner of the shield.
	 *
	 * @param owner the new owner
	 */
	public void setOwner(Sprite owner)
	{
		this.owner = owner;
	}

}
