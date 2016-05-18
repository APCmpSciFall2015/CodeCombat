package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

public class Projectile extends Sprite
{
	private static final float PROJECTILE_SPEED = 5;
	
	public Projectile(float x, float y, Vector2 velocity, Color color, World world)
	{
		super(x, y, world);
		setVelocity(velocity.copy().normalize().mult(PROJECTILE_SPEED));
		setWidth(10);
		setHeight(10);
		setColor(color);
	}

	public void paint(Graphics g) 
	{
		g.setColor(getColor());
		g.fillOval((int) (getPosition().getX() + getWidth() / 2), (int) (getPosition().getY()  + getHeight() / 2), getWidth(), getHeight());
	}

	@Override
	public void update()
	{
		setPosition(getPosition().add(getVelocity()));
	}

	@Override
	public void collide(Sprite s)
	{
	}
}
