package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

public class Obstacle extends Sprite
{
	/**
	 * 5-Argument Obstacle Constructor
	 * @param width width
	 * @param height height
	 * @param position position
	 * @param color color
	 * @param world plane of existence
	 */
	public Obstacle(int width, int height, Vector2 position, Color color, World world)
	{
		super(width, height, position, new Vector2(0, 0), new Vector2(0, 0), color, world);
	}

	public void paint(Graphics g)
	{
		// @formatter:off
		// paint rectangle for obstacle
		g.setColor(getColor());
		g.fillRect(
				(int) (getPosition().getX() - getWidth() / 2),
				(int) (getPosition().getY() - getHeight() / 2),
				getWidth(), getHeight());
		// @formatter:on
	}

	@Override
	public void update()
	{
	}

	@Override
	public void collide(Sprite s)
	{
	}
}
