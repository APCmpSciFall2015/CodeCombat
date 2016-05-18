package cc;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import lib.Vector2;

public class World
{
	private ArrayList<Sprite> sprites;
	private Main main;
	
	public World(Main main)
	{
		this.main = main;
		sprites = new ArrayList<Sprite>();
		
		sprites.add(new Circle(400, 300, this));
	}

	public void update()
	{
		for (int i = sprites.size() - 1; i >= 0; i--)
		{
			if(sprites.get(i).isAlive()) sprites.get(i).update();
			else sprites.remove(i);
		}
		System.out.println(sprites.size());
		checkCollisions();
	}

	public void paint(Graphics g)
	{
		for (Sprite s : sprites)
		{
			s.paint(g);
		}
	}

	public void generateObject(Sprite s)
	{
		sprites.add(s);
	}
	
	public boolean checkCollisions()
	{
		boolean collisions = false;
		for (int i = 0; i < sprites.size() - 1; i++)
		{
			for (int j = i + 1; j < sprites.size(); j++)
			{
				if (colliding(sprites.get(i), sprites.get(j)))
				{
					collisions = true;
					sprites.get(i).collide(sprites.get(j));
					sprites.get(j).collide(sprites.get(i));
				}
			}
		}
		return collisions;
	}

	public boolean colliding(Sprite A, Sprite B)
	{
		return colliding(
				A.getPosition().getX() - A.getWidth() / 2, A.getPosition().getY() - A.getHeight() / 2,
				A.getPosition().getX() + A.getWidth() / 2, A.getPosition().getY() + A.getHeight() / 2,
				B.getPosition().getX() - B.getWidth() / 2, B.getPosition().getY() - B.getHeight() / 2,
				B.getPosition().getX() + B.getWidth() / 2, B.getPosition().getY() + B.getHeight() / 2);
	}

	public boolean colliding(float Ax, float Ay, float AX, float AY, float Bx, float By, float BX, float BY)
	{
		return !(AX < Bx || BX < Ax || AY < By || BY < Ay);
	}
	
	public int getHeight()
	{
		return main.getHeight();
	}
	
	public int getWidth()
	{
		return main.getWidth();
	}
}
