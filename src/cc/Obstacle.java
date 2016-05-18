package cc;

import java.awt.Color;
import java.awt.Graphics;

import lib.Vector2;

public class Obstacle extends Sprite
{
	public Obstacle(float x, float y, World world)
	{
		super(x, y, world);
		// TODO Auto-generated constructor stub
	}

	public Obstacle(int width, int height, Vector2 position, Color color, World world)
	{
		super(width, height, position, world);
	}

	public void paint(Graphics g)
	{
		g.setColor(getColor());
		g.fillRect((int) (getPosition().getX() - getWidth() / 2), (int) (getPosition().getY() - getHeight() / 2),
				getWidth(), getHeight());
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
