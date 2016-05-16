package cc;

import java.awt.Graphics;

import lib.Vector2;

public abstract class Sprite
{
	private Vector2 position;
	
	public Sprite()
	{
		this.position = new Vector2(0, 0);
	}
	
	public Sprite(float x, float y)
	{
		this.position = new Vector2(x, y);
	}
	
	public abstract void paint(Graphics g);
	
	public Vector2 getPositition()
	{
		return position.copy();
	}
	
	public void setPosition(Vector2 position)
	{
		this.position = position.copy();
	}
}
