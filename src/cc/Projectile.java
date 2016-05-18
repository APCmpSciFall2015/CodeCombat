package cc;

import java.awt.Graphics;

import lib.Vector2;

public class Projectile extends Sprite
{
	private static final float PROJECTILE_SPEED = 5;
	private Circle c;

	public Projectile(float x, float y, Vector2 velocity, Circle c, World world)
	{
		super(x, y, world);
		setVelocity(velocity.copy().normalize().mult(PROJECTILE_SPEED));
		setWidth(10);
		setHeight(10);
		setColor(c.getColor());
		this.c = c;
	}

	public void paint(Graphics g)
	{
		g.setColor(getColor());
		g.fillOval((int) (getPosition().getX() + getWidth() / 2), (int) (getPosition().getY() + getHeight() / 2),
				getWidth(), getHeight());
	}

	@Override
	public void update()
	{
		// die if out of bounds
		if (getPosition().getX() - getWidth() / 2 < 0 || getPosition().getX() + getWidth() / 2 > getWorld().getWidth()
				|| getPosition().getY() - getWidth() / 2 < 0
				|| getPosition().getY() + getWidth() / 2 > getWorld().getHeight())
			setAlive(false);
		// update position
		setPosition(getPosition().add(getVelocity()));
	}

	@Override
	public void collide(Sprite s)
	{
		if (s != c)
		{
			setAlive(false);
		}
	}
	
	public boolean isOwner(Circle c)
	{
		return this.c == c;
	}
}
