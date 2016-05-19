package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

public class Projectile extends Sprite
{
	/** maximum speed of a projectile **/
	private static final float PROJECTILE_SPEED = 5;
	/** owner circle **/
	private Circle c;

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
		super(10, 10,
				new Vector2(x, y), // pos
				new Vector2(PROJECTILE_SPEED, velocity.angle(), true), // vel
				new Vector2(0, 0), // acc
				c.getColor(), world);
		this.c = c;
		// @formatter:on
	}

	@Override
	public void paint(Graphics g)
	{	
		// @formatter:off
		// paint circle for projectile
		g.setColor(getColor());
		g.fillOval(
				(int) (getPosition().getX() - getWidth() / 2), 
				(int) (getPosition().getY() - getHeight() / 2),
				getWidth(), getHeight());
		// @formatter:on
		
		g.setColor(new Color(255 - getColor().getRed(), 255 - getColor().getGreen(), 255 - getColor().getBlue()));
		if(Main.DEBUG)
			g.drawRect(
					(int) getPosition().getX() - getWidth() / 2,
					(int) getPosition().getY() - getWidth() / 2,
					getWidth(), getHeight());
	}

	@Override
	public void update()
	{
		// @formatter:off
		// die if out of bounds
		if (getPosition().getX() - getWidth() / 2 < 0 
				|| getPosition().getX() + getWidth() / 2 > getWorld().getWidth()
				|| getPosition().getY() - getWidth() / 2 < 0
				|| getPosition().getY() + getWidth() / 2 > getWorld().getHeight())
			setAlive(false);
		// @formatter:on

		// update position
		setPosition(getPosition().add(getVelocity()));
	}

	@Override
	public void collide(Sprite s)
	{
		// @formatter:off
		if (s != c) setAlive(false);
		// @formatter:on
	}

	/**
	 * The isOwner method tells whether the given circle is the owner of the projectile
	 * @param c Circle to check
	 * @return is owner circle?
	 */
	public boolean isOwner(Circle c)
	{
		return this.c == c; // compare reference (must be exact same instance)
	}
}
