package cc;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import lib.Vector2;

public class Circle extends Sprite
{

	public Circle(float x, float y, World world)
	{
		super(x, y, world);
		setVelocity(new Vector2(1, 0, true));
		setWidth(40);
		setHeight(40);
		setColor(Color.BLUE);
	}

	public Circle(float x, float y, Color color, World world)
	{
		super(x, y, world);
		setColor(color);
	}

	public static final float MAX_TURNING_ANGLE = (float) Math.PI / 60f;

	int i = 0;

	@Override
	public void paint(Graphics g)
	{
		// paint circle with direction visualizer
		g.setColor(getColor());
		g.fillOval((int) getPosition().getX() - getWidth() / 2, (int) getPosition().getY() - getWidth() / 2, 40, 40);
		g.setColor(new Color(255 - getColor().getRed(), 255 - getColor().getGreen(), 255 - getColor().getBlue()));
		g.fillOval((int) (getPosition().getX() + getVelocity().normalize().getX() * getWidth() / 2) - 5,
				(int) (getPosition().getY() + getVelocity().normalize().getY() * getHeight() / 2) - 5, 10, 10);
	}

	@Override
	public void update()
	{
		// slide on walls
		if (getPosition().getX() - getWidth() / 2 < 0) setPosition(getPosition().setX(getWidth() / 2));
		else if (getPosition().getX() + getWidth() / 2 > getWorld().getWidth()) setPosition(getPosition().setX(getWorld().getWidth() - getWidth() / 2));
		if (getPosition().getY() - getWidth() / 2 < 0) setPosition(getPosition().setY(getWidth() / 2));
		else if (getPosition().getY() + getWidth() / 2 > getWorld().getHeight()) setPosition(getPosition().setY(getWorld().getHeight() - getHeight() / 2));
		
		// update position
		setPosition(getPosition().add(getVelocity()));
		
		// test change in velocity and shooting
		setVelocity(new Vector2(2f, (float) (getVelocity().angle() + Math.PI / 500), true));
		if (i % 60 == 0)
		{
			shoot();
		}
		i++;
	}

	public void exchange(Sprite s)
	{
		setVelocity(s.getVelocity());
	}
	
	public void bounce(Sprite s)
	{
		if (getPosition().getX() < s.getPosition().getX() + s.getWidth() / 2
				&& getPosition().getX() > s.getPosition().getX() - s.getWidth() / 2)
		{
			setVelocity(getVelocity().setY(-getVelocity().getY()));
		}
		if (getPosition().getY() < s.getPosition().getY() + s.getWidth() / 2
				&& getPosition().getY() > s.getPosition().getY() - s.getWidth() / 2)
		{
			setVelocity(getVelocity().setX(-getVelocity().getX()));
		}
	}

	@Override
	public void collide(Sprite s)
	{
		if (s instanceof Obstacle)
		{
			bounce(s);
		}
		else if (s instanceof Circle)
		{
			exchange(s);
		}
		else if (s instanceof Projectile)
		{
			if (!((Projectile) s).isOwner(this)) setAlive(false); // die when hit by hostile projectile
		}
	}

	public void shoot()
	{
		getWorld().generateObject(new Projectile(
				(int) (getPosition().getX() + getVelocity().normalize().getX() * getWidth() / 2) - getHeight() / 4,
				(int) (getPosition().getY() + getVelocity().normalize().getY() * getHeight() / 2) - getHeight() / 4,
				getVelocity(), this, getWorld()));
	}

}
