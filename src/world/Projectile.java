package world;

import java.awt.Graphics;

import app.Main;
import lib.Vector2;

public class Projectile extends Sprite
{
	/** maximum speed of a projectile **/
	private static final float SPEED = Integer.parseInt(Main.CONFIG.get("projectileSpeed"));
	private static final float RADIUS = Integer.parseInt(Main.CONFIG.get("projectileRadius"));
	/** owner circle **/
	private Circle owner;

	// Constructors
	// --------------------------------------------------------------

	public Projectile(Projectile p)
	{
		super(p);
	}

	/**
	 * 5-Argument Projectile Constructor
	 * @param x x pos
	 * @param y y pos
	 * @param velocity velocity vector
	 * @param c owner circle
	 * @param world plane of existence
	 */
	public Projectile(float x, float y, Vector2 velocity, Circle c, World world)
	{
		// @formatter:off
		super(
				new Vector2(RADIUS, RADIUS), //size
				new Vector2(x, y), // pos
				new Vector2(SPEED, velocity.angle(), true), // vel
				new Vector2(0, 0), // acc
				c.getColor(), world);
		this.owner = c; // intentional shallow copy
		// @formatter:on
	}

	// Overridden methods
	// -----------------------------------------------------------------

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		// @formatter:off
		// paint circle for projectile
		g.setColor(getColor());
		g.fillOval(
				(int) (getPosition().getX() - getSize().getX() / 2), 
				(int) (getPosition().getY() - getSize().getY() / 2),
				(int) getSize().getX(), (int) getSize().getY());
		// @formatter:on

		if (Main.debug)
			super.paint(g);
	}

	@Override
	public void update()
	{
		// @formatter:off
		// die if out of bounds
		if (getPosition().getX() - getSize().getX() / 2 < 0 
				|| getPosition().getX() + getSize().getX() / 2 > getWorld().getSize().getX()
				|| getPosition().getY() - getSize().getX() / 2 < 0
				|| getPosition().getY() + getSize().getX() / 2 > getWorld().getSize().getY())
			setExistence(false);
		// @formatter:on

		// update position
		setPosition(getPosition().add(getVelocity()));
	}

	@Override
	public void collide(Sprite s)
	{
		// @formatter:off
		if (s.getId() != owner.getId() && !(s instanceof Shield && ((Shield) s).isOwner(owner)))
		{
				setExistence(false);
				if(s instanceof Circle) 
				{
					owner.setKills(owner.getKills() + 1);
					owner.setHits(owner.getHits() + 1);
					owner.setTotalHits(owner.getTotalHits() + 1);
					owner.setTotalKills(owner.getTotalHits() + 1);
				}
				else if (s instanceof Shield)
				{
					owner.setHits(owner.getHits() + 1);
					owner.setTotalHits(owner.getTotalHits() + 1);
				}
		}
		// @formatter:on
	}

	@Override
	public Sprite copy()
	{
		return new Projectile(this);
	}

	/**
	 * The isOwner method tells whether the given circle is the owner of the
	 * projectile
	 * @param c Circle to check
	 * @return is owner circle?
	 */
	public boolean isOwner(Sprite c)
	{
		return owner.getId() == c.getId();
	}
}
