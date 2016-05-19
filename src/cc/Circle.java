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
	 * 3-Argument Circle constructor
	 * @param x x pos
	 * @param y y pos
	 * @param world plane of existence
	 */
	public Circle(float x, float y, World world)
	{
		// @formatter:off
		super(40, 40, // size
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
		super(40, 40, new Vector2(x, y), new Vector2(1, 0, true), new Vector2(0, 0), color, world);
	}

	@Override
	public void paint(Graphics g)
	{	
		// @formatter:off
		g.setColor(getColor());
		// paint circle
		g.fillOval(
				(int) getPosition().getX() - getWidth() / 2,
				(int) getPosition().getY() - getWidth() / 2,
				getWidth(), getHeight());

		// paint direction visualizer (color declared is inverted)
		g.setColor(new Color(255 - getColor().getRed(), 255 - getColor().getGreen(), 255 - getColor().getBlue()));
		g.fillOval(
				(int) (getPosition().getX() + getVelocity().normalize().getX() * getWidth() / 2) - 5,
				(int) (getPosition().getY() + getVelocity().normalize().getY() * getHeight() / 2) - 5,
				getWidth() / 4, getHeight() / 4);
		// @formatter:on
		
		if(Main.DEBUG)
			g.drawRect(
					(int) getPosition().getX() - getWidth() / 2,
					(int) getPosition().getY() - getWidth() / 2,
					getWidth(), getHeight());
	}

	int i = 0;

	@Override
	public void update()
	{
		// slide on left and right walls
		if (getPosition().getX() - getWidth() / 2 < 0)
			setPosition(getPosition().setX(getWidth() / 2));
		else if (getPosition().getX() + getWidth() / 2 > getWorld().getWidth())
			setPosition(getPosition().setX(getWorld().getWidth() - getWidth() / 2));

		// slide on top and bottom walls
		if (getPosition().getY() - getWidth() / 2 < 0)
			setPosition(getPosition().setY(getWidth() / 2));
		else if (getPosition().getY() + getWidth() / 2 > getWorld().getHeight())
			setPosition(getPosition().setY(getWorld().getHeight() - getHeight() / 2));

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

	/**
	 * The shoot method generates a projectile in an attempt to destroy other
	 * circles.
	 */
	public void shoot()
	{
		// generate a new projectile in front of the circle traveling faster in
		// the same direction
		getWorld().generateObject(new Projectile(
				(int) (getPosition().getX() + getVelocity().normalize().getX() * getWidth() / 2) - getHeight() / 4,
				(int) (getPosition().getY() + getVelocity().normalize().getY() * getHeight() / 2) - getHeight() / 4,
				getVelocity(), this, getWorld()));
	}

}
