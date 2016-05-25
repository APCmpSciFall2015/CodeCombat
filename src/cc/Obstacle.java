package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

public class Obstacle extends Sprite
{
	/**
	 * Obstacle Copy Constructor
	 * @param o
	 */
	public Obstacle(Obstacle o)
	{
		super(o);
	}
	
	/**
	 * 5-Argument Obstacle Constructor
	 * @param width width
	 * @param height height
	 * @param position position
	 * @param color color
	 * @param world plane of existence
	 */
	public Obstacle(Vector2 size, Vector2 position, Color color, World world)
	{
		super(size, position, new Vector2(0, 0), new Vector2(0, 0), color, world);
	}

	
	// Overridden methods
	// --------------------------------------------
	
	@Override
	public void paint(Graphics g)
	{
		// @formatter:off
		// paint rectangle for obstacle
		g.setColor(getColor());
		g.fillRect(
				(int) (getPosition().getX() - getSize().getX() / 2),
				(int) (getPosition().getY() - getSize().getY() / 2),
				(int) getSize().getX(), (int) getSize().getY());
		// @formatter:on\
		
		if(Main.DEBUG) super.paint(g);
	}

	@Override
	public void update()
	{
	}

	@Override
	public void collide(Sprite s)
	{
	}
	
	@Override
	public Sprite copy()
	{
		return new Obstacle(this);
	}
}
