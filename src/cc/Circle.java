package cc;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import lib.Vector2;

public class Circle extends Sprite
{
	private ArrayList<Projectile> projectiles;

	public Circle(float x, float y, World world)
	{
		super(x, y, world);
		setVelocity(new Vector2(1, 0, true));
		setWidth(40);
		setHeight(40);
		projectiles = new ArrayList<Projectile>();
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

		// paint projectiles
		for (Projectile p : projectiles)
		{
			p.paint(g);
		}
	}

	@Override
	public void update()
	{
		setPosition(getPosition().add(getVelocity()));
		setVelocity(new Vector2(1f, (float) (getVelocity().angle() + Math.PI / 100), true));
		if (i % 60 == 0)
		{
			shoot();
		}
		System.out.println(getVelocity());

		for (Projectile p : projectiles)
		{
			p.update();
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
	}

	public void shoot()
	{
		projectiles.add(new Projectile(
				(int) (getPosition().getX() + getVelocity().normalize().getX() * getWidth() / 2) - getHeight() / 4,
				(int) (getPosition().getY() + getVelocity().normalize().getY() * getHeight() / 2) - getHeight() / 4,
				getVelocity(), getColor(), getWorld()));
	}

}
