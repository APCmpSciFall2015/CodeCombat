package cc;

import java.awt.Graphics;

import lib.Vector2;

public class Projectile extends Sprite
{
	/** maximum speed of a projectile **/
	private static final float PROJECTILE_SPEED = 5;
	/** owner circle **/
	private Circle c;

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
				new Vector2(10, 10), //size
				new Vector2(x, y), // pos
				new Vector2(PROJECTILE_SPEED, velocity.angle(), true), // vel
				new Vector2(0, 0), // acc
				c.getColor(), world);
		this.c = ( Circle) c.copy();
		// @formatter:on
	}

	// Overridden methods
	// -----------------------------------------------------------------
	
	@Override
	public void paint(Graphics g)
	{	
		// @formatter:off
		// paint circle for projectile
		g.setColor(getColor());
		g.fillOval(
				(int) (getPosition().getX() - getSize().getX() / 2), 
				(int) (getPosition().getY() - getSize().getY() / 2),
				(int) getSize().getX(), (int) getSize().getY());
		// @formatter:on
		
		
		if(Main.DEBUG) super.paint(g);
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
			setAlive(false);
		// @formatter:on

		// update position
		setPosition(getPosition().add(getVelocity()));
	}

	@Override
	public void collide(Sprite s)
	{
		// @formatter:off
		if (s.getId() != c.getId()) setAlive(false);
		// @formatter:on
	}
	
	@Override
	public Sprite copy()
	{
		return new Projectile(this);
	}

	/**
	 * The isOwner method tells whether the given circle is the owner of the projectile
	 * @param c Circle to check
	 * @return is owner circle?
	 */
	public boolean isOwner(Circle c)
	{
		return this.c.getId() == c.getId();
	}
}
