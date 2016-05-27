package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

/**
 * The Shield class provides a layer of protection for Circles in game.
 * @author Brian, Robbie, Josh
 * @version 0.1
 */
public class Shield extends Sprite
{
	public static final int RESPAWN_TIME = 5 * 60;
	/** whether or not the shield has found its mama duck **/
	private boolean unbound;
	/** shield's mama duck **/
	private Sprite owner;
	private int respawnTimer;

	/**
	 * Shield copy constructor
	 * @param s duck to copy
	 */
	public Shield(Shield s)
	{
		super(s);
		this.unbound = s.unbound;
		this.owner = s.getOwner(); // deep copy
	}

	/**
	 * 1-Argument Circle constructor
	 * @param world plane of existence
	 */
	public Shield(World world)
	{
		// @formatter:off
		super(
				new Vector2(10, 10), // size
				new Vector2((int) (Math.random() * world.getSize().getX()), (int) (Math.random() * world.getSize().getY())), // pos
				new Vector2(1, (float) (Math.random() * Math.PI * 2), true), // vel
				new Vector2(0, 0), // acc
				new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)), // random color
				world);
		unbound = true;
		// @formatter:on
	}

	@Override
	public void update()
	{
		if(isAlive()){
		// look for mama duck
		if (unbound)
		{
			super.update();
			// update position (move in circle)
			setAcceleration(new Vector2(1f / 30f, (float) (getVelocity().angle() + Math.PI / 2), true));
			Vector2 previousVelocity = getVelocity();
			setVelocity(getVelocity().add(getAcceleration()).normalize());
			setPosition(getPosition().add(getVelocity().add(previousVelocity).div(2)));
		}
	}
		else
		{
			respawnTimer ++;
			if(respawnTimer < 0){
				this.setAlive(true);
			}
					
		}
	}

	public final void paint(Graphics g)
	{
		super.paint(g);
		// paint the sad little duck looking for its mama
		if (unbound)
		{
			// @formatter:off
			g.setColor(Color.CYAN);
			// paint circle
			g.drawOval(
					(int) (getPosition().getX() - getSize().getX() / 2),
					(int) (getPosition().getY() - getSize().getX() / 2),
					(int) getSize().getX(), (int) getSize().getY());
		}
		// paint the shield (duckling) as a circle around the master (mama duck)
		else
		{
			// @formatter:off
			g.setColor(Color.CYAN);
			// paint circle
			g.drawOval(
					(int) (getPosition().getX() - getSize().getX() / 2),
					(int) (getPosition().getY() - getSize().getX() / 2),
					(int) getSize().getX(), (int) getSize().getY());
			// @formatter:on
		}
	}

	@Override
	public Sprite copy()
	{
		return new Shield(this);
	}

	@Override
	public void collide(Sprite s)
	{
		// follow master circle
		if (!unbound && s.getId() == owner.getId())
		{
			setPosition(s.getPosition());
		}
		// attach to master circle (like a little ducky)
		else if (unbound && s instanceof Circle)
		{
			setOwner(s);
			unbound = false;
			setSize(new Vector2(60, 60));
			setPosition(s.getPosition());
		}
		// run in circle (also like a little ducky)
		if (!unbound)
		{
			if (s instanceof Projectile && !((Projectile) s).isOwner(owner))
			{
				this.setAlive(false);
				unbound = true;
				respawnTimer = RESPAWN_TIME;
			}
		}
	}

	/**
	 * The isOwner method tells whether the given circle is the owner of the
	 * projectile
	 * @param c Circle to check
	 * @return is owner circle?
	 */
	public boolean isOwner(Sprite c)
	{
		return owner != null && owner.getId() == c.getId();
	}

	public boolean isUnbound()
	{
		return unbound;
	}

	public void setUnbound(boolean unbound)
	{
		this.unbound = unbound;
	}

	public Sprite getOwner()
	{
		if (owner != null)
			return owner.copy();
												return null;
	}

	public void setOwner(Sprite owner)
	{
		this.owner = owner.copy();
	}

}
