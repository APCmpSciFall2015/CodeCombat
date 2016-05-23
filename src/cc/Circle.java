package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

/**
 * The Circle class represents a battle bot sprite in its generic form.
 * @author Robert, Brian, Dalton, Derek
 * @version 0.1
 * @see Sprite
 */
public class Circle extends Sprite
{
	/** maximum rate of change in the direction of velocity **/
	public static final float MAX_TURNING_ANGLE = (float) Math.PI / 60f;

	/**
	 * Circle copy constructor
	 * @param x x pos
	 * @param y y pos
	 * @param world plane of existence
	 */
	public Circle(Circle c)
	{
		super(c.getSize(), c.getPosition(), c.getVelocity(), c.getAcceleration(), c.getColor(),
				c.getWorld());
	}

	/**
	 * 3-Argument Circle constructor
	 * @param x x pos
	 * @param y y pos
	 * @param world plane of existence
	 */
	public Circle(float x, float y, World world)
	{
		// @formatter:off
		super(
				new Vector2(40, 40), // size
				new Vector2(x, y), new Vector2(1, 0, true), new Vector2(0, 0), // pos, vel, acc
				new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)), // random color
				world);
		// @formatter:on
	}

	/**
	 * 4-Argument Circle constructor
	 * @param x x pos
	 * @param y y pos
	 * @param color color
	 * @param world plane of existence
	 */
	public Circle(float x, float y, Color color, World world)
	{
		super(new Vector2(40, 40), new Vector2(x, y), new Vector2(1, 0, true), new Vector2(0, 0), color, world);
	}

	@Override
	public void paint(Graphics g)
	{
		// @formatter:off
		g.setColor(getColor());
		// paint circle
		g.fillOval(
				(int) (getPosition().getX() - getSize().getX() / 2),
				(int) (getPosition().getY() - getSize().getX() / 2),
				(int) getSize().getX(), (int) getSize().getY());

		// paint direction visualizer (color declared is inverted)
		g.setColor(new Color(255 - getColor().getRed(), 255 - getColor().getGreen(), 255 - getColor().getBlue()));
		g.fillOval(
				(int) (getPosition().getX() + getVelocity().normalize().getX() * getSize().getX() / 2) - 5,
				(int) (getPosition().getY() + getVelocity().normalize().getY() * getSize().getY() / 2) - 5,
				(int) (getSize().getX() / 4), (int) (getSize().getY() / 4));
		// @formatter:on

		if (Main.DEBUG)
			g.drawRect((int) (getPosition().getX() - getSize().getX() / 2), (int) (getPosition().getY() - getSize().getX() / 2),
					(int) getSize().getX(), (int) getSize().getY());
	}

	int i = 0;

	@Override
	public void update()
	{
		// slide on left and right walls
		if (getPosition().getX() - getSize().getX() / 2 < 0)
			setPosition(getPosition().setX(getSize().getX() / 2));
		else if (getPosition().getX() + getSize().getX() / 2 > getWorld().getSize().getX())
			setPosition(getPosition().setX(getWorld().getSize().getX() - getSize().getX() / 2));

		// slide on top and bottom walls
		if (getPosition().getY() - getSize().getX() / 2 < 0)
			setPosition(getPosition().setY(getSize().getX() / 2));
		else if (getPosition().getY() + getSize().getX() / 2 > getWorld().getSize().getY())
			setPosition(getPosition().setY(getWorld().getSize().getY() - getSize().getY() / 2));

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

	@Override
	public void collide(Sprite s)
	{
		// @formatter:off
		// die when hit by projectile (not own projectile)
		if (s instanceof Projectile && !((Projectile) s).isOwner(this)) setAlive(false);
		// slide on all other objects
		else if (!(s instanceof Projectile)) slide(s);
		// @formatter:on
	}

	@Override
	public Sprite copy()
	{
		return new Circle(this);
	}

	/**
	 * The shoot method generates a projectile in an attempt to destroy other
	 * circles.
	 */
	public void shoot()
	{
		// generate a new projectile in front of the circle traveling faster in
		// the same direction
		getWorld().generateObject(new Projectile(
				(int) (getPosition().getX() + getVelocity().normalize().getX() * getSize().getX() / 2) - getSize().getY() / 4,
				(int) (getPosition().getY() + getVelocity().normalize().getY() * getSize().getY() / 2) - getSize().getY() / 4,
				getVelocity(), this, getWorld()));
	}

}
